package ch.lan.teko.service;

import ch.lan.teko.model.Milestone;
import ch.lan.teko.repository.MilestoneRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MilestoneServiceImpl implements MilestoneService {

	@Autowired
    MilestoneRepository milestoneRepository;

	public long countAllMilestones() {
        return milestoneRepository.count();
    }

	public void deleteMilestone(Milestone milestone) {
        milestoneRepository.delete(milestone);
    }

	public Milestone findMilestone(Long id) {
        return milestoneRepository.findOne(id);
    }

	public List<Milestone> findAllMilestones() {
        return milestoneRepository.findAll();
    }

	public List<Milestone> findMilestoneEntries(int firstResult, int maxResults) {
        return milestoneRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveMilestone(Milestone milestone) {
        milestoneRepository.save(milestone);
    }

	public Milestone updateMilestone(Milestone milestone) {
        return milestoneRepository.save(milestone);
    }
}
