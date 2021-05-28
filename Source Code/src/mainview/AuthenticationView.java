package mainview;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Dimension;

public class AuthenticationView extends View {
	
	public JPanel panel = createPanel();
		
	JLabel userLabel = new JLabel("User");
	JTextField userText = new JTextField(MAX_INPUT_LENGTH);
	JLabel passwordLabel = new JLabel("Password");
	JPasswordField passwordText = new JPasswordField(MAX_INPUT_LENGTH);
	public JButton loginButton = new JButton("Log in");

	private static final Dimension PREFERRED_SIZE = new Dimension(60,25);
	private static final int MAX_INPUT_LENGTH = 20;
	
	public AuthenticationView(Display display) {
		/** 
		 * View for user's login
		 * @param: display	reference to the common display shared by the whole program
		 * 
		 */
		super(display);
	}
    
	protected void placeComponents() {
		
		userText.setPreferredSize(PREFERRED_SIZE);
        passwordText.setPreferredSize(PREFERRED_SIZE);
        
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        
        
        this.display.setVisible();
    }

	public String getUserName() {
		return this.userText.getText();
	}

	public String getPassword() {
		return String.valueOf(passwordText.getPassword());
	}

}
