package ch.lan.teko.service;
import ch.lan.teko.model.Milestone;
import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { ch.lan.teko.model.Milestone.class })
public interface MilestoneService {

	public abstract long countAllMilestones();


	public abstract void deleteMilestone(Milestone milestone);


	public abstract Milestone findMilestone(Long id);


	public abstract List<Milestone> findAllMilestones();


	public abstract List<Milestone> findMilestoneEntries(int firstResult, int maxResults);


	public abstract void saveMilestone(Milestone milestone);


	public abstract Milestone updateMilestone(Milestone milestone);

}
