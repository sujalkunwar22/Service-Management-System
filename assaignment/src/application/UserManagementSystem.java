package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class UserManagementSystem extends Application {
    private TableView<User> userTable;
    private ObservableList<User> userData;

    // Represents a user with their information.
    public static class User {
        private final StringProperty id;
        private final StringProperty name;
        private final StringProperty address;
        private final StringProperty email;
        private final StringProperty phone;
        private final StringProperty username;
        private final StringProperty password;
        private final StringProperty role;
        private final StringProperty status;

        public User(String id, String name, String address, String email, String phone, String username, String password, String role, String status) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.address = new SimpleStringProperty(address);
            this.email = new SimpleStringProperty(email);
            this.phone = new SimpleStringProperty(phone);
            this.username = new SimpleStringProperty(username);
            this.password = new SimpleStringProperty(password);
            this.role = new SimpleStringProperty(role);
            this.status = new SimpleStringProperty(status);
        }

        public StringProperty idProperty() {
            return id;
        }

        public StringProperty nameProperty() {
            return name;
        }

        public StringProperty addressProperty() {
            return address;
        }

        public StringProperty emailProperty() {
            return email;
        }

        public StringProperty phoneProperty() {
            return phone;
        }

        public StringProperty usernameProperty() {
            return username;
        }

        public StringProperty passwordProperty() {
            return password;
        }

        public StringProperty roleProperty() {
            return role;
        }

        public StringProperty statusProperty() {
            return status;
        }
    }

    // Database handler for establishing connection
    public static class DatabaseHandler {
        private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
        private static final String HOST = "localhost";
        private static final int PORT = 3306;
        private static final String DATABASE = "MyDB";
        private static final String DBUSER = "root";
        private static final String DBPASS = "Ss234@##*";

        public static Connection getConnection() {
            Connection conn = null;
            try {
                Class.forName(DRIVER);
                String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
                conn = DriverManager.getConnection(url, DBUSER, DBPASS);
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }
            return conn;
        }
    }

    private void retrieveDataFromDatabase() {
        userData = FXCollections.observableArrayList();
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM login_credentials")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String status = rs.getString("status");
                userData.add(new User(id, name, address, email, phone, username, password, role, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private TableView<User> createUserTable() {
        TableView<User> table = new TableView<>();

        TableColumn<User, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.idProperty().set(event.getNewValue());
            updateDatabase(user);
        });

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.nameProperty().set(event.getNewValue());
            updateDatabase(user);
        });

        TableColumn<User, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        addressColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.addressProperty().set(event.getNewValue());
            updateDatabase(user);
        });

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.emailProperty().set(event.getNewValue());
            updateDatabase(user);
        });

        TableColumn<User, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.phoneProperty().set(event.getNewValue());
            updateDatabase(user);
        });

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.usernameProperty().set(event.getNewValue());
            updateDatabase(user);
        });

        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.passwordProperty().set(event.getNewValue());
            updateDatabase(user);
        });

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<User, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statusColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.statusProperty().set(event.getNewValue());
            updateDatabase(user);
        });

        table.getColumns().addAll(idColumn, nameColumn, addressColumn, emailColumn, phoneColumn, usernameColumn, passwordColumn, roleColumn, statusColumn);
        table.setItems(userData);

        return table;
    }

    private void updateDatabase(User user) {
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE login_credentials SET name=?, address=?, email=?, phone=?, username=?, password=?, role=?, status=? WHERE id=?")) {
            stmt.setString(1, user.nameProperty().get());
            stmt.setString(2, user.addressProperty().get());
            stmt.setString(3, user.emailProperty().get());
            stmt.setString(4, user.phoneProperty().get());
            stmt.setString(5, user.usernameProperty().get());
            stmt.setString(6, user.passwordProperty().get());
            stmt.setString(7, user.roleProperty().get());
            stmt.setString(8, user.statusProperty().get());
            stmt.setString(9, user.idProperty().get());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createLayout() {
        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        HBox searchBar = createSearchBar();
        HBox buttonBar = createButtonBar();
        userTable = createUserTable();

        layout.getChildren().addAll(searchBar, buttonBar, userTable);

        return layout;
    }

    private HBox createSearchBar() {
        HBox searchBar = new HBox();
        searchBar.setSpacing(10);
        searchBar.setPadding(new Insets(10));

        Label searchLabel = new Label("Search ID:");
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> {
            String searchId = searchField.getText();
            if (!searchId.isEmpty()) {
                for (User user : userData) {
                    if (user.idProperty().getValue().equals(searchId)) {
                        userTable.scrollTo(user);
                        userTable.getSelectionModel().select(user);
                        break;
                    }
                }
            }
        });

        searchBar.getChildren().addAll(searchLabel, searchField, searchButton);

        return searchBar;
    }

    private HBox createButtonBar() {
        HBox buttonBar = new HBox();
        buttonBar.setSpacing(10);
        buttonBar.setPadding(new Insets(10));

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> addUser());

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            if (userTable.isEditable()) {
                userTable.setEditable(false);
                editButton.setText("Edit");
            } else {
                userTable.setEditable(true);
                editButton.setText("Done");
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteUser());

        Button requestsButton = new Button("Requests");
        requestsButton.setOnAction(event -> openRequestsScene());

        buttonBar.getChildren().addAll(addButton, editButton, deleteButton, requestsButton);

        return buttonBar;
    }

    private void addUser() {
        VBox addUserDialog = new VBox();
        addUserDialog.setSpacing(10);
        addUserDialog.setPadding(new Insets(10));

        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            String id = idField.getText();
            String name = nameField.getText();
            String address = addressField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (id.isEmpty() || name.isEmpty() || address.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Fields Empty");
                alert.setContentText("All fields are required!");
                alert.showAndWait();
                return;
            }

            User newUser = new User(id, name, address, email, phone, username, password, "customer", "Active");
            userData.add(newUser);

            try (Connection conn = DatabaseHandler.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO login_credentials (id, name, address, email, phone, username, password, role, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, id);
                stmt.setString(2, name);
                stmt.setString(3, address);
                stmt.setString(4, email);
                stmt.setString(5, phone);
                stmt.setString(6, username);
                stmt.setString(7, password);
                stmt.setString(8, "customer");
                stmt.setString(9, "Active");
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        addUserDialog.getChildren().addAll(idField, nameField, addressField, emailField, phoneField, usernameField, passwordField, addButton);

        Scene dialogScene = new Scene(addUserDialog, 300, 400);
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add User");
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }


    private void deleteUser() {
        ObservableList<User> selectedUsers = userTable.getSelectionModel().getSelectedItems();
        userData.removeAll(selectedUsers);
        try (Connection conn = DatabaseHandler.getConnection()) {
            for (User user : selectedUsers) {
                String id = user.idProperty().getValue();
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM login_credentials WHERE id = ?")) {
                    stmt.setString(1, id);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openRequestsScene() {
        RequestsScene requestsScene = new RequestsScene();
        requestsScene.show();
    }

    @Override
    public void start(Stage primaryStage) {
        retrieveDataFromDatabase();
        Scene scene = new Scene(createLayout(), 800, 600);
        primaryStage.setTitle("User Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
