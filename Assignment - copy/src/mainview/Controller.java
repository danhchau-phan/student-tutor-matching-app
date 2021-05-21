package mainview;

import java.awt.event.MouseEvent;
import java.util.List;

import model.Bid;
import model.Contract;
import model.Message;
import model.User;
import studentview.CreateRequestView;
import studentview.StudentAllBidsView;
import studentview.StudentAllContractsView;
import studentview.StudentView;

public class Controller {
    private Display display;
    private User user;
    private List<Bid> initiatedBids, allBids;
    // private List<Message> messages;
    private List<Contract> contractsAsFirstParty, contractsAsSecondParty;
    private HomeView homeView;
    private StudentAllBidsView studentAllBidsView;
    private StudentAllContractsView studentAllContractsView;
    private StudentView studentView;
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
    				// (new HomeView(display, user)).display();
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
        this.studentAllBidsView = new StudentAllBidsView(display, user);
        this.studentAllContractsView = new StudentAllContractsView(display, user);

        homeView.addSwitchPanelListener(homeView.panel, homeView.studentButton, studentView);

        studentView.addSwitchPanelListener(studentView.main, studentView.homeButton, homeView);
        studentView.addSwitchPanelListener(studentView.main, studentView.viewAllBids, studentAllBidsView);
        studentView.addSwitchPanelListener(studentView.main, studentView.viewContracts, studentAllContractsView);
        studentView.addSwitchPanelListener(studentView.main, studentView.createBid, new CreateRequestView(display, user));
        
    }

}
