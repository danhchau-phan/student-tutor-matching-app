package mainview;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import model.*;
import studentview.CreateRequest;
import studentview.StudentAllBids;
import studentview.StudentAllContracts;
import studentview.StudentMessageView;
import studentview.StudentResponseView;
import studentview.StudentView;
import tutorview.*;

public class Controller {
    private Display display;
    private User user;
//    private List<Bid> initiatedBids, allBids;
//    private List<Contract> contractsAsFirstParty, contractsAsSecondParty;

    private Bid activeBid, subscriberBid = new Bid();
    private Contract activeContract, subscriberContract = new Contract();
    private Message activeMessage;

    private HomeView homeView;
//    private Bid activeBid;
//    private Message activeMessage;

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



    
    // private HashMap<EventType, List<Observer>> observers;

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
    			    display.removePanel(authView.panel);
                    // maybe admin??
                    if (user.isStudent()) {
//                        initModels();
                        initStudentViews();
//                        subscribeViews();
//                        homeView.display();
                    }
                    if (user.isTutor()) {
//                        display.removePanel(authView.panel);
//                        initModels();
                        initTutorViews();
//                        subscribeViews();
                    }

                    homeView.display();
    			} else {
					Utils.INVALID_USER.show();
				}
			}
        });

        authView.display();
    }

//
//    private void initModels() {
//        assert (this.user != null);
//        this.initiatedBids = user.getInitiatedBids();
//        this.allBids = Bid.getAll();
//        this.contractsAsFirstParty = Contract.getAllContractsAsFirstParty(this.user.getId());
//        this.contractsAsSecondParty = Contract.getAllContractsAsSecondParty(this.user.getId());
//    }

//    private void initViews() {
//        assert (this.user != null);
//        // this.allBids = tempBid.getAll();
//        // this.contractsAsFirstParty = (new Contract()).getAllContractsAsFirstParty(this.user.getId());
//        // this.contractsAsSecondParty = Contract.getAllContractsAsSecondParty(this.user.getId());
//
//        initStudentViews();
//        subscribeStudentViews();
//        initTutorViews();
//        subscribeTutorViews();
//    }


    private void initTutorViews() {
        assert (this.user != null);
        this.homeView = new HomeView(display, user);
        this.tutorView = new TutorView(display, user);
        this.tutorAllBids = new TutorAllBids(user, new Bid().getAll());
        this.tutorAllContracts = new TutorAllContracts(user, subscriberContract);

        homeView.setSwitchPanelListener(homeView.panel, homeView.tutorButton, tutorView);
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
                    tutorResponse = new TutorResponseView(activeBid);
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
    }

    private void initStudentViews() {
        assert (this.user != null);
        this.homeView = new HomeView(display, user);
        this.studentView = new StudentView(display, user);
        this.studentAllBids = new StudentAllBids(user);
        this.studentAllContracts = new StudentAllContracts(user, subscriberContract);
        this.createRequest = new CreateRequest();

        homeView.setSwitchPanelListener(homeView.panel, homeView.studentButton, studentView);
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

//    public void

    private void subscribeViews() {
        // user.subscribe(studentAllBids);
    }

    // Display the Active Message SubPanel correspond to the ActiveBid
    private void showStudentMessagePanel() {
        studentMessage = new StudentMessageView(user, activeMessage, activeBid);
        studentMessage.setSendMessageListener(new SendMessageListener());
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



    /** JButton Listener on each SubPanel (tutorAllBid, studentAllBid etc.)*/


    /** Response portals: MessageList View and Response View*/
    class ResponseListener implements MouseClickListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            if (activeBid.getType() == Bid.BidType.close) {
                activeMessage = studentResponse.getSelectedMessage();
                showStudentMessagePanel();
            } else {
                BidResponse selectedResponse = studentResponse.getSelectedResponse();
                if (selectedResponse == null)
                    return;
                subscriberContract.postContract(user.getId(),
                        selectedResponse.getBidderId(),
                        activeBid.getSubject().getId(),
                        new ContractAddInfo(true, false));
                subscriberBid.closeDownBid(activeBid.getId());
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
            subscriberBid.closeDownBid(activeBid.getId());
            Utils.SUCCESS_CONTRACT_CREATION.show();

        }

    }

    class CreateBidListener implements MouseClickListener{
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            if (activeBid.checkEligibility(user)) {
                createBid = new CreateBid();

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
                            Utils.PLEASE_FILL_IN.show();
                        }
                    }
                });

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
                activeBid.closeDownBid(activeBid.getId());
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

    class SendMessageListener implements MouseClickListener {
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
            subscriberBid.closeDownBid(activeBid.getId());
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
}
