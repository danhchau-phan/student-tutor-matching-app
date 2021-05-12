package studentview;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mainview.Display;
import mainview.MessageView;
import mainview.MouseClickListener;
import mainview.Utils;
import model.Message;
import model.User;
import model.Bid;
import model.Contract;
import model.ContractAddInfo;

/**
 * The View where Student chats with the Tutor in close bidding
 */
class StudentMessageView extends StudentView implements MessageView{

	private Message message;
	private Bid bid;
	public StudentMessageView(Display display, User user, Message message, Bid bid) {
		super(display, user);
		this.bid = bid;
		this.message = message;
	}


	protected void placeComponents() {
		super.placeComponents();
		
		Message mS = this.message;
	
		JTextArea log = this.getLogArea(mS.getMessageLog());
		
		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());

		JTextField chatBox = this.getChatBox();
		chatArea.add(chatBox);

		JButton send = new JButton("Send");
		JButton selectBid = new JButton("Select bid");
		JPanel bTs = new JPanel();
		bTs.setLayout(new BoxLayout(bTs, BoxLayout.Y_AXIS));
		bTs.add(send);
		bTs.add(selectBid);

		chatArea.add(bTs, BorderLayout.EAST);

		send.addMouseListener(new MouseClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String content = chatBox.getText();
				mS.addNewMessage(content, user.getUsername());
				chatBox.setText("");
				display.removePanel(main);
				(new StudentMessageView(display, user, message, bid)).display();
			}});
		
		selectBid.addMouseListener(new MouseClickListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Contract.postContract(user.getId(), 
						message.getPosterId(), 
						bid.getSubject().getId(),
						new ContractAddInfo(true, false));
				Bid.closeDownBid(bid.getId());
				Utils.SUCCESS_CONTRACT_CREATION.show();
				
			}});
		
		main.add(log, BorderLayout.CENTER);
		main.add(chatArea, BorderLayout.SOUTH);
		this.display.setVisible();
	}

}
