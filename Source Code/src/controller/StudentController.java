package controller;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import mainview.ContractDurationFrame;
import mainview.Display;
import mainview.HomeView;
import mainview.MouseClickListener;
import mainview.NearExpiryContractFrame;
import mainview.Observer;
import mainview.Utils;
import model.Bid;
import model.BidAddInfo;
import model.BidResponse;
import model.Contract;
import model.ContractAddInfo;
import model.EventType;
import model.Message;
import model.Subject;
import model.User;
import studentview.ContractReuse;
import studentview.CreateDifferentTutorContract;
import studentview.CreateRequest;
import studentview.CreateSameTutorContract;
import studentview.ReviseContractTerm;
import studentview.StudentAllBids;
import studentview.StudentAllContracts;
import studentview.StudentMessageView;
import studentview.StudentResponseView;
import studentview.StudentView;

public class StudentController implements Observer {
	private Display display;
    private User user;
    private List<Bid> initiatedBids = new ArrayList<Bid>();
    private List<Contract> allUnexpiredContracts = new ArrayList<Contract>();
    private List<Contract> studentExpiredContracts = new ArrayList<Contract>();

    private Bid activeBid, subscriberBid = new Bid();
    private Contract activeContract, subscriberContract = new Contract();
    private Message activeMessage;

    private HomeView homeView;

    private StudentAllBids studentAllBids;
    private StudentAllContracts studentAllContracts;
    private CreateRequest createRequest;
    private StudentView studentView;
    private StudentResponseView studentResponse;
    private StudentMessageView studentMessage;
    private ContractReuse contractReuse;
    private ReviseContractTerm reviseContractTerm;
    private CreateSameTutorContract createSameTutorContract;
    private CreateDifferentTutorContract createDifferentTutorContract;
    
    private ContractDurationFrame contractDurationFrame = new ContractDurationFrame();
    public StudentController(Display display, User user, HomeView homeView) {
    	this.display = display;
    	this.user = user;
    	this.homeView = homeView;
    }
    
    private void fetchInitiatedBids() {
        this.initiatedBids.clear();
        for (Bid b : user.getInitiatedBids()) {
            this.initiatedBids.add(b);
            b.subscribe(EventType.BID_CLOSEDDOWN, this);
        }
    }
    
    private void reFetchInitiatedBids() {
        this.initiatedBids.clear();
        for (Bid b : user.getInitiatedBids()) {
            this.initiatedBids.add(b);
            b.subscribe(EventType.BID_CLOSEDDOWN, this);
            b.subscribe(EventType.BID_CLOSEDDOWN, studentAllBids);
        }
    }
    
