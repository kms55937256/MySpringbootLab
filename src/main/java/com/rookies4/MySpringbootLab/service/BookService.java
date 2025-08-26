package com.rookies4.MySpringbootLab.service;

import com.rookies4.MySpringbootLab.controller.dto.BookDTO;
import com.rookies4.MySpringbootLab.entity.Book;
import com.rookies4.MySpringbootLab.entity.BookDetail;
import com.rookies4.MySpringbootLab.exception.BusinessException;
import com.rookies4.MySpringbootLab.exception.ErrorCode;
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

    /* ===================== CREATE ===================== */
    public BookDTO.BookResponse create(BookDTO.BookCreateRequest req) {
        // ISBN 중복 검사
        if (bookRepository.existsByIsbn(req.getIsbn())) {
            throw new BusinessException(
                    ErrorCode.ISBN_DUPLICATE.formatMessage(req.getIsbn()),
                    ErrorCode.ISBN_DUPLICATE.getHttpStatus()
            );
        }

        Book book = req.toEntity();

        // 상세 동시 처리(있을 때만)
        if (req.getDetailRequest() != null) {
            BookDetail detail = req.getDetailRequest().toEntity();
            book.setDetail(detail); // 편의 메서드로 양방향 연결(detail.setBook(this) 포함)
        }

        Book saved = bookRepository.save(book);
        return BookDTO.BookResponse.from(saved);
    }

    /* ===================== READ ===================== */
    @Transactional(readOnly = true)
    public List<BookDTO.BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookDTO.BookResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookDTO.BookResponse getById(Long id) {
        Book book = bookRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Book", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));
        return BookDTO.BookResponse.from(book);
    }

    @Transactional(readOnly = true)
    public BookDTO.BookResponse getByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithDetail(isbn)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Book", "isbn", isbn),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));
        return BookDTO.BookResponse.from(book);
    }

    @Transactional(readOnly = true)
    public List<BookDTO.BookResponse> getByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .stream()
                .map(BookDTO.BookResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookDTO.BookResponse> getByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookDTO.BookResponse::from)
                .toList();
    }

    /* ===================== UPDATE (PUT: 전체 교체) ===================== */
    public BookDTO.BookResponse update(Long id, BookDTO.BookUpdateRequest req) {
        Book book = bookRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Book", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));

        // Book 필드 전체 교체(ISBN은 PUT 스펙상 유지; ISBN 변경은 PATCH에서 처리)
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setPrice(req.getPrice());
        book.setPublishDate(req.getPublishDate());

        // 상세가 오면 "전체 교체" 정책으로 반영
        if (req.getDetailRequest() != null) {
            BookDetail detail = book.getDetail();
            if (detail == null) {
                detail = new BookDetail();
                book.setDetail(detail); // 양방향 연결 포함
            }
            detail.setDescription(req.getDetailRequest().getDescription());
            detail.setLanguage(req.getDetailRequest().getLanguage());
            detail.setPageCount(req.getDetailRequest().getPageCount());
            detail.setPublisher(req.getDetailRequest().getPublisher());
            detail.setCoverImageUrl(req.getDetailRequest().getCoverImageUrl());
            detail.setEdition(req.getDetailRequest().getEdition());
        }

        Book updated = bookRepository.save(book);
        return BookDTO.BookResponse.from(updated);
    }

    /* ===================== PATCH (부분수정) ===================== */
    public BookDTO.BookResponse patchBook(Long id, BookDTO.PatchRequest req) {
        Book book = bookRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Book", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));

        // null 아닌 필드만 반영
        if (req.getTitle() != null)      book.setTitle(req.getTitle());
        if (req.getAuthor() != null)     book.setAuthor(req.getAuthor());
        if (req.getPrice() != null)      book.setPrice(req.getPrice());
        if (req.getPublishDate() != null)book.setPublishDate(req.getPublishDate());

        if (req.getIsbn() != null) {
            String newIsbn = req.getIsbn();
            if (!newIsbn.equals(book.getIsbn())) {
                if (bookRepository.existsByIsbn(newIsbn)) {
                    throw new BusinessException(
                            ErrorCode.ISBN_DUPLICATE.formatMessage(newIsbn),
                            ErrorCode.ISBN_DUPLICATE.getHttpStatus()
                    );
                }
                book.setIsbn(newIsbn);
            }
        }

        Book saved = bookRepository.save(book);
        return BookDTO.BookResponse.from(saved);
    }

    public BookDTO.BookResponse patchBookDetail(Long id, BookDTO.BookDetailPatchRequest req) {
        Book book = bookRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Book", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));

        BookDetail detail = book.getDetail();
        if (detail == null) {
            detail = new BookDetail();
            book.setDetail(detail); // 양방향 연결
        }

        // null 아닌 상세 필드만 반영
        if (req.getDescription() != null)   detail.setDescription(req.getDescription());
        if (req.getLanguage() != null)      detail.setLanguage(req.getLanguage());
        if (req.getPageCount() != null)     detail.setPageCount(req.getPageCount());
        if (req.getPublisher() != null)     detail.setPublisher(req.getPublisher());
        if (req.getCoverImageUrl() != null) detail.setCoverImageUrl(req.getCoverImageUrl());
        if (req.getEdition() != null)       detail.setEdition(req.getEdition());

        Book saved = bookRepository.save(book); // cascade로 detail까지 반영
        return BookDTO.BookResponse.from(saved);
    }

    /* ===================== DELETE ===================== */
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Book", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));
        bookRepository.delete(book);
    }
}