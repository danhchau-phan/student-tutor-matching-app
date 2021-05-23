package model;

import mainview.Observer;

import java.util.*;

/** Monitor store all the Subscribed Bid Request by tutor*/
public class Monitor implements Observer {
    private List<BidResponse> newResponses = new ArrayList<>();
    private Map<Bid, List<BidResponse>> subscribedBidsMap = new HashMap<>();
    private List<Bid> bidAllRequests;
    private List<Bid> activeMonitorBids;
    private Bid bidObserved;
    private boolean isChanged;

    public Monitor() {
    }

//    public Monitor(List<Bid> subscribedBids, Bid bidsToObserved) {
////        this.subscribedBidsMap = subscribedBids;
//        this.bidObserved = bidsToObserved;
//        subscribedBidsMap = new HashMap<>();
//    }

    public void addSubscribe(Bid bid) {
        System.out.println("Adding Bid Subscription..." + bid.getId());
        newResponses.clear();
        if (bid.getResponse() == null) {
            newResponses = new ArrayList<>();
        } else {
            newResponses = bid.getResponse();
        }
        this.subscribedBidsMap.put(bid, newResponses);
        setChanged(true);
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
        List<BidResponse> previousBidResponses = subscribedBidsMap.get(bidModel);
        // Check Any New Responses
        if (previousBidResponses.size() != bidModel.getResponse().size()) {
            System.out.println("Response added!!!");
            setChanged(true);
            subscribedBidsMap.replace(bidModel, bidModel.getResponse());
        }
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }


    public Set<Bid> getSubscribedBids() {
        return subscribedBidsMap.keySet();
    }


    @Override
    public void update(EventType e) {
        System.out.println("Bid has Changed, notifying Moniter .....");
        bidAllRequests= Bid.getAll();   // get all updated bids
        activeMonitorBids.clear();

        for (Bid bidModel: bidAllRequests) {
            if (subscribedBidsMap.containsKey(bidModel)) {
                activeMonitorBids.add(bidModel);
                checkResponses(bidModel);
            }
        }

        subscribedBidsMap.keySet().retainAll(activeMonitorBids);    // Only retain the bid request that is active
        for (Bid bid: subscribedBidsMap.keySet()) {
            System.out.println(bid.getId() + " stored in Monitor");
        }
    }
}
