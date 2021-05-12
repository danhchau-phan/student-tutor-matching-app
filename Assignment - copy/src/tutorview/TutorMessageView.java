package tutorview;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mainview.Display;
import mainview.MessageView;
import mainview.MouseClickListener;
import model.Bid;
import model.Message;
import model.MessageAddInfo;
import model.User;

/**
 * This is the View where Tutor messages Student in close bidding
 */
class TutorMessageView extends TutorView implements MessageView{
	private Bid bid;
	public TutorMessageView(Display display, User user, Bid bid) {
		super(display, user);
		this.bid = bid;
	}
	
	protected void placeComponents() {
		super.placeComponents();
		
		Message mS = Message.getMessagebyId(user.getId(), this.bid);
		
		JTextArea log = (mS == null? new JTextArea() : this.getLogArea(mS.getMessageLog()));
		main.add(log, BorderLayout.CENTER);
		
		JPanel chatArea = new JPanel(new BorderLayout());
		JButton send = new JButton("Send");
		
		JTextField chatBox = this.getChatBox();
		chatArea.add(chatBox);
		chatArea.add(send, BorderLayout.EAST);
		
		send.addMouseListener(new MouseClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String content = chatBox.getText();
				if (mS != null)
					mS.addNewMessage(content, user.getUsername());
				else {
					Message.postMessage(bid.getId(), user.getId(), content, new MessageAddInfo(content, user.getUsername()));
					
				}
				chatBox.setText("");
				display.removePanel(main);
				(new TutorMessageView(display, user, bid)).display();
			}});
		
		main.add(chatArea, BorderLayout.SOUTH);
		this.display.setVisible();
	}
}
