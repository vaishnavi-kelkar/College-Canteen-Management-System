package com.mycompany.collegecanteen;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderDashboard {

    int total = 0;

    public OrderDashboard(){

        JFrame frame = new JFrame("Order Dashboard");

        JLabel title = new JLabel("Select Food Items");
        title.setBounds(130,20,200,30);

        JCheckBox idli = new JCheckBox("Idli - ₹30");
        idli.setBounds(50,60,120,30);

        JSpinner idliQty = new JSpinner(new SpinnerNumberModel(0,0,10,1));
        idliQty.setBounds(200,60,50,30);

        JCheckBox coffee = new JCheckBox("Coffee - ₹20");
        coffee.setBounds(50,100,120,30);

        JSpinner coffeeQty = new JSpinner(new SpinnerNumberModel(0,0,10,1));
        coffeeQty.setBounds(200,100,50,30);

        JCheckBox sandwich = new JCheckBox("Sandwich - ₹40");
        sandwich.setBounds(50,140,140,30);

        JSpinner sandwichQty = new JSpinner(new SpinnerNumberModel(0,0,10,1));
        sandwichQty.setBounds(200,140,50,30);

        JCheckBox dosa = new JCheckBox("Dosa - ₹50");
        dosa.setBounds(50,180,120,30);

        JSpinner dosaQty = new JSpinner(new SpinnerNumberModel(0,0,10,1));
        dosaQty.setBounds(200,180,50,30);

        JCheckBox juice = new JCheckBox("Juice - ₹35");
        juice.setBounds(50,220,120,30);

        JSpinner juiceQty = new JSpinner(new SpinnerNumberModel(0,0,10,1));
        juiceQty.setBounds(200,220,50,30);

        JLabel totalLabel = new JLabel("Running Total: ₹0");
        totalLabel.setBounds(120,260,200,30);

        JButton orderBtn = new JButton("Place Order");
        orderBtn.setBounds(130,300,120,30);

        frame.setLayout(null);

        frame.add(title);
        frame.add(idli); frame.add(idliQty);
        frame.add(coffee); frame.add(coffeeQty);
        frame.add(sandwich); frame.add(sandwichQty);
        frame.add(dosa); frame.add(dosaQty);
        frame.add(juice); frame.add(juiceQty);
        frame.add(totalLabel);
        frame.add(orderBtn);

        // ✅ TOTAL CALCULATION
        ActionListener calculateTotal = e -> {

            total = 0;

            int idliQ = (int) idliQty.getValue();
            int coffeeQ = (int) coffeeQty.getValue();
            int sandwichQ = (int) sandwichQty.getValue();
            int dosaQ = (int) dosaQty.getValue();
            int juiceQ = (int) juiceQty.getValue();

            if(idli.isSelected()) total += idliQ * 30;
            if(coffee.isSelected()) total += coffeeQ * 20;
            if(sandwich.isSelected()) total += sandwichQ * 40;
            if(dosa.isSelected()) total += dosaQ * 50;
            if(juice.isSelected()) total += juiceQ * 35;

            totalLabel.setText("Running Total: ₹" + total);
        };

        // Checkbox actions
        idli.addActionListener(calculateTotal);
        coffee.addActionListener(calculateTotal);
        sandwich.addActionListener(calculateTotal);
        dosa.addActionListener(calculateTotal);
        juice.addActionListener(calculateTotal);

        // Spinner actions
        idliQty.addChangeListener(e -> {
            idli.setSelected((int)idliQty.getValue() > 0);
            calculateTotal.actionPerformed(null);
        });

        coffeeQty.addChangeListener(e -> {
            coffee.setSelected((int)coffeeQty.getValue() > 0);
            calculateTotal.actionPerformed(null);
        });

        sandwichQty.addChangeListener(e -> {
            sandwich.setSelected((int)sandwichQty.getValue() > 0);
            calculateTotal.actionPerformed(null);
        });

        dosaQty.addChangeListener(e -> {
            dosa.setSelected((int)dosaQty.getValue() > 0);
            calculateTotal.actionPerformed(null);
        });

        juiceQty.addChangeListener(e -> {
            juice.setSelected((int)juiceQty.getValue() > 0);
            calculateTotal.actionPerformed(null);
        });

        // ✅ PLACE ORDER + SAVE + UPDATE STOCK
        orderBtn.addActionListener(e -> {

            double tax = total * 0.05;
            double finalAmount = total + tax;

            JOptionPane.showMessageDialog(frame,
                    "Order Summary\n\nTotal: ₹" + total +
                    "\nTax (5%): ₹" + tax +
                    "\nFinal Amount: ₹" + finalAmount);

            try {
                Connection con = DBConnection.getConnection();

                // 1️⃣ Save transaction
                String sql = "INSERT INTO transactions (student_id, total_amount, order_date) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, "admin");
                ps.setDouble(2, finalAmount);
                ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));

                ps.executeUpdate();

                // 2️⃣ Update inventory

                if(idli.isSelected()){
                    int qty = (int) idliQty.getValue();
                    PreparedStatement p = con.prepareStatement(
                        "UPDATE inventory SET stock = stock - ? WHERE item_name='Idli'");
                    p.setInt(1, qty);
                    p.executeUpdate();
                }

                if(coffee.isSelected()){
                    int qty = (int) coffeeQty.getValue();
                    PreparedStatement p = con.prepareStatement(
                        "UPDATE inventory SET stock = stock - ? WHERE item_name='Coffee'");
                    p.setInt(1, qty);
                    p.executeUpdate();
                }

                if(sandwich.isSelected()){
                    int qty = (int) sandwichQty.getValue();
                    PreparedStatement p = con.prepareStatement(
                        "UPDATE inventory SET stock = stock - ? WHERE item_name='Sandwich'");
                    p.setInt(1, qty);
                    p.executeUpdate();
                }

                if(dosa.isSelected()){
                    int qty = (int) dosaQty.getValue();
                    PreparedStatement p = con.prepareStatement(
                        "UPDATE inventory SET stock = stock - ? WHERE item_name='Dosa'");
                    p.setInt(1, qty);
                    p.executeUpdate();
                }

                if(juice.isSelected()){
                    int qty = (int) juiceQty.getValue();
                    PreparedStatement p = con.prepareStatement(
                        "UPDATE inventory SET stock = stock - ? WHERE item_name='Juice'");
                    p.setInt(1, qty);
                    p.executeUpdate();
                }

                JOptionPane.showMessageDialog(frame, "Order Placed & Stock Updated!");

            } catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }

        });

        frame.setSize(400,420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}