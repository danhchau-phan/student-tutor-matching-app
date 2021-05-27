package mainview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import model.*;
import studentview.*;
import tutorview.*;

import javax.swing.*;

public class Controller implements Observer{
    private static final int monitorCheckInterval = 5000;
    private boolean isLogOut;

    private Display display;
    private User user;
    private List<Bid> initiatedBids = new ArrayList<Bid>();
    private List<Bid> allBids = new ArrayList<Bid>();
    private List<Bid> monitoredBids = new ArrayList<Bid>();
    private List<Contract> allContracts = new ArrayList<Contract>();
    private List<Contract> studentExpiredContracts = new ArrayList<Contract>();

    private Bid activeBid, subscriberBid = new Bid();
    private Contract activeContract, subscriberContract = new Contract();
    private Message activeMessage;

    private HomeView homeView;

    //Student Fields
    private StudentAllBids studentAllBids;
    private StudentAllContracts studentAllContracts;
    private CreateRequest createRequest;
    private StudentView studentView;
    private StudentResponseView studentResponse;
    private StudentMessageView studentMessage;
    private ContractReuse contractReuse;
    private ReviseContractTerm reviseContractTerm;

    //Tutor Fields
    private TutorAllBids tutorAllBids;
    private TutorAllContracts tutorAllContracts;
    private CreateBid createBid;
    private TutorView tutorView;
    private TutorResponseView tutorResponse;
    private TutorMonitorView tutorMonitor;
    private TutorMessageView tutorMessage;
    private CreateSameTutorContract createSameTutorContract;
    private CreateDifferentTutorContract createDifferentTutorContract;


