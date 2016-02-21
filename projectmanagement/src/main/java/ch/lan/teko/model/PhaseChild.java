package ch.lan.teko.model;

import java.time.LocalDate;

/**
 * Represents a sortable child of a phase.
 * @author landeregg
 */
public abstract class PhaseChild implements Comparable<PhaseChild>, ITimeBoxed {
	
	/**
	 * Returns the date to used when sorting
	 * @return a {@link LocalDate}
	 */
	protected abstract LocalDate getDateToCompare();
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(PhaseChild o) {
		if(this.getDateToCompare() == null){
			return -1;
		}
		return this.getDateToCompare().compareTo(o.getDateToCompare());
	}
}
