package com.rookies4.MySpringbootLab.controller;

import com.rookies4.MySpringbootLab.controller.dto.BookDTO;
import com.rookies4.MySpringbootLab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    /** ✅ 전체 목록 조회 (DTO 응답) */
    @GetMapping
    public List<BookDTO.BookResponse> getAllBooks() {
        return bookService.getAll();
    }

    /** ✅ ID 조회 (DTO 응답) */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    /** ✅ ISBN 조회 (DTO 응답) */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.BookResponse> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getByIsbn(isbn));
    }

    /** ✅ 저자별 목록 조회 (DTO 응답) */
    @GetMapping("/author/{author}")
    public List<BookDTO.BookResponse> getBooksByAuthor(@PathVariable String author) {
        return bookService.getByAuthor(author);
    }

    /** ✅ 등록: 201 Created + Location 헤더 (DTO 요청/응답) */
    @PostMapping
    public ResponseEntity<BookDTO.BookResponse> createBook(@RequestBody @Valid BookDTO.BookCreateRequest request) {
        BookDTO.BookResponse saved = bookService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    /** ✅ 수정 (전체 교체, PUT) - DTO 요청/응답 */
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> updateBook(@PathVariable Long id,
                                                           @RequestBody @Valid BookDTO.BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    /** ✅ 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}