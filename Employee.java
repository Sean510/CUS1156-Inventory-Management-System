import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Employee {
    private String username;
    private String password;

    //declaration with no parameters
    public Employee() {
        this.username = null;
        this.password = null;
    }

    //declaration with username and password parameters
    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }

    //verifies a user with this username and password exists in the database
    public Boolean verifyCredentials(Connection conn) throws Exception {
        String queryResult = null;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT username " +
                        "FROM employees " +
                        "WHERE username = '" + username +
                        "' AND password = '" + password + "';"
        );

        while (rs.next()) {
            queryResult = rs.getString("username");
        }

        return queryResult != null;
    }

    //tests if a user is a manager or regular employee
    public Boolean isManager(Connection conn) throws Exception {
        String queryResult = null;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT isManager " +
                        "FROM employees " +
                        "WHERE username = '" + username +
                        "' AND password = '" + password + "';"
        );

        while (rs.next()) {
            queryResult = rs.getString("isManager");
        }

        System.out.println(queryResult);
        return queryResult.equals("true");
    }

    //display menu for employee
    public void displayMenu(Connection conn) throws Exception {
        System.out.println("-----------------------------------------------------");
        System.out.println("| St. John's University Inventory Management System |");
        System.out.println("-----------------------------------------------------");
        System.out.println();
        System.out.println("Please select from an option below: ");
        System.out.println();
        System.out.println("---------------------------");
        System.out.println("|1. Sell Stock            |");
        System.out.println("|2. Purchase Stock        |");
        System.out.println("|3. Generate Report       |");
        System.out.println("|4. Logout                |");
        System.out.println("---------------------------");

        Boolean logout = false;
        do {
            Scanner scnr = new Scanner(System.in);
            int input = scnr.nextInt();
            switch (input) {
                case 0:
                    System.out.println("-----------------------------------------------------");
                    System.out.println("| St. John's University Inventory Management System |");
                    System.out.println("-----------------------------------------------------");
                    System.out.println();
                    System.out.println("Please select from an option below: ");
                    System.out.println();
                    System.out.println("---------------------------");
                    System.out.println("|1. Sell Stock            |");
                    System.out.println("|2. Purchase Stock        |");
                    System.out.println("|3. Generate Report       |");
                    System.out.println("|4. Logout                |");
                    System.out.println("---------------------------");
                    break;
                case 1:
                    System.out.println("Enter the stock code of the stock you want to sell:");
                    String saleStockCode = scnr.next();
                    Stock saleStock = new Stock(saleStockCode);
                    saleStock = saleStock.getStockInfo(conn);
                    System.out.println("Enter the quantity of stock you want to sell:");
                    int saleQuantity = scnr.nextInt();
                    System.out.println("Enter the sale date in format YYYY-MM-DD:");
                    String saleDate = scnr.next();
                    saleStock.sellStock(conn, saleQuantity, saleDate);
                    break;
                case 2:
                    System.out.println("Enter the stock code of the stock you want to purchase:");
                    String purchaseStockCode = scnr.next();
                    Stock purchaseStock = new Stock(purchaseStockCode);
                    purchaseStock = purchaseStock.getStockInfo(conn);
                    System.out.println("Enter the quantity of stock you want to purchase:");
                    int purchaseQuantity = scnr.nextInt();
                    System.out.println("Enter the purchase date in format YYYY-MM-DD:");
                    String purchaseDate = scnr.next();
                    purchaseStock.purchaseStock(conn, purchaseQuantity, purchaseDate);
                    break;
                case 3:
                    generateReport(conn);
                    break;
                case 4:
                    System.out.println("LOGGING OUT.......");
                    System.out.println("GOODBYE!");
                    logout = true;
            }
            if (!logout) {
                System.out.println();
                System.out.println("If you need to see the menu again press 0, If you wish to logout press 4:");
                System.out.println("Select another task to preform: ");
            }
        } while (!logout);
    }

    //generates report for current stock status
    public void generateReport(Connection conn) throws Exception {
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM stock_status;");

        while(rs.next()) {
            ArrayList<String> tempList = new ArrayList<String>();
            tempList.add(rs.getString("stockCode"));
            tempList.add(rs.getString("name"));
            tempList.add(rs.getString("price"));
            tempList.add(rs.getString("openingStock"));
            tempList.add(rs.getString("purchases"));
            tempList.add(rs.getString("sales"));
            tempList.add(rs.getString("closingStock"));
            tempList.add(rs.getString("totalValue"));

            list.add(tempList);
        }

//        System.out.printf("|%5s| |%5s| |%5s| |%5s| |%5s| |%5s| |%5s| |%5s|%n",
//                "Stock Code", "Name", "Price", "Opening Stock",
//                "Purchases", "Sales", "Closing Stock", "Total Value");
//        //System.out.println();
//        for (ArrayList<String> stringLists : list) {
//            for (String strings : stringLists) {
//                System.out.printf("%5s%5s" ,strings, "");
//            }
//            System.out.println();
//        }
//
        System.out.print("|SOCK CODE|");
        System.out.print("          ");
        System.out.print("|NAME|");
        System.out.print("               ");
        System.out.print("|PRICE|");
        System.out.print("          ");
        System.out.print("|OPENING STOCK|");
        System.out.print("          ");
        System.out.print("|PURCHASES|");
        System.out.print("          ");
        System.out.print("|SALES|");
        System.out.print("          ");
        System.out.print("|CLOSING STOCK|");
        System.out.print("          ");
        System.out.print("|TOTAL VALUE|");
        System.out.println();


        for (int i=0; i<list.size(); i++) {
            for (int j=0; j<list.get(0).size(); j++) {
                //System.out.printf("%5s%5s" ,strings, "");
//                if (j==0) System.out.print("                     " + j);
                System.out.print("     " + list.get(i).get(j) + "     ");
            }
            System.out.println();
        }

    }

}
