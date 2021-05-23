package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mainview.Observer;

public abstract class Observable {
    Map<EventType, List<Observer>> observers = new HashMap<EventType, List<Observer>>();
    
    public void inform(EventType e) {
        if (!this.observers.containsKey(e))
            return;
        for (Observer o : this.observers.get(e)) {
            o.update(e);
        }
    };

    public void subscribe(EventType e, Observer o) {
        if (!this.observers.containsKey(e))
            this.observers.put(e, new ArrayList<Observer>());
        this.observers.get(e).add(o);
            
    }
    public void unsubscribe(EventType e, Observer o) {
        this.observers.get(e).add(o);
    }

    public void emptySubscription(EventType e) {
        if (!this.observers.containsKey(e))
            return;
        this.observers.get(e).clear();
    }
}
