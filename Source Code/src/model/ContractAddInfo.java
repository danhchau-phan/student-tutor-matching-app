package model;

import com.fasterxml.jackson.databind.JsonNode;
/**
 * This class models the Contract additional info, which contains the signing status of the two parties
 */
public class ContractAddInfo {
	
	private boolean firstPartySigned,
		secondPartySigned;
	private int duration;
	private String competency;
	private String hourPerLesson;
	private String sessionsPerWeek;
	private String rate;

	public ContractAddInfo(boolean firstPartySigned, boolean secondPartySigned, int duration,
		String competency, String hourPerLesson, String sessionsPerWeek, String rate) {
		this.firstPartySigned = firstPartySigned;
		this.secondPartySigned = secondPartySigned;
		this.duration = duration;
		this.competency = competency;
		this.hourPerLesson = hourPerLesson;
		this.sessionsPerWeek = sessionsPerWeek;
		this.rate = rate;
	}
	
	public ContractAddInfo(JsonNode node) {
		this.firstPartySigned = Boolean.parseBoolean(node.get("firstPartySigned").textValue());
		this.secondPartySigned = Boolean.parseBoolean(node.get("secondPartySigned").textValue());
		this.duration = Integer.parseInt(node.get("duration").textValue());
		this.competency = node.get("competency").textValue();
		this.hourPerLesson = node.get("hourPerLesson").textValue();
		this.sessionsPerWeek = node.get("sessionsPerWeek").textValue();
		this.rate = node.get("rate").textValue();
	}
	
	public String toJson() {

		 return "{\"firstPartySigned\":\"" + this.firstPartySigned + "\"," +
		 		"\"secondPartySigned\":\"" + this.secondPartySigned + "\"," +
		 		"\"duration\":\"" + this.duration + "\"," +
		 		"\"competency\":\"" + this.competency + "\"," +
		 		"\"hourPerLesson\":\"" + this.hourPerLesson + "\"," +
		 		"\"sessionsPerWeek\":\"" + this.sessionsPerWeek + "\"," +
		 		"\"rate\":\"" + this.rate + "\"}" ;
	}
	
	public String toString() {
		return "First party: " + (this.firstPartySigned? "signed":"") +
				"Second party: " + (this.secondPartySigned? "signed":"") +
				"Duration: " + String.valueOf(duration) +
				"Competency: " + this.competency +
				"Hour per lesson: " + this.hourPerLesson +
				"Sessions per week: " + this.sessionsPerWeek +
				"Rate: " + this.rate
				;
	}
	

	public boolean isFirstPartySigned() {
		return firstPartySigned;
	}
	public void firstPartySign(boolean firstPartySigned) {
		this.firstPartySigned = firstPartySigned;
	}
	public boolean isSecondPartySigned() {
		return secondPartySigned;
	}
	public void secondPartySign(boolean secondPartySigned) {
		this.secondPartySigned = secondPartySigned;
	}
	
	public int getContractDuration() {
		return duration;
	}
	
	public int getCompetency() {
		return Integer.parseInt(competency);
	}
}
