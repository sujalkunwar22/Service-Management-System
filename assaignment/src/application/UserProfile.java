package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfile extends Application {

    private String loggedInUsername;

    public UserProfile(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Profile");

        // Create labels to display user information
        Label usernameLabel = new Label("Username: " + loggedInUsername);
        Label nameLabel = new Label();
        Label emailLabel = new Label();
        Label phoneLabel = new Label();

        // Fetch additional details from the database
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name, email, phone FROM user_details WHERE username = ?")) {
            stmt.setString(1, loggedInUsername);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");

                // Set the retrieved details to corresponding labels
                nameLabel.setText("Name: " + name);
                emailLabel.setText("Email: " + email);
                phoneLabel.setText("Phone: " + phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a layout to organize labels
        VBox layout = new VBox(10);
        layout.getChildren().addAll(usernameLabel, nameLabel, emailLabel, phoneLabel);

        // Set the scene
        primaryStage.setScene(new Scene(layout, 300, 200));
        primaryStage.show();
    }
}
