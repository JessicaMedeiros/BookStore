package com.jetmedeiros.livraria.api.resource;

import com.jetmedeiros.livraria.api.dto.BookDTO;
import com.jetmedeiros.livraria.api.model.entity.Book;
import com.jetmedeiros.livraria.expection.ApiErrors;
import com.jetmedeiros.livraria.expection.BusinessExpection;
import com.jetmedeiros.livraria.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper) {
        this.bookService = service;
        this.modelMapper = mapper;
    }


    //RequestBody significa que o Json enviado na requisicao seja transformado no dto
    //eh bom usar dto pra nao expor propriedades das entidades
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto){
        //modelMapper mapeia as duas classes e transfere as propriedades de mesmo nome de um classe pra outra
        Book entity = modelMapper.map(dto, Book.class);
        entity = bookService.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }

    //toda vez que o expection handler essa expection, ele a trata com o metodo abaixo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessExpection.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessExpection ex){
        return new ApiErrors(ex);
    }

}
