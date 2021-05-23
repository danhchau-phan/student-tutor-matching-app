package studentview;

import java.util.List;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import mainview.Observer;
import model.EventType;
import model.Contract;

/**
 * Display expired / terminated contracts for reuse
 */
public class ContractReuse extends JPanel implements Observer{
    private List<Contract> contracts;
    public ContractReuse(List<Contract> contracts) {
        super(new BorderLayout());
        this.contracts = contracts;
    }

    private void placeComponents() {
        this.removeAll();
    }

    @Override
    public void update(EventType e) {
        placeComponents();
    }
    
}
