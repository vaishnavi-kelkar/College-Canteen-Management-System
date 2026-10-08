package com.mycompany.collegecanteen;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection
{
     public static Connection  getConnection(){
         Connection con=null;
         try
         {
             Class.forName("com.mysql.cj.jdbc.Driver");
             con=DriverManager.getConnection("jdbc:mysql://localhost:3306/colcanteen_db","root","");
         }catch(Exception e){
             e.printStackTrace();
             System.out.println("DB ERROR: " + e.getMessage());

         }
         return con;
     }
}