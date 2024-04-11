package br.infuse.application.service.impl;

import br.infuse.application.repository.ILogsRepository;
import br.infuse.application.service.ILogsService;
import br.infuse.application.model.Logs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LogsServiceImpl implements ILogsService {

    @Value("${aplicacao.nome}")
    private String servico;

    private final ILogsRepository iLogsRepository;

    public void addLogExec(String error, String method) {

        iLogsRepository.save(Logs.builder()
                .nmMethod(method)
                .dtRegister(new Date())
                .nmService(servico)
                .logExecute(error).build());
    }
}
