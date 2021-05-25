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
    private List<BidResponse> responses  = new ArrayList<>();


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
        responses.clear();
        for (Bid b : this.bids) {
        	responses.addAll(b.getResponse());
        }
        DefaultListModel<BidResponse> model = new DefaultListModel<BidResponse>();
		for (BidResponse r : responses)
			model.addElement(r);
		responseList = new JList<BidResponse>(model);
		responseList.setCellRenderer(new ResponseCellRenderer());
		JScrollPane scrollp = new JScrollPane(responseList);
		this.add(scrollp);
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
            JEditorPane eP = new JEditorPane();
            eP.setText(value.toString());
            this.add(eP);
            return this;
        }
    }
}
