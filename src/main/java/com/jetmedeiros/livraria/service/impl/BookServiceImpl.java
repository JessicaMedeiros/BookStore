package com.jetmedeiros.livraria.service.impl;

import com.jetmedeiros.livraria.api.model.entity.Book;
import com.jetmedeiros.livraria.api.model.respository.BookRepository;
import com.jetmedeiros.livraria.expection.BusinessExpection;
import com.jetmedeiros.livraria.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }


    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new BusinessExpection("Isbn ja cadastrado");
        }
        return repository.save(book);
    }

}
