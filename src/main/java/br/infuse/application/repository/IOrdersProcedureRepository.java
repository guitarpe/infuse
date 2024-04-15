package br.infuse.application.repository;

import br.infuse.application.model.OrdersProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface IOrdersProcedureRepository extends JpaRepository<OrdersProcedure, Long> {

    @Procedure(name = "OrdersProcedure.ProcRegisterOrders")
    void procRegisterOrders(@Param("CLIENT") long client,
                            @Param("NUM_CONTROL") long control,
                            @Param("NM_PRODUCT") String product,
                            @Param("VL_PRODUCT") double value,
                            @Param("AMOUNT_ORDER") int amount,
                            @Param("REGISTER") LocalDateTime register);
}