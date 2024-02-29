package com.cibertec.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Square {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
	private int npoints;	
	private String xPoints;// se debe guardar un array de 4 puntos como string	
	private String yPoints; // se debe guardar un array de 4 puntos como string
	
	private String polygons; // se debe guardar un array con los poligonos que intercepte


}
	

