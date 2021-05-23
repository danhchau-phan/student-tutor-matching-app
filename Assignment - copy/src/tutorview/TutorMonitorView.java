package tutorview;

import mainview.Observer;
import model.Bid;
import model.BidResponse;
import model.EventType;
import model.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/** Couldnt implement Observer here because must update every N seconds*/
public class TutorMonitorView extends JPanel {
//    private Timer timer;
//    private static final int threadSleep = 1000000;
//    private static final int monitorIntervalCheck = 5000;
//    private Monitor monitor;
    private List<Bid> activeBidList;
    private List<JPanel> bidPanels = new ArrayList<>();
    private JList<BidResponse> responseList;
    private List<BidResponse> responses;


    public TutorMonitorView(List<Bid> activeBids) {
        super(new BorderLayout());
        this.activeBidList = activeBids;
        placeComponents();
    }


//    public TutorMonitorView(Monitor monitor) {
//        super(new BorderLayout());
//        this.monitor = monitor;
//    }

    protected void placeComponents(){
        this.removeAll();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel AllBidContainer = new JPanel(new GridLayout(0, 1));


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


            activeBidPanel.add(new JLabel(activeBid.getId(), JLabel.LEFT));
            activeBidPanel.add(responseList);

            AllBidContainer.add(activeBidPanel);

        }

        scrollPane.setViewportView(AllBidContainer);

        setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private class ResponseCellRenderer extends JPanel implements ListCellRenderer<BidResponse> {

        @Override
        public Component getListCellRendererComponent(JList<? extends BidResponse> list, BidResponse value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            this.removeAll();

            JPanel panel = new JPanel(new BorderLayout());
            JEditorPane eP = new JEditorPane();
            JButton bT = new JButton("Select Bid");

            eP.setText(value.toString());
            panel.add(eP);
            panel.add(bT, BorderLayout.EAST);

            this.add(panel);
            return this;
        }
    }

    public void add() {

    }

    public void monitorRun() {

    }

    /** Update the latest Bid Response or expired Bid Request*/
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
