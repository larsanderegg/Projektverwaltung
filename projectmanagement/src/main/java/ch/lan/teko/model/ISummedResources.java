package ch.lan.teko.model;

/**
 * Interface to return summed resources
 * @author landeregg
 */
public interface ISummedResources {

	/**
	 * Returns a {@link ResourceCollector} which is filled with all resources of this object.
	 * @return a {@link ResourceCollector}
	 */
	ResourceCollector getSummedResources();
}
