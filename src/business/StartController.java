package business;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class StartController {
    @FXML
    private Spinner<Integer> sInitialCars;

    @FXML
    private void initialize() {
        sInitialCars.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 25, 5));
    }

    @FXML
    private void startSimulation() {
        int numCars = sInitialCars.getValue();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/Main.fxml"));
            Parent root = loader.load();

            // Pasar el número de carros iniciales al MainController
            MainController mainController = loader.getController();
            mainController.setInitialCarCount(numCars);

            Stage stage = new Stage();
            stage.setTitle("Simulación de Tráfico");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar ventana de inicio
            Stage currentStage = (Stage) sInitialCars.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}