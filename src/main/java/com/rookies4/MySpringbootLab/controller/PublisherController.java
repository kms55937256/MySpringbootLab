package com.rookies4.MySpringbootLab.controller;

import com.rookies4.MySpringbootLab.controller.dto.PublisherDTO;
import com.rookies4.MySpringbootLab.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public List<PublisherDTO.SimpleResponse> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<PublisherDTO.Response> getPublisherByName(@RequestParam String name) {
        return ResponseEntity.ok(publisherService.getPublisherByName(name));
    }

    @PostMapping
    public ResponseEntity<PublisherDTO.Response> createPublisher(@RequestBody PublisherDTO.Request request) {
        PublisherDTO.Response saved = publisherService.createPublisher(request);
        return ResponseEntity.created(URI.create("/api/publishers/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> updatePublisher(
            @PathVariable Long id,
            @RequestBody PublisherDTO.Request request) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}