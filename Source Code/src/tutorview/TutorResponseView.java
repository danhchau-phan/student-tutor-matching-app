package tutorview;
import java.awt.*;
import java.util.List;

import javax.swing.*;

import mainview.*;
import model.*;

/**
 * This is the View where Tutor view the response to a specific match request.
 * If the match request is type open, the Tutor sees all competing bids from other Tutors.
 * If the match request is type close, the Tutor is redirected to TutorMessageView.
 */
public class TutorResponseView extends RemovablePanel {
	private Bid bid;
	private JList<BidResponse> responseList;
	private List<BidResponse> responses;

	public JButton createBid;
	public JButton buyOut;
	public JButton subscribeBid;

	public TutorResponseView(Bid bid) {
		super(new BorderLayout());
		this.bid = bid;
		placeComponents();
	}

	public TutorResponseView() {
		super(new BorderLayout());
	}

	private void placeComponents() {
		this.removeAll();
		this.createBid = new JButton("Create Bid");
		this.buyOut = new JButton("Buy Out Bid");
		this.subscribeBid = new JButton("Subscribe Bid");
		if (bid.getType() == Bid.BidType.open) {
			
			DefaultListModel<BidResponse> model = new DefaultListModel<>();
			responses = bid.getResponse();
			for (BidResponse r : responses)
				model.addElement(r);
			responseList = new JList<>(model);
			responseList.setCellRenderer(new ResponseCellRenderer());

		}


		if (bid.getType() == Bid.BidType.close) {
			// JScrollPane scrollp = new JScrollPane(messageList);
			// this.add(scrollp);
		} else {
			JScrollPane scrollp = new JScrollPane(responseList);
			this.add(scrollp);
		}


		// Bottom Panel with buttons - Create Bid Button and Subscribe Button (correspond to Request)
		JPanel panel = new JPanel(new FlowLayout());
		if (bid.getType() == Bid.BidType.open) {
			panel.add(createBid);
			panel.add(buyOut);
			panel.add(subscribeBid);
		}
		this.add(panel, BorderLayout.SOUTH);
	}

	public void setCreateBidListener(MouseClickListener listener) {
		this.createBid.addMouseListener(listener);
	}

	public void setBuyOutListener(MouseClickListener listener) {
		this.buyOut.addMouseListener(listener);
	}

	public void setSubscribeBidListener(MouseClickListener listener) {
		this.subscribeBid.addMouseListener(listener);
	}

	public BidResponse getSelectedResponse() {
		return responseList.getSelectedValue();
	}



	private class ResponseCellRenderer extends JPanel implements ListCellRenderer<BidResponse> {

		@Override
		public Component getListCellRendererComponent(JList<? extends BidResponse> list, BidResponse value, int index,
													  boolean isSelected, boolean cellHasFocus) {
			this.removeAll();
			JEditorPane eP = new JEditorPane();
			eP.setText(value.toString());
			this.add(eP);
			return this;
		}
	}

	private class MessageCellRenderer extends JPanel implements ListCellRenderer<Message> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Message> list, Message value, int index,
													  boolean isSelected, boolean cellHasFocus) {
			this.removeAll();

			JPanel panel = new JPanel(new BorderLayout());
			JEditorPane eP = new JEditorPane();
			JButton bT = new JButton("Start Conversation");

			eP.setText(value.toString());
			panel.add(eP);
			panel.add(bT, BorderLayout.EAST);
			this.add(panel);
			return this;
		}
	}

	@Override
	public void update(EventType e) {
		placeComponents();
	}

	public void setBid(Bid b) {
		this.bid = b;
		placeComponents();
	}
}
