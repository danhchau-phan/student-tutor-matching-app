package mainview;

import model.Bid;

public interface BidObserver extends Observer{
    public void update(Bid bid);
}
