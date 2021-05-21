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
	public JPanel activePanel;
	
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
	
	/**
	 * @params:
	 * 	panel: the panel where to be removed
	 * 	comp: the component where listener is attached to
	 * 	view: the view to be displayed
	 */
	protected void addSwitchPanelListener(JPanel panel, Component comp, View newView) {
		MouseListener mouseListener = new MouseClickListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				display.removePanel(panel);
				// display.removeAll();
				newView.display();
			}
		};
		comp.addMouseListener(mouseListener);
	}

	protected void addSwitchPanelListener(JPanel main, Component comp, JPanel newPanel) {
		MouseListener mouseListener = new MouseClickListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (activePanel != null) {
					main.remove(activePanel);
				}
				main.add(newPanel);
				activePanel = newPanel;
				display.setVisible();
			}
		};
		comp.addMouseListener(mouseListener);
	}

	protected void addMouseListener(JComponent component, MouseClickListener listener) {
		component.addMouseListener(listener);
	}
}
