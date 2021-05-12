package mainview;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * An interface that populates top navigation panel
 */
public interface NavigationPane {
	
	public default void showNavigationPane(Display display, JPanel parent, JButton[] buttons) {
	
		JPanel panel = new JPanel();
		
		parent.add(panel, BorderLayout.NORTH);
		
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		for (JButton b : buttons) {
			panel.add(b);
		}
		
	}
	
	
}
