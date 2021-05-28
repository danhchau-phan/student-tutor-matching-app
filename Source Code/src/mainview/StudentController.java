package mainview;

import java.awt.event.MouseEvent;
import java.util.List;

import mainview.MainController.Role;
import model.Bid;
import model.BidAddInfo;
import model.BidResponse;
import model.ContractAddInfo;
import model.Subject;
import model.User;
import studentview.CreateDifferentTutorContract;
import studentview.StudentAllContracts;

public class StudentController {

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
	
}
