package mainview;

import java.awt.Color;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 * The interface provides ready-to-use JComponents for Message Views
 */
public interface MessageView {
    
    public default JTextArea getLogArea(List<String> messageLog) {
        JTextArea log = new JTextArea();
        log.setEditable(false);
        log.setForeground(Color.BLUE);
        for (String m : messageLog) {
            int colon = m.indexOf(":");
            log.append(m.substring(1, colon+1).toUpperCase() 
                + m.substring(colon+1,m.length()-1) 
                + '\n');

		}
        return log;
    }

    public default JTextField getChatBox() {
        JTextField chatBox = new JTextField();
		chatBox.setBounds(0, Display.FRAME_HEIGHT-100, Display.FRAME_WIDTH * 2 / 3, 100);
        chatBox.setMinimumSize(chatBox.getPreferredSize());
        return chatBox;
    }
}
