package ch.lan.teko.service;

import ch.lan.teko.model.PersonalResource;
import ch.lan.teko.repository.PersonalResourceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonalResourceServiceImpl implements PersonalResourceService {

	@Autowired
    PersonalResourceRepository personalResourceRepository;

	public long countAllPersonalResources() {
        return personalResourceRepository.count();
    }

	public void deletePersonalResource(PersonalResource personalResource) {
        personalResourceRepository.delete(personalResource);
    }

	public PersonalResource findPersonalResource(Long id) {
        return personalResourceRepository.findOne(id);
    }

	public List<PersonalResource> findAllPersonalResources() {
        return personalResourceRepository.findAll();
    }

	public List<PersonalResource> findPersonalResourceEntries(int firstResult, int maxResults) {
        return personalResourceRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void savePersonalResource(PersonalResource personalResource) {
        personalResourceRepository.save(personalResource);
    }

	public PersonalResource updatePersonalResource(PersonalResource personalResource) {
        return personalResourceRepository.save(personalResource);
    }
}
