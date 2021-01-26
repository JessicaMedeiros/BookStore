package com.jetmedeiros.livraria.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetmedeiros.livraria.api.dto.BookDTO;
import com.jetmedeiros.livraria.expection.BusinessExpection;
import com.jetmedeiros.livraria.service.BookService;
import com.jetmedeiros.livraria.api.model.entity.Book;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;


@ExtendWith(SpringExtension.class)  //anotação do Junit 5
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc //testa o comportamento da api
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc; //simula um requisicao pra api

    @MockBean //mock utilizado pela Spring para criar um mock dessa instancia
    BookService service;


    @Test
    @DisplayName("Deve criar um licro com sucesso") //Anottation do Junit5. Coloca no terminal um nome dado pra funcao
    public void createBookTest() throws Exception{

        BookDTO dto = createNewBook();

        // esse aqui eh o livro "salvo na base de dados. Quando o service chama o save ele compara ao Jso do DTO
        Book savedBook = Book.builder().id(10l).author("eu").title("meu app").isbn("092").build();


        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willReturn(savedBook);

        //recebe um objeto e transforma em json ja que nao eh interessante fazer injecao de json dentro no content
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated()) //Ele compara se o resultado foi 201, ou seja, Created na requisicao http
                .andExpect( MockMvcResultMatchers.jsonPath("id").isNotEmpty() ) //vai verificar se o json de reposta sera igual a desejada e nao vazia. Se o id nao estiver vazio eh porque o objeto foi criado
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(dto.getIsbn()));

    }



    //1. sem criar o controller com a rota e o DTO, a resposta eh 404 ja que ele nao conhece a rota
    //--------------------------------------

    @Test
    @DisplayName("DEve dar erro de validacao quando nao houver dados suficientes")
    public void createInvalidBookTest() throws Exception{
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  //Compara se eh 404
                .andExpect( MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)));

    }

    @Test
    @DisplayName("Erro de validacao caso repetido")
    public void createBookWithDuplicatedIsbn() throws Exception{

        String messagemErro = "Isbn ja cadastrada";

        BookDTO dto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willThrow(new BusinessExpection(messagemErro));


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  //Compara se eh 404
                .andExpect( MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(messagemErro));
    }


    private BookDTO createNewBook() {
        return BookDTO.builder().author("eu").title("meu app").isbn("092").build();
    }

}
