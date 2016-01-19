package ch.lan.teko.model;
import ch.lan.teko.repository.DocumentReferenceRepository;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
@RooDataOnDemand(entity = DocumentReference.class)
public class DocumentReferenceDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<DocumentReference> data;

	@Autowired
    DocumentReferenceRepository documentReferenceRepository;

	public DocumentReference getNewTransientDocumentReference(int index) {
        DocumentReference obj = new DocumentReference();
        return obj;
    }

	public DocumentReference getSpecificDocumentReference(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        DocumentReference obj = data.get(index);
        Long id = obj.getId();
        return documentReferenceRepository.findOne(id);
    }

	public DocumentReference getRandomDocumentReference() {
        init();
        DocumentReference obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return documentReferenceRepository.findOne(id);
    }

	public boolean modifyDocumentReference(DocumentReference obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = documentReferenceRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'DocumentReference' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<DocumentReference>();
        for (int i = 0; i < 10; i++) {
            DocumentReference obj = getNewTransientDocumentReference(i);
            try {
                documentReferenceRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            documentReferenceRepository.flush();
            data.add(obj);
        }
    }
}
