package tutorview;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import model.EventType;

import javax.swing.*;

import mainview.*;
import model.Contract;
import model.User;

/**
 * This is the View for the Tutor to see all the contracts that involves them as the second party
 */
public class TutorAllContracts extends RemovablePanel {
	private JList<Contract> contractList;
	List<Contract> contracts;

	public TutorAllContracts(List<Contract> contracts) {
		super(new BorderLayout());
		this.contracts = contracts;
		placeComponents();
	}

	private void placeComponents() {
		this.removeAll();
		this.revalidate();
		this.repaint();
		DefaultListModel<Contract> model = new DefaultListModel<Contract>();
		for (Contract c : contracts)
			model.addElement(c);
		contractList = new JList<>(model);

		contractList.setCellRenderer(new ContractCellRenderer());

		JScrollPane scrollp = new JScrollPane(contractList);
		this.add(scrollp);
	}

	public void setSignContractListener(MouseClickListener listener) {
		contractList.addMouseListener(listener);
	}

	public Contract getSelectedContract() {
		return contractList.getSelectedValue();
	}

	public int getSignedContracts() {
		int cnt = 0;
		for (Contract c : contracts) {
			if (c.isSigned())
				cnt++;
		}
		return cnt;
	}

	private class ContractCellRenderer extends JPanel implements ListCellRenderer<Contract> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Contract> list, Contract c, int index,
													  boolean isSelected, boolean cellHasFocus) {
			this.removeAll();
			JPanel panel = new JPanel();
			if (c.secondPartySigned() || c.isSigned()) {
				JTextArea tA = new JTextArea();
				tA.setText(c.toString());
				tA.setEditable(false);
				panel.add(tA);
			} else {
				JTextArea tA = new JTextArea();
				JButton bT = new JButton("Sign");

				tA.setText(c.toString());
				tA.setEditable(false);
				panel.add(tA, BorderLayout.CENTER);
				panel.add(bT, BorderLayout.EAST);
			}
			this.add(panel);
			return this;
		}

	}


	@Override
	public void update(EventType e) {
		this.placeComponents();
	}
}
