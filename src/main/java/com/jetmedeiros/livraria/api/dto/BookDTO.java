package com.jetmedeiros.livraria.api.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;


@Data //alem dos getters and setters, toString e EqualsAndHashCode
@Builder //vai criar um builder com as configuracoes abaixo
@NoArgsConstructor //cria um construtor sem argumentos
@AllArgsConstructor
public class BookDTO {
//eh uma classe com atributos para representar os dados da requisicao (nesse caso, json) e converter em objeto


    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    @NotEmpty
    @Column(unique = true)
    private String isbn;

}
