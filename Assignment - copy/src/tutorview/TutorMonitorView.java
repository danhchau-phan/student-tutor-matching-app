package tutorview;

import mainview.Observer;
import model.Bid;
import model.EventType;
import model.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;


/** Couldnt implement Observer here because must update every N seconds*/
public class TutorMonitorView extends JPanel {
//    private Timer timer;
//    private static final int threadSleep = 1000000;
//    private static final int monitorIntervalCheck = 5000;
//    private Monitor monitor;

    public TutorMonitorView() {
        super(new BorderLayout());
        placeComponents();
    }


//    public TutorMonitorView(Monitor monitor) {
//        super(new BorderLayout());
//        this.monitor = monitor;
//    }

    protected void placeComponents(){

    }

    public void add() {

    }

    public void monitorRun() {

    }

    public void setLatestMonitorView(Set<Bid> bids) {

    }

    public void addMonitorRunnableListener(ActionListener listener) {
        monitorRun();
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
