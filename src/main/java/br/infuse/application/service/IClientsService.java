package br.infuse.application.service;

import br.infuse.application.dto.request.ClientDTO;
import br.infuse.application.dto.response.ServiceResponse;

public interface IClientsService {
    ServiceResponse saveClient(ClientDTO client, String methodName) throws Exception;
    ServiceResponse searchClient(ClientDTO client, String methodName) throws Exception;
}
