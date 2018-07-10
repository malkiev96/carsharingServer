package com.carsharing.service.impl;

import com.carsharing.model.Document;
import com.carsharing.repository.DocumentRepository;
import com.carsharing.service.ClientService;
import com.carsharing.service.DocumentService;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    private DocumentRepository documentRepository;
    private ClientService clientService;

    public DocumentServiceImpl(DocumentRepository documentRepository, ClientService clientService) {
        this.documentRepository = documentRepository;
        this.clientService = clientService;
    }

    @Override
    public void save(Document document) {
        documentRepository.save(document);
    }
}
