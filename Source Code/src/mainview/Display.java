package mainview;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.*;

public class Display {
	private JFrame frame;
	public static final int FRAME_WIDTH = 700;
	public static final int FRAME_HEIGHT = 500;
	/** The display shared by the whole program, contains a JFrame that multiple
	 * JComponents could be attached to and removed from
	 * 
	 */
	public Display() {
		frame = new JFrame("Assignment 3");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void createPanel(JComponent panel) {
		frame.setContentPane(panel);
	}
	
	public void setVisible() {
		frame.setVisible(true);
	}
	
	public void removePanel(JComponent panel) {
		frame.remove(panel);
		frame.revalidate();
		frame.repaint();
	}

	public void closeWindow() {
		frame.setVisible(false);
	}
}
