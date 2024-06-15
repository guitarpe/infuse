package br.infuse.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Table(name="TB_ORDERS")
public class Pedidos implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NUM_CONTROL", nullable=false)
	@JsonProperty("control")
	private Long controle;

	@Column(name = "CLIENT_ID", nullable=false)
	@JsonProperty("client")
	private Long cliente;
	
	@Column(name = "NM_PRODUCT", nullable=false)
	@JsonProperty("name_product")
	private String nomeProduto;

	@Column(name = "VL_PRODUCT", nullable=false)
	@JsonProperty("value_product")
	private BigDecimal vlProduto;

	@Column(name = "AMOUNT_ORDER")
	@JsonProperty("client")
	private int quantidade;

	@Column(name = "PERCENT_DISCOUNT")
	@JsonProperty("percent_discount")
	private BigDecimal percDesconto;

	@Column(name = "VL_DISCOUNT")
	@JsonProperty("value_discount")
	private BigDecimal vlDesconto;

	@Column(name = "VL_ORDER", nullable=false)
	@JsonProperty("value_order")
	private BigDecimal vlPedido;

	@Column(name = "DT_REGISTER", columnDefinition = "TIMESTAMP")
	@JsonProperty("register")
	private LocalDate dtRegistro;

	@Column(name = "DT_UPDATE")
	private LocalDate dtUpdate;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Pedidos pedidos = (Pedidos) o;
		return controle != null && Objects.equals(controle, pedidos.controle);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
