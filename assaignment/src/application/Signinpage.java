package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Signinpage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Page");

        // Create UI components
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");

        // Add event handler for the login button
        loginButton.setOnAction(event -> {
            // Validate username and password
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (isValidLogin(username, password)) {
                // If login is successful, close the login window
                primaryStage.close();
            } else {
                // If login fails, show error message
                System.out.println("Invalid username or password");
            }
        });
        // Add event handler for the signup button
        signupButton.setOnAction(event -> openSignUpPage());
        // Create layout and add components
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(signupButton, 1, 3);
        // Set scene and show stage
        primaryStage.setScene(new Scene(grid, 300, 200));
        primaryStage.show();
    }
 // Method to validate login by fetching details from the database
    @SuppressWarnings("resource")
	private boolean isValidLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseHandler.getConnection();
            // Check login details in admins table first
            stmt = conn.prepareStatement("SELECT * FROM admins WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                openStaffManagement(); // Open StaffManagement if found in admins table
                return true; // Login successful
            } else {
                // If not found in admins table, check in login_credentials table
                stmt = conn.prepareStatement("SELECT * FROM login_credentials WHERE username = ? AND password = ?");
                stmt.setString(1, username);
                stmt.setString(2, password);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    // Retrieve role from the result set
                    String role = rs.getString("role");
                    // Check the role and open the respective window
                    if ("Customer".equals(role)) {
                        openCustomerDashboard(username);
                    } else if ("Staff".equals(role)) {
                        openUserManagementSystem();
                    } else {
                        System.out.println("Invalid role: " + role);
                    }
                    return true; // Login successful
                } else {
                    return false; // Login failed
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to open SignUp page
    private void openSignUpPage() {
        SignUp signUp = new SignUp();
        signUp.start(new Stage());
    }

    // Method to open CustomerDashboard window
    private void openCustomerDashboard(String username) {
        CustomerDashboard customerDashboard = new CustomerDashboard(username);
        customerDashboard.start(new Stage());
    }

    // Method to open UserManagementSystem window
    private void openUserManagementSystem() {
        UserManagementSystem userManagementSystem = new UserManagementSystem();
        userManagementSystem.start(new Stage());
    }

    // Method to open StaffManagement window
    private void openStaffManagement() {
        StaffManagement staffManagement = new StaffManagement();
        staffManagement.start(new Stage());
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            // Print the stack trace of the underlying exception
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
    }
}
