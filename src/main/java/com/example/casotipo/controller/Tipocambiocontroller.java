package com.example.casotipo.controller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.example.casotipo.service.TipoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.casotipo.models.Tipodecambio;
import com.example.casotipo.service.TipoService;
import com.zoho.oauth.client.ZohoOAuthClient;
import com.zoho.oauth.common.ZohoOAuthException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class Tipocambiocontroller {
	@Autowired
	TipoService ordenService;
	
	@GetMapping(value ="/hola")
	public String enviarOrden(){		
		return "o2";		
	}
	//busqueda desde postman:
	// comsatel.m4g.site:8089/tipodecambio?moneda=USD&fecha=2024-09-18T00:00:00
	@GetMapping(value ="/tipodecambio")
	public ResponseEntity<Map<String,Object>> solicitarTipodecambio(@RequestParam(required = false) String moneda ,@RequestParam String fecha)
	  throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders(); 			
		headers.set("Authorization", "Bearer " + ordenService.getTokenOauthSap());
		String currencyFilter = moneda != null ? moneda : "USD";
		String url=String.format(
		"https://apigateway.sap.comsatel.com.pe:8094/sap/ViewService/company=1/AUX.xsodata/getCurrency?$format=json&$filter=Currency eq '%s' and RateDate eq datetime'%s'",
		currencyFilter,fecha);				
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = new HttpEntity<>(headers);		
		ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.GET,entity,String.class);		
		ObjectMapper mapper= new ObjectMapper();
		JsonNode jsonResponseBody = mapper.readTree(response.getBody());		
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("status", response.getStatusCode());
		responseBody.put("body", jsonResponseBody);		
		return ResponseEntity.ok(responseBody);		
	}
	// desde postman: comsatel.m4g.site:8089/CRM/RegistrarTipo
	@PostMapping("/CRM/RegistrarTipo")
	public ResponseEntity<?> registrarTipo(@RequestBody Tipodecambio tipocam){
		System.out.println(tipocam.toString());
		// en CMR:
		Map<String,Object> respuestaCarga=ordenService.cargar(tipocam);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		if (!respuestaCarga.get("codigo").toString().equalsIgnoreCase("200")) {
			respuesta.put("codigo", 500);
			respuesta.put("mensaje", respuestaCarga.get("mensaje"));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respuesta.put("codigo", 200);
		respuesta.put("mensaje", "Se registró correctamente");
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);		
	}
	// desde postman: comsatel.m4g.site:8089/CRM/ActualizarTipo
	@PutMapping("/CRM/ActualizarTipo")
	public ResponseEntity<?> actualizarTipo(@RequestBody Tipodecambio tipocam){
		System.out.println(tipocam.toString());
		//actualiza en CMR:
		Map<String,Object> respuestaCarga=ordenService.cargar(tipocam);
		Map<String, Object> respuesta=new HashMap<String, Object>();
		if (!respuestaCarga.get("codigo").toString().equalsIgnoreCase("200")) {
			respuesta.put("codigo", 500);
			respuesta.put("mensaje", respuestaCarga.get("mensaje"));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respuesta.put("codigo", 200);
		respuesta.put("mensaje", "Se actualizó correctamente");
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);		
	}
  
   
}
