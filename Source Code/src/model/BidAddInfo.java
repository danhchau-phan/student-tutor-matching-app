package model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
/**
 * This class models the Bid's additional info
 */
public class BidAddInfo {
	private MatchRequest matchRequest;
	private List<BidResponse> bidResponse = new ArrayList<BidResponse>();
	
	public BidAddInfo(String competency, String hourPerLesson, String sessionPerWeek, String rate, String rateType) {
		if (!isNumeric(hourPerLesson) || !isNumeric(sessionPerWeek) 
				|| !isNumeric(rate))
			throw new NumberFormatException();
		matchRequest = new MatchRequest(competency, hourPerLesson, sessionPerWeek, rate, rateType);
	}

	public static boolean isNumeric(String s) {
		if (s == null) {
	        return true;
	    }
	    try {
	        double d = Double.parseDouble(s);
	    } catch (NumberFormatException e) {
	        return false;
	    }
		return true;
	}

	/** Getter for match Request*/
	public String getValidCompetency() {
		return this.matchRequest.getCompetency();
	}

	public String getValidHourPerLesson() {
		return this.matchRequest.getHourPerLesson();
	}

	public String getValidRate() {
		return this.matchRequest.getRate();
	}

	public String getValidSessionPerWeek() {
		return this.matchRequest.getSessionsPerWeek();
	}
	
	public String getMatchRequest() {
		return this.matchRequest.toJson();
	}
	
	public String getBidResponseJson() {
		String jsonString = "[";
		String comma = "";
		for (BidResponse r : bidResponse) {
			jsonString = jsonString + comma + r.toJson();
			comma = ",";
		}
		jsonString = jsonString + "]";
		return jsonString;
	}
	
	public BidAddInfo(JsonNode node) {
		this.matchRequest = new MatchRequest(node.get("matchRequest"));  
		assert (node.get("bidResponse").isArray());
		this.bidResponse = getBidResponse(node.get("bidResponse").iterator());
	}
	
	private List<BidResponse> getBidResponse(Iterator<JsonNode> iter) {
		List<BidResponse> list = new ArrayList<BidResponse>();
		while (iter.hasNext()) {
		    list.add(new BidResponse(iter.next()));
		}
		return list;
	}
	
	public String toJson() {
		String jsonString = "{" +
		  		"\"matchRequest\":" + matchRequest.toJson() + "," +
		  		"\"bidResponse\":" + getBidResponseJson() + "}";
		return jsonString;
	}
	
	public String toString() {
		String str = matchRequest.toString();
		
		return str;
	}

	public List<BidResponse> getResponses() {
		List<BidResponse> responses = new ArrayList<BidResponse>();
		for (BidResponse r : this.bidResponse) 
			responses.add(r);
		return responses;
	}

	public void addResponse(BidResponse r) {
		this.bidResponse.add(r);
	}
	
	public void addResponse(BidResponse r, String tutorId) {
		for (int i = 0; i < this.bidResponse.size(); i++ ) {
			if (this.bidResponse.get(i).getBidderId().equals(tutorId))
				this.bidResponse.set(i, r);
		}
	}
	
	public void resetResponse(List<BidResponse> bR) {
		this.bidResponse = bR;
	}
	public int getPreferredCompetency() {
		return Integer.parseInt(this.matchRequest.getCompetency());
	}
	
}
