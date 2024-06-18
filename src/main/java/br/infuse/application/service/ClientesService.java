package br.infuse.application.service;

import br.infuse.application.dto.request.ClienteDTO;
import br.infuse.application.dto.response.PagesRespose;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.exception.NotFoundException;
import br.infuse.application.model.Clientes;
import br.infuse.application.repository.IClientesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientesService {

    private final IClientesRepository repository;

    public ServiceResponse cadastrarClientes(ClienteDTO objeto) {
        String mensagem = Mensagens.CLIENT_SUCCESS_SAVE.value();
        List<ClienteDTO> clientes = new ArrayList<>();
        boolean status = true;

        try {
            Clientes cliente = new Clientes();
            cliente.setNomeCliente(objeto.getNome());
            cliente.setEmailClient(objeto.getEmail());
            cliente.setPhoneClient(objeto.getTelefone());
            cliente.setDtRegistro(LocalDateTime.now());

            repository.save(cliente);

            clientes.add(objeto);

        } catch (Exception ex) {
            status = false;
            mensagem = Mensagens.CLIENT_ERROR_SAVE.value() + ex.getMessage();
        }

        return ServiceResponse.builder()
                .status(status)
                .mensagem(mensagem)
                .dados(clientes).build();
    }

    public ServiceResponse consultarClientePorId(Long id) throws NotFoundException {
        String mensagem = Mensagens.CLIENT_SUCCESS_FOUND.value();
        List<ClienteDTO> clientes = new ArrayList<>();

        try {
            Clientes cliente = repository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Mensagens.NO_RESULTS.value()));

            clientes.add(mapToDto(cliente));
        } catch (Exception ex) {
            throw ex;
        }

        return ServiceResponse.builder()
                .status(true)
                .mensagem(mensagem)
                .dados(clientes).build();
    }

    public ServiceResponse consultarClientes(int page, int size) throws NotFoundException {

        List<ClienteDTO> content;

        String mensagem = Mensagens.CLIENT_SUCCESS_LIST.value();

        PagesRespose clientesResponse = new PagesRespose();

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Clientes> clientes = repository.findAll(pageable);
            List<Clientes> listOfClientes = clientes.getContent();
            content = listOfClientes.stream().map(this::mapToDto).collect(Collectors.toList());

            if (content.isEmpty())
                throw new NotFoundException(Mensagens.NO_RESULTS.value());

            clientesResponse.setContent(content);
            clientesResponse.setPageNo(clientes.getNumber());
            clientesResponse.setPageSize(clientes.getSize());
            clientesResponse.setTotalElements(clientes.getTotalElements());
            clientesResponse.setTotalPages(clientes.getTotalPages());
            clientesResponse.setLast(clientes.isLast());

        } catch (Exception ex) {
            throw ex;
        }

        return ServiceResponse.builder()
                .status(true)
                .mensagem(mensagem)
                .dados(clientesResponse).build();
    }

    public ServiceResponse atualizarClientes(ClienteDTO objeto, Long id) throws NotFoundException {
        String mensagem = Mensagens.CLIENT_SUCCESS_UPDT.value();
        List<ClienteDTO> clientes = new ArrayList<>();

        try {
            Clientes cliente = repository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Mensagens.NO_RESULTS.value()));

            cliente.setNomeCliente(objeto.getNome());
            cliente.setEmailClient(objeto.getEmail());
            cliente.setPhoneClient(objeto.getTelefone());
            cliente.setDtUpdate(LocalDateTime.now());

            Clientes clienteUpdt = repository.save(cliente);

            clientes.add(mapToDto(clienteUpdt));

        } catch (Exception ex) {
            throw ex;
        }

        return ServiceResponse.builder()
                .status(true)
                .mensagem(mensagem)
                .dados(clientes).build();
    }

    public ServiceResponse deletarClientes(Long id) throws NotFoundException {
        String mensagem = Mensagens.CLIENT_SUCCESS_DEL.value();

        try {
            Clientes consulta = repository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Mensagens.NO_RESULTS.value()));

            repository.delete(consulta);
        } catch (Exception ex) {
            throw ex;
        }

        return ServiceResponse.builder()
                .status(true)
                .mensagem(mensagem)
                .dados(null).build();
    }

    private ClienteDTO mapToDto(Clientes cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNomeCliente())
                .email(cliente.getEmailClient())
                .telefone(cliente.getPhoneClient()).build();
    }
}

