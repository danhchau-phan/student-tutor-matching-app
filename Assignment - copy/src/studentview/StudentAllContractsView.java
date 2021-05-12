package studentview;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mainview.Display;
import mainview.ListPanel;
import mainview.MouseClickListener;
import mainview.Utils;
import model.Contract;
import model.User;

/**
 * View that displays all contracts where this Student is first party
 */
class StudentAllContractsView extends StudentView {
	public static final int CONTRACT_QUOTA = 5; 
	public StudentAllContractsView(Display display, User user) {
		super(display, user);
	}
	
	protected void placeComponents() {
		super.placeComponents();
		
		List<Contract> contracts = new ArrayList<Contract>();
		contracts = Contract.getAllContractsAsFirstParty(user.getId());
		
		List<JComponent> comp = new ArrayList<JComponent>();
		
		int cnt = 0;
		for (Contract c : contracts) {
			if (c.isSigned())
				cnt++;
		}
		final int contractCnt = cnt; 
		for (Contract c : contracts) {
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

				bT.addMouseListener(new MouseClickListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (contractCnt >= StudentAllContractsView.CONTRACT_QUOTA) {
							Utils.REACHED_CONTRACT_LIMIT.show();
						}
						else {
							c.firstPartySign(true);
							if (c.isSigned()) {
								c.signContract();
								Utils.CONTRACT_SIGNED.show();
							} else
								Utils.OTHER_PARTY_PENDING.show();
						}
					}
				});
			}
			comp.add(panel);
		}
		
		JPanel midPanel = new ListPanel(comp);
        main.add(midPanel);
		JScrollPane scrollp = new JScrollPane(midPanel);
		main.add(scrollp);
		this.display.setVisible();
	}

}
