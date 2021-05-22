package tutorview;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import mainview.*;
import model.*;
import studentview.StudentResponseView;

/**
 * This is the View where Tutor view the response to a specific match request.
 * If the match request is type open, the Tutor sees all competing bids from other Tutors.
 * If the match request is type close, the Tutor is redirected to TutorMessageView.
 */
public class TutorResponseView extends JPanel {
	private Bid bid;
	private JList<BidResponse> responseList;
	private JList<Message> messageList;
	private List<BidResponse> responses;
	private List<Message> messages;

	public JButton createBid;
	public JButton buyOut;
	public JButton subscribeBid;

	public TutorResponseView(Bid bid) {
		super(new BorderLayout());
		this.bid = bid;
		placeComponents();
	}

	private void placeComponents() {
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
//		else if (bid.getType() == Bid.BidType.close) {
////			display.removePanel(this.main);
//			new TutorMessageView(user, );
////
////			DefaultListModel<Message> model = new DefaultListModel<>();
////			messages = bid.getMessages();
////
////			for (Message m : messages)
////				model.addElement(m);
////			messageList = new JList<>(model);
////			messageList.setCellRenderer(new MessageCellRenderer());
//		}

		// adds JList to panel
		if (bid.getType() == Bid.BidType.close) {
			JScrollPane scrollp = new JScrollPane(messageList);
			this.add(scrollp);
		} else {
			JScrollPane scrollp = new JScrollPane(responseList);
			this.add(scrollp);
		}


		// Bottom Panel with buttons - Create Bid Button and Subscribe Button (correspond to Request)
		JPanel panel = new JPanel(new FlowLayout());
//		panel.add(createBid);
		if (bid.getType() == Bid.BidType.open) {
			panel.add(createBid);
			panel.add(buyOut);
			panel.add(subscribeBid);
		}
		this.add(panel, BorderLayout.SOUTH);


//		for (JButton b : buttons) {
//			panel.add(b);
//		}

//		if (bid.getType() == Bid.BidType.open) {
//			JButton createBid = new JButton("Create Bid");
//			JButton buyOut = new JButton("Buy Out Bid");
//
//			ArrayList<JComponent> panels = new ArrayList<JComponent> ();
//
//			List<BidResponse> responses = bid.getResponse();
//			for (BidResponse r : responses) {
//				JPanel panel = new JPanel();
//				JEditorPane eP = new JEditorPane();
//
//				eP.setText(r.toString());
//				panel.add(eP);
//			}
//			JPanel midPanel = new ListPanel(panels);
//	        main.add(midPanel);
//
//			JPanel bottomP = new JPanel(new FlowLayout() );
//			bottomP.add(createBid);
//			bottomP.add(buyOut);
//	        main.add(bottomP, BorderLayout.SOUTH);
//
//	        createBid.addMouseListener(new MouseClickListener() {
//				@Override
//				public void mouseClicked(MouseEvent e) {
//					if (bid.checkEligibility(user)) {
//						display.removePanel(main);
//						View cBV = new CreateBidView(display, user, bid);
//						cBV.display();
//					} else {
//						Utils.INSUFFICIENT_COMPETENCY.show();
//					}
//				}});
//
//	        buyOut.addMouseListener(new MouseClickListener() {
//				@Override
//				public void mouseClicked(MouseEvent e) {
//					if (bid.checkEligibility(user)) {
//
//						Contract.postContract(bid.getInitiatorId(), user.getId(),
//								bid.getSubject().getId(),
//								new ContractAddInfo(true, true));
//						Bid.closeDownBid(bid.getId());
//						Utils.SUCCESS_CONTRACT_CREATION.show();
//					} else {
//						Utils.INSUFFICIENT_COMPETENCY.show();
//					}
//				}
//	        });
//			this.display.setVisible();
//		} else if (bid.getType() == Bid.BidType.close) {
//			display.removePanel(this.main);
//			(new TutorMessageView(display, user, bid)).display();
//		}
	}

	public void setResponseListener(MouseClickListener listener) {
		if (bid.getType() == Bid.BidType.close) {
			this.messageList.addMouseListener(listener);
		} else {
			this.responseList.addMouseListener(listener);
		}
	}

	public void setCreateBidListener(MouseClickListener listener) {
		this.getCreateBid().addMouseListener(listener);
	}

	public void setBuyOutListener(MouseClickListener listener) {
		this.getBuyOut().addMouseListener(listener);
	}

	public void setSubscribeBidListener(MouseClickListener listener) {
		this.getSubscribeBid().addMouseListener(listener);
	}

	public int getSelectedMessageIndex() {
		return this.messageList.getSelectedIndex();
	}

	public BidResponse getSelectedResponse() {
		return responseList.getSelectedValue();
	}

	public Message getSelectedMessage() {
		return messageList.getSelectedValue();
	}

	private class ResponseCellRenderer extends JPanel implements ListCellRenderer<BidResponse> {

		@Override
		public Component getListCellRendererComponent(JList<? extends BidResponse> list, BidResponse value, int index,
													  boolean isSelected, boolean cellHasFocus) {
			this.removeAll();

			JPanel panel = new JPanel(new BorderLayout());
			JEditorPane eP = new JEditorPane();
			JButton bT = new JButton("Select Bid");

			eP.setText(value.toString());
			panel.add(eP);
			panel.add(bT, BorderLayout.EAST);

			this.add(panel);
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

	public JButton getCreateBid() {
		return createBid;
	}

	public void setCreateBid(JButton createBid) {
		this.createBid = createBid;
	}

	public JButton getBuyOut() {
		return buyOut;
	}

	public void setBuyOut(JButton buyOut) {
		this.buyOut = buyOut;
	}

	public JButton getSubscribeBid() {
		return subscribeBid;
	}

	public void setSubscribeBid(JButton subscribeBid) {
		this.subscribeBid = subscribeBid;
	}
}
