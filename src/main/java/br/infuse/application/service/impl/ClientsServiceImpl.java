package br.infuse.application.service.impl;

import br.infuse.application.dto.request.ClientDTO;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.MessageSystem;
import br.infuse.application.model.Clients;
import br.infuse.application.repository.IClientsRepository;
import br.infuse.application.service.IClientsService;
import br.infuse.application.service.ILogsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientsServiceImpl implements IClientsService {

    private final IClientsRepository clientsRepository;
    private final ILogsService logsService;

    @Override
    public ServiceResponse saveClient(ClientDTO client, String method) throws Exception {
        try{
            if (!Objects.isNull(clientsRepository.findByNmClient(client.getNmClient())))
                throw new Exception(MessageSystem.ERROR_CLIENT_EXISTS.value());

            clientsRepository.save(Clients.builder()
                    .nmClient(client.getNmClient())
                    .dtRegister(LocalDateTime.now())
                    .build());

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_SAVE_CLIENT.value())
                    .data(null).build();

        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_SAVE_CLIENT.value()+ex.getMessage();
            logsService.addLogExec(erroMsg, method);
            throw new Exception(erroMsg);
        }
    }

    @Override
    public ServiceResponse searchClient(ClientDTO client, String method) throws Exception {
        try{
            List<Clients> clients = clientsRepository.searchClients(client.getId(), client.getNmClient());

            if(Objects.isNull(clients))
                throw new Exception(MessageSystem.ERROR_LIST_NOT_FOUND.value());

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_LIST_CLIENTS.value())
                    .data(clients).build();
        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_LIST_CLIENTS.value()+ex.getMessage();
            logsService.addLogExec(erroMsg, method);
            throw new Exception(erroMsg);
        }
    }
}
