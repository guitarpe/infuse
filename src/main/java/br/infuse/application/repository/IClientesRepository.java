package br.infuse.application.repository;

import br.infuse.application.model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClientesRepository extends JpaRepository<Clientes, Long> {
	Optional<Clientes> findByNomeCliente(String name);

	@Query("select c from Clientes c where c.id = :id or c.nomeCliente like %:nome%")
	List<Clientes> searchClients(@Param("id") Long id,
                                 @Param("nome") String nome);
}