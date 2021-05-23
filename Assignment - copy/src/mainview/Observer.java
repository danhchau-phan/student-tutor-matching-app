package mainview;

import model.EventType;

public interface Observer {
    public void update(EventType e);
}
