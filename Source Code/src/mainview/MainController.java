package mainview;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import model.*;
import studentview.*;
import tutorview.*;

import javax.swing.*;

public class MainController implements Observer{
    private static final int monitorCheckInterval = 5000;

    private Display display;
    private User user;

    private HomeView homeView;

    private enum Role {
        student,
        tutor
    }
    private Role activeRole;
    public MainController() {
        this.start();
    }

    public void start() {
        this.display = new Display();
        AuthenticationView authView = new AuthenticationView(display);

        // Login listener
        authView.addMouseListener(authView.loginButton, new MouseClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
                String username = authView.getUserName();
                String password = authView.getPassword();

    			try {
    				user = User.logIn(username, password);
    			} catch (Exception exception) {
    				exception.printStackTrace();
    			}
    			if (!(user == null)) {
    			    display.removePanel(authView.panel);
                    homeView = new HomeView(display, user);
                    homeView.logOut.addMouseListener(new LogoutListener());
                    homeView.studentButton.addMouseListener(new StudentRoleActivationListener());
                    homeView.tutorButton.addMouseListener(new TutorRoleActivationListener());
                    
                    homeView.display();
    			} else {
					Utils.INVALID_USER.show();
				}
			}
        });

        authView.display();
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
    	assert this.activeRole == Role.tutor;
    	this.monitoredBids.clear();
    	
    	for (Bid b : this.allBids)
    		if (this.user.monitor(b))
    			this.monitoredBids.add(b);
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

    private void fetchStudentExpiredContract() {
    	assert this.activeRole == Role.student;
        this.studentExpiredContracts.clear();
        for (Contract c : Contract.getAllExpiredContracts(this.user.getId())) {
            this.studentExpiredContracts.add(c);
            c.subscribe(EventType.CONTRACT_DELETED, this);
            c.subscribe(EventType.CONTRACT_REUSE, this);
        }
    }
    
    private void reFetchStudentExpiredContract() {
    	assert this.activeRole == Role.student;
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
                    tutorMessage = new TutorMessageView(user, activeMessage, activeBid);
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

    private void subscribeBidCreation() {
        subscriberBid.subscribe(EventType.BID_CREATED, this);
        if (activeRole == Role.student)
            subscriberBid.subscribe(EventType.BID_CREATED, studentAllBids);
        else if (activeRole == Role.tutor)
            subscriberBid.subscribe(EventType.BID_CREATED, tutorAllBids);
        
    }

    private void subscribeBidNewResponse() {
        activeBid.emptySubscription(EventType.BID_NEWRESPONSE);
        activeBid.subscribe(EventType.BID_NEWRESPONSE, tutorResponse);
    }

    private void subscribeMessage() {
        activeMessage.subscribe(EventType.MESSAGE_PATCH, this);
    }

    private void subscribeContractCreation() {
        subscriberContract.subscribe(EventType.CONTRACT_CREATED, this);
        if (activeRole == Role.student)
            subscriberContract.subscribe(EventType.CONTRACT_CREATED, studentAllContracts);
        else if (activeRole == Role.tutor)
            subscriberContract.subscribe(EventType.CONTRACT_CREATED, tutorAllContracts);
    }

    /** Display the Active Message SubPanel correspond to the ActiveBid
     * 
     */
    private void showStudentMessagePanel() {
        
        studentMessage = new StudentMessageView(user, activeMessage, activeBid);
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

    private void showTutorMessagePanel() {
        tutorMessage = new TutorMessageView(user, activeMessage, activeBid);
        tutorMessage.setSendMessageListener(new SendTutorMessageListener());

        if (tutorView.activePanel != null) {
            tutorView.main.remove(tutorView.activePanel);
        }
        tutorView.main.add(tutorMessage);
        tutorView.activePanel = tutorMessage;
        display.createPanel(tutorView.main);
        display.setVisible();
    }

    class LogoutListener implements MouseClickListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            display.closeWindow();
            new MainController();
        }
        
    }

    @Override
    public void update(EventType e) {
        
    }
}
