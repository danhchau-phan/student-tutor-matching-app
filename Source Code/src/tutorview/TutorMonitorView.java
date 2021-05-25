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

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel AllBidContainer = new JPanel(new BorderLayout());


        // Create a Bid Panel for each bid
        for (Bid activeBid: activeBidList) {
            JPanel activeBidPanel = new JPanel();

            // Responses JList
            DefaultListModel<BidResponse> model = new DefaultListModel<>();
            responses = activeBid.getResponse();
            for (BidResponse r : responses)
                model.addElement(r);
            responseList = new JList<>(model);
            responseList.setCellRenderer(new ResponseCellRenderer());


            JPanel bidRequestPanel = new JPanel();
            bidRequestPanel.add(new JLabel("Bid Request " + activeBid.getId(), JLabel.LEFT));
            bidRequestPanel.add(new JLabel(activeBid.getInitiatorName(), JLabel.LEFT));
            JPanel responseListPanel = new JPanel();
            responseListPanel.add(responseList);

            activeBidPanel.add(bidRequestPanel);
            activeBidPanel.add(responseListPanel);

            AllBidContainer.add(activeBidPanel);

        }

        scrollPane.setViewportView(AllBidContainer);

        setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
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
