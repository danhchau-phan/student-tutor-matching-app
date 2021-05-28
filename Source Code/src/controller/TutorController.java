package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import model.BidResponse;
import model.Contract;
import model.ContractAddInfo;
import model.EventType;
import model.Message;
import model.MessageAddInfo;
import model.User;
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
    
    public TutorController(Display display, User user, HomeView homeView) {
    	this.display = display;
    	this.user = user;
    	this.homeView = homeView;
    }
    
    private void fetchAllBids() {
        this.allBids.clear();
        for (Bid b : Bid.getAll()) {
            this.allBids.add(b);
            b.subscribe(EventType.BID_CLOSEDDOWN, this);
        }
    }
    
    private void reFetchAllBids() {
        this.allBids.clear();
        for (Bid b : Bid.getAll()) {
            this.allBids.add(b);
            b.subscribe(EventType.BID_CLOSEDDOWN, this);
            b.subscribe(EventType.BID_CLOSEDDOWN, tutorAllBids);
            b.subscribe(EventType.BID_FETCH_NEWRESPONSE_FROM_API, tutorMonitor);
        }
    }
    
    private void fetchMonitoredBids() {
    	this.monitoredBids.clear();
    	
    	for (Bid b : this.allBids)
    		if (this.user.monitor(b))
    			this.monitoredBids.add(b);
    }
    
    private void initTutorViews() {
        assert (this.user != null);
        this.tutorView = new TutorView(display, user);
        this.tutorAllBids = new TutorAllBids(this.allBids);
        this.tutorAllContracts = new TutorAllContracts(this.allUnexpiredContracts);
        this.tutorResponse = new TutorResponseView();
        this.tutorMonitor = new TutorMonitorView(this.monitoredBids, new MonitorReloadListener());
        this.createBid = new CreateBid();

        tutorView.setSwitchPanelListener(tutorView.main, tutorView.homeButton, homeView);
        tutorView.setSwitchPanelListener(tutorView.main, tutorView.viewAllBids, tutorAllBids);
        tutorView.setSwitchPanelListener(tutorView.main, tutorView.viewContracts, tutorAllContracts);
        tutorView.setSwitchPanelListener(tutorView.main, tutorView.viewMonitor, tutorMonitor);


        /** Tutor Response Portal: Response Bid View and Message View*/
        tutorAllBids.setListListener(new MouseClickListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tutorView.activePanel != null) {
                    tutorView.main.remove(tutorView.activePanel);
                }

                activeBid = tutorAllBids.getSelectedBid();
                if (activeBid.getType() == Bid.BidType.open) {
                    tutorResponse.setBid(activeBid);
                    subscribeBidNewResponse();
                    
                    tutorResponse.setCreateBidListener(new CreateBidListener());
                    tutorResponse.setBuyOutListener(new BuyOutListener());
                    tutorResponse.setSubscribeBidListener(new SubscribeBidListener());
                    tutorView.main.add(tutorResponse);
                    tutorView.activePanel = tutorResponse;
                } else {
                    tutorMessage = new TutorMessageView(activeMessage, activeBid);
                    tutorMessage.setSendMessageListener(new SendTutorMessageListener());
                    tutorView.main.add(tutorMessage);
                    tutorView.activePanel = tutorMessage;
                }
                display.createPanel(tutorView.main);
                display.setVisible();
            }
        });


        tutorAllContracts.setSignContractListener(new TutorSignContractListener());

        createBid.setSubmitBidListener(new SubmitBidListener());
        
        user.emptySubscription(EventType.USER_SUBSCRIBE_NEW_BID);
        user.subscribe(EventType.USER_SUBSCRIBE_NEW_BID, this);
        user.subscribe(EventType.USER_SUBSCRIBE_NEW_BID, tutorMonitor);
        
        for (Bid b : this.allBids) {
            b.subscribe(EventType.BID_CLOSEDDOWN, tutorAllBids);
            b.subscribe(EventType.BID_FETCH_NEWRESPONSE_FROM_API, tutorMonitor);
        }
        
        for (Contract c : this.allUnexpiredContracts) {
	        c.subscribe(EventType.CONTRACT_SIGN, tutorAllContracts);
	        c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, tutorAllContracts);
        }
    }
    
    private void showTutorMessagePanel() {
        tutorMessage = new TutorMessageView(activeMessage, activeBid);
        tutorMessage.setSendMessageListener(new SendTutorMessageListener());

        if (tutorView.activePanel != null) {
            tutorView.main.remove(tutorView.activePanel);
        }
        tutorView.main.add(tutorMessage);
        tutorView.activePanel = tutorMessage;
        display.createPanel(tutorView.main);
        display.setVisible();
    }
    
    private void subscribeContractCreation() {
        subscriberContract.subscribe(EventType.CONTRACT_CREATED, this);
        subscriberContract.subscribe(EventType.CONTRACT_CREATED, tutorAllContracts);
    }
    
    private void subscribeBidNewResponse() {
        activeBid.emptySubscription(EventType.BID_NEWRESPONSE);
        activeBid.subscribe(EventType.BID_NEWRESPONSE, tutorResponse);
    }
    
    private void subscribeBidCreation() {
        subscriberBid.subscribe(EventType.BID_CREATED, this);
        subscriberBid.subscribe(EventType.BID_CREATED, tutorAllBids);
    }
    
    private void fetchAllContractAsSecondParty() {
        this.allUnexpiredContracts.clear();
        for (Contract c : Contract.getAllContractsAsSecondParty(this.user.getId())) {
            this.allUnexpiredContracts.add(c);
            c.subscribe(EventType.CONTRACT_SIGN, this);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, this);
        }
    }
    
    private void reFetchAllContractAsSecondParty() {
        this.allUnexpiredContracts.clear();
        for (Contract c : Contract.getAllContractsAsSecondParty(this.user.getId())) {
            this.allUnexpiredContracts.add(c);
            c.subscribe(EventType.CONTRACT_SIGN, this);
            c.subscribe(EventType.CONTRACT_SIGN, tutorAllContracts);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, this);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, tutorAllContracts);
        }
    }
    
    private void fetchNearExpiredContract() {
    	List<Contract> c = Contract.getNearExpiryContracts(allUnexpiredContracts);
    	(new NearExpiryContractFrame(c)).show();
    };
    
    private void subscribeMessage() {
        activeMessage.subscribe(EventType.MESSAGE_PATCH, this);
    }
	public class TutorRoleActivationListener implements MouseClickListener {
	@Override
	public void mouseClicked(MouseEvent e) {
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
            reFetchAllBids();
            break;
            }
        case BID_CLOSEDDOWN: {
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
        	break;
        }
        case CONTRACT_REUSE: {
        	break;
        }
        case USER_SUBSCRIBE_NEW_BID: {
        	this.monitoredBids.add(activeBid);
        	break;
        }
        }
	}

}
