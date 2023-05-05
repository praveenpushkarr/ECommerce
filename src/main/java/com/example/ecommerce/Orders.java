package com.example.ecommerce;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

public class Orders {
        private SimpleStringProperty name;
        private SimpleIntegerProperty quantity;
        private SimpleDoubleProperty price;

        public Orders(String name, int quantity, double price) {
            this.name = new SimpleStringProperty(name);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = new SimpleDoubleProperty(price);
        }

        public static ObservableList<Orders> getAllOrders(int id){
            String selectAllOrders="select product.name,orders.quantity,product.price from orders join product on orders.product_id=product.id where customer_id="+id;
            return fetchOrderDataFromDB(selectAllOrders);
        }
        public static ObservableList<Orders> fetchOrderDataFromDB(String query){
            ObservableList<Orders> data= FXCollections.observableArrayList();
            DbConnection dbConnection=new DbConnection();
            try{
                ResultSet rs=dbConnection.getQueryTable(query);
                while(rs.next()){
                    Orders order=new Orders(rs.getString("name"),rs.getInt("quantity"),rs.getDouble("price"));
                    data.add(order);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return data;
        }

        public int getQuantity() {
            return quantity.get();
        }

        public String getName() {
            return name.get();
        }

        public double getPrice() {
            return price.get();
        }

}

