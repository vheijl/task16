package se.experis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class EmployeeDirectory {
    private Connection conn = Connect.connect();
    private Scanner scanner = new Scanner(System.in);
    private String SQLString;
    private String[] selMethods = new String[] {"Country", "City", "Region", "Title"};


    public EmployeeDirectory() {
        promptInput();
        getInput();
        //queryDB();
    }

    private void promptInput() {
        printHeader();
        promptSelectionMethod();
    }

    private void promptSelectionMethod() {
        for (int i = 0; i < selMethods.length; i++) {
            System.out.println(("[" + i + "] " + selMethods[i]));
        }
        System.out.println("Select method by number:");
    }

    private void getInput() {
        try {
            int inputOption = Integer.parseInt(scanner.nextLine());
            if (inputOption < 0 || inputOption > selMethods.length - 1) {
                clearConsole();
                promptInput();
                System.out.println("You must select an option that exists!");
                getInput();
            } else {
                selectMethod(inputOption);
            }
        } catch(NumberFormatException e) {
            clearConsole();
            promptInput();
            System.out.println("You must select the number of the option!");
            getInput();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printHeader() {
        System.out.println("+---------------------------------------+");
        System.out.println("List all employees at ACME INC");
        System.out.println("+---------------------------------------+");
        System.out.println("How do you want to select employees?");
    }

    private void selectMethod(int selection) {
        String sel = selMethods[selection];
        String filterKeyword;
        System.out.println("Provide a " + sel + ":");
        try {
            filterKeyword = scanner.nextLine();
            query(selection, filterKeyword);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String padRight(String str, int length, char c) {
        while(str.length() < length) {
            str += c;
        }
        return str;
    }

    private void query(int selection, String keyword) {
        String SQLQuery;
        switch(selection) {
            case 0:
                SQLQuery = "SELECT * FROM Employee WHERE country = '" + keyword + "' COLLATE NOCASE";
                break;
            case 1:
                SQLQuery = "SELECT * FROM Employee WHERE city = '" + keyword + "' COLLATE NOCASE";
                break;
            case 2:
                SQLQuery = "SELECT * FROM Employee WHERE region = '" + keyword + "' COLLATE NOCASE";
                break;
            case 3:
                SQLQuery = "SELECT * FROM Employee WHERE title = '" + keyword + "' COLLATE NOCASE";
                break;
            default:
                SQLQuery = "";
                break;
        }
        try {
            Statement stmt  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQLQuery);
            String separator = new String(new char[108]).replace("\0", "-");
            System.out.println(separator);
            System.out.println(
                    padRight("Id", 3, ' ') +
                    padRight("FirstName", 18, ' ') +
                    padRight("LastName", 18, ' ') +
                    padRight("Title", 24, ' ') +
                    padRight("City", 14, ' ') +
                    padRight("Region", 15, ' ') +
                    padRight("Country", 2, ' ')
            );
            System.out.println(separator);
            while (rs.next()) {
                System.out.println(
                        padRight(Integer.toString(rs.getInt("Id")), 3, ' ') +
                        padRight(rs.getString("FirstName"), 18, ' ') +
                        padRight(rs.getString("LastName"), 18, ' ') +
                        padRight(rs.getString("Title"), 24, ' ') +
                        padRight(rs.getString("City"), 14, ' ') +
                        padRight(rs.getString("Region"), 15, ' ') +
                        padRight(rs.getString("Country"), 2, ' ')
                );
            }
            System.out.println(separator);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

