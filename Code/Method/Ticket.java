package com.tcg.json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

//用來儲存票的資訊
public class Ticket {
	// id type
	private String code;
	private String uid;// ok
	private String PhoneNumber = "0";
	private String tradingStatus = "Unpaid";

	// ticket info
	private String ticketType;// ok
	private int GoCount;// ok
	private int BackCount;// ok

	private double goDiscount;// ok
	private double backDiscount;
	private double price;// ok
	private double goprice;// ok

	private double backprice;// ok
	private String expireDate;// ok
	private String[] goSeats;
	private String[] backSeats;

	private String prefer;// ok
	private String carType;// ok

	// train info
	private String goTrainNo;// ok
	private String backTrainNo;// ok
	private String goDate;// ok
	private String backDate;// ok
	private String start;// ok
	private String end;// ok

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

	// 耕傑寫的
	public void WriteTicketIntoBooking() throws IOException {
		JSONArray booking = JSONUtils.getJSONArrayFromFile("/booking.json");
		JSONArray time = JSONUtils.getJSONArrayFromFile("/timeTable.json");
		// 找 times
		String goDepartureTime = "";
		String goArriveTime = "";
		String backDepartureTime = "";
		String backArriveTime = "";
		for (int i = 0; i < time.length(); i++) {
			if (time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").get("TrainNo")
					.equals(goTrainNo)) {
				for (int j = 0; j < time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
						.length(); j++) {
					if (time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
							.getJSONObject(j).get("StationID").equals(start)) {
						goDepartureTime = time.getJSONObject(i).getJSONObject("GeneralTimetable")
								.getJSONArray("StopTimes").getJSONObject(j).get("DepartureTime").toString();
					}
					if (time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
							.getJSONObject(j).get("StationID").equals(end)) {
						goArriveTime = time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
								.getJSONObject(j).get("DepartureTime").toString();
					}
				}
			}
		}
		for (int i = 0; i < time.length(); i++) {
			if (time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").get("TrainNo")
					.equals(backTrainNo)) {
				for (int j = 0; j < time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
						.length(); j++) {
					if (time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
							.getJSONObject(j).get("StationID").equals(start)) {
						backArriveTime = time.getJSONObject(i).getJSONObject("GeneralTimetable")
								.getJSONArray("StopTimes").getJSONObject(j).get("DepartureTime").toString();
					}
					if (time.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes")
							.getJSONObject(j).get("StationID").equals(end)) {
						backDepartureTime = time.getJSONObject(i).getJSONObject("GeneralTimetable")
								.getJSONArray("StopTimes").getJSONObject(j).get("DepartureTime").toString();
					}
				}
			}
		}

		// 輸入JSON
		org.json.JSONObject to = new JSONObject();
		org.json.simple.JSONObject goInfo = new org.json.simple.JSONObject();
		org.json.simple.JSONObject backInfo = new org.json.simple.JSONObject();
		org.json.simple.JSONArray ticketInfo = new org.json.simple.JSONArray();
		org.json.simple.JSONArray JgoSeats = new org.json.simple.JSONArray();
		org.json.simple.JSONArray JbackSeats = new org.json.simple.JSONArray();
		for (int i = 0; i < goSeats.length; i++) {
			JgoSeats.add(i, goSeats[i]);
		}
		to.put("code", code);
		to.put("uid", uid);
		to.put("Phone", PhoneNumber);
		to.put("trading status", tradingStatus);
		to.put("carType", carType);
		goInfo.put("date", goDate);
		goInfo.put("ticketsType", ticketType);
		goInfo.put("ticketsCount", GoCount);
		goInfo.put("start", start);
		goInfo.put("end", end);
		goInfo.put("seats", JgoSeats);
		goInfo.put("TrainNo", goTrainNo);
		goInfo.put("departure time", goDepartureTime);
		goInfo.put("arrival time", goArriveTime);
		goInfo.put("price", goprice);
		goInfo.put("Driving time", duration(goDepartureTime, goArriveTime));
		ticketInfo.add(0, goInfo);
		if (BackCount != 0) {
			for (int i = 0; i < backSeats.length; i++) {
				JbackSeats.add(backSeats[i]);
			}
			backInfo.put("date", backDate);
			backInfo.put("ticketsType", ticketType);
			backInfo.put("ticketsCount", BackCount);
			backInfo.put("start", end);
			backInfo.put("end", start);
			backInfo.put("seats", JbackSeats);
			backInfo.put("TrainNo", backTrainNo);
			backInfo.put("departure time", backDepartureTime);
			backInfo.put("arrival time", backArriveTime);
			backInfo.put("price", backprice);
			backInfo.put("Driving time", duration(backDepartureTime, backArriveTime));
			ticketInfo.add(1, backInfo);
		}
		to.put("ticketInfo", ticketInfo);
		to.put("payDeadline", expireDate);
		to.put("payment", price);
		booking.put(to);

		String book = booking.toString();
		BufferedWriter bw = new BufferedWriter(
				new FileWriter("C:/Users/evanl/eclipse-workspace/JSONT/assets/booking.json"));
		bw.write(book);
		bw.newLine();
		bw.flush();
		bw.close();

	}

