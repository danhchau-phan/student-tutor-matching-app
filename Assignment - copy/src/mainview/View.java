package mainview;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Factory class for all views
 */
public abstract class View {
	protected Display display;
	protected JButton homeButton = new JButton("Home");
	
	public View(Display display) {
		this.display = display;
	}
	
	public void display() {
		this.placeComponents();
	}
	
	protected JPanel createPanel(LayoutManager layout) {
		JPanel panel = new JPanel();
		this.display.createPanel(panel);
		panel.setLayout(layout);
		return panel;
	}
	
	protected JPanel createPanel() {
		JPanel panel = new JPanel();
		this.display.createPanel(panel);
		return panel;
	}
	
	protected abstract void placeComponents();
	
	protected void addSwitchPanelListener(JPanel panel, Component comp, View view) {
		MouseListener mouseListener = new MouseClickListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				display.removePanel(panel);
				view.display();
			}
		};
		comp.addMouseListener(mouseListener);
	}

	protected void addMouseListener(JComponent component, MouseClickListener listener) {
		component.addMouseListener(listener);
	}
}
