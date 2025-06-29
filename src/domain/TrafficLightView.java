package domain;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TrafficLightView {
	private Label xLight;
	private Label yLight;
	private StackPane container;

	public TrafficLightView() {
		xLight = new Label("X");
		xLight.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 9px;");
		xLight.setPrefSize(18, 18);

		yLight = new Label("Y");
		yLight.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 9px;");
		yLight.setPrefSize(18, 18);

		container = new StackPane();
		container.setPrefSize(30, 30);
		container.getChildren().addAll(xLight, yLight);
	}

	public Label getxLight() {
		return xLight;
	}

	public Label getyLight() {
		return yLight;
	}

	public StackPane getContainer() {
		return container;
	}
}
