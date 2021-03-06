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
import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repository.CategoryRepository;
import com.devsuperior.dscatalog.repository.ProductRepository;
import com.devsuperior.dscatalog.service.exception.DatabaseException;
import com.devsuperior.dscatalog.service.exception.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	ProductRepository repository;
	
	@Autowired
	CategoryRepository repositoryCategory;
	
	
	@Transactional(readOnly = true)
	public Page<ProductDto> findAllPaged(PageRequest pageRequest){
		return repository.findAll(pageRequest)
				.map(x -> new ProductDto(x));
	}
	@Transactional(readOnly = true)
	public ProductDto findById(Long id){
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDto(entity, entity.getCategories());
	}
	
	
	@Transactional
	public ProductDto insert(ProductDto dto){
		Product entity = new Product();
		entity = CopyDtoToEntity(dto,entity);
		 
		return new ProductDto(repository.save(entity));
	}
	
	
	@Transactional
	public ProductDto update(Long id, ProductDto dto) {
		try {

			Product entity = repository.getOne(id);
			entity = CopyDtoToEntity(dto,entity);
			 
			ProductDto productDto = new ProductDto(repository.save(entity));
			return productDto;
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

	private Product CopyDtoToEntity(ProductDto dto, Product entity) {
		entity.setDate(dto.getDate());
		entity.setDecription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		
		for(CategoryDto categoryDto : dto.getCategories()) {
			Category category = repositoryCategory.getOne(categoryDto.getId());
			entity.getCategories().add(category);
		}
		
		return entity;
	}
}
