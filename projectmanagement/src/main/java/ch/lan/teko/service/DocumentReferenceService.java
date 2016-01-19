package ch.lan.teko.service;
import ch.lan.teko.model.DocumentReference;
import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { ch.lan.teko.model.DocumentReference.class })
public interface DocumentReferenceService {

	public abstract long countAllDocumentReferences();


	public abstract void deleteDocumentReference(DocumentReference documentReference);


	public abstract DocumentReference findDocumentReference(Long id);


	public abstract List<DocumentReference> findAllDocumentReferences();


	public abstract List<DocumentReference> findDocumentReferenceEntries(int firstResult, int maxResults);


	public abstract void saveDocumentReference(DocumentReference documentReference);


	public abstract DocumentReference updateDocumentReference(DocumentReference documentReference);

}
