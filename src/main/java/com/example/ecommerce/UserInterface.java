package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {

    GridPane loginPage;

    HBox headerBar;
    VBox body;
    HBox footerBar;
    Button signInButton;
    Label welcomeLabel;
    Customer loggedInCustomer;

    ProductList productList=new ProductList();
    OrderList orderList=new OrderList();
    VBox productPage;
    VBox ordersPage;
    Button placeOrderButton=new Button("Place Order");
    ObservableList<Product> itemsInCart= FXCollections.observableArrayList();
    public BorderPane createContent(){
        BorderPane root=new BorderPane();
        root.setPrefSize(800,600);

        root.setTop(headerBar);
        //root.setCenter(loginPage);

        body=new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);
        root.setCenter(body);
        productPage=productList.getAllProducts();
        body.getChildren().add(productPage);

        root.setBottom(footerBar);

        return root;
    }

    public UserInterface(){
        createLoginPage();
        createHeaderBar();
        createFooterBar();
    }

    private void createLoginPage(){
        Text userNameText=new Text("User Name");
        Text passwordText=new Text("Password");

        TextField userName=new TextField();
        userName.setPromptText("Type your user name here");
        PasswordField password=new PasswordField();
        password.setPromptText("Type your password here");
        Label messageLabel=new Label();

        Button loginButton=new Button("Login");

        loginPage=new GridPane();
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(userNameText,0,0);
        loginPage.add(userName,1,0);
        loginPage.add(passwordText,0,1);
        loginPage.add(password,1,1);
        loginPage.add(messageLabel,0,2);
        loginPage.add(loginButton,1,2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name=userName.getText();
                String pass=password.getText();
                Login login=new Login();
                loggedInCustomer=login.customerLogin(name,pass);
                if(loggedInCustomer!=null){
                    messageLabel.setText("Welcome "+loggedInCustomer.getName());
                    welcomeLabel.setText("Welcome "+loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);
                }
                else{
                    messageLabel.setText("Login failed! please give correct username and password");
                }

            }
        });


    }

    private void createHeaderBar(){
        Button homeButton=new Button();
        Image image=new Image("D:\\Praveen\\Projects\\ecommerce project\\ECommerce\\src\\img.png");
        ImageView imageView=new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(120);
        homeButton.setGraphic(imageView);

        TextField searchBar=new TextField();
        searchBar.setPromptText("Search here");
        searchBar.setPrefWidth(300);

        Button searchButton=new Button("Search");

        signInButton=new Button("Sign In");
        welcomeLabel=new Label();

        Button cartButton=new Button("Cart");
        Button orderButton=new Button("Orders");

        headerBar = new HBox();
        headerBar.setPadding(new Insets(10));
        headerBar.setAlignment(Pos.CENTER);
        headerBar.setSpacing(10);
        headerBar.getChildren().addAll(homeButton,searchBar,searchButton,signInButton,cartButton,orderButton);

        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(loginPage);
                headerBar.getChildren().remove(signInButton);
            }
        });

        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox prodPage=productList.getProductsInCart(itemsInCart);
                prodPage.getChildren().add(placeOrderButton);
                prodPage.setSpacing(10);
                prodPage.setAlignment(Pos.CENTER);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false);
            }
        });

        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(itemsInCart==null){
                    showDialog("Please add some products in cart to place order");
                    return;
                }
                if(loggedInCustomer==null){
                    showDialog("Please login first to place order");
                    return;
                }
                int count=Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
                if(count!=0){
                    showDialog("Order for "+count+" products is placed successfully!!");
                }
                else{
                    showDialog("Order failed!!");
                }
            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(productPage);
                footerBar.setVisible(true);
                if(loggedInCustomer==null && headerBar.getChildren().indexOf(signInButton)==-1){
                    headerBar.getChildren().add(3,signInButton);
                }
            }
        });

        orderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(loggedInCustomer==null){
                    showDialog("Sign In to view your orders!!");
                }
                else{
                    body.getChildren().clear();
                    ordersPage=orderList.getAllOrders(loggedInCustomer.getId());
                    body.getChildren().add(ordersPage);
                    footerBar.setVisible(false);
                }
            }
        });
    }

    private void createFooterBar(){
        Button buyNowButton=new Button("Buy Now");
        Button addToCartButton=new Button("Add to Cart");

        footerBar = new HBox();
        footerBar.setPadding(new Insets(10));
        footerBar.setAlignment(Pos.CENTER);
        footerBar.setSpacing(10);
        footerBar.getChildren().addAll(buyNowButton,addToCartButton);

        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productList.getSelectedProduct();
                if(product==null){
                    showDialog("Please select a order first to place order");
                    return;
                }
                if(loggedInCustomer==null){
                    showDialog("Please login first to place order");
                    return;
                }
                boolean status=Order.placeOrder(loggedInCustomer,product);
                if(status){
                    showDialog("Order placed successfully!!");
                }
                else{
                    showDialog("Order failed!!");
                }
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productList.getSelectedProduct();
                if(product==null){
                    showDialog("Please select a order first to add it to cart");
                    return;
                }
                itemsInCart.add(product);
                showDialog("Selected items are added to cart successfully!!");
            }
        });
    }

    private void showDialog(String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
