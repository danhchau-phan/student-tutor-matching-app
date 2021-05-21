package mainview;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

/**
 * Factory class for all views
 */
public abstract class View {
	private JFrame frame;
	public static final int FRAME_WIDTH = 500;
	public static final int FRAME_HEIGHT = 500;
	protected JButton homeButton = new JButton("Home");
	
	public View() {
//		this.display = display;
		frame = new JFrame("TimTam App");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

	public void addSwitchPanelListener(JPanel panel, Component comp, View view) {

		comp.addMouseListener(mouseListener);
	}

}
