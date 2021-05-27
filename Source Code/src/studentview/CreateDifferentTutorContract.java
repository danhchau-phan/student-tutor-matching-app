package studentview;

import mainview.MouseClickListener;
import mainview.RemovablePanel;
import model.Contract;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CreateDifferentTutorContract extends RemovablePanel {
    private List<Contract> contracts = new ArrayList<>();
    public JComboBox<String> tutorsContract = new JComboBox<String>();
    private String selectedTutor;

    private JButton reuseContractButton = new JButton("Reuse");

    public CreateDifferentTutorContract() {
        super(new BorderLayout());
    }

    public void show() {
        int result = JOptionPane.showConfirmDialog(new CreateDifferentTutorContract(),
                null, "Reuse Contract!", JOptionPane.DEFAULT_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            this.selectedTutor = (String) tutorsContract.getSelectedItem();
        }
    }

    public void setListener(MouseClickListener listener) {
        this.reuseContractButton.addMouseListener(listener);
    }

    public void addAllContract(List<Contract> contractList) {
        this.contracts.addAll(contractList);
        for (Contract contract: this.contracts) {
            tutorsContract.addItem(contract.getSecondPartyId());
        }
    }

    public String getSelectedTutor() {
        return this.selectedTutor;
    }
}
