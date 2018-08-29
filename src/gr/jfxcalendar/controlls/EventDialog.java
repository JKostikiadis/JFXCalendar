package gr.jfxcalendar.controlls;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EventDialog extends JFXDialog {

	private JFXButton optionalEventButton;
	private JFXButton standardEventButton;
	private JFXButton importantEventButton;
	private JFXButton criticalEventButton;
	private int eventType;

	private JFXTextField titleField;
	private CalendarTextField dateField;
	private JFXComboBox<String> beginHourButt;
	private JFXComboBox<String> endHourButt;
	private TextArea eventNote;

	public EventDialog(StackPane stackPane) {

		JFXDialogLayout content = new JFXDialogLayout();

		VBox headerPane = new VBox(15);
		HBox eventButtonPane = new HBox(10);

		// Title pane initialization
		StackPane titlePane = new StackPane();
		Label promptLabel = new Label("Event Title");
		titleField = new JFXTextField();
		titlePane.getChildren().addAll(promptLabel, titleField);

		promptLabel.visibleProperty().bind(titleField.textProperty().isEmpty());

		StackPane.setAlignment(promptLabel, Pos.CENTER_LEFT);
		promptLabel.setStyle("-fx-font-size : 22px; -fx-text-fill : #B9B9B9;");
		titleField.setStyle("-fx-font-size : 22px; -fx-background-color : transparent");
		// titleField.setPromptText("Event Title");

		// Event Buttons initialization
		optionalEventButton = new JFXButton("Optional");
		standardEventButton = new JFXButton("Standard");
		importantEventButton = new JFXButton("Important");
		criticalEventButton = new JFXButton("Critical");
		eventButtonPane.getChildren().addAll(optionalEventButton, standardEventButton, importantEventButton,
				criticalEventButton);

		optionalEventButton.getStyleClass().add("event_button");
		standardEventButton.getStyleClass().add("event_button");
		importantEventButton.getStyleClass().add("event_button");
		criticalEventButton.getStyleClass().add("event_button");

		cleanSelection();

		// Event Button Listeners
		optionalEventButton.setOnAction(e -> {
			cleanSelection();
			eventType = 1;
			optionalEventButton.setStyle("-fx-background-color : #4C95CE");
		});

		standardEventButton.setOnAction(e -> {
			cleanSelection();
			eventType = 2;
			standardEventButton.setStyle("-fx-background-color : #81C457");
		});

		importantEventButton.setOnAction(e -> {
			cleanSelection();
			eventType = 3;
			importantEventButton.setStyle("-fx-background-color : #F8D566");
		});

		criticalEventButton.setOnAction(e -> {
			cleanSelection();
			eventType = 4;
			criticalEventButton.setStyle("-fx-background-color : #E85569");
		});

		headerPane.getChildren().addAll(titlePane, eventButtonPane);

		String timeStamps[] = { "1 am", "2 am", "3 am", "4 am", "5 am", "6 am", "7 am", " 8 am", "9 am", "10 am",
				"11 am", "12 pm", "1 pm", "2 pm", "3 pm", "4 pm", "5 pm", "6 pm", "7 pm", "8 pm", "9 pm", "10 pm",
				"11 pm" };

		VBox bodyPane = new VBox(15);
		bodyPane.setPadding(new Insets(20, 0, 0, 0));

		HBox timePane = new HBox(10);

		Label noteLabel = new Label("Event Description");
		noteLabel.setStyle("-fx-font-size: 14px;");

		eventNote = new TextArea();
		eventNote.setId("noteArea");

		eventNote.setPrefWidth(100);
		eventNote.setPrefHeight(100);

		eventNote.wrapTextProperty().set(true);
		eventNote.setPromptText("Event Description..");

		bodyPane.getChildren().addAll(timePane, noteLabel, eventNote);

		dateField = new CalendarTextField();
		dateField.setPromptText("Event Date");

		beginHourButt = new JFXComboBox<String>();
		beginHourButt.setPromptText(" Start");

		endHourButt = new JFXComboBox<String>();
		endHourButt.setPromptText(" End");

		Label untilLabel = new Label(" - ");
		untilLabel.setStyle("-fx-font-size : 24px;");

		beginHourButt.getItems().addAll(timeStamps);
		endHourButt.getItems().addAll(timeStamps);

		timePane.getChildren().addAll(dateField, beginHourButt, untilLabel, endHourButt);

		content.setBody(bodyPane);

		JFXButton canselButton = new JFXButton("Cansel");
		canselButton.getStyleClass().add("rectangle_button");
		canselButton.setOnAction(e -> {
			this.close();
		});

		JFXButton addButton = new JFXButton("Add event");
		addButton.setId("saveButton");
		HBox actionButtonPane = new HBox(15);
		addButton.setOnAction(e -> {
			// TODO : add event
			this.close();
		});

		actionButtonPane.setPadding(new Insets(20, 0, 0, 0));
		actionButtonPane.getChildren().addAll(canselButton, addButton);

		content.setHeading(headerPane);
		content.setActions(actionButtonPane);

		setDialogContainer(stackPane);
		setContent(content);

		optionalEventButton.fire();

		setOnDialogOpened((event) -> {
			titleField.requestFocus();
		});
	}

	private void cleanSelection() {
		eventType = -1;
		optionalEventButton.setStyle("-fx-background-color : #BDC6CC ");
		standardEventButton.setStyle("-fx-background-color : #BDC6CC ");
		importantEventButton.setStyle("-fx-background-color : #BDC6CC ");
		criticalEventButton.setStyle("-fx-background-color : #BDC6CC ");
	}

	public void clear() {
		titleField.setText("");
		dateField.setText("");
		beginHourButt.getSelectionModel().select(0);
		endHourButt.getSelectionModel().select(1);
		eventNote.setText("");

		optionalEventButton.fire();
	}

	public int getEventType() {
		return eventType;
	}

}
