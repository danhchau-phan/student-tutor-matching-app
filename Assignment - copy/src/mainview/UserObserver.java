package mainview;

import model.User;

public interface UserObserver {
    public void update(User user);
}
