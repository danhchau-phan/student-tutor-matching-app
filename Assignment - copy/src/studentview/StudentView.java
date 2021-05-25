package studentview;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import mainview.Display;
import mainview.NavigationPane;
import mainview.View;
import model.User;

/**
 * The view that is in charges of redirecting the student to other views
 */
public class StudentView extends View implements NavigationPane {
	protected User user;
	public JButton createBid = new JButton("Create Match Request");
	public JButton viewAllBids = new JButton("View All Bids");
	public JButton viewContracts = new JButton("View Contracts");
	public JButton reuseContracts = new JButton("Reuse Contracts");
	public JPanel main = new JPanel(new BorderLayout());
	
	public StudentView(Display display, User user) {
		super(display);
		this.user = user;
	}
	
	protected void placeComponents() {
		
		showNavigationPane(display, main, new JButton[]{homeButton, createBid, viewAllBids, viewContracts, reuseContracts});
		display.createPanel(main);
		this.display.setVisible();
	}
	
	
}
