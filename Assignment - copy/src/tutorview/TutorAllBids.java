package tutorview;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import java.awt.Component;

import java.awt.BorderLayout;
import model.Bid;
import model.User;
import mainview.MouseClickListener;
import mainview.Observer;

/**
 * This is the View for Tutor to see all available (unclosed, unexpired) mathc requests
 */
public class TutorAllBids extends JPanel implements Observer {
	private List<Bid> bids;
	private User user;
	private Bid subscriber;
	private JList<Bid> bidList;
	
	public TutorAllBids(User user, Bid subscriber) {
		super(new BorderLayout());
		this.user = user;
		this.bids = subscriber.getAll();
		placeComponents();
	}

	protected void placeComponents() {
		// ArrayList<JComponent> panels = new ArrayList<JComponent> ();
		
		// for (Bid b : bids) {
		// 	String text = b.toString();
		// 	JPanel panel = new JPanel();
		// 	JTextArea tA = new JTextArea();
		// 	tA.setText(text);
		// 	panel.add(tA);
		// 	tA.setEditable(false);
			
		// 	this.setSwitchPanelListener(main, tA, new TutorResponseView(display, user, b));
		// 	panels.add(panel);
		// }
		
		// JPanel midPanel = new ListPanel(panels);
        // this.add(midPanel);
		// JScrollPane scrollp = new JScrollPane(midPanel);
		// this.add(scrollp);

		DefaultListModel<Bid> model = new DefaultListModel<Bid>();
		for (Bid b : bids)
			model.addElement(b);
		bidList = new JList<Bid>(model);
		bidList.setCellRenderer(new CellRenderer());
		JScrollPane scrollp = new JScrollPane(bidList);
		this.add(scrollp);
	}

	@Override
	public void update() {
		this.bids = subscriber.getAll();
		this.placeComponents();
	}

	public void setListListener(MouseClickListener listener) {
		this.bidList.addMouseListener(listener);
	}
	
	private class CellRenderer extends JPanel implements ListCellRenderer<Bid> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Bid> list, Bid value, int index,
				boolean isSelected, boolean cellHasFocus) {
			this.removeAll();
			String text = value.toString();
			JTextArea tA = new JTextArea();
			tA.setText(text);
			tA.setEditable(false);
			this.add(tA);
			return this;
		}
	}
}
