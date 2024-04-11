package br.infuse.application.repository;

import br.infuse.application.model.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClientsRepository extends JpaRepository<Clients, Long> {
	Clients findByClientId(Long clientId);
	Clients findByNmClient(String name);
	@Query("select c from Clients c where c.clientId=:id or c.nmClient=:name")
	List<Clients> searchClients(@Param("id") Long id,
							   @Param("name") String name);
}