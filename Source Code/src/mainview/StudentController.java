package mainview;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import mainview.MainController.Role;
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
import tutorview.CreateBid;
import tutorview.TutorAllBids;
import tutorview.TutorAllContracts;
import tutorview.TutorMessageView;
import tutorview.TutorMonitorView;
import tutorview.TutorResponseView;
import tutorview.TutorView;

public class StudentController implements Observer {
	private Display display;
    private User user;
    private List<Bid> initiatedBids = new ArrayList<Bid>();
    private List<Bid> allBids = new ArrayList<Bid>();
    private List<Bid> monitoredBids = new ArrayList<Bid>();
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
    
    private ContractDurationFrame contractDurationFrame = new ContractDurationFrame();
    
	class StudentRoleActivationListener implements MouseClickListener {
	@Override
	public void mouseClicked(MouseEvent e) {
	    activeRole = Role.student;
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
	    	createSameTutorContract.setCurrentContract(activeContract);
	    	
	    	if (studentView.activePanel != null) {
	            studentView.main.remove(studentView.activePanel);
	        }
	    	studentView.main.add(createSameTutorContract);
	    	studentView.activePanel = createSameTutorContract;
	        display.createPanel(studentView.main);
	        display.setVisible();
	    }
	}

	class ReuseDifferentTutorListener implements MouseClickListener {
	    @Override
	    public void mouseClicked(MouseEvent mouseEvent) {
	    	List<String> allTutorsId = User.getAllTutorsId();
	    	CreateDifferentTutorContract createDifferentTutorContract = new CreateDifferentTutorContract(activeContract,allTutorsId);
	    	createDifferentTutorContract.show();
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

	/**
	     * Listener to revise contract's term (if student reuse contract with the same tutor)
	     *
	     */
	class ReviseContractTermListener implements MouseClickListener {
	
	        @Override
	        public void mouseClicked(MouseEvent e) {
	//        	assert activeRole == Role.student;
	//            activeContract = contractReuse.getSelectedContract();
	//            reviseContractTerm.setContract(activeContract);
	//            if (studentView.activePanel != null) {
	//                studentView.main.remove(studentView.activePanel);
	//            }
	//            studentView.main.add(reviseContractTerm);
	//            studentView.activePanel = reviseContractTerm;
	//            display.createPanel(studentView.main);
	//            display.setVisible();
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
