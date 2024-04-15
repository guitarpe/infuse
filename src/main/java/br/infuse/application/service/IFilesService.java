package br.infuse.application.service;

import br.infuse.application.dto.request.Order;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFilesService {
    List<Order> fileToEntity(MultipartFile file, String method) throws Exception;
    int checkDocumentType(MultipartFile file);
}
