package com.gru.ifsp.AgendamentoBanca.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario")
/*@AllArgsConstructor*/
@NoArgsConstructor
@EqualsAndHashCode
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banca_id")
    private AgendamentoBanca banca;
    /*
    @OneToMany(mappedBy = "participantes")
    List<AgendamentoUsuarios> participantes;
    */
    public Usuario(Long id, String email, String password, boolean enabled, List<Permissao> permissaoList) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.permissaoList = permissaoList;
    }

    /*
    @Getter
    @Setter
    private String nome;

    @Getter
    @Setter
    private String prontuario;
    */
}
