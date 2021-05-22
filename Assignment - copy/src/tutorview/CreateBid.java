package tutorview;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

import mainview.MouseClickListener;

/**
 * This is the View where the Tutor creates the bid in open bidding
 */
public class CreateBidView extends JPanel {
	public JRadioButton perSession = new JRadioButton("per session");
	public JRadioButton perHour = new JRadioButton("per hour");
	public ButtonGroup rateType = new ButtonGroup();
	{
		perSession.setActionCommand("per session");
		perHour.setActionCommand("per hour");
		rateType.add(perSession);
		rateType.add(perHour);
		perSession.setSelected(true);
	}
	public JRadioButton yes = new JRadioButton("Yes");
	public JRadioButton no = new JRadioButton("No");
	public ButtonGroup freeLesson = new ButtonGroup();
	{
		yes.setActionCommand("yes");
		no.setActionCommand("no");
		freeLesson.add(yes);
		freeLesson.add(no);
		yes.setSelected(true);
	}

	public JTextField rate = new JTextField();
	public JTextField duration = new JTextField();
	public JTextField timeDate = new JTextField();
	public JTextField sessionsPerWeek = new JTextField();
	public JTextField addInfo = new JTextField();

	private JLabel rateLb = new JLabel("Rate");
	private JLabel durationLb = new JLabel("Duration");
	private JLabel timeDateLb = new JLabel("Time and Date"); // Wednesday 2PM
	private JLabel sessionsPerWeekLb = new JLabel("Sessions/Week");
	private JLabel freeLessonLb = new JLabel("freeLesson");
	private JLabel addInfoLb = new JLabel("Additional information");

	private JButton createBid = new JButton("Create Bid");

	public CreateBidView() {
		super(new BorderLayout());
		placeComponents();
	}
	
	protected void placeComponents() {
		JPanel midPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(midPanel);
		midPanel.setLayout(groupLayout);
		midPanel.setBackground(Color.green);

		this.add(midPanel);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(rateLb)
						.addComponent(durationLb)
						.addComponent(timeDateLb)
						.addComponent(sessionsPerWeekLb)
						.addComponent(freeLessonLb)
						.addComponent(addInfoLb))
				.addGroup(groupLayout.createParallelGroup()
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(rate)
								.addComponent(perSession)
								.addComponent(perHour))
						.addComponent(duration)
						.addComponent(timeDate)
						.addComponent(sessionsPerWeek)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(yes)
								.addComponent(no))
//						.addComponent(addInfo)
				));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(rateLb)
						.addComponent(rate)
						.addComponent(perSession)
						.addComponent(perHour))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(durationLb)
						.addComponent(duration))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(timeDateLb)
						.addComponent(timeDate))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(sessionsPerWeekLb)
						.addComponent(sessionsPerWeek))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(freeLessonLb)
						.addComponent(yes)
						.addComponent(no))
//				.addGroup(groupLayout.createParallelGroup()
//						.addComponent(addInfoLb)
//						.addComponent(addInfo))
		);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.red);
		bottomPanel.add(createBid);
//
//		createBid.addMouseListener(new MouseClickListener() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				String r = rate.getText();
//				String rT = rateType.getSelection().getActionCommand();
//				String d = duration.getText();
//				String tD = timeDate.getText();
//				String s = sessionsPerWeek.getText();
//				String a = addInfo.getText();
//				boolean f = freeLesson.getSelection().getActionCommand() == "yes"? true : false;
//				try {
//					BidResponse response = new BidResponse(
//							user.getId(),
//							user.getFullName(),
//							r,
//							rT,
//							d,
//							tD,
//							s,
//							a,
//							f);
//					bid.addResponse(response);
//					Utils.SUCCESS_BID_CREATION.show();
//				} catch (NumberFormatException nfe) {
//					Utils.INVALID_FIELDS.show();
//				} catch (NullPointerException npe) {
//					Utils.PLEASE_FILL_IN.show();
//				}
//			}
//		});
//
//		this.display.setVisible();

	}

	public void setCreateBidListener(MouseClickListener listener) {
		this.createBid.addMouseListener(listener);
	}
}
