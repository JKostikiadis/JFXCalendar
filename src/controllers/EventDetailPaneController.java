package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.CalendarEvent;

public class EventDetailPaneController {

	@FXML
	private JFXButton updateEventButton;

	@FXML
	private JFXButton closeDialogButton;

	@FXML
	private TextField titleEventField;

	@FXML
	private DatePicker eventDatePicker;

	@FXML
	private TextArea eventDescriptionArea;

	@FXML
	private JFXButton optionalEventButton;

	@FXML
	private JFXButton standardEventButton;

	@FXML
	private JFXButton importantEventButton;

	@FXML
	private JFXButton criticalEventButton;

	@FXML
	private Label dateL;

	@FXML
	private Label typeLabel;

	@FXML
	private TextField typeField;

	private JFXDialog dialog;

	private CalendarEvent event;

	private int selectedPriority = -1;

	private boolean hasChanged = false;

	@FXML
	private void initialize() {

		closeDialogButton.setOnAction(e -> {
			dialog.close();
		});

		optionalEventButton.setOnAction(e -> {
			selectPriority(CalendarEvent.OPTIONAL);
		});

		standardEventButton.setOnAction(e -> {
			selectPriority(CalendarEvent.STANDARD);
		});

		importantEventButton.setOnAction(e -> {
			selectPriority(CalendarEvent.IMPORTANT);
		});

		criticalEventButton.setOnAction(e -> {
			selectPriority(CalendarEvent.URGENT);
		});

		updateEventButton.setOnAction(e -> {
			// update event information

			event.setTitle(titleEventField.getText());
			event.setDescription(eventDescriptionArea.getText());
			event.setPriority(selectedPriority);
			if (event.getType() == CalendarEvent.ONE_TIME_EVENT) {
				event.setDate(eventDatePicker.getValue());
			}

			hasChanged = true;
			dialog.close();
		});
	}

	public void loadEvent(CalendarEvent event) {
		this.event = event;
		titleEventField.setText(event.getTitle());
		eventDescriptionArea.setText(event.getDescription());

		selectPriority(event.getPriority());

		if (event.getType() == CalendarEvent.ONE_TIME_EVENT) {
			typeLabel.setText("Date");
			eventDatePicker.setValue(event.getDate());

		} else {
			eventDatePicker.setVisible(false);
			typeLabel.setText("Event type");

			typeField.setVisible(true);
			if (event.getPeriodicType() == CalendarEvent.PER_WEEK) {
				typeField.setText("Weekly");
			} else if (event.getPeriodicType() == CalendarEvent.PER_MONTH) {
				typeField.setText("Monthly");
			} else {
				typeField.setText("Yearly");
			}
		}
	}

	private void clearPriorityOptions() {
		optionalEventButton
				.setStyle("-fx-background-color: #BDC6CC; -fx-background-radius:15px; ");
		standardEventButton
				.setStyle("-fx-background-color: #BDC6CC; -fx-background-radius:15px; ");
		importantEventButton
				.setStyle("-fx-background-color: #BDC6CC; -fx-background-radius:15px; ");
		criticalEventButton
				.setStyle("-fx-background-color: #BDC6CC; -fx-background-radius:15px; ");
	}

	private void selectPriority(int priority) {
		clearPriorityOptions();

		if (priority == 1) {
			optionalEventButton.setStyle(
					"-fx-background-color: #4C95CE; -fx-background-radius:15px; ");
		} else if (priority == 2) {
			standardEventButton.setStyle(
					"-fx-background-color: #81C457; -fx-background-radius:15px; ");
		} else if (priority == 3) {
			importantEventButton.setStyle(
					"-fx-background-color: #F7C531; -fx-background-radius:15px; ");
		} else {
			criticalEventButton.setStyle(
					"-fx-background-color: #E85569; -fx-background-radius:15px; ");
		}
		this.selectedPriority = priority;
	}

	public void setDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}

	public boolean hasUpdates() {
		return hasChanged;
	}
}