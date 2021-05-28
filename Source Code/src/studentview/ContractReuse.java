package studentview;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.JSpinner.ListEditor;

import mainview.MouseClickListener;
import mainview.Observer;
import mainview.RemovablePanel;
import model.EventType;
import model.Contract;

/**
 * Display expired / terminated contracts for reuse
 */
public class ContractReuse extends RemovablePanel {
    private List<Contract> contracts;
    private JList<Contract> contractList;
    public ContractReuse(List<Contract> contracts) {
        super(new BorderLayout());
        this.contracts = contracts;
        placeComponents();
    }

    private void placeComponents() {
        this.removeAll();
        DefaultListModel<Contract> model = new DefaultListModel<>();
        for (Contract contract : contracts)
            model.addElement(contract);
        contractList= new JList<>(model);
        contractList.setCellRenderer(new ContractCellRenderer());


        JScrollPane scrollp = new JScrollPane(contractList);
        this.add(scrollp);
}

    @Override
    public void update(EventType e) {
        placeComponents();
    }
    
    public void setReuseContractListener(MouseClickListener listener) {
        this.contractList.addMouseListener(listener);
    }

    public Contract getSelectedContract() {
        return this.contractList.getSelectedValue();
    }

    private class ContractCellRenderer extends JPanel implements ListCellRenderer<Contract> {

        @Override
        public Component getListCellRendererComponent(JList<? extends Contract> list, Contract value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            this.removeAll();
            String text = value.toString();
            JTextArea tA = new JTextArea();
            tA.setText(text);
            tA.setEditable(false);
            this.add(tA);
            return this;
        }
    }
}

