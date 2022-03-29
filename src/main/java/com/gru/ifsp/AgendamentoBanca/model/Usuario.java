package com.gru.ifsp.AgendamentoBanca.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private LocalDateTime dataCadastro;
    @OneToMany(mappedBy = "permissoes")
    private List<Permissoes> tipo = new ArrayList<>();

}
