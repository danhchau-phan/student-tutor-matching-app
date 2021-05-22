package tutorview;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import mainview.MouseClickListener;
import mainview.Observer;
import model.Bid;
import model.Model;

/**
 * This is the View for Tutor to see all available (unclosed, unexpired) mathc requests
 */
public class TutorAllBidsView extends JPanel implements Observer {
	List<Bid> bids;
	JList<Bid> bidList;
	
//	public TutorAllBidsView(Display display, User user) {
//		super(display, user);
//	}

	public TutorAllBidsView(List<Bid> bids) {
		super(new BorderLayout());
		this.setBackground(Color.BLUE);
		this.bids = bids;
		placeComponents();
	}

	protected void placeComponents() {
		DefaultListModel<Bid> model = new DefaultListModel<Bid>();
		for (Bid b : bids)
			model.addElement(b);
		bidList = new JList<Bid>(model);
		bidList.setCellRenderer(new CellRenderer());
		JScrollPane scrollp = new JScrollPane(bidList);
		this.add(scrollp);
//		super.placeComponents();
//		bids = Bid.getAll();
//		ArrayList<JComponent> panels = new ArrayList<JComponent> ();
//
//		for (Bid b : bids) {
//			String text = b.toString();
//			JPanel panel = new JPanel();
//			JTextArea tA = new JTextArea();
//			tA.setText(text);
//			panel.add(tA);
//			tA.setEditable(false);
//
//			this.setSwitchPanelListener(main, tA, new TutorResponseView(display, user, b));
//			panels.add(panel);
//		}
//
//		JPanel midPanel = new ListPanel(panels);
//        main.add(midPanel);
//		JScrollPane scrollp = new JScrollPane(midPanel);
//		main.add(scrollp);
//		this.display.setVisible();
		
	}

	public int getSelectedIndex() {
		return this.bidList.getSelectedIndex();
	}

	// Listener for tutor response
	public void setListListener(MouseClickListener listener) {
		this.bidList.addMouseListener(listener);
	}

	@Override
	public void update(Model model) {

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
