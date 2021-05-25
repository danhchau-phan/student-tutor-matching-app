package studentview;

import javax.swing.*;

import mainview.MouseClickListener;
import mainview.RemovablePanel;
import model.Contract;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Revise contract term if reused
 */
public class ReviseContractTerm extends RemovablePanel {
    private Contract contract;
    private JButton sameTutorReuse = new JButton("Reuse With Same Tutor");
    private JButton differentTutorReuse = new JButton("Reuse With Different Tutor");

    public ReviseContractTerm() {
        super(new BorderLayout());
    }

    protected void placeComponents() {
        JPanel panel= new JPanel();
        this.add(panel);
        panel.add(sameTutorReuse);
        panel.add(differentTutorReuse);

        sameTutorReuse.addMouseListener(new MouseClickListener(){

            @Override
            public void mouseClicked(MouseEvent e) {

            }

        });
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
