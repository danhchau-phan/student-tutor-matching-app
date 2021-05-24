package model;

import com.fasterxml.jackson.databind.JsonNode;

public class ContractCessationInfo {
    private String secondPartyId;
    private int duration;
	private String competency;
	private String hourPerLesson;
	private String sessionsPerWeek;
	private String rate;

    public ContractCessationInfo(String secondPartyId,
        int duration,
	    String competency,
	    String hourPerLesson,
	    String sessionsPerWeek,
	    String rate) {
        this.secondPartyId = secondPartyId;
        this.duration = duration;
        this.competency = competency;
        this.hourPerLesson = hourPerLesson;
        this.sessionsPerWeek = sessionsPerWeek;
        this.rate = rate;
    }

    public ContractCessationInfo(JsonNode node) {
        this.secondPartyId = node.get("secondPartyId").textValue();
        this.duration = Integer.parseInt(node.get("duration").textValue());
		this.competency = node.get("competency").textValue();
		this.hourPerLesson = node.get("hourPerLesson").textValue();
		this.sessionsPerWeek = node.get("sessionsPerWeek").textValue();
		this.rate = node.get("rate").textValue();
    }
    
    public String toJson() {
        return "{\"secondPartyId\":\"" + this.secondPartyId + "\"," +
        "\"duration\":\"" + this.duration + "\"," +
        "\"competency\":\"" + this.competency + "\"," +
        "\"hourPerLesson\":\"" + this.hourPerLesson + "\"," +
        "\"sessionsPerWeek\":\"" + this.sessionsPerWeek + "\"," +
        "\"rate\":\"" + this.rate + "\"}" ;
    }

    /**
     * Create ContractAdditionalInfo. Used when contract is reused
     */
    public ContractAddInfo createContractAddInfo() {
        return new ContractAddInfo(false, false, duration, competency, hourPerLesson, sessionsPerWeek, rate);
    }

    public String getSecondPartyId() {
        return secondPartyId;
    }
}
