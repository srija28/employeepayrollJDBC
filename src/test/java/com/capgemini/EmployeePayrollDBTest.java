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
	
	@Test
 	public void givenNewSalary_whenUpdatedShouldMatch() throws EmpPayrollException {
 		EmpPayrollService empPayRollService = new EmpPayrollService();
 		List<EmployeePayrollData> empPayrollList = empPayRollService.readEmpPayrollData(IOService.DB_IO);
 		empPayRollService.updateEmployeeSalary("Shreya Reddy", 3000000.00);
 		boolean result = empPayRollService.checkEmployeePayrollInSyncWithDB("Shreya Reddy");
 		Assert.assertTrue(result);
 	}
	
	@Test
 	public void givenDBFindSumOfSalaryOfMale_shouldReturnSum() throws EmpPayrollException {
 		EmpPayrollService empPayRollService = new EmpPayrollService();
 		double sum = empPayRollService.getSumByGender(IOService.DB_IO,"M");
 		double sum1 = empPayRollService.getEmpDataGroupedByGender(IOService.DB_IO, "salary", "SUM","M");
 		Assert.assertTrue(sum == sum1);
 	}
}