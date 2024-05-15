package br.infuse.application.model;

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
@Table(name="TB_CLIENTS")
public class Clientes implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long clientId;
	
	@Column(name = "NM_CLIENT", nullable=false)
	private String nmClient;
	
	@Column(name = "DT_REGISTER", columnDefinition = "TIMESTAMP")
	private LocalDateTime dtRegister;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Clientes that = (Clientes) o;
		return clientId != null && Objects.equals(clientId, that.clientId);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
