package com.jetmedeiros.livraria.api.model.respository;

import com.jetmedeiros.livraria.api.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
}
