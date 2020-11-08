package com.capgemini;

import java.time.LocalDate;
import java.util.List;
import java.time.Duration;
import java.time.Instant;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import com.capgemini.EmpPayrollService.IOService;

public class EmployeePayrollDBTest {

	@Test
	public void givenEmployeePayrollDB_shouldReturnCount() throws EmpPayrollException {
		EmpPayrollService empPayRollService = new EmpPayrollService();
		List<EmployeePayrollData> empPayrollList = empPayRollService.readEmpPayrollData(IOService.DB_IO);
		Assert.assertEquals(8, empPayrollList.size());
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
	
	
	@Test
 	public void givenMultipleEmployee_WhenAdded_ShouldMatchEntries() throws EmpPayrollException {
 	   List<String> deptList = new ArrayList<>();
 	   deptList.add("Sales");
 		EmployeePayrollData[] arrOfEmps = {
 				new EmployeePayrollData(0, "Jeff", 500.0, LocalDate.now(), "M", deptList),
 				new EmployeePayrollData(0, "Charlie", 400.0, LocalDate.now(), "M", deptList),
 				new EmployeePayrollData(0, "Tom", 300.0, LocalDate.now(), "M", deptList)
 		};
 		EmpPayrollService empPayRollService = new EmpPayrollService();
 		empPayRollService.readEmpPayrollData(IOService.DB_IO);
 		Instant start = Instant.now();
 		empPayRollService.addEmpToPayroll(Arrays.asList(arrOfEmps));
 		Instant end = Instant.now();
 		System.out.println("Duration without Thread : "+ Duration.between(start, end));
 		Assert.assertEquals(4, empPayRollService.countEntries(IOService.DB_IO));
 	}
}