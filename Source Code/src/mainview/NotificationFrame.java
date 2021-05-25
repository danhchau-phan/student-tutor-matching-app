package mainview;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * A JFrame for fast displaying notification
 */
public class NotificationFrame extends JFrame {
	private String message;
	public NotificationFrame(String message) {
		this.message = message;
	}
	
	public void show() {
		JOptionPane.showMessageDialog(this, this.message);
	}
}
