package br.infuse.application.service;

import br.infuse.application.dto.request.PedidoDTO;
import br.infuse.application.dto.request.PedidosDTO;
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
import org.xml.sax.SAXException;

import javax.persistence.EntityNotFoundException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArquivosService {

    public List<PedidoDTO> converterParaEntidade(MultipartFile file) {
        try {
            int tipoDocumento = checkTipoDocument(file);
            if (tipoDocumento == 1) {
                return converterJson(file);
            } else if (tipoDocumento == 2) {
                return converterXML(file);
            }
        } catch (IOException ex) {
            log.error(Mensagens.FILE_ERROR_PROCCESS.value(), ex.getMessage());
        }
        return Collections.emptyList();
    }

    public int checkTipoDocument(MultipartFile file) {
        if (Objects.equals(file.getContentType(), "application/json")) {
            return 1;
        } else if (Objects.equals(file.getContentType(), "application/xml")) {
            return 2;
        }
        return 0;
    }

    private List<PedidoDTO> converterXML(MultipartFile file) throws IOException {
        List<PedidoDTO> pedidoDTOS = new ArrayList<>();
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

                    validarCamposPedido(element);

                    PedidoDTO pedidoDTO = lerPedido(element);
                    pedidoDTOS.add(pedidoDTO);
                }
            }
        } catch (ParserConfigurationException | SAXException ex) {
            throw new IOException(Mensagens.FILE_ERROR_READ_XML.value() + ex.getMessage(), ex);
        }

        return pedidoDTOS;
    }

    private void validarNumeroDePedidos(NodeList nodeList) {
        if (nodeList.getLength() < 1 || nodeList.getLength() > 10) {
            throw new EntityNotFoundException(Mensagens.FILE_ERROR_N_ORDERS_INVALID.value());
        }
    }

    private void validarCamposPedido(Element element) {
        if (element.getElementsByTagName("cliente").getLength() == 0 ||
                element.getElementsByTagName("controle").getLength() == 0 ||
                element.getElementsByTagName("produto").getLength() == 0 ||
                element.getElementsByTagName("valor").getLength() == 0) {
            throw new EntityNotFoundException(Mensagens.FILE_ERROR_INVALID_FIELDS.value());
        }
    }

    private PedidoDTO lerPedido(Element element) {
        String datRegister = Utils.pegarDataAtual();
        int amountOrder = 1;

        long clientId = Long.parseLong(element.getElementsByTagName("cliente").item(0).getTextContent());
        long numControl = Long.parseLong(element.getElementsByTagName("controle").item(0).getTextContent());
        String nameProduct = element.getElementsByTagName("produto").item(0).getTextContent();
        BigDecimal valOrder = new BigDecimal(element.getElementsByTagName("valor").item(0).getTextContent());

        if (element.getElementsByTagName("quantidade").getLength() > 0) {
            amountOrder = Integer.parseInt(element.getElementsByTagName("quantidade").item(0).getTextContent());
        }

        if (element.getElementsByTagName("registro").getLength() > 0) {
            if(!element.getElementsByTagName("registro").item(0).getTextContent().isEmpty())
                datRegister = element.getElementsByTagName("registro").item(0).getTextContent();
        }

        return PedidoDTO.builder()
                .quantidade(amountOrder)
                .clientId(clientId)
                .controle(numControl)
                .valor(valOrder)
                .produto(nameProduct)
                .dtRegistro(datRegister).build();
    }

    public List<PedidoDTO> converterJson(MultipartFile file) throws IOException {
        PedidosDTO lista;

        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException(Mensagens.FILE_ERROR_JSON_EMPTY.value());
            }

            byte[] bytes = file.getBytes();
            String jsonString = new String(bytes);

            ObjectMapper objectMapper = new ObjectMapper();
            lista = objectMapper.readValue(jsonString, PedidosDTO.class);

            if (lista.getPedidos().size() < 1 || lista.getPedidos().size() > 10) {
                throw new IllegalArgumentException(Mensagens.FILE_ERROR_AMOUNT_ORD_INVALID.value());
            }
        } catch (IOException ex) {
            throw new IOException(Mensagens.FILE_ERROR_READ_JSON_FILE.value() + ex.getMessage(), ex);
        }

        return validarPedidos(lista.getPedidos());
    }

    private List<PedidoDTO> validarPedidos(List<PedidoDTO> pedidoDTOS) {
        List<PedidoDTO> ordersVerify = new ArrayList<>();

        for (PedidoDTO pedidoDTO : pedidoDTOS) {
            if (pedidoDTO.getClientId() == null || pedidoDTO.getControle() == null || pedidoDTO.getProduto().isEmpty()) {
                throw new EntityNotFoundException(Mensagens.FILE_ERROR_FIELD_ORD_INVALID.value());
            }

            if (pedidoDTO.getQuantidade() == null || pedidoDTO.getQuantidade() == 0) {
                pedidoDTO.setQuantidade(1);
            }

            if (pedidoDTO.getDtRegistro() == null) {
                pedidoDTO.setDtRegistro(Utils.pegarDataAtual());
            }

            ordersVerify.add(pedidoDTO);
        }

        return ordersVerify;
    }
}
