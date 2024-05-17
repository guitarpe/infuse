package br.infuse.application.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NamedStoredProcedureQuery(
        name = "OrdersProcedure.ProcRegisterOrders",
        procedureName = "PROC_REGISTER_ORDERS",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "CLIENT", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "CONTROL", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "PRODUCT", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "V_PRODUCT", type = Double.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "AMOUNT", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "REGISTER", type = LocalDateTime.class)
        })
public class PedidosProcedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private long client;

    @Column
    private long control;

    @Column
    private String product;

    @Column
    private double value;

    @Column
    private int amount;

    @Column
    private LocalDateTime register;
}

