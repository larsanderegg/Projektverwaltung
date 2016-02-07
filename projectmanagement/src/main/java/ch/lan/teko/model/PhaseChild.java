package ch.lan.teko.model;

import java.time.LocalDate;

public abstract class PhaseChild implements Comparable<PhaseChild> {
	
	protected abstract LocalDate getDateToCompare();
	
	@Override
	public int compareTo(PhaseChild o) {
		return this.getDateToCompare().compareTo(o.getDateToCompare());
	}
}