    private void initStudentViews() {
        assert (this.user != null);
        this.studentView = new StudentView(display, user);
        this.studentAllBids = new StudentAllBids(this.initiatedBids);
        this.studentAllContracts = new StudentAllContracts(this.allUnexpiredContracts);
        this.createRequest = new CreateRequest();
        this.contractReuse = new ContractReuse(studentExpiredContracts);
        //////// requirement 3 ////////
        this.createSameTutorContract = new CreateSameTutorContract();
//        this.createDifferentTutorContract = new CreateDifferentTutorContract();
        this.reviseContractTerm = new ReviseContractTerm();
        
        
        studentView.setSwitchPanelListener(studentView.main, studentView.homeButton, homeView);
        studentView.setSwitchPanelListener(studentView.main, studentView.viewAllBids, studentAllBids);
        studentView.setSwitchPanelListener(studentView.main, studentView.viewContracts, studentAllContracts);
        studentView.setSwitchPanelListener(studentView.main, studentView.createMatchRequest, createRequest);
        studentView.setSwitchPanelListener(studentView.main, studentView.reuseContracts, contractReuse);
        
        createRequest.setCreateRequestListener(new CreateRequestListener());

        studentAllBids.setListListener(new MouseClickListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                activeBid = studentAllBids.getSelectedBid();
                studentResponse = new StudentResponseView(activeBid);
                studentResponse.setResponseListener(new ResponseListener());

                if (studentView.activePanel != null) {
                    studentView.main.remove(studentView.activePanel);
                }
                studentView.main.add(studentResponse);
                studentView.activePanel = studentResponse;
                display.createPanel(studentView.main);
                display.setVisible();

            }
        });
    
        studentAllContracts.setSignContractListener(new StudentSignContractListener());

        contractReuse.setReuseContractListener(new ReuseContractListener());
        
        createSameTutorContract.setListener(new SubmitReuseSameTutorListener());
        for (Contract c : this.allUnexpiredContracts) {
	        c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, studentAllContracts);
	        c.subscribe(EventType.CONTRACT_SIGN, studentAllContracts);
        }
        
        for (Contract c : this.studentExpiredContracts) {
            c.subscribe(EventType.CONTRACT_CESSATIONINFO_UPDATED, contractReuse);
            c.subscribe(EventType.CONTRACT_DELETED, contractReuse);
            c.subscribe(EventType.CONTRACT_REUSE, studentAllBids);
        }
        
        for (Bid b : this.initiatedBids) {
        	b.subscribe(EventType.BID_CLOSEDDOWN, studentAllBids);
        }
        
    }
    
    private void fetchAllContractAsFirstParty() {
        this.allUnexpiredContracts.clear();
        for (Contract c : Contract.getAllContractsAsFirstParty(this.user.getId())) {
            this.allUnexpiredContracts.add(c);
            c.subscribe(EventType.CONTRACT_SIGN, this);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, this);
            
        }
    }
    
    private void reFetchAllContractAsFirstParty() {
        this.allUnexpiredContracts.clear();
        for (Contract c : Contract.getAllContractsAsFirstParty(this.user.getId())) {
            this.allUnexpiredContracts.add(c);
            c.subscribe(EventType.CONTRACT_SIGN, this);
            c.subscribe(EventType.CONTRACT_SIGN, studentAllContracts);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, this);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, studentAllContracts);
        }
    }
    
    /** Display the Active Message SubPanel correspond to the ActiveBid
     * 
     */
    private void showStudentMessagePanel() {
        
        studentMessage = new StudentMessageView(activeMessage, activeBid);
        studentMessage.setSendMessageListener(new SendStudentMessageListener());
        studentMessage.setSelectBidListener(new MessageSelectBidListener());

        if (studentView.activePanel != null) {
            studentView.main.remove(studentView.activePanel);
        }
        studentView.main.add(studentMessage);
        studentView.activePanel = studentMessage;
        display.createPanel(studentView.main);
        display.setVisible();
    }
    
    private void subscribeBidCreation() {
        subscriberBid.subscribe(EventType.BID_CREATED, this);
        subscriberBid.subscribe(EventType.BID_CREATED, studentAllBids);
        
    }
    
    private void subscribeContractCreation() {
        subscriberContract.subscribe(EventType.CONTRACT_CREATED, this);
        subscriberContract.subscribe(EventType.CONTRACT_CREATED, studentAllContracts);
    }
    
    private void subscribeMessage() {
        activeMessage.subscribe(EventType.MESSAGE_PATCH, this);
    }
    
    private void fetchStudentExpiredContract() {
        this.studentExpiredContracts.clear();
        for (Contract c : Contract.getAllExpiredContracts(this.user.getId())) {
            this.studentExpiredContracts.add(c);
            c.subscribe(EventType.CONTRACT_DELETED, this);
            c.subscribe(EventType.CONTRACT_REUSE, this);
        }
    }
    
    private void reFetchStudentExpiredContract() {
        this.studentExpiredContracts.clear();
        for (Contract c : Contract.getAllExpiredContracts(this.user.getId())) {
            this.studentExpiredContracts.add(c);
            c.subscribe(EventType.CONTRACT_CESSATIONINFO_UPDATED, contractReuse);
            c.subscribe(EventType.CONTRACT_REUSE, this);
            c.subscribe(EventType.CONTRACT_DELETED, this);
            c.subscribe(EventType.CONTRACT_DELETED, contractReuse);
        }
    }
    
    private void fetchNearExpiredContract() {
    	List<Contract> c = Contract.getNearExpiryContracts(allUnexpiredContracts);
    	(new NearExpiryContractFrame(c)).show();
    };
    
	public class StudentRoleActivationListener implements MouseClickListener {
	@Override
	public void mouseClicked(MouseEvent e) {
	    fetchInitiatedBids();
	    fetchAllContractAsFirstParty();
	    fetchStudentExpiredContract();
	    fetchNearExpiredContract();
	
	    initStudentViews();
	    subscribeBidCreation();
	    subscribeContractCreation();
	    display.removePanel(homeView.panel);
	    studentView.display();
	}}

	class ResponseListener implements MouseClickListener{
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        if (activeBid.getType() == Bid.BidType.close) {
	            activeMessage = studentResponse.getSelectedMessage();
	            subscribeMessage();
	            showStudentMessagePanel();
	        } else {
	            BidResponse selectedResponse = studentResponse.getSelectedResponse();
	            if (selectedResponse == null)
	                return;
	
	            /** Set up the contract expiry date*/
	            contractDurationFrame.show();
	            int contractDuration = contractDurationFrame.getDuration();
	            
	            subscriberContract.postContract(user.getId(),
	                    selectedResponse.getBidderId(),
	                    activeBid.getSubject().getId(),
	                    new ContractAddInfo(true, false, contractDuration,
	                    		activeBid.getRequestCompetency(),
	                    		activeBid.getRequestHourPerLesson(),
	                    		activeBid.getRequestSessionPerWeek(),
	                    		activeBid.getRequestRate()));
	            activeBid.closeDownBid();
	            Utils.SUCCESS_CONTRACT_CREATION.show();
	        }
	    }
	}

	/**
	 * Listener for reusing contract with same tutor
	 *
	 */
	class ReuseSameTutorListener implements MouseClickListener {
	
	    @Override
	    public void mouseClicked(MouseEvent mouseEvent) {
	    	reviseContractTerm.setStrategy(createSameTutorContract);
	    }
	}

	class ReuseDifferentTutorListener implements MouseClickListener {
	    @Override
	    public void mouseClicked(MouseEvent mouseEvent) {
	    	createDifferentTutorContract = new CreateDifferentTutorContract(activeContract, User.getAllTutorsId());
	    	reviseContractTerm.setStrategy(createDifferentTutorContract);
	    	
	        String tutorId = createDifferentTutorContract.getSelectedTutor();
	        if (tutorId == null) 
	        	return;
	    	String newSecondPartyId = tutorId;
	    	activeContract.reuseContract(newSecondPartyId);
	    }
	}

	class CreateRequestListener implements MouseClickListener {
	
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        // needs refactoring
	        String c = (String) createRequest.competency.getSelectedItem();
	        String h = createRequest.hourPerLesson.getText();
	        String ss = createRequest.sessionsPerWeek.getText();
	        String r = createRequest.rate.getText();
	        String rT = createRequest.rateType.getSelection().getActionCommand();
	        String sj = (String) createRequest.subject.getSelectedItem();
	        String t = createRequest.bidType.getSelection().getActionCommand();
	        try {
	            BidAddInfo addInfo = new BidAddInfo(c,h,ss,r,rT);
	            subscriberBid.postBid(t, user.getId(), Subject.getSubjectId(sj), addInfo);
	            Utils.SUCCESS_MATCH_REQUEST.show();
	        } catch (NumberFormatException nfe) {
	            Utils.INVALID_FIELDS.show();
	        } catch (NullPointerException npe) {
	            npe.printStackTrace();
	            Utils.PLEASE_FILL_IN.show();
	        }
	    }
	
	}

	class MessageSelectBidListener implements MouseClickListener {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        contractDurationFrame.show();
	        int contractDuration = contractDurationFrame.getDuration();
	        subscriberContract.postContract(user.getId(),
	                activeMessage.getPosterId(),
	                activeBid.getSubject().getId(),
	                new ContractAddInfo(true, false, contractDuration, activeBid.getRequestCompetency(),
	                		activeBid.getRequestHourPerLesson(),
	                		activeBid.getRequestSessionPerWeek(),
	                		activeBid.getRequestRate()));
	        activeBid.closeDownBid();
	        Utils.SUCCESS_CONTRACT_CREATION.show();
	
	    }
	}

	class SendStudentMessageListener implements MouseClickListener {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        String content = studentMessage.getChatContent();
	        activeMessage.addNewMessage(content, user.getUsername());
	        showStudentMessagePanel();
	    }
	}

	class StudentSignContractListener implements MouseClickListener {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	activeContract = studentAllContracts.getSelectedContract();
	        if (!activeContract.isSigned() && studentAllContracts.getSignedContracts() >= StudentAllContracts.CONTRACT_QUOTA) {
	            Utils.REACHED_CONTRACT_LIMIT.show();
	        }
	        else {
	            activeContract.firstPartySign();
	            if (activeContract.isSigned()) {
	                Utils.CONTRACT_SIGNED.show();
	            } else
	                Utils.OTHER_PARTY_PENDING.show();
	        }
	    }
	    
	}

	class SubmitReuseSameTutorListener implements MouseClickListener {
	
	    @Override
	    public void mouseClicked(MouseEvent mouseEvent) {
	    	
	        
	    	String c = (String) createSameTutorContract.competency.getSelectedItem();
	        String h = createSameTutorContract.hourPerLesson.getText();
	        String ss = createSameTutorContract.sessionsPerWeek.getText();
	        String r = createSameTutorContract.rate.getText();
	        String rT = createSameTutorContract.rateType.getSelection().getActionCommand();
	    	activeContract.reuseContract(new ContractAddInfo(false, false, activeContract.getContractDuration() , c, h, ss, r));
	    	Utils.SUCCESS_CONTRACT_CREATION.show();
	    }
	}

	/**
	 * Listener to create new contract and delete current contract
	 *
	 */
	class ReuseContractListener implements MouseClickListener {
	
		@Override
		public void mouseClicked(MouseEvent e) {
	        activeContract = contractReuse.getSelectedContract();
	        reviseContractTerm.setContract(activeContract);
	        reviseContractTerm.setReuseSameTutorListener(new ReuseSameTutorListener());
	        reviseContractTerm.setReuseDifferentTutorListener(new ReuseDifferentTutorListener());
	
	
	        if (studentView.activePanel != null) {
	            studentView.main.remove(studentView.activePanel);
	        }
	        studentView.main.add(reviseContractTerm);
	        studentView.activePanel = reviseContractTerm;
	        display.createPanel(studentView.main);
	        display.setVisible();
	
	    }}

	@Override
	
	public void update(EventType e) {
		switch (e) {
        case BID_CREATED: {
            reFetchInitiatedBids();
            break;
            }
        case BID_CLOSEDDOWN: {
            this.initiatedBids.remove(activeBid);
            break;
        }
        case MESSAGE_PATCH: {
            showStudentMessagePanel();
            break;
        }
        case CONTRACT_CREATED: {
            reFetchAllContractAsFirstParty();
            break;
        }
        case CONTRACT_SIGN: {
        	int id = this.allUnexpiredContracts.indexOf(activeContract);
        	Contract newContract = activeContract.updateContract();
        	this.allUnexpiredContracts.set(id, newContract); 
        	activeContract = newContract;
        	break;
        }
        case CONTRACT_ONE_PARTY_SIGN: {
        	int id = this.allUnexpiredContracts.indexOf(activeContract);
        	Contract newContract = activeContract.updateContract();
        	this.allUnexpiredContracts.set(id, newContract); 
        	activeContract = newContract;
        	break;
        }
        case CONTRACT_DELETED: {
            studentExpiredContracts.remove(activeContract);
            this.reFetchStudentExpiredContract();
            break;
        }
        case CONTRACT_REUSE: {
            this.reFetchAllContractAsFirstParty();
            break;
        }
        case USER_SUBSCRIBE_NEW_BID: {
        }
        }
	}
	
}
