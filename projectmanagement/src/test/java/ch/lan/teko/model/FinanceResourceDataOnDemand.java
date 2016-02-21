package ch.lan.teko.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class FinanceResourceDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<FinanceResource> data;

	public FinanceResource getNewTransientFinanceResource(int index) {
		FinanceResource obj = new FinanceResource();
		setActivityId(obj, index);
		setEffectiv(obj, index);
		setExplanation(obj, index);
		setPlaned(obj, index);
		setType(obj, index);
		return obj;
	}

	public void setActivityId(FinanceResource obj, int index) {
		Long activityId = new Integer(index).longValue();
		obj.setActivityId(activityId);
	}

	public void setEffectiv(FinanceResource obj, int index) {
		Integer effectiv = new Integer(index);
		obj.setEffectiv(effectiv);
	}

	public void setExplanation(FinanceResource obj, int index) {
		String explanation = "explanation_" + index;
		obj.setExplanation(explanation);
	}

	public void setPlaned(FinanceResource obj, int index) {
		Integer planed = new Integer(index);
		obj.setPlaned(planed);
	}

	public void setType(FinanceResource obj, int index) {
		String type = "type_" + index;
		obj.setType(type);
	}

	public FinanceResource getSpecificFinanceResource(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		FinanceResource obj = data.get(index);
		Long id = obj.getId();
		return FinanceResource.findFinanceResource(id);
	}

	public FinanceResource getRandomFinanceResource() {
		init();
		FinanceResource obj = data.get(rnd.nextInt(data.size()));
		Long id = obj.getId();
		return FinanceResource.findFinanceResource(id);
	}

	public boolean modifyFinanceResource(FinanceResource obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = FinanceResource.findFinanceResourceEntries(from, to);
		if (data == null) {
			throw new IllegalStateException(
					"Find entries implementation for 'FinanceResource' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<FinanceResource>();
		for (int i = 0; i < 10; i++) {
			FinanceResource obj = getNewTransientFinanceResource(i);
			try {
				obj.persist();
			} catch (final ConstraintViolationException e) {
				final StringBuilder msg = new StringBuilder();
				for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
					final ConstraintViolation<?> cv = iter.next();
					msg.append("[").append(cv.getRootBean().getClass().getName()).append(".")
							.append(cv.getPropertyPath()).append(": ").append(cv.getMessage())
							.append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
				}
				throw new IllegalStateException(msg.toString(), e);
			}
			obj.flush();
			data.add(obj);
		}
	}
}
