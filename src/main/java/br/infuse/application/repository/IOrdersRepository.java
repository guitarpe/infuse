package br.infuse.application.repository;

import br.infuse.application.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IOrdersRepository extends JpaRepository<Orders, Long> {
	Optional<Orders> findByNumControl(Long numControl);

	@Query("select o from Orders o where o.numControl=:control or o.dtRegister=:register " +
			"or o.amountOrder=:amount or o.vlProduct=:vproduct or o.nmProduct=:nproduct or o.clientId=:client")
	List<Orders> searchOrders(@Param("control") Long numControl,
							   @Param("register") LocalDateTime dtRegister,
							   @Param("amount") Integer amountOrder,
							   @Param("vproduct") Double vlProduct,
							   @Param("nproduct") String nmProduct,
							   @Param("client") Long clientId);
}