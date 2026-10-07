import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerManagerApp extends Application {

    // Step 2: Customer class
    public static class Customer {
        private final StringProperty name = new SimpleStringProperty();
        private final StringProperty province = new SimpleStringProperty();

        public Customer(String name, String province) {
            this.name.set(name);
            this.province.set(province);
        }

        public StringProperty nameProperty() { return name; }
        public StringProperty provinceProperty() { return province; }
        public String getName() { return name.get(); }
    }

    // Step 2: ObservableList<Customer>
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        // Step 1: Form with a name field and province list
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        ComboBox<String> provinceBox = new ComboBox<>(FXCollections.observableArrayList(
                "Central", "Copperbelt", "Eastern", "Luapula", "Lusaka",
                "Muchinga", "Northern", "North-Western", "Southern", "Western"));
        provinceBox.setPromptText("Select province");

        Button addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        Button deleteBtn = new Button("Delete");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Step 3: TableView with name and province columns
        TableView<Customer> table = new TableView<>(customers);
        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(c -> c.getValue().provinceProperty());
        table.getColumns().add(nameCol);
        table.getColumns().add(provinceCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Step 4: Validate input, then add the customer
        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            if (name.isEmpty()) {
                errorLabel.setText("Please enter a name.");
                nameField.requestFocus();
                return;
            }
            if (province == null) {
                errorLabel.setText("Please select a province.");
                provinceBox.requestFocus();
                return;
            }

            customers.add(new Customer(name, province));
            errorLabel.setText("");
            nameField.clear();
            provinceBox.setValue(null);
            nameField.requestFocus();
        });

        // Step 5: Confirm deletion of a selected customer
        deleteBtn.setOnAction(e -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                errorLabel.setText("Select a customer to delete.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);
            confirm.showAndWait().ifPresent(result -> {
                if (result == ButtonType.YES) {
                    customers.remove(selected);
                    errorLabel.setText("");
                }
            });
        });

        VBox root = new VBox(10, nameField, provinceBox, new HBox(10, addBtn, deleteBtn), errorLabel, table);
        root.setPadding(new Insets(15));

        stage.setTitle("Customer Manager");
        stage.setScene(new Scene(root, 420, 450));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
