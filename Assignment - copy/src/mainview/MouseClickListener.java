package mainview;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * An interface for only mouse click listener
 */
public interface MouseClickListener extends MouseListener {
	

	@Override
	public default void mousePressed(MouseEvent e) {
	}

	@Override
	public default void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public default void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public default void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
