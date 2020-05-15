package com.carsharing.service.impl;

import com.carsharing.model.Document;
import com.carsharing.repository.DocumentRepository;
import com.carsharing.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public void save(Document document) {
        documentRepository.save(document);
    }
}
