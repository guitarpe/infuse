import br.infuse.application.controller.OrdersController;
import br.infuse.application.dto.request.ClientDTO;
import br.infuse.application.dto.request.OrderDTO;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.service.IClientsService;
import br.infuse.application.service.IFilesService;
import br.infuse.application.service.IOrdersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.http.HttpHeaders;

import javax.persistence.EntityNotFoundException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrdersControllerTest {
    @Mock
    private IOrdersService orderService;

    @Mock
    private IClientsService clientsService;

    @Mock
    private IFilesService filesService;

    @InjectMocks
    private OrdersController ordersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveXML() throws Exception {
        Resource resource = new ClassPathResource("orders.xml");
        InputStream inputStream = resource.getInputStream();
        MockMultipartFile file = new MockMultipartFile("file", "orders.xml", "application/xml", inputStream);

        List<OrderDTO> orders = filesService.fileToEntity(file, "testReceiveXML");

        when(filesService.fileToEntity(file, "receive")).thenReturn(orders);
        when(orderService.saveOrder(any(), any())).thenReturn(
                ServiceResponse.builder().status(true)
                .message("Success")
                .data(null).build());

        ResponseEntity<SuccessResponse> responseEntity = ordersController.receive(file, new HttpHeaders());

        assertEquals(200, responseEntity.getBody().getCode());
        assertEquals(true, responseEntity.getBody().isSucesso());
        assertEquals("Success", responseEntity.getBody().getMessage());
    }

    @Test
    void testReceiveJSON() throws Exception {
        Resource resource = new ClassPathResource("orders.json");
        InputStream inputStream = resource.getInputStream();
        MockMultipartFile file = new MockMultipartFile("file", "orders.json", "application/json", inputStream);

        List<OrderDTO> orders = filesService.fileToEntity(file, "testReceiveJSON");

        when(filesService.fileToEntity(file, "receive")).thenReturn(orders);
        when(orderService.saveOrder(any(), any())).thenReturn(
                ServiceResponse.builder().status(true)
                        .message("Success")
                        .data(null).build());

        ResponseEntity<SuccessResponse> responseEntity = ordersController.receive(file, new HttpHeaders());

        assertEquals(200, responseEntity.getBody().getCode());
        assertEquals(true, responseEntity.getBody().isSucesso());
        assertEquals("Success", responseEntity.getBody().getMessage());
    }

    @Test
    void testAllClients_Success() throws Exception {
        ClientDTO client = ClientDTO.builder().build();
        HttpHeaders headers = new HttpHeaders();
        when(clientsService.searchClient(any(ClientDTO.class), anyString())).thenReturn(
                ServiceResponse.builder().status(true).message("Success").build());

        ResponseEntity<SuccessResponse> responseEntity = ordersController.allClients(client, headers);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getBody().getCode());
        assertTrue(responseEntity.getBody().isSucesso());
        assertNotNull(responseEntity.getBody().getTimestamp());
        assertEquals("Success", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
        verify(clientsService, times(1)).searchClient(any(ClientDTO.class), anyString());
    }

    @Test
    void testAllClients_EntityNotFoundException() throws Exception {
        ClientDTO client = ClientDTO.builder().build();
        HttpHeaders headers = new HttpHeaders();
        when(clientsService.searchClient(any(ClientDTO.class), anyString())).thenReturn(
                ServiceResponse.builder().status(false).message("Client not found").build());

        assertThrows(EntityNotFoundException.class, () -> ordersController.allClients(client, headers));
        verify(clientsService, times(1)).searchClient(any(ClientDTO.class), anyString());
    }
}