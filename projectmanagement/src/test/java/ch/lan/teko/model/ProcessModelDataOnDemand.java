package ch.lan.teko.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ProcessModelDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ProcessModel> data;

	public ProcessModel getNewTransientProcessModel(int index) {
		ProcessModel obj = new ProcessModel();
		setName(obj, index);
		setPhases(obj, index);
		return obj;
	}

	public void setName(ProcessModel obj, int index) {
		String name = "name_" + index;
		obj.setName(name);
	}

	public void setPhases(ProcessModel obj, int index) {
		String phases = "phases_" + index;
		obj.setPhases(phases);
	}

	public ProcessModel getSpecificProcessModel(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		ProcessModel obj = data.get(index);
		Long id = obj.getId();
		return ProcessModel.findProcessModel(id);
	}

	public ProcessModel getRandomProcessModel() {
		init();
		ProcessModel obj = data.get(rnd.nextInt(data.size()));
		Long id = obj.getId();
		return ProcessModel.findProcessModel(id);
	}

	public boolean modifyProcessModel(ProcessModel obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = ProcessModel.findProcessModelEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'ProcessModel' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<ProcessModel>();
		for (int i = 0; i < 10; i++) {
			ProcessModel obj = getNewTransientProcessModel(i);
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
