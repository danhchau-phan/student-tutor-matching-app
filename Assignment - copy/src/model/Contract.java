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
	private Date dateCreated, dateSigned, expiryDate, terminationDate;
	
	private ContractAddInfo addInfo;
	private ContractCessationInfo cessationInfo;
	private static final int YEAR_IN_MILLIS = 3600 * 24 * 365 * 1000;
	private static final int ONE_MONTH_IN_MILLIS = 3600 * 24 * 30 * 1000;
	public static final int DEFAULT_CONTRACT_DURATION = 6;
	public static final int MAX_CONTRACT_VIEWED = 5;

	private static final Date ADD_MONTH(Date now, int months) {
		Calendar c = Calendar.getInstance();
        c.setTime(now);
		c.add(Calendar.MONTH, months);
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
		this.terminationDate = Utils.formatDate(node.get("terminationDate").textValue()); ///// req 2 //////
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

	public void signContract() {
		this.addInfo.firstPartySign(true);
		this.addInfo.secondPartySign(true);
		/////////// set Contract expiry Date ////////
		Date now = new Date();
		setContractExpiryDate(ADD_MONTH(now, getContractDuration()));
		/////////// sign contract ////////
		String url = Application.rootUrl + "/contract/" + this.id + "/sign";
		String jsonString = "{" +
    	  		"\"dateSigned\":\"" + Utils.format.format(now) + "\"}";
		Model.post(url, jsonString);
		
		this.inform(EventType.CONTRACT_SIGN);
	}

	private int getContractDuration() {
		return this.addInfo.getContractDuration();
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
				
			if (c.firstParty.getId().equals(userId)) {
				// c.updateDateSigned();
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
			if (c.secondParty.getId().equals(userId))  {
				// c.updateDateSigned();
				allContracts.add(c);
			}
		}
		return allContracts;
	}

	/////////////////// requirement 3 ///////////////////////
	/**
	 * gets all unexpired contracts as second party
	 * @param allContracts
	 * @param userId
	 * @return
	 */
	public static List<Contract> getAllContractsAsSecondParty(List<Contract> allContracts, String userId) {
		List<Contract> contracts = new ArrayList<Contract>();
		for (Contract c : allContracts) {
			if (c.secondParty.getId().equals(userId) && c.terminationDate == null)  {
				// c.updateDateSigned();
				contracts.add(c);
			}
		}
		return contracts;
	}

	/**
	 * gets all unexpired contracts as first party
	 * @param allContracts
	 * @param userId
	 * @return
	 */
	public static List<Contract> getAllContractsAsFirstParty(List<Contract> allContracts, String userId) {
		List<Contract> contracts = new ArrayList<Contract>();
		for (Contract c : allContracts) {
			if (c.firstParty.getId().equals(userId) && c.terminationDate == null)  {
				// c.updateDateSigned();
				contracts.add(c);
			}
		}
		return contracts;
	}
	/**
	 * gets all expired contracts as first party
	 * @param allContracts
	 * @param userId
	 * @return
	 */
	public static List<Contract> getAllExpiredContracts(List<Contract> allContracts) {
		List<Contract> contracts = new ArrayList<Contract>();
		for (Contract c : allContracts)
			if (c.terminationDate != null)
				contracts.add(c);
		return contracts;
	}

	private void deleteContract() {
		/////////// INCOMPLETE: Making API call to delete contract //////////////
		this.inform(EventType.CONTRACT_DELETED);
	}
	/**
	 * Post new contract from currentContract's cessationInfo. currentContract is deleted right after
	 */
	public void postNewContractForReuse(Contract currentContract) {
		postContract(currentContract.firstParty.getId(), currentContract.getSecondPartyId(), currentContract.subject.getId(), currentContract.createContractAddInfo());
		currentContract.deleteContract();
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
		return this.cessationInfo.getSecondPartyId();
	}

	//////// requirement2: notify near expired contracts /////////
	public static List<Contract> getNearExpiryContracts(List<Contract> allContracts) {
		List<Contract> contracts = new ArrayList<Contract>();
		for (Contract c : allContracts) {
			if (c.isSigned() && c.terminationDate == null && c.expiryDate.before(new Date(System.currentTimeMillis() + ONE_MONTH_IN_MILLIS)) )
				contracts.add(c);
		}
		return contracts;
	}
	
	//////// end requirement2 /////////

	private void updateDateSigned() {
		if (this.addInfo == null)
			return;
		if (this.isSigned() && this.dateSigned == null) {
			this.signContract();
		} 
	}
	
	public String toString() {
		return "First Party: " + firstParty.getFullName() + '\n'
				+  "Second Party: " + secondParty.getFullName() + '\n'
				+  "Subject: " + '\n' + subject + '\n'
				+ "Date created: " + '\n' + dateCreated + '\n'
				+ "Date signed: " + '\n' + (dateSigned == null?"unsigned" : dateSigned) + '\n'
				+ "Date expire: " + '\n' + expiryDate;
	}
	
	public boolean isSigned() {
		return firstPartySigned() && secondPartySigned(); 
	}
	
	public boolean firstPartySigned() {
		return this.addInfo.isFirstPartySigned();
	}
	
	public boolean secondPartySigned() {
		return this.addInfo.isSecondPartySigned();
	}

	public void firstPartySign(boolean sign) {
		this.addInfo.firstPartySign(sign);
		if (this.isSigned())
			this.signContract();
		else
			this.patchContract();
	}

	public void secondPartySign(boolean sign) {
		this.addInfo.secondPartySign(sign);
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
		  		"\"dateCreated\":\"" + Utils.format.format(new Date()) + "\"," +
		  		"\"expiryDate\":\"" + this.expiryDate + "\"," +
		  		"\"paymentInfo\":{}," +
		  		"\"lessonInfo\":{}," +
		  		"\"additionalInfo\":" + addInfo.toJson() + "}";
		    	
		Model.patch(url, jsonString);
		this.inform(EventType.CONTRACT_ONE_PARTY_SIGN);
	}

	public void terminateContract() {

	}
	
}
