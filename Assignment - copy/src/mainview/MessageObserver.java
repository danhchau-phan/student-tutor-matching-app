package mainview;

import model.Message;

public interface MessageObserver extends Observer {
    public void update(Message message);
}
