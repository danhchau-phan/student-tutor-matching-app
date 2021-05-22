package mainview;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import model.Bid;
import model.BidAddInfo;
import model.BidResponse;
import model.Contract;
import model.ContractAddInfo;
import model.EventType;
import model.Message;
import model.Subject;
import model.User;
import studentview.CreateRequest;
import studentview.StudentAllBids;
import studentview.StudentAllContracts;
import studentview.StudentMessageView;
import studentview.StudentResponseView;
import studentview.StudentView;
import tutorview.TutorAllBids;
import tutorview.TutorAllContracts;
import tutorview.TutorView;

public class Controller {
    private Display display;
    private User user;
    // private List<Bid> allBids;
    // private List<Contract> contractsAsFirstParty, contractsAsSecondParty;
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
    private TutorView tutorView;
    private TutorAllBids tutorAllBids;
    private TutorAllContracts tutorAllContracts;

    public Controller() {
        this.start();
    }

    public void start() {
        this.display = new Display();
        AuthenticationView authView = new AuthenticationView(display);
        
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
                    initViews();
                    
                    homeView.display();
    			} else {
					Utils.INVALID_USER.show();
				}
			}

        });

        authView.display();
    }

    private void initViews() {
        assert (this.user != null);
        // this.allBids = tempBid.getAll();
        // this.contractsAsFirstParty = (new Contract()).getAllContractsAsFirstParty(this.user.getId());
        // this.contractsAsSecondParty = Contract.getAllContractsAsSecondParty(this.user.getId());

        initStudentViews();
        subscribeStudentViews();
        initTutorViews();
        subscribeTutorViews();
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
        studentView.setSwitchPanelListener(studentView.main, studentView.viewContracts,studentAllContracts);
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

    private void subscribeStudentViews() {
        subscriberBid.subscribe(studentAllBids);
        subscriberContract.subscribe(studentAllContracts);
    }

    private void initTutorViews() {
        assert (this.user != null);
        this.homeView = new HomeView(display, user);
        this.tutorView = new TutorView(display, user);
        this.tutorAllBids = new TutorAllBids(user, subscriberBid);
        this.tutorAllContracts = new TutorAllContracts(user, subscriberContract);
        this.createBid = new CreateBid();

        homeView.setSwitchPanelListener(homeView.panel, homeView.studentButton, studentView);
        studentView.setSwitchPanelListener(studentView.main, studentView.homeButton, homeView);
        studentView.setSwitchPanelListener(studentView.main, studentView.viewAllBids, studentAllBids);
        studentView.setSwitchPanelListener(studentView.main, studentView.viewContracts,studentAllContracts);
        studentView.setSwitchPanelListener(studentView.main, studentView.createBid, createRequest);
    }

    private void subscribeTutorViews() {
        subscriberBid.subscribe(tutorAllBids);
        subscriberContract.subscribe(tutorAllContracts);
    }
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

    class SendMessageListener implements MouseClickListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            String content = studentMessage.getChatContent();
            activeMessage.addNewMessage(content, user.getUsername());
            showStudentMessagePanel();
        }
    }

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
