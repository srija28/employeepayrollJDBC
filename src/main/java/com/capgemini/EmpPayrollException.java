package com.capgemini;

public class EmpPayrollException extends Exception{

	enum ExceptionType{
		CONNECTION_ERROR, INCORRECT_INFO
	}

	ExceptionType type;
	
	public EmpPayrollException(ExceptionType type, String message) {
		super(message);
		this.type = type;
	}
}