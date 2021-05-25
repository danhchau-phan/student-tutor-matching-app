package studentview;

import javax.swing.JPanel;

import mainview.RemovablePanel;
import model.Contract;

/**
 * Revise contract term if reused
 */
public class ReviseContractTerm extends RemovablePanel {
    private Contract contract;
    public ReviseContractTerm() {
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
