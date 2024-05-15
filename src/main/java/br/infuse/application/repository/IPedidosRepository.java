package br.infuse.application.repository;

import br.infuse.application.model.Pedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPedidosRepository extends JpaRepository<Pedidos, Long> {
	Optional<Pedidos> findByNumControl(Long pedido);

	@Query("select o from Pedidos o where o.numControl=:pedido or o.dtRegister=:data or o.amountOrder=:qtde " +
			"or o.vlProduct=:valor or o.nmProduct like %:produto% or o.clientId=:cliente")
	List<Pedidos> searchOrders(@Param("pedido") Long pedido,
                               @Param("data") LocalDate data,
                               @Param("qtde") Integer qtde,
                               @Param("valor") Double valor,
                               @Param("produto") String produto,
                               @Param("cliente") Long cliente);
}