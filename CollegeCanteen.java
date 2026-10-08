package com.mycompany.collegecanteen;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CollegeCanteen {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Canteen Login");

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50,50,80,25);

        JTextField userText = new JTextField();
        userText.setBounds(140,50,120,25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50,90,80,25);

        JPasswordField passText = new JPasswordField();
        passText.setBounds(140,90,120,25);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(120,140,90,30);

        frame.setLayout(null);

        frame.add(userLabel);
        frame.add(userText);
        frame.add(passLabel);
        frame.add(passText);
        frame.add(loginButton);

        // ✅ FIXED LOGIN BUTTON CODE
        loginButton.addActionListener(e -> {

            String username = userText.getText();
            String password = new String(passText.getPassword());

            try{

                Connection con = DBConnection.getConnection();

                String query = "SELECT * FROM users WHERE username=? AND password=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, username);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    JOptionPane.showMessageDialog(frame,"Login Successful");
                    frame.dispose();
                    new OrderDashboard();
                }else{
                    JOptionPane.showMessageDialog(frame,"Invalid Username or Password");
                }

            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,"Error: " + ex.getMessage());
            }

        }); 

        frame.setSize(350,250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}