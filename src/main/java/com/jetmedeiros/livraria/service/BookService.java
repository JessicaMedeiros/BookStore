package com.jetmedeiros.livraria.service;

import com.jetmedeiros.livraria.api.model.entity.Book;

//responsavel por processar o livro, salvar na base e retornar povoado
//criado como interface porque por enquanto nao sera implementado
public interface BookService{

    Book save(Book any);
}
