package com.example.casotipo.config;

import java.util.HashMap;

import com.zoho.crm.library.setup.restclient.ZCRMRestClient;



public class CrmInitialization {

	public void produccion() {
		HashMap<String, String> zcrmConfigurations = new HashMap<String, String>();
		zcrmConfigurations.put("minLogLevel", "OFF");
		zcrmConfigurations.put("currentUserEmail", "infra@comsatel.com.pe");
		zcrmConfigurations.put("client_id", "1000.1ACFCPMFK1BG7R8JVP7G45J9WB6NTP");
		zcrmConfigurations.put("client_secret", "75d24687a1ed6d3fe3e500a37c16ef9ec73c2102b6");
		zcrmConfigurations.put("redirect_uri", "https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class", "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");
		//zcrmConfigurations.put("oauth_tokens_file_path","C:\\Users\\User\\Documents\\Comsatel\\comsatel-SIGO-ordenventa-aprvd\\src\\main\\java\\com\\comsatel\\ordenVenta\\path_to_tokens\\socioNegocio.properties");
		zcrmConfigurations.put("oauth_tokens_file_path", "/home/ec2-user/orden85/socioNegocio.properties");// optional
		zcrmConfigurations.put("domainSuffix", "com");// optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType", "Production");// Production->www(default), Development->developer,// Sandbox->sandbox(optional)
		// zcrmConfigurations.put("scope","ZohoCRM.modules.ALL,ZohoCRM.settings.ALL,Aaaserver.profile.read");
		try {
			ZCRMRestClient.initialize(zcrmConfigurations);
			System.out.println("**********************************************************************************");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sandbox() {
		// **** Regenerar el token cada vez que se cambie de entorno sandbox ***** //
		HashMap<String, String> zcrmConfigurations = new HashMap<String, String>();
		zcrmConfigurations.put("minLogLevel", "OFF");
		zcrmConfigurations.put("currentUserEmail", "infra@comsatel.com.pe");
		zcrmConfigurations.put("client_id", "1000.1ACFCPMFK1BG7R8JVP7G45J9WB6NTP");
		zcrmConfigurations.put("client_secret", "75d24687a1ed6d3fe3e500a37c16ef9ec73c2102b6");
		zcrmConfigurations.put("redirect_uri", "https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class", "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");
		//zcrmConfigurations.put("oauth_tokens_file_path","C:\\Users\\User\\Documents\\GitProduccion\\ordenVenta\\src\\main\\java\\com\\comsatel\\ordenVenta\\path_to_tokens\\socioNegocioSandbox.properties");// optional
		zcrmConfigurations.put("oauth_tokens_file_path", "/home/ec2-user/orden85/socioNegocio.properties");// optional
		zcrmConfigurations.put("domainSuffix", "com");// optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType", "Sandbox");// Production->www(default), Development->developer, Sandbox->sandbox(optional)
		// zcrmConfigurations.put("scope","ZohoCRM.modules.ALL,ZohoCRM.settings.ALL,Aaaserver.profile.read");
		try {
			ZCRMRestClient.initialize(zcrmConfigurations);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
