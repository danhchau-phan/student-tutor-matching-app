package model;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mainview.Application;
import mainview.Utils;

/**
 * Class that models a Contract
 */
public class Contract extends Observable implements Model {
	private String id;
	private User firstParty, secondParty;
	private Subject subject;
	private Date dateCreated, dateSigned, expiryDate;
	
	private ContractAddInfo addInfo;
	private ContractCessationInfo cessationInfo;
	private static final int YEAR_IN_MILLIS = 3600 * 24 * 365 * 1000;
	public static final int DEFAULT_CONTRACT_DURATION = 6;
	public static final int MAX_CONTRACT_VIEWED = 5;

	private static final Date ADD_MONTH(Date now, int months) {
		Calendar c = Calendar.getInstance();
        c.setTime(now);
		c.add(Calendar.MONTH, months);
		return c.getTime();
	}
	
	/**
	 * For testing purpose
	 * @param now
	 * @param minutes
	 * @return
	 */
	private static final Date ADD_MINUTES(Date now, int minutes) {
		Calendar c = Calendar.getInstance();
        c.setTime(now);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}
	
	public Contract(JsonNode node) {
		this.id = node.get("id").textValue();
		this.firstParty = new User(node.get("firstParty"));
		this.secondParty = new User(node.get("secondParty"));
		this.subject = new Subject(node.get("subject"));
		this.dateCreated = Utils.formatDate(node.get("dateCreated").textValue());
		this.dateSigned = Utils.formatDate(node.get("dateSigned").textValue());
		this.expiryDate = Utils.formatDate(node.get("expiryDate").textValue());
		
		this.addInfo = (node.get("additionalInfo").isEmpty()? null : new ContractAddInfo(node.get("additionalInfo")));
	}
	
	public Contract() {
    }

