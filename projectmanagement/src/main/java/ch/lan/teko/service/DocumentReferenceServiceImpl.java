package ch.lan.teko.service;

import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.repository.DocumentReferenceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentReferenceServiceImpl implements DocumentReferenceService {

	@Autowired
    DocumentReferenceRepository documentReferenceRepository;

	public long countAllDocumentReferences() {
        return documentReferenceRepository.count();
    }

	public void deleteDocumentReference(DocumentReference documentReference) {
        documentReferenceRepository.delete(documentReference);
    }

	public DocumentReference findDocumentReference(Long id) {
        return documentReferenceRepository.findOne(id);
    }

	public List<DocumentReference> findAllDocumentReferences() {
        return documentReferenceRepository.findAll();
    }

	public List<DocumentReference> findDocumentReferenceEntries(int firstResult, int maxResults) {
        return documentReferenceRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveDocumentReference(DocumentReference documentReference) {
        documentReferenceRepository.save(documentReference);
    }

	public DocumentReference updateDocumentReference(DocumentReference documentReference) {
        return documentReferenceRepository.save(documentReference);
    }
}
