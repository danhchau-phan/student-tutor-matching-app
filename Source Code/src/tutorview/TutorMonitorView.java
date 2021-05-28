package tutorview;

import mainview.Observer;
import mainview.RemovablePanel;
import model.Bid;
import model.BidResponse;
import model.EventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TutorMonitorView extends RemovablePanel {
	private static final int monitorCheckInterval = 15000;
	private Timer timer;
    private List<Bid> bids = new ArrayList<>();
    private ActionListener listener;
    private JList<BidResponse> responseList;
    private List<BidResponse> responses  = new ArrayList<>();
    
    public TutorMonitorView(List<Bid> bids, ActionListener listener) {
        super(new BorderLayout());
        this.listener = listener;
        this.bids = bids;
    }
    
    public void placeComponents(){
    	this.removeAll();
    	this.revalidate();
    	responses.clear();
        for (Bid b : this.bids) {
        	responses.addAll(b.getResponse());
        }
        Collections.reverse(responses);
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

	@Override
	public void onRemoved() {
		this.timer.stop();
		this.timer.removeActionListener(listener);
		this.timer = null;
		System.out.println("Timer Stopped");
	}
	
	public void onAttached() {
		this.removeAll();
        this.timer = new Timer(this.monitorCheckInterval, listener);
        this.timer.setInitialDelay(0);
        this.timer.start();
		placeComponents();
	}
	
}
