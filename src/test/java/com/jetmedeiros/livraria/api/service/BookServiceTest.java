package com.jetmedeiros.livraria.api.service;

import com.jetmedeiros.livraria.api.model.entity.Book;
import com.jetmedeiros.livraria.service.impl.BookServiceImpl;
import com.jetmedeiros.livraria.expection.BusinessExpection;
import com.jetmedeiros.livraria.api.model.respository.BookRepository;
import com.jetmedeiros.livraria.service.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach //faz com esse teste seja executado antes de cada teste
    public void setUp(){
        this.service = new BookServiceImpl( repository );
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        //cenario
        Book book = createValidBook();

        Mockito.when(repository.save(book)).thenReturn(
                Book.builder()
                        .id(1l)
                        .author("eu")
                        .title("meu app")
                        .isbn("092")
                        .build()
        );

        Book  savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("092");
        assertThat(savedBook.getTitle()).isEqualTo("meu app");
        assertThat(savedBook.getAuthor()).isEqualTo("eu");
    }

    private Book createValidBook() {
        return Book.builder().author("eu").title("meu app").isbn("092").build();
    }

    @Test
    @DisplayName("Livro ja cadastrado")
    public void shouldNotSaveBookWithDuplicatedIsbn(){
        Book book = createValidBook();

        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable( () -> service.save(book));
        assertThat(exception)
                .isInstanceOf(BusinessExpection.class)
                .hasMessage("Isbn ja cadastrado");

        //para verificar que o livro nao sera salvo
        Mockito.verify(repository, Mockito.never()).save(book);
    }

}
