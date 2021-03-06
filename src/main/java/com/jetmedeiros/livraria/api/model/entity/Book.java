package com.jetmedeiros.livraria.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor //Qquando o buidlder eh chamado eh necessario chamar essa annotation
@NoArgsConstructor//Qquando o buidlder eh chamado eh necessario chamar essa annotation
@Entity
@Table
public class Book {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String isbn;

}
