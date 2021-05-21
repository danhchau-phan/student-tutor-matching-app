package mainview;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import model.Bid;
import model.BidAddInfo;
import model.Contract;
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

public class Controller {
    private Display display;
    private User user;
    private List<Bid> initiatedBids, allBids;
    private List<Contract> contractsAsFirstParty, contractsAsSecondParty;
    private HomeView homeView;
    private StudentAllBids studentAllBids;
    private StudentAllContracts studentAllContracts;
    private CreateRequest createRequest;
    private StudentView studentView;
    private StudentResponseView studentResponse;
    private StudentMessageView studentMessage;
    private Bid activeBid;

    
    private HashMap<EventType, List<Observer>> observers;

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
                    initModels();
                    initViews();
                    homeView.display();
    			} else {
					Utils.INVALID_USER.show();
				}
			}
        });

        authView.display();
    }

    private void initModels() {
        assert (this.user != null);
        this.initiatedBids = user.getInitiatedBids();
        this.allBids = Bid.getAll();
        this.contractsAsFirstParty = Contract.getAllContractsAsFirstParty(this.user.getId());
        this.contractsAsSecondParty = Contract.getAllContractsAsSecondParty(this.user.getId());
    }

    private void initViews() {
        assert (this.user != null);
        this.homeView = new HomeView(display, user);
        this.studentView = new StudentView(display, user);
        this.studentAllBids = new StudentAllBids(initiatedBids);
        this.studentAllContracts = new StudentAllContracts(contractsAsFirstParty);
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
					Bid.postBid(t, user.getId(), Subject.getSubjectId(sj), addInfo);
					Utils.SUCCESS_MATCH_REQUEST.show();
				} catch (NumberFormatException nfe) {
					Utils.INVALID_FIELDS.show();
				} catch (NullPointerException npe) {
					Utils.PLEASE_FILL_IN.show();
				}
			}});

        
    }

    

}
