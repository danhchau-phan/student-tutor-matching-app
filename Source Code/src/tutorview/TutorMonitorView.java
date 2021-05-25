package tutorview;

import mainview.Observer;
import model.Bid;
import model.BidResponse;
import model.EventType;
import model.Monitor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Couldnt implement Observer here because must update every N seconds*/
public class TutorMonitorView extends JPanel implements Observer {
//    private Timer timer;
//    private static final int threadSleep = 1000000;
//    private static final int monitorIntervalCheck = 5000;
//    private Monitor monitor;
	
	private Timer timer;
	private JButton stopMonitor = new JButton("Stop monitor"); // Button for stopping timer
    private List<Bid> bids = new ArrayList<>();
    private JList<BidResponse> responseList;
    private List<BidResponse> responses;


    public TutorMonitorView(List<Bid> bids, Timer timer) {
        super(new BorderLayout());
        this.bids = bids;
        this.timer = timer;
        placeComponents();
    }
//
//    public TutorMonitorView(Monitor monitor) {
//        super(new BorderLayout());
//        this.monitor = monitor;
//        placeComponents();
//    }

    protected void placeComponents(){
        this.removeAll();

    }

    /** Update the latest Bid Response or expired Bid Request*/
    @Override
    public void update(EventType e) {
        placeComponents();
    }

    private class ResponseCellRenderer extends JPanel implements ListCellRenderer<BidResponse> {

        @Override
        public Component getListCellRendererComponent(JList<? extends BidResponse> list, BidResponse value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            this.removeAll();

            JPanel panel = new JPanel(new BorderLayout());
            JEditorPane eP = new JEditorPane();

            eP.setText(value.toString());
            panel.add(eP);


            this.add(panel);
            return this;
        }
    }
}
