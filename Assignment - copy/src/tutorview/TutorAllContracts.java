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
public class TutorAllContracts extends JPanel implements Observer {
//	public static final int CONTRACT_QUOTA = 5;
	private JList<Contract> contractList;
	private Contract subscriber;
	List<Contract> contracts;
	private User user;

	public TutorAllContracts(User user, Contract subsriber) {
		super(new BorderLayout());
		this.user = user;
		this.subscriber = subsriber;
		this.contracts = subscriber.getAllContractsAsFirstParty(user.getId());
		placeComponents();
	}

	private void placeComponents() {
		DefaultListModel<Contract> model = new DefaultListModel<Contract>();
		for (Contract c : contracts)
			model.addElement(c);
		contractList = new JList<>(model);

		contractList.setCellRenderer(new ContractCellRenderer());

		JScrollPane scrollp = new JScrollPane(contractList);
		this.add(scrollp);
//		List<Contract> contracts = new ArrayList<Contract>();
//		contracts = Contract.getAllContractsAsSecondParty(user.getId());
//
//		List<JComponent> comp = new ArrayList<JComponent>();
//
//		for (Contract c : contracts) {
//			JPanel panel = new JPanel();
//			if (c.secondPartySigned()) {
//				JTextArea tA = new JTextArea();
//				tA.setText(c.toString());
//				tA.setEditable(false);
//				panel.add(tA);
//			} else if (c.secondPartySigned() == false) {
//				JTextArea tA = new JTextArea();
//				JButton bT = new JButton("Sign");
//
//				tA.setText(c.toString());
//				tA.setEditable(false);
//				panel.add(tA, BorderLayout.WEST);
//				panel.add(bT, BorderLayout.EAST);
//
//				bT.addMouseListener(new MouseClickListener() {
//					@Override
//					public void mouseClicked(MouseEvent e) {
//						c.secondPartySign(true);
//						if (c.isSigned()) {
//							c.signContract();
//							Utils.CONTRACT_SIGNED.show();
//						} else
//							Utils.OTHER_PARTY_PENDING.show();
//					}
//				});
//			}
//			comp.add(panel);
//		}
//
//		JPanel midPanel = new ListPanel(comp);
//        main.add(midPanel);
//		JScrollPane scrollp = new JScrollPane(midPanel);
//		main.add(scrollp);
//		this.display.setVisible();
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
			if (c.firstPartySigned()) {
				JTextArea tA = new JTextArea();
				tA.setText(c.toString());
				tA.setEditable(false);
				panel.add(tA);
			} else if (c.firstPartySigned() == false) {
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

	}
}
