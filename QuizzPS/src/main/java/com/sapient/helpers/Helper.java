package com.sapient.helpers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Helper {
    public static void printResultSet(ResultSet resultSet){
        try{
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }    
}
