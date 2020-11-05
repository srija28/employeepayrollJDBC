package com.capgemini;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.capgemini.EmpPayrollService.IOService;

public class EmployeePayrollDBTest {

	@Test
	public void givenEmployeePayrollDB_shouldReturnCount() throws EmpPayrollException {
		EmpPayrollService empPayRollService = new EmpPayrollService();
		List<EmployeePayrollData> empPayrollList = empPayRollService.readEmpPayrollData(IOService.DB_IO);
		Assert.assertEquals(3, empPayrollList.size());
	}
}