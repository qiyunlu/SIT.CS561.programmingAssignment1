package evaluation;

import java.sql.*;
import java.text.*;
import java.util.*;

public class Query1 {

    /*
     * header comments:
     * 
     * 1. General instructions on how to execute my program
     *   (1) Import my program into Eclipse
     *   (2) Import the JDBC package (with corresponding version of PostgreSQL and Java) into the project.
     *   (3) Open “src/evaluation/Query 1.java”. Change variables ‘usr’, ‘pwd’ and ‘url’ with corresponding values on your computer.
     *   (4) Click “Run” button at the tool bar.
     *   (5) See result in “Console” window.
     * 
     * 2. Justification of my choice of data structures for my program
     *   I use arrays to store information of maximum quantity, etc. for each customer because arrays are very flexible for random access and the length of the information is fixed.
     *   I use an ArrayList to hold all customers' arrays because the ArrayList can change its length to facilitate the insertion of new arrays.
     * 
     * 3. A detailed description of the algorithm of my program
     *   (1) First I get a row of 'sales' table
     *   (2) Then I get the customer's name of this row and traverse each element in the 'list'(ArrayList) to see if the name is the same in one of those elements
     *   (3) If the 'list' has already had this customer's information, I take corresponding 'arr'(array) out of 'list', compare the maximum and minimum quantities of product,
     *       and update the arr's data about the average and the changes of maximum or minimum.
     *   (4) If the 'list' does not have this customer's information, I create a new 'arr', store this information as both maximum and minimum quantities and update average value.
     *       Then add this 'arr' into 'list'
     *   (5) After go through all data in the database, I have every customer's information in 'list'. At last I print it all out with specific format.
     * 
     */
    
    public static void main(String[] args) {

        // for each customer, we need 11 empty spaces in an array to store his maximum, minimum quantity etc.
        int space = 11;
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
                String date =   new DecimalFormat("00").format(Integer.parseInt(rs.getString("month"))) + "/" +
                                new DecimalFormat("00").format(Integer.parseInt(rs.getString("day"))) + "/" +
                                new DecimalFormat("0000").format(Integer.parseInt(rs.getString("year")));
                String state = rs.getString("state");
                
                // notInList is a flag to show whether someone's data is in the list or not
                boolean notInList = true;
                // to see where is someone's data in the list
                int inWhere = -1;
                for(int i = 0; i < list.size(); i++) {
                    String[] arr = list.get(i);
                    if(arr[0].equals(cust)) {
                        notInList = false;
                        inWhere = i;
                        break;
                    }
                }
                
                if(notInList) {
                    // someone's data is not in the list. Just add the data in.
                    String[] arr = new String[space];
                    arr[0] = cust;      // CUSTOMER
                    arr[1] = quant;     // MAX_Q
                    arr[2] = prod;      // PRODUCT
                    arr[3] = date;      // DATE
                    arr[4] = state;     // ST
                    arr[5] = quant;     // MIN_Q
                    arr[6] = prod;      // PRODUCT
                    arr[7] = date;      // DATE
                    arr[8] = state;     // ST
                    arr[9] = quant;     // AVG_Q = arr[9]/arr[10]
                    arr[10] = 1 + "";
                    
                    list.add(arr);
                }
                else {
                    // someone's data has already been in the list. Just update the data.
                    String[] arr = list.get(inWhere);
                    
                    // update maximum
                    if(Integer.parseInt(quant) > Integer.parseInt(arr[1])) {
                        arr[1] = quant;
                        arr[2] = prod;
                        arr[3] = date;
                        arr[4] = state;
                    }
                    // update minimum
                    if(Integer.parseInt(quant) < Integer.parseInt(arr[5])) {
                        arr[5] = quant;
                        arr[6] = prod;
                        arr[7] = date;
                        arr[8] = state;
                    }
                    // update average
                    arr[9] = Integer.parseInt(arr[9]) + Integer.parseInt(quant) + "";
                    arr[10] = Integer.parseInt(arr[10]) + 1 + "";
                    
                    list.set(inWhere, arr);
                }
                
            }
            
            // finish reading, print out result
            System.out.println("CUSTOMER  MAX_Q  PRODUCT  DATE        ST  MIN_Q  PRODUCT  DATE        ST  AVG_Q");
            System.out.println("========  =====  =======  ==========  ==  =====  =======  ==========  ==  =====");
            for(int i = 0; i < list.size(); i++) {
                String[] arr = list.get(i);
                System.out.println( String.format("%-10s", arr[0]) +
                                    String.format("%5s", arr[1]) + "  " +
                                    String.format("%-9s", arr[2]) +
                                    String.format("%s", arr[3]) +
                                    String.format("%4s", arr[4]) +
                                    String.format("%7s", arr[5]) + "  " +
                                    String.format("%-9s", arr[6]) +
                                    String.format("%s", arr[7]) +
                                    String.format("%4s", arr[8]) +
                                    String.format("%7s", (Integer.parseInt(arr[9])/Integer.parseInt(arr[10]) + "")));
            }
            
        }
        catch(SQLException e) {
            System.out.println("Connection URL or username or password errors!");
            e.printStackTrace();
        }

    }

}
