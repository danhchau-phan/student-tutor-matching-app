package mainview;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Display {
	private JFrame frame;
	public static final int FRAME_WIDTH = 500;
	public static final int FRAME_HEIGHT = 500;
	/** The display shared by the whole program, contains a JFrame that multiple
	 * JComponents could be attached to and removed from
	 * 
	 */
	public Display() {
		frame = new JFrame("TimTam App");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void createPanel(JComponent panel) {
        frame.add(panel);

	}
	
	public void setVisible() {
		frame.setVisible(true);
	}
	
	public void removePanel(JComponent panel) {
		frame.remove(panel);
	}
}
