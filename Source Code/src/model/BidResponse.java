package model;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * This class models a response to an open bid
 */
public class BidResponse {
	private String bidderId,
				bidderName,
				rate,
				duration,
				timeDate,
				sessionsPerWeek,
				additionalInfo;
	private boolean hasFreeLesson; 
	
	public BidResponse(String bidderId, 
			String bidderName, 
			String rate, 
			String rateType, 
			String duration, 
			String timeDate, 
			String sessionsPerWeek, 
			String additionalInfo, 
			boolean hasFreeLesson) {
		this.bidderId = bidderId;
		this.bidderName = bidderName;
		this.rate = rate + " " + rateType;
		this.duration = duration;
		this.timeDate = timeDate;
		this.sessionsPerWeek = sessionsPerWeek;
		this.additionalInfo = additionalInfo;
		this.hasFreeLesson = hasFreeLesson;
	}
	
	public BidResponse(JsonNode node) {
		this.bidderId = node.get("bidderId").textValue();
		this.bidderName = node.get("bidderName").textValue();
		this.rate = node.get("rate").textValue();
		this.duration = node.get("duration").textValue();
		this.timeDate = node.get("timeDate").textValue();
		this.sessionsPerWeek = node.get("sessionsPerWeek").textValue();
		this.additionalInfo = node.get("additionalInfo").textValue();
		this.hasFreeLesson = node.get("hasFreeLesson").booleanValue();
	}

	public String toJson() {
		String jsonString = "{" +
				"\"bidderId\":\"" + bidderId + "\"," +
				"\"bidderName\":\"" + bidderName + "\"," +
		  		"\"rate\":\"" + rate + "\"," +
		  		"\"duration\":\"" + duration + "\"," +
		  		"\"timeDate\":\"" + timeDate + "\"," +
		  		"\"sessionsPerWeek\":\"" + sessionsPerWeek + "\"," +
		  		"\"additionalInfo\":\"" + additionalInfo + "\"," +
		  		"\"hasFreeLesson\":\"" + hasFreeLesson + "\"" + "}";
		return jsonString;
	}
	
	public String toString() {
		return 
				"Bidder Name:" + bidderName + '\n' +
		  		"Rate:" + rate + '\n'+
		  		"Duration:" + duration + '\n'+
		  		"Time & Date:" + timeDate + '\n' +
		  		"Sessions/week:" + sessionsPerWeek + '\n' +
		  		"Additional Info:" + additionalInfo + '\n' +
		  		"FreeLesson:" + (hasFreeLesson? "yes" : "no") + '\n';
	}

	public String getBidderId() {
		return bidderId;
	}
	
	public String getRate() {
		return rate;
	}

	public String getDuration() {
		return duration;
	}

	public String getTimeDate() {
		return timeDate;
	}

	public String getSessionsPerWeek() {
		return sessionsPerWeek;
	}

	public String getAddInfo() {
		return additionalInfo;
	}

	public boolean hasFreeLesson() {
		return hasFreeLesson;
	}
}
