package ch.lan.teko.service;

import ch.lan.teko.model.Phase;
import ch.lan.teko.repository.PhaseRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PhaseServiceImpl implements PhaseService {

	@Autowired
    PhaseRepository phaseRepository;

	public long countAllPhases() {
        return phaseRepository.count();
    }

	public void deletePhase(Phase phase) {
        phaseRepository.delete(phase);
    }

	public Phase findPhase(Long id) {
        return phaseRepository.findOne(id);
    }

	public List<Phase> findAllPhases() {
        return phaseRepository.findAll();
    }

	public List<Phase> findPhaseEntries(int firstResult, int maxResults) {
        return phaseRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void savePhase(Phase phase) {
        phaseRepository.save(phase);
    }

	public Phase updatePhase(Phase phase) {
        return phaseRepository.save(phase);
    }
}
