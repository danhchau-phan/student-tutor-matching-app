package mainview;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class RemovablePanel extends JPanel implements Observer {
	
	public RemovablePanel() {
		super();
	}
	
	public RemovablePanel(LayoutManager layout) {
		super(layout);
	}

	public void onRemoved() {};
	
	public void onAttached() {};
}
