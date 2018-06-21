package evaluation;

import java.sql.*;
import java.text.*;
import java.util.*;

public class Query2 {

    /*
     * header comments:
     * 
     * 1. General instructions on how to execute my program
     *   (1) Import my program into Eclipse
     *   (2) Import the JDBC package (with corresponding version of PostgreSQL and Java) into the project.
     *   (3) Open “src/evaluation/Query 2.java”. Change variables ‘usr’, ‘pwd’ and ‘url’ with corresponding values on your computer.
     *   (4) Click “Run” button at the tool bar.
     *   (5) See result in “Console” window.
     * 
     * 2. Justification of my choice of data structures for my program
     *   I use arrays to store information of maximum quantity, etc. for each customer because arrays are very flexible for random access and the length of the information is fixed.
     *   I use an ArrayList to hold all customers' arrays because the ArrayList can change its length to facilitate the insertion of new arrays.
     * 
     * 3. A detailed description of the algorithm of my program
     *   (1) First I get a row of 'sales' table
     *   (2) Then I get the customer's name and product of this row and traverse each element in the 'list'(ArrayList) to see if the name and product are the same in one of those elements
     *   (3) If the 'list' has already had this name-product combination's information, I take corresponding 'arr'(array) out of 'list'
     *   (4) If arr's month == 1 and year between 2000 and 2005, I compare JAN_MAX's information; If arr's month == 2, I compare FEB_MIN's information;
     *       If arr's month == 3, I compare MAR_MIN's information; At last I update the arr's data about the changes of JAN_MAX or FEB_MIN or MAR_MIN.
     *   (5) If the 'list' does not have this name-product combination's information, I create a new 'arr' and fill customer and product in. Fill other information with 'null'.
     *   (6) If arr's month == 1 and year between 2000 and 2005, I update JAN_MAX's information; If arr's month == 2, I update FEB_MIN's information;
     *       If arr's month == 3, I update MAR_MIN's information; At last I add this 'arr' in the 'list'
     *   (7) After go through all data in the database, I have all name-product combination's information in 'list'. At last I print it all out with specific format.
     * 
     */

    public static void main(String[] args) {

        // for each combination of customer and product, we need 8 empty spaces in an array to store his maximum, minimum quantity etc.
        int space = 8;
        // stores each customer's array
        ArrayList<String[]> list = new ArrayList<String[]>();

        // the user name, password and database name of postgreSQL
        String usr ="postgres";
        String pwd ="26892681147";
        String url ="jdbc:postgresql://localhost:5432/CS561";

        // load the class, and initialize it
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Loading successfully.");
        }
        catch(Exception e) {
            System.out.println("Loading unsuccessfully.");
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(url, usr, pwd);
            System.out.println("Connecting server successfully.\n");

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM sales");

            // read each row (one row at a time) and process it
            while (rs.next()) {
                
                // get parameters of a row
                String cust = rs.getString("cust");
                String prod = rs.getString("prod");
                String quant = rs.getString("quant");
                int month = Integer.parseInt(rs.getString("month"));
                int year = Integer.parseInt(rs.getString("year"));
                String date =   new DecimalFormat("00").format(month) + "/" +
                                new DecimalFormat("00").format(Integer.parseInt(rs.getString("day"))) + "/" +
                                new DecimalFormat("0000").format(year);
                
                // notInList is a flag to show whether someone's data is in the list or not
                boolean notInList = true;
                // to see where is someone's data in the list
                int inWhere = -1;
                for(int i = 0; i < list.size(); i++) {
                    String[] arr = list.get(i);
                    if(arr[0].equals(cust) && arr[1].equals(prod)) {
                        notInList = false;
                        inWhere = i;
                        break;
                    }
                }
                
                if(notInList) {
                    // someone's data is not in the list. Just add the data in.
                    String[] arr = new String[space];
                    arr[0] = cust;      // CUSTOMER
                    arr[1] = prod;      // PRODUCT
                    arr[2] = "null";    // JAN_MAX
                    arr[3] = "null";    // DATE
                    arr[4] = "null";    // FEB_MIN
                    arr[5] = "null";    // DATE
                    arr[6] = "null";    // MAR_MIN
                    arr[7] = "null";    // DATE
                    
                    if(month == 1 && year >= 2000 && year <= 2005) {
                        // add JAN_MAX's data
                        arr[2] = quant;
                        arr[3] = date;
                    }
                    else if(month == 2) {
                        // add FEB_MIN's data
                        arr[4] = quant;
                        arr[5] = date;
                    }
                    else if(month == 3) {
                        // add MAR_MIN's data
                        arr[6] = quant;
                        arr[7] = date;
                    }
                    
                    list.add(arr);
                }
                else {
                    // someone's data has already been in the list. Just update the data.
                    String[] arr = list.get(inWhere);
                    
                    // update JAN_MAX's data
                    if((arr[2] == "null" || Integer.parseInt(quant) > Integer.parseInt(arr[2])) && (month == 1 && year >= 2000 && year <= 2005)) {
                        arr[2] = quant;
                        arr[3] = date;
                    }
                    // update FEB_MIN's data
                    if((arr[4] == "null" || Integer.parseInt(quant) < Integer.parseInt(arr[4])) && month == 2) {
                        arr[4] = quant;
                        arr[5] = date;
                    }
                    // update MAR_MIN's data
                    if((arr[6] == "null" || Integer.parseInt(quant) < Integer.parseInt(arr[6])) && month == 3) {
                        arr[6] = quant;
                        arr[7] = date;
                    }
                    
                    list.set(inWhere, arr);
                }
                
            }
            
            // finish reading, print out result
            System.out.println("CUSTOMER  PRODUCT  JAN_MAX  DATE        FEB_MIN  DATE        MAR_MIN  DATE      ");
            System.out.println("========  =======  =======  ==========  =======  ==========  =======  ==========");
            for(int i = 0; i < list.size(); i++) {
                String[] arr = list.get(i);
                System.out.println( String.format("%-10s", arr[0]) +
                                    String.format("%-9s", arr[1]) +
                                    String.format("%7s", arr[2]) +
                                    String.format("%12s", arr[3]) +
                                    String.format("%9s", arr[4]) +
                                    String.format("%12s", arr[5]) +
                                    String.format("%9s", arr[6]) +
                                    String.format("%12s", arr[7]));
            }
            
        }
        catch(SQLException e) {
            System.out.println("Connection URL or username or password errors!");
            e.printStackTrace();
        }

    }

}
