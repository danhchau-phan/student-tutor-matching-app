package studentview;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import mainview.Display;
import mainview.HomeView;
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
	public JPanel main = createPanel(new BorderLayout());
	
	public StudentView(Display display, User user) {
		super(display);
		this.user = user;
	}
	
	protected void placeComponents() {
		main = createPanel(new BorderLayout());
		this.display.setVisible();
		// addSwitchPanelListener(main, homeButton, new HomeView(display, user));
		// addSwitchPanelListener(main, createBid, new CreateRequestView(display, user));
		// addSwitchPanelListener(main, viewAllBids, new StudentAllBidsView(display, user));
		// addSwitchPanelListener(main, viewContracts, new StudentAllContractsView(display, user));
		showNavigationPane(display, main, new JButton[]{homeButton, createBid, viewAllBids, viewContracts});
		this.display.setVisible();
	}
	
	
}
