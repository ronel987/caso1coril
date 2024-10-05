package com.example.casotipo.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.casotipo.models.Empleados;
import com.example.casotipo.models.Tipodecambio;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.casotipo.config.CrmInitialization;

import com.zoho.crm.library.api.response.APIResponse;
import com.zoho.crm.library.api.response.BulkAPIResponse;
import com.zoho.crm.library.common.ZCRMConfigUtil;
import com.zoho.crm.library.crud.ZCRMModule;
import com.zoho.crm.library.crud.ZCRMRecord;
import com.zoho.crm.library.exception.ZCRMException;
import com.zoho.oauth.common.ZohoOAuthException;

@Service
public class TipoService {
	JSONArray articulosSubForm = new JSONArray();

	// obtiene de sap
	public String getTokenOauthSap() {
		String baseUrl = "https://apigateway.sap.comsatel.com.pe:8094/api/Login";
		JSONObject respuesta = new JSONObject(
				"{\"username\":\"comsatel\",\"password\":\"Coms@96#tele\",\"key\":\"ComNb680QvMWShSJxChv2AzGHay8p4Mlcov9wvKt\"}");

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> httpEntity = new HttpEntity<Object>(respuesta.toString(), headers);

		ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, httpEntity, String.class);
		System.out.println("TOKEN: " + response.getBody());
		return response.getBody();

	}
	
	// obtiene desde sap
	public List<Empleados> obtenerEmpleados() {
		String url = "http://servicios-sap.comsatel.com.pe:8000/WS_EXX_COM_VIEWTEST/Employeer.xsodata/get?$format=json";
		ResponseEntity<Empleados> response = invocarServicioSap(url);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		List<Empleados> respuesta = objectMapper.convertValue(response.getBody().getResults(),
				new TypeReference<List<Empleados>>() {
				});
		;
		return respuesta;
	}
	

	// tipocambio llega cargada desde sap y actualiza en CMR:
	public Map<String, Object> cargar(Tipodecambio tipocambio) {
		new CrmInitialization().produccion();		
		List<Empleados> empleados = this.obtenerEmpleados();		
		Map<String, Object> respuesta = new HashMap<String, Object>();
		ZCRMModule module = ZCRMModule.getInstance("rden_de_venta"); // nombre del modulo
		long idtipocambio = Long.MIN_VALUE;
		try {
			idtipocambio = Long.parseLong(tipocambio.getIdZoho());
		} catch (Exception e) {
			try { 
				BulkAPIResponse respuestaBusqueda = module
						.searchByCriteria("ID_tipocambio_de_Venta:equals:" + tipocambio.getIdtipocambio());
				System.out.println(respuestaBusqueda.getData().size());
				ZCRMRecord tipocambioRecordTmp = (ZCRMRecord) respuestaBusqueda.getData().get(0); 
																								
				idtipocambio = tipocambioRecordTmp.getEntityId();
			} catch (ZCRMException e1) {
				e1.printStackTrace();
			}
		}
		// si vas actualizar primero necesitas setear el id del obj a actualizar:
		tipocambio.setIdZoho(idtipocambio + "");
		// obtiene el registro-objeto del crm
		ZCRMRecord tipocambioRecord = ZCRMRecord.getInstance((String) "rden_de_venta", (Long) idtipocambio);
		HashMap<String, Object> datos = new HashMap<String, Object>();		
		datos.put("morigen", tipocambio.getMorigen());
		datos.put("mdestino", tipocambio.getMdestino());
		datos.put("montotc", tipocambio.getMontotc());		
		
		datos.put("tipocambio",tipocambio.getTipocambio()); // agrego el listado como un solo atributo
		datos.entrySet().removeIf(e -> e.getValue() == null);
		datos.entrySet().removeIf(e -> e.getValue().toString().equalsIgnoreCase("null"));
		// setea en tipocambioRecord:
		datos.entrySet().stream().forEach(e -> tipocambioRecord.setFieldValue((String) e.getKey(), e.getValue()));

		try {
			// linea principal:actualiza el tipocambioRecord
			APIResponse response = tipocambioRecord.update();			
			respuesta.put("codigo", "200");
			respuesta.put("mensaje", response.toString());
			return respuesta;
		} catch (ZCRMException e) {
			respuesta.put("codigo", "500");
			respuesta.put("mensaje", "Error en tipocambio: " + e.getMessage());
			e.printStackTrace();
			return respuesta;
		}

	}

}
