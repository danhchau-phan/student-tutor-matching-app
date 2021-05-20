package mainview;

import java.awt.event.MouseEvent;
import java.util.List;

import model.Bid;
import model.Contract;
import model.Message;
import model.User;

public class Controller {
    private Display display;
    private User user;
    private List<Bid> initiatedBids, allBids;
    // private List<Message> messages;
    // private List<Contract> contracts;
    private View homeView;
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
        
    }

    private void initViews() {
        assert (this.user != null);
    }

}