    private ContractDurationFrame contractDurationFrame = new ContractDurationFrame();
    private enum Role {
        student,
        tutor
    }
    private Role activeRole;
    public Controller() {
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
    			    isLogOut = false;
                    // should be a separate function
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
        this.allContracts.clear();
        for (Contract c : Contract.getAllContractsAsFirstParty(this.user.getId())) {
            this.allContracts.add(c);
            c.subscribe(EventType.CONTRACT_SIGN, this);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, this);
            
        }
    }
    
    private void reFetchAllContractAsFirstParty() {
        this.allContracts.clear();
        for (Contract c : Contract.getAllContractsAsFirstParty(this.user.getId())) {
            this.allContracts.add(c);
            c.subscribe(EventType.CONTRACT_SIGN, this);
            c.subscribe(EventType.CONTRACT_SIGN, studentAllContracts);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, this);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, studentAllContracts);
        }
    }

    private void fetchAllContractAsSecondParty() {
        this.allContracts.clear();
        for (Contract c : Contract.getAllContractsAsSecondParty(this.user.getId())) {
            this.allContracts.add(c);
            c.subscribe(EventType.CONTRACT_SIGN, this);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, this);
        }
    }
    
    private void reFetchAllContractAsSecondParty() {
        this.allContracts.clear();
        for (Contract c : Contract.getAllContractsAsSecondParty(this.user.getId())) {
            this.allContracts.add(c);
            c.subscribe(EventType.CONTRACT_SIGN, this);
            c.subscribe(EventType.CONTRACT_SIGN, tutorAllContracts);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, this);
            c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, tutorAllContracts);
        }
    }

    private void fetchStudentExpiredContract() {
    	assert this.activeRole == Role.student;
        this.studentExpiredContracts.clear();
        for (Contract c : Contract.getAllExpiredContracts(this.allContracts)) {
            this.studentExpiredContracts.add(c);
            c.subscribe(EventType.CONTRACT_CESSATIONINFO_UPDATED, contractReuse);
            c.subscribe(EventType.CONTRACT_DELETED, this);
            c.subscribe(EventType.CONTRACT_DELETED, contractReuse);
        }
    }

    private void fetchNearExpiredContract() {
    	List<Contract> c = Contract.getNearExpiryContracts(allContracts);
    	(new NearExpiryContractFrame(c)).show();
    };
    
    private void initTutorViews() {
        assert (this.user != null);
        this.tutorView = new TutorView(display, user);
        this.tutorAllBids = new TutorAllBids(this.allBids);
        this.tutorAllContracts = new TutorAllContracts(this.allContracts);
        this.tutorResponse = new TutorResponseView();
        this.tutorMonitor = new TutorMonitorView(this.monitoredBids, new MonitorReloadListener());
        this.createBid = new CreateBid();
        this.createSameTutorContract = new CreateSameTutorContract();
        this.createDifferentTutorContract = new CreateDifferentTutorContract();

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
                    tutorResponse.setModifyBidListener(new ModifyBidListener());
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
        
        for (Contract c : this.allContracts) {
	        c.subscribe(EventType.CONTRACT_SIGN, tutorAllContracts);
	        c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, tutorAllContracts);
        }
    }

    private void initStudentViews() {
        assert (this.user != null);
        this.studentView = new StudentView(display, user);
        this.studentAllBids = new StudentAllBids(this.initiatedBids);
        this.studentAllContracts = new StudentAllContracts(this.allContracts);
        this.createRequest = new CreateRequest();
        this.contractReuse = new ContractReuse(studentExpiredContracts);
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
        for (Contract c : this.allContracts) {
	        c.subscribe(EventType.CONTRACT_ONE_PARTY_SIGN, studentAllContracts);
	        c.subscribe(EventType.CONTRACT_SIGN, studentAllContracts);
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

    /**
     * Listener for reusing contract with same tutor
     *
     */
    class ReuseSameTutorListener implements MouseClickListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        	createSameTutorContract.setCurrentContract(activeContract);
        	String c = (String) createSameTutorContract.competency.getSelectedItem();
            String h = createSameTutorContract.hourPerLesson.getText();
            String ss = createSameTutorContract.sessionsPerWeek.getText();
            String r = createSameTutorContract.rate.getText();
            String rT = createSameTutorContract.rateType.getSelection().getActionCommand();
        	activeContract.reuseContract(new ContractAddInfo(false, false, activeContract.getContractDuration() , c, h, ss, r));
        }
    }


    class ReuseDifferentTutorListener implements MouseClickListener {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        	///////// INCOMPLETE: INPUT FOR TUTOR SELECTION NEEDED ///////////////
            createDifferentTutorContract.addAllContract(allContracts);
            createDifferentTutorContract.show();
            String tutorId = createDifferentTutorContract.getSelectedTutor();
        	String newSecondPartyId = tutorId;
        	activeContract.reuseContract(newSecondPartyId);
        }
    }

    class LogoutListener implements MouseClickListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            display.closeWindow();
            isLogOut = true;
            new Controller();
        }
        
    }

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
//                ContractCessationInfo cessationInfo = new ContractCessationInfo(subscriberContract.getSecondPartyId(), contractDuration, activeBid.getRequestCompetency(), activeBid.getRequestHourPerLesson(), activeBid.getRequestHourPerLesson(), activeBid.getRequestRate());
//                subscriberContract.patchContractCessationInfo(cessationInfo);
                activeBid.closeDownBid();
                Utils.SUCCESS_CONTRACT_CREATION.show();
            } else {
                Utils.INSUFFICIENT_COMPETENCY.show();
            }
        }
    }

    class SubscribeBidListener implements MouseClickListener{
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        	user.addBidToMonitor(activeBid);
        }
    }
    
    class ModifyBidListener implements MouseClickListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			
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

    class StudentSignContractListener implements MouseClickListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (studentAllContracts.getSignedContracts() >= StudentAllContracts.CONTRACT_QUOTA) {
                Utils.REACHED_CONTRACT_LIMIT.show();
            }
            else {
                Contract c = studentAllContracts.getSelectedContract();
                c.firstPartySign(true);
                if (c.isSigned()) {
                    //////////// CHANGES HERE //////////////
                    Utils.CONTRACT_SIGNED.show();
                } else
                    Utils.OTHER_PARTY_PENDING.show();
            }
        }
        
    }

    class TutorSignContractListener implements MouseClickListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            
            Contract c = tutorAllContracts.getSelectedContract();
            c.secondPartySign(true);
            if (c.isSigned()) {
                Utils.CONTRACT_SIGNED.show();
            } else
                Utils.OTHER_PARTY_PENDING.show();   
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
    
    class SaveCessationInfoListener implements MouseClickListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			//////// INCOMPLETE /////////
		}}
    
    /**
     * Listener to create new contract and delete current contract (when JList is clicked)
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
                fetchInitiatedBids();
            } else if (activeRole == Role.tutor)
                fetchAllBids();
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
            if (activeRole == Role.student)
                showStudentMessagePanel();
            else if (activeRole == Role.tutor)
                showTutorMessagePanel();
        }
        case CONTRACT_CREATED: {
            if (activeRole == Role.student)
                reFetchAllContractAsFirstParty();
            else if (activeRole == Role.tutor)
                reFetchAllContractAsSecondParty();
        }
        case CONTRACT_SIGN: {
        }
        case CONTRACT_ONE_PARTY_SIGN: {

        }
        case CONTRACT_DELETED: {
            studentExpiredContracts.remove(activeContract);
        }
        case USER_SUBSCRIBE_NEW_BID: {
        	this.monitoredBids.add(activeBid);
//        	this.fetchMonitoredBids();
        }
        }
    }
}
