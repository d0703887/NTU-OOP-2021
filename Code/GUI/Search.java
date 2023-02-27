package com.tcg.json;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;

import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;

public class Search {

	private JFrame frame;
	private JTextField textUID;
	private JTextField textDate;
	private JTable table;
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	private JTextField textCode;
	private JTextField textTrainNo;
	private JTextField textDepart;
	private JTextField textArrival;
	private JTextField textDuration;
	private JTextField textTicket;
	private JTextField textCar;
	private JTextField textSeat;
	private JTextField textFare;
	private JTextField textTicketCount;
	String[][] g;
	int GB = 0;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Search window = new Search();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Search() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBounds(10, 21, 966, 75);
		frame.getContentPane().add(panel);

		JLabel lblNewLabel = new JLabel("HSR Searching System");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Car Type");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(581, 124, 74, 15);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Ticket Type");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_1.setBounds(360, 124, 105, 15);
		frame.getContentPane().add(lblNewLabel_1_1);

		JComboBox cmbFrom = new JComboBox();
		cmbFrom.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cmbFrom.setModel(new DefaultComboBoxModel(new String[] { "From", "Nangang", "Taipei", "Banciao", "Taoyuan",
				"Hsinchu", "Miaoli", "Taichung", "Changhua", "Yunlin", "Chiayi", "Tainan", "Zuoying" }));
		cmbFrom.setBounds(59, 248, 149, 23);
		frame.getContentPane().add(cmbFrom);

		JComboBox cmbTo = new JComboBox();
		cmbTo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cmbTo.setModel(new DefaultComboBoxModel(new String[] { "To", "Nangang", "Taipei", "Banciao", "Taoyuan",
				"Hsinchu", "Miaoli", "Taichung", "Changhua", "Yunlin", "Chiayi", "Tainan", "Zuoying" }));
		cmbTo.setBounds(232, 248, 149, 23);
		frame.getContentPane().add(cmbTo);

		JLabel lblNewLabel_1_1_1 = new JLabel("Station");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_1_1.setBounds(22, 222, 105, 15);
		frame.getContentPane().add(lblNewLabel_1_1_1);

		JLabel lblNewLabel_2 = new JLabel("UID");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(22, 124, 47, 15);
		frame.getContentPane().add(lblNewLabel_2);

		textUID = new JTextField();
		textUID.setBounds(79, 118, 96, 21);
		frame.getContentPane().add(textUID);
		textUID.setColumns(10);

		JLabel lblNewLabel_1_2 = new JLabel("Time Table");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_2.setBounds(42, 327, 105, 15);
		frame.getContentPane().add(lblNewLabel_1_2);

		JLabel lblNewLabel_2_1 = new JLabel("Date");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_1.setBounds(22, 164, 47, 15);
		frame.getContentPane().add(lblNewLabel_2_1);

		textDate = new JTextField();
		textDate.setColumns(10);
		textDate.setBounds(79, 164, 167, 45);
		frame.getContentPane().add(textDate);

		JLabel lblNewLabel_1_1_2 = new JLabel("Seat");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_1_2.setBounds(446, 164, 47, 15);
		frame.getContentPane().add(lblNewLabel_1_1_2);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(22, 312, 954, 15);
		frame.getContentPane().add(separator_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 352, 954, 188);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setFont(new Font("Tahoma", Font.BOLD, 16));
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "TrainNo", "\u5357\u6E2F", "\u53F0\u5317", "\u677F\u6A4B", "\u6843\u5712",
						"\u65B0\u7AF9", "\u82D7\u6817", "\u53F0\u4E2D", "\u5F70\u5316", "\u96F2\u6797", "\u5609\u7FA9",
						"\u53F0\u5357", "\u5DE6\u71DF" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(25);
		table.getColumnModel().getColumn(1).setPreferredWidth(25);
		table.getColumnModel().getColumn(2).setPreferredWidth(25);
		table.getColumnModel().getColumn(3).setPreferredWidth(25);
		table.getColumnModel().getColumn(4).setPreferredWidth(25);
		table.getColumnModel().getColumn(5).setPreferredWidth(25);
		table.getColumnModel().getColumn(6).setPreferredWidth(25);
		table.getColumnModel().getColumn(7).setPreferredWidth(25);
		table.getColumnModel().getColumn(8).setPreferredWidth(25);
		table.getColumnModel().getColumn(9).setPreferredWidth(25);
		table.getColumnModel().getColumn(10).setPreferredWidth(25);
		table.getColumnModel().getColumn(11).setPreferredWidth(25);
		table.getColumnModel().getColumn(12).setPreferredWidth(25);

		JSeparator separator_1_3_1 = new JSeparator();
		separator_1_3_1.setBounds(22, 550, 954, 15);
		frame.getContentPane().add(separator_1_3_1);

		JLabel lblNewLabel_1_2_1 = new JLabel("Discount Train");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_2_1.setBounds(42, 564, 138, 15);
		frame.getContentPane().add(lblNewLabel_1_2_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(22, 589, 954, 164);
		frame.getContentPane().add(scrollPane_1);

		table_1 = new JTable();
		scrollPane_1.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "TrainNo", "From", "To",
				"Discount(\u5927\u5B78\u751F/\u65E9\u9CE5)", "Departure", "Arrival", "Duration" }));
		table_1.getColumnModel().getColumn(0).setPreferredWidth(20);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(5).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(6).setPreferredWidth(30);

		JLabel lblNewLabel_2_2 = new JLabel("Code");
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_2.setBounds(185, 124, 47, 15);
		frame.getContentPane().add(lblNewLabel_2_2);

		textCode = new JTextField();
		textCode.setColumns(10);
		textCode.setBounds(244, 118, 96, 21);
		frame.getContentPane().add(textCode);

		JLabel lblNewLabel_2_1_1 = new JLabel("Train No.");
		lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_1_1.setBounds(256, 164, 74, 15);
		frame.getContentPane().add(lblNewLabel_2_1_1);

		textTrainNo = new JTextField();
		textTrainNo.setColumns(10);
		textTrainNo.setBounds(340, 164, 96, 45);
		frame.getContentPane().add(textTrainNo);

		JLabel lblNewLabel_1_3 = new JLabel("Departure");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_3.setBounds(418, 222, 96, 15);
		frame.getContentPane().add(lblNewLabel_1_3);

		JLabel lblNewLabel_1_4 = new JLabel("Arrival");
		lblNewLabel_1_4.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_4.setBounds(546, 222, 74, 15);
		frame.getContentPane().add(lblNewLabel_1_4);

		JLabel lblNewLabel_1_5 = new JLabel("Duration");
		lblNewLabel_1_5.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_5.setBounds(673, 222, 74, 15);
		frame.getContentPane().add(lblNewLabel_1_5);

		textDepart = new JTextField();
		textDepart.setColumns(10);
		textDepart.setBounds(418, 248, 96, 45);
		frame.getContentPane().add(textDepart);

		textArrival = new JTextField();
		textArrival.setColumns(10);
		textArrival.setBounds(546, 248, 96, 45);
		frame.getContentPane().add(textArrival);

		textDuration = new JTextField();
		textDuration.setColumns(10);
		textDuration.setBounds(673, 248, 174, 45);
		frame.getContentPane().add(textDuration);

		textTicket = new JTextField();
		textTicket.setColumns(10);
		textTicket.setBounds(475, 118, 96, 21);
		frame.getContentPane().add(textTicket);

		textCar = new JTextField();
		textCar.setColumns(10);
		textCar.setBounds(665, 118, 96, 21);
		frame.getContentPane().add(textCar);

		textSeat = new JTextField();
		textSeat.setColumns(10);
		textSeat.setBounds(492, 164, 96, 45);
		frame.getContentPane().add(textSeat);

		textFare = new JTextField();
		textFare.setColumns(10);
		textFare.setBounds(862, 124, 96, 21);
		frame.getContentPane().add(textFare);

		JLabel lblNewLabel_1_1_2_1 = new JLabel("Fare");
		lblNewLabel_1_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_1_2_1.setBounds(805, 124, 47, 15);
		frame.getContentPane().add(lblNewLabel_1_1_2_1);

		JLabel lblNewLabel_3 = new JLabel("( Input Date )");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_3.setBounds(140, 327, 99, 15);
		frame.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel_3_1 = new JLabel("( Input Date and Station )");
		lblNewLabel_3_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_3_1.setBounds(164, 564, 230, 15);
		frame.getContentPane().add(lblNewLabel_3_1);

		JLabel lblNewLabel_1_1_2_2 = new JLabel("Count");
		lblNewLabel_1_1_2_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_1_2_2.setBounds(609, 164, 109, 15);
		frame.getContentPane().add(lblNewLabel_1_1_2_2);

		textTicketCount = new JTextField();
		textTicketCount.setColumns(10);
		textTicketCount.setBounds(673, 164, 96, 21);
		frame.getContentPane().add(textTicketCount);

		JButton btnSearch = new JButton("Search");
		btnSearch.setToolTipText("Search ticket");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[][] b = new String[1][30];
				TicketSearch a = new TicketSearch();
				frame = new JFrame("Search");
				if (!(textUID.getText().equals("")) && !(textCode.getText().equals(""))) {
					b = a.TicketSearchWithCode(textCode.getText(), textUID.getText());
					if (b[0][0] == "您輸入的身份識別號碼有誤，請重新輸入") {
						JOptionPane.showMessageDialog(frame, "您輸入的身份識別號碼有誤，請重新輸入");
					} else if (b[0][0] == "您輸入的訂位代號有誤，請重新輸入") {
						JOptionPane.showMessageDialog(frame, "您輸入的訂位代號有誤，請重新輸入");

					} else if (b[0][0] == "無此訂票紀錄") {
						JOptionPane.showMessageDialog(frame, "無此訂票紀錄");

					} else {
						textDate.setText(b[0][2]);
						textTrainNo.setText(b[0][1]);
						textCar.setText(b[0][18]);
						textTicket.setText(b[0][24]);
						textSeat.setText(b[0][8]);
						textFare.setText(b[0][20]);
						textDepart.setText(b[0][5]);
						textArrival.setText(b[0][6]);
						textDuration.setText(b[0][7]);
						textTicketCount.setText(b[0][19]);
						cmbFrom.setSelectedItem(b[0][3]);
						cmbTo.setSelectedItem(b[0][4]);
					}
				} else if (!(textUID.getText().equals("")) && !(textDate.getText().equals(""))
						&& !(textTrainNo.getText().equals("")) && !(cmbFrom.getSelectedItem().equals("From"))
						&& !(cmbTo.getSelectedItem().equals("To"))) {
					b = a.CodeSearch(textUID.getText(), a.NameToStationID((String) cmbFrom.getSelectedItem()),
							a.NameToStationID((String) cmbTo.getSelectedItem()), textDate.getText(),
							textTrainNo.getText());
					if (b[0][0] == "查無記錄") {
						JOptionPane.showMessageDialog(frame, "查無記錄");
					} else {
						textUID.setText(b[0][2]);
						textCode.setText(b[0][0]);
						textDate.setText(b[0][6]);
						textTrainNo.setText(b[0][5]);
						textCar.setText(b[0][17]);
						textTicket.setText(b[0][13]);
						textSeat.setText(b[0][12]);
						textFare.setText(b[0][11]);
						textDepart.setText(b[0][8]);
						textArrival.setText(b[0][10]);
						textTicketCount.setText(b[0][14]);
						textDuration.setText(b[0][16]);
						cmbFrom.setSelectedItem(b[0][7]);
						cmbTo.setSelectedItem(b[0][9]);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "資訊不足 無法查詢");
				}
				g = b;
			}
		});

		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSearch.setBounds(42, 281, 96, 23);
		frame.getContentPane().add(btnSearch);

		JButton btnReset = new JButton("Reset");
		btnReset.setToolTipText("Reset System");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				cmbFrom.setSelectedItem("From");
				cmbTo.setSelectedItem("To");
				textUID.setText(null);
				textDate.setText(null);
				textCode.setText(null);
				textTrainNo.setText(null);
				textCar.setText(null);
				textTicket.setText(null);
				textSeat.setText(null);
				textFare.setText(null);
				textDepart.setText(null);
				textArrival.setText(null);
				textDuration.setText(null);
				textTicketCount.setText(null);
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0);
				DefaultTableModel model_1 = (DefaultTableModel) table_1.getModel();
				model_1.setRowCount(0);
			}
		});

		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnReset.setBounds(873, 218, 85, 23);
		frame.getContentPane().add(btnReset);

		JButton btnExit = new JButton("Exit");
		btnExit.setToolTipText("Exit System");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frame = new JFrame("Exit");
				if (JOptionPane.showConfirmDialog(frame, "Confirm if you want to exit", "Booking Systems",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
					System.exit(0);
				}
			}
		});
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnExit.setBounds(873, 270, 85, 23);
		frame.getContentPane().add(btnExit);

		JButton btnSearcht = new JButton("SearchT");
		btnSearcht.setToolTipText("Timetable search");
		btnSearcht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				if (textDate.getText().equals("")) {
					JOptionPane.showMessageDialog(frame, "未輸入日期");
				} else {
					try {

						model.setRowCount(0);
						TicketSearch a = new TicketSearch();
						int c = a.Number(textDate.getText());
						String[][] b = new String[c + 2][13];
						b = a.TimetableTodaySearch(textDate.getText());
						for (int i = 0; i < c + 2; i++) {
							model.addRow(new Object[] { b[i][0], b[i][1], b[i][2], b[i][3], b[i][4], b[i][5], b[i][6],
									b[i][7], b[i][8], b[i][9], b[i][10], b[i][11], b[i][12] });
						}
					} catch (Exception i) {
						JOptionPane.showMessageDialog(frame, "日期不正確");
					}
					if (model.getRowCount() == 0) {
						JOptionPane.showMessageDialog(frame, "無此日期資訊");
					}
				}
			}
		});
		btnSearcht.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSearcht.setBounds(244, 323, 96, 23);
		frame.getContentPane().add(btnSearcht);

		JButton btnSearchd = new JButton("SearchD");
		btnSearchd.setToolTipText("Discount search");
		btnSearchd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(textDate.getText().equals("")) && !(textDepart.getText().equals(""))
						&& !(textArrival.getText().equals("")) && !(cmbFrom.getSelectedItem().equals("From"))
						&& !(cmbTo.getSelectedItem().equals("To"))) {
					try {
						DefaultTableModel model = (DefaultTableModel) table_1.getModel();
						model.setRowCount(0);
						TicketSearch a = new TicketSearch();
						String[][] b;
						b = a.DiscountedTrain(textDate.getText(), textDepart.getText(), textArrival.getText(),
								a.NameToStationID((String) cmbFrom.getSelectedItem()),
								a.NameToStationID((String) cmbTo.getSelectedItem()));
						if (b[0][0].equals("0")) {
							JOptionPane.showMessageDialog(frame, "無優惠車次");
						} else {
							for (int i = 0; i < Integer.parseInt(b[0][7]); i++) {
								model.addRow(
										new Object[] { b[i][0], b[i][1], b[i][3], b[i][6], b[i][2], b[i][4], b[i][5] });
							}
						}
					} catch (Exception i) {
						JOptionPane.showMessageDialog(frame, "資訊不正確");
					}
				} else {
					JOptionPane.showMessageDialog(frame, "未輸入完整");
				}
			}
		});
		btnSearchd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSearchd.setBounds(340, 560, 96, 23);
		frame.getContentPane().add(btnSearchd);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONArray arr = JSONUtils.getJSONArrayFromFile("/booking.json");
				try {
					TicketCancel a = new TicketCancel();
					a.Cancel(textCode.getText(), textUID.getText());
					JOptionPane.showMessageDialog(frame, "退票成功，已取消您的訂位紀錄");
				} catch (Ticket_Exception c) {
					JOptionPane.showMessageDialog(frame, c.getMessage());
				} catch (IOException i) {
					JOptionPane.showMessageDialog(frame, i.getMessage());
				}
			}
		});
		btnCancel.setToolTipText("Cancel ticket");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCancel.setBounds(296, 279, 85, 23);
		frame.getContentPane().add(btnCancel);

		JButton btnChange = new JButton("Change");
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONArray arr = JSONUtils.getJSONArrayFromFile("/booking.json");
				try {
					TicketCancel a = new TicketCancel();
					a.Change(textCode.getText(), textUID.getText(), Integer.parseInt(textTicketCount.getText()));
					JOptionPane.showMessageDialog(frame,
							"修改成功，已改成" + (arr.getJSONObject(a.Ticket_index(textCode.getText(), textUID.getText()))
									.getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats").length()
									- Integer.parseInt(textTicketCount.getText())) + "張");
				} catch (Ticket_Exception c) {
					JOptionPane.showMessageDialog(frame, c.getMessage());
				} catch (IOException i) {
					JOptionPane.showMessageDialog(frame, i.getMessage());
				}
			}
		});
		btnChange.setToolTipText("Change amount");
		btnChange.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnChange.setBounds(164, 279, 106, 23);
		frame.getContentPane().add(btnChange);

		JButton btnGoback = new JButton("Go/Back");
		btnGoback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (g[0][0].equals("Back")) {
					if (GB == 0) {
						textDate.setText(g[0][11]);
						textTrainNo.setText(g[0][10]);
						textSeat.setText(g[0][17]);
						textFare.setText(g[0][23]);
						textDepart.setText(g[0][14]);
						textArrival.setText(g[0][15]);
						textDuration.setText(g[0][16]);
						textTicketCount.setText(g[0][22]);
						cmbFrom.setSelectedItem(g[0][4]);
						cmbTo.setSelectedItem(g[0][3]);
						GB = 1;
					} else {
						textDate.setText(g[0][2]);
						textTrainNo.setText(g[0][1]);
						textSeat.setText(g[0][8]);
						textFare.setText(g[0][20]);
						textDepart.setText(g[0][5]);
						textArrival.setText(g[0][6]);
						textDuration.setText(g[0][7]);
						textTicketCount.setText(g[0][19]);
						cmbFrom.setSelectedItem(g[0][3]);
						cmbTo.setSelectedItem(g[0][4]);
						GB = 0;
					}
				}
			}
		});
		btnGoback.setToolTipText("Go / Back switch");
		btnGoback.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnGoback.setBounds(843, 175, 115, 23);
		frame.getContentPane().add(btnGoback);

		JLabel lblNewLabel_2_1_2 = new JLabel("YYYY/MM/DD");
		lblNewLabel_2_1_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2_1_2.setBounds(340, 328, 85, 15);
		frame.getContentPane().add(lblNewLabel_2_1_2);

	}
}
