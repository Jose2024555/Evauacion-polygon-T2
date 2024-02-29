package com.cibertec.assessment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.assessment.beans.PolygonBean;
import com.cibertec.assessment.service.PolygonService;

@RestController
@RequestMapping("/polygon")
public class PolygonController {
	
	@Autowired
	PolygonService polygonService;
	
	@GetMapping
	public ResponseEntity<List<PolygonBean>> list() {
		
		return new ResponseEntity<>(polygonService.list(), HttpStatus.OK);	
	}

}
