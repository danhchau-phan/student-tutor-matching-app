package studentview;

import mainview.MouseClickListener;
import mainview.RemovablePanel;
import model.Contract;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class CreateDifferentTutorContract extends RemovablePanel {
    private List<User> contractTutors = new ArrayList<>();
    public JButton reuseContractButton = new JButton("reuseWithDifferentTutor");
    public JComboBox<String> tutorList = new JComboBox<>();

    public CreateDifferentTutorContract() {
        super(new BorderLayout());
    }

    private void placeComponents() {
        for (User tutor: contractTutors) {
            tutorList.addItem(tutor.getId() + tutor.getFullName());
        }
    }

    public void setReuseWithDifferentTutorButtonListener(MouseClickListener listener) {
        this.reuseContractButton.addMouseListener(listener);
    }

    public void setContractTutors(List<User> contractTutors) {
        this.contractTutors.addAll(contractTutors);
        placeComponents();
    }
}
