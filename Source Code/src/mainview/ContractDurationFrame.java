package mainview;

import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

public class ContractDurationFrame extends JPanel{
    private JRadioButtonMenuItem three = new JRadioButtonMenuItem("3 months");
    private JRadioButtonMenuItem six = new JRadioButtonMenuItem("6 months");
    private JRadioButtonMenuItem twelve = new JRadioButtonMenuItem("12 months");
    private JRadioButtonMenuItem twentyfour = new JRadioButtonMenuItem("24 months");
    private JRadioButtonMenuItem longer = new JRadioButtonMenuItem("Longer");
    private JTextField longerDuration = new JTextField();
    private ButtonGroup options = new ButtonGroup();
    public ButtonGroup duration = new ButtonGroup();
    {
        three.setActionCommand("3");
        six.setActionCommand("6");
        twelve.setActionCommand("12");
        twentyfour.setActionCommand("24");
        duration.add(three);
        duration.add(six);
        duration.add(twelve);
        duration.add(twentyfour);
        six.setSelected(true);
    }

    private int selectedDuration;
    {
    three.setActionCommand("3");
    six.setActionCommand("6");
    twelve.setActionCommand("12");
    twentyfour.setActionCommand("24");
    longer.setActionCommand("longer");
    six.setSelected(true);
    }
    public ContractDurationFrame() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        reload();
        this.add(three);
        this.add(six);
        this.add(twelve);
        this.add(twentyfour);
        this.add(longer);
        this.add(longerDuration);
        this.longer.addMouseListener(new MouseClickListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                longerDuration.setEditable(true);
            }
            
        });
    }

    public void show() {
        
		JOptionPane.showMessageDialog(null, this);
        
	}

    private void reload() {
        longerDuration.setText("");
        longerDuration.setEditable(false);
    }

    public int getSelectedDuration() {
        selectedDuration = Integer.parseInt(duration.getSelection().getActionCommand());
        if (duration.getSelection().getActionCommand().equalsIgnoreCase("longer")) {
            if (!isNumeric(longerDuration.getText())) {
                throw new NumberFormatException();
            }
            selectedDuration = Integer.parseInt(longerDuration.getText());
        }
        reload();
        return selectedDuration;
    }

    private boolean isNumeric(String s) {
        if (s == null) {
            return true;
        }
        try {
            double d = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
}
