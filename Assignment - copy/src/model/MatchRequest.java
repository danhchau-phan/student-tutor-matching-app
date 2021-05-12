package model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class models a Match Request which will be placed into Bid's additional info
 */
public class MatchRequest {
	private String competency, 
					hourPerLesson,
					sessionsPerWeek,
					rate;
	public MatchRequest(String competency, String hourPerLesson, String sessionsPerWeek, String rate, String rateType) {
		this.competency = competency;
		this.hourPerLesson = hourPerLesson;
		this.sessionsPerWeek = sessionsPerWeek;
		this.rate = rate + " " + rateType;
	}
	public MatchRequest(JsonNode node) {
		this.competency = node.get("competency").textValue();
		this.hourPerLesson = node.get("hourPerLesson").textValue();
		this.sessionsPerWeek = node.get("sessionsPerWeek").textValue();
		this.rate = node.get("rate").textValue();
	}
	
	public String getCompetency() {
		return this.competency;
	}
	
	public String getHourPerLesson() {
		return this.hourPerLesson;
	}
	
	public String getSessionsPerWeek() {
		return this.sessionsPerWeek;
	}
	
	public String getRate() {
		return this.rate;
	}
	
	public String toJson() {
		String jsonString = "{" +
		  		"\"competency\":\"" + competency + "\"," +
		  		"\"hourPerLesson\":\"" + hourPerLesson + "\"," +
		  		"\"sessionsPerWeek\":\"" + sessionsPerWeek + "\"," +
		  		"\"rate\":\"" + rate + "\"" + "}";
		return jsonString;
	}
	
	public String toString() {
		String str = "Preffered Competency: " + competency + '\n' +
				"Preffered hour/lesson: " + hourPerLesson + '\n' +
				"Preffered sessions/week: " + sessionsPerWeek + '\n' +
				"Preffered rate: " + rate;
		return str;
	}
}
