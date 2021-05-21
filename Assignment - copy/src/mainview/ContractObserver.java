package mainview;

import model.Contract;

public interface ContractObserver extends Observer{
    public void update(Contract contract);
}
