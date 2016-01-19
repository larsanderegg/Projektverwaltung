package ch.lan.teko.service;

import ch.lan.teko.model.FinanceResource;
import ch.lan.teko.repository.FinanceResourceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FinanceResourceServiceImpl implements FinanceResourceService {

	@Autowired
    FinanceResourceRepository financeResourceRepository;

	public long countAllFinanceResources() {
        return financeResourceRepository.count();
    }

	public void deleteFinanceResource(FinanceResource financeResource) {
        financeResourceRepository.delete(financeResource);
    }

	public FinanceResource findFinanceResource(Long id) {
        return financeResourceRepository.findOne(id);
    }

	public List<FinanceResource> findAllFinanceResources() {
        return financeResourceRepository.findAll();
    }

	public List<FinanceResource> findFinanceResourceEntries(int firstResult, int maxResults) {
        return financeResourceRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveFinanceResource(FinanceResource financeResource) {
        financeResourceRepository.save(financeResource);
    }

	public FinanceResource updateFinanceResource(FinanceResource financeResource) {
        return financeResourceRepository.save(financeResource);
    }
}
