package com.thresholdsoft.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.thresholdsoft.util.DbUtil;
import com.thresholdsoft.util.ValidationUtil;

/**
 * This Main class contains service method to provide the services to the User
 * 
 * @author uday
 * @since 06-09-2019
 */
public class Main {
	private static Scanner scanner = new Scanner(System.in);
	private Connection connection;
	private String sql;
	
	private PreparedStatement prepareStmt;
	private Integer count;
	private Integer id;
	private String name;
	private String address;
	private String email;
	private String phone;
	private String branch;
	private String role;
	private Timestamp createdTime;
	private Timestamp modifiedTime;
	private Integer createdBy;
	private Integer modifiedBy;
	private boolean isDeleted;
	Date date;
	private ResultSet resultSet;
	User user = new User();

	public static void main(String[] args)
			throws ClassNotFoundException, SQLException, NumberFormatException, IOException {
		Main main = new Main();
		main.service();
	}

	/**
	 * this method contains switch case and it have many operations mainly create by
	 * Object and create by file update delete modify
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void service() throws ClassNotFoundException, SQLException, NumberFormatException, IOException {
		System.out.println("1.createByObject" + "\n" + "2.createByFile" + "\n" + "3.update" + "\n" + "4.getSingle"
				+ "\n" + "5.getAll" + "\n" + "6.delete" + "\n" + "7.findRecord");
		System.out.println("please provide Operation Type number : ");
		String operation = scanner.nextLine();
		while (ValidationUtil.isValidInput(operation)) {
			System.out.println("Please provide correct Operation : ");
			operation = scanner.nextLine();
		}
		switch (operation) { // switch case for operations
		case "1":
			createByObject();
			break;
		case "2":
			System.out.println("Enter File Path");
			String filePath = scanner.nextLine();
			while (ValidationUtil.isBlank(filePath)) {
				System.out.println("Please provide correct filePath : ");
				filePath = scanner.nextLine();
			}
			createByFile(filePath);
			break;
		case "3":
			update();
			break;
		case "4":
			getSingle();
			break;
		case "5":
			getAll();
			break;
		case "6":
			delete();
			break;
		case "7":
			findRecord();
			break;
		default:
			System.out.println(
					"there is no operation type please re run the application and provide mention input only!!!");
		}
	}

	/**
	 * This method used for creating user using User Object
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private void createByObject() throws SQLException, ClassNotFoundException {
		user = ValidateAndGetValues();
		if (findDuplicate(phone)) {
			System.out.println("record is already exist please insert another record or change phone number!!!!");
			return;
		} else {
			connection = DbUtil.getConnection();
			sql = "INSERT INTO db1.user(name, address, email, phone, branch, role, created_time, created_by, modified_time, modified_by) VALUES(?,?,?,?,?,?,?,?,?,?)";
			prepareStmt = connection.prepareStatement(sql);
			prepareStmt.setString(1, user.getName());
			prepareStmt.setString(2, user.getAddress());
			prepareStmt.setString(3, user.getEmail());
			prepareStmt.setString(4, user.getPhone());
			prepareStmt.setString(5, user.getBranch());
			prepareStmt.setString(6, user.getRole());
			prepareStmt.setTimestamp(7, new Timestamp(user.getCreatedTime().getTime()));
			prepareStmt.setInt(8, user.getCreatedBy());
			prepareStmt.setTimestamp(9, new Timestamp(user.getModifiedTime().getTime()));
			prepareStmt.setInt(10, user.getModifiedBy());
			count = 0;
			count = prepareStmt.executeUpdate();
			DbUtil.closeConnection(connection);
			if (count > 0) {
				System.out.println("Inserted successfully inserted!!!!");
			} else {
				System.out.println("Not Inserted successfully!!!");
			}
		}
	}

	@SuppressWarnings("resource")
	private void createByFile(String filePath) throws ClassNotFoundException, SQLException, IOException {
		List<User> users = new ArrayList<User>(); // list to add the users
		BufferedReader bufferReader = null;
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		bufferReader = new BufferedReader(fileReader);
		String line = "";
		bufferReader.readLine(); // if want skip first line
		while ((line = bufferReader.readLine()) != null) {
			String[] fields = line.split(","); // split the word by using specified delimiter and store into array
			if (fields.length > 0) { // Retrieve data from array one by one and store into variables
				name = fields[0];
				if (ValidationUtil.isValidName(name)) {
					System.out.println(
							"please provide correct name only alphabets in csv file and re-run the application !!!!");
					return;
				}
				address = fields[1];
				if (ValidationUtil.isValidAddress(address)) {
					System.out.println("please provide correct address in csv file and re-run the application !!!!");
					return;
				}
				email = fields[2];
				if (ValidationUtil.isValidEmail(email)) {
					System.out.println("please provide valid email in csv file and re-run the application !!!! ");
					return;
				}
				phone = fields[3];
				if (ValidationUtil.isValidPhone(phone)) {
					System.out.println("please correct phone number in csv file and re-run the application !!!!");
					return;
				}
				branch = fields[4];
				if (ValidationUtil.isValidAddress(address)) {
					System.out.println("please provide correct branch in csv file and re-run the application !!!!");
					return;
				}
				role = fields[5];
				if (ValidationUtil.isValidRole(role)) {
					System.out.println("please provide correct role in csv file and re-run the application !!!!!");
					return;
				}
				String creator = fields[6];
				if (ValidationUtil.isValidInput(creator)) {
					System.out.println(
							"please provide correct Changer int format only in csv file and re-run the application !!!!!");
					return;
				}
				createdBy = Integer.parseInt(creator);// parse the value
				String changer = fields[7];
				if (ValidationUtil.isValidInput(changer)) {
					System.out.println(
							"please provide correct modifier int format only in csv file and re-run the application !!!!!");
					return;
				}
				modifiedBy = Integer.parseInt(changer);// parse the value
			}
			if (findDuplicate(phone)) {
				System.out.println("record is exist please insert another record or change phone number!!!!");
				continue;
			} else {
				createdTime = new Timestamp(new Date().getTime());
				modifiedTime = new Timestamp(new Date().getTime());
				users.add(user);
				connection = DbUtil.getConnection(); // get the connection
				sql = "INSERT INTO user(name, address, email, phone, branch, role, created_time, created_by, modified_time, modified_by) VALUES(?,?,?,?,?,?,?,?,?,?)";
				prepareStmt = connection.prepareStatement(sql);
				prepareStmt.setString(1, name);
				prepareStmt.setString(2, address);
				prepareStmt.setString(3, email);
				prepareStmt.setString(4, phone);
				prepareStmt.setString(5, branch);
				prepareStmt.setString(6, role);
				prepareStmt.setTimestamp(7, createdTime);
				prepareStmt.setInt(8, createdBy);
				prepareStmt.setTimestamp(9, modifiedTime);
				prepareStmt.setInt(10, modifiedBy);
				count = 0;
				count = prepareStmt.executeUpdate();
				DbUtil.closeConnection(connection);
				if (count > 0) {
					System.out.println("record successfully inserted!!!");
				} else {
					System.out.println("record not successfully inserted!!");
				}
			}
		}
		bufferReader.close();
	}

	/*
	 * catch (Exception e) { e.printStackTrace(); //
	 * System.out.println("enter correct file path!!!!"); }
	 */

