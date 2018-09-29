import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ContactUs extends JPanel implements ActionListener {
	private CampusTradeClient campusTradeClient;
	private List lstMsg = new List();
	private JTextField txtInput = new JTextField();
	private JButton btnSend = new JButton("Send");
	private JLabel sendResult = new JLabel();
	
	public ContactUs(CampusTradeClient ct) {
		campusTradeClient = ct;
		setLayout(null);
		setBackground(Color.WHITE);
		
		lstMsg.setBounds(0, 0, 500, 340);
		lstMsg.setMultipleMode(false);
		txtInput.setBounds(10, 350, 420, 40);
		txtInput.addActionListener(this);
		btnSend.setBounds(430, 350, 60, 40);
		btnSend.addActionListener(this);
		sendResult.setBounds(200, 390, 100, 20);
		
		add(lstMsg, null);
		add(txtInput, null);
		add(btnSend, null);
		add(sendResult,null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String msg = txtInput.getText();
		if (msg == null || msg.length() == 0) {
			sendResult.setForeground(Color.RED);
			sendResult.setText("不能发送空信息");
			return;
		}
		boolean success = true;
		if (!campusTradeClient.sendMsg("#CHAT"))
			success = false;
		if (!campusTradeClient.sendMsg(msg))
			success = false;
		if (success) {
			sendResult.setForeground(Color.BLUE);
			sendResult.setText("提交成功");
			lstMsg.add("Client: " + msg);
		}
		else {
			sendResult.setForeground(Color.RED);
			sendResult.setText("提交失败");
		}
	}
	
	public void addMsgFromServer(String msg) {
		lstMsg.add("Server: " + msg);
	}
}