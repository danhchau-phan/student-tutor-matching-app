package tutorview;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mainview.Display;
import mainview.ListPanel;
import model.Bid;
import model.User;

/**
 * This is the View for Tutor to see all available (unclosed, unexpired) mathc requests
 */
class TutorAllBidsView extends TutorView {
	List<Bid> bids;
	
	public TutorAllBidsView(Display display, User user) {
		super(display, user);
	}

	protected void placeComponents() {
		super.placeComponents();
		bids = Bid.getAll();
		ArrayList<JComponent> panels = new ArrayList<JComponent> ();
		
		for (Bid b : bids) {
			String text = b.toString();
			JPanel panel = new JPanel();
			JTextArea tA = new JTextArea();
			tA.setText(text);
			panel.add(tA);
			tA.setEditable(false);
			
			this.addSwitchPanelListener(main, tA, new TutorResponseView(display, user, b));
			panels.add(panel);
		}
		
		JPanel midPanel = new ListPanel(panels);
        main.add(midPanel);
		JScrollPane scrollp = new JScrollPane(midPanel);
		main.add(scrollp);
		this.display.setVisible();
		
	}
}
