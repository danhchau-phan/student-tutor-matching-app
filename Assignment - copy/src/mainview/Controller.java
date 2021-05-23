package mainview;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import model.*;
import studentview.CreateRequest;
import studentview.StudentAllBids;
import studentview.StudentAllContracts;
import studentview.StudentMessageView;
import studentview.StudentResponseView;
import studentview.StudentView;
import tutorview.*;

public class Controller implements Observer{
    private Display display;
    private User user;
    private List<Bid> initiatedBids = new ArrayList<Bid>();
    private List<Bid> allBids = new ArrayList<Bid>();
   private List<Contract> allContracts;

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

    //Tutor Fields
    private TutorAllBids tutorAllBids;
    private TutorAllContracts tutorAllContracts;
    private CreateBid createBid;
    private TutorView tutorView;
    private TutorResponseView tutorResponse;
    private TutorMessageView tutorMessage;
    private enum Role {
        student,
        tutor,
        admin
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
                    // should be a separate function
    			    display.removePanel(authView.panel);
                    homeView = new HomeView(display, user);
                    // maybe admin??
                    if (user.isStudent())
                        initStudentViews();
                    if (user.isTutor())
                        initTutorViews();
                    
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
            // b.subscribe(EventType.BID_NEWRESPONSE, studentResponse);
        }
    }

    private void fetchAllBids() {
        this.allBids.clear();
        for (Bid b : Bid.getAll()) {
            this.allBids.add(b);
            b.subscribe(EventType.BID_CLOSEDDOWN, this);
            b.subscribe(EventType.BID_CLOSEDDOWN, tutorAllBids);
            // b.subscribe(EventType.BID_NEWRESPONSE, tutorResponse);
        }
    }

    private void fetchAllContractAsFirstParty() {
        this.allContracts = Contract.getAllContractsAsFirstParty(this.user.getId());
    }

    private void fetchAllContractAsSecondParty() {
        this.allContracts = Contract.getAllContractsAsSecondParty(this.user.getId());
    }

    private void initTutorViews() {
        assert (this.user != null);
        this.tutorView = new TutorView(display, user);
        this.tutorAllBids = new TutorAllBids(this.allBids);
        this.tutorAllContracts = new TutorAllContracts(user, subscriberContract);
        this.tutorResponse = new TutorResponseView();
        this.createBid = new CreateBid();
        
        tutorView.setSwitchPanelListener(tutorView.main, tutorView.homeButton, homeView);
        tutorView.setSwitchPanelListener(tutorView.main, tutorView.viewAllBids, tutorAllBids);
        tutorView.setSwitchPanelListener(tutorView.main, tutorView.viewContracts, tutorAllContracts);

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
                    tutorResponse.setResponseListener(new TutorResponseListener());
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

        tutorAllContracts.setSignContractListener(new SignContractListener());

        createBid.setCreateBidListener(new MouseClickListener(){
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
        });
    }

    private void initStudentViews() {
        assert (this.user != null);
        this.studentView = new StudentView(display, user);
        this.studentAllBids = new StudentAllBids(this.initiatedBids);
        this.studentAllContracts = new StudentAllContracts(user, subscriberContract);
        this.createRequest = new CreateRequest();

        
        studentView.setSwitchPanelListener(studentView.main, studentView.homeButton, homeView);
        studentView.setSwitchPanelListener(studentView.main, studentView.viewAllBids, studentAllBids);
        studentView.setSwitchPanelListener(studentView.main, studentView.viewContracts, studentAllContracts);
        studentView.setSwitchPanelListener(studentView.main, studentView.createBid, createRequest);

        createRequest.setCreateRequestListener(new MouseClickListener() {
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
            }});

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
    
        studentAllContracts.setSignContractListener(new SignContractListener());
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

    // private void addSubscription(Bid b) {
    // }

    private void subscribeMessage() {
        activeMessage.subscribe(EventType.MESSAGE_PATCH, this);
    }

    // Display the Active Message SubPanel correspond to the ActiveBid
    private void showStudentMessagePanel() {
        
        studentMessage = new StudentMessageView(user, activeMessage, activeBid);
        studentMessage.setSendMessageListener(new SendStudentMessageListener());
        studentMessage.setSelectBidListener(new SelectBidListener());

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
//        tutorMessage.setSelectBidListener(new SelectBidListener());

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
            new Controller();
        }
        
    }

    class StudentRoleActivationListener implements MouseClickListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            activeRole = Role.student;
            fetchInitiatedBids();
            fetchAllContractAsFirstParty();
            initStudentViews();
            subscribeBidCreation();
            display.removePanel(homeView.panel);
            studentView.display();
        }}

    class TutorRoleActivationListener implements MouseClickListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            activeRole = Role.tutor;
            fetchAllBids();
            fetchAllContractAsSecondParty();
            initTutorViews();
            subscribeBidCreation();
            display.removePanel(homeView.panel);
            tutorView.display();
        }}
    /** JButton Listener on each SubPanel (tutorAllBid, studentAllBid etc.)*/
    /** Response portals: MessageList View and Response View*/
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
                subscriberContract.postContract(user.getId(),
                        selectedResponse.getBidderId(),
                        activeBid.getSubject().getId(),
                        new ContractAddInfo(true, false));
                activeBid.closeDownBid();
                Utils.SUCCESS_CONTRACT_CREATION.show();
            }

        }

    }

    class TutorResponseListener implements MouseClickListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            BidResponse selectedResponse = tutorResponse.getSelectedResponse();
            if (selectedResponse == null)
                return;
            subscriberContract.postContract(user.getId(),
                    selectedResponse.getBidderId(),
                    activeBid.getSubject().getId(),
                    new ContractAddInfo(true, false));
            activeBid.closeDownBid();
            Utils.SUCCESS_CONTRACT_CREATION.show();

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
                subscriberContract.postContract(user.getId(), activeBid.getInitiatorId(),
                        activeBid.getSubject().getId(),
                        new ContractAddInfo(true, true));
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
            // Notify View for update (check monitor)
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

    class SelectBidListener implements MouseClickListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            subscriberContract.postContract(user.getId(),
                    activeMessage.getPosterId(),
                    activeBid.getSubject().getId(),
                    new ContractAddInfo(true, false));
            activeBid.closeDownBid();
            Utils.SUCCESS_CONTRACT_CREATION.show();

        }
    }

    class SignContractListener implements MouseClickListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (studentAllContracts.getSignedContracts() >= StudentAllContracts.CONTRACT_QUOTA) {
                Utils.REACHED_CONTRACT_LIMIT.show();
            }
            else {
                Contract c = studentAllContracts.getSelectedContract();
                c.firstPartySign(true);
                if (c.isSigned()) {
                    c.signContract();
                    Utils.CONTRACT_SIGNED.show();
                } else
                    Utils.OTHER_PARTY_PENDING.show();
            }
        }
        
    }

    @Override
    public void update(EventType e) {
        if (e == EventType.BID_CREATED) {
            if (activeRole == Role.student) {
                fetchInitiatedBids();
            } else if (activeRole == Role.tutor)
                fetchAllBids();
        }
        if (e == EventType.BID_CLOSEDDOWN) {
            if (activeRole == Role.student)
                this.initiatedBids.remove(activeBid);
            else if (activeRole == Role.tutor)
                this.allBids.remove(activeBid);
            activeBid = null;
        }
        if (e == EventType.MESSAGE_PATCH) {
            if (activeRole == Role.student)
                showStudentMessagePanel();
            else if (activeRole == Role.tutor)
                showTutorMessagePanel();
        }
    }
}