	private void getSingle() throws ClassNotFoundException, SQLException {
		count = 0; // count varibale for exist record
		if (!findRecord()) {
			System.out.println("Record not existed!!!!");
		} else {
			connection = DbUtil.getConnection();
			sql = "SELECT name, address, email, phone, branch, role, created_time, created_by, modified_time, modified_by, is_deleted FROM user where id = ? ";
			isDeleted = false;
			prepareStmt = connection.prepareStatement(sql);
			prepareStmt.setInt(1, id);
			// prepareStmt.setBoolean(2, isDeleted);
			resultSet = prepareStmt.executeQuery();
			while (resultSet.next()) { // iterate the resultset and get the value
				name = resultSet.getString("name");
				address = resultSet.getString("address");
				email = resultSet.getString("email");
				phone = resultSet.getString("phone");
				branch = resultSet.getString("branch");
				role = resultSet.getString("role");
				createdTime = resultSet.getTimestamp("created_time");
				createdBy = resultSet.getInt("created_by");
				modifiedTime = resultSet.getTimestamp("modified_time");
				modifiedBy = resultSet.getInt("modified_by");
				isDeleted = resultSet.getBoolean("is_deleted");
				DbUtil.closeConnection(connection);
				// priint the headings
				System.out.println("NAME " + "\t" + "ADDRESS " + "\t" + " EMAIL " + "\t" + "\t" + " PHONE_NUMBER "
						+ "\t" + " BRANCH " + "\t" + " ROLE " + "\t" + "CREATED_TIME " + "\t" + " CREATED_BY " + "\t"
						+ " MODIFIED_TIME " + "\t" + " MODIFIED_BY " + "\t" + " IS_DELETED " + "\t");
				System.out.println(
						"-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				// print the values
				System.out.println(name + "\t" + address + "\t" + email + "\t" + phone + "\t" + branch + "\t" + role
						+ "\t" + createdTime + "\t" + createdBy + "\t" + modifiedTime + "\t" + modifiedBy + "\t" + "\t"
						+ isDeleted + "\t");
			}
		}
	}

	private void getAll() throws ClassNotFoundException, SQLException {
		connection = DbUtil.getConnection(); // get the connection
		sql = "SELECT id,name, address, email, phone, branch, role, created_time, created_by, modified_time, modified_by, is_deleted from user WHERE is_deleted = ?";
		isDeleted = false;
		prepareStmt = connection.prepareStatement(sql);
		prepareStmt.setBoolean(1, isDeleted);
		resultSet = prepareStmt.executeQuery();
		DbUtil.closeConnection(connection);
		System.out.println(" ID " + "\t" + " NAME " + "\t" + " ADDRESS " + "\t" + " EMAIL " + "\t" + "\t"
				+ " PHONE_NUMBER " + "\t" + " BRANCH " + "\t" + " ROLE " + "\t" + " CREATED_TIME " + "\t"
				+ " CREATED_BY " + "\t" + " MODIFIED_TIME " + "\t" + " MODIFIED_BY " + "\t" + " IS_DELETED " + "\t");
		System.out.println(
				"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		while (resultSet.next()) { // Retrieve the records one by one from resultset object and printing the data
			id = resultSet.getInt("id");
			name = resultSet.getString("name");
			address = resultSet.getString("address");
			email = resultSet.getString("email");
			phone = resultSet.getString("phone");
			branch = resultSet.getString("branch");
			role = resultSet.getString("role");
			createdTime = resultSet.getTimestamp("created_time");
			createdBy = resultSet.getInt("created_by");
			modifiedTime = resultSet.getTimestamp("modified_time");
			modifiedBy = resultSet.getInt("modified_by");
			isDeleted = resultSet.getBoolean("is_deleted");
			System.out.println(id + "\t" + name + "\t" + address + "\t" + email + "\t" + phone + "\t " + branch + "\t"
					+ role + "\t" + createdTime + "\t" + createdBy + "\t" + modifiedTime + "\t" + modifiedBy + "\t"
					+ "\t" + isDeleted + "\t");

		}
	}

	private void update() throws SQLException, ClassNotFoundException {
		count = 0; // count varibale for fing the record is exit or not based on that loop will
		// executive
		if (!findRecord()) {
			user = ValidateAndGetValues();
			isDeleted = false;
			connection = DbUtil.getConnection(); // get the connection
			sql = "UPDATE user SET name=?, address=?, email=?, phone=?, branch=?, role=?, modified_time=?, modified_by=? WHERE is_deleted = ? and id = ? ";
			prepareStmt = connection.prepareStatement(sql);
			prepareStmt.setString(1, user.getName());
			prepareStmt.setString(2, user.getAddress());
			prepareStmt.setString(3, user.getEmail());
			prepareStmt.setString(4, user.getPhone());
			prepareStmt.setString(5, user.getBranch());
			prepareStmt.setString(6, user.getRole());
			prepareStmt.setTimestamp(7, new Timestamp(user.getModifiedTime().getTime()));
			prepareStmt.setInt(8, user.getModifiedBy());
			prepareStmt.setBoolean(9, isDeleted);
			prepareStmt.setInt(10, id);
			count = 0;
			count = prepareStmt.executeUpdate();
			DbUtil.closeConnection(connection);
			if (count > 0) {
				System.out.println("updated successfully!!!");
			} else {
				System.out.println("not updated!!!");
			}
		}
	}

	private void delete() throws ClassNotFoundException, SQLException {
		count = 0; // check record exist or not
		if (findRecord()) {
			isDeleted = true;
			connection = DbUtil.getConnection(); // get the connection
			sql = "UPDATE user SET modified_time = ?, is_deleted = ? WHERE id = ? ";
			prepareStmt = connection.prepareStatement(sql);
			prepareStmt.setTimestamp(1, new Timestamp(new Date().getTime()));
			prepareStmt.setBoolean(2, isDeleted);
			prepareStmt.setInt(3, id);
			connection.setAutoCommit(true);
			count = prepareStmt.executeUpdate();
			DbUtil.closeConnection(connection);
			if (count > 0) {
				System.out.println("Delete successfully!!!");
			} else {
				System.out.println("not Deleted!!!");
			}
		}
	}

	private Boolean findDuplicate(String phone) throws ClassNotFoundException, SQLException {
		count = 0;
		Boolean result = false;
		connection = DbUtil.getConnection();
		sql = "SELECT COUNT(*) as `count` FROM user WHERE phone = ? ";
		prepareStmt = connection.prepareStatement(sql);
		prepareStmt.setString(1, phone);
		result = prepareStmt.execute();
		DbUtil.closeConnection(connection);
		// count = resultSet.getInt("count");
		/*
		 * if (count > 0) { System.out.println("we find the record!! : " + result); }
		 * else { System.out.println("not found the record!!! " + result); }
		 */
		return result;
	}

	private Boolean findRecord() throws ClassNotFoundException, SQLException {
		boolean result = false;
		// for find record if exist return 1 else return 0.
		System.out.println("please enter valid id : ");
		String isValidId = scanner.nextLine();
		while (ValidationUtil.isValidInput(isValidId)) {
			System.out.println("Please provide input which as not deleted record Id and int type only!!!!!: ");
			isValidId = scanner.nextLine();
		}
		id = Integer.parseInt(isValidId);
		isDeleted = false;
		connection = DbUtil.getConnection();
		sql = "SELECT COUNT(*) as `count` FROM user WHERE id = ? and is_deleted = ? ";
		prepareStmt = connection.prepareStatement(sql);
		prepareStmt.setInt(1, id);
		prepareStmt.setBoolean(2, isDeleted);
		result = prepareStmt.execute();
		DbUtil.closeConnection(connection);
		return result;
	}

	public User ValidateAndGetValues() {
		System.out.println("please enter name  : ");
		name = scanner.nextLine();
		while (ValidationUtil.isValidName(name)) {
			System.out.println("Please provide correct name and only provide alphabets : ");
			name = scanner.nextLine();
		}
		user.setName(name);
		System.out.println("please enter User Address  : ");
		address = scanner.nextLine();
		while (ValidationUtil.isValidAddress(address)) {
			System.out.println("Please provide correct address Address : ");
			address = scanner.nextLine();
		}
		user.setAddress(address);
		System.out.println("Please Enter email  : ");
		email = scanner.nextLine();
		while (ValidationUtil.isValidEmail(email)) {
			System.out.println("Please provide correct email must be matched email pattern : ");
			email = scanner.nextLine();
		}
		user.setEmail(email);
		System.out.println("please enter phone number  : ");
		phone = scanner.nextLine();
		while (ValidationUtil.isValidPhone(phone)) {
			System.out.println("Please provide correct Phone number must be used numbers only : ");
			phone = scanner.nextLine();
		}
		user.setPhone(phone);
		System.out.println("please enter Branch  : ");
		branch = scanner.nextLine();
		while (ValidationUtil.isValidAddress(branch)) {
			System.out.println("Please provide correct branch : ");
			branch = scanner.nextLine();
		}
		user.setBranch(branch);
		System.out.println("please Enter Role existed role : ");
		role = scanner.nextLine();
		while (ValidationUtil.isValidRole(role)) {
			System.out.println("Please provide correct Role and which must be existed role : ");
			role = scanner.nextLine();
		}
		user.setRole(role);
		date = new Date();
		user.setCreatedTime(date);
		System.out.println("please enter who is creating  : ");
		String creator = scanner.nextLine();
		while (ValidationUtil.isValidInput(creator)) {
			System.out.println("please give correct input  who is creating ");
			creator = scanner.nextLine();
		}
		createdBy = Integer.parseInt(creator);
		user.setCreatedBy(createdBy);
		System.out.println("please enter Who is modyfing : ");
		user.setModifiedTime(date);
		String modifyBy = scanner.nextLine();
		while (ValidationUtil.isValidInput(modifyBy)) {
			System.out.println("Please provide correct modify person and int type only: ");
			modifyBy = scanner.nextLine();
		}
		modifiedBy = Integer.parseInt(modifyBy);
		user.setModifiedBy(modifiedBy);
		return user;

	}

}
