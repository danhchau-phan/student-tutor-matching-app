package model;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mainview.Application;

public interface Model {
	
	public static HttpResponse<String> get(String url) {
		HttpResponse<String> response;
		HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest
	      .newBuilder(URI.create(url))
	      .setHeader("Authorization", Application.myApiKey)
	      .GET()
	      .build();
	    try {
	    	response = client.send(request, HttpResponse.BodyHandlers.ofString());    	
	    	return response;
	    } catch (Exception e) {
	    	return null;
	    }
	    
	}
	
	public static List<ObjectNode> getAll(String extension) {
		List<ObjectNode> models = new ArrayList<ObjectNode>();
		String url = Application.rootUrl + extension;
		
		 
		HttpResponse<String> response = Model.get(url);
		
		if (response.statusCode() != 200)
 	    	return null;
    	ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		ObjectNode[] jsonNodes;
		try {
			jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
			for (ObjectNode node: jsonNodes) {
	    	      models.add(node);
	    	    };
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	
		return models;
	};
	
	public static ObjectNode get(String extension, String id) {
    	String url = Application.rootUrl + extension + id;
    	HttpClient client = HttpClient.newHttpClient();
    	HttpRequest request = HttpRequest
    	        .newBuilder(URI.create(url))
    	        .setHeader("Authorization", Application.myApiKey)
    	        .GET()
    	        .build();
    	  	
	  	try {
	  		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	  		if (response.statusCode() != 200) {
	  			System.out.println(response.body());
	 	    	return null;
	  		}
//	    	ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
	    	ObjectNode jsonNode = new ObjectMapper().readValue(response.body(), ObjectNode.class);
	    	return jsonNode;
	  	} catch (IOException | InterruptedException e) {
	  		e.printStackTrace();
	  	}
		return null;
    }
	
	
	public static void post(String url, String json) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create(url))
    	        .setHeader("Authorization", Application.myApiKey)
    	        .header("Content-Type","application/json")
    	        .POST(HttpRequest.BodyPublishers.ofString(json))
    	        .build();
    	  	
	  	try {
	  		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	  		System.out.println(response.statusCode());
	  		System.out.println(response.body());
	  	} catch (IOException e) {
	  		e.printStackTrace();
	  	} catch (InterruptedException e) {
	  		e.printStackTrace();
	  	}
	}

	public static void delete(String url) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
						.setHeader("Authorization", Application.myApiKey)
				        .uri(URI.create(url))
				        .header("Content-Type", "application/json")
				        .DELETE()
				        .build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.statusCode() + response.body());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void patch(String url, String json) {
		HttpClient client = HttpClient.newHttpClient();
    	HttpRequest request = HttpRequest.newBuilder(URI.create(url))
    	        .setHeader("Authorization", Application.myApiKey)
    	        .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
    	        .header("Content-Type","application/json")
    	        .build();
    	
    	try {
	  		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.statusCode() + response.body());
		} catch (IOException e) {
	  		e.printStackTrace();
	  	} catch (InterruptedException e) {
	  		e.printStackTrace();
	  	}
	}
}
