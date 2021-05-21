package studentview;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mainview.ListPanel;
import mainview.MouseClickListener;
import mainview.Utils;
import model.Contract;
import model.User;

/**
 * View that displays all contracts where this Student is first party
 */
public class StudentAllContracts extends JPanel {
	public static final int CONTRACT_QUOTA = 5; 
	User user;
	public StudentAllContracts(User user) {
		super(new BorderLayout());
		this.setBackground(Color.CYAN);
		this.user = user;
	}
	
	protected void placeComponents() {
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
						if (contractCnt >= StudentAllContracts.CONTRACT_QUOTA) {
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
		JScrollPane scrollp = new JScrollPane(midPanel);
		this.add(midPanel);
		this.add(scrollp);
	}

}
