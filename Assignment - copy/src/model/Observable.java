package model;
import java.util.List;

import mainview.Observer;

public abstract class Observable {
    List<Observer> observers;
    
    // public abstract void notify();
    public void subscribe(Observer o) {
        this.observers.add(o);
    }
    public void unsubscribe(Observer o) {
        this.observers.remove(o);
    }

}
