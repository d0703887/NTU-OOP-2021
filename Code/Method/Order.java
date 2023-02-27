package com.tcg.json;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.*;
import org.json.simple.parser.*;

//用來處理訂單
public class Order {
	// ----------------幫忙的method----------------

	// 站名轉代號
	public static String station(String s) {
		if (s.equals("南港") || s.equals("Nangang")) {
			return "0990";
		} else if (s.equals("雲林") || s.equals("Yunlin")) {
			return "1047";
		} else if (s.equals("彰化") || s.equals("Changhua")) {
			return "1043";
		} else if (s.equals("台北") || s.equals("Taipei")) {
			return "1000";
		} else if (s.equals("板橋") || s.equals("Banciao")) {
			return "1010";
		} else if (s.equals("桃園") || s.equals("Taoyuan")) {
			return "1020";
		} else if (s.equals("新竹") || s.equals("Hsinchu")) {
			return "1030";
		} else if (s.equals("台中") || s.equals("Taichung")) {
			return "1040";
		} else if (s.equals("嘉義") || s.equals("Chiayi")) {
			return "1050";
		} else if (s.equals("台南") || s.equals("Tainan")) {
			return "1060";
		} else if (s.equals("左營") || s.equals("Zuoying")) {
			return "1070";
		} else {
			return "1035";
		}

	}

	// 數字轉String
	public static String number(int i) {
		if (i < 10) {
			return "0" + i;
		} else {
			return String.valueOf(i);
		}
	}

	// 數字換座位
	public static String position(int i) {
		if (i == 0) {
			return "A";
		} else if (i == 1) {
			return "B";
		} else if (i == 2) {
			return "C";
		} else if (i == 3) {
			return "D";
		} else {
			return "E";
		}
	}

	// 正男格式轉成帥哥格式
	public static String changeFormat(String date) {
		String result = date.substring(0, 4);
		if (Integer.valueOf(date.substring(5, 6)) == 0) {
			result += "/" + date.substring(6, 7);
		} else {
			result += "/" + date.substring(5, 7);
		}
		if ((Integer.valueOf(date.substring(8, 9))) == 0) {
			result += "/" + date.substring(9);

		} else {
			result += "/" + date.substring(8);
		}
		return result;
	}

	// 根據date回傳一個Calendar
	public static Calendar date(String date) {
		int year = Integer.valueOf(date.substring(0, 4));
		int month;
		int day;
		if (date.substring(5).indexOf("/") == 2) {
			month = Integer.valueOf(date.substring(5, 7));
		} else {
			month = Integer.valueOf(date.substring(5, 6));
		}
		day = Integer.valueOf(date.substring(date.substring(5).indexOf("/") + 6));
		Calendar result = Calendar.getInstance();
		result.set(year, month - 1, day);
		return result;

	}

