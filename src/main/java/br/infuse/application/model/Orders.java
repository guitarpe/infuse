package br.infuse.application.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Table(name="TB_ORDERS", schema="dbo")
public class Orders implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "CLIENT_ID", nullable=false)
	private Long clientId;

	@Column(name = "NUM_CONTROL", nullable=false)
	private Long numControl;
	
	@Column(name = "NM_PRODUCT", nullable=false)
	private String nmProduct;

	@Column(name = "VL_PRODUCT", nullable=false)
	private long vlProduct;

	@Column(name = "AMOUNT_ORDER")
	private int amountOrder;

	@Column(name = "PERCENT_DISCOUNT")
	private Double percentDiscount;

	@Column(name = "VL_DISCOUNT")
	private Double vlDiscount;

	@Column(name = "VL_ORDER", nullable=false)
	private Double vlOrder;

	@Column(name = "DT_REGISTER", columnDefinition = "TIMESTAMP")
	private LocalDateTime dtRegister;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Orders that = (Orders) o;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
