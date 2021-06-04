package com.devsuperior.dscatalog.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ClientDto;
import com.devsuperior.dscatalog.entities.Client;
import com.devsuperior.dscatalog.repository.ClientRepository;
import com.devsuperior.dscatalog.service.exception.DatabaseException;
import com.devsuperior.dscatalog.service.exception.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDto> findAllPaged(PageRequest pageRequest){
			
		return repository.findAll(pageRequest)
				.map(x -> new ClientDto(x));

	}
	

	@Transactional(readOnly = true)
	public ClientDto findById(Long id) {
		  Optional<Client> obj = repository.findById(id);
		  Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		  return new ClientDto(entity);
	
	}
	
	@Transactional
	public ClientDto insert(ClientDto clientDto) {
		Client client = new Client();
		client = CopyDtoToEntity(clientDto, client);
	
		ClientDto dto = new ClientDto(repository.save(client));

		return dto;
	}

	@Transactional
	public ClientDto update(Long id, ClientDto clientDto) {
		try {
			
			Client entity = repository.getOne(id);
			entity = CopyDtoToEntity(clientDto, entity);
			
			ClientDto dto = new ClientDto(repository.save(entity));
			return dto;
		} catch (EntityNotFoundException e) {
				throw new ResourceNotFoundException("Id not found " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);			
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation " + id);
		}

	}
	
	private Client CopyDtoToEntity(ClientDto dto, Client entity) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		return entity;
	}
}
