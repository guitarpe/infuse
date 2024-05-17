package br.infuse.application.repository;

import br.infuse.application.model.PedidosProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IPedidosProcedureRepository extends JpaRepository<PedidosProcedure, Long> {

    @Procedure(name = "OrdersProcedure.ProcRegisterOrders")
    void procRegisterOrders(@Param("CLIENT") long client,
                            @Param("CONTROL") long control,
                            @Param("PRODUCT") String product,
                            @Param("V_PRODUCT") double value,
                            @Param("AMOUNT") int amount,
                            @Param("REGISTER") LocalDateTime register);
}