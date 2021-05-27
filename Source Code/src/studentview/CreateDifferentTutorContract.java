package studentview;

import mainview.MouseClickListener;
import mainview.RemovablePanel;
import model.Contract;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CreateDifferentTutorContract extends JPanel {
    public JComboBox<String> allTutors;
    private String selectedTutorId;

    public CreateDifferentTutorContract(List<String> allTutorsId) {
        super();
        allTutors = new JComboBox<String>((String[]) allTutorsId.toArray());
        this.add(allTutors);
    }

    public void show() {
        int result = JOptionPane.showConfirmDialog(null,
                this, "Reuse Contract With Different Tutor", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.INFORMATION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            this.selectedTutorId = (String) allTutors.getSelectedItem();
        }
    }

    public String getSelectedTutor() {
        return this.selectedTutorId;
    }
}
