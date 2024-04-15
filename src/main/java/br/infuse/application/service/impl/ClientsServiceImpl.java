package br.infuse.application.service.impl;

import br.infuse.application.dto.request.Client;
import br.infuse.application.dto.request.ClientsList;
import br.infuse.application.dto.response.DataResponse;
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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientsServiceImpl implements IClientsService {

    private final IClientsRepository clientsRepository;
    private final ILogsService logsService;

    @Override
    public ServiceResponse saveClient(ClientsList clients, String method) {
        try{
            String retMessage = MessageSystem.SUCCESS_SAVE_CLIENT.value();
            DataResponse retData = new DataResponse();
            List<Long> notInsert = new ArrayList<>();

            clients.getClients().forEach(client -> {
                Optional<Clients> getclient = clientsRepository.findByNmClient(client.getNmClient());

                if (!getclient.isPresent()) {
                    clientsRepository.save(Clients.builder()
                            .nmClient(client.getNmClient())
                            .dtRegister(LocalDateTime.now())
                            .build());

                    notInsert.add(client.getId());
                }
            });

            if(notInsert.isEmpty()){
                retMessage = MessageSystem.ERROR_NOT_CLIENT_SAVE.value();
            }

            return ServiceResponse.builder()
                    .status(true)
                    .message(retMessage)
                    .data(retData).build();

        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_SAVE_CLIENT.value()+ex.getMessage();
            logsService.addLogExec(erroMsg, method);
            return ServiceResponse.builder()
                    .status(false)
                    .message(erroMsg)
                    .data(null).build();
        }
    }

    @Override
    public ServiceResponse searchClient(Client client, String method) {
        try{
            List<Clients> clients = clientsRepository.searchClients(client.getId(), client.getNmClient());

            if(clients.isEmpty())
                throw new EntityNotFoundException(MessageSystem.ERROR_LIST_NOT_FOUND.value());

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_LIST_CLIENTS.value())
                    .data(clients).build();
        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_LIST_CLIENTS.value()+ex.getMessage();
            logsService.addLogExec(erroMsg, method);
            return ServiceResponse.builder()
                    .status(false)
                    .message(erroMsg)
                    .data(null).build();
        }
    }
}
