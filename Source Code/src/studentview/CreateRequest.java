package studentview;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import mainview.MouseClickListener;
import mainview.RemovablePanel;
import model.Bid;
import model.EventType;
import model.Subject;

/**
 * View where the Student creates a match request
 */
public class CreateRequest extends RemovablePanel {
	public JRadioButton openBid = new JRadioButton("Open");
    public JRadioButton closeBid = new JRadioButton("Close");
    public ButtonGroup bidType = new ButtonGroup();
    {
    openBid.setActionCommand(Bid.BidType.open.toString());
    closeBid.setActionCommand(Bid.BidType.close.toString());
    bidType.add(openBid);
    bidType.add(closeBid);
    openBid.setSelected(true);
    }
    
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
    public JComboBox<String> subject = new JComboBox<String>(Subject.getAllSubjectsNames());
	public JComboBox<String> competency = new JComboBox<String>(new String[] {"0","1","2","3","4","5","6","7","8","9","10"});
	public JTextField hourPerLesson = new JTextField();
	public JTextField sessionsPerWeek = new JTextField();
	public JTextField rate = new JTextField();
	
	private JLabel typeLb = new JLabel("Type");
	private JLabel subjectLb = new JLabel("Subject");
	private JLabel competencyLb = new JLabel("Tutor's Level of competency");
	private JLabel hourPerLessonLb = new JLabel("Preferred Hour/Lesson");
	private JLabel sessionsPerWeekLb = new JLabel("Preferred Sesions/Week");
	private JLabel rateLb = new JLabel("Preferred Rate");

	private JButton createRequest = new JButton("Create Request"); 
	
	public CreateRequest() {
		super(new BorderLayout());
		placeComponents();
	}
	
	private void placeComponents() {
		JPanel midPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(midPanel);
		midPanel.setLayout(groupLayout);
		midPanel.setBackground(Color.green);
		
		this.add(midPanel);
		
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
				.addComponent(typeLb)
				.addComponent(subjectLb)
				.addComponent(competencyLb)
				.addComponent(hourPerLessonLb)
				.addComponent(sessionsPerWeekLb)
				.addComponent(rateLb))
				.addGroup(groupLayout.createParallelGroup()
						.addGroup(groupLayout.createSequentialGroup().addComponent(openBid)
								.addComponent(closeBid))
						.addComponent(subject)
						.addComponent(competency)
						.addComponent(hourPerLesson)
						.addComponent(sessionsPerWeek)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(rate)
								.addComponent(perHour)
								.addComponent(perSession))
						)
				);
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(typeLb)
						.addComponent(openBid)
						.addComponent(closeBid))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(subjectLb)
						.addComponent(subject))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(competencyLb)
						.addComponent(competency))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(hourPerLessonLb)
						.addComponent(hourPerLesson))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(sessionsPerWeekLb)
						.addComponent(sessionsPerWeek))
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(rateLb)
						.addComponent(rate)
						.addComponent(perHour)
						.addComponent(perSession))
				);
	
		JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.red);
        this.add(bottomPanel, BorderLayout.SOUTH);
        
		bottomPanel.add(createRequest);
	}

	public void setCreateRequestListener(MouseClickListener listener) {
		this.createRequest.addMouseListener(listener);
	}

	@Override
	public void update(EventType e) {
	}

}
