package com.example.casotipo.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
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
public class Tipodecambio {
	
	
	 @JsonProperty(value="morigen")
	private String morigen;
	 @JsonProperty(value="mdestino")
	private String mdestino;  
	 @JsonProperty(value="montotc")
	private String montotc;
	 @JsonProperty(value="tipocambio")
	private Double tipocambio;
	 @JsonProperty(value="idempleado")
	private Integer idempleado;
	

	
	

	
	


	
	
}
