package com.capgemini;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

public class EmployeePayrollDBService {
	
	private static EmployeePayrollDBService employeePayrollDBService;
	private PreparedStatement employeePayrollDataStatement;
	
	private EmployeePayrollDBService() {
	}
	
	public static EmployeePayrollDBService getInstance() {
		if(employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}

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
			employeePayrollList = getEmployeePayrollData(result);
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

	public List<EmployeePayrollData> getEmployeePayrollData(String name) throws EmpPayrollException{
		List<EmployeePayrollData> employeePayrollList = null;
		if(this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		}catch(SQLException e) {
			throw new EmpPayrollException(EmpPayrollException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
		return 	employeePayrollList;
		}
	
	private void prepareStatementForEmployeeData() throws EmpPayrollException {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT * FROM employee_data WHERE name = ?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		}catch(SQLException e) {
			throw new EmpPayrollException(EmpPayrollException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet result) throws EmpPayrollException{
		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		try {
			while(result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				String gender = result.getString("gender");
				employeePayrollList.add(new EmployeePayrollData(id,name,salary, startDate,gender));
				
		}
		}catch(SQLException e) {
			throw new EmpPayrollException(EmpPayrollException.ExceptionType.CONNECTION_ERROR, e.getMessage());
		}
		return employeePayrollList;
	}
	public double getSumByGender(String c) throws EmpPayrollException {
 		List<EmployeePayrollData> employeePayrollList = this.readData();
 		double sum = 0.0;
 		List<EmployeePayrollData> sortByGenderList = employeePayrollList.stream()
 				                            .filter(employee -> employee.getGender().equals(c))
 				                            .collect(Collectors.toList());
 		sum = sortByGenderList.stream()
 				                           .map(employee -> employee.getSalary()).reduce(0.0, Double::sum);
 		return sum;
 	}


 	public double getEmpDataGroupedByGender(String column, String operation, String gender) throws EmpPayrollException {

 		Map<String, Double> sumByGenderMap = new HashMap<>();
 		String sql = String.format("SELECT gender, %s(%s) FROM employee_data GROUP BY gender;", operation, column);
 		try (Connection connection = getConnection()) {
 			PreparedStatement preparedStatement = connection.prepareStatement(sql);
 			ResultSet resultSet = preparedStatement.executeQuery();
 			while (resultSet.next()) {
 				sumByGenderMap.put(resultSet.getString(1), resultSet.getDouble(2));
 			}
 		} catch (SQLException e) {
 			throw new EmpPayrollException(EmpPayrollException.ExceptionType.INCORRECT_INFO, e.getMessage());
 		}
 		if(gender.equals("M")) {
 		return sumByGenderMap.get("M");
 		}
 		return sumByGenderMap.get("F");
 	}
 }
