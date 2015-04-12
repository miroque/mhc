package ru.shirokuma.mhc;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.shirokuma.mhc.model.Pressure;
import ru.shirokuma.mhc.model.PressureListWrapper;
import ru.shirokuma.mhc.view.PressureEditDialogController;
import ru.shirokuma.mhc.view.PressureLayoutController;
import ru.shirokuma.mhc.view.RootLayoutController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.prefs.Preferences;

public class MhcApplication extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Pressure> pressureData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("My Health Card");
        // Set the application icon.
//        this.primaryStage.getIcons().add(new Image("file:res/img/ico.png"));
        initRootLayout();

        showPressure();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MhcApplication.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened person file.
        File file = getPressureFilePath();
        if (file != null) {
            loadPressureDataFromFile(file);
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPressure() {
        try {
            // Load pressure overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MhcApplication.class.getResource("view/PressureLayout.fxml"));
            AnchorPane pressureOverview = (AnchorPane) loader.load();

            // Set pressure overview into the center of root layout.
            rootLayout.setCenter(pressureOverview);
            // Give the controller access to the main app.
            PressureLayoutController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public MhcApplication() {
        // Add some sample data
        pressureData.add(new Pressure(110, 65, 72));
        pressureData.add(new Pressure(115, 70, 70));
        pressureData.add(new Pressure(110, 62, 70));
        pressureData.add(new Pressure(120, 70, 75));
        pressureData.add(new Pressure(105, 62, 68));
        pressureData.add(new Pressure(90, 75, 60));
        pressureData.add(new Pressure(110, 65, 72));
    }

    /**
     * Returns the data as an observable list of Persons.
     *
     * @return
     */
    public ObservableList<Pressure> getPressureData() {
        return pressureData;
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getPressureFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MhcApplication.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setPressureFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MhcApplication.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("AddressApp");
        }
    }

    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
    public void loadPressureDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PressureListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            PressureListWrapper wrapper = (PressureListWrapper) um.unmarshal(file);

            pressureData.clear();
            pressureData.addAll(wrapper.getPressureList());

            // Save the file path to the registry.
            setPressureFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Error");
            dialog.setHeaderText("Could not load data to file:\n" + file.getPath());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane.
            dialog.getDialogPane().setExpandableContent(expContent);

            dialog.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void savePressureDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PressureListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            PressureListWrapper wrapper = new PressureListWrapper();
            wrapper.setPressureList(pressureData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setPressureFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Error");
            dialog.setHeaderText("Could not save data to file:\n" + file.getPath());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane.
            dialog.getDialogPane().setExpandableContent(expContent);

            dialog.showAndWait();
        }
    }
    /**
     * Opens a dialog to show birthday statistics.
     */
   /* public void showBirthdayStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MhcApplication.class.getResource("view/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(pressureData);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}