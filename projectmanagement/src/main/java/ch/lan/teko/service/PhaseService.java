package ch.lan.teko.service;
import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.model.Phase;
import ch.lan.teko.model.ProcessModel;

import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { ch.lan.teko.model.Phase.class })
public interface PhaseService {

	public abstract long countAllPhases();


	public abstract void deletePhase(Phase phase);


	public abstract Phase findPhase(Long id);


	public abstract List<Phase> findAllPhases();


	public abstract List<Phase> findPhaseEntries(int firstResult, int maxResults);


	public abstract void savePhase(Phase phase);


	public abstract Phase updatePhase(Phase phase);


	public abstract List<Phase> generatePhases(ProcessModel processModel);


	public abstract void addDocumentReference(Long phaseId, DocumentReference documentReference);

}