    public void postContract(String firstPartyId,
			String secondPartyId,
			String subjectId,
			ContractAddInfo addInfo) {
		String url = Application.rootUrl + "/contract";
		String jsonString = "{" +
		  		"\"firstPartyId\":\"" + firstPartyId + "\"," +
		  		"\"secondPartyId\":\"" + secondPartyId + "\"," +
				"\"subjectId\":\"" + subjectId + "\"," +
		  		"\"dateCreated\":\"" + Utils.format.format(new Date()) + "\"," +
		  		"\"expiryDate\":\"" + Utils.format.format(new Date(System.currentTimeMillis() + YEAR_IN_MILLIS)) + "\"," +
		  		"\"paymentInfo\":{}," +
		  		"\"lessonInfo\":{}," +
		  		"\"additionalInfo\":" + addInfo.toJson() + "}";
		    	
		Model.post(url, jsonString);
		this.inform(EventType.CONTRACT_CREATED);
	}	
    /**
     * Finalize contract expiry date and sign contract
     */
	public void signContract() {
		this.addInfo.firstPartySign(true);
		this.addInfo.secondPartySign(true);
		
		Date now = new Date();
//		setContractExpiryDate(ADD_MONTH(now, getContractDuration()));
		////////// FOR TESTING ///////////
		setContractExpiryDate(ADD_MINUTES(now, getContractDuration())); 
		
		// sign contract 
		String url = Application.rootUrl + "/contract/" + this.id + "/sign";
		String jsonString = "{" +
    	  		"\"dateSigned\":\"" + Utils.format.format(now) + "\"}";
		Model.post(url, jsonString);
		
		this.inform(EventType.CONTRACT_SIGN);
	}

	
	/**
	 * Sets contract expiry date before signing
	 * @param expiryDate
	 */
	private void setContractExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
		patchContract();
	}

	public static List<Contract> getAllContractsAsFirstParty(String userId) {
		List<Contract> allContracts = new ArrayList<Contract>();
		for (ObjectNode node : Model.getAll("/contract")) {
			Contract c = new Contract(node);
				
			if (c.firstParty.getId().equals(userId) && c.expiryDate.after(new Date())) {
				allContracts.add(c);
			}
		}
		return allContracts;
	}

	/**
	 * Fetch all unterminated contracts as second party
	 * @param userId
	 * @return
	 */
	public static List<Contract> getAllContractsAsSecondParty(String userId) {
		List<Contract> allContracts = new ArrayList<Contract>();
		for (ObjectNode node : Model.getAll("/contract")) {
			Contract c = new Contract(node);
			if (c.secondParty.getId().equals(userId) && c.expiryDate.after(new Date()))  {
				allContracts.add(c);
			}
		}
		return allContracts;
	}

	/////////////////// requirement 3 ///////////////////////
	/**
	 * gets 5 most recently expired contracts as first party
	 * @param allContracts
	 * @param userId
	 * @return
	 */
	public static List<Contract> getAllExpiredContracts(String userId) {
		List<Contract> contracts = new ArrayList<Contract>();
		for (ObjectNode node : Model.getAll("/contract")) {
			Contract c = new Contract(node);
			if (c.expiryDate.before(new Date()) && contracts.size() < MAX_CONTRACT_VIEWED)
				contracts.add(c);
//			else if (c.terminationDate != null && contracts.size() >= MAX_CONTRACT_VIEWED)
//				for (Contract c1 : contracts) {
//					if (c1.terminationDate < c.terminationDate)
//						
//				}
		}
		return contracts;
	}

	private void deleteContract() {
		String url = Application.rootUrl + "/contract/" + this.id;
		Model.delete(url);
		this.inform(EventType.CONTRACT_DELETED);
	}
	/**
	 * Post new contract from currentContract. currentContract is deleted right after
	 */
	public void reuseContract(ContractAddInfo addInfo) {
		postContract(this.firstParty.getId(), 
				this.secondParty.getId(), 
				this.subject.getId(), addInfo);
		this.deleteContract();
		this.inform(EventType.CONTRACT_REUSE);
	}
	
	public void reuseContract(String newSecondPartyId) {
		postContract(this.firstParty.getId(), 
				newSecondPartyId, 
				this.subject.getId(), 
				this.addInfo);
		this.deleteContract();
		this.inform(EventType.CONTRACT_REUSE);
	}

	public String getSubjectId() {
		return this.subject.getId();
	}
	
	public int getRequiredCompetency() {
		return this.addInfo.getCompetency();
	}
	
	public void patchContractCessationInfo(ContractCessationInfo newInfo) {
		this.cessationInfo = newInfo;
		String url = Application.rootUrl + "/contract/" + this.id;
		String jsonString = "{" +
		  		"\"cessationInfo\":" + cessationInfo.toJson() + "}";
		    	
		Model.patch(url, jsonString);
		this.inform(EventType.CONTRACT_CESSATIONINFO_UPDATED);
	}

	public ContractAddInfo createContractAddInfo() {
		return this.cessationInfo.createContractAddInfo();
	}

	/**
	 * Get second party id for reused contract
	 * @return
	 */
	public String getSecondPartyId() {
//		return this.cessationInfo.getSecondPartyId();
		return this.secondParty.getId();
	}

	//////// requirement2: notify near expired contracts /////////
	public static List<Contract> getNearExpiryContracts(List<Contract> allContracts) {
		List<Contract> contracts = new ArrayList<Contract>();
		for (Contract c : allContracts) {
			if (c.isSigned() && c.expiryDate.after(new Date()) && c.expiryDate.before(ADD_MONTH(new Date(), 1)) )
				contracts.add(c);
		}
		return contracts;
	}
	
	public int getContractDuration() {
		return this.addInfo.getContractDuration();
	}
	
	public Contract updateContract() {
		return new Contract(Model.get("/contract/", this.id));
	}
	//////// end requirement2 /////////

	/////// for testing //////////
	public static void deleteAllContracts() {
		for (ObjectNode node : Model.getAll("/contract")) {
			String url = Application.rootUrl + "/contract/" + node.get("id").textValue();
			Model.delete(url);
		}
	}
	
	public String toString() {
		if (dateSigned == null)
			return "First Party: " + firstParty.getFullName() + '\n'
					+  "Second Party: " + secondParty.getFullName() + '\n'
					+  "Subject: " + '\n' + subject + '\n'
					+ "Date created: " + '\n' + dateCreated + '\n'
					+ "Date signed: " + '\n' + "unsigned" + '\n'
					+ "Duration: " + '\n' + this.getContractDuration();
		else
			return "First Party: " + firstParty.getFullName() + '\n'
					+  "Second Party: " + secondParty.getFullName() + '\n'
					+  "Subject: " + '\n' + subject + '\n'
					+ "Date created: " + '\n' + dateCreated + '\n'
					+ "Date signed: " + '\n' + dateSigned + '\n'
					+ "Date expire: " + '\n' + expiryDate;
	}
	
	public boolean isSigned() {
		return firstPartySigned() && secondPartySigned() || this.dateSigned != null; 
	}
	
	public boolean firstPartySigned() {
		return this.addInfo.isFirstPartySigned();
	}
	
	public boolean secondPartySigned() {
		return this.addInfo.isSecondPartySigned();
	}

	public void firstPartySign() {
		this.addInfo.firstPartySign(true);
		if (this.isSigned() && this.dateSigned == null)
			this.signContract();
		else
			this.patchContract();
	}

	public void secondPartySign() {
		this.addInfo.secondPartySign(true);
		if (this.isSigned())
			this.signContract();
		else
			this.patchContract();
	}

	public void patchContract() {
		String url = Application.rootUrl + "/contract/" + this.id;
		String jsonString = "{" +
		  		"\"firstPartyId\":\"" + firstParty.getId() + "\"," +
		  		"\"secondPartyId\":\"" + secondParty.getId() + "\"," +
				"\"subjectId\":\"" + subject.getId() + "\"," +
		  		"\"dateCreated\":\"" + Utils.format.format(this.dateCreated) + "\"," +
		  		"\"expiryDate\":\"" + Utils.format.format(this.expiryDate) + "\"," +
		  		"\"paymentInfo\":{}," +
		  		"\"lessonInfo\":{}," +
		  		"\"additionalInfo\":" + addInfo.toJson() + "}";
		    	
		Model.patch(url, jsonString);
		this.inform(EventType.CONTRACT_ONE_PARTY_SIGN);
	}
	
}
