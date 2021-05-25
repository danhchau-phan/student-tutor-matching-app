package mainview;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JComponent;

public class ListPanel extends JPanel {
	/**
		 * A JComponent for fast-populating a BoxLayout with list of JComponents
		 * @param comp list of JComponents
		 */
	public ListPanel(List<JComponent> comp) {
		
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for (JComponent c : comp) {
			this.add(c);
		}
		this.add(Box.createVerticalGlue());
	}
}
