package application;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestsScene extends Stage {
    private ListView<String> requestsListView;

    public RequestsScene() {
        VBox layout = new VBox();

        // Initialize ListView to display requests
        requestsListView = new ListView<>();
        fetchRequests(); // Fetch requests from the database and populate the ListView

        layout.getChildren().add(requestsListView);

        Scene scene = new Scene(layout, 400, 300);
        setTitle("Requests");
        setScene(scene);
    }

    private void fetchRequests() {
        // Assuming you have a method to establish a database connection
        try (Connection conn = DatabaseHandler.getConnection()) {
            String sql = "SELECT service_name, request_date, request_data FROM requests";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String serviceName = rs.getString("service_name");
                        String requestDate = rs.getDate("request_date").toString();
                        String requestData = rs.getString("request_data");
                        String requestInfo = "Service: " + serviceName + " | Date: " + requestDate + " | Request: " + requestData;
                        requestsListView.getItems().add(requestInfo);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential exceptions
        }
    }
}
