package ru.shirokuma.mhc.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.shirokuma.mhc.model.Pressure;
import ru.shirokuma.mhc.util.DateTimeUtil;

/**
 * Created by shirokuma on 05.04.2015.
 */
public class PressureEditDialogController {
    @FXML
    private TextField dateTimeField;
    @FXML
    private TextField upperField;
    @FXML
    private TextField bottomField;
    @FXML
    private TextField pulseField;

    private Stage dialogStage;
    private Pressure pressure;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the pressure to be edited in the dialog.
     *
     * @param pressure
     */
    public void setPressure(Pressure pressure) {
        this.pressure = pressure;

        upperField.setText(Integer.toString(pressure.getSbp()));
        bottomField.setText(Integer.toString(pressure.getDbp()));
        pulseField.setText(Integer.toString(pressure.getPulse()));
        dateTimeField.setText(DateTimeUtil.format(pressure.getTimePoint()));
        dateTimeField.setPromptText("dd.mm.yyyy HH:mm");
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            pressure.setSbp(Integer.parseInt(upperField.getText()));
            pressure.setDbp(Integer.parseInt(bottomField.getText()));
            pressure.setPulse(Integer.parseInt(pulseField.getText()));
            pressure.setTimePoint(DateTimeUtil.parse(dateTimeField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (upperField.getText() == null || upperField.getText().length() == 0) {
            errorMessage += "No valid Upper data!\n";
        }
        if (bottomField.getText() == null || bottomField.getText().length() == 0) {
            errorMessage += "No valid Bottom data!\n";
        }
        if (pulseField.getText() == null || pulseField.getText().length() == 0) {
            errorMessage += "No valid pulseField!\n";
        }

        if (dateTimeField.getText() == null || dateTimeField.getText().length() == 0) {
            errorMessage += "No valid Date and Time!\n";
        } else {
            if (!DateTimeUtil.validDate(dateTimeField.getText())) {
                errorMessage += "No valid Date or TIme. Use the format dd.mm.yyyy HH:mm!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Invalid Fields");
            dialog.setHeaderText("Please correct invalid fields");
            dialog.setContentText(errorMessage);
            dialog.showAndWait();
            return false;
        }
    }
}
