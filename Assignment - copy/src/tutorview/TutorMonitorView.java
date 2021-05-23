package tutorview;

import model.Monitor;

import javax.swing.*;
import java.awt.*;

public class TutorMonitorView extends JPanel {
    private Monitor monitor;

    public TutorMonitorView(boolean b, Monitor monitor) {
        super(new BorderLayout());
        this.monitor = monitor;
        placeComponents();
    }

    protected void placeComponents(){

    }

    public void setLatestMonitorView() {

    }
}
