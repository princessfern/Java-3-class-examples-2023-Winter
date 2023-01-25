package ca.nl.cna.java3.jdbc;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Loading in objects from a database using an old school approach.
 * Note: usually objects are loaded from databases using ORMs. The issue with the approach below
 * is that the database and the object are very tightly coupled.
 *
 * @author Josh
 */
public class FunWithLoadingEmployees {

    public static void main(String[] args) {
        //The list to store the employees
        LinkedList<Employee> employeeLinkedList = new LinkedList<>();

        //Connect to the database and load in the employees
        try{
            Connection connection = DriverManager.getConnection(DBConfiguration.DB_URL + DBConfiguration.DB_EMPLOYEE_DB_NAME, DBConfiguration.DB_USER, DBConfiguration.DB_PASSWORD);
            //method defined statically below. Can be used in any other example
            employeeLinkedList = loadEmployeesFromDB(connection);
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();;
        }

        employeeLinkedList.forEach(employee -> employee.printEmployeeDetails(System.out));

    }

    /**
     * Load in all the employees into a LinkedList. Note: this will not scale. Never do this if you have >1000 employees
     * @param connection connection to EmployeeDB
     * @return linkedList of ALL employees (warning, bad form, use a select parameter to reduce size)
     */
    public static LinkedList<Employee> loadEmployeesFromDB(Connection connection){
        LinkedList<Employee> employeeLinkedList = new LinkedList<>();
        try{
            Statement statement = connection.createStatement();
            String sql = "SELECT id, first, last, age FROM Employees";
            ResultSet resultSet = statement.executeQuery(sql);

            //Create the employee objects from the result set and add them to the list
            while(resultSet.next()){
                employeeLinkedList.add(
                        new Employee(
                            resultSet.getString(DBConfiguration.DB_EMPLOYEE_COL_FIRST_NAME),
                            resultSet.getString(DBConfiguration.DB_EMPLOYEE_COL_LAST_NAME),
                            resultSet.getInt(DBConfiguration.DB_EMPLOYEE_COL_AGE),
                            resultSet.getInt(DBConfiguration.DB_EMPLOYEE_COL_ID))
                );
            }
            statement.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();;
        }

        return employeeLinkedList;
    }


}
