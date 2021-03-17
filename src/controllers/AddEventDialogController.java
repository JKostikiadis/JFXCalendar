package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import model.CalendarEvent;

public class AddEventDialogController {

	@FXML
	private JFXTabPane tabPane;

	@FXML
	private JFXTextField titleField;

	@FXML
	private JFXButton optionalEventButton;

	@FXML
	private JFXButton standardEventButton;

	@FXML
	private JFXButton importantEventButton;

	@FXML
	private JFXButton criticalEventButton;

	@FXML
	private JFXTabPane customTabPane;

	@FXML
	private DatePicker dateField;

	@FXML
	private TextArea eventNote1;

	@FXML
	private TextArea eventNote2;

	@FXML
	private JFXRadioButton everyWeekRB;

	@FXML
	private JFXRadioButton everyMonthRB;

	@FXML
	private JFXRadioButton everyYearRB;

	@FXML
	private JFXCheckBox mondayCB;

	@FXML
	private JFXCheckBox tuesdayCB;

	@FXML
	private JFXCheckBox wednesdayCB;

	@FXML
	private JFXCheckBox thursdayCB;

	@FXML
	private JFXCheckBox fridayCB;

	@FXML
	private JFXCheckBox saturdayCB;

	@FXML
	private JFXCheckBox sundayCB;

	@FXML
	private JFXCheckBox startOfTheMonthCB;

	@FXML
	private JFXCheckBox endOfTheMonthCB;

	@FXML
	private DatePicker yearlyDatePicker;

	private int eventType;

	@FXML
	public void initialize() {
		addPriorityButtonListeners();

		addPeriodicChoisesListeners();

		optionalEventButton.fire();
		everyWeekRB.fire();
	}

	private void addPeriodicChoisesListeners() {
		everyWeekRB.setOnAction(e -> {
			resetAndDisableAll();
			mondayCB.setDisable(false);
			tuesdayCB.setDisable(false);
			wednesdayCB.setDisable(false);
			thursdayCB.setDisable(false);
			fridayCB.setDisable(false);
			saturdayCB.setDisable(false);
			sundayCB.setDisable(false);
		});

		everyMonthRB.setOnAction(e -> {
			resetAndDisableAll();
			startOfTheMonthCB.setDisable(false);
			endOfTheMonthCB.setDisable(false);
		});

		everyYearRB.setOnAction(e -> {
			resetAndDisableAll();
			yearlyDatePicker.setDisable(false);
		});
	}

	private void resetAndDisableAll() {
		mondayCB.setSelected(false);
		tuesdayCB.setSelected(false);
		wednesdayCB.setSelected(false);
		thursdayCB.setSelected(false);
		fridayCB.setSelected(false);
		saturdayCB.setSelected(false);
		sundayCB.setSelected(false);
		startOfTheMonthCB.setSelected(false);
		endOfTheMonthCB.setSelected(false);
		yearlyDatePicker.setValue(null);

		mondayCB.setDisable(true);
		tuesdayCB.setDisable(true);
		wednesdayCB.setDisable(true);
		thursdayCB.setDisable(true);
		fridayCB.setDisable(true);
		saturdayCB.setDisable(true);
		sundayCB.setDisable(true);
		startOfTheMonthCB.setDisable(true);
		endOfTheMonthCB.setDisable(true);
		yearlyDatePicker.setDisable(true);
	}

	private void addPriorityButtonListeners() {
		// Event Button Listeners
		optionalEventButton.setOnAction(e -> {
			cleanSelection();
			eventType = CalendarEvent.OPTIONAL;
			optionalEventButton.setStyle(
					"-fx-background-color : #4C95CE; -fx-background-radius:15;");
		});

		standardEventButton.setOnAction(e -> {
			cleanSelection();
			eventType = CalendarEvent.STANDARD;
			standardEventButton.setStyle(
					"-fx-background-color : #81C457; -fx-background-radius:15;");
		});

		importantEventButton.setOnAction(e -> {
			cleanSelection();
			eventType = CalendarEvent.IMPORTANT;
			importantEventButton.setStyle(
					"-fx-background-color : #F8D500; -fx-background-radius:15;");
		});

		criticalEventButton.setOnAction(e -> {
			cleanSelection();
			eventType = CalendarEvent.URGENT;
			criticalEventButton.setStyle(
					"-fx-background-color : #E85569; -fx-background-radius:15;");
		});
	}

