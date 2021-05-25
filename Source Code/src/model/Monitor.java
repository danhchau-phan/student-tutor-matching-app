package model;

import com.fasterxml.jackson.databind.JsonNode;
import mainview.Observer;

import java.util.*;

/** Monitor store all the Subscribed Bid Request by tutor*/
public class Monitor extends Observable implements Observer {
    private List<Bid> bidAllRequests;

    private List<BidResponse> newResponses = new ArrayList<>();
    private Map<Bid, List<BidResponse>> subscribedBidsMap = new HashMap<>();
    private List<Bid> activeMonitorBids;
    private boolean isChanged;

    public Monitor(List<Bid> bidAllRequests) {
        if (bidAllRequests == null)
            bidAllRequests = new ArrayList<>();
        this.bidAllRequests = bidAllRequests;
        for (Bid bid: bidAllRequests) {
//            if (bid.getResponse() == null)
//                subscribedBidsMap.put(bid, new ArrayList<>());
//            subscribedBidsMap.put(bid, bid.getResponse());
            addRequestBidToSubscribe(bid);
        }
    }

    public Monitor() {
    }

    public Monitor(JsonNode node) {
        assert (node.get("bidSubscribed").isArray());
        this.bidAllRequests = getBidsSubscribed(node.get("bidSubscribed").iterator());
        for (Bid bid: bidAllRequests) {
//            subscribedBidsMap.put(bid, bid.getResponse());
            addRequestBidToSubscribe(bid);
        }
    }

    public List<Bid> getBidsSubscribed(Iterator<JsonNode> iter) {
        List<Bid> list = new ArrayList<Bid>();
        while (iter.hasNext()) {
            list.add(new Bid(iter.next()));
        }
        return list;
    }

    /** New Resquest Bid added*/
    public void addRequestBidToSubscribe(Bid bid) {
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

    /** Remove Request Bid*/
    public void removeRequestBidToSubscribe(Bid bid) {
        this.subscribedBidsMap.remove(bid);
    }

    public boolean hasChanged() {
        return this.isChanged;
    }

    public void confirmChanges() {
        this.isChanged = false;
    }

    public List<Bid> getBidAllRequests() {
        if (!subscribedBidsMap.isEmpty()) {
            bidAllRequests.addAll(subscribedBidsMap.keySet());
        }
        return bidAllRequests;
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

    public String toJson() {
        String jsonString = "{" +
                "\"bidSubscribed\":" + getBidSubscribedJson() + "}";
        return jsonString;
    }

    public String getBidSubscribedJson() {
        String jsonString = "[";
        String comma = "";
        for (Bid bid : bidAllRequests) {
            jsonString = jsonString + comma + bid.toJson();
            comma = ",";
        }
        jsonString = jsonString + "]";
        return jsonString;
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
