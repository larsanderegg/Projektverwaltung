package ch.lan.teko.model;

/**
 * Interface to return time boxed duration
 * @author landeregg
 */
public interface ITimeBoxed {
	
	/**
	 * Returns a {@link TimeBoxedData} which contains the duration of this object.
	 * @return a {@link TimeBoxedData}
	 */
	TimeBoxedData getTimeBoxedData();
}
