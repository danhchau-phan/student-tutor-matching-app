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
    
    public ButtonGroup duration = new ButtonGroup();
    {
        three.setActionCommand("3");
        six.setActionCommand("6");
        twelve.setActionCommand("12");
        twentyfour.setActionCommand("24");
        longer.setActionCommand("longer");
        duration.add(three);
        duration.add(six);
        duration.add(twelve);
        duration.add(twentyfour);
        duration.add(longer);
    }

    private int selectedDuration;
    {
    three.setActionCommand("3");
    six.setActionCommand("6");
    twelve.setActionCommand("12");
    twentyfour.setActionCommand("24");
    longer.setActionCommand("longer");
    }
    public ContractDurationFrame() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
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
    	reload();
		int result = JOptionPane.showConfirmDialog(null,
                this , "Select Contract Duration",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				this.getSelectedDuration();
			} catch (NumberFormatException e) {
				this.show();
			}
		} 
	}


    private void reload() {
        longerDuration.setText("");
        longerDuration.setEditable(false);
    }

    private void getSelectedDuration() throws NumberFormatException {
        selectedDuration = Integer.parseInt(duration.getSelection().getActionCommand());
        if (duration.getSelection().getActionCommand().equalsIgnoreCase("longer")) {
            if (!isNumeric(longerDuration.getText())) {
                throw new NumberFormatException();
            }
            selectedDuration = Integer.parseInt(longerDuration.getText());
        }
        return;
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
    
    /**
     * To be called in Controller to retrieve user's selected duration
     */
    public int getDuration() {
    	return this.selectedDuration;
    }
    
}
