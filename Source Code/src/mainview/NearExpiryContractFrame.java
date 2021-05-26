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

import model.Contract;

public class NearExpiryContractFrame extends JPanel{
    List<Contract> contracts;
    public NearExpiryContractFrame(List<Contract> contracts) {
        super();
        this.contracts = contracts;
    }

    public void show() {
        this.removeAll();
        JList<Contract> cList;
        DefaultListModel<Contract> model = new DefaultListModel<>();
        for (Contract c : contracts) {
            model.addElement(c);
        }
        cList = new JList<>(model);
        cList.setCellRenderer(new ListCellRenderer<Contract>(){

            @Override
            public Component getListCellRendererComponent(JList<? extends Contract> list, Contract contract, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JTextArea tA = new JTextArea();
                tA.setText(contract.toString());
                tA.setEditable(false);
                return tA; // DOUBLE CHECK
            }
            
        });

        JScrollPane scrollp = new JScrollPane(cList);
		this.add(scrollp);

        JOptionPane.showMessageDialog(null, this, "Contracts About to expire", JOptionPane.INFORMATION_MESSAGE);
    }
}
