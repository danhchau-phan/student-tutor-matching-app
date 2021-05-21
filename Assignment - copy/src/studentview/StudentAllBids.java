package studentview;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import java.awt.BorderLayout;

import mainview.Display;
import mainview.ListPanel;
import mainview.NavigationPane;
import mainview.View;
import model.User;
import model.Bid;

/**
 * View that allows the Student to see all unexpired and unclosed match requests created by them
 */
public class StudentAllBids extends JPanel{
	List<Bid> bids;
	User user;
	// public JPanel main = new JPanel(new BorderLayout());
	// public StudentAllBidsView(Display display, User user, NavigationPane navPane) {
	// 	super(display);
	// 	this.user = user;
	// }

	public StudentAllBids(User user) {
		super(new BorderLayout());
		this.user = user;
		placeComponents();
	}
	
	private void placeComponents() {
		// super.placeComponents();

		bids = user.getInitiatedBids();
		ArrayList<JComponent> panels = new ArrayList<JComponent> ();
		for (Bid b : bids) {
			String text = b.toString();
			JPanel panel = new JPanel();
			JTextArea tA = new JTextArea();
			tA.setText(text);
			panel.add(tA);
			tA.setEditable(false);
			
			// this.addSwitchPanelListener(main, tA, new StudentResponseView(display, user, b));
			panels.add(panel);
		}
		
		JPanel midPanel = new ListPanel(panels);
        // main.add(midPanel);
		JScrollPane scrollp = new JScrollPane(midPanel);
		this.add(midPanel);
		this.add(scrollp);
		// main.add(scrollp);
		// this.display.setVisible();
	}

}
