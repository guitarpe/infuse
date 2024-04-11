package br.infuse.application.repository;

import br.infuse.application.model.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILogsRepository extends JpaRepository<Logs, Long> {
	
}