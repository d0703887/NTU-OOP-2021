package com.tcg.json;

import org.json.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class TicketSearch {

	JSONArray ary = JSONUtils.getJSONArrayFromFile("/booking.json");
	JSONArray sta = JSONUtils.getJSONArrayFromFile("/station.json");
	JSONArray timetable = JSONUtils.getJSONArrayFromFile("/timeTable.json");
	JSONObject earlydiscount = JSONUtils.getJSONObjectFromFile("/earlyDiscountNumber.json");
	JSONObject universitydiscount = JSONUtils.getJSONObjectFromFile("/universityDiscount.json");

	// Convert stationID to its Name
	public String StationIDToName(String StationID) {
		for (int i = 0; i < sta.length(); i++) {
			if (StationID.equals(sta.getJSONObject(i).get("StationID"))) {
				String Name = // sta.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw") + " / "
						sta.getJSONObject(i).getJSONObject("StationName").getString("En");
				return Name;
			}
		}
		return "Your StationID is wrong. \nPleas input it again.";
	}

	// Convert stationName to its ID
	public String NameToStationID(String StationName) {
		for (int i = 0; i < sta.length(); i++) {
			if (StationName.equals(sta.getJSONObject(i).getJSONObject("StationName").getString("En"))) {
				String ID = // sta.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw") + " / "
						sta.getJSONObject(i).getString("StationID");
				return ID;
			}
		}
		return "Your StationName is wrong. \nPleas input it again.";
	}

	// Search Ticket By Code
	// Ticket[0][0] is result
	public String[][] TicketSearchWithCode(String code, String uid) {
		String[][] Ticket = new String[1][30];
		int match = 0;
		int match2 = 0;
		int c = 0;
		for (int i = 0; i < ary.length(); i++) {
			if (uid.equals(ary.getJSONObject(i).getString("uid"))) {
				match += 1;
			}
		}
		// match = 0 代表 uid error
		if (match == 0) {
			for (int j = 0; j < ary.length(); j++) {
				if (code.equals(ary.getJSONObject(j).getString("code"))) {
					match2 += 1;
				}
			}
			// match = 0 帶表 code error, uid error + code error = 無訂票紀錄
			if (match2 == 0) {
				Ticket[0][0] = "無此訂票紀錄";
				return Ticket;
			} else {
				// match2 !=0 but match = 0 代表 only Uid error
				Ticket[0][0] = "您輸入的身份識別號碼有誤，請重新輸入";
				return Ticket;
			}
		} else {
			// match != 0, uid correct
			for (int j = 0; j < ary.length(); j++) {
				if (code.equals(ary.getJSONObject(j).getString("code"))) {
					match2 += 1;
					c = j;
				}
			}
			// match != 0 but match2 = 0 代表 only code error
			if (match2 == 0) {
				Ticket[0][0] = "您輸入的訂位代號有誤，請重新輸入";
				return Ticket;
			} else {
				// Both correct
				// 開始存入資料
				Ticket[0][0] = "Outbound Train Details: \n";
				Ticket[0][1] = // "TrainNo : "
						ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getString("TrainNo") + "\n";
				Ticket[0][2] = // "Date : "
						ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getString("date") + "\n";
				Ticket[0][3] = // "Start station : "
						StationIDToName(
								ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getString("start"));
				Ticket[0][4] = // "Arrival station : "
						StationIDToName(
								ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getString("end"));
				Ticket[0][5] = // "Departure time : "
						ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getString("departure time")
								+ "\n";
				Ticket[0][6] = // "Arrival time : "
						ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getString("arrival time")
								+ "\n";
				Ticket[0][7] = // "Driving time : "
						ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getString("Driving time")
								+ "\n";
				Ticket[0][8] = // "Seats : "
						ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats")
								.getString(0);
				if (ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats")
						.length() > 1) {
					for (int x = 1; x < ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0)
							.getJSONArray("seats").length(); x++) {
						Ticket[0][8] = Ticket[0][8] + "-" + ary.getJSONObject(c).getJSONArray("ticketInfo")
								.getJSONObject(0).getJSONArray("seats").getString(x);
					}
				}
				Ticket[0][18] = // "Car type : "
						ary.getJSONObject(c).getString("carType") + "\n";
				Ticket[0][24] = // "Ticket type : "
						ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getString("ticketsType")
								+ "\n";
				Ticket[0][19] = // "Ticket Count : "
						ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getInt("ticketsCount") + "\n";
				Ticket[0][20] = // "Fare : "
						String.valueOf(
								ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(0).getInt("price"));
				if (ary.getJSONObject(c).getJSONArray("ticketInfo").length() > 1) {
					Ticket[0][0] = "Back";
					Ticket[0][9] = "Inbound Train Details: \n";
					Ticket[0][10] = // "TrainNo : "
							ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getString("TrainNo")
									+ "\n";
					Ticket[0][11] = // "Date : "
							ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getString("date") + "\n";
					Ticket[0][12] = // "Start station : "
							StationIDToName(ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1)
									.getString("start"));
					Ticket[0][13] = // "Arrival station : "
							StationIDToName(
									ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getString("end"));
					Ticket[0][14] = // "Departure time : "
							ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getString("departure time")
									+ "\n";
					Ticket[0][15] = // "Arrival time : "
							ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getString("arrival time")
									+ "\n";
					Ticket[0][16] = // "Driving time : "
							ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getString("Driving time")
									+ "\n";
					Ticket[0][17] = // "Seats : "
							ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getJSONArray("seats")
									.getString(0);
					if (ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getJSONArray("seats")
							.length() > 1) {
						for (int x = 1; x < ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1)
								.getJSONArray("seats").length(); x++) {
							Ticket[0][17] = Ticket[0][17] + "-" + ary.getJSONObject(c).getJSONArray("ticketInfo")
									.getJSONObject(1).getJSONArray("seats").getString(x);
						}
					}
					Ticket[0][21] = // "Car type : "
							ary.getJSONObject(c).getString("carType") + "\n";
					Ticket[0][22] = // "Ticket Count : "
							ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getInt("ticketsCount")
									+ "\n";
					Ticket[0][23] = // "Fare : "
							String.valueOf(
									ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getInt("price"));
					Ticket[0][25] = // "Ticket type : "
							ary.getJSONObject(c).getJSONArray("ticketInfo").getJSONObject(1).getString("ticketsType")
									+ "\n";

					return Ticket;
				}
				return Ticket;
			}
		}
	}

	// Search code
	// "查無記錄" is at [0][0]
	public String[][] CodeSearch(String uid, String start, String end, String date, String TrainNo) {
		for (int i = 0; i < ary.length(); i++) {
			if (uid.equals(ary.getJSONObject(i).getString("uid"))
					&& start.equals(ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).getString("start"))
					// 少一個getJSONObject
					&& end.equals(ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).getString("end"))
					&& date.equals(ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).getString("date"))
					&& TrainNo.equals(
							ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).getString("TrainNo"))) {

				String[][] GoTicket = new String[1][20];

				GoTicket[0][0] = // "Code : " +
						ary.getJSONObject(i).getString("code") + "\n";
				GoTicket[0][1] = // "Trading Status: " +
						ary.getJSONObject(i).getString("trading status") + "\n";
				GoTicket[0][2] = // "UID: " +
						uid + "\n";
				GoTicket[0][3] = // "Phone: " +
						ary.getJSONObject(i).getString("Phone") + "\n";
				GoTicket[0][4] = // "Go / Back: Go";
						GoTicket[0][5] = // "TrainNo: " +
								TrainNo;
				GoTicket[0][6] = // "Date: " +
						date;
				GoTicket[0][7] = // "Start Station: " +
						StationIDToName(start) + "\n";
				GoTicket[0][8] = // "Departure Time: "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).get("departure time") + "\n";
				GoTicket[0][9] = // "Arrival Station: " + StationIDToName(end) + "\n";
						GoTicket[0][10] = // "Arrival Time: "
								ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).get("arrival time")
										+ "\n";
				GoTicket[0][11] = // "Ticket Price: "
						String.valueOf(
								ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).getInt("price"));
				GoTicket[0][12] = // "Seats : "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats")
								.getString(0);
				if (ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats")
						.length() > 1) {
					for (int x = 1; x < ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0)
							.getJSONArray("seats").length(); x++) {
						GoTicket[0][12] = GoTicket[0][12] + "-" + ary.getJSONObject(i).getJSONArray("ticketInfo")
								.getJSONObject(0).getJSONArray("seats").getString(x);
					}
				}
				GoTicket[0][13] = // "Ticket Type: "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).get("ticketsType") + "\n";
				GoTicket[0][14] = // "Ticket Count: "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).get("ticketsCount") + "\n";
				GoTicket[0][15] = // "Total Price(go + back): " +
						String.valueOf(ary.getJSONObject(i).getInt("payment"));
				GoTicket[0][16] = // "Driving time : "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(0).getString("Driving time")
								+ "\n";
				GoTicket[0][17] = // "Car Type: "
						ary.getJSONObject(i).getString("carType") + "\n";

				return GoTicket;
			} else if (ary.getJSONObject(i).getJSONArray("ticketInfo").length() > 1
					&& uid.equals(ary.getJSONObject(i).getString("uid"))
					&& start.equals(ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).getString("start"))
					// 少一個getJSONObject
					&& end.equals(ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).getString("end"))
					&& date.equals(ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).getString("date"))
					&& TrainNo.equals(
							ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).getString("TrainNo"))) {

				String[][] BackTicket = new String[1][20];

				BackTicket[0][0] = // "Code : " +
						ary.getJSONObject(i).getString("code") + "\n";
				BackTicket[0][1] = // "Trading Status: " +
						ary.getJSONObject(i).getString("trading status") + "\n";
				BackTicket[0][2] = // "UID: " +
						uid + "\n";
				BackTicket[0][3] = // "Phone: " +
						ary.getJSONObject(i).getString("Phone") + "\n";
				BackTicket[0][4] = // "Go / Back: Back";
						BackTicket[0][5] = // "TrainNo: " +
								TrainNo;
				BackTicket[0][6] = // "Date: " +
						date;
				BackTicket[0][7] = // "Start Station: " +
						StationIDToName(start);
				BackTicket[0][8] = // "Departure Time: "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).get("departure time") + "\n";
				BackTicket[0][9] = // "Arrival Station: " +
						StationIDToName(end);
				BackTicket[0][10] = // "Arrival Time: "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).get("arrival time") + "\n";
				BackTicket[0][11] = // "Ticket Price: "
						String.valueOf(
								ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).getInt("price"));
				BackTicket[0][12] = // "Seats : "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).getJSONArray("seats")
								.getString(0);
				if (ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).getJSONArray("seats")
						.length() > 1) {
					for (int x = 1; x < ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1)
							.getJSONArray("seats").length(); x++) {
						BackTicket[0][12] = BackTicket[0][12] + "-" + ary.getJSONObject(i).getJSONArray("ticketInfo")
								.getJSONObject(1).getJSONArray("seats").getString(x);
					}
				}
				BackTicket[0][13] = // "Ticket Type: "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).get("ticketsType") + "\n";
				BackTicket[0][14] = // "Ticket Count: "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).get("ticketsCount") + "\n";
				BackTicket[0][15] = "Total Price(go + back): " + ary.getJSONObject(i).getInt("payment");
				BackTicket[0][16] = // "Driving time : "
						ary.getJSONObject(i).getJSONArray("ticketInfo").getJSONObject(1).getString("Driving time")
								+ "\n";
				BackTicket[0][17] = // "Car Type: "
						ary.getJSONObject(i).getString("carType") + "\n";

				return BackTicket;
			}
		}

		String[][] NoTicket = new String[1][1];
		NoTicket[0][0] = "查無記錄";
		return NoTicket;
	}

	public String DateToDay(String date) {
		SimpleDateFormat yo = new SimpleDateFormat("yyyy/MM/dd");
		String[] day = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		Date dt = new Date();
		Calendar cal = Calendar.getInstance();
		try {
			dt = yo.parse(date);
			cal.setTime(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return day[w];
	}

	public Number intToNum(int intyee) {
		Integer Intyee = new Integer(intyee);
		Number Numyee = (Number) Intyee;
		return Numyee;
	}

	public int Number(String date) {
		int NumberOfSouthTrain = 0;
		int NumberOfNorthTrain = 0;
		String day = DateToDay(date);
		for (int i = 0; i < timetable.length(); i++) {
			if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay")
					.getNumber(day)).equals(intToNum(1))) {
				if (timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo")
						.getNumber("Direction").equals(intToNum(0))) {
					NumberOfSouthTrain += 1;
				}

				else if (timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo")
						.getNumber("Direction").equals(intToNum(1))) {
					NumberOfNorthTrain += 1;
				}
			}
		}
		return NumberOfSouthTrain + NumberOfNorthTrain;
	}

	public static boolean compareTime(String first, String next) {
		if (Integer.valueOf(first.substring(0, 2)) - Integer.valueOf(next.substring(0, 2)) > 0) {
			return true;
		} else if (Integer.valueOf(first.substring(0, 2)) - Integer.valueOf(next.substring(0, 2)) < 0) {
			return false;
		} else {
			return true;
		}
	}

	public static String[][] sort(String[][] list, String x, String x2, String x3, String x4, String x5, String x6,
			String x7, String x8, String x9, String x10, String x11, String x12, String x13, int length) {

		if (length == 0) {
			list[0][0] = x;
			list[0][1] = x2;
			list[0][2] = x3;
			list[0][3] = x4;
			list[0][4] = x5;
			list[0][5] = x6;
			list[0][6] = x7;
			list[0][7] = x8;
			list[0][8] = x9;
			list[0][9] = x10;
			list[0][10] = x11;
			list[0][11] = x12;
			list[0][12] = x13;

		} else {
			// 如果比第0項小，直接丟到第0項
			if (compareTime(list[0][7], x8)) {
				for (int i = length - 1; i >= 0; i--) {
					list[i + 1][0] = list[i][0];
					list[i + 1][1] = list[i][1];
					list[i + 1][2] = list[i][2];
					list[i + 1][3] = list[i][3];
					list[i + 1][4] = list[i][4];
					list[i + 1][5] = list[i][5];
					list[i + 1][6] = list[i][6];
					list[i + 1][7] = list[i][7];
					list[i + 1][8] = list[i][8];
					list[i + 1][9] = list[i][9];
					list[i + 1][10] = list[i][10];
					list[i + 1][11] = list[i][11];
					list[i + 1][12] = list[i][12];
				}
				list[0][0] = x;
				list[0][1] = x2;
				list[0][2] = x3;
				list[0][3] = x4;
				list[0][4] = x5;
				list[0][5] = x6;
				list[0][6] = x7;
				list[0][7] = x8;
				list[0][8] = x9;
				list[0][9] = x10;
				list[0][10] = x11;
				list[0][11] = x12;
				list[0][12] = x13;
			}
			// 插進正確的位置
			else {
				boolean check = false;
				for (int i = 0; i < length - 1; i++) {
					// 找到正確的位置之後，插入
					if (compareTime(list[i][7], x8) == false && compareTime(list[i + 1][7], x8)) {
						for (int j = length - 1; j >= i + 1; j--) {
							list[j + 1][0] = list[j][0];
							list[j + 1][1] = list[j][1];
							list[j + 1][2] = list[j][2];
							list[j + 1][3] = list[j][3];
							list[j + 1][4] = list[j][4];
							list[j + 1][5] = list[j][5];
							list[j + 1][6] = list[j][6];
							list[j + 1][7] = list[j][7];
							list[j + 1][8] = list[j][8];
							list[j + 1][9] = list[j][9];
							list[j + 1][10] = list[j][10];
							list[j + 1][11] = list[j][11];
							list[j + 1][12] = list[j][12];
						}
						list[i + 1][0] = x;
						list[i + 1][1] = x2;
						list[i + 1][2] = x3;
						list[i + 1][3] = x4;
						list[i + 1][4] = x5;
						list[i + 1][5] = x6;
						list[i + 1][6] = x7;
						list[i + 1][7] = x8;
						list[i + 1][8] = x9;
						list[i + 1][9] = x10;
						list[i + 1][10] = x11;
						list[i + 1][11] = x12;
						list[i + 1][12] = x13;
						check = true;
						break;
					} else {
					}
				}
				// 如果都沒找到正確位置，就插在最後面
				if (check == true) {

				} else {
					list[length][0] = x;
					list[length][1] = x2;
					list[length][2] = x3;
					list[length][3] = x4;
					list[length][4] = x5;
					list[length][5] = x6;
					list[length][6] = x7;
					list[length][7] = x8;
					list[length][8] = x9;
					list[length][9] = x10;
					list[length][10] = x11;
					list[length][11] = x12;
					list[length][12] = x13;
				}
			}
		}
		return list;
	}

	// All train in that date
	public String[][] TimetableTodaySearch(String date) {
		int NumberOfSouthTrain = 0;
		int NumberOfNorthTrain = 0;
		String day = DateToDay(date);
		for (int i = 0; i < timetable.length(); i++) {
			if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay")
					.getNumber(day)).equals(intToNum(1))) {
				if (timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo")
						.getNumber("Direction").equals(intToNum(0))) {
					NumberOfSouthTrain += 1;
				}

				else if (timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo")
						.getNumber("Direction").equals(intToNum(1))) {
					NumberOfNorthTrain += 1;
				}
			}
		}

		String[][] SouthStopInfo = new String[NumberOfSouthTrain + 1][13];
		// SouthStopInfo[0][0] = "南下車班: ";
		String[][] NorthStopInfo = new String[NumberOfNorthTrain + 1][13];
		// NorthStopInfo[0][0] = "北上車班: ";
		int arraynumber = 0;
		int arraynumber1 = 0;
		for (int i = 0; i < timetable.length(); i++) {
			if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay")
					.getNumber(day)).equals(intToNum(1))) {
				// [][0] is TrainNo,[1] is Nangang,[2] is Taipei,[3] is Banciao,... from north
				// to south
				if (timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo")
						.getNumber("Direction").equals(intToNum(0))) {
					SouthStopInfo[arraynumber][0] = // "TrainNo: " +
							timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONObject("GeneralTrainInfo").getString("TrainNo");
					for (int j = 0; j < timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
							.getJSONArray("StopTimes").length(); j++) {

						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("0990")) {
							SouthStopInfo[arraynumber][1] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("DepartureTime");
						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1000")) {
							SouthStopInfo[arraynumber][2] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1010")) {
							SouthStopInfo[arraynumber][3] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1020")) {
							SouthStopInfo[arraynumber][4] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1030")) {
							SouthStopInfo[arraynumber][5] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1035")) {
							SouthStopInfo[arraynumber][6] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1040")) {
							SouthStopInfo[arraynumber][7] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1043")) {
							SouthStopInfo[arraynumber][8] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1047")) {
							SouthStopInfo[arraynumber][9] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1050")) {
							SouthStopInfo[arraynumber][10] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1060")) {
							SouthStopInfo[arraynumber][11] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1070")) {
							SouthStopInfo[arraynumber][12] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}

					}
					String x = SouthStopInfo[arraynumber][0];
					String x1 = SouthStopInfo[arraynumber][1];
					String x2 = SouthStopInfo[arraynumber][2];
					String x3 = SouthStopInfo[arraynumber][3];
					String x4 = SouthStopInfo[arraynumber][4];
					String x5 = SouthStopInfo[arraynumber][5];
					String x6 = SouthStopInfo[arraynumber][6];
					String x7 = SouthStopInfo[arraynumber][7];
					String x8 = SouthStopInfo[arraynumber][8];
					String x9 = SouthStopInfo[arraynumber][9];
					String x10 = SouthStopInfo[arraynumber][10];
					String x11 = SouthStopInfo[arraynumber][11];
					String x12 = SouthStopInfo[arraynumber][12];
					if (arraynumber != 0) {
						sort(SouthStopInfo, x, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, arraynumber);
					}
					for (int y = 0; y <= arraynumber; y++) {
						// System.out.print(SouthStopInfo[y][7] + " / ");
						// System.out.println();
					}
					arraynumber += 1;
				}
				// [][0] is TrainNo,[1] is Nangang,[2] is Taipei,[3] is Banciao,... from north
				// to south
				if (timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo")
						.getNumber("Direction").equals(intToNum(1))) {
					NorthStopInfo[arraynumber1][0] = // "TrainNo: " +
							timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONObject("GeneralTrainInfo").getString("TrainNo");
					for (int j = 0; j < timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
							.getJSONArray("StopTimes").length(); j++) {

						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("0990")) {

							NorthStopInfo[arraynumber1][1] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1000")) {
							NorthStopInfo[arraynumber1][2] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1010")) {
							NorthStopInfo[arraynumber1][3] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1020")) {
							NorthStopInfo[arraynumber1][4] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1030")) {
							NorthStopInfo[arraynumber1][5] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1035")) {
							NorthStopInfo[arraynumber1][6] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1040")) {
							NorthStopInfo[arraynumber1][7] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1043")) {
							NorthStopInfo[arraynumber1][8] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1047")) {
							NorthStopInfo[arraynumber1][9] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1050")) {
							NorthStopInfo[arraynumber1][10] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1060")) {
							NorthStopInfo[arraynumber1][11] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
						if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).getString("StationID")).equals("1070")) {
							NorthStopInfo[arraynumber1][12] = timetable.getJSONObject(i)
									.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j)
									.getString("DepartureTime");

						}
					}
					String x = NorthStopInfo[arraynumber1][0];
					String x1 = NorthStopInfo[arraynumber1][1];
					String x2 = NorthStopInfo[arraynumber1][2];
					String x3 = NorthStopInfo[arraynumber1][3];
					String x4 = NorthStopInfo[arraynumber1][4];
					String x5 = NorthStopInfo[arraynumber1][5];
					String x6 = NorthStopInfo[arraynumber1][6];
					String x7 = NorthStopInfo[arraynumber1][7];
					String x8 = NorthStopInfo[arraynumber1][8];
					String x9 = NorthStopInfo[arraynumber1][9];
					String x10 = NorthStopInfo[arraynumber1][10];
					String x11 = NorthStopInfo[arraynumber1][11];
					String x12 = NorthStopInfo[arraynumber1][12];
					if (arraynumber1 != 0) {
						sort(NorthStopInfo, x, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, arraynumber1);
					}
					arraynumber1 += 1;
				}
			}
		}

		String[][] StopInfo = new String[SouthStopInfo.length + NorthStopInfo.length][13];
		for (int s = 0; s < SouthStopInfo.length; s++) {
			for (int ss = 0; ss < SouthStopInfo[s].length; ss++) {
				StopInfo[s][ss] = SouthStopInfo[s][ss];
			}
		}
		for (int n = 0; n < NorthStopInfo.length; n++) {
			for (int nn = 0; nn < NorthStopInfo[n].length; nn++) {
				StopInfo[SouthStopInfo.length + n][nn] = NorthStopInfo[n][nn];
			}
		}
		return StopInfo;
	}

	public int TimeFromStringToInt(String time) {
		String formmer = time.substring(0, 2);
		String latter = time.substring(3);
		int IntTime = Integer.valueOf(formmer) * 60 + Integer.valueOf(latter);
		return IntTime;
	}

	public String[][] Copy2DArray(String[][] yee, int IncreaseLength) {
		String[][] newarray = new String[yee.length + IncreaseLength][];
		for (int i = 0; i < yee.length; i++) {
			newarray[i] = new String[yee[i].length];
			for (int j = 0; j < yee[i].length; j++) {
				newarray[i][j] = yee[i][j];
			}
		}
		return newarray;
	}

	public String CountDrivingTime(String BeginTime, String EndTime) {
		int inttime = TimeFromStringToInt(EndTime) - TimeFromStringToInt(BeginTime);
		if (inttime < 0) {
			inttime += 24 * 60;
		}
		String dt = inttime / 60 + "h" + inttime % 60 + "min";
		return dt;
	}

	public JSONObject findTrainNo(JSONArray database, String trainno) {
		for (int i = 0; i < database.length(); i++) {
			try {
				JSONObject want = database.getJSONObject(i).getJSONObject(trainno);
				return want;
			} catch (Exception e) {
			}
		}
		return null;
	}

	public String findUniversityDiscount(JSONObject database, String trainno, String day) {
		for (int i = 0; i < database.getJSONArray("DiscountTrains").length(); i++) {
			if (trainno.equals(database.getJSONArray("DiscountTrains").getJSONObject(i).getString("TrainNo"))) {
				if (database.getJSONArray("DiscountTrains").getJSONObject(i).getJSONObject("ServiceDayDiscount")
						.getNumber(day).doubleValue() != 1) {
					return "" + database.getJSONArray("DiscountTrains").getJSONObject(i)
							.getJSONObject("ServiceDayDiscount").getNumber(day);
				} else
					return null;
			}
		}
		return null;
	}

	public String WhichDiscount(JSONObject trainno, String day) {
		if (trainno.get(day) instanceof JSONArray) {
			for (int i = 0; i < trainno.getJSONArray(day).length(); i++) {
				if (trainno.getJSONArray(day).getJSONObject(i).getNumber("discount").doubleValue() == 0.65
						&& trainno.getJSONArray(day).getJSONObject(i).getNumber("tickets").intValue() > 0) {
					return "0.65";
				} else if (trainno.getJSONArray(day).getJSONObject(i).getNumber("discount").doubleValue() == 0.8
						&& trainno.getJSONArray(day).getJSONObject(i).getNumber("tickets").intValue() > 0) {
					return "0.8";
				} else if (trainno.getJSONArray(day).getJSONObject(i).getNumber("discount").doubleValue() == 0.9
						&& trainno.getJSONArray(day).getJSONObject(i).getNumber("tickets").intValue() > 0) {
					return "0.9";
				}
			}
		}
		return null;
	}

	// Start/Arrival station input is its codename not English/Chinese name.
	public String[][] DiscountedTrain(String date, String departureTime, String arrivalTime, String Startstation,
			String Arrivalstation) {
		String day = DateToDay(date);
		String answer[][] = new String[0][];
		int answerlength = 0;
		// 改形式:YYYY/MM/DD - YYYY/M/D
		Order a = new Order();
		String d = a.changeFormat(date);

		// 找全部的車子
		for (int i = 0; i < timetable.length(); i++) {
			// 判斷車子當日有沒有行駛
			if ((timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay")
					.getNumber(day)).equals(intToNum(1))) {
				// 判斷客人南下嗎
				if (Integer.valueOf(Startstation) < Integer.valueOf(Arrivalstation)) {
					// 判斷起訖站有沒有在行駛範圍內
					if (Integer.valueOf(Startstation) >= Integer
							.valueOf(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONObject("GeneralTrainInfo").getString("StartingStationID"))
							&& Integer.valueOf(Arrivalstation) <= Integer
									.valueOf(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
											.getJSONObject("GeneralTrainInfo").getString("EndingStationID"))) {
						// 尋找在第幾站是起始站/抵達站
						int BeginStationNumber = 0;
						int EndStationNumber = 0;
						for (int j = 0; j < timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
								.getJSONArray("StopTimes").length(); j++) {
							if (Startstation.equals(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("StationID"))) {
								BeginStationNumber = j;
								continue;
							}
							if (Arrivalstation.equals(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("StationID"))) {
								EndStationNumber = j;
								continue;
							}

						}
						boolean earlyOrNot = true;
						if (findTrainNo(earlydiscount.getJSONArray(d),
								timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
										.getJSONObject("GeneralTrainInfo").getString("TrainNo")) == null) {
							earlyOrNot = false;
						}
						if (earlyOrNot) {
							if (WhichDiscount(
									findTrainNo(earlydiscount.getJSONArray(d),
											timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
													.getJSONObject("GeneralTrainInfo").getString("TrainNo")),
									day) == null
									&& findUniversityDiscount(universitydiscount,
											timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
													.getJSONObject("GeneralTrainInfo").getString("TrainNo"),
											day) == null) {
								continue;
							}
						} else {
							if (findUniversityDiscount(universitydiscount,
									timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
											.getJSONObject("GeneralTrainInfo").getString("TrainNo"),
									day) == null) {
								continue;
							}
						}
						int dpt = 0;
						if (TimeFromStringToInt(
								timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
										.getJSONObject(EndStationNumber).getString("DepartureTime")) <= 60) {
							dpt = TimeFromStringToInt(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(EndStationNumber)
									.getString("DepartureTime")) + 24 * 60;
						} else {
							dpt = TimeFromStringToInt(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(EndStationNumber)
									.getString("DepartureTime"));
						}
						// 判斷能不能在客人時間搭到和達到
						if (TimeFromStringToInt(departureTime) < TimeFromStringToInt(
								timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
										.getJSONObject(BeginStationNumber).getString("DepartureTime"))
								&& TimeFromStringToInt(arrivalTime) > dpt) {

							answer = Copy2DArray(answer, 1);
							answer[answerlength] = new String[8];
							// TrainNo
							answer[answerlength][0] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONObject("GeneralTrainInfo").getString("TrainNo");
							// Begin Station(return its English name/Chinese name)
							answer[answerlength][1] = StationIDToName(Startstation);
							// Departure time
							answer[answerlength][2] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(BeginStationNumber)
									.getString("DepartureTime");
							// Arrival Station(return its English name/Chinese name)
							answer[answerlength][3] = StationIDToName(Arrivalstation);
							// Arrival time
							answer[answerlength][4] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(EndStationNumber)
									.getString("DepartureTime");
							// Driving time
							answer[answerlength][5] = CountDrivingTime(
									timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
											.getJSONArray("StopTimes").getJSONObject(BeginStationNumber)
											.getString("DepartureTime"),
									timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
											.getJSONArray("StopTimes").getJSONObject(EndStationNumber)
											.getString("DepartureTime"));
							// University Discount / Early Discount
							if (earlyOrNot) {
								answer[answerlength][6] = findUniversityDiscount(universitydiscount,
										timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
												.getJSONObject("GeneralTrainInfo").getString("TrainNo"),
										day)
										+ " / "
										+ WhichDiscount(findTrainNo(earlydiscount.getJSONArray(d),
												timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
														.getJSONObject("GeneralTrainInfo").getString("TrainNo")),
												day);
							} else {
								answer[answerlength][6] = findUniversityDiscount(universitydiscount,
										timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
												.getJSONObject("GeneralTrainInfo").getString("TrainNo"),
										day) + " / " + null;
							}

							answerlength += 1;
							answer[0][7] = String.valueOf(answerlength);
							continue;
						}
					}
				}
				// 判斷客人北上嗎
				else if (Integer.valueOf(Startstation) > Integer.valueOf(Arrivalstation)) {
					// 判斷起訖站有沒有在行駛範圍內
					if (Integer.valueOf(Startstation) < Integer
							.valueOf(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONObject("GeneralTrainInfo").getString("StartingStationID"))
							&& Integer.valueOf(Arrivalstation) > Integer
									.valueOf(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
											.getJSONObject("GeneralTrainInfo").getString("EndingStationID"))) {
						// 尋找在第幾站是起始站/抵達站
						int BeginStationNumber = 0;
						int EndStationNumber = 0;
						for (int j = 0; j < timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
								.getJSONArray("StopTimes").length(); j++) {
							if (Startstation.equals(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("StationID"))) {
								BeginStationNumber = j;
								continue;
							}
							if (Arrivalstation.equals(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(j).getString("StationID"))) {
								EndStationNumber = j;
								continue;
							}

						}
						boolean earlyOrNot = true;
						if (findTrainNo(earlydiscount.getJSONArray(d),
								timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
										.getJSONObject("GeneralTrainInfo").getString("TrainNo")) == null) {
							earlyOrNot = false;
						}
						if (earlyOrNot) {
							if (WhichDiscount(
									findTrainNo(earlydiscount.getJSONArray(d),
											timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
													.getJSONObject("GeneralTrainInfo").getString("TrainNo")),
									day) == null
									&& findUniversityDiscount(universitydiscount,
											timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
													.getJSONObject("GeneralTrainInfo").getString("TrainNo"),
											day) == null) {
								continue;
							}
						} else {
							if (findUniversityDiscount(universitydiscount,
									timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
											.getJSONObject("GeneralTrainInfo").getString("TrainNo"),
									day) == null) {
								continue;
							}
						}
						int dpt = 0;
						if (TimeFromStringToInt(
								timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
										.getJSONObject(EndStationNumber).getString("DepartureTime")) <= 60) {
							dpt = TimeFromStringToInt(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(EndStationNumber)
									.getString("DepartureTime")) + 24 * 60;
						} else {
							dpt = TimeFromStringToInt(timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(EndStationNumber)
									.getString("DepartureTime"));
						}
						// 判斷能不能在客人時間搭到和達到
						if (TimeFromStringToInt(departureTime) < TimeFromStringToInt(
								timetable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
										.getJSONObject(BeginStationNumber).getString("DepartureTime"))
								&& TimeFromStringToInt(arrivalTime) > dpt) {
							answer = Copy2DArray(answer, 1);
							answer[answerlength] = new String[7];
							// TrainNo
							answer[answerlength][0] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONObject("GeneralTrainInfo").getString("TrainNo");
							// Begin Station(return its English name/Chinese name)
							answer[answerlength][1] = StationIDToName(Startstation);
							// Departure time
							answer[answerlength][2] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(BeginStationNumber)
									.getString("DepartureTime");
							// Arrival Station(return its English name/Chinese name)
							answer[answerlength][3] = StationIDToName(Arrivalstation);
							// Arrival time
							answer[answerlength][4] = timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
									.getJSONArray("StopTimes").getJSONObject(EndStationNumber)
									.getString("DepartureTime");
							// Driving time
							answer[answerlength][5] = CountDrivingTime(
									timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
											.getJSONArray("StopTimes").getJSONObject(BeginStationNumber)
											.getString("DepartureTime"),
									timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
											.getJSONArray("StopTimes").getJSONObject(EndStationNumber)
											.getString("DepartureTime"));
							// University Discount / Early Discount
							if (earlyOrNot) {
								answer[answerlength][6] = findUniversityDiscount(universitydiscount,
										timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
												.getJSONObject("GeneralTrainInfo").getString("TrainNo"),
										day)
										+ " / "
										+ WhichDiscount(findTrainNo(earlydiscount.getJSONArray(d),
												timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
														.getJSONObject("GeneralTrainInfo").getString("TrainNo")),
												day);
							} else {
								answer[answerlength][6] = findUniversityDiscount(universitydiscount,
										timetable.getJSONObject(i).getJSONObject("GeneralTimetable")
												.getJSONObject("GeneralTrainInfo").getString("TrainNo"),
										day) + " / " + null;
							}

							answerlength += 1;
							answer[0][7] = String.valueOf(answerlength);
						}
					}
				}
			}
		}
		if (answer.length == 0) {
			String[][] n = new String[1][1];
			n[0][0] = "0";
			return n;
		} else
			return answer;
	}

}