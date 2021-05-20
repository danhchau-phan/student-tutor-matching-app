package mainview;
import javax.swing.JButton;
import javax.swing.JPanel;

import tutorview.TutorView;
import model.User;
import studentview.StudentView;

import java.awt.FlowLayout;
public class HomeView extends View {

	private User user;
	
	/**
		 * Home view which directs user to different site
		 * @param display the shared display
		 * @param user the loged-in user
		 */
	public HomeView(Display display, User user) {
		
		super(display);
		this.user = user;
	}

	protected void placeComponents() {
		JPanel panel = createPanel(new FlowLayout());
		JButton studentButton = new JButton("Student site");
		JButton tutorButton = new JButton("Tutor site");
		JButton logOut = new JButton("Log out");
        
        if (user.isStudent()) { 
        	panel.add(studentButton);
        	addSwitchPanelListener(panel, studentButton, new StudentView(display, user));
        }
        if (user.isTutor()) {
        	panel.add(tutorButton);
        	addSwitchPanelListener(panel, tutorButton, new TutorView(display, user));
        }  
        
        panel.add(logOut);
        // addSwitchPanelListener(panel, logOut, new AuthenticationView(display));
        this.display.setVisible();
	}
}
