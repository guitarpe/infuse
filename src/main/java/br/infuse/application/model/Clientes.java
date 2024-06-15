package br.infuse.application.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Table(name="TB_CLIENTS")
public class Clientes {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NM_CLIENT", nullable = false)
    private String nomeCliente;

    @Column(name = "EMAIL_CLIENT", nullable = false)
    private String emailClient;

    @Column(name = "PHONE_CLIENT", nullable = false)
    private String phoneClient;

    @Column(name = "DT_REGISTER", columnDefinition = "TIMESTAMP")
    private LocalDateTime dtRegistro;

    @Column(name = "DT_UPDATE")
    private LocalDateTime dtUpdate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Clientes clientes = (Clientes) o;
        return id != null && Objects.equals(id, clientes.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
