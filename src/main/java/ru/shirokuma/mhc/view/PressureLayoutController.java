package ru.shirokuma.mhc.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.shirokuma.mhc.MhcApplication;
import ru.shirokuma.mhc.model.Pressure;

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
