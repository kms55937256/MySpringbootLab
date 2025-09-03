package com.rookies4.MySpringbootLab.service;

import com.rookies4.MySpringbootLab.controller.dto.BookDTO;
import com.rookies4.MySpringbootLab.entity.Book;
import com.rookies4.MySpringbootLab.entity.BookDetail;
import com.rookies4.MySpringbootLab.exception.BusinessException;
import com.rookies4.MySpringbootLab.exception.ErrorCode;
import com.rookies4.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
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

        // 상세 동시 처리
        if (req.getDetailRequest() != null) {
            BookDetail detail = req.getDetailRequest().toEntity();
            book.setDetail(detail); // 양방향 연결
        }

        Book saved = bookRepository.save(book);
        return BookDTO.BookResponse.from(saved);
    }

    /* ===================== READ ===================== */
    @Transactional(readOnly = true)
    public List<BookDTO.BookResponse> getAll() {
        // Lazy 문제 해결: 항상 BookDetail까지 함께 로딩
        return bookRepository.findAllWithDetail()
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

    /* ===================== UPDATE (PUT) ===================== */
    public BookDTO.BookResponse update(Long id, BookDTO.BookUpdateRequest req) {
        Book book = bookRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Book", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));

        // Book 필드 전체 교체
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setPrice(req.getPrice());
        book.setPublishDate(req.getPublishDate());

        // BookDetail 교체
        if (req.getDetailRequest() != null) {
            BookDetail detail = book.getDetail();
            if (detail == null) {
                detail = new BookDetail();
                book.setDetail(detail);
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

    /* ===================== PATCH ===================== */
    public BookDTO.BookResponse patchBook(Long id, BookDTO.PatchRequest req) {
        Book book = bookRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Book", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));

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
            book.setDetail(detail);
        }

        if (req.getDescription() != null)   detail.setDescription(req.getDescription());
        if (req.getLanguage() != null)      detail.setLanguage(req.getLanguage());
        if (req.getPageCount() != null)     detail.setPageCount(req.getPageCount());
        if (req.getPublisher() != null)     detail.setPublisher(req.getPublisher());
        if (req.getCoverImageUrl() != null) detail.setCoverImageUrl(req.getCoverImageUrl());
        if (req.getEdition() != null)       detail.setEdition(req.getEdition());

        Book saved = bookRepository.save(book);
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
