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
    private JRadioButtonMenuItem twtyfour = new JRadioButtonMenuItem("24 months");
    private JRadioButtonMenuItem longer = new JRadioButtonMenuItem("Longer");
    private JTextField longerDuration = new JTextField();
    private ButtonGroup options = new ButtonGroup();
    private int selectedDuration;
    {
    three.setActionCommand("3");
    six.setActionCommand("6");
    twelve.setActionCommand("12");
    twtyfour.setActionCommand("24");
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
        longerDuration.setText("");
        longerDuration.setEditable(false);
    }

    public int getSelectedDuration() {
        reload();
        return selectedDuration;
    }
}
