package com.tcg.json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;

public class TicketCancel {

	// 判斷輸入的uid跟code存不存在訂票紀錄中，存在的話回傳那筆資料在jsonArray的第幾個index
	public static int Ticket_index(String CODE, String UID) throws Ticket_Exception {
		JSONArray arr = JSONUtils.getJSONArrayFromFile("/booking.json");
		int index = -1;
		int trainIndex = -1;
		String StartTime = "";
		boolean b = false;
		for (int i = 0; i < arr.length(); i++) {
			if (arr.getJSONObject(i).get("code").equals(CODE) && arr.getJSONObject(i).get("uid").equals(UID)) {
				b = true;
				index = i;
			}
		}
		if (b == false) {
			throw new Ticket_Exception(Ticket_Exception.ExceptionTYPE.InvalidCode);
		}

		// 取得發車前半小時的時間
		JSONArray time = JSONUtils.getJSONArrayFromFile("/timeTable.json");
		for (int i = 0; i < time.length(); i++) {
			if (time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").get("TrainNo")
					.equals(arr.getJSONObject(index).getJSONArray("ticketInfo").getJSONObject(0).get("TrainNo"))) {
				trainIndex = i;
			}
		}
		for (int i = 0; i < time.getJSONObject(trainIndex).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
				.length(); i++) {

			if (arr.getJSONObject(index).getJSONArray("ticketInfo").getJSONObject(0).get("start")
					.equals(time.getJSONObject(trainIndex).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
							.getJSONObject(i).get("StationID"))) {
				StartTime = time.getJSONObject(trainIndex).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
						.getJSONObject(i).get("DepartureTime").toString();
			}
		}
		int year = Integer.valueOf(arr.getJSONObject(index).getJSONArray("ticketInfo").getJSONObject(0).get("date")
				.toString().split("/")[0]);
		int month = Integer.valueOf(arr.getJSONObject(index).getJSONArray("ticketInfo").getJSONObject(0).get("date")
				.toString().split("/")[1]);
		int day = Integer.valueOf(arr.getJSONObject(index).getJSONArray("ticketInfo").getJSONObject(0).get("date")
				.toString().split("/")[2]);
		int hour = Integer.valueOf(StartTime.substring(0, 2));
		int min = Integer.valueOf(StartTime.substring(3, 5));
		Calendar last = Calendar.getInstance();
		last.set(year, month, day, hour, min, 0);
		last.add(Calendar.MINUTE, -30);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, 1);

		// 若現在已超過發前半小時，THROW TOOLATE
		if (now.after(last)) {
			throw new Ticket_Exception(Ticket_Exception.ExceptionTYPE.TooLATE);
		}

		return index;

	}

	public static void Cancel(String CODE, String UID) throws Ticket_Exception, IOException {
		JSONArray arr = JSONUtils.getJSONArrayFromFile("/booking.json");
		int index = Ticket_index(CODE, UID);
		if (index != -1) {
			arr.remove(index);
			String book = arr.toString();
			BufferedWriter bw = new BufferedWriter(
					new FileWriter("C:/Users/evanl/eclipse-workspace/JSONT/assets/booking.json"));
			bw.write(book);
			bw.newLine();
			bw.flush();
			bw.close();
		}

	}

	public static void Change(String CODE, String UID, int AmountOfDecrease) throws Ticket_Exception, IOException {
		JSONArray arr = JSONUtils.getJSONArrayFromFile("/booking.json");
		int ti = Ticket_index(CODE, UID);
		if ((arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats").length()
				- AmountOfDecrease) <= 0) {
			throw new Ticket_Exception(Ticket_Exception.ExceptionTYPE.InvalidFixNumber);
		}
		for (int i = (arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats").length()
				- AmountOfDecrease); i < (arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(0)
						.getJSONArray("seats").length()); i++) {
			arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats").remove(i);
			if (arr.getJSONObject(ti).getJSONArray("ticketInfo").length() > 1) {
				arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(1).getJSONArray("seats").remove(i);
			}
			arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(0).remove("ticketsCount");
			arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(0).put("ticketsCount",
					arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats").length());
			arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(1).remove("ticketsCount");
			arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(1).put("ticketsCount",
					arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats").length());

		}
		String book = arr.toString();
		BufferedWriter bw = new BufferedWriter(
				new FileWriter("C:/Users/evanl/eclipse-workspace/JSONT/assets/booking.json"));
		bw.write(book);
		bw.newLine();
		bw.flush();
		bw.close();
		// System.out.println("修改成功，已改成"
		// +
		// arr.getJSONObject(ti).getJSONArray("ticketInfo").getJSONObject(0).getJSONArray("seats").length()
		// + "張");
	}
}
