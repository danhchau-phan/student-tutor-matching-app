package mainview;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class RemovablePanel extends JPanel {
	
	public RemovablePanel() {
		super();
	}
	
	public RemovablePanel(LayoutManager layout) {
		super(layout);
	}
	public void isRemoved() {};
}
