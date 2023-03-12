package com.example.library.servises;

import com.example.library.models.Person;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.library.models.Book;
import com.example.library.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleService peopleService;

    public BooksService(BooksRepository booksRepository, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
    }


    public List<Book> findAll(String sort){
        if(sort == null || !Boolean.parseBoolean(sort))
            return booksRepository.findAll();
        return booksRepository.findAll(Sort.by("yearOfPublishing"));
    }
    public List<Book> findAll(int page, int size, String sort){
        return booksRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public List<Book> findByName(String key){
        return booksRepository.findByTitleStartingWith(key);
    }

    public Book findOne(int id){
        Optional<Book> foundBook =  booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public void appoint(int bookId, int pId){
        Book book = findOne(bookId);
        Person person = peopleService.findOne(pId);
        book.setOwner(person);
        person.addBook(book);
    }

    @Transactional
    public void free(int bookId){
        Book book = findOne(bookId);
        Person person = book.getOwner();
        person.deleteBook(book);
        book.setOwner(null);

    }
}
