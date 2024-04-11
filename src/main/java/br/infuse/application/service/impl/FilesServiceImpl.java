package br.infuse.application.service.impl;

import br.infuse.application.dto.request.OrderDTO;
import br.infuse.application.enuns.MessageSystem;
import br.infuse.application.service.IFilesService;
import br.infuse.application.service.ILogsService;
import com.fasterxml.jackson.core.type.TypeReference;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilesServiceImpl implements IFilesService {

    private final ILogsService logsService;

    @Override
    public List<OrderDTO> fileToEntity(MultipartFile file, String method) {
        try {
            if (checkDocumentType(file) == 1) {
                return convertJson(file);
            } else if (checkDocumentType(file) == 2) {
                return convertXml(file);
            } else {
                return null;
            }
        }catch (Exception ex){
            logsService.addLogExec(ex.getMessage(), method);
            return null;
        }
    }

    private int checkDocumentType(MultipartFile file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file.getInputStream());
            return 2;
        } catch (Exception e) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.readTree(file.getInputStream());
                return 1;
            } catch (Exception ex) {
                String erroMsg = MessageSystem.ERROR_INVALID_FILE.value() + e.getMessage();
                throw new RuntimeException(erroMsg);
            }
        }
    }

    private static List<OrderDTO> convertXml(MultipartFile file) {
        List<OrderDTO> orders = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = file.getInputStream();
            Document doc = builder.parse(inputStream);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("pedido");

            if(nodeList.getLength() < 1 || nodeList.getLength() > 10){
                throw new Exception(MessageSystem.ERROR_LIMIT_ORDERS.value());
            }

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    if (element.getElementsByTagName("cliente").getLength() == 0) {
                        throw new Exception(MessageSystem.ERROR_FIELD_CLIENT_EMPTY.value());
                    }

                    if (element.getElementsByTagName("controle").getLength() == 0) {
                        throw new Exception(MessageSystem.ERROR_FIELD_CONTROL_EMPTY.value());
                    }

                    if (element.getElementsByTagName("produto").getLength() == 0) {
                        throw new Exception(MessageSystem.ERROR_FIELD_NAME_EMPTY.value());
                    }

                    if (element.getElementsByTagName("valor").getLength() == 0) {
                        throw new Exception(MessageSystem.ERROR_FIELD_VALUE_EMPTY.value());
                    }

                    LocalDateTime datRegister = LocalDateTime.now();
                    int amountOrder = 1;

                    long clientId = Long.parseLong(element.getElementsByTagName("cliente").item(0).getTextContent());
                    long numControl = Long.parseLong(element.getElementsByTagName("controle").item(0).getTextContent());
                    String nameProduct = element.getElementsByTagName("produto").item(0).getTextContent();
                    long valOrder = Long.parseLong(element.getElementsByTagName("valor").item(0).getTextContent());

                    if (element.getElementsByTagName("quantidade").getLength() > 0) {
                        amountOrder = Integer.parseInt(element.getElementsByTagName("quantidade").item(0).getTextContent());
                    }

                    if (element.getElementsByTagName("cadastro").getLength() > 0) {
                        datRegister = convertToDate(element.getElementsByTagName("cadastro").item(0).getTextContent());
                    }

                    orders.add(OrderDTO.builder()
                            .clientId(clientId)
                            .numControl(numControl)
                            .nmProduct(nameProduct)
                            .vlProduct(valOrder)
                            .amountOrder(amountOrder).dtRegister(datRegister).build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    private List<OrderDTO> convertJson(MultipartFile file) throws Exception {
        ObjectMapper objectMapper = null;
        List<OrderDTO> orders = new ArrayList<>();

        try {
            assert objectMapper != null;
            orders = objectMapper.readValue(file.getInputStream(), new TypeReference<List<OrderDTO>>() {});

            if(orders.size() < 1 && orders.size() > 10){
                throw new Exception(MessageSystem.ERROR_LIMIT_ORDERS.value());
            }
        } catch (IOException e) {
            throw new IOException(MessageSystem.ERROR_READ_JSON_FILE.value(), e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        orders = validateOrders(orders);

        return orders;
    }

    private List<OrderDTO> validateOrders(List<OrderDTO> orders) throws Exception {

        List<OrderDTO> ordersVerify = new ArrayList<>();

        for (OrderDTO order : orders) {
            if (order.getClientId() == null) {
                throw new Exception(MessageSystem.ERROR_FIELD_CLIENT_EMPTY.value());
            }

            if (order.getNumControl() == null) {
                throw new Exception(MessageSystem.ERROR_FIELD_CONTROL_EMPTY.value());
            }

            if (order.getNmProduct().isEmpty()) {
                throw new Exception(MessageSystem.ERROR_FIELD_NAME_EMPTY.value());
            }

            if (order.getVlProduct() == null) {
                throw new Exception(MessageSystem.ERROR_FIELD_VALUE_EMPTY.value());
            }

            if (order.getAmountOrder() == null || order.getAmountOrder() == 0) {
                order.setAmountOrder(1);
            }

            if (order.getDtRegister() == null) {
                order.setDtRegister(LocalDateTime.now());
            }

            ordersVerify.add(order);
        }

        return ordersVerify;
    }

    private static LocalDateTime convertToDate(String strDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(strDate, formatter);
    }
}
