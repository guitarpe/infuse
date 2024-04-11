package br.infuse.application.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Table(name="TB_LOGS")
public class Logs implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;
	
	@Column(name = "NM_SERVICE", nullable=false)
	private String nmService;
	
	@Column(name = "NM_METHOD", nullable=false)
	private String nmMethod;
	
	@Column(name = "LOG_EXECUTE", nullable=false)
	private String logExecute;
	
	@Column(name = "DT_REGISTER", nullable=false, columnDefinition = "TIMESTAMP")
	private Date dtRegister;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Logs that = (Logs) o;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
