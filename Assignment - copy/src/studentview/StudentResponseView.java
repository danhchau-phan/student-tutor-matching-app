package studentview;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mainview.ListPanel;
import mainview.MouseClickListener;
import mainview.Utils;
import model.Bid;
import model.BidResponse;
import model.Contract;
import model.ContractAddInfo;
import model.Message;
import model.User;

/**
 * The View where Student can view all responses to a specific match requests.
 * If the match request is type open, the student can View all bids.
 * If the match request is type close, the student can View all incoming messages..
 */
public class StudentResponseView extends JPanel {
	private Bid bid;
	private User user;
	private JList<BidResponse> responseList;
	private JList<Message> messageList;
	
	public StudentResponseView(User user, Bid bid) {
		super(new BorderLayout());
		this.bid = bid;
		this.user = user;
		placeComponents();
	}
	
	private void placeComponents() {
		ArrayList<JComponent> panels = new ArrayList<JComponent> ();
		
		if (bid.getType() == Bid.BidType.open) {
			Bid.updateBid(bid);
			List<BidResponse> responses = bid.getResponse();
			for (BidResponse r : responses) {
				JPanel panel = new JPanel(new BorderLayout());
				JEditorPane eP = new JEditorPane();
				JButton bT = new JButton("Select Bid"); 
				
				eP.setText(r.toString());
				panel.add(eP);
				panel.add(bT, BorderLayout.EAST);
				bT.addMouseListener(new MouseClickListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						Contract.postContract(user.getId(), 
								r.getBidderId(), 
								bid.getSubject().getId(),
								new ContractAddInfo(true, false));
						Bid.closeDownBid(bid.getId());
						Utils.SUCCESS_CONTRACT_CREATION.show();
						
					}});
				panels.add(panel);
			}
		} else if (bid.getType() == Bid.BidType.close) {
			List<Message> messages = bid.getMessages();
			for (Message m : messages) {
				JPanel panel = new JPanel(new BorderLayout());
				JEditorPane eP = new JEditorPane();
				JButton bT = new JButton("Start Conversation");
				
				eP.setText(m.toString());
				panel.add(eP);
				panel.add(bT, BorderLayout.EAST);
				// bT.addMouseListener(new MouseClickListener() {
				// 	@Override
				// 	public void mouseClicked(MouseEvent e) {
				// 		display.removePanel(main);
				// 		(new StudentMessageView(display, user, m, bid)).display();
				// 	}}); 
				panels.add(panel);
			}
		}
		
		JPanel midPanel = new ListPanel(panels);
        this.add(midPanel);
		JScrollPane scrollp = new JScrollPane(midPanel);
		this.add(scrollp);
	}

	public void setOpenBidListener(MouseClickListener listener) {
		if (bid.getType() == Bid.BidType.close) {
			this.messageList.addMouseListener(listener);
		} else {
			this.responseList.addMouseListener(listener);
		}
	}
}
