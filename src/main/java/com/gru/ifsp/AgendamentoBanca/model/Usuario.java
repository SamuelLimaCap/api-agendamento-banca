package com.gru.ifsp.AgendamentoBanca.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(unique = true)
    @Email
    private String email;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id", referencedColumnName = "id")
    )
    @Getter
    @Setter
    private List<Permissao> permissaoList;

    @Getter
    @Setter
    @Column(unique = true)
    private String prontuario;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String activationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banca_id")
    private AgendamentoBanca banca;

    public Usuario(Long id, String email, String password, boolean enabled, List<Permissao> permissaoList, String prontuario, String username) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.permissaoList = permissaoList;
        this.prontuario = prontuario;
        this.username = username;
    }

    public Usuario(Long id, String email, String password, boolean enabled, List<Permissao> permissaoList, String prontuario, String username, String activationCode) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.permissaoList = permissaoList;
        this.prontuario = prontuario;
        this.username = username;
        this.activationCode = activationCode;
    }

    public Usuario(Long id, String email, String password, boolean enabled, List<Permissao> permissaoList) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.permissaoList = permissaoList;
    }
}
