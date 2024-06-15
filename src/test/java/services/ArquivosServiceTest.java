package services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Collections;

import br.infuse.application.dto.request.PedidoDTO;
import br.infuse.application.service.ArquivosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ArquivosServiceTest {

    @Mock
    private MultipartFile file;

    @InjectMocks
    private ArquivosService arquivosService;

    @BeforeEach
    void setUp() {
        arquivosService = new ArquivosService();
    }

    @Test
    void ArquivosServices_ConverterParaEntidade_Json() throws IOException {

        ClassPathResource resource = new ClassPathResource("pedidos.json");
        String jsonContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "pedido.json",
                MediaType.APPLICATION_JSON_VALUE, jsonContent.getBytes());

        List<PedidoDTO> result = arquivosService.converterParaEntidade(file);

        assertEquals(2, result.size());
        assertEquals(3L, result.get(0).getClientId());
    }

    @Test
    void ArquivosServices_ConverterParaEntidade_Xml() throws Exception {

        ClassPathResource resource = new ClassPathResource("pedidos.xml");
        String jsonContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "pedido.xml",
                MediaType.APPLICATION_XML_VALUE, jsonContent.getBytes());

        List<PedidoDTO> result = arquivosService.converterParaEntidade(file);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getClientId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"application/json", "application/xml"})
    void checkTipoDocument_ValidTypes(String contentType) {
        when(file.getContentType()).thenReturn(contentType);

        int tipo = arquivosService.checkTipoDocument(file);

        if ("application/json".equals(contentType)) {
            assertEquals(1, tipo);
        } else if ("application/xml".equals(contentType)) {
            assertEquals(2, tipo);
        }
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"application/pdf", "image/jpeg", "text/plain"})
    void checkTipoDocument_InvalidTypes(String contentType) {
        when(file.getContentType()).thenReturn(contentType);

        int tipo = arquivosService.checkTipoDocument(file);

        assertEquals(0, tipo);
    }

    @Test
    void ArquivosServices_ConverterParaEntidade_InvalidFileType() {
        when(file.getContentType()).thenReturn("application/pdf");

        List<PedidoDTO> result = arquivosService.converterParaEntidade(file);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void ArquivosServices_ConverterParaEntidade_IOException() throws IOException {
        when(file.getContentType()).thenReturn("application/json");
        when(file.getBytes()).thenThrow(new IOException("Test IOException"));

        List<PedidoDTO> result = arquivosService.converterParaEntidade(file);

        assertEquals(Collections.emptyList(), result);
    }
}
