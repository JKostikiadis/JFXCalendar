package dialog;

import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogHandler {

	public static void showErrorDialog(String errorMessage) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);

				alert.setGraphic(null);

				alert.getDialogPane().setStyle(
						".dialog-pane > .content.label {-fx-padding: 0.5em 0.5em 0.5em 0.5em; -fx-font-size:14px;}");
				setDialogIcon(alert);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText(errorMessage);
				alert.showAndWait();
			}

		});
	}

	public static boolean showConfirmationDialog(String confirmationMessage) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().setStyle(
				".dialog-pane > .content.label {-fx-padding: 0.5em 0.5em 0.5em 0.5em; -fx-font-size:14px;}");

		setDialogIcon(alert);

		alert.setTitle("Attention");
		alert.setHeaderText(confirmationMessage);
		alert.setContentText("Do you wish to continue;");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	private static void setDialogIcon(Dialog<?> dialog) {
		// if you want to load your custom icon
		// Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		// Image logo = new
		// Image(DialogHandler.class.getResourceAsStream("/resources/favicon.png"));
		// stage.getIcons().add(logo);
	}
}