	private void cleanSelection() {
		eventType = -1;
		optionalEventButton
				.setStyle("-fx-background-color : #BDC6CC ; -fx-background-radius:15; ");
		standardEventButton
				.setStyle("-fx-background-color : #BDC6CC ; -fx-background-radius:15;");
		importantEventButton
				.setStyle("-fx-background-color : #BDC6CC ; -fx-background-radius:15;");
		criticalEventButton
				.setStyle("-fx-background-color : #BDC6CC ; -fx-background-radius:15;");
	}

	public int getEventType() {
		return eventType;
	}

	public void clear() {

		cleanSelection();
		eventNote1.setText("");
		eventNote2.setText("");
		titleField.setText("");

		dateField.setValue(null);
		yearlyDatePicker.setValue(null);

		tabPane.getSelectionModel().select(0);

		optionalEventButton.fire();
		everyWeekRB.fire();
	}

	public CalendarEvent getEvent() {
		String title = titleField.getText();
		if (title.isEmpty()) {
			return null;
		}

		CalendarEvent event = null;

		if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
			if (dateField.getValue() != null) {
				event = new CalendarEvent(title, eventType, eventNote1.getText());
				event.setType(CalendarEvent.ONE_TIME_EVENT);
				event.setDate(dateField.getValue());
			}
		} else {
			if (everyWeekRB.isSelected() && hasDaysSelected()) {
				// per week
				event = new CalendarEvent(title, eventType, eventNote2.getText());
				event.setType(CalendarEvent.RECURRING_EVENT);
				event.setPeriodicType(CalendarEvent.PER_WEEK);
				event.setDaysInWeek(collectDaysInWeek());
			} else if (everyMonthRB.isSelected() && hasMonthPlaceSelected()) {
				// per month
				event = new CalendarEvent(title, eventType, eventNote2.getText());
				event.setType(CalendarEvent.RECURRING_EVENT);
				event.setPeriodicType(CalendarEvent.PER_MONTH);
				if (startOfTheMonthCB.isSelected()) {
					event.setPlaceInMonth(CalendarEvent.START_OF_MONTH);
				} else {
					event.setPlaceInMonth(CalendarEvent.END_OF_MONTH);
				}
			} else if (yearlyDatePicker.getValue() != null) {
				// per year
				event = new CalendarEvent(title, eventType, eventNote2.getText());
				event.setType(CalendarEvent.RECURRING_EVENT);
				event.setPeriodicType(CalendarEvent.PER_YEAR);
				event.setYearlyDate(yearlyDatePicker.getValue());
			}
		}

		return event;
	}

	private boolean hasMonthPlaceSelected() {
		return startOfTheMonthCB.isSelected() || endOfTheMonthCB.isSelected();
	}

	private String collectDaysInWeek() {
		String days = "";
		if (mondayCB.isSelected()) {
			days += "1,";
		}
		if (tuesdayCB.isSelected()) {
			days += "2,";
		}
		if (wednesdayCB.isSelected()) {
			days += "3,";
		}
		if (thursdayCB.isSelected()) {
			days += "4,";
		}
		if (fridayCB.isSelected()) {
			days += "5,";
		}
		if (saturdayCB.isSelected()) {
			days += "6,";
		}
		if (sundayCB.isSelected()) {
			days += "7,";
		}
		return days;
	}

	private boolean hasDaysSelected() {
		return mondayCB.isSelected() || tuesdayCB.isSelected() || wednesdayCB.isSelected()
				|| thursdayCB.isSelected() || fridayCB.isSelected()
				|| saturdayCB.isSelected() || sundayCB.isSelected();
	}
}