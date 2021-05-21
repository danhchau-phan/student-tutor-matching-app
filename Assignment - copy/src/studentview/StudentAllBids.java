package studentview;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;

import mainview.ListPanel;
import mainview.MouseClickListener;
import mainview.Observer;
import model.Bid;
import model.Model;

/**
 * View that allows the Student to see all unexpired and unclosed match requests created by them
 */
public class StudentAllBids extends JPanel implements Observer{
	List<Bid> bids;
	JList<JPanel> bidList;
	public StudentAllBids(List<Bid> bids) {
		super(new BorderLayout());
		this.setBackground(Color.BLUE);
		this.bids = bids;
		placeComponents();
	}
	
	private void placeComponents() {
		DefaultListModel<JPanel> model = new DefaultListModel<JPanel>();
		// ArrayList<JComponent> panels = new ArrayList<JComponent> ();
		for (Bid b : bids) {
			String text = b.toString();
			JPanel panel = new JPanel();
			JTextArea tA = new JTextArea();
			tA.setText(text);
			panel.add(tA);
			tA.setEditable(false);
			// tA.addMouseListener(new MouseClickListener(){
			// 	@Override
			// 	public void mouseClicked(MouseEvent e) {
										
			// 	}
			// });
			// this.addSwitchPanelListener(main, tA, new StudentResponseView(display, user, b));
			// panels.add(panel);
			model.addElement(panel);
		}
		bidList = new JList<JPanel>(model);
		// bidList.setCellRenderer(new ListCellRenderer<JPanel>(){
		// 	@Override
		// 	public Component getListCellRendererComponent(JList<? extends JPanel> list, JPanel value, int index,
		// 			boolean isSelected, boolean cellHasFocus) {
		// 		// TODO Auto-generated method stub
		// 		return null;
		// 	}
		// });
		JScrollPane scrollp = new JScrollPane(bidList);
		this.add(scrollp);
		// JPanel midPanel = new ListPanel(panels);
		// JScrollPane scrollp = new JScrollPane(midPanel);
		// this.add(midPanel);
		// this.add(scrollp);
	}

	public int getSelectedIndex() {
		return this.bidList.getSelectedIndex();
	}

	public void setListListener(MouseClickListener listener) {
		this.bidList.addMouseListener(listener);
	}

	@Override
	public void update(Model model) {
		// TODO Auto-generated method stub
		
	}
}
