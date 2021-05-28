package studentview;

import mainview.MouseClickListener;
import mainview.RemovablePanel;
import mainview.Utils;
import model.Bid;
import model.Contract;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CreateDifferentTutorContract extends JPanel implements Strategy {
    private JComboBox<String> allTutors;
    private Contract contract;
    private String selectedTutorId;

    public CreateDifferentTutorContract(Contract contract, List<String> allTutorsId) {
        super();
        this.contract = contract;
        allTutors = new JComboBox<String>(allTutorsId.toArray(new String[0]));
        this.add(allTutors);
    }

    public void execute() {
        int result = JOptionPane.showConfirmDialog(null,
                this, "Reuse Contract With Different Tutor", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.INFORMATION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            this.selectedTutorId = (String) allTutors.getSelectedItem();
            if (User.getCompetency(selectedTutorId, contract.getSubjectId()) < contract.getRequiredCompetency() + Bid.COMPETENCY_PADDING)
            	Utils.INSUFFICIENT_COMPETENCY_REUSE_CONTRACT.show();
            	this.show();
        }
    }

    public String getSelectedTutor() {
        return this.selectedTutorId;
    }
}
