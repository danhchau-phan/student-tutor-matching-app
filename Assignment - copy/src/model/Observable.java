package model;
import java.util.ArrayList;
import java.util.List;

import mainview.Observer;

public abstract class Observable {
    List<Observer> observers = new ArrayList<Observer>();
    
    public void inform() {
        for (Observer o : this.observers) {
            o.update();
        }
    };

    public void subscribe(Observer o) {
        this.observers.add(o);
    }
    public void unsubscribe(Observer o) {
        this.observers.remove(o);
    }

}
