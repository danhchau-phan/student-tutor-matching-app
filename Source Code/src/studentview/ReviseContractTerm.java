package studentview;

import javax.swing.JPanel;

import model.Contract;

/**
 * Revise contract term if reused
 */
public class ReviseContractTerm extends JPanel {
    private Contract contract;
    public ReviseContractTerm() {
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
