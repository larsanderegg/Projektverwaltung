package ch.lan.teko.service;

import ch.lan.teko.model.Activity;
import ch.lan.teko.model.DocumentReference;
import ch.lan.teko.repository.ActivityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

	@Autowired
    ActivityRepository activityRepository;

	public long countAllActivitys() {
        return activityRepository.count();
    }

	public void deleteActivity(Activity activity) {
        activityRepository.delete(activity);
    }

	public Activity findActivity(Long id) {
        return activityRepository.findOne(id);
    }

	public List<Activity> findAllActivitys() {
        return activityRepository.findAll();
    }

	public List<Activity> findActivityEntries(int firstResult, int maxResults) {
        return activityRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveActivity(Activity activity) {
        activityRepository.save(activity);
    }

	public Activity updateActivity(Activity activity) {
        return activityRepository.save(activity);
    }

	@Override
	public void addDocumentReference(Long activityId, DocumentReference documentReference) {
		Activity activity = findActivity(activityId);
		if (activity != null) {
			if (!activity.getLinks().contains(documentReference)) {
				activity.getLinks().add(documentReference);
				updateActivity(activity);
			}
		}
	}
}
