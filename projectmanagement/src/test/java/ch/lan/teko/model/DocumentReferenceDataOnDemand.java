package ch.lan.teko.model;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class DocumentReferenceDataOnDemand {
	
private Random rnd = new SecureRandom();
    
    private List<DocumentReference> data;
    
    public DocumentReference getNewTransientDocumentReference(int index) {
        DocumentReference obj = new DocumentReference();
        setActivityId(obj, index);
        setName(obj, index);
        setPath(obj, index);
        setPhaseId(obj, index);
        setProjectId(obj, index);
        return obj;
    }
    
    public void setActivityId(DocumentReference obj, int index) {
        Long activityId = new Integer(index).longValue();
        obj.setActivityId(activityId);
    }
    
    public void setName(DocumentReference obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void setPath(DocumentReference obj, int index) {
        String path = "path_" + index;
        obj.setPath(path);
    }
    
    public void setPhaseId(DocumentReference obj, int index) {
        Long phaseId = new Integer(index).longValue();
        obj.setPhaseId(phaseId);
    }
    
    public void setProjectId(DocumentReference obj, int index) {
        Long projectId = new Integer(index).longValue();
        obj.setProjectId(projectId);
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
        return DocumentReference.findDocumentReference(id);
    }
    
    public DocumentReference getRandomDocumentReference() {
        init();
        DocumentReference obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return DocumentReference.findDocumentReference(id);
    } 
    
    public boolean modifyDocumentReference(DocumentReference obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = DocumentReference.findDocumentReferenceEntries(from, to);
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
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
