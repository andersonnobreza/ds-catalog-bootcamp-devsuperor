package com.devsuperior.dscatalog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repository.CategoryRepository;
import com.devsuperior.dscatalog.service.exception.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDto> findAll(){
			
		return categoryRepository.findAll().stream().map(x -> new CategoryDto(x)).collect(Collectors.toList());

	}
	
	public CategoryDto findById(Long id) {
		  Optional<Category> obj = categoryRepository.findById(id);
		  Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		  return new CategoryDto(entity);
	
	}
	
	@Transactional
	public CategoryDto insert(CategoryDto categoryDto) {
		Category category = new Category();
		category.setName(categoryDto.getName());
		
		CategoryDto dto = new CategoryDto(categoryRepository.save(category));

		return dto;
	}
}
