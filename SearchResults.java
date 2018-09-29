import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SearchResults extends JPanel implements ItemListener {
	private CampusTradeClient campusTradeClient;
	private int category = 0;
	private String keyword = null;
	
	private SearchPanel searchPanel;
	private JLabel promptLabel;
	private JTable resultsTable;
	private List resultsList;
	private JLabel detailsLabel;
	java.util.Vector<GoodRecord> goodRecords;
	
	public SearchResults(CampusTradeClient ct, int c, String s) {
		goodRecords = new java.util.Vector<GoodRecord>();
		campusTradeClient = ct;
		category = c;
		keyword = s;
		setLayout(null);
		setBackground(Color.WHITE);
	
		searchPanel = new SearchPanel(ct);
		searchPanel.setBounds(new Rectangle(0, 0, 500, 50));
		
		promptLabel = new JLabel();
		promptLabel.setBounds(new Rectangle(0, 50, 500, 30));
		if (category != 0) {
			if (keyword != null && !keyword.equals(""))
				promptLabel.setText("You are looking for " + keyword + " in category " + GoodComboBox.getNameByIndex(category) + ":");
			else
				promptLabel.setText("You are looking in category " + GoodComboBox.getNameByIndex(category) + ":");
		}
		else {
			if (keyword != null && !keyword.equals(""))
				promptLabel.setText("You are looking for " + keyword + ":");
			else
				promptLabel.setText("You are looking at all records:");
		}
		
		resultsList = new List();
		resultsList.setBounds(0, 80, 500, 300);
		resultsList.setMultipleMode(false);
		resultsList.addItemListener(this);
		StringBuffer columnNames = new StringBuffer("Category");
		for (int i = 0; i < 26; i++) {
			columnNames.append(' ');
		}
		columnNames.append("Name");
		for (int i = 0; i < 30; i++) {
			columnNames.append(' ');
		}
		columnNames.append("Price");
		resultsList.add(columnNames.toString());
		
		detailsLabel = new JLabel();
		detailsLabel.setBounds(0, 380, 500, 40);
		
		add(searchPanel);
		add(promptLabel);
		add(resultsList);
		add(detailsLabel);
		
		ct.sendMsg("#SEARCH");
		ct.sendMsg(category + "");
		ct.sendMsg(keyword);
	}
	
	public void addRecord(GoodRecord gr) {
		goodRecords.addElement(gr);
	}
	
	public void refreshTable() {
		if (goodRecords.isEmpty()) {
			resultsList.add("No results found!");
			return;
		}
		for (int i = 0; i < goodRecords.size(); i++) {
			// System.out.println(goodRecords.elementAt(i).getCategory());
			int category = Integer.parseInt(goodRecords.elementAt(i).getCategory());
			String c = GoodComboBox.getNameByIndex(category);
			String n = goodRecords.elementAt(i).getName();
			String p = goodRecords.elementAt(i).getPrice();
			StringBuffer recordString = new StringBuffer(c);
			for (int i1 = 0; i1 < GoodComboBox.getBlanksByIndex(category); i1++) {
				recordString.append(' ');
			}
			recordString.append(n);
			for (int i1 = 0; i1 < 22 - 2 * n.length(); i1++) {
				recordString.append("  ");
			}
			recordString.append(p);
			resultsList.add(recordString.toString());
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		int selected = resultsList.getSelectedIndex();
		if (selected == 0) {
			detailsLabel.setText("");
			return;
		}
		GoodRecord tmp = goodRecords.elementAt(selected - 1);
		detailsLabel.setText(tmp.getOwnerName() + "   " + tmp.getOwnerID() + "   " + tmp.getOwnerPhone());
	}
}