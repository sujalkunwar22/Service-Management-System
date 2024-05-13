package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerDashboard extends Application {

    private String loggedInUsername; // Username of the logged-in user

    // Default constructor
    public CustomerDashboard() {
        // You can optionally leave this constructor empty
    }

    // Constructor with logged-in username parameter
    public CustomerDashboard(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Dashboard");

        // Create label for welcome message
        Label welcomeLabel = new Label("Welcome, " + loggedInUsername);

        // Create buttons
        Button ordersButton = new Button("Request a Service");
        Button cartButton = new Button("Old Requests");
        Button settingsButton = new Button("Settings");

        // Set button styles
        ordersButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        cartButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        settingsButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        // Set event handlers for buttons
        ordersButton.setOnAction(event -> openOrdersPage());
        cartButton.setOnAction(event -> openCartPage());
        settingsButton.setOnAction(event -> openSettingsPage());

        // Create a layout for welcome message and profile section
        VBox welcomeLayout = new VBox(10);
        welcomeLayout.setPadding(new Insets(10));
        welcomeLayout.getChildren().addAll(welcomeLabel);

        // Create a layout for buttons
        VBox buttonLayout = new VBox(10);
        buttonLayout.setPadding(new Insets(10));
        buttonLayout.getChildren().addAll(ordersButton, cartButton, settingsButton);

        // Set the scene
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(welcomeLayout, buttonLayout);

        primaryStage.setScene(new Scene(layout, 200, 250));
        primaryStage.show();
    }

    // Method to open the orders page
    private void openOrdersPage() {
        Stage ordersStage = new Stage();
        ordersStage.setTitle("Orders Page");

        // Create label for services
        Label servicesLabel = new Label("Choose a Service:");
        ComboBox<String> serviceComboBox = new ComboBox<>();
        serviceComboBox.getItems().addAll("Electrician", "Plumber", "Cleaner");

        // Create labels and text fields for name and phone number
        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();
        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();

        // Create a DatePicker for selecting the date
        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker();

        // Create a button to save the entries into the database
        Button requestButton = new Button("Request");
        requestButton.setOnAction(event -> {
            // Get the selected values
            String service = serviceComboBox.getValue();
            String location = locationField.getText();
            String phoneNumber = phoneField.getText();
            LocalDate date = datePicker.getValue();

            // Perform database insertion here
            // Example: Call a method to insert data into the database
            insertOrderIntoDatabase(service, location, phoneNumber, date);

            // Optionally, close the ordersStage after saving
            ordersStage.close();
        });

        // Create a layout using GridPane for better arrangement
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.add(servicesLabel, 0, 0);
        gridPane.add(serviceComboBox, 1, 0);
        gridPane.add(locationLabel, 0, 1);
        gridPane.add(locationField, 1, 1);
        gridPane.add(phoneLabel, 0, 2);
        gridPane.add(phoneField, 1, 2);
        gridPane.add(dateLabel, 0, 3);
        gridPane.add(datePicker, 1, 3);
        gridPane.add(requestButton, 1, 4);

        // Set the scene for the orders stage
        ordersStage.setScene(new Scene(gridPane, 300, 250));
        ordersStage.show();
    }

    // Method to insert order data into the database
 // Method to insert order data into the database
    private void insertOrderIntoDatabase(String service, String location, String phoneNumber, LocalDate date) {
        try {
            // Establish connection
            Connection connection = DatabaseHandler.getConnection();

            // Prepare SQL statement
            String sql = "INSERT INTO orders (username, service, location, phone_number, order_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, loggedInUsername); // Use loggedInUsername to fetch the username
            statement.setString(2, service);
            statement.setString(3, location);
            statement.setString(4, phoneNumber);
            statement.setDate(5, java.sql.Date.valueOf(date));

            // Execute the statement
            statement.executeUpdate();

            // Close resources
            statement.close();
            connection.close();

            System.out.println("Request placed successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
        }
    }


    // Method to open the cart page
    private void openCartPage() {
        // Create a new stage for displaying old requests
        Stage oldRequestsStage = new Stage();

        // Call method to display old requests
        displayOldRequests(oldRequestsStage);
    }

    // Method to fetch and display old requests
    private void displayOldRequests(Stage primaryStage) {
        // Create a ListView to display old requests
        ListView<String> oldRequestsListView = new ListView<>();

        // Fetch old requests from the database
        try {
            // Establish connection
            Connection connection = DatabaseHandler.getConnection();
            Statement statement = connection.createStatement();

            // Query to select old requests for the logged-in user
            String query = "SELECT * FROM orders WHERE username = '" + loggedInUsername + "'";
            ResultSet resultSet = statement.executeQuery(query);

            // Add old requests to the ListView
            while (resultSet.next()) {
                String service = resultSet.getString("service");
                String location = resultSet.getString("location");
                String phoneNumber = resultSet.getString("phone_number");
                String orderDate = resultSet.getDate("order_date").toString();
                String request = service + " | " + location + " | " + phoneNumber + " | " + orderDate;
                oldRequestsListView.getItems().add(request);
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error fetching old requests: " + e.getMessage());
        }

        // Create a layout for old requests
        VBox oldRequestsLayout = new VBox(10);
        oldRequestsLayout.getChildren().addAll(oldRequestsListView);

        // Set the scene for old requests
        primaryStage.setScene(new Scene(oldRequestsLayout, 400, 300));
        primaryStage.setTitle("Old Requests");
        primaryStage.show();
    }

    // Method to open the settings page
    private void openSettingsPage() {
        // Add code to open the settings page
        System.out.println("Opening Settings Page...");
    }

    public static void main(String[] args) {
        // Example usage: Pass a username to the CustomerDashboard constructor
        Application.launch(CustomerDashboard.class, "JohnDoe");
    }
}
