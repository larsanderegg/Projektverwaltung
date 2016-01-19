package ch.lan.teko.service;
import ch.lan.teko.model.PersonalResource;
import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { ch.lan.teko.model.PersonalResource.class })
public interface PersonalResourceService {

	public abstract long countAllPersonalResources();


	public abstract void deletePersonalResource(PersonalResource personalResource);


	public abstract PersonalResource findPersonalResource(Long id);


	public abstract List<PersonalResource> findAllPersonalResources();


	public abstract List<PersonalResource> findPersonalResourceEntries(int firstResult, int maxResults);


	public abstract void savePersonalResource(PersonalResource personalResource);


	public abstract PersonalResource updatePersonalResource(PersonalResource personalResource);

}
