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

    /* 목록 */
    @GetMapping
    public List<BookDTO.BookResponse> getAllBooks() {
        return bookService.getAll();
    }

    /* ID 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    /* ISBN 조회 */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.BookResponse> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getByIsbn(isbn));
    }

    /* 저자 검색 (QueryParam 스타일) */
    @GetMapping("/search/author")
    public List<BookDTO.BookResponse> searchByAuthor(@RequestParam String author) {
        return bookService.getByAuthor(author);
    }

    /* 제목 검색 (필요 시 Service에 구현) */
    @GetMapping("/search/title")
    public List<BookDTO.BookResponse> searchByTitle(@RequestParam String title) {
        return bookService.getByTitle(title);
    }

    /* 등록: 201 + Location */
    @PostMapping
    public ResponseEntity<BookDTO.BookResponse> createBook(
            @RequestBody @Valid BookDTO.BookCreateRequest request) {
        BookDTO.BookResponse saved = bookService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    /* 전체수정(PUT) */
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody @Valid BookDTO.BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    /* 부분수정(PATCH) - Book 필드 */
    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> patchBook(
            @PathVariable Long id,
            @RequestBody BookDTO.PatchRequest request) {
        return ResponseEntity.ok(bookService.patchBook(id, request));
    }

    /* 부분수정(PATCH) - BookDetail 필드 */
    @PatchMapping("/{id}/detail")
    public ResponseEntity<BookDTO.BookResponse> patchBookDetail(
            @PathVariable Long id,
            @RequestBody BookDTO.BookDetailPatchRequest request) {
        return ResponseEntity.ok(bookService.patchBookDetail(id, request));
    }

    /* 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}