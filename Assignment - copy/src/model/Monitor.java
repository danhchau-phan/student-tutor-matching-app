package model;

import mainview.Observer;

import java.util.*;

/** Monitor store all the Subscribed Bid Request by tutor*/
public class Monitor implements Observer {
    private Map<Bid, List<BidResponse>> subscribedBidsMap;
    private List<Bid> bidAllRequests;
    private List<Bid> activeMonitorBids;
    private Bid bidObserved;
    private boolean isChanged;

    public Monitor() {
    }

    public Monitor(List<Bid> subscribedBids, Bid bidsToObserved) {
//        this.subscribedBidsMap = subscribedBids;
        this.bidObserved = bidsToObserved;
        subscribedBidsMap = new HashMap<>();
    }

    public void addSubscribe(Bid bid) {
        this.subscribedBidsMap.put(bid, bid.getResponse());
    }

    public void unSubscribe(Bid bid) {
        this.subscribedBidsMap.remove(bid);
    }

    public boolean hasChanged() {
        return this.isChanged;
    }

    public void confirmChanges() {
        this.isChanged = false;
    }

    /** Check any changes happen on the responses for relevant subscribed bid request*/
    public void checkResponses(Bid bidModel) {
        activeMonitorBids.add(bidModel);

        List<BidResponse> previousBidResponses = subscribedBidsMap.get(bidModel);
        // Check Any New Responses
        if (previousBidResponses.size() != bidModel.getResponse().size()) {
            setChanged(true);
            subscribedBidsMap.replace(bidModel, bidModel.getResponse());
        }
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public Map<Bid, List<BidResponse>> getSubscribedBidsMap() {
        return subscribedBidsMap;
    }

    public Set<Bid> getSubscribedBids() {
        return subscribedBidsMap.keySet();
    }


    @Override
    public void update(EventType e) {
        bidAllRequests= Bid.getAll();   // get all updated bids
        activeMonitorBids.clear();

        for (Bid bidModel: bidAllRequests) {
            if (subscribedBidsMap.containsKey(bidModel)) {
                checkResponses(bidModel);
            }
        }

        subscribedBidsMap.keySet().retainAll(activeMonitorBids);    // Only retain the bid request that is active



//        subscribedBidsMap.keySet().removeAll(expiredBidList);
    }
}
