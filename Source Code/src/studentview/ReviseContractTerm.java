package studentview;

import javax.swing.*;

import mainview.MouseClickListener;
import mainview.RemovablePanel;
import model.Contract;
import model.EventType;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Revise contract term if reused
 */
public class ReviseContractTerm extends RemovablePanel {
    private Contract contract;
    private JButton sameTutorReuse = new JButton("Reuse With Same Tutor");
    private JButton differentTutorReuse = new JButton("Reuse With Different Tutor");
    
    private Strategy strategy;
    
    public ReviseContractTerm() {
        super(new BorderLayout());
        placeComponents();
    }

    protected void placeComponents() {
        JPanel panel= new JPanel();
        this.add(panel);
        panel.add(sameTutorReuse);
        panel.add(differentTutorReuse);
    }

    public void setReuseSameTutorListener(MouseClickListener listener) {
        sameTutorReuse.addMouseListener(listener);
    }

    public void setReuseDifferentTutorListener(MouseClickListener listener) {
        differentTutorReuse.addMouseListener(listener);
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

	@Override
	public void update(EventType e) {
	}
	
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
		this.strategy.execute();
	}
}

