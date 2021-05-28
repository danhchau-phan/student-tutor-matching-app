package mainview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import mainview.MainController.Role;
import model.Bid;
import model.BidResponse;
import model.Contract;
import model.ContractAddInfo;
import model.EventType;
import model.Message;
import model.MessageAddInfo;
import model.User;
import studentview.ContractReuse;
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

public class TutorController implements Observer {
	private Display display;
    private User user;
    private List<Bid> allBids = new ArrayList<Bid>();
    private List<Bid> monitoredBids = new ArrayList<Bid>();
    private List<Contract> allUnexpiredContracts = new ArrayList<Contract>();

    private Bid activeBid, subscriberBid = new Bid();
    private Contract activeContract, subscriberContract = new Contract();
    private Message activeMessage;

    private HomeView homeView;
    private TutorAllBids tutorAllBids;
    private TutorAllContracts tutorAllContracts;
    private CreateBid createBid;
    private TutorView tutorView;
    private TutorResponseView tutorResponse;
    private TutorMonitorView tutorMonitor;
    private TutorMessageView tutorMessage;
    
    private ContractDurationFrame contractDurationFrame = new ContractDurationFrame();
    
	class TutorRoleActivationListener implements MouseClickListener {
	@Override
	public void mouseClicked(MouseEvent e) {
	    activeRole = Role.tutor;
	    fetchAllBids();
	    fetchAllContractAsSecondParty();
	    fetchNearExpiredContract();
	    fetchMonitoredBids();
	    initTutorViews();
	    subscribeBidCreation();
	    subscribeContractCreation();
	    display.removePanel(homeView.panel);
	    tutorView.display();
	}}

	class SubscribeBidListener implements MouseClickListener{
	    @Override
	    public void mouseClicked(MouseEvent mouseEvent) {
	    	user.addBidToMonitor(activeBid);
	    }
	}

	class BuyOutListener implements MouseClickListener{
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        if (activeBid.checkEligibility(user)) {
	            contractDurationFrame.show();
	            int contractDuration = contractDurationFrame.getDuration();
	            subscriberContract.postContract(user.getId(), activeBid.getInitiatorId(),
	                    activeBid.getSubject().getId(),
	                    new ContractAddInfo(true, true, contractDuration,
	                    		activeBid.getRequestCompetency(),
	                    		activeBid.getRequestHourPerLesson(),
	                    		activeBid.getRequestSessionPerWeek(),
	                    		activeBid.getRequestRate()));
	
	            activeBid.closeDownBid();
	            Utils.SUCCESS_CONTRACT_CREATION.show();
	        } else {
	            Utils.INSUFFICIENT_COMPETENCY.show();
	        }
	    }
	}

	/**
	 * Listener to switch to CreateBid view
	 *
	 */
	class CreateBidListener implements MouseClickListener{
	    @Override
	    public void mouseClicked(MouseEvent mouseEvent) {
	        if (activeBid.checkEligibility(user)) {
	            if (tutorView.activePanel != null) {
	                tutorView.main.remove(tutorView.activePanel);
	            }
	            tutorView.main.add(createBid);
	            tutorView.activePanel = createBid;
	            display.createPanel(tutorView.main);
	            display.setVisible();
	        } else {
	            Utils.INSUFFICIENT_COMPETENCY.show();
	        }
	    }
	}

	/**
	 * Listener to submit new bid
	 *
	 */
	class SubmitBidListener implements MouseClickListener {
	
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        String r = createBid.rate.getText();
	            String d = createBid.duration.getText();
	            String tD = createBid.timeDate.getText();
	            String s = createBid.sessionsPerWeek.getText();
	            String rT = createBid.rateType.getSelection().getActionCommand();
	            String a = createBid.addInfo.getText();
	            boolean f = createBid.freeLesson.getSelection().getActionCommand() == "yes"? true : false;
	            try {
	                BidResponse response = new BidResponse(
	                        user.getId(),
	                        user.getFullName(),
	                        r,
	                        rT,
	                        d,
	                        tD,
	                        s,
	                        a,
	                        f);
	                if (activeBid.tutorHasBidded(user.getId()))
	                	activeBid.addResponse(response, user.getId());
	                else
	                	activeBid.addResponse(response);
	                Utils.SUCCESS_BID_CREATION.show();
	            } catch (NumberFormatException nfe) {
	                Utils.INVALID_FIELDS.show();
	            } catch (NullPointerException npe) {
	                npe.printStackTrace();
	                Utils.PLEASE_FILL_IN.show();
	            }
	    }
	
	}

	class TutorSignContractListener implements MouseClickListener {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        
	        activeContract = tutorAllContracts.getSelectedContract();
	        activeContract.secondPartySign();
	        if (activeContract.isSigned()) {
	            Utils.CONTRACT_SIGNED.show();
	        } else
	            Utils.OTHER_PARTY_PENDING.show();   
	    }
	}

	class SendTutorMessageListener implements MouseClickListener {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	        String content = tutorMessage.getChatContent();
	        if (activeMessage != null) {
	            activeMessage.addNewMessage(content, user.getUsername());
	        } else {
	            activeMessage.postMessage(activeBid.getId(), user.getId(), content, new MessageAddInfo(content, user.getUsername()));
	        }
	        showTutorMessagePanel();
	    }
	}

	/**
	 * Listener to reload monitor
	 *
	 */
	class MonitorReloadListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Reload monitor");
			for (Bid b : monitoredBids) {
				b.updateBid();
			}
			tutorMonitor.placeComponents();
			
		}}

	@Override
	public void update(EventType e) {
		switch (e) {
        case BID_CREATED: {
            if (activeRole == Role.student) {
                reFetchInitiatedBids();
            } else if (activeRole == Role.tutor)
                reFetchAllBids();
            break;
            }
        case BID_CLOSEDDOWN: {
            if (activeRole == Role.student)
                this.initiatedBids.remove(activeBid);
            else if (activeRole == Role.tutor)
                this.allBids.remove(activeBid);
            activeBid = null;
            break;
        }
        case MESSAGE_PATCH: {
            showTutorMessagePanel();
            break;
        }
        case CONTRACT_CREATED: {
            reFetchAllContractAsSecondParty();
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
        }
        case CONTRACT_REUSE: {
        }
        case USER_SUBSCRIBE_NEW_BID: {
        	this.monitoredBids.add(activeBid);
        	break;
        }
        }
	}

}
