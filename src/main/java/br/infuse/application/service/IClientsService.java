package br.infuse.application.service;

import br.infuse.application.dto.request.Client;
import br.infuse.application.dto.request.ClientsList;
import br.infuse.application.dto.response.ServiceResponse;

public interface IClientsService {
    ServiceResponse saveClient(ClientsList clients, String methodName) throws Exception;
    ServiceResponse searchClient(Client client, String methodName) throws Exception;
}
