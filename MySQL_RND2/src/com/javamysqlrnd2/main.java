package com.javamysqlrnd2;
import java.sql.*;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
	
	// MySQL Connection string
	private static String connectionString = "jdbc:mysql://localhost:3306/hw04database?useSSL=false&allowPublicKeyRetrieval=true";
	private static String username = "new_username1";
	private static String password = "new_password1";

	public static void main(String[] args)
	{
		EnsureTableExists();
		Scanner scanner = new Scanner(System.in);
		boolean keepRunning = true;
		
		while (keepRunning)
		{
			System.out.println("Choose an operation:");
			System.out.println("1. Create a record");
			System.out.println("2. Read records");
			System.out.println("3. Update a record");
			System.out.println("4. Delete a record");
			System.out.println("5. Exit");
			
			String choice = scanner.nextLine();

			switch (choice)
			{
				case "1":
					CreateRecord(scanner);
					break;
				case "2":
					ReadRecords();
					break;
				case "3":
				    UpdateRecord(scanner);
				    break;
				case "4":
				    DeleteRecord(scanner);
				    break;
				case "5":
				    keepRunning = false;
				    break;
				default:
					System.out.println("Invalid choice. Try again.");
					break;
			}
		}
		scanner.close();
		//client.close();

	}
	// 1. Create a new record with user input and validation
	private static void CreateRecord(Scanner scanner)
	{
		String name;
		String email;
		String phone;
		String address;
		
		// Input validation for "name"
		do
		{
			System.out.print("Enter name : ");
			name = scanner.nextLine();
			if (name.isEmpty())
			{
				System.out.println("Name cannot be empty.");
			}
		}
		while (name.isEmpty());
		// Input validation for "email"
		while (true)
		{
			try
			{
				System.out.print("Enter email : ");
				email = scanner.nextLine();
				String regex = "^(.+)@(\\S+)$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(email);
				if(matcher.matches())
				{
					break;
				}
				else
				{
					System.out.println("Please enter a valid email.");
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter a valid email.");
			}
		}
		// Input validation for "phone"
		while (true)
		{
			try
			{
				System.out.print("Enter phone : ");
				phone = scanner.nextLine();
				if(phone.matches("\\d+") && phone.length() >= 10) 
				{
					break;
				}
				else
				{
					System.out.println("Please enter a valid phone number.");
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter a valid phone number.");
			}
		}
		// Input validation for "address"
		do
		{
			System.out.print("Enter address : ");
			address = scanner.nextLine();
			if (address.isEmpty())
			{
				System.out.println("Address cannot be empty.");
			}
		}
		while (address.isEmpty());
		// Get Date
		Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

		// Insert the record into MySQL
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			String query = "INSERT INTO user (name, email, phone, address, created_at) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1,name);
			stmt.setString(2,email);
			stmt.setString(3,phone);
			stmt.setString(4,address);
			stmt.setDate(5,sqlDate);
			stmt.executeUpdate();
			System.out.println("Record inserted successfully");
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void ReadRecords()
	{
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			String query = "SELECT * FROM USER";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			// Print headers
			System.out.printf("%-5s %-20s %-20s %-15s %-20s %-30s%n", "ID", "Name", "Email", "Phone", "Address", "Created At");
			System.out.println(new String(new char[115]).replace("\0", "-"));
			while(rs.next())
			{
				System.out.printf("%-5s %-20s %-20s %-15s %-20s %-30s%n", rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"), rs.getString("address"), rs.getString("created_at"));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void UpdateRecord(Scanner scanner)
	{
		int ID;
		while (true)
		{
			try
			{
				
				System.out.print("Enter the ID of the person to update: ");
				ID = Integer.parseInt(scanner.nextLine());
				if (ID > 0) 
				{
					break;
				}
				else
				{
					System.out.println("Please enter a valid ID.");
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter a valid ID.");
			}
		}
		
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			String selectQuery = "SELECT * FROM USER WHERE id = ?";
			PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
			selectStmt.setInt(1, ID);
			ResultSet rs = selectStmt.executeQuery();
			
			if(!rs.next())
			{
				System.out.println("No record found with ID: "+ID);
				return;
			}
			// Input validation for "name"
			String newName;
			do
			{
				System.out.print("Enter new name: ");
				newName = scanner.nextLine();
				if (newName.isEmpty())
				{
					System.out.println("Name cannot be empty.");
				}
			} while (newName.isEmpty());
			// Input validation for "email"
			String newEmail;
			while (true)
			{
				try
				{
					System.out.print("Enter new email : ");
					newEmail = scanner.nextLine();
					String regex = "^(.+)@(\\S+)$";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(newEmail);
					if(matcher.matches())
					{
						break;
					}
					else
					{
						System.out.println("Please enter a valid email.");
					}
				}
				catch(NumberFormatException e)
				{
					System.out.println("Please enter a valid email.");
				}
			}
			// Input validation for "phone"
			String newPhone;
			while (true)
			{
				try
				{
					System.out.print("Enter new phone number : ");
					newPhone = scanner.nextLine();
					if(newPhone.matches("\\d+") && newPhone.length() >= 10) 
					{
						break;
					}
					else
					{
						System.out.println("Please enter a valid phone number.");
					}
				}
				catch(NumberFormatException e)
				{
					System.out.println("Please enter a valid phone number.");
				}
			}
			// Input validation for "address"
			String newAddress;
			do
			{
				System.out.print("Enter address : ");
				newAddress = scanner.nextLine();
				if (newAddress.isEmpty())
				{
					System.out.println("Address cannot be empty.");
				}
			}
			while (newAddress.isEmpty());
			
			
			String updateQuery = "UPDATE USER SET name=?, email=?, phone=?, address=? WHERE ID=?";
			PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
			updateStmt.setString(1,newName);
			updateStmt.setString(2,newEmail);
			updateStmt.setString(3,newPhone);
			updateStmt.setString(4,newAddress);
			updateStmt.setInt(5,ID);
			
			int result = updateStmt.executeUpdate();
			if(result > 0)
			{
				System.out.println("Document updated successfully.");
			}
			else
			{
				System.out.println("No documents updated...");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	private static void DeleteRecord(Scanner scanner)
	{
		int ID;
		while (true)
		{
			try
			{
				System.out.print("Enter the ID of the person to delete: ");
				ID = Integer.parseInt(scanner.nextLine());
				if (ID > 0) 
				{
					break;
				}
				else
				{
					System.out.println("Please enter a valid ID.");
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter a valid ID.");
			}
		}
		
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			String deleteQuery = "DELETE FROM USER WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(deleteQuery);
			stmt.setInt(1, ID);
			
			int result = stmt.executeUpdate();
			if(result > 0)
			{
				System.out.println("Record deleted successfully");
			}
			else
			{
				System.out.println("No records deleted...");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void EnsureTableExists() 
	{
		try(Connection conn = DriverManager.getConnection(connectionString, username, password))
		{
			String createTableQuery = """ 
					
				CREATE TABLE IF NOT EXISTS user(
				id INT AUTO_INCREMENT PRIMARY KEY,
				name VARCHAR(100) NOT NULL,
				email VARCHAR(255) NOT NULL,
				phone VARCHAR(100) NOT NULL,
				address VARCHAR(255) NOT NULL,
				created_at DATETIME
				) """;
			Statement stmt = conn.createStatement();
			stmt.execute(createTableQuery);
			System.out.println("Table 'user' is ensured to exist");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
