package model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class models the message's additional info
 */
public class MessageAddInfo {
	
	private List<String> messageLog;
	
	public MessageAddInfo(JsonNode node) {
		assert (node.get("messageLog").isArray());
		this.messageLog = getMessageLog(node.get("messageLog").iterator());
	}
	
	
	public MessageAddInfo(String content, String userName) {
		this.messageLog = new ArrayList<String>();
		this.addNewMessage(content, userName);
	}

	private List<String> getMessageLog(Iterator<JsonNode> iter) {
		List<String> list = new ArrayList<String>();
		while (iter.hasNext()) {
			String next = iter.next().toString();
		    list.add(next);
			System.out.println(next);
		}
		return list;
	}
	
	public void addNewMessage(String content, String userName) {
		this.messageLog.add("\"" + userName + " : " + content + "\"");
	}

	public List<String> getMessageLog() {
		return this.messageLog;
	}

	public String toJson() {
		String json = "{\"messageLog\":[";
		String comma = "";
		for (String l : this.messageLog) {
			json = json + comma + l;
			comma = ",";
		}

		json = json + "]}";
		return json;
	}
}
