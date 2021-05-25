package studentview;

import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner.ListEditor;

import java.awt.BorderLayout;

import mainview.MouseClickListener;
import mainview.Observer;
import mainview.RemovablePanel;
import model.EventType;
import model.Contract;

/**
 * Display expired / terminated contracts for reuse
 */
public class ContractReuse extends RemovablePanel implements Observer{
    private List<Contract> contracts;
    private JList<Contract> contractList;
    public ContractReuse(List<Contract> contracts) {
        super(new BorderLayout());
        this.contracts = contracts;
    }

    private void placeComponents() {
        this.removeAll();
        /////////// INCOMPLETE //////////////
    }

    @Override
    public void update(EventType e) {
        placeComponents();
    }
    
    public void setReuseContractListener(MouseClickListener listener) {
        this.contractList.addMouseListener(listener);
    }

    public Contract getSelectedContract() {
        return this.contractList.getSelectedValue();
    }
}
