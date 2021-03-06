package ch.lan.teko.model;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class PhaseDataOnDemand {
	
	private Random rnd = new SecureRandom();

	private List<Phase> data;

	public Phase getNewTransientPhase(int index) {
		Phase obj = new Phase();
		setApprovalDate(obj, index);
		setName(obj, index);
		setPhaseState(obj, index);
		setPlanedReviewDate(obj, index);
		setProgress(obj, index);
		setProjectId(obj, index);
		setReviewDate(obj, index);
		return obj;
	}

	public void setApprovalDate(Phase obj, int index) {
		LocalDate approvalDate = null;
		obj.setApprovalDate(approvalDate);
	}

	public void setName(Phase obj, int index) {
		String name = "name_" + index;
		obj.setName(name);
	}

	public void setPhaseState(Phase obj, int index) {
		String phaseState = "phaseState_" + index;
		obj.setPhaseState(phaseState);
	}

	public void setPlanedReviewDate(Phase obj, int index) {
		LocalDate planedReviewDate = null;
		obj.setPlanedReviewDate(planedReviewDate);
	}

	public void setProgress(Phase obj, int index) {
		Byte progress = new Byte("1");
		obj.setProgress(progress);
	}

	public void setProjectId(Phase obj, int index) {
		Long projectId = new Integer(index).longValue();
		obj.setProjectId(projectId);
	}

	public void setReviewDate(Phase obj, int index) {
		LocalDate reviewDate = null;
		obj.setReviewDate(reviewDate);
	}

	public Phase getSpecificPhase(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		Phase obj = data.get(index);
		Long id = obj.getId();
		return Phase.findPhase(id);
	}

	public Phase getRandomPhase() {
		init();
		Phase obj = data.get(rnd.nextInt(data.size()));
		Long id = obj.getId();
		return Phase.findPhase(id);
	}

	public boolean modifyPhase(Phase obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = Phase.findPhaseEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'Phase' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<Phase>();
		for (int i = 0; i < 10; i++) {
			Phase obj = getNewTransientPhase(i);
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
