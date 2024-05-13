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

public class ProfilePage extends Application {

    private final String username;

    public ProfilePage(String username) {
        this.username = username;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Profile");

        // Create labels for profile data
        Label nameLabel = new Label();
        Label addressLabel = new Label();
        Label emailLabel = new Label();
        Label phoneLabel = new Label();

        // Fetch profile data from the database
        try {
            Connection conn = DatabaseHandler.getConnection();
            String query = "SELECT name, address, email, phone FROM login_credentials WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameLabel.setText("Name: " + rs.getString("name"));
                addressLabel.setText("Address: " + rs.getString("address"));
                emailLabel.setText("Email: " + rs.getString("email"));
                phoneLabel.setText("Phone: " + rs.getString("phone"));
            } else {
                // Handle case when profile data is not found
                nameLabel.setText("Profile data not found");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }

        // Create a layout
        VBox layout = new VBox(10); // Vertical spacing between nodes
        layout.getChildren().addAll(nameLabel, addressLabel, emailLabel, phoneLabel);

        // Set the scene
        primaryStage.setScene(new Scene(layout, 300, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
