package br.infuse.application.service;

import br.infuse.application.dto.request.OrderDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFilesService {
    List<OrderDTO> fileToEntity(MultipartFile file, String method) throws Exception;
}
