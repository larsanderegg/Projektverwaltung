package ch.lan.teko.service;
import ch.lan.teko.model.FinanceResource;
import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { ch.lan.teko.model.FinanceResource.class })
public interface FinanceResourceService {

	public abstract long countAllFinanceResources();


	public abstract void deleteFinanceResource(FinanceResource financeResource);


	public abstract FinanceResource findFinanceResource(Long id);


	public abstract List<FinanceResource> findAllFinanceResources();


	public abstract List<FinanceResource> findFinanceResourceEntries(int firstResult, int maxResults);


	public abstract void saveFinanceResource(FinanceResource financeResource);


	public abstract FinanceResource updateFinanceResource(FinanceResource financeResource);

}
