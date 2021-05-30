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

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repository.CategoryRepository;
import com.devsuperior.dscatalog.service.exception.DatabaseException;
import com.devsuperior.dscatalog.service.exception.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDto> findAllPaged(PageRequest pageRequest){
			
		return repository.findAll(pageRequest)
				.map(x -> new CategoryDto(x));

	}
	
	public CategoryDto findById(Long id) {
		  Optional<Category> obj = repository.findById(id);
		  Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		  return new CategoryDto(entity);
	
	}
	
	@Transactional
	public CategoryDto insert(CategoryDto categoryDto) {
		Category category = new Category();
		category.setName(categoryDto.getName());
	
		CategoryDto dto = new CategoryDto(repository.save(category));

		return dto;
	}

	@Transactional
	public CategoryDto update(Long id, CategoryDto categoriaDto) {
		try {
			
			Category entity = repository.getOne(id);
			entity.setName(categoriaDto.getName());
			CategoryDto dto = new CategoryDto(repository.save(entity));
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
}
