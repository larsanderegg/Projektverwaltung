package ch.lan.teko.service;
import ch.lan.teko.model.Activity;
import ch.lan.teko.model.DocumentReference;

import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { ch.lan.teko.model.Activity.class })
public interface ActivityService {

	public abstract long countAllActivitys();


	public abstract void deleteActivity(Activity activity);


	public abstract Activity findActivity(Long id);


	public abstract List<Activity> findAllActivitys();


	public abstract List<Activity> findActivityEntries(int firstResult, int maxResults);


	public abstract void saveActivity(Activity activity);


	public abstract Activity updateActivity(Activity activity);


	public abstract void addDocumentReference(Long activityId, DocumentReference documentReference);

}
