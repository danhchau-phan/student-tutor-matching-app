package studentview;
import java.awt.BorderLayout;
import mainview.Observer;
import mainview.RemovablePanel;
import model.EventType;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mainview.MessageView;
import mainview.MouseClickListener;
import model.Message;
import model.User;
import model.Bid;

/**
 * The View where Student chats with the Tutor in close bidding
 */
public class StudentMessageView extends RemovablePanel implements MessageView{

	private Message message;
	private Bid bid;
	private JButton send = new JButton("Send");
	private JButton selectBid = new JButton("Select bid");
	private JTextField chatBox;

	public StudentMessageView(Message message, Bid bid) {
		this.bid = bid;
		this.message = message;
	}


	protected void placeComponents() {
		
		Message mS = this.message;
	
		JTextArea log = this.getLogArea(mS.getMessageLog());
		
		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());

		chatBox = this.getChatBox();
		chatArea.add(chatBox);

		JPanel bTs = new JPanel();
		bTs.setLayout(new BoxLayout(bTs, BoxLayout.Y_AXIS));
		bTs.add(send);
		bTs.add(selectBid);

		chatArea.add(bTs, BorderLayout.EAST);
		
		this.add(log, BorderLayout.CENTER);
		this.add(chatArea, BorderLayout.SOUTH);
	}

	public String getChatContent() {
		return this.chatBox.getText();
	}

	public void setSendMessageListener(MouseClickListener listener) {
		this.send.addMouseListener(listener);
	}

	public void setSelectBidListener(MouseClickListener listener) {
		this.selectBid.addMouseListener(listener);
	}


	@Override
	public void update(EventType e) {
	}
}