	// 修改earlyDiscountNumber裡面的數量
	public static void updateEarlyDiscountNumber(Ticket tic) {
		String goDate = tic.getGoDate();
		String goTrainNo = tic.getGoTrainNo();
		int goCount = tic.getGoCount();
		double goDiscount = tic.getGoDiscount();
		String backDate = tic.getBackDate();
		String backTrainNo = tic.getBackTrainNo();
		int backCount = tic.getBackCount();
		double backDiscount = tic.getBackDiscount();

		JSONObject earlyDiscount = JSONUtils.getJSONObjectFromFile("/earlyDiscountNumber.json");
		JSONArray trainDate = earlyDiscount.getJSONArray(goDate);
		for (int i = 0; i < trainDate.length(); i++) {
			if (trainDate.getJSONObject(i) == null) {
			} else {
				String name = trainDate.getJSONObject(i).names().getString(0);
				if (name.equals(goTrainNo)) {
					// 乘車日 Day of week
					Calendar c = Order.date(goDate);
					String dayOfWeek = Order.Day(c.get(Calendar.DAY_OF_WEEK - 1));
					earlyDiscount.remove(goDate);
					JSONArray day = trainDate.getJSONObject(i).getJSONArray(dayOfWeek);
					for (int j = 0; j < day.length(); j++) {
						if (day.getJSONObject(j).getDouble("discount") == goDiscount) {
							int tmp = trainDate.getJSONObject(i).getJSONArray(dayOfWeek).getJSONObject(j)
									.getInt("tickets");
							trainDate.getJSONObject(i).getJSONArray(dayOfWeek).getJSONObject(j).remove("tickets");
							trainDate.getJSONObject(i).getJSONArray(dayOfWeek).getJSONObject(j).put("tickets",
									tmp - goCount);
							earlyDiscount.put(goDate, trainDate);
							break;
						} else {
						}
					}
					break;
				}
			}
		}
		JSONArray trainDate2 = earlyDiscount.getJSONArray(backDate);
		for (int i = 0; i < trainDate2.length(); i++) {
			if (trainDate2.getJSONObject(i) == null) {
			} else {
				String name = trainDate2.getJSONObject(i).names().getString(0);
				if (name.equals(backTrainNo)) {
					// 乘車日 Day of week
					Calendar c = Order.date(backDate);
					String dayOfWeek2 = Order.Day(c.get(Calendar.DAY_OF_WEEK - 1));
					earlyDiscount.remove(backDate);
					JSONArray day2 = trainDate2.getJSONObject(i).getJSONArray(dayOfWeek2);
					for (int j = 0; j < day2.length(); j++) {
						if (day2.getJSONObject(j).getDouble("discount") == backDiscount) {
							int tmp2 = trainDate2.getJSONObject(i).getJSONArray(dayOfWeek2).getJSONObject(j)
									.getInt("tickets");
							trainDate2.getJSONObject(i).getJSONArray(dayOfWeek2).getJSONObject(j).remove("tickets");
							trainDate2.getJSONObject(i).getJSONArray(dayOfWeek2).getJSONObject(j).put("tickets",
									tmp2 - backCount);
							earlyDiscount.put(backDate, trainDate2);
							break;
						} else {
						}
					}
					break;
				}
			}
		}
		try (FileWriter file = new FileWriter("C:\\Users\\evanl\\eclipse-workspace\\JSONT\\assets\\seatX.json")) {
			file.write(earlyDiscount.toString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 修改seatNumber裡面的數量
	public static void updateSeatNumber(String backSeat, String backDate, String backTrainNo, int backTicCount,
			String goSeat, String goDate, String goTrainNo, int goTicCount) {
		int carNo = Integer.valueOf(goSeat.substring(0, 2));
		JSONObject seatNumber = JSONUtils.getJSONObjectFromFile("/seatNumber.json");
		JSONArray seatDate = seatNumber.getJSONArray(goDate);
		for (int i = 0; i < seatDate.length(); i++) {
			String name = seatDate.getJSONObject(i).names().getString(0);
			if (name.equals(goTrainNo)) {
				int tmp = seatDate.getJSONObject(i).getJSONArray(goTrainNo).getInt(carNo - 1);
				seatDate.getJSONObject(i).getJSONArray(goTrainNo).put(carNo - 1, tmp - goTicCount);
				seatNumber.remove(goDate);
				seatNumber.put(goDate, seatDate);
				break;
			} else {
			}

		}
		int carNo2 = Integer.valueOf(backSeat.substring(0, 2));
		JSONArray seatDate2 = seatNumber.getJSONArray(backDate);
		for (int i = 0; i < seatDate2.length(); i++) {
			String name = seatDate2.getJSONObject(i).names().getString(0);
			if (name.equals(backTrainNo)) {
				int tmp2 = seatDate.getJSONObject(i).getJSONArray(backTrainNo).getInt(carNo2 - 1);
				seatDate2.getJSONObject(i).getJSONArray(backTrainNo).put(carNo2 - 1, tmp2 - backTicCount);
				seatNumber.remove(backDate);
				seatNumber.put(backDate, seatDate2);
				break;
			}
		}
		try (FileWriter file = new FileWriter("C:\\Users\\evanl\\eclipse-workspace\\JSONT\\assets\\seatX.json")) {
			file.write(seatNumber.toString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 修改seatX裡面的位置boolean
	public static JSONObject updateSeatX(JSONObject seatX, String seat, String date, String trainNo) {
		int carNo = Integer.valueOf(seat.substring(0, 2));
		int row = Integer.valueOf(seat.substring(2, 4));
		String position = seat.substring(4);
		JSONArray trainDate = seatX.getJSONArray(date);
		for (int i = 0; i < trainDate.length(); i++) {
			String name = trainDate.getJSONObject(i).names().getString(0);
			if (name.equals(trainNo)) {
				JSONObject train = trainDate.getJSONObject(i);
				JSONArray tmp = train.getJSONObject(trainNo).getJSONArray("cars").getJSONObject(carNo - 1)
						.getJSONObject("seats").getJSONArray(String.valueOf(row));
				JSONArray replace = new JSONArray();
				if (position.equals("A")) {
					replace.put(0, 1);
					replace.put(1, tmp.getInt(1));
					replace.put(2, tmp.getInt(2));
					replace.put(3, tmp.getInt(3));
					replace.put(4, tmp.getInt(4));
				} else if (position.equals("B")) {
					replace.put(1, 1);
					replace.put(0, tmp.getInt(0));
					replace.put(2, tmp.getInt(2));
					replace.put(3, tmp.getInt(3));
					replace.put(4, tmp.getInt(4));
				} else if (position.equals("C")) {
					replace.put(2, 1);
					replace.put(0, tmp.getInt(0));
					replace.put(1, tmp.getInt(1));
					replace.put(3, tmp.getInt(3));
					replace.put(4, tmp.getInt(4));
				} else if (position.equals("D")) {
					replace.put(3, 1);
					replace.put(0, tmp.getInt(0));
					replace.put(1, tmp.getInt(1));
					replace.put(2, tmp.getInt(2));
					replace.put(4, tmp.getInt(4));
				} else if (position.equals("E")) {
					replace.put(4, 1);
					replace.put(0, tmp.getInt(0));
					replace.put(1, tmp.getInt(1));
					replace.put(2, tmp.getInt(2));
					replace.put(3, tmp.getInt(3));
				}
				train.getJSONObject(trainNo).getJSONArray("cars").getJSONObject(carNo - 1).getJSONObject("seats")
						.remove(String.valueOf(row));
				train.getJSONObject(trainNo).getJSONArray("cars").getJSONObject(carNo - 1).getJSONObject("seats")
						.put(String.valueOf(row), replace);
				trainDate.put(i, train);
				seatX.remove(date);
				seatX.put(date, trainDate);
				break;
			} else {
			}
		}
		return seatX;

	}

	// 楷威寫的
	// Convert station to Chinese Name
	JSONArray sta = JSONUtils.getJSONArrayFromFile("/station.json");

	public String EnToCh(String StationEn) {
		for (int i = 0; i < sta.length(); i++) {
			if (StationEn.equals(sta.getJSONObject(i).getJSONObject("StationName").getString("En"))) {
				String Name = // sta.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw") + " / "
						sta.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw");
				return Name;
			}
		}
		return "Your StationID is wrong. \nPleas input it again.";
	}

	// compare兩個時間，first大於next 輸出true
	public static boolean compareTime(String first, String next) {
		if (Integer.valueOf(first.substring(0, 2)) - Integer.valueOf(next.substring(0, 2)) > 0) {
			return true;
		} else if (Integer.valueOf(first.substring(0, 2)) - Integer.valueOf(next.substring(0, 2)) < 0) {
			return false;
		} else {
			return true;
		}
	}

	// 輸東西進去array
	public static String[][] sort(String[][] list, String trainNo, String discount, String departure, String arrival,
			String duration, int length) {
		if (length == 0) {
			list[0][0] = trainNo;
			list[0][1] = discount;
			list[0][2] = departure;
			list[0][3] = arrival;
			list[0][4] = duration;

		} else {
			// 如果比第0項小，直接丟到第0項
			if (compareTime(list[0][2], departure)) {
				for (int i = length - 1; i >= 0; i--) {
					list[i + 1][0] = list[i][0];
					list[i + 1][1] = list[i][1];
					list[i + 1][2] = list[i][2];
					list[i + 1][3] = list[i][3];
					list[i + 1][4] = list[i][4];
				}
				list[0][0] = trainNo;
				list[0][1] = discount;
				list[0][2] = departure;
				list[0][3] = arrival;
				list[0][4] = duration;
			}
			// 插進正確的位置
			else {
				boolean check = false;
				for (int i = 0; i < length - 1; i++) {
					// 找到正確的位置之後，插入
					if (compareTime(list[i][2], departure) == false && compareTime(list[i + 1][2], departure)) {
						for (int j = length - 1; j >= i + 1; j--) {
							list[j + 1][0] = list[j][0];
							list[j + 1][1] = list[j][1];
							list[j + 1][2] = list[j][2];
							list[j + 1][3] = list[j][3];
							list[j + 1][4] = list[j][4];
						}
						list[i + 1][0] = trainNo;
						list[i + 1][1] = discount;
						list[i + 1][2] = departure;
						list[i + 1][3] = arrival;
						list[i + 1][4] = duration;
						check = true;
						break;
					} else {
					}
				}
				// 如果都沒找到正確位置，就插在最後面
				if (check == true) {

				} else {
					list[length][0] = trainNo;
					list[length][1] = discount;
					list[length][2] = departure;
					list[length][3] = arrival;
					list[length][4] = duration;
				}
			}
		}
		return list;
	}

	// 時間相差
	public static String duration(String start, String end) {
		int duration;
		int sta = Integer.valueOf(start.substring(0, 2)) * 60 + Integer.valueOf(start.substring(3));
		int en = Integer.valueOf(end.substring(0, 2)) * 60 + Integer.valueOf(end.substring(3));
		if (en - sta < 0) {
			duration = 1440 - sta + en;
		} else {
			duration = en - sta;
		}
		if (duration % 60 < 10) {
			return duration / 60 + ":0" + duration % 60;
		} else {
			return duration / 60 + ":" + duration % 60;
		}

	}

	// 回傳第i車廂有幾排座位
	public static int rowOfSeat(int i) {
		if (i == 1) {
			return 13;
		} else if (i == 2 || i == 4 || i == 8 || i == 10) {
			return 20;
		} else if (i == 3 || i == 9 || i == 11) {
			return 18;
		} else if (i == 5 || i == 6) {
			return 17;
		} else if (i == 7) {
			return 12;
		}
		// i==12
		else {
			return 14;
		}

	}

	// goOrBack表示現在檢查去程日期還是回程日期
	// 判斷南下(0)/北上(1)
	public static int Direction(Ticket tic, String goOrBack) {
		JSONArray station = JSONUtils.getJSONArrayFromFile("/station.json");
		String start = "0";
		String end = "0";
		double sta;
		double en;
		for (int i = 0; i < station.length(); i++) {
			if (tic.getStart().equals(station.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw"))
					|| tic.getStart().equals(station.getJSONObject(i).getJSONObject("StationName").getString("En"))) {
				start = station.getJSONObject(i).getString("StationID");
			} else if (tic.getEnd().equals(station.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw"))
					|| tic.getEnd().equals(station.getJSONObject(i).getJSONObject("StationName").getString("En"))) {
				end = station.getJSONObject(i).getString("StationID");
			} else {
			}
		}
		// 去程
		if (goOrBack.equals("go")) {
			sta = Double.valueOf(start);
			en = Double.valueOf(end);
		}
		// 回程
		else {
			sta = Double.valueOf(end);
			en = Double.valueOf(start);
		}
		if ((sta - en) < 0) {
			return 0;
		} else if ((sta - en) > 0) {
			return 1;
		} else {
			return 2;
		}
	}

	// goOrBack表示現在檢查去程日期還是回程日期
	// 有無在早鳥優惠期限裡，從乘車日(含)前28天的00:00:01~乘車日(含)前5天的23:59:59
	public static boolean earlyDiscount(Ticket tic, String goOrBack) {
		String date;

		// 去程乘車日
		if (goOrBack.equals("go")) {
			date = tic.getGoDate();
		}
		// 回程乘車日
		else {
			date = tic.getBackDate();
		}
		// 乘車日(含)前5天
		Calendar last = Order.date(date);
		last.add(Calendar.DATE, -4);
		last.set(Calendar.HOUR, 11);
		last.set(Calendar.AM_PM, Calendar.PM);
		last.set(Calendar.MINUTE, 59);
		last.set(Calendar.SECOND, 59);
		// 乘車日(含)前28天
		Calendar first = Order.date(date);
		first.add(Calendar.DATE, -27);
		first.set(Calendar.HOUR, 0);
		first.set(Calendar.AM_PM, Calendar.AM);
		first.set(Calendar.MINUTE, 0);
		first.set(Calendar.SECOND, 1);
		Calendar now = Calendar.getInstance();
		// 確認訂票當日是否在期限內
		if (now.compareTo(last) <= 0 && now.compareTo(first) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	// 找乘車日當天是禮拜幾
	public static String Day(int i) {
		if (i == 0) {
			return "Sunday";
		} else if (i == 1) {
			return "Monday";
		} else if (i == 2) {
			return "Tuesday";
		} else if (i == 3) {
			return "Wednesday";
		} else if (i == 4) {
			return "Thursday";
		} else if (i == 5) {
			return "Friday";
		} else {
			return "Saturday";
		}
	}

	// goOrBack表示現在檢查去程日期還是回程日期
	// 找出發時間
	public static String DepartureTime(JSONObject obj, Ticket tic, String goOrBack) {
		JSONArray Stoptimes = obj.getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		String target;
		// 去程
		if (goOrBack.equals("go")) {
			target = tic.getStart();
		}
		// 回程
		else {
			target = tic.getEnd();
		}

		for (int i = 0; i < Stoptimes.length(); i++) {
			if (target.equals(Stoptimes.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw"))
					|| target.equals(Stoptimes.getJSONObject(i).getJSONObject("StationName").getString("En"))) {
				return Stoptimes.getJSONObject(i).getString("DepartureTime");
			} else {
			}
		}
		return null;
	}

	// goOrBack表示現在檢查去程日期還是回程日期
	// 找抵達時間
	public static String ArrivalTime(JSONObject obj, Ticket tic, String goOrBack) {
		JSONArray Stoptimes = obj.getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		String target;
		if (goOrBack.equals("go")) {
			target = tic.getEnd();
		} else {
			target = tic.getStart();
		}
		for (int i = 0; i < Stoptimes.length(); i++) {
			if (target.equals(Stoptimes.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw"))
					|| target.equals(Stoptimes.getJSONObject(i).getJSONObject("StationName").getString("En"))) {
				return Stoptimes.getJSONObject(i).getString("DepartureTime");
			} else {
			}
		}
		return null;
	}

	// ----------------檢查順序--------------------
	// goOrBack表示現在檢查去程日期還是回程日期
	// 找符合條件(起站，迄站，當天有無車次，方向正確)的列車
	public static boolean matchTrain(JSONObject obj, Ticket tic, String goOrBack) {
		boolean startsta = false;
		boolean endsta = false;
		Calendar c = Calendar.getInstance();
		// 去程乘車日
		if (goOrBack.equals("go")) {
			c = Order.date(tic.getGoDate());
		}
		// 回程乘車日
		else {
			c = Order.date(tic.getBackDate());
		}

		String dayOfWeek = Order.Day(c.get(Calendar.DAY_OF_WEEK) - 1);
		JSONObject General = obj.getJSONObject("GeneralTimetable");
		// 方向有沒有正確
		int direction = Order.Direction(tic, goOrBack);
		if (obj.getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getInt("Direction") == direction) {
		} else {
			return false;
		}
		// 找起站
		JSONArray Stoptime = General.getJSONArray("StopTimes");
		for (int i = 0; i < Stoptime.length(); i++) {
			String startCh = Stoptime.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw");
			String startEn = Stoptime.getJSONObject(i).getJSONObject("StationName").getString("En");
			if (tic.getStart().equals(startCh) || tic.getStart().equals(startEn)) {
				startsta = true;
			} else {

			}
		}
		// 找迄站

		for (int i = 0; i < Stoptime.length(); i++) {
			String EndCh = Stoptime.getJSONObject(i).getJSONObject("StationName").getString("Zh_tw");
			String EndEn = Stoptime.getJSONObject(i).getJSONObject("StationName").getString("En");
			if (tic.getEnd().equals(EndCh) || tic.getEnd().equals(EndEn)) {
				endsta = true;
			} else {

			}
		}

		// 確認當天有運行
		if (General.getJSONObject("ServiceDay").getInt(dayOfWeek) == 1) {
			return startsta && endsta;
		} else {
			return false;
		}
	}

	// output 失敗，日期，現在還不能預約
	public static boolean valid(Ticket tic) {
		if (tic.getBackCount() != 0) {
			String godate = tic.getGoDate();
			String backdate = tic.getBackDate();
			Calendar now = Calendar.getInstance();
			Calendar go = Order.date(godate);
			go.add(Calendar.DATE, -27);
			go.set(Calendar.HOUR, 0);
			go.set(Calendar.AM_PM, Calendar.AM);
			go.set(Calendar.MINUTE, 0);
			go.set(Calendar.SECOND, 1);
			Calendar back = Order.date(backdate);
			back.add(Calendar.DATE, -27);
			back.set(Calendar.HOUR, 0);
			back.set(Calendar.AM_PM, Calendar.AM);
			back.set(Calendar.MINUTE, 0);
			back.set(Calendar.SECOND, 1);
			if (now.compareTo(back) < 0) {

				return false;
			} else if (now.compareTo(go) < 0) {

				return false;
			} else {
				return true;
			}
		} else {
			String godate = tic.getGoDate();
			Calendar now = Calendar.getInstance();
			Calendar go = Order.date(godate);
			go.add(Calendar.DATE, -27);
			go.set(Calendar.HOUR, 0);
			go.set(Calendar.AM_PM, Calendar.AM);
			go.set(Calendar.MINUTE, 0);
			go.set(Calendar.SECOND, 1);
			if (now.compareTo(go) < 0) {

				return false;
			} else {
				return true;
			}
		}
	}

	// goOrBack表示現在檢查去程日期還是回程日期
	// 僅提供預訂當日及未來28日以內之車票。訂位開放時間為乘車日(含)前28日凌晨0點開始
	// 當日車次之預訂僅受理至列車出發時間前1小時為止
	public static boolean book(JSONObject obj, Ticket tic, String goOrBack) {
		String date;
		// 去程乘車日
		if (goOrBack.equals("go")) {
			date = tic.getGoDate();
		}
		// 回程乘車日
		else {
			date = tic.getBackDate();
		}

		Calendar now = Calendar.getInstance();
		// 乘車時間前1小時
		String Departure = Order.DepartureTime(obj, tic, goOrBack);
		;
		Calendar last = Order.date(date);
		last.set(Calendar.HOUR_OF_DAY, Integer.valueOf(Departure.substring(0, 2)));
		last.set(Calendar.MINUTE, Integer.valueOf(Departure.substring(3)));
		last.add(Calendar.HOUR_OF_DAY, -1);
		// 乘車日(含)前28天
		Calendar first = Order.date(date);
		first.add(Calendar.DATE, -27);
		first.set(Calendar.HOUR, 0);
		first.set(Calendar.AM_PM, Calendar.AM);
		first.set(Calendar.MINUTE, 0);
		first.set(Calendar.SECOND, 1);
		// 確認訂票當時是否在期限內
		if (now.compareTo(last) <= 0 && now.compareTo(first) >= 0) {
			return true;
		} else {
			return false;
		}

	}

	// 亂數產生CODE
	public static String code() {
		long t = System.currentTimeMillis();
		Random r1 = new Random(t);
		long a = Math.round(r1.nextDouble() * 100000000);
		String code = String.valueOf(a);
		while (code.length() < 9) {
			code = "0" + code;
		}
		return code;
	}

	// goOrBack表示現在檢查去程日期還是回程日期
	// 判斷那輛車還有沒有位子
	public static boolean hasSeat(JSONObject obj, Ticket tic, String goOrBack) {
		String date;
		int ticketCount;
		JSONObject seatNumber = JSONUtils.getJSONObjectFromFile("/seatNumber.json");
		// 搭乘車次
		String trainNo = obj.getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("TrainNo");
		// 去程乘車日
		if (goOrBack.equals("go")) {
			date = tic.getGoDate();
			ticketCount = tic.getGoCount();
		}
		// 回程乘車日
		else {
			date = tic.getBackDate();
			ticketCount = tic.getBackCount();
		}
		// 乘車日當天的所有列車
		JSONArray trains = seatNumber.getJSONArray(date);
		// 開始搜尋目標列車的座位表，然後檢查
		for (int i = 0; i < trains.length(); i++) {
			String name = trains.getJSONObject(i).names().getString(0);
			// 先找到目標列車
			if (name.equals(trainNo)) {
				JSONArray number = trains.getJSONObject(i).getJSONArray(trainNo);
				// 再一節一節車廂檢查有沒有剩餘位子
				// 先檢查標準
				if (tic.getCarType().equals("標準")) {
					for (int j = 0; j < 12; j++) {
						if (j != 5) {
							if (number.getInt(j) > ticketCount) {
								return true;
							} else {
							}
						} else {
						}
					}
				}
				// 檢查商務
				else {
					if (number.getInt(5) > ticketCount) {
						return true;
					} else {
					}
				}
				// 都沒有找到位子再輸出false
				return false;
			} else {
			}
		}
		// 在這之前就應該要完成
		return false;
	}

	// goOrBack表示現在檢查去程日期還是回程日期
	// 判斷折數
	public static String discount(JSONObject obj, Ticket tic, String goOrBack) {
		String ticType = tic.getTicketType();
		String date;
		// 敬老、孩童、愛心
		if (ticType.equals("敬老") || ticType.equals("孩童") || ticType.equals("愛心")) {
			return "0.5";
		}
		// 大學生折數
		else if (ticType.equals("學生") && tic.getCarType().equals("標準")) {
			if (goOrBack.equals("go")) {
				date = tic.getGoDate();
			} else {
				date = tic.getBackDate();
			}
			Calendar c = Order.date(date);
			String dayOfWeek = Order.Day(c.get(Calendar.DAY_OF_WEEK) - 1);
			JSONArray discount = JSONUtils.getJSONObjectFromFile("/universityDiscount.json")
					.getJSONArray("DiscountTrains");
			for (int i = 0; i < discount.length(); i++) {
				if (discount.getJSONObject(i).getString("TrainNo").equals(
						obj.getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("TrainNo"))) {
					double dis = discount.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(dayOfWeek);
					if (dis != 1 && dis != 0) {
						return String.valueOf(dis);
					} else {
						return "";
					}
				} else {
				}
			}

			return "";
		}
		// 早鳥票折數
		else if (ticType.equals("一般") && tic.getCarType().equals("標準")) {
			int count;
			JSONObject earlyNumber = JSONUtils.getJSONObjectFromFile("/earlyDiscountNumber.json");
			// 搭乘車次
			String trainNo = obj.getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo")
					.getString("TrainNo");
			// 去程乘車日日期
			if (goOrBack.equals("go")) {
				date = tic.getGoDate();
				count = tic.getGoCount();
			}
			// 回程乘車日日期
			else {
				date = tic.getBackDate();
				count = tic.getBackCount();
			}
			// 符合早鳥票資格
			if (Order.earlyDiscount(tic, goOrBack)) {
				// 乘車日 Day of week
				Calendar c = Order.date(date);
				String dayOfWeek = Order.Day(c.get(Calendar.DAY_OF_WEEK - 1));
				// 乘車日當天所有列車剩餘早鳥票張數
				JSONArray early = earlyNumber.getJSONArray(date);
				// 開始搜尋目標列車的剩餘早鳥票張數
				for (int i = 0; i < early.length(); i++) {
					if (early.getJSONObject(i).names() == null) {
					} else {
						String name = early.getJSONObject(i).names().getString(0);
						// 開始搜尋目標列車的剩餘早鳥票張數
						if (name.equals(trainNo)) {
							try {
								int j = 0;
								JSONArray a = early.getJSONObject(i).getJSONObject(trainNo).getJSONArray(dayOfWeek);
								// 輪流找不同折數的剩餘張數
								while (j < a.length()) {
									if (a.getJSONObject(j).getInt("tickets") >= count) {
										return a.getJSONObject(j).getDouble("discount") + "";
									} else {
									}
								}
								// 都沒有的話
								return "";

							} catch (JSONException e) {
								return "";
							}
						} else {
						}
					}
				}
			}
			// 不符合早鳥票資格
			else {
				return "";
			}

			// 在這之前應該就要return完成
			return "";
		}
		// 商務艙沒有折扣 北七
		else {
			return "";
		}

	}

	// ----------------使用者選好車次以後----------------
	// 配給位置給用戶
	public static String[][] seat(Ticket tic) {
		String goDate = tic.getGoDate();
		String goTrainNo = tic.getGoTrainNo();
		int goCount = tic.getGoCount();
		String backDate = tic.getBackDate();
		String backTrainNo = tic.getBackTrainNo();
		int backCount = tic.getBackCount();
		String[] backResult = new String[backCount];
		String[] goResult = new String[goCount];

		// --------------------------------去程座位-------------------------------
		// 檢查有沒有找到目標列車座位表
		int count = 0;
		// 目標列車座位表在date[r]
		int r = -1;
		// 目標列車的座位表
		JSONArray car = new JSONArray();
		JSONObject seatX = JSONUtils.getJSONObjectFromFile("/seatX.json");
		JSONArray gotrainDate = seatX.getJSONArray(goDate);
		for (int i = 0; i < gotrainDate.length(); i++) {
			String name = gotrainDate.getJSONObject(i).names().getString(0);
			if (name.equals(goTrainNo)) {
				car = gotrainDate.getJSONObject(i).getJSONObject(goTrainNo).getJSONArray("cars");
				count += 1;
				r = i;
				break;
			} else {
			}
		}
		// 沒找到
		if (count == 0) {
			JSONObject seatExample = JSONUtils.getJSONObjectFromFile("/seatExample.json");
			// 目標列車座位表
			JSONObject a = new JSONObject();
			// 開始配座位
			if (tic.getCarType().equals("標準")) {
				// 標準靠窗優先
				if (tic.getPrefer().equals("靠窗優先")) {
					if (goCount == 1) {
						seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats").getJSONArray("1")
								.put(0, 1);
						goResult[0] = "0101A";
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 標準靠窗優先 1張");
					}
					// 訂多張
					else {
						// 代表現在找到幾個座位了
						int m = 0;
						// 第一車廂第i排
						for (int i = 1; i <= Order.rowOfSeat(1); i++) {
							// A,B,C,D,E座位
							for (int j = 0; j < 5; j++) {
								if (seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
										.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
									seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).put(j, 1);
									goResult[m] = "01" + Order.number(i) + Order.position(j);
									m += 1;
									if (m == goCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 標準靠窗優先 多張");
					}
				}
				// 標準走道優先
				else if (tic.getPrefer().equals("走道優先")) {
					if (goCount == 1) {
						seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats").getJSONArray("1")
								.put(2, 1);
						goResult[0] = "0101C";
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 標準走道優先 1張");
					}
					// 訂多張
					else {
						// 代表現在找到幾個座位了
						int m = 0;
						// 第一車廂第i排
						for (int i = 1; i <= Order.rowOfSeat(1); i++) {
							// A,B,C,D,E座位
							for (int j = 0; j < 5; j++) {
								if (seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
										.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
									seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).put(j, 1);
									goResult[m] = "01" + Order.number(i) + Order.position(j);
									m += 1;
									if (m == goCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 標準走道優先 多張");
					}
				}
				// 標準無要求
				else {
					if (goCount == 1) {
						seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats").getJSONArray("1")
								.put(0, 1);
						goResult[0] = "0101A";
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 標準無要求 1張");
					}
					// 訂多張
					else {
						// 代表現在找到幾個座位了
						int m = 0;
						// 第一車廂第i排
						for (int i = 1; i <= Order.rowOfSeat(1); i++) {
							// A,B,C,D,E座位
							for (int j = 0; j < 5; j++) {
								if (seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
										.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
									seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).put(j, 1);
									goResult[m] = "01" + Order.number(i) + Order.position(j);
									m += 1;
									if (m == goCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 標準無要求 多張");
					}

				}
			}
			// 訂商務
			else {
				// 商務靠窗優先
				if (tic.getPrefer().equals("靠窗優先")) {
					if (goCount == 1) {
						seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats").getJSONArray("1")
								.put(4, 1);
						goResult[0] = "0601E";
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 商務靠窗優先 1張");
					}
					// 訂多張
					else {
						// 代表現在找到幾個座位了
						int m = 0;
						// 第6車廂第i排
						for (int i = 1; i <= Order.rowOfSeat(6); i++) {
							// A,B,C,D,E座位
							for (int j = 0; j < 5; j++) {
								if (seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
										.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
									seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).put(j, 1);
									goResult[m] = "06" + Order.number(i) + Order.position(j);
									m += 1;
									if (m == goCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 商務靠窗優先 多張");
					}
				}
				// 商務走道優先
				else if (tic.getPrefer().equals("走道優先")) {
					if (goCount == 1) {
						seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats").getJSONArray("1")
								.put(3, 1);
						goResult[0] = "0601D";
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 商務走道優先 1張");
					}
					// 訂多張
					else {
						// 代表現在找到幾個座位了
						int m = 0;
						// 第6車廂第i排
						for (int i = 1; i <= Order.rowOfSeat(6); i++) {
							// A,B,C,D,E座位
							for (int j = 0; j < 5; j++) {
								if (seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
										.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
									seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).put(j, 1);
									goResult[m] = "06" + Order.number(i) + Order.position(j);
									m += 1;
									if (m == goCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 商務走道優先 多張");
					}
				}
				// 商務無要求
				else {
					if (goCount == 1) {
						seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats").getJSONArray("1")
								.put(4, 1);
						goResult[0] = "0601E";
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 商務無要求 1張");
					}
					// 訂多張
					else {
						// 代表現在找到幾個座位了
						int m = 0;
						// 第6車廂第i排
						for (int i = 1; i <= Order.rowOfSeat(6); i++) {
							// A,B,C,D,E座位
							for (int j = 0; j < 5; j++) {
								if (seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
										.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
									seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).put(j, 1);
									goResult[m] = "06" + Order.number(i) + Order.position(j);
									m += 1;
									if (m == goCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						a.put(goTrainNo, seatExample);
						gotrainDate.put(a);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("沒找到 商務無要求 多張");
					}
				}
			}
		}
		// 再gotrainDate裡面有找到目標列車座位表
		// count!=0
		else {
			// 標準車廂
			if (tic.getCarType().equals("標準")) {
				// 標準靠窗優先
				if (tic.getPrefer().equals("靠窗優先")) {
					if (goCount == 1) {
						// 第i+1節車廂
						for (int i = 0; i < 12; i++) {
							// 第j排
							for (int j = 0; j <= Order.rowOfSeat(i + 1); j++) {
								// A座位
								if (car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
										.getInt(0) == 0) {
									car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j)).put(0,
											1);
									goResult[0] = Order.number(i + 1) + Order.number(j) + "A";
									gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
									gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
									seatX.remove(goDate);
									seatX.put(goDate, gotrainDate);
									// System.out.println("有找到 標準靠窗優先 1張");
								}
								// E座位
								else if (car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
										.getInt(4) == 0) {
									car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j)).put(4,
											1);
									goResult[0] = Order.number(i + 1) + Order.number(j) + "E";
									gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
									gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
									seatX.remove(goDate);
									seatX.put(goDate, gotrainDate);
									// System.out.println("有找到 標準靠窗優先 1張");
								} else {
								}
							}
						}
					}
					// 訂多張
					else {
						int m = 0;
						// 第i+1節車廂
						for (int i = 0; i < 12; i++) {
							// 第j排
							for (int j = 1; j <= Order.rowOfSeat(i + 1); j++) {
								// A,B,C,D,E座位
								for (int k = 0; k < 5; k++) {
									if (car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
											.getInt(k) == 0) {
										car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
												.put(k, 1);
										goResult[m] = Order.number(i + 1) + Order.number(j) + Order.position(k);
										m += 1;
										if (m == goCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == goCount) {
									break;
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
						gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("有找到 標準靠窗優先 多張");
					}
				}
				// 標準走道優先
				else if (tic.getPrefer().equals("走道優先")) {
					if (goCount == 1) {
						// 第i+1節車廂
						for (int i = 0; i < 12; i++) {
							// 第j排
							for (int j = 0; j <= Order.rowOfSeat(i + 1); j++) {
								// C座位
								if (car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
										.getInt(2) == 0) {
									car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j)).put(2,
											1);
									goResult[0] = Order.number(i + 1) + Order.number(j) + "C";
									gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
									gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
									seatX.remove(goDate);
									seatX.put(goDate, gotrainDate);
									// System.out.println("有找到 標準走道優先 1張");
								}
								// D座位
								else if (car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
										.getInt(3) == 0) {
									car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j)).put(3,
											1);
									goResult[0] = Order.number(i + 1) + Order.number(j) + "D";
									gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
									gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
									seatX.remove(goDate);
									seatX.put(goDate, gotrainDate);
									// System.out.println("有找到 標準走道優先 1張");
								} else {
								}
							}
						}
					}
					// 訂多張
					else {
						int m = 0;
						// 第i+1節車廂
						for (int i = 0; i < 12; i++) {
							// 第j排
							for (int j = 1; j <= Order.rowOfSeat(i + 1); j++) {
								// A,B,C,D,E座位
								for (int k = 0; k < 5; k++) {
									if (car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
											.getInt(k) == 0) {
										car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
												.put(k, 1);
										goResult[m] = Order.number(i + 1) + Order.number(j) + Order.position(k);
										m += 1;
										if (m == goCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == goCount) {
									break;
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
						gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("有找到 標準走道優先 多張");
					}
				}
				// 標準無要求
				else {
					int m = 0;
					// 第i+1節車廂
					for (int i = 0; i < 12; i++) {
						// 第j排
						for (int j = 1; j <= Order.rowOfSeat(i + 1); j++) {
							// A,B,C,D,E座位
							for (int k = 0; k < 5; k++) {
								if (car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
										.getInt(k) == 0) {
									car.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j)).put(k,
											1);
									goResult[m] = Order.number(i + 1) + Order.number(j) + Order.position(k);
									m += 1;
									if (m == goCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						if (m == goCount) {
							break;
						} else {
						}
					}
					gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
					gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
					seatX.remove(goDate);
					seatX.put(goDate, gotrainDate);
					// System.out.println("有找到 標準無要求 ");
				}
			}
			// 商務車廂
			else {
				// 商務靠窗優先
				if (tic.getPrefer().equals("靠窗優先")) {
					if (goCount == 1) {
						// 第6車廂第i排
						for (int i = 1; i <= Order.rowOfSeat(6); i++) {
							// A座位
							if (car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
									.getInt(0) == 0) {
								car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(0, 1);
								goResult[0] = "06" + Order.number(i) + "A";
								gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
								gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
								seatX.remove(goDate);
								seatX.put(goDate, gotrainDate);
								// System.out.println("有找到 商務靠窗優先 1張");
							}
							// E座位
							else if (car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
									.getInt(4) == 0) {
								car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(4, 1);
								goResult[0] = "06" + Order.number(i) + "E";
								gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
								gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
								seatX.remove(goDate);
								seatX.put(goDate, gotrainDate);
								// System.out.println("有找到 商務靠窗優先 1張");
							} else {
							}
						}
					}
					// 訂多張
					else {
						// 抓到幾個位置了
						int m = 0;
						// 第6車廂第i排
						for (int i = 0; i <= Order.rowOfSeat(6); i++) {
							// A,B,C,D,E座位
							for (int k = 0; k < 5; k++) {
								if (car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
										.getInt(k) == 0) {
									car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(k,
											1);
									goResult[m] = "06" + Order.number(i) + Order.position(k);
									m += 1;

									if (m == goCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
						gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("有找到 商務靠窗優先 多張");
					}

				}
				// 商務走道優先
				else if (tic.getPrefer().equals("走道優先")) {
					if (goCount == 1) {
						// 第6車廂第i排
						for (int i = 1; i <= Order.rowOfSeat(6); i++) {
							// C座位
							if (car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
									.getInt(2) == 0) {
								car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(2, 1);
								goResult[0] = "06" + Order.number(i) + "C";
								gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
								gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
								seatX.remove(goDate);
								seatX.put(goDate, gotrainDate);
								// System.out.println("有找到 商務走道優先 1張");
							}
							// D座位
							else if (car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
									.getInt(3) == 0) {
								car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(3, 1);
								goResult[0] = "06" + Order.number(i) + "D";
								gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
								gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
								seatX.remove(goDate);
								seatX.put(goDate, gotrainDate);
								// System.out.println("有找到 商務走道優先 1張");
							} else {
							}
						}
					}
					// 訂多張
					else {
						// 抓到幾個位置了
						int m = 0;
						// 第6車廂第i排
						for (int i = 0; i <= Order.rowOfSeat(6); i++) {
							// A,B,C,D,E座位
							for (int k = 0; k < 5; k++) {
								if (car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
										.getInt(k) == 0) {
									car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(k,
											1);
									goResult[m] = "06" + Order.number(i) + Order.position(k);
									m += 1;

									if (m == goCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == goCount) {
								break;
							} else {
							}
						}
						gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
						gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
						seatX.remove(goDate);
						seatX.put(goDate, gotrainDate);
						// System.out.println("有找到 商務走道優先 多張");
					}
				}
				// 商務無要求
				else {
					// 抓到幾個位置了
					int m = 0;
					// 第6車廂第i排
					for (int i = 0; i <= Order.rowOfSeat(6); i++) {
						// A,B,C,D,E座位
						for (int k = 0; k < 5; k++) {
							if (car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
									.getInt(k) == 0) {
								car.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(k, 1);
								goResult[m] = "06" + Order.number(i) + Order.position(k);
								m += 1;

								if (m == goCount) {
									break;
								} else {
								}
							} else {
							}
						}
						if (m == goCount) {
							break;
						} else {
						}
					}
					gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).remove("cars");
					gotrainDate.getJSONObject(r).getJSONObject(goTrainNo).put("cars", car);
					seatX.remove(goDate);
					seatX.put(goDate, gotrainDate);
					// System.out.println("有找到 商務無要求");
				}
			}
		}

		// ---------------------------------回程座位--------------------------------
		if (backCount != 0) {
			// 檢查有沒有找到目標列車座位表
			int count2 = 0;
			// 目標列車座位表在date[r]
			int r2 = -1;
			// 目標列車的座位表
			JSONArray car2 = new JSONArray();
			JSONArray backtrainDate = seatX.getJSONArray(backDate);
			for (int i = 0; i < backtrainDate.length(); i++) {
				String name = backtrainDate.getJSONObject(i).names().getString(0);
				if (name.equals(backTrainNo)) {
					car2 = backtrainDate.getJSONObject(i).getJSONObject(backTrainNo).getJSONArray("cars");
					count2 += 1;
					r2 = i;
					break;
				} else {
				}
			}
			// 沒找到
			if (count2 == 0) {
				JSONObject seatExample = JSONUtils.getJSONObjectFromFile("/seatExample.json");
				// 目標列車座位表
				JSONObject a = new JSONObject();
				// 開始配座位
				if (tic.getCarType().equals("標準")) {
					// 標準靠窗優先
					if (tic.getPrefer().equals("靠窗優先")) {
						if (backCount == 1) {
							seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats").getJSONArray("1")
									.put(0, 1);
							backResult[0] = "0101A";
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 標準靠窗優先 1張");
						}
						// 訂多張
						else {
							// 代表現在找到幾個座位了
							int m = 0;
							// 第一車廂第i排
							for (int i = 1; i <= Order.rowOfSeat(1); i++) {
								// A,B,C,D,E座位
								for (int j = 0; j < 5; j++) {
									if (seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
										seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
												.getJSONArray(String.valueOf(i)).put(j, 1);
										backResult[m] = "01" + Order.number(i) + Order.position(j);
										m += 1;
										if (m == backCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 標準靠窗優先 多張");
						}
					}
					// 標準走道優先
					else if (tic.getPrefer().equals("走道優先")) {
						if (backCount == 1) {
							seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats").getJSONArray("1")
									.put(2, 1);
							backResult[0] = "0101C";
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 標準走道優先 1張");
						}
						// 訂多張
						else {
							// 代表現在找到幾個座位了
							int m = 0;
							// 第一車廂第i排
							for (int i = 1; i <= Order.rowOfSeat(1); i++) {
								// A,B,C,D,E座位
								for (int j = 0; j < 5; j++) {
									if (seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
										seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
												.getJSONArray(String.valueOf(i)).put(j, 1);
										backResult[m] = "01" + Order.number(i) + Order.position(j);
										m += 1;
										if (m == backCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 標準走道優先 多張");
						}
					}
					// 標準無要求
					else {
						if (backCount == 1) {
							seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats").getJSONArray("1")
									.put(0, 1);
							backResult[0] = "0101A";
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 標準無要求 1張");
						}
						// 訂多張
						else {
							// 代表現在找到幾個座位了
							int m = 0;
							// 第一車廂第i排
							for (int i = 1; i <= Order.rowOfSeat(1); i++) {
								// A,B,C,D,E座位
								for (int j = 0; j < 5; j++) {
									if (seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
										seatExample.getJSONArray("cars").getJSONObject(0).getJSONObject("seats")
												.getJSONArray(String.valueOf(i)).put(j, 1);
										backResult[m] = "01" + Order.number(i) + Order.position(j);
										m += 1;
										if (m == backCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 標準無要求 多張");
						}

					}
				}
				// 訂商務
				else {
					// 商務靠窗優先
					if (tic.getPrefer().equals("靠窗優先")) {
						if (backCount == 1) {
							seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats").getJSONArray("1")
									.put(4, 1);
							backResult[0] = "0601E";
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 商務靠窗優先 1張");
						}
						// 訂多張
						else {
							// 代表現在找到幾個座位了
							int m = 0;
							// 第6車廂第i排
							for (int i = 1; i <= Order.rowOfSeat(6); i++) {
								// A,B,C,D,E座位
								for (int j = 0; j < 5; j++) {
									if (seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
										seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
												.getJSONArray(String.valueOf(i)).put(j, 1);
										backResult[m] = "06" + Order.number(i) + Order.position(j);
										m += 1;
										if (m == backCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 商務靠窗優先 多張");
						}
					}
					// 商務走道優先
					else if (tic.getPrefer().equals("走道優先")) {
						if (backCount == 1) {
							seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats").getJSONArray("1")
									.put(3, 1);
							backResult[0] = "0601D";
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 商務走道優先 1張");
						}
						// 訂多張
						else {
							// 代表現在找到幾個座位了
							int m = 0;
							// 第6車廂第i排
							for (int i = 1; i <= Order.rowOfSeat(6); i++) {
								// A,B,C,D,E座位
								for (int j = 0; j < 5; j++) {
									if (seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
										seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
												.getJSONArray(String.valueOf(i)).put(j, 1);
										backResult[m] = "06" + Order.number(i) + Order.position(j);
										m += 1;
										if (m == backCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 商務走道優先 多張");
						}
					}
					// 商務無要求
					else {
						if (backCount == 1) {
							seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats").getJSONArray("1")
									.put(4, 1);
							backResult[0] = "0601E";
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 商務無要求 1張");
						}
						// 訂多張
						else {
							// 代表現在找到幾個座位了
							int m = 0;
							// 第6車廂第i排
							for (int i = 1; i <= Order.rowOfSeat(6); i++) {
								// A,B,C,D,E座位
								for (int j = 0; j < 5; j++) {
									if (seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
											.getJSONArray(String.valueOf(i)).getInt(j) == 0) {
										seatExample.getJSONArray("cars").getJSONObject(5).getJSONObject("seats")
												.getJSONArray(String.valueOf(i)).put(j, 1);
										backResult[m] = "06" + Order.number(i) + Order.position(j);
										m += 1;
										if (m == backCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							a.put(backTrainNo, seatExample);
							backtrainDate.put(a);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("沒找到 商務無要求 多張");
						}
					}
				}
			}
			// 再gotrainDate裡面有找到目標列車座位表
			// count!=0
			else {
				// 標準車廂
				if (tic.getCarType().equals("標準")) {
					// 標準靠窗優先
					if (tic.getPrefer().equals("靠窗優先")) {
						if (backCount == 1) {
							// 第i+1節車廂
							for (int i = 0; i < 12; i++) {
								// 第j排
								for (int j = 0; j <= Order.rowOfSeat(i + 1); j++) {
									// A座位
									if (car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
											.getInt(0) == 0) {
										car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
												.put(0, 1);
										backResult[0] = Order.number(i + 1) + Order.number(j) + "A";
										backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
										backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
										seatX.remove(backDate);
										seatX.put(backDate, backtrainDate);
										// System.out.println("有找到 標準靠窗優先 1張");
									}
									// E座位
									else if (car2.getJSONObject(i).getJSONObject("seats")
											.getJSONArray(String.valueOf(j)).getInt(4) == 0) {
										car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
												.put(4, 1);
										backResult[0] = Order.number(i + 1) + Order.number(j) + "E";
										backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
										backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
										seatX.remove(backDate);
										seatX.put(backDate, backtrainDate);
										// System.out.println("有找到 標準靠窗優先 1張");
									} else {
									}
								}
							}
						}
						// 訂多張
						else {
							int m = 0;
							// 第i+1節車廂
							for (int i = 0; i < 12; i++) {
								// 第j排
								for (int j = 1; j <= Order.rowOfSeat(i + 1); j++) {
									// A,B,C,D,E座位
									for (int k = 0; k < 5; k++) {
										if (car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
												.getInt(k) == 0) {
											car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
													.put(k, 1);
											backResult[m] = Order.number(i + 1) + Order.number(j) + Order.position(k);
											m += 1;
											if (m == backCount) {
												break;
											} else {
											}
										} else {
										}
									}
									if (m == backCount) {
										break;
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
							backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("有找到 標準靠窗優先 多張");
						}
					}
					// 標準走道優先
					else if (tic.getPrefer().equals("走道優先")) {
						if (backCount == 1) {
							// 第i+1節車廂
							for (int i = 0; i < 12; i++) {
								// 第j排
								for (int j = 0; j <= Order.rowOfSeat(i + 1); j++) {
									// C座位
									if (car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
											.getInt(2) == 0) {
										car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
												.put(2, 1);
										backResult[0] = Order.number(i + 1) + Order.number(j) + "C";
										backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
										backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
										seatX.remove(backDate);
										seatX.put(backDate, backtrainDate);
										// System.out.println("有找到 標準走道優先 1張");
									}
									// D座位
									else if (car2.getJSONObject(i).getJSONObject("seats")
											.getJSONArray(String.valueOf(j)).getInt(3) == 0) {
										car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
												.put(3, 1);
										backResult[0] = Order.number(i + 1) + Order.number(j) + "D";
										backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
										backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
										seatX.remove(backDate);
										seatX.put(backDate, backtrainDate);
										// System.out.println("有找到 標準走道優先 1張");
									} else {
									}
								}
							}
						}
						// 訂多張
						else {
							int m = 0;
							// 第i+1節車廂
							for (int i = 0; i < 12; i++) {
								// 第j排
								for (int j = 1; j <= Order.rowOfSeat(i + 1); j++) {
									// A,B,C,D,E座位
									for (int k = 0; k < 5; k++) {
										if (car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
												.getInt(k) == 0) {
											car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
													.put(k, 1);
											backResult[m] = Order.number(i + 1) + Order.number(j) + Order.position(k);
											m += 1;
											if (m == backCount) {
												break;
											} else {
											}
										} else {
										}
									}
									if (m == backCount) {
										break;
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
							backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("有找到 標準走道優先 多張");
						}
					}
					// 標準無要求
					else {
						int m = 0;
						// 第i+1節車廂
						for (int i = 0; i < 12; i++) {
							// 第j排
							for (int j = 1; j <= Order.rowOfSeat(i + 1); j++) {
								// A,B,C,D,E座位
								for (int k = 0; k < 5; k++) {
									if (car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
											.getInt(k) == 0) {
										car2.getJSONObject(i).getJSONObject("seats").getJSONArray(String.valueOf(j))
												.put(k, 1);
										backResult[m] = Order.number(i + 1) + Order.number(j) + Order.position(k);
										m += 1;
										if (m == backCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							if (m == backCount) {
								break;
							} else {
							}
						}
						backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
						backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
						seatX.remove(backDate);
						seatX.put(backDate, backtrainDate);
						// System.out.println("有找到 標準無要求 ");
					}
				}
				// 商務車廂
				else {
					// 商務靠窗優先
					if (tic.getPrefer().equals("靠窗優先")) {
						if (backCount == 1) {
							// 第6車廂第i排
							for (int i = 1; i <= Order.rowOfSeat(6); i++) {
								// A座位
								if (car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
										.getInt(0) == 0) {
									car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(0,
											1);
									backResult[0] = "06" + Order.number(i) + "A";
									backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
									backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
									seatX.remove(backDate);
									seatX.put(backDate, backtrainDate);
									// System.out.println("有找到 商務靠窗優先 1張");
								}
								// E座位
								else if (car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
										.getInt(4) == 0) {
									car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(4,
											1);
									backResult[0] = "06" + Order.number(i) + "E";
									backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
									backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
									seatX.remove(backDate);
									seatX.put(backDate, backtrainDate);
									// System.out.println("有找到 商務靠窗優先 1張");
								} else {
								}
							}
						}
						// 訂多張
						else {
							// 抓到幾個位置了
							int m = 0;
							// 第6車廂第i排
							for (int i = 0; i <= Order.rowOfSeat(6); i++) {
								// A,B,C,D,E座位
								for (int k = 0; k < 5; k++) {
									if (car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
											.getInt(k) == 0) {
										car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
												.put(k, 1);
										backResult[m] = "06" + Order.number(i) + Order.position(k);
										m += 1;

										if (m == backCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
							backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("有找到 商務靠窗優先 多張");
						}

					}
					// 商務走道優先
					else if (tic.getPrefer().equals("走道優先")) {
						if (backCount == 1) {
							// 第6車廂第i排
							for (int i = 1; i <= Order.rowOfSeat(6); i++) {
								// C座位
								if (car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
										.getInt(2) == 0) {
									car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(2,
											1);
									backResult[0] = "06" + Order.number(i) + "C";
									backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
									backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
									seatX.remove(backDate);
									seatX.put(backDate, backtrainDate);
									// System.out.println("有找到 商務走道優先 1張");
								}
								// D座位
								else if (car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
										.getInt(3) == 0) {
									car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(3,
											1);
									backResult[0] = "06" + Order.number(i) + "D";
									backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
									backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
									seatX.remove(backDate);
									seatX.put(backDate, backtrainDate);
									// System.out.println("有找到 商務走道優先 1張");
								} else {
								}
							}
						}
						// 訂多張
						else {
							// 抓到幾個位置了
							int m = 0;
							// 第6車廂第i排
							for (int i = 0; i <= Order.rowOfSeat(6); i++) {
								// A,B,C,D,E座位
								for (int k = 0; k < 5; k++) {
									if (car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
											.getInt(k) == 0) {
										car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
												.put(k, 1);
										backResult[m] = "06" + Order.number(i) + Order.position(k);
										m += 1;

										if (m == backCount) {
											break;
										} else {
										}
									} else {
									}
								}
								if (m == backCount) {
									break;
								} else {
								}
							}
							backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
							backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
							seatX.remove(backDate);
							seatX.put(backDate, backtrainDate);
							// System.out.println("有找到 商務走道優先 多張");
						}
					}
					// 商務無要求
					else {
						// 抓到幾個位置了
						int m = 0;
						// 第6車廂第i排
						for (int i = 0; i <= Order.rowOfSeat(6); i++) {
							// A,B,C,D,E座位
							for (int k = 0; k < 5; k++) {
								if (car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i))
										.getInt(k) == 0) {
									car2.getJSONObject(5).getJSONObject("seats").getJSONArray(String.valueOf(i)).put(k,
											1);
									backResult[m] = "06" + Order.number(i) + Order.position(k);
									m += 1;

									if (m == backCount) {
										break;
									} else {
									}
								} else {
								}
							}
							if (m == backCount) {
								break;
							} else {
							}
						}
						backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).remove("cars");
						backtrainDate.getJSONObject(r2).getJSONObject(backTrainNo).put("cars", car2);
						seatX.remove(backDate);
						seatX.put(backDate, backtrainDate);
						// System.out.println("有找到 商務無要求");
					}
				}
			}
		} else {

		}
		try (FileWriter file = new FileWriter("C:\\Users\\evanl\\eclipse-workspace\\JSONT\\assets\\seatX.json")) {
			file.write(seatX.toString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int fin;
		if (goCount >= backCount) {
			fin = goCount;
		} else {
			fin = backCount;
		}
		String[][] fi = new String[2][fin];
		fi[0] = goResult;
		fi[1] = backResult;
		return fi;

	}

	// 產生最後付款期限
	public static String expire(Ticket tic, String goDepartureTime) {
		String expire;
		String goDate = tic.getGoDate();
		Calendar date = Order.date(goDate);
		Calendar now = Calendar.getInstance();
		Calendar expireDate;
		// 乘車日三天前
		Calendar three = Order.date(goDate);
		three.add(Calendar.DATE, -2);
		three.set(Calendar.HOUR_OF_DAY, 0);
		three.set(Calendar.MINUTE, 1);
		// 預訂當日乘車票者，最遲應於列車出發前 30 分鐘前完成付款。
		if (now.get(Calendar.DATE) == date.get(Calendar.DATE) && now.get(Calendar.MONTH) == date.get(Calendar.MONTH)) {
			expireDate = Order.date(tic.getGoDate());
			expireDate.set(Calendar.HOUR_OF_DAY, Integer.valueOf(goDepartureTime.substring(0, 2)));
			expireDate.set(Calendar.MINUTE, Integer.valueOf(goDepartureTime.substring(3)));
			expireDate.add(Calendar.MINUTE, -30);
			expire = goDate + "  " + expireDate.get(Calendar.HOUR_OF_DAY) + ":" + expireDate.get(Calendar.MINUTE);

		}
		// 預訂之行程為訂位日起（含）3 日內發車者，至遲應於乘車日之前 1 日完成付款。
		else if (now.compareTo(three) > 0) {
			date.add(Calendar.DATE, -1);
			expire = date.get(Calendar.YEAR) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DATE)
					+ "  " + "23:59";
		} else if (now.compareTo(three) < 0) {
			expire = three.get(Calendar.YEAR) + "/" + (three.get(Calendar.MONTH) + 1) + "/" + three.get(Calendar.DATE)
					+ "  " + "23:59";
		} else {
			expire = "錯了";
		}

		return expire;

	}

	// 價錢
	public static String price(Ticket tic, String goOrBack) {
		String condition;
		String date;
		int count;
		String start;
		double discount;
		String end;
		if (goOrBack.equals("go")) {
			date = tic.getGoDate();
			count = tic.getGoCount();
			start = Order.station(tic.getStart());
			end = Order.station(tic.getEnd());
			discount = tic.getGoDiscount();
		} else {
			date = tic.getBackDate();
			count = tic.getBackCount();
			end = Order.station(tic.getStart());
			start = Order.station(tic.getEnd());
			discount = tic.getBackDiscount();
		}
		if (tic.getCarType().equals("商務")) {
			condition = "business";
		} else if (discount == 1) {
			condition = "standard";
		} else {
			condition = String.valueOf(discount);
		}
		JSONArray price = JSONUtils.getJSONArrayFromFile("/price.json");
		for (int i = 0; i < price.length(); i++) {
			if (price.getJSONObject(i).getString("OriginStationID").equals(start)) {
				JSONArray d = price.getJSONObject(i).getJSONArray("DesrinationStations");
				for (int j = 0; j < d.length(); j++) {
					if (d.getJSONObject(j).getString("ID").equals(end)) {
						JSONArray target = d.getJSONObject(j).getJSONArray("Fares");
						for (int k = 0; k < target.length(); k++) {
							if (target.getJSONObject(k).getString("TicketType").equals(condition)) {
								int p = count * target.getJSONObject(k).getInt("Price");
								return String.valueOf(p);
							}
						}
					}
				}
			} else {
			}
		}
		return "";
	}

	// goOrBack表示現在檢查去程日期還是回程日期
	// 楷威要求我寫的method>///<
	public static String[][] array(Ticket tic, String goOrBack) {
		// 去程日期
		String goDate = tic.getGoDate();
		// 回程日期
		String backDate = tic.getBackDate();
		// 起站
		String start = tic.getStart();
		// 迄站
		String end = tic.getEnd();
		// 這邊檢查的東西還沒弄好
		org.json.JSONArray train = JSONUtils.getJSONArrayFromFile("/timeTable.json");
		// 用來輸出的Array
		String[][] List = new String[train.length()][6];
		// 用來登記符合資格有幾輛車
		int trainNumber = 0;
		// 每次檢查一個車號
		for (int i = 0; i < train.length(); i++) {
			JSONObject obj = train.getJSONObject(i);

			// ----------------底下開始檢查--------------------

			// 找符合條件(起站，迄站，當天有無車次，方向正確)的列車
			if (matchTrain(obj, tic, goOrBack) == true) {
				// 預定時間有在期限內
				if (book(obj, tic, goOrBack) == true) {
					// 判斷這輛車有無剩餘位子
					if (hasSeat(obj, tic, goOrBack) == true) {
						// 列車車號
						String trainNo = obj.getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo")
								.getString("TrainNo");
						// 出發時間
						String departureTime = DepartureTime(obj, tic, goOrBack);
						// 抵達時間
						String arrivalTime = ArrivalTime(obj, tic, goOrBack);
						// 存進Array
						// List[trainNumber][0] = trainNo;
						// List[trainNumber][1] = discount(obj, tic, goOrBack);
						// List[trainNumber][2] = departureTime;
						// List[trainNumber][3] = arrivalTime;
						// List[trainNumber][4] = duration(departureTime, arrivalTime);
						Order.sort(List, trainNo, discount(obj, tic, goOrBack), departureTime, arrivalTime,
								duration(departureTime, arrivalTime), trainNumber);
						trainNumber += 1;
					}
					// 沒有位子就不理
					else {
					}
				}
				// 不在就不要理他
				else {
				}
			}
			// 其他不符合條件的列車7414
			else {
			}
		}
		List[0][5] = String.valueOf(trainNumber);
		return List;

	}

	//

	/*
	 * public static void main(String[] arg) {
	 * 
	 * //創建一個新的ticket ///// String[][] go=Order.array(tic.getGoDate(),
	 * tic.getBackDate(), tic.getStart(), tic.getEnd(), tic.getTicketType(),
	 * tic.getPrefer(), tic.getGoCount(), tic.getBackCount(), tic.getUid(),
	 * tic.getCarType(), "go"); //user要選完列車，然後把去程列車車號輸進tic String goTrainNo; //楷威
	 * String GodepartureTime; //楷威 double goDiscount; tic.setGoTrainNo(goTrainNo);
	 * tic.setGoDiscount(goDiscount); //接下來輸出回程列車表 String[][]
	 * back=Order.array(tic.getGoDate(), tic.getBackDate(), tic.getStart(),
	 * tic.getEnd(), tic.getTicketType(), tic.getPrefer(), tic.getGoCount(),
	 * tic.getBackCount(), tic.getUid(), tic.getCarType(), "back"); String
	 * backTrainNo; //楷威 double backDiscount; //楷威 tic.setBackTrainNo(backTrainNo);
	 * tic.setBackDiscount(backDiscount);
	 * tic.setGoprice(Double.valueOf(Order.price(tic, "go")));
	 * tic.setBackprice(Double.valueOf(Order.price(tic, "back")));
	 * tic.setPrice(tic.getGoprice()+tic.getBackprice());
	 * tic.setExpireDate(Order.expire(tic,GodepartureTime));; String[] goSeat=new
	 * String[tic.getGoCount()]; String[] backSeat; tic.setGoSeats(goSeat);
	 * tic.setBackSeats(backSeat);
	 * 
	 * 
	 * Ticket tic = new Ticket("2021/7/1", "2021/7/1", "台北", "左營", "一般", "無", 1, 1,
	 * "A123", "標準"); tic.setGoDiscount(0.8); tic.setGoTrainNo("0639");
	 * tic.setBackTrainNo("0639"); String[][] hi = Order.seat(tic);
	 * System.out.println(hi[0][0]); System.out.println(hi[1][0]);
	 * 
	 * }
	 * 
	 * // ---------------------------------------------
	 */
}
