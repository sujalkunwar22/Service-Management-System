package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class SignUp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sign Up Page");

        // Create UI components
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();
        Label roleLabel = new Label("Role:");
        ChoiceBox<String> roleChoiceBox = new ChoiceBox<>();
        roleChoiceBox.getItems().addAll("Staff", "Customer");
        roleChoiceBox.setValue("Customer"); // Default selection
        Button signUpButton = new Button("Sign Up");
        Button closeButton = new Button("Close");

        // Add event handler for the sign-up button
        signUpButton.setOnAction(event -> {
            // Perform sign-up actions (e.g., validate and save new user)
            String name = nameField.getText();
            String address = addressField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String role = roleChoiceBox.getValue();
            String username = generateUsername(name); // Generate a random username
            String password = generatePassword(); // Generate a random password

            if (isValidData(name, address, email, phone)) {
                // Proceed with sign-up
                boolean success = insertUserData(name, address, email, phone, role, username, password);
                if (success) {
                    // Show success message and close the application
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User signed up successfully! \nUsername: " + username + "\nPassword: " + password);
                    primaryStage.close();
                } else {
                    // Show error message
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to sign up user!");
                }
            } else {
                // Show warning for empty fields
                showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all the required fields.");
            }
        });

        // Add event handler for the close button
        closeButton.setOnAction(event -> {
            // Close the application
            primaryStage.close();
        });

        // Create layout and add components
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(addressLabel, 0, 1);
        grid.add(addressField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(phoneLabel, 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(roleLabel, 0, 4);
        grid.add(roleChoiceBox, 1, 4);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(signUpButton, closeButton);
        grid.add(buttonBox, 1, 5);

        // Set scene and show stage
        primaryStage.setScene(new Scene(grid, 400, 250));
        primaryStage.show();
    }

    // Method to generate a random username based on the user's name
    private String generateUsername(String name) {
        // Split the name into parts
        String[] parts = name.split("\\s+");
        Random random = new Random();
        // Generate a random number
        int randomNumber = random.nextInt(1000);
        // Combine parts of the name and the random number to form the username
        String username = parts[0].substring(0, Math.min(parts[0].length(), 3)) + parts[parts.length - 1].substring(0, Math.min(parts[parts.length - 1].length(), 3)) + randomNumber;
        // Limit the username to a maximum length of 6 characters
        return username.substring(0, Math.min(username.length(), 6));
    }

    // Method to generate a random password
    private String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        // Generate a random password of length 8
        for (int i = 0; i < 8; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    // Method to insert user data into the database
    private boolean insertUserData(String name, String address, String email, String phone, String role, String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // Get database connection
            conn = DatabaseHandler.getConnection();
            // Prepare SQL statement
            pstmt = conn.prepareStatement("INSERT INTO login_credentials (name, address, email, phone, role, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, role);
            pstmt.setString(6, username);
            pstmt.setString(7, password);
            // Execute SQL statement
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to validate data
    private boolean isValidData(String name, String address, String email, String phone) {
        return !name.isEmpty() && !address.isEmpty() && !email.isEmpty() && !phone.isEmpty();
    }

    // Method to show an alert dialog
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
