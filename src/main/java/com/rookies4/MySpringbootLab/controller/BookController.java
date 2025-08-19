package com.rookies4.MySpringbootLab.controller;

import com.rookies4.MySpringbootLab.entity.Book;
import com.rookies4.MySpringbootLab.exception.BusinessException;
import com.rookies4.MySpringbootLab.repository.BookRepository;
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

    private final BookRepository bookRepository;

    /** ✅ 전체 목록 조회 */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /** ✅ ID로 단건 조회 (없으면 BusinessException → Advice에서 JSON 404) */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book not found with id: " + id));
        return ResponseEntity.ok(book);
    }

    /** ✅ ISBN으로 단건 조회 (없으면 BusinessException → 404) */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book not found by isbn: " + isbn));
        return ResponseEntity.ok(book);
    }

    /** ✅ 저자명으로 목록 조회 (없으면 빈 배열) */
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }

    /** ✅ 등록: 201 Created + Location 헤더 */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        // (실습 2-3 확장 대비) 예: ISBN 중복 체크
        bookRepository.findByIsbn(book.getIsbn()).ifPresent(b -> {
            throw new BusinessException("Duplicate ISBN: " + book.getIsbn());
        });

        Book saved = bookRepository.save(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    /** ✅ 수정 (전체 교체, PUT) */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cannot update. Book not found with id: " + id));

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setIsbn(book.getIsbn());
        existing.setPrice(book.getPrice());
        existing.setPublishDate(book.getPublishDate());

        Book updated = bookRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    /** ✅ 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cannot delete. Book not found with id: " + id));

        bookRepository.delete(book);
        return ResponseEntity.noContent().build();
    }
}