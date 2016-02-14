package ch.lan.teko.model;

import java.time.LocalDate;

public abstract class PhaseChild implements Comparable<PhaseChild>, ITimeBoxed {
	
	protected abstract LocalDate getDateToCompare();
	
	@Override
	public int compareTo(PhaseChild o) {
		if(this.getDateToCompare() == null){
			return -1;
		}
		return this.getDateToCompare().compareTo(o.getDateToCompare());
	}
}
