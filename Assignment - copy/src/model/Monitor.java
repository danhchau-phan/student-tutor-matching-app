package model;

import mainview.Observer;
import java.util.List;

/** Monitor store all the Subscribed Bid Request by tutor*/
public class Monitor implements Observer {
    private List<Bid> subscribedBids;
    private List<Bid> bidAllRequests;
    private Bid bidObserved;
    private boolean isChanged;

    public Monitor(List<Bid> subscribedBids, Bid bidsToObserved) {
        this.subscribedBids = subscribedBids;
        this.bidObserved = bidsToObserved;
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

    /** Check any changes happen on the responses for relevent subscribed bid request*/
    public void checkResponses(Bid bid) {

    }


    @Override
    public void update(EventType e) {
        bidAllRequests= bidObserved.getAll();   // get all updated bids

        for (Bid bid: subscribedBids) {
            if (bidAllRequests.contains(bid)) {
                checkResponses(bid);
            } else {
                // Expired Bid should be removed from monitor
                subscribedBids.remove(bid);
            }
        }


        for (Bid bid: bidAllRequests) {
            // Make sure these bids exist in subscribed bid to ensure it remain active
            if (subscribedBids.contains(bid)) {
                List<BidResponse> latestResponses = bid.getResponse();
            }
            else {

            }

        }

    }
}
