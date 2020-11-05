package com.capgemini;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeePayrollDBService {
	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?allowPublicKeyRetrieval=true&&useSSL=false";
		String userName = "root";
		String password = "Shreya@57";
		Connection connection;
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		return connection;
	}
	
	public List<EmployeePayrollData> readData() throws EmpPayrollException{
		String sql = "SELECT * FROM employee_data;";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try(Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				employeePayrollList.add(new EmployeePayrollData(id,name,salary));
			}
		}catch(SQLException e) {
			throw new EmpPayrollException(EmpPayrollException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
		return employeePayrollList;
	}
	

 	public int updateEmployeeData(String name, double salary) throws EmpPayrollException {
 		return this.updateEmployeeDataUsingStatement(name, salary);
 	}

 	private int updateEmployeeDataUsingStatement(String name, double salary) throws EmpPayrollException {
 		String sql = String.format("update employee_data set salary = %.2f where name ='%s';", salary, name);
 		try(Connection connection = this.getConnection()){
 			Statement statement = connection.createStatement();
 			return statement.executeUpdate(sql);
 		}catch(SQLException e) {
 			throw new EmpPayrollException(EmpPayrollException.ExceptionType.CONNECTION_ERROR, e.getMessage());
 		}
 	}

 	public EmployeePayrollData getEmployeePayrollData(String name) throws EmpPayrollException {
 			List<EmployeePayrollData> employeePayrollList = this.readData();
 			EmployeePayrollData employeeData = employeePayrollList.stream()
 					.filter(employee -> employee.getName().contentEquals(name))
 					.findFirst()
 					.orElse(null);
 			return employeeData;


 		}
}