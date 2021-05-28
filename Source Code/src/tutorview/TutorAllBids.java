package tutorview;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import model.EventType;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import mainview.MouseClickListener;
import mainview.Observer;
import mainview.RemovablePanel;
import model.Bid;
import model.Model;
import model.User;

/**
 * This is the View for Tutor to see all available (unclosed, unexpired) match requests
 */
public class TutorAllBids extends RemovablePanel {
	private List<Bid> bids;
	private JList<Bid> bidList;

	public TutorAllBids(List<Bid> bids) {
		super(new BorderLayout());
		this.setBackground(Color.BLUE);
		this.bids = bids;
		placeComponents();
	}

	protected void placeComponents() {
		this.removeAll();
		DefaultListModel<Bid> model = new DefaultListModel<Bid>();
		for (Bid b : bids)
			model.addElement(b);
		bidList = new JList<Bid>(model);
		bidList.setCellRenderer(new CellRenderer());
		JScrollPane scrollp = new JScrollPane(bidList);
		this.add(scrollp);
	}

	public Bid getSelectedBid() {
		return this.bidList.getSelectedValue();
	}

	public void setListListener(MouseClickListener listener) {
		this.bidList.addMouseListener(listener);
	}

	@Override
	public void update(EventType e) {
		this.placeComponents();
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
