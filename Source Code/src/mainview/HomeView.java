package mainview;
import javax.swing.JButton;
import javax.swing.JPanel;

import tutorview.TutorView;
import model.User;
import studentview.StudentView;

import java.awt.FlowLayout;
public class HomeView extends View {

	private User user;
	// JPanel panel = createPanel(new FlowLayout());
	// JButton studentButton = new JButton("Student site");
	// JButton tutorButton = new JButton("Tutor site");
	// JButton logOut = new JButton("Log out");
	public JPanel panel;
	public JButton studentButton;
	public JButton tutorButton;
	public JButton logOut;
	
	/**
		 * Home view which directs user to different site
		 * @param display the shared display
		 * @param user the loged-in user
		 */
	public HomeView(Display display, User user) {
		
		super(display);
		this.user = user;
		
		studentButton = new JButton("Student site");
		tutorButton = new JButton("Tutor site");
		logOut = new JButton("Log out");
		panel = new JPanel(new FlowLayout());
        if (user.isStudent())
        	panel.add(studentButton);
        if (user.isTutor())
        	panel.add(tutorButton);
        panel.add(logOut);
	}

	protected void placeComponents() {
		display.createPanel(panel);
		this.display.setVisible();
	}
}
