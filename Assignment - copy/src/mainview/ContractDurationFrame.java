package mainview;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ContractDurationFrame extends JPanel{
    JRadioButton three = new JRadioButton("3 months");
    JRadioButton six = new JRadioButton("6 months");
    JRadioButton twelve = new JRadioButton("12 months");
    JRadioButton twtyfour = new JRadioButton("24 months");
    JRadioButton longer = new JRadioButton("Longer");
    JTextField longerDuration = new JTextField();
    ButtonGroup options = new ButtonGroup();
    {
    three.setActionCommand("3");
    six.setActionCommand("6");
    twelve.setActionCommand("12");
    twtyfour.setActionCommand("24");
    longer.setActionCommand("longer");
    six.setSelected(true);
    }
    JButton OK = new JButton("OK");
    public ContractDurationFrame() {
        super();

    }

    public void show() {
        
		JOptionPane.showInputDialog(null, this, "Select contract duration");
	}

    public int getSelectedDuration() {
        return 6;
    }
}
