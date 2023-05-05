package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class OrderList {
    private TableView<Orders> ordersTable;

    public VBox createTable(ObservableList<Orders> data){
        //columns
        TableColumn name=new TableColumn("NAME");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn quantity=new TableColumn("QUANTITY");
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn price=new TableColumn("PRICE");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        ordersTable=new TableView<>();
        ordersTable.getColumns().addAll(name,quantity,price);
        ordersTable.setItems(data);
        ordersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vBox=new VBox();
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(ordersTable);
        return vBox;
    }
    public VBox getAllOrders(int id){
        ObservableList<Orders> data= Orders.getAllOrders(id);
        return createTable(data);
    }
}
