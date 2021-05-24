package mainview;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ContractDurationFrame extends JPanel{
    public ContractDurationFrame() {
        super();

    }

    public void show() {
		JOptionPane.showInputDialog(null, this, "Select contract duration");
	}

    public int getSelectedDuration() {
        return 6;
    }
    
    public void setListener(MouseClickListener listener) {

    }
}
