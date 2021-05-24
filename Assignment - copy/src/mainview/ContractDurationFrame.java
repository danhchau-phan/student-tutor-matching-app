package mainview;

import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ContractDurationFrame extends JPanel{
    private JRadioButton three = new JRadioButton("3 months");
    private JRadioButton six = new JRadioButton("6 months");
    private JRadioButton twelve = new JRadioButton("12 months");
    private JRadioButton twtyfour = new JRadioButton("24 months");
    private JRadioButton longer = new JRadioButton("Longer");
    private JTextField longerDuration = new JTextField();
    private ButtonGroup options = new ButtonGroup();
    private int selectedDuration;
    {
    three.setActionCommand("3");
    six.setActionCommand("6");
    twelve.setActionCommand("12");
    twtyfour.setActionCommand("24");
    longer.setActionCommand("longer");
    }
    public ContractDurationFrame() {
        super();
        reload();
        this.add(three);
        this.add(six);
        this.add(twelve);
        this.add(twtyfour);
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
        longerDuration.setEditable(false);
        six.setSelected(true);
    }

    public int getSelectedDuration() {
        reload();
        return selectedDuration;
    }
}
