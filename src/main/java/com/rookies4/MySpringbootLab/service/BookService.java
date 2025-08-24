package com.rookies4.MySpringbootLab.service;

import com.rookies4.MySpringbootLab.controller.dto.BookDTO;
import com.rookies4.MySpringbootLab.entity.Book;
import com.rookies4.MySpringbootLab.exception.BusinessException;
import com.rookies4.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    // CREATE
    public BookDTO.BookResponse create(BookDTO.BookCreateRequest req) {
        // ISBN 중복 검사
        bookRepository.findByIsbn(req.getIsbn()).ifPresent(b -> {
            throw new BusinessException("Duplicate ISBN: " + req.getIsbn(), HttpStatus.CONFLICT);
        });

        Book saved = bookRepository.save(req.toEntity());
        return BookDTO.BookResponse.from(saved);
    }

    // READ - all
    @Transactional(readOnly = true)
    public List<BookDTO.BookResponse> getAll() {
        return bookRepository.findAll()
                .stream().map(BookDTO.BookResponse::from)
                .toList();
    }

    // READ - by id
    @Transactional(readOnly = true)
    public BookDTO.BookResponse getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book not found with id: " + id));
        return BookDTO.BookResponse.from(book);
    }

    // READ - by isbn
    @Transactional(readOnly = true)
    public BookDTO.BookResponse getByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book not found by isbn: " + isbn));
        return BookDTO.BookResponse.from(book);
    }

    // READ - by author
    @Transactional(readOnly = true)
    public List<BookDTO.BookResponse> getByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .stream().map(BookDTO.BookResponse::from)
                .toList();
    }

    // UPDATE (전체 수정: 2-3 기준)
    public BookDTO.BookResponse update(Long id, BookDTO.BookUpdateRequest req) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cannot update. Book not found with id: " + id));

        // 2-3 요구: author, price, title, publishDate 수정
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setPrice(req.getPrice());
        book.setPublishDate(req.getPublishDate());

        // ※ ISBN 수정은 2-3 요구사항 범위를 벗어나므로 제외 (2-4에서 중복검사 로직과 함께 다루기)
        Book updated = bookRepository.save(book);
        return BookDTO.BookResponse.from(updated);
    }

    // DELETE
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cannot delete. Book not found with id: " + id));
        bookRepository.delete(book);
    }
}