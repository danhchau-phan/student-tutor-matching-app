package studentview;
import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import mainview.MouseClickListener;
import mainview.Observer;
import mainview.RemovablePanel;
import model.Bid;
import model.BidResponse;
import model.EventType;
import model.Message;
import java.awt.Component;

/**
 * The View where Student can view all responses to a specific match requests.
 * If the match request is type open, the student can View all bids.
 * If the match request is type close, the student can View all incoming messages..
 */
public class StudentResponseView extends RemovablePanel {
	private Bid bid;
	private JList<BidResponse> responseList;
	private JList<Message> messageList;
	private List<BidResponse> responses;
	private List<Message> messages;
	
	public StudentResponseView(Bid bid) {
		super(new BorderLayout());
		this.bid = bid;
		placeComponents();
	}
	
	private void placeComponents() {
		// renders JList
		if (bid.getType() == Bid.BidType.open) {

			DefaultListModel<BidResponse> model = new DefaultListModel<>();
			responses = bid.getResponse();
			for (BidResponse r : responses)
				model.addElement(r);
			responseList = new JList<>(model);
			responseList.setCellRenderer(new ResponseCellRenderer());
		
		} else if (bid.getType() == Bid.BidType.close) {
			DefaultListModel<Message> model = new DefaultListModel<>();
			messages = bid.getMessages();

			for (Message m : messages)
				model.addElement(m);
			messageList = new JList<>(model);
			messageList.setCellRenderer(new MessageCellRenderer());
		}
		// adds JList to panel
		if (bid.getType() == Bid.BidType.close) {
			JScrollPane scrollp = new JScrollPane(messageList);
			this.add(scrollp);
		} else {
			JScrollPane scrollp = new JScrollPane(responseList);
			this.add(scrollp);
		}
		
	}

	public void setResponseListener(MouseClickListener listener) {
		if (bid.getType() == Bid.BidType.close) {
			this.messageList.addMouseListener(listener);
		} else {
			this.responseList.addMouseListener(listener);
		}
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

	@Override
	public void update(EventType e) {
		placeComponents();
	}
}
