package br.infuse.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Table(name="TB_ORDERS")
public class Pedidos implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NUM_CONTROL", nullable=false)
	@JsonProperty("control")
	private Long numControl;

	@Column(name = "CLIENT_ID", nullable=false)
	@JsonProperty("client")
	private Long clientId;
	
	@Column(name = "NM_PRODUCT", nullable=false)
	@JsonProperty("name_product")
	private String nmProduct;

	@Column(name = "VL_PRODUCT", nullable=false)
	@JsonProperty("value_product")
	private double vlProduct;

	@Column(name = "AMOUNT_ORDER")
	@JsonProperty("client")
	private int amountOrder;

	@Column(name = "PERCENT_DISCOUNT")
	@JsonProperty("percent_discount")
	private Double percentDiscount;

	@Column(name = "VL_DISCOUNT")
	@JsonProperty("value_discount")
	private Double vlDiscount;

	@Column(name = "VL_ORDER", nullable=false)
	@JsonProperty("value_order")
	private Double vlOrder;

	@Column(name = "DT_REGISTER", columnDefinition = "TIMESTAMP")
	@JsonProperty("register")
	private LocalDateTime dtRegister;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Pedidos that = (Pedidos) o;
		return numControl != null && Objects.equals(numControl, that.numControl);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
