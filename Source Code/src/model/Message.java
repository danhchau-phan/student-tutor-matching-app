package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mainview.Utils;
import mainview.Application;
/**
 * This class models a Message (used in close bidding)
 */
public class Message extends Observable implements Model{
	
	private String id;
	private String bidId;
	private User poster;
	private String content;
	private MessageAddInfo addInfo;

	public Message(JsonNode node) {
		this.id = node.get("id").textValue();
		this.bidId = node.get("bidId").textValue();
		this.poster = new User(node.get("poster"));
		this.content = node.get("content").textValue();
		if (node.get("additionalInfo").isEmpty())
			this.addInfo = null;
		else
			this.addInfo = new MessageAddInfo(node.get("additionalInfo"));
	}

	public Message(JsonNode node, String bidId) {
		this.id = node.get("id").textValue();
		this.bidId = bidId;
		this.poster = new User(node.get("poster"));
		this.content = node.get("content").textValue();
		if (node.get("additionalInfo").isEmpty())
			this.addInfo = null;
		else
			this.addInfo = new MessageAddInfo(node.get("additionalInfo"));
	}
	
	public Message(String bidId, User poster, String content, MessageAddInfo addInfo) {
		this.bidId = bidId;
		this.poster = poster;
		this.content = content;
		this.addInfo = addInfo;
	}

	public static List<Message> getAll() {
		List<Message> messages = new ArrayList<Message> ();
		for (ObjectNode node : Model.getAll("/message")) {
			messages.add(new Message(node));
		}
		return messages;
	}

	public void addNewMessage(String content, String username) {
		this.addInfo.addNewMessage(content, username);
		this.patchMessage(content, addInfo);
		this.inform(EventType.MESSAGE_PATCH);
	}
	
	private static List<Message> getMessageArray(JsonNode node) {
		List<Message> messages = new ArrayList<Message>();
		Iterator<JsonNode> iter = node.iterator();
		while (iter.hasNext()) {
		    messages.add(new Message(iter.next()));
		}
		return messages;
	}
	
	public static Message getMessagebyId(String posterId, Bid bid) {
		List<Message> messages = bid.getMessages();
		for (Message m : messages) {
			if (m.poster.getId().equals(posterId) 
					&& m.addInfo != null
					&& m.bidId.equals(bid.getId())) {
				return m;
			}
		}
		return null;
	}
	
	public String getPosterId() {
		return this.poster.getId();
	}
	
	public static void postMessage(String bidId,
			String posterId,
			String content,
			MessageAddInfo addInfo) {
		String url = Application.rootUrl + "/message";
		
    	String jsonString = "{" +
  		"\"bidId\":\"" + bidId + "\"," +
  		"\"posterId\":\"" + posterId + "\"," +
  		"\"datePosted\":\"" + Utils.format.format(new Date()) + "\"," +
  		"\"content\":\"" + content + "\"," +
  		"\"additionalInfo\":" + addInfo.toJson() + "}";
    	
    	Model.post(url, jsonString);
	}
	
	private void patchMessage(String newContent, MessageAddInfo addInfo) {
		String url = Application.rootUrl + "/message/" + this.id;
    	
    	String jsonString = "{" +
    			"\"content\":\"" + newContent + "\"," +
    			"\"additionalInfo\":" + addInfo.toJson() + "}";
    	System.out.println(jsonString);
		Model.patch(url, jsonString);
	}

	public String toString() {
		return "Bid ID: " + bidId + '\n' + 
			"Poster: " + poster.getFullName();
	}

	public List<String> getMessageLog() {
		return this.addInfo.getMessageLog();
	}
}