	public Ticket() {
	}

	public Ticket(String goTime, String backTime, String start, String end, String ticketType, String prefer,
			int goCount, int backCount, String Uid, String carType) {
		this.setGoDate(goTime);
		this.setBackDate(backTime);
		this.setStart(start);
		this.setEnd(end);
		this.setTicketType(ticketType);
		this.setPrefer(prefer);
		this.setGoCount(goCount);
		this.setBackCount(backCount);
		this.setUid(Uid);
		this.setCarType(carType);
	}

	public void SetVariable(int i, String a) {

		if (i == 0) {
			this.setGoDate(a);
		} else if (i == 1) {
			this.setStart(a);
		} else if (i == 2) {
			this.setEnd(a);
		} else if (i == 3) {
			this.setTicketType(a);
		} else if (i == 4) {
			this.setPrefer(a);
		} else if (i == 7) {
			this.setUid(a);
		} else if (i == 8) {
			this.setCarType(a);

		}
	}

	public void SetVariable(int i, int a) {
		if (i == 5) {
			this.setGoCount(a);
		} else if (i == 6) {
			this.setBackCount(a);
		} else {

		}
	}

	// getter, setter
	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getTradingStatus() {
		return tradingStatus;
	}

	public void setTradingStatus(String tradingStatus) {
		this.tradingStatus = tradingStatus;
	}

	public double getGoprice() {
		return goprice;
	}

	public void setGoprice(double goprice) {
		this.goprice = goprice;
	}

	public double getBackprice() {
		return backprice;
	}

	public void setBackprice(double backprice) {
		this.backprice = backprice;
	}

	public String[] getBackSeats() {
		return backSeats;
	}

	public void setBackSeats(String[] backSeats) {
		this.backSeats = backSeats;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public int getBackCount() {
		return BackCount;
	}

	public void setBackCount(int backCount) {
		BackCount = backCount;
	}

	public String getPrefer() {
		return prefer;
	}

	public void setPrefer(String prefer) {
		this.prefer = prefer;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGoDate() {
		return goDate;
	}

	public void setGoDate(String date) {
		this.goDate = date;
	}

	public String getBackDate() {
		return backDate;
	}

	public void setBackDate(String date) {
		this.backDate = date;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public int getGoCount() {
		return GoCount;
	}

	public void setGoCount(int ticketsCount) {
		this.GoCount = ticketsCount;
	}

	public String[] getGoSeats() {
		return goSeats;
	}

	public void setGoSeats(String[] seats) {
		this.goSeats = seats;
	}

	public String getGoTrainNo() {
		return goTrainNo;
	}

	public void setGoTrainNo(String trainNo) {
		this.goTrainNo = trainNo;
	}

	public String getBackTrainNo() {
		return backTrainNo;
	}

	public void setBackTrainNo(String trainNo) {
		this.backTrainNo = trainNo;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getGoDiscount() {
		return goDiscount;
	}

	public void setGoDiscount(double goDiscount) {
		this.goDiscount = goDiscount;
	}

	public double getBackDiscount() {
		return backDiscount;
	}

	public void setBackDiscount(double backDiscount) {
		this.backDiscount = backDiscount;
	}

}
