package com.gru.ifsp.AgendamentoBanca.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Permissoes {
    @Id
    private String code;
    private String nome;
}
