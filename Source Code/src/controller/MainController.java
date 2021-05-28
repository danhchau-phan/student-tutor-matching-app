package controller;

import java.awt.event.MouseEvent;

import mainview.AuthenticationView;
import mainview.Display;
import mainview.HomeView;
import mainview.MouseClickListener;
import mainview.Observer;
import mainview.Utils;
import model.*;

public class MainController implements Observer{

    private Display display;
    private User user;

    private HomeView homeView;

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
                    homeView.studentButton.addMouseListener((new StudentController(display, user, homeView)).new StudentRoleActivationListener());
                    homeView.tutorButton.addMouseListener((new TutorController(display, user, homeView)).new TutorRoleActivationListener());
                    
                    homeView.display();
    			} else {
					Utils.INVALID_USER.show();
				}
			}
        });

        authView.display();
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
