package studentview;

import mainview.MouseClickListener;
import mainview.RemovablePanel;
import model.Bid;
import model.Contract;
import model.Subject;

import javax.swing.*;
import java.awt.*;

public class CreateSameTutorContract extends RemovablePanel {
    public JRadioButton perSession = new JRadioButton("per session");
    private Contract contract;
    public JRadioButton perHour = new JRadioButton("per hour");
    public ButtonGroup rateType = new ButtonGroup();
    {
        perSession.setActionCommand("per session");
        perHour.setActionCommand("per hour");
        rateType.add(perSession);
        rateType.add(perHour);
        perSession.setSelected(true);
    }
    public JComboBox<String> competency = new JComboBox<String>(new String[] {"0","1","2","3","4","5","6","7","8","9","10"});
    public JTextField hourPerLesson = new JTextField();
    public JTextField sessionsPerWeek = new JTextField();
    public JTextField rate = new JTextField();

    private JLabel competencyLb = new JLabel("Tutor's Level of competency");
    private JLabel hourPerLessonLb = new JLabel("Preferred Hour/Lesson");
    private JLabel sessionsPerWeekLb = new JLabel("Preferred Sesions/Week");
    private JLabel rateLb = new JLabel("Preferred Rate");

    private JButton reuseContractButton = new JButton("Reuse");

    public CreateSameTutorContract() {
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
                        .addComponent(rateLb)
                        .addComponent(sessionsPerWeekLb)
                .addGroup(groupLayout.createParallelGroup()
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(rate)
                                .addComponent(perSession)
                                .addComponent(perHour))
                        .addComponent(sessionsPerWeek)
                ))
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(rateLb)
                        .addComponent(rate)
                        .addComponent(perSession)
                        .addComponent(perHour))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(sessionsPerWeekLb)
                        .addComponent(sessionsPerWeek))
        );
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.red);
        bottomPanel.add(reuseContractButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void setListener(MouseClickListener listener) {
		this.reuseContractButton.addMouseListener(listener);
	}
    
    public void setCurrentContract(Contract c) {
    	this.contract = c;
    }
}
