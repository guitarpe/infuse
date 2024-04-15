package br.infuse.application.service.impl;

import br.infuse.application.dto.request.Order;
import br.infuse.application.dto.request.OrderRoot;
import br.infuse.application.enuns.MessageSystem;
import br.infuse.application.service.IFilesService;
import br.infuse.application.service.ILogsService;
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
public class FilesServiceImpl implements IFilesService {

    private final ILogsService logsService;

    @Override
    public List<Order> fileToEntity(MultipartFile file, String method) {
        try {
            if (checkDocumentType(file) == 1) {
                return convertJson(file);
            } else if (checkDocumentType(file) == 2) {
                return convertXml(file);
            }
        }catch (Exception ex){
            logsService.addLogExec(ex.getMessage(), method);
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

    @Override
    public int checkDocumentType(MultipartFile file){
        if(file.getContentType().equals("application/json")){
            return 1;
        }else if(file.getContentType().equals("application/xml")){
            return 2;
        }
        return 0;
    }

    private static List<Order> convertXml(MultipartFile file) throws Exception{
        List<Order> orders = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new ByteArrayInputStream(file.getBytes())));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("order");

            if(nodeList.getLength() < 1 || nodeList.getLength() > 10){
                throw new Exception(MessageSystem.ERROR_LIMIT_ORDERS.value());
            }

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    if (element.getElementsByTagName("client").getLength() == 0) {
                        throw new Exception(MessageSystem.ERROR_FIELD_CLIENT_EMPTY.value());
                    }

                    if (element.getElementsByTagName("control").getLength() == 0) {
                        throw new Exception(MessageSystem.ERROR_FIELD_CONTROL_EMPTY.value());
                    }

                    if (element.getElementsByTagName("product").getLength() == 0) {
                        throw new Exception(MessageSystem.ERROR_FIELD_NAME_EMPTY.value());
                    }

                    if (element.getElementsByTagName("value").getLength() == 0) {
                        throw new Exception(MessageSystem.ERROR_FIELD_VALUE_EMPTY.value());
                    }

                    String datRegister = Utils.pegarDataAtual();
                    int amountOrder = 1;

                    long clientId = Long.parseLong(element.getElementsByTagName("client").item(0).getTextContent());
                    long numControl = Long.parseLong(element.getElementsByTagName("control").item(0).getTextContent());
                    String nameProduct = element.getElementsByTagName("product").item(0).getTextContent();
                    double valOrder = Double.parseDouble(element.getElementsByTagName("value").item(0).getTextContent());

                    if (element.getElementsByTagName("amount").getLength() > 0) {
                        amountOrder = Integer.parseInt(element.getElementsByTagName("amount").item(0).getTextContent());
                    }

                    if (element.getElementsByTagName("register").getLength() > 0) {
                        datRegister = element.getElementsByTagName("register").item(0).getTextContent();
                    }

                    orders.add(Order.builder()
                            .amountOrder(amountOrder)
                            .clientId(clientId)
                            .numControl(numControl)
                            .vlProduct(valOrder)
                            .nmProduct(nameProduct)
                            .dtRegister(datRegister).build());
                }
            }
        } catch (Exception ex) {
            throw new Exception(MessageSystem.ERROR_READ_XML_FILE.value() + ex.getMessage());
        }

        return orders;
    }

    public List<Order> convertJson(MultipartFile file) throws Exception {
        OrderRoot orderRoot;

        try {
            if (file == null) {
                throw new IllegalArgumentException(MessageSystem.ERROR_FILE_JSON_NULL.value());
            }

            if (file.isEmpty()) {
                throw new IllegalArgumentException(MessageSystem.ERROR_FILE_JSON_EMPTY.value());
            }

            byte[] bytes = file.getBytes();
            String jsonString = new String(bytes);

            ObjectMapper objectMapper = new ObjectMapper();
            orderRoot = objectMapper.readValue(jsonString, OrderRoot.class);

            if (orderRoot.getOrdersList().getOrder().size() < 1 || orderRoot.getOrdersList().getOrder().size() > 10) {
                throw new IllegalArgumentException(MessageSystem.ERROR_LIMIT_ORDERS.value());
            }
        } catch (Exception ex) {
            throw new IOException(MessageSystem.ERROR_READ_JSON_FILE.value(), ex);
        }

        return validateOrders(orderRoot.getOrdersList().getOrder());
    }

    private List<Order> validateOrders(List<Order> orders) throws Exception {

        List<Order> ordersVerify = new ArrayList<>();

        for (Order order : orders) {
            if (order.getClientId() == null) {
                throw new Exception(MessageSystem.ERROR_FIELD_CLIENT_EMPTY.value());
            }

            if (order.getNumControl() == null) {
                throw new Exception(MessageSystem.ERROR_FIELD_CONTROL_EMPTY.value());
            }

            if (order.getNmProduct().isEmpty()) {
                throw new Exception(MessageSystem.ERROR_FIELD_NAME_EMPTY.value());
            }

            if (Objects.isNull(order.getVlProduct())) {
                throw new Exception(MessageSystem.ERROR_FIELD_VALUE_EMPTY.value());
            }

            if (order.getAmountOrder() == null || order.getAmountOrder() == 0) {
                order.setAmountOrder(1);
            }

            if (order.getDtRegister() == null) {
                order.setDtRegister(Utils.pegarDataAtual());
            }

            ordersVerify.add(order);
        }

        return ordersVerify;
    }

}