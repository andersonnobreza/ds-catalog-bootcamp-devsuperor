package com.devsuperior.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.service.CategoryService;

import sun.print.resources.serviceui;

@RestController
@RequestMapping(value="/categories")
public class CategoryResource {
	
	@Autowired
	CategoryService Service;
	
	@GetMapping
	public ResponseEntity<List<CategoryDto>> findAll(){
		
		List<CategoryDto> list = Service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> findAll(@PathVariable Long id){
		
		CategoryDto dto= Service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDto> insert(@RequestBody CategoryDto categoriaDto){
		
		CategoryDto dto = Service.insert(categoriaDto);
		
	
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
	
		//response.setHeader("Location", uri.toASCIIString());
	
		return ResponseEntity.created(uri).body(dto);
	}
}
