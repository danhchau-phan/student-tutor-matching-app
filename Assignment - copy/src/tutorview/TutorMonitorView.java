package tutorview;

import model.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TutorMonitorView extends JPanel {
//    private Timer timer;
//    private static final int threadSleep = 1000000;
//    private static final int monitorIntervalCheck = 5000;
    private Monitor monitor;

    public TutorMonitorView(Monitor monitor) {
        super(new BorderLayout());
        this.monitor = monitor;
        placeComponents();
    }

    protected void placeComponents(){

    }

    public void setLatestMonitorView() {

    }

//    // In controller
//    public static void main(String [] args) throws Exception{
//        /** Listener on checking monitor every N seconds*/
//        ActionListener taskPerformer = new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//
//            }
//        };
//        Timer timer = new Timer(monitorIntervalCheck ,taskPerformer);
//        timer.setRepeats(true);
//        timer.start();
//
//        Thread.sleep(threadSleep);
//    }
}
