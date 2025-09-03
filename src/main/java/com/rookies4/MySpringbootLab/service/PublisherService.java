package com.rookies4.MySpringbootLab.service;

import com.rookies4.MySpringbootLab.controller.dto.PublisherDTO;
import com.rookies4.MySpringbootLab.entity.Publisher;
import com.rookies4.MySpringbootLab.exception.BusinessException;
import com.rookies4.MySpringbootLab.exception.ErrorCode;
import com.rookies4.MySpringbootLab.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherService {

    private final PublisherRepository publisherRepository;

    /** 모든 출판사 조회 */
    public List<PublisherDTO.SimpleResponse> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(PublisherDTO.SimpleResponse::fromEntityWithCount)
                .toList();
    }

    /** ID로 출판사 조회 (책 목록 포함) */
    public PublisherDTO.Response getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Publisher", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));
        return PublisherDTO.Response.from(publisher);
    }

    /** 이름으로 출판사 조회 */
    public PublisherDTO.Response getPublisherByName(String name) {
        Publisher publisher = publisherRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Publisher", "name", name),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));
        return PublisherDTO.Response.from(publisher);
    }

    /** 출판사 생성 */
    public PublisherDTO.Response createPublisher(PublisherDTO.Request req) {
        if (publisherRepository.existsByName(req.getName())) {
            throw new BusinessException(
                    ErrorCode.PUBLISHER_DUPLICATE.formatMessage(req.getName()),
                    ErrorCode.PUBLISHER_DUPLICATE.getHttpStatus()
            );
        }
        Publisher saved = publisherRepository.save(req.toEntity());
        return PublisherDTO.Response.from(saved);
    }

    /** 출판사 수정 */
    public PublisherDTO.Response updatePublisher(Long id, PublisherDTO.Request req) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Publisher", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));

        publisher.setName(req.getName());
        publisher.setEstablishedDate(req.getEstablishedDate());
        publisher.setAddress(req.getAddress());

        return PublisherDTO.Response.from(publisherRepository.save(publisher));
    }

    /** 출판사 삭제 (책이 있는 경우 불가) */
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND.formatMessage("Publisher", "id", id),
                        ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()
                ));

        if (!publisher.getBooks().isEmpty()) {
            throw new BusinessException(
                    ErrorCode.BAD_REQUEST.formatMessage("출판사에 등록된 도서가 있어 삭제할 수 없습니다."),
                    ErrorCode.BAD_REQUEST.getHttpStatus()
            );
        }

        publisherRepository.delete(publisher);
    }
}