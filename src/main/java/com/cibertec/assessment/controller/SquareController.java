package com.cibertec.assessment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cibertec.assessment.beans.SquareBean;
import com.cibertec.assessment.model.Square;
import com.cibertec.assessment.service.SquareService;


@RestController
@RequestMapping("/square")
public class SquareController {
	
	@Autowired
	SquareService squareService;
	
	@GetMapping
	public ResponseEntity<List<SquareBean>> list() {
		
		return new ResponseEntity<>(squareService.list(), HttpStatus.OK);	
	}

	@PostMapping
	public ResponseEntity<Square> add(@RequestBody Square s) {
		return new ResponseEntity<>(squareService.add(s), HttpStatus.CREATED);	
	}


	
}
