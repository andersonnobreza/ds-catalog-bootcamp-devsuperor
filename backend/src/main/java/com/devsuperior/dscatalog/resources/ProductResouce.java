package com.devsuperior.dscatalog.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.service.ProductService;
	
@RestController
@RequestMapping(value="/products")
public class ProductResouce {
	
	@Autowired
	ProductService service;
	
	@GetMapping
	public ResponseEntity<Page<ProductDto>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy
			){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<ProductDto> list = service.findAllPaged(pageRequest);
		
		return ResponseEntity.ok().body(list);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> findById(@PathVariable Long id){
		ProductDto dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<CategoryDto> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
