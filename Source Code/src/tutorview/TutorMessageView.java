package tutorview;

import java.awt.BorderLayout;

import javax.swing.*;
import model.EventType;
import mainview.MessageView;
import mainview.MouseClickListener;
import mainview.Observer;
import mainview.RemovablePanel;
import model.Bid;
import model.Message;
import model.User;

/**
 * This is the View where Tutor messages Student in close bidding
 */
public class TutorMessageView extends RemovablePanel implements MessageView {

	private Message message;
	private Bid bid;
	private JButton send = new JButton("Send");
	private JTextField chatBox;

	public TutorMessageView(Message message, Bid bid) {
		this.bid = bid;
		this.message = message;
		placeComponents();
	}

	protected void placeComponents() {
		Message mS = this.message;

		JTextArea log = (mS == null? new JTextArea() : this.getLogArea(mS.getMessageLog()));

		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());

		chatBox = this.getChatBox();
		chatArea.add(chatBox);

		JPanel bTs = new JPanel();
		bTs.setLayout(new BoxLayout(bTs, BoxLayout.Y_AXIS));
		bTs.add(send);

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


	@Override
	public void update(EventType e) {

	}
}
