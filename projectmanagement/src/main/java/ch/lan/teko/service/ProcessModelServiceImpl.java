package ch.lan.teko.service;

import ch.lan.teko.model.ProcessModel;
import ch.lan.teko.repository.ProcessModelRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcessModelServiceImpl implements ProcessModelService {

	@Autowired
    ProcessModelRepository processModelRepository;

	public long countAllProcessModels() {
        return processModelRepository.count();
    }

	public void deleteProcessModel(ProcessModel processModel) {
        processModelRepository.delete(processModel);
    }

	public ProcessModel findProcessModel(Long id) {
        return processModelRepository.findOne(id);
    }

	public List<ProcessModel> findAllProcessModels() {
        return processModelRepository.findAll();
    }

	public List<ProcessModel> findProcessModelEntries(int firstResult, int maxResults) {
        return processModelRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveProcessModel(ProcessModel processModel) {
        processModelRepository.save(processModel);
    }

	public ProcessModel updateProcessModel(ProcessModel processModel) {
        return processModelRepository.save(processModel);
    }
}
