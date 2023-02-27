package com.tcg.json;

public class Ticket_Exception extends Exception {

	private ExceptionTYPE excptionCondition;

//exception有兩種, uid跟code不正確 退票、修改時間太晚
	public enum ExceptionTYPE {
		InvalidFixNumber, InvalidCode, TooLATE
	}

	public Ticket_Exception(ExceptionTYPE ex_type) {
		if (ex_type.equals(ExceptionTYPE.InvalidFixNumber)) {
			excptionCondition = ExceptionTYPE.InvalidFixNumber;
		}
		if (ex_type.equals(ExceptionTYPE.InvalidCode)) {
			excptionCondition = ExceptionTYPE.InvalidCode;
		}
		if (ex_type.equals(ExceptionTYPE.TooLATE)) {
			excptionCondition = ExceptionTYPE.TooLATE;
		}
	}

	public String getMessage() {
		if (excptionCondition == ExceptionTYPE.InvalidCode) {
			return "輸入uid、code錯誤";
		}
		if (excptionCondition == ExceptionTYPE.InvalidFixNumber) {
			return "您退票張數已超過原訂票張數";
		} else {
			return "已超過退票、修改期限";
		}
	}
}