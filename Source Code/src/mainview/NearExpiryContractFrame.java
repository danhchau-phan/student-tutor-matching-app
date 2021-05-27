package mainview;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import model.Bid;
import model.Contract;

public class NearExpiryContractFrame extends JPanel{
    List<Contract> contracts;
    public NearExpiryContractFrame(List<Contract> contracts) {
        super();
        this.contracts = contracts;
    }

    public void show() {
    	System.out.print(this.contracts.size());
        this.removeAll();
        JList<Contract> cList;
        DefaultListModel<Contract> model = new DefaultListModel<>();
        for (Contract c : contracts) {
            model.addElement(c);
        }
        cList = new JList<>(model);
        cList.setCellRenderer(new CellRenderer());

        JScrollPane scrollp = new JScrollPane(cList);
		this.add(scrollp);

        JOptionPane.showMessageDialog(null, this);
    }
    
    private class CellRenderer extends JPanel implements ListCellRenderer<Contract> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Contract> list, Contract contract, int index,
                boolean isSelected, boolean cellHasFocus) {
			this.removeAll();
			JTextArea tA = new JTextArea();
            tA.setText(contract.toString());
            tA.setEditable(false);
            this.add(tA);
			return this;
		}

	}
}
