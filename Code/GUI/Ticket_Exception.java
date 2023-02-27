package com.tcg.json;

public class Ticket_Exception extends Exception {

	private ExceptionTYPE excptionCondition;

//exception�����, uid��code�����T �h���B�ק�ɶ��ӱ�
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
			return "��Juid�Bcode���~";
		}
		if (excptionCondition == ExceptionTYPE.InvalidFixNumber) {
			return "�z�h���i�Ƥw�W�L��q���i��";
		} else {
			return "�w�W�L�h���B�ק����";
		}
	}
}