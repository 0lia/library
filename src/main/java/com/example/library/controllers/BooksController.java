package com.example.library.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.library.models.Book;
import com.example.library.servises.BooksService;
import com.example.library.servises.PeopleService;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) String page,
                        @RequestParam(value = "size", required = false) String size,
                        @RequestParam(value = "sort", required = false) String sort){


        if(page == null || size == null)
            model.addAttribute("books", booksService.findAll(sort));
        else
            model.addAttribute("books",
                    booksService.findAll(Integer.parseInt(page), Integer.parseInt(size), sort));
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book")Book book){
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.findOne(id));
        model.addAttribute("people", peopleService.findAll());
        return "books/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";
        booksService.update(id, book);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/appoint")
    public String giveToPerson(@PathVariable("id") int bookId, @RequestParam(value = "pid") Integer pid){
        booksService.appoint(bookId, pid);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int bookId){
        booksService.free(bookId);
        return "redirect:/books/{id}";
    }

    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("keyword") String keyword){
        model.addAttribute("foundBooks", booksService.findByName(keyword));
        return "books/search";
    }

    @PostMapping("/search")
    public String find(@RequestParam("key") String key, Model model){
        model.addAttribute("foundBooks", booksService.findByName(key));
        return "books/search";
    }
}
