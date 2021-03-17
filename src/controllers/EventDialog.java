package controllers;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import dialog.DialogHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.CalendarEvent;
import model.CalendarEventManager;

public class EventDialog extends JFXDialog {

	private CalendarEventManager manager;
	private AddEventDialogController eventDialogController;

	public EventDialog(StackPane stackPane) {
		JFXDialogLayout content = new JFXDialogLayout();

		FXMLLoader loader = new FXMLLoader(
				this.getClass().getResource("/views/fxml/AddEventDialog.fxml"));

		VBox bodyPane = null;
		try {
			bodyPane = loader.load();
			eventDialogController = loader.getController();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		content.setBody(bodyPane);

		JFXButton canselButton = new JFXButton("cancel");
		canselButton.getStyleClass().add("removeButton");
		canselButton.setOnAction(e -> {
			this.close();
		});

		JFXButton addButton = new JFXButton("Add");
		addButton.getStyleClass().add("addButton");
		HBox actionButtonPane = new HBox(15);
		addButton.setOnAction(e -> {
			CalendarEvent event = eventDialogController.getEvent();

			if (event == null) {
				DialogHandler.showErrorDialog(
						"Event canceled. There were not enough data for its creation");
			} else {
				addEvent(event);
			}

			this.close();
		});

		actionButtonPane.setPadding(new Insets(20, 0, 0, 0));
		actionButtonPane.getChildren().addAll(canselButton, addButton);

		content.setActions(actionButtonPane);

		setDialogContainer(stackPane);
		setContent(content);

		getStylesheets().add(
				this.getClass().getResource("/style/DialogStyle.css").toExternalForm());

	}

	private void addEvent(CalendarEvent event) {
		manager.addEvent(event);
	}

	public void clear() {
		eventDialogController.clear();
	}

	public void setCalendarEventManager(CalendarEventManager manager) {
		this.manager = manager;
	}
}
