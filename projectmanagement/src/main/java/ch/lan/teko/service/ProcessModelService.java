package ch.lan.teko.service;
import ch.lan.teko.model.ProcessModel;
import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { ch.lan.teko.model.ProcessModel.class })
public interface ProcessModelService {

	public abstract long countAllProcessModels();


	public abstract void deleteProcessModel(ProcessModel processModel);


	public abstract ProcessModel findProcessModel(Long id);


	public abstract List<ProcessModel> findAllProcessModels();


	public abstract List<ProcessModel> findProcessModelEntries(int firstResult, int maxResults);


	public abstract void saveProcessModel(ProcessModel processModel);


	public abstract ProcessModel updateProcessModel(ProcessModel processModel);

}
