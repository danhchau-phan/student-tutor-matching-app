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
public abstract class  View {
	protected Display display;
	public JButton homeButton = new JButton("Home");
	public RemovablePanel activePanel;
	
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
	public void setSwitchPanelListener(RemovablePanel panel, Component comp, View newView) {
		MouseListener mouseListener = new MouseClickListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				display.removePanel(panel);
				panel.onRemoved();
				newView.display();
			}
		};
		comp.addMouseListener(mouseListener);
	}
	/**
	 * @params:
	 * 	main: the panel to be mounted by new panel
	 * 	comp: the component where listener is attached to
	 * 	newPanel: the panel to be displayed
	 */
	public void setSwitchPanelListener(JPanel main, Component comp, RemovablePanel newPanel) {
		MouseListener mouseListener = new MouseClickListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (activePanel != null) {
					main.remove(activePanel);
					activePanel.onRemoved();
				}
				main.add(newPanel);
				newPanel.onAttached();
				activePanel = newPanel;
				display.createPanel(main);
				display.setVisible();
			}
		};
		comp.addMouseListener(mouseListener);
	}

	public void addMouseListener(JComponent component, MouseClickListener listener) {
		component.addMouseListener(listener);
	}
}
