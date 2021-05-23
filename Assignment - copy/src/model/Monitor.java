package model;

import mainview.Observer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/** Monitor store all the Subscribed Bid Request by tutor*/
public class Monitor implements Observer {
    private List<Bid> subscribedBids;
    private List<Bid > allBids;
    private boolean isChanged;

    public Monitor(List<Bid> subscribedBids, List<Bid> bidsToObserved) {
        this.subscribedBids = subscribedBids;
        this.allBids = bidsToObserved;
    }

    public void addSubscribe(Bid bid) {
        this.subscribedBids.add(bid);
    }

    public void unSubscribe(Bid bid) {
        this.subscribedBids.remove(bid);
    }

    public boolean hasChanged() {
        return this.isChanged;
    }

    public void confirmChanges() {
        this.isChanged = false;
    }




    public static void main(String [] args) throws Exception{
        /** Listener on checking monitor every N seconds*/
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

            }
        };
        Timer timer = new Timer(5000 ,taskPerformer);
        timer.setRepeats(true);
        timer.start();

        Thread.sleep(1000000);
    }


    @Override
    public void update() {
        this.allBids =
    }
}
