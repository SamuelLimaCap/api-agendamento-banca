package com.gru.ifsp.AgendamentoBanca.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
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



}