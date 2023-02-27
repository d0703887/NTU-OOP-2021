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

public class Booking {

	private JFrame frame;
	private JTextField textUID;
	private JTextField textDate;
	private JTable table;
	private JTable table_1;

	/**
	 * Launch the application.
	 */

	private JTextField textDateBack;
	private JTextField textCount;
	private JTextField textCountBack;

	Ticket a = new Ticket();
	org.json.JSONArray train = JSONUtils.getJSONArrayFromFile("/timeTable.json");
	Order b = new Order();
	int Re = 0;
	String GodepartureTime;
	Double goDiscount;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Booking window = new Booking();
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
	public Booking() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1370, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBounds(10, 21, 1336, 75);
		frame.getContentPane().add(panel);

		JLabel lblNewLabel = new JLabel("HSR Booking System");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Car Type");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(56, 132, 74, 15);
		frame.getContentPane().add(lblNewLabel_1);

		JRadioButton rdbtnStandard = new JRadioButton("Standard");
		rdbtnStandard.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnStandard.setBounds(56, 162, 105, 23);
		frame.getContentPane().add(rdbtnStandard);

		JRadioButton rdbtnBusiness = new JRadioButton("Business");
		rdbtnBusiness.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnBusiness.setBounds(163, 162, 105, 23);
		frame.getContentPane().add(rdbtnBusiness);

		JLabel lblNewLabel_1_1 = new JLabel("Ticket Type");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_1.setBounds(56, 215, 105, 15);
		frame.getContentPane().add(lblNewLabel_1_1);

		JRadioButton rdbtnChild = new JRadioButton("Child");
		rdbtnChild.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnChild.setBounds(163, 249, 105, 23);
		frame.getContentPane().add(rdbtnChild);

		JRadioButton rdbtnAdult = new JRadioButton("Adult");
		rdbtnAdult.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnAdult.setBounds(56, 249, 105, 23);
		frame.getContentPane().add(rdbtnAdult);

		JRadioButton rdbtnLove = new JRadioButton("Love");
		rdbtnLove.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnLove.setBounds(270, 249, 105, 23);
		frame.getContentPane().add(rdbtnLove);

		JRadioButton rdbtnAge = new JRadioButton("Age");
		rdbtnAge.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnAge.setBounds(56, 289, 105, 23);
		frame.getContentPane().add(rdbtnAge);

		JRadioButton rdbtnUniversity = new JRadioButton("University");
		rdbtnUniversity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnUniversity.setBounds(163, 289, 105, 23);
		frame.getContentPane().add(rdbtnUniversity);

		JComboBox cmbFrom = new JComboBox();
		cmbFrom.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cmbFrom.setModel(new DefaultComboBoxModel(new String[] { "From", "Nangang", "Taipei", "Banqiao", "Taoyuan",
				"Hsinchu", "Miaoli", "Taichung", "Changhua", "Yunlin", "Chiayi", "Tainan", "Zuoying" }));
		cmbFrom.setBounds(56, 363, 149, 23);
		frame.getContentPane().add(cmbFrom);

		JComboBox cmbTo = new JComboBox();
		cmbTo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cmbTo.setModel(new DefaultComboBoxModel(new String[] { "To", "Nangang", "Taipei", "Banqiao", "Taoyuan",
				"Hsinchu", "Miaoli", "Taichung", "Changhua", "Yunlin", "Chiayi", "Tainan", "Zuoying" }));
		cmbTo.setBounds(226, 363, 149, 23);
		frame.getContentPane().add(cmbTo);

		JLabel lblNewLabel_1_1_1 = new JLabel("Station");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_1_1.setBounds(56, 338, 105, 15);
		frame.getContentPane().add(lblNewLabel_1_1_1);

		JLabel lblNewLabel_2 = new JLabel("UID");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(56, 435, 47, 15);
		frame.getContentPane().add(lblNewLabel_2);

		textUID = new JTextField();
		textUID.setBounds(109, 435, 96, 21);
		frame.getContentPane().add(textUID);
		textUID.setColumns(10);

		JLabel lblNewLabel_1_2 = new JLabel("Train Info");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_2.setBounds(469, 132, 105, 15);
		frame.getContentPane().add(lblNewLabel_1_2);

		JLabel lblNewLabel_2_1 = new JLabel("Date");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_1.setBounds(56, 472, 47, 15);
		frame.getContentPane().add(lblNewLabel_2_1);

		textDate = new JTextField();
		textDate.setColumns(10);
		textDate.setBounds(109, 472, 96, 21);
		frame.getContentPane().add(textDate);

		JLabel lblNewLabel_1_1_2 = new JLabel("Seat Preference");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_1_2.setBounds(56, 547, 138, 15);
		frame.getContentPane().add(lblNewLabel_1_1_2);

		JRadioButton rdbtnNone = new JRadioButton("none");
		rdbtnNone.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnNone.setBounds(56, 568, 105, 23);
		frame.getContentPane().add(rdbtnNone);

		JRadioButton rdbtnWindow = new JRadioButton("Window");
		rdbtnWindow.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnWindow.setBounds(163, 568, 105, 23);
		frame.getContentPane().add(rdbtnWindow);

		JRadioButton rdbtnAisle = new JRadioButton("Aisle");
		rdbtnAisle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnAisle.setBounds(270, 568, 105, 23);
		frame.getContentPane().add(rdbtnAisle);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(422, 132, 37, 488);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(56, 199, 342, 27);
		frame.getContentPane().add(separator_1);

		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(56, 326, 342, 27);
		frame.getContentPane().add(separator_1_1);

		JSeparator separator_1_2 = new JSeparator();
		separator_1_2.setBounds(56, 411, 342, 27);
		frame.getContentPane().add(separator_1_2);

		JSeparator separator_1_3 = new JSeparator();
		separator_1_3.setBounds(56, 535, 342, 27);
		frame.getContentPane().add(separator_1_3);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(458, 162, 888, 276);
		frame.getContentPane().add(scrollPane);

		JSeparator separator_1_3_1 = new JSeparator();
		separator_1_3_1.setBounds(458, 460, 888, 27);
		frame.getContentPane().add(separator_1_3_1);

		JLabel lblNewLabel_1_2_1 = new JLabel("Ticket Details");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1_2_1.setBounds(469, 472, 138, 15);
		frame.getContentPane().add(lblNewLabel_1_2_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(458, 493, 898, 127);
		frame.getContentPane().add(scrollPane_1);

		table_1 = new JTable();
		scrollPane_1.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Trip", "Date", "Train No.", "From",
				"To", "Departure", "Arrival", "TicketType", "Seats", "Fare" }));
		table_1.getColumnModel().getColumn(0).setPreferredWidth(15);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(5).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(6).setPreferredWidth(30);
		table_1.getColumnModel().getColumn(7).setPreferredWidth(35);
		table_1.getColumnModel().getColumn(8).setPreferredWidth(35);
		table_1.getColumnModel().getColumn(9).setPreferredWidth(20);

		JRadioButton rdbtnReturn = new JRadioButton("Return");
		rdbtnReturn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnReturn.setBounds(281, 431, 74, 23);
		frame.getContentPane().add(rdbtnReturn);

		JLabel lblNewLabel_2_1_1 = new JLabel("Date");
		lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_1_1.setBounds(226, 472, 47, 15);
		frame.getContentPane().add(lblNewLabel_2_1_1);

		textDateBack = new JTextField();
		textDateBack.setColumns(10);
		textDateBack.setBounds(279, 472, 96, 21);
		frame.getContentPane().add(textDateBack);

		JLabel lblNewLabel_2_1_2 = new JLabel("Count");
		lblNewLabel_2_1_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_1_2.setBounds(56, 510, 47, 15);
		frame.getContentPane().add(lblNewLabel_2_1_2);

		JLabel lblNewLabel_2_1_3 = new JLabel("Count");
		lblNewLabel_2_1_3.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_1_3.setBounds(226, 510, 47, 15);
		frame.getContentPane().add(lblNewLabel_2_1_3);

		textCount = new JTextField();
		textCount.setColumns(10);
		textCount.setBounds(109, 504, 96, 21);
		frame.getContentPane().add(textCount);

		textCountBack = new JTextField();
		textCountBack.setColumns(10);
		textCountBack.setBounds(279, 504, 96, 21);
		frame.getContentPane().add(textCountBack);

		JButton btnSearch = new JButton("Search");
		btnSearch.setToolTipText("Search Train");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((rdbtnAdult.isSelected() || rdbtnChild.isSelected() || rdbtnLove.isSelected()
						|| rdbtnAge.isSelected() || rdbtnUniversity.isSelected())
						&& (rdbtnStandard.isSelected() || rdbtnBusiness.isSelected())
						&& (rdbtnNone.isSelected() || rdbtnAisle.isSelected()
								|| rdbtnWindow.isSelected() && !(textDate.getText().equals(""))
										&& !(textCount.getText().equals("")) && !(textUID.getText().equals(""))
										&& !(cmbFrom.getSelectedItem().equals("From"))
										&& !(cmbTo.getSelectedItem().equals("To")))) {
					if ((rdbtnReturn.isSelected() && !(textDateBack.getText().equals(""))
							&& !(textCountBack.getText().equals("")))
							|| (!rdbtnReturn.isSelected() && textDateBack.getText().equals("")
									&& textCountBack.getText().equals(""))) {
						Re = 0;
						if (rdbtnAdult.isSelected()) {
							a.setTicketType("一般");
						} else if (rdbtnChild.isSelected()) {
							a.setTicketType("小孩");
						} else if (rdbtnLove.isSelected()) {
							a.setTicketType("愛心");
						} else if (rdbtnAge.isSelected()) {
							a.setTicketType("敬老");
						} else if (rdbtnUniversity.isSelected()) {
							a.setTicketType("學生");
						}
						if (rdbtnStandard.isSelected()) {
							a.setCarType("標準");
						} else if (rdbtnBusiness.isSelected()) {
							a.setCarType("商務");
						}
						if (rdbtnNone.isSelected()) {
							a.setPrefer("無");
						} else if (rdbtnAisle.isSelected()) {
							a.setPrefer("走道");
						} else if (rdbtnWindow.isSelected()) {
							a.setPrefer("窗邊");
						}

						String[][] c;
						if (textCountBack.getText().equals("")) {
							Ticket aa = new Ticket(textDate.getText(), textDateBack.getText(),
									b.EnToCh((String) cmbFrom.getSelectedItem()),
									b.EnToCh((String) cmbTo.getSelectedItem()), a.getTicketType(), a.getPrefer(),
									Integer.parseInt(textCount.getText()), 0, textUID.getText(), a.getCarType());
							a = aa;
						} else {
							Ticket aa = new Ticket(textDate.getText(), textDateBack.getText(),
									b.EnToCh((String) cmbFrom.getSelectedItem()),
									b.EnToCh((String) cmbTo.getSelectedItem()), a.getTicketType(), a.getPrefer(),
									Integer.parseInt(textCount.getText()), Integer.parseInt(textCountBack.getText()),
									textUID.getText(), a.getCarType());
							a = aa;
						}
						if (Order.valid(a)) {
							c = b.array(a, "go");
							for (int i = 0; i < Integer.parseInt(c[0][5]); i++) {
								DefaultTableModel model = (DefaultTableModel) table.getModel();
								model.addRow(new Object[] { null, c[i][0], c[i][1], c[i][2], c[i][3], c[i][4] });
							}
						} else {
							JOptionPane.showMessageDialog(frame, "失敗, " + a.getGoDate() + " 尚未能預約");
						}
					} else {
						JOptionPane.showMessageDialog(frame, "回程資料不完整");
					}

				} else {
					JOptionPane.showMessageDialog(frame, "訂票資訊不正確");
				}
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSearch.setBounds(56, 597, 85, 23);
		frame.getContentPane().add(btnSearch);

		JButton btnReset = new JButton("Reset");
		btnReset.setToolTipText("Reset System");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ticket aaa = new Ticket();
				a = aaa;
				rdbtnAdult.setSelected(false);
				rdbtnChild.setSelected(false);
				rdbtnLove.setSelected(false);
				rdbtnAge.setSelected(false);
				rdbtnUniversity.setSelected(false);
				rdbtnStandard.setSelected(false);
				rdbtnBusiness.setSelected(false);
				rdbtnNone.setSelected(false);
				rdbtnAisle.setSelected(false);
				rdbtnWindow.setSelected(false);
				rdbtnReturn.setSelected(false);
				cmbFrom.setSelectedItem("From");
				cmbTo.setSelectedItem("To");
				textUID.setText(null);
				textDate.setText(null);
				textDateBack.setText(null);
				textCount.setText(null);
				textCountBack.setText(null);
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0);
				DefaultTableModel model_1 = (DefaultTableModel) table_1.getModel();
				model_1.setRowCount(0);
			}
		});

		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnReset.setBounds(163, 597, 85, 23);
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
		btnExit.setBounds(270, 597, 85, 23);
		frame.getContentPane().add(btnExit);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				DefaultTableModel model_1 = (DefaultTableModel) table_1.getModel();
				String d = null;
				for (int x = 0; x < train.length(); x++) {
					if (table.getValueAt(i, 1).equals(train.getJSONObject(x).getJSONObject("GeneralTimetable")
							.getJSONObject("GeneralTrainInfo").getString("TrainNo"))) {
						if (train.getJSONObject(x).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo")
								.getInt("Direction") == 0) {
							d = "南下";
						} else {
							d = "北上";
						}
					}
				}
				if (Re == 0) {
					a.setGoTrainNo((String) table.getValueAt(i, 1));
					GodepartureTime = (String) table.getValueAt(i, 3);
					if (((String) table.getValueAt(i, 2)).equals("")) {
						a.setGoDiscount(1.0);
					} else {
						a.setGoDiscount(Double.valueOf((String) table.getValueAt(i, 2)));
					}
					a.setGoprice(Double.valueOf(Order.price(a, "go")));
					double p = a.getGoprice();
					if (!rdbtnReturn.isSelected()) {
						String[][] s = Order.seat(a);
						String seat = s[0][0];
						if (Integer.parseInt(textCount.getText()) > 1) {
							for (int j = 1; j < Integer.parseInt(textCount.getText()); j++) {
								seat = seat + " / " + s[0][j];
							}
						}
						a.setGoSeats(s[0]);
						a.setPrice(p);
						model_1.addRow(new Object[] { d, textDate.getText(), table.getValueAt(i, 1),
								b.EnToCh((String) cmbFrom.getSelectedItem()),
								b.EnToCh((String) cmbTo.getSelectedItem()), table.getValueAt(i, 3),
								table.getValueAt(i, 4), a.getTicketType(), seat, p });
					} else {
						model_1.addRow(new Object[] { d, textDate.getText(), table.getValueAt(i, 1),
								b.EnToCh((String) cmbFrom.getSelectedItem()),
								b.EnToCh((String) cmbTo.getSelectedItem()), table.getValueAt(i, 3),
								table.getValueAt(i, 4), a.getTicketType(), "", p });
					}
				} else {
					a.setBackTrainNo((String) table.getValueAt(i, 1));
					if (((String) table.getValueAt(i, 2)).equals("")) {
						a.setBackDiscount(1.0);
					} else {
						a.setBackDiscount(Double.valueOf((String) table.getValueAt(i, 2)));
					}
					a.setBackprice(Double.valueOf(Order.price(a, "back")));
					double p = a.getBackprice();
					String[][] s = Order.seat(a);
					String seat = s[0][0];
					String seatBack = s[1][0];
					if (Integer.parseInt(textCount.getText()) > 1) {
						for (int j = 1; j < Integer.parseInt(textCount.getText()); j++) {
							seat = seat + " / " + s[0][j];
						}
					}
					if (Integer.parseInt(textCountBack.getText()) > 1) {
						for (int j = 1; j < Integer.parseInt(textCountBack.getText()); j++) {
							seatBack = seatBack + " / " + s[1][j];
						}
					}
					a.setGoSeats(s[0]);
					a.setBackSeats(s[1]);
					a.setPrice(a.getGoprice() + a.getBackprice());
					model_1.setValueAt(seat, 0, 8);
					model_1.addRow(new Object[] { d, textDateBack.getText(), table.getValueAt(i, 1),
							b.EnToCh((String) cmbTo.getSelectedItem()), b.EnToCh((String) cmbFrom.getSelectedItem()),
							table.getValueAt(i, 3), table.getValueAt(i, 4), a.getTicketType(), seatBack, p });
				}
				if (Re == 0) {
					if (rdbtnReturn.isSelected()) {
						model.setRowCount(0);
						String[][] c;
						c = b.array(a, "back");
						for (int j = 0; j < Integer.parseInt(c[0][5]); j++) {

							model.addRow(new Object[] { null, c[j][0], c[j][1], c[j][2], c[j][3], c[j][4] });
						}
						Re += 1;
					}
				}
			}
		});
		scrollPane.setViewportView(table);
		table.setFont(new Font("Tahoma", Font.BOLD, 16));
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Select", "Train No.", "Discount", "Departure", "Arrival", "Duration" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(15);

		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a.setExpireDate(Order.expire(a, GodepartureTime));
				a.setStart(Order.station(a.getStart()));
				a.setEnd(Order.station(a.getEnd()));
				a.setCode(Order.code());
				try {
					a.WriteTicketIntoBooking();
				} catch (Exception i) {
					JOptionPane.showMessageDialog(frame, i.getMessage());
				}
			}
		});
		btnConfirm.setToolTipText("Confirm booking");
		btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnConfirm.setBounds(1202, 630, 105, 23);
		frame.getContentPane().add(btnConfirm);

		JLabel lblNewLabel_2_1_4 = new JLabel("YYYY/M/D");
		lblNewLabel_2_1_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2_1_4.setBounds(43, 488, 77, 15);
		frame.getContentPane().add(lblNewLabel_2_1_4);

		JLabel lblNewLabel_2_1_4_1 = new JLabel("YYYY/M/D");
		lblNewLabel_2_1_4_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2_1_4_1.setBounds(214, 488, 77, 15);
		frame.getContentPane().add(lblNewLabel_2_1_4_1);

	}
}
