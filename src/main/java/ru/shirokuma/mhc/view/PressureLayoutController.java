package ru.shirokuma.mhc.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.shirokuma.mhc.MhcApplication;
import ru.shirokuma.mhc.model.Pressure;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Created by shirokuma on 15.03.2015.
 */
public class PressureLayoutController {
    @FXML
    private TableView<Pressure> pressureTable;
    @FXML
    private TableColumn<Pressure, LocalDateTime> date;
    @FXML
    private TableColumn<Pressure, Number> up;
    @FXML
    private TableColumn<Pressure, Number> bottom;
    @FXML
    private TableColumn<Pressure, Number> pulse;

    private MhcApplication mainApp;
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new pressure.
     */
    @FXML
    private void handleNewPressure() {
        Pressure tempPressure = new Pressure();
        boolean okClicked = showPressureEditDialog(tempPressure);
        if (okClicked) {
            mainApp.getPressureData().add(tempPressure);
        }
    }
    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param pressure the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPressureEditDialog(Pressure pressure) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MhcApplication.class.getResource("view/PressureEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Pressure");
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(this);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PressureEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPressure(pressure);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditPressure() {
        Pressure selectedPressure = pressureTable.getSelectionModel().getSelectedItem();
        if (selectedPressure != null) {
            boolean okClicked = showPressureEditDialog(selectedPressure);
        } else {
            // Nothing selected.
            Alert dialog = new Alert(Alert.AlertType.WARNING);
            dialog.setTitle("No Selection");
            dialog.setHeaderText("No Pressure Selected");
            dialog.setContentText("Please select a pressure in the table.");
            dialog.showAndWait();
        }
    }

    @FXML
    private void handleDeletePressure() {
        int selectedPressure = pressureTable.getSelectionModel().getSelectedIndex();
        if (selectedPressure != -1) {
            mainApp.getPressureData().remove(selectedPressure);
        } else {
            // Nothing selected.
            Alert dialog = new Alert(Alert.AlertType.WARNING);
            dialog.setTitle("No Selection");
            dialog.setHeaderText("No Pressure Selected");
            dialog.setContentText("Please select a pressure in the table.");
            dialog.showAndWait();
        }
    }

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        date.setCellValueFactory(
                cellData -> cellData.getValue().timePointProperty());
        DateTimeFormatter datePointFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        date.setCellFactory(column -> {
            return new TableCell<Pressure, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item==null || empty){
                        setText(null);
                    }else{
                        // Format date
                        setText(datePointFormatter.format(item));
                    }
                }
            };
        });
        up.setCellValueFactory(
                cellData -> cellData.getValue().sbpProperty());
        bottom.setCellValueFactory(
                cellData -> cellData.getValue().dbpProperty());
        pulse.setCellValueFactory(
                cellData -> cellData.getValue().pulseProperty());


    }

    public void setMainApp(MhcApplication mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        pressureTable.setItems(mainApp.getPressureData());
    }
}
