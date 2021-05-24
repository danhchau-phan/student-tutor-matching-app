package model;

public class ContractCessationInfo {
    private String secondPartyId;
    private int duration;
	private String competency;
	private String hourPerLesson;
	private String sessionsPerWeek;
	private String rate;

    public String toJson() {
        return "{\"secondPartyId\":\"" + this.secondPartyId + "\"," +
        "\"duration\":\"" + this.duration + "\"," +
        "\"competency\":\"" + this.competency + "\"," +
        "\"hourPerLesson\":\"" + this.hourPerLesson + "\"," +
        "\"sessionsPerWeek\":\"" + this.sessionsPerWeek + "\"," +
        "\"rate\":\"" + this.rate + "\"}" ;
    }
}
