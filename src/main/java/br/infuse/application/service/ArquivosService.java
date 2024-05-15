package br.infuse.application.service;

import br.infuse.application.dto.request.Pedido;
import br.infuse.application.dto.request.Pedidos;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.persistence.EntityNotFoundException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArquivosService{

    public List<Pedido> arquivoToEntidade(MultipartFile file) {
        try {
            if (checkTipoDocument(file) == 1) {
                return converterJson(file);
            } else if (checkTipoDocument(file) == 2) {
                return converterXML(file);
            }
        }catch (Exception ex){
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

    public int checkTipoDocument(MultipartFile file){
        if(Objects.equals(file.getContentType(), "application/json")){
            return 1;
        }else if(Objects.equals(file.getContentType(),"application/xml")){
            return 2;
        }
        return 0;
    }

    private static List<Pedido> converterXML(MultipartFile file) {
        List<Pedido> pedidos = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new ByteArrayInputStream(file.getBytes())));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("pedido");

            validarNumeroDePedidos(nodeList);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    validarElementoPedido(element);

                    Pedido pedido = lerPedido(element);
                    pedidos.add(pedido);
                }
            }
        } catch (Exception ex) {
            throw new EntityNotFoundException(Mensagens.ERROR_READ_XML_FILE.value() + ex.getMessage());
        }

        return pedidos;
    }

    private static void validarNumeroDePedidos(NodeList nodeList) {
        if (nodeList.getLength() < 1 || nodeList.getLength() > 10) {
            throw new EntityNotFoundException(Mensagens.ERROR_LIMIT_ORDERS.value());
        }
    }

    private static void validarElementoPedido(Element element) {
        if (element.getElementsByTagName("cliente").getLength() == 0 ||
                element.getElementsByTagName("controle").getLength() == 0 ||
                element.getElementsByTagName("produto").getLength() == 0 ||
                element.getElementsByTagName("valor").getLength() == 0) {
            throw new EntityNotFoundException(Mensagens.ERROR_FIELD_VALUE_EMPTY.value());
        }
    }

    private static Pedido lerPedido(Element element) {
        String datRegister = Utils.pegarDataAtual();
        int amountOrder = 1;

        long clientId = Long.parseLong(element.getElementsByTagName("cliente").item(0).getTextContent());
        long numControl = Long.parseLong(element.getElementsByTagName("controle").item(0).getTextContent());
        String nameProduct = element.getElementsByTagName("produto").item(0).getTextContent();
        double valOrder = Double.parseDouble(element.getElementsByTagName("valor").item(0).getTextContent());

        if (element.getElementsByTagName("quantidade").getLength() > 0) {
            amountOrder = Integer.parseInt(element.getElementsByTagName("quantidade").item(0).getTextContent());
        }

        if (element.getElementsByTagName("registro").getLength() > 0) {
            datRegister = element.getElementsByTagName("registro").item(0).getTextContent();
        }

        return Pedido.builder()
                .quantidade(amountOrder)
                .clientId(clientId)
                .controle(numControl)
                .valor(valOrder)
                .produto(nameProduct)
                .dtRegistro(datRegister).build();
    }


    public List<Pedido> converterJson(MultipartFile file) throws Exception {
        Pedidos lista;

        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException(Mensagens.ERROR_FILE_JSON_EMPTY.value());
            }

            byte[] bytes = file.getBytes();
            String jsonString = new String(bytes);

            ObjectMapper objectMapper = new ObjectMapper();
            lista = objectMapper.readValue(jsonString, Pedidos.class);

            if (lista.getPedidos().size() < 1 || lista.getPedidos().size() > 10) {
                throw new IllegalArgumentException(Mensagens.ERROR_LIMIT_ORDERS.value());
            }
        } catch (Exception ex) {
            throw new IOException(Mensagens.ERROR_READ_JSON_FILE.value(), ex);
        }

        return validarPedidos(lista.getPedidos());
    }

    private List<Pedido> validarPedidos(List<Pedido> pedidos) {

        List<Pedido> ordersVerify = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getClientId() == null || pedido.getControle() == null || pedido.getProduto().isEmpty()) {
                throw new EntityNotFoundException(Mensagens.ERROR_FIELD_NAME_EMPTY.value());
            }

            if (pedido.getQuantidade() == null || pedido.getQuantidade() == 0) {
                pedido.setQuantidade(1);
            }

            if (pedido.getDtRegistro() == null) {
                pedido.setDtRegistro(Utils.pegarDataAtual());
            }

            ordersVerify.add(pedido);
        }

        return ordersVerify;
    }
}