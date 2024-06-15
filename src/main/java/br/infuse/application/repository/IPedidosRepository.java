package br.infuse.application.repository;

import br.infuse.application.model.Pedidos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface IPedidosRepository extends JpaRepository<Pedidos, Long> {

	int countByControle(Long controle);

	Optional<Pedidos> findByControle(Long controle);

	@Query("select o from Pedidos o where (o.controle = :pedido and o.dtRegistro = :data) " +
			"or (o.quantidade = :qtde or o.vlProduto = :valor or o.nomeProduto like %:produto% or o.cliente = :cliente)")
	Page<Pedidos> searchOrders(@Param("pedido") Long pedido,
							   @Param("data") LocalDate data,
							   @Param("qtde") Integer qtde,
							   @Param("valor") BigDecimal valor,
							   @Param("produto") String produto,
							   @Param("cliente") Long cliente, Pageable pageable);

}