package studentview;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;

import mainview.ListPanel;
import mainview.MouseClickListener;
import model.Bid;

/**
 * View that allows the Student to see all unexpired and unclosed match requests created by them
 */
public class StudentAllBids extends JPanel{
	List<Bid> bids;

	public StudentAllBids(List<Bid> bids) {
		super(new BorderLayout());
		this.setBackground(Color.BLUE);
		this.bids = bids;
		placeComponents();
	}
	
	private void placeComponents() {
		ArrayList<JComponent> panels = new ArrayList<JComponent> ();
		for (Bid b : bids) {
			String text = b.toString();
			JPanel panel = new JPanel();
			JTextArea tA = new JTextArea();
			tA.setText(text);
			panel.add(tA);
			tA.setEditable(false);
			tA.addMouseListener(new MouseClickListener(){
				@Override
				public void mouseClicked(MouseEvent e) {
										
				}
			});
			// this.addSwitchPanelListener(main, tA, new StudentResponseView(display, user, b));
			panels.add(panel);
		}
		
		JPanel midPanel = new ListPanel(panels);
		JScrollPane scrollp = new JScrollPane(midPanel);
		this.add(midPanel);
		this.add(scrollp);
	}

	public void setListListener(MouseClickListener listener) {
		
	}
}
