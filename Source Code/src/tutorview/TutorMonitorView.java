package tutorview;

import mainview.Observer;
import mainview.RemovablePanel;
import model.Bid;
import model.BidResponse;
import model.EventType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Couldnt implement Observer here because must update every N seconds*/
public class TutorMonitorView extends RemovablePanel implements Observer {
	private Timer timer;
    private List<Bid> bids = new ArrayList<>();
    private JList<BidResponse> responseList;
    private List<BidResponse> responses  = new ArrayList<>();


    public TutorMonitorView(List<Bid> bids, Timer timer) {
        super(new BorderLayout());
        
        this.bids = bids;
        placeComponents();
    }
    
    protected void placeComponents(){
        this.removeAll();
        this.timer.start();
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

	@Override
	public void isRemoved() {
		this.timer.stop();
	}
}
