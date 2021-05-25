package model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This class models a Subject
 */
public class Subject extends Observable implements Model {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    
    static Map<String, String> idToName = new HashMap<String, String>();
    
    public Subject (String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
   

    public Subject() {}

    public Subject(JsonNode node) {
    	this.id = node.get("id").textValue();
    	this.name = node.get("name").textValue();
    	
    	this.description = (node.get("description") == null ? null : node.get("description").textValue());
	}

	@Override
    public String toString() {
        return "Subject name : " + name + '\n'
        		+"Subject description : " + (description == null ? "":description);
    }

	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	public static String[] getAllSubjectsNames() {
		Subject.mapIdToName();
		return idToName.values().toArray(new String[0]);
	}
	
	public static String getSubjectName(String id) {
		return idToName.get(id);
	}
	
	public static String getSubjectId(String name) {
		for (String s : idToName.keySet()) {
			if (idToName.get(s).equals(name)) 
				return s;
		}
		return null;
	}
	private static void mapIdToName() {
		
		for (ObjectNode node: Model.getAll("/subject")) {
  	      Subject s = new Subject(node);
  	      if (!idToName.containsKey(s.id)) {
  	    	  idToName.put(s.id, s.name+ " " + s.description);
  	      }
  	    };

	    
	}

	
}

