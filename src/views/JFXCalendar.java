package views;

import java.time.LocalDate;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.CalendarEvent;
import model.CalendarEventManager;

public class JFXCalendar extends StackPane {

	private final int DAY = 0;
	private final int WEEK = 1;
	private final int MONTH = 2;

	private JFXButton menuButton;

	private LocalDate selectedDate;

	private CalendarWeekView calendarWeekView;
	private CalendarDayView calendarDayView;

	private BorderPane mainPane;
	private int currentView;
	private NavigationCalendar navigationCalendar;
	private Label dateLabel;

	private CalendarEventManager eventManager;

	private StringProperty navigationDateProperty;

	private VBox leftPane;

	private BooleanProperty optionalFilterProperty;
	private BooleanProperty standardFilterProperty;
	private BooleanProperty importantFilterProperty;
	private BooleanProperty criticalFilterProperty;
	private BooleanProperty completedFilterProperty;
	private ToggleButton dayButton;
	private ToggleButton weekButton;
	private ToggleButton monthButton;

	private CalendarMonthView calendarMonthView;

	public static JFXButton todayButton;

	public JFXCalendar(CalendarEventManager eventManager) {

		getStylesheets().add(this.getClass().getResource("/style/CalendarScheduler.css")
				.toExternalForm());

		this.eventManager = eventManager;
		selectedDate = LocalDate.now();

		mainPane = new BorderPane();
		getChildren().add(mainPane);

		initLeftPane();
		initMainPane();
		initToolBar();
	}

	public void setEventManager(CalendarEventManager eventManager) {
		this.eventManager = eventManager;
	}

	private void initToolBar() {
		HBox toolPane = new HBox(15);
		toolPane.setAlignment(Pos.CENTER_LEFT);
		toolPane.setPadding(new Insets(10));

		String iconPaint = "#767676";

		// Initialize all the icons
		FontAwesomeIconView hideButtonIcon = new FontAwesomeIconView(
				FontAwesomeIcon.NAVICON);
		FontAwesomeIconView prevWeekButtonIcon = new FontAwesomeIconView(
				FontAwesomeIcon.ANGLE_LEFT);
		FontAwesomeIconView nextWeekButtonIcon = new FontAwesomeIconView(
				FontAwesomeIcon.ANGLE_RIGHT);
		FontAwesomeIconView viewButtonIcon = new FontAwesomeIconView(
				FontAwesomeIcon.ANGLE_DOWN);

		hideButtonIcon.setFill(Paint.valueOf(iconPaint));
		prevWeekButtonIcon.setFill(Paint.valueOf(iconPaint));
		nextWeekButtonIcon.setFill(Paint.valueOf(iconPaint));
		viewButtonIcon.setFill(Paint.valueOf(iconPaint));

		hideButtonIcon.setSize("24");
		prevWeekButtonIcon.setSize("18");
		nextWeekButtonIcon.setSize("18");
		viewButtonIcon.setSize("18");

		// The Navicon Button on the top left
		menuButton = new JFXButton();
		menuButton.getStyleClass().add("circle_buttom");
		menuButton.setGraphic(hideButtonIcon);

		menuButton.setOnAction((ActionEvent evt) -> {
			if (leftPane.getTranslateX() != 0 || mainPane.getLeft() == null) {
				mainPane.setLeft(leftPane);
			} else {
				mainPane.setLeft(null);
			}
		});

		// The Calendar Label
		Label calendarLabel = new Label("Calendar");
		calendarLabel.setId("calendar_label");

		// An Empty Space
		Pane spacerPane = new Pane();
		spacerPane.setMinWidth(15);
		spacerPane.setPrefWidth(30);

		// Today button with the date navigation controls
		todayButton = new JFXButton("Today");
		todayButton.getStyleClass().add("rectangle_button");

		JFXButton prevButton = new JFXButton();
		JFXButton nextButton = new JFXButton();

		prevButton.getStyleClass().add("circle_buttom");
		nextButton.getStyleClass().add("circle_buttom");

		prevButton.setGraphic(prevWeekButtonIcon);
		nextButton.setGraphic(nextWeekButtonIcon);

		prevButton.setOnAction(e -> {
			moveBackward();
		});

		nextButton.setOnAction(e -> {
			moveForward();
		});

		dateLabel = new Label();
		dateLabel.setId("dateLabel");

		// An Empty Space
		Pane grownedEmptyPane = new Pane();
		HBox.setHgrow(grownedEmptyPane, Priority.ALWAYS);

		// The control pane showing (day,week,month,year)
		HBox viewPane = createViewPane();

		// Add all components
		toolPane.getChildren().addAll(menuButton, calendarLabel, spacerPane, todayButton,
				prevButton, nextButton, dateLabel, grownedEmptyPane, viewPane);

		// Set toolPane on top of the BorderPane
		mainPane.setTop(toolPane);

		todayButton.setOnAction(e -> {
			// re-init the date
			selectedDate = LocalDate.now();

			if (navigationCalendar != null) {
				// Refresh navigation Calendar
				navigationCalendar.setSelectedDate(selectedDate);
				navigationCalendar.select(selectedDate.getDayOfMonth());
				navigationCalendar.refreshCalendar();
			}

			// Refresh main calendar
			displayCalendarBy(currentView);
		});

		todayButton.fire();
	}

	private void updateDate() {

		// Change the label

		dateLabel.setText(CalendarEvent.MONTHS[selectedDate.getMonthValue() - 1] + " "
				+ selectedDate.getYear());
	}

	private HBox createViewPane() {

		ToggleGroup group = new ToggleGroup();
		dayButton = new ToggleButton("Day");
		weekButton = new ToggleButton("Week");
		monthButton = new ToggleButton("Month");

		dayButton.setOnAction(e -> {
			if (!dayButton.isSelected()) {
				dayButton.setSelected(true);
				e.consume();
			} else {
				displayCalendarBy(DAY);
				currentView = DAY;
			}
		});

		weekButton.setOnAction(e -> {
			if (!weekButton.isSelected()) {
				weekButton.setSelected(true);
				e.consume();
			} else {
				displayCalendarBy(WEEK);
				currentView = WEEK;
			}
		});

		monthButton.setOnAction(e -> {
			if (!monthButton.isSelected()) {
				monthButton.setSelected(true);
				e.consume();
			} else {
				displayCalendarBy(MONTH);
				currentView = MONTH;
			}
		});

		dayButton.setToggleGroup(group);
		weekButton.setToggleGroup(group);
		monthButton.setToggleGroup(group);

		dayButton.setStyle("-fx-background-radius: 5 0 0 5");
		weekButton.setStyle("-fx-background-radius: 0 0 0 0");
		monthButton.setStyle("-fx-background-radius: 0 5 5 0");

		final int size = 27;

		dayButton.setPrefHeight(size);
		weekButton.setPrefHeight(size);
		monthButton.setPrefHeight(size);

		HBox viewPane = new HBox();
		viewPane.setAlignment(Pos.CENTER);
		viewPane.getChildren().addAll(dayButton, weekButton, monthButton);

		dayButton.setSelected(true);
		return viewPane;
	}

	private void displayCalendarBy(int displayMode) {
		this.currentView = displayMode;
		if (displayMode == DAY) {
			mainPane.setCenter(calendarDayView);
			calendarDayView.setAddButtonEnable(false);
			calendarDayView.refreshCalendar(selectedDate);
		} else if (displayMode == WEEK) {
			mainPane.setCenter(calendarWeekView);
			calendarWeekView.setAddButtonEnable(true);
			calendarWeekView.refreshCalendar(selectedDate);
		} else if (displayMode == MONTH) {
			mainPane.setCenter(calendarMonthView);
			calendarMonthView.setAddButtonEnable(true);
			calendarMonthView.refreshCalendar(selectedDate);
		}
		updateDate();
	}

	private void initLeftPane() {

		leftPane = new VBox(5);
		leftPane.setPrefWidth(260);
		leftPane.setMinWidth(260);
		leftPane.setMaxWidth(260);

		navigationCalendar = new NavigationCalendar();

		navigationDateProperty = navigationCalendar.getSelectedDateProperty();

		navigationDateProperty.addListener(e -> {
			selectedDate = navigationCalendar.getLocalDate();
			displayCalendarBy(currentView);
		});

		mainPane.setLeft(leftPane);

		leftPane.getChildren().add(navigationCalendar);

		// CheckBox initiliazation
		VBox checkBoxPane = new VBox(9);
		checkBoxPane.setAlignment(Pos.CENTER_LEFT);
		VBox.setMargin(checkBoxPane, new Insets(0, 0, 0, 45));

		Label displayEventLabel = new Label("Events");
		displayEventLabel.setStyle("-fx-font-size : 16;");
		displayEventLabel.setPadding(new Insets(0, 0, 5, -20));

		JFXCheckBox optionalCheckBox = new JFXCheckBox("Optional");
		JFXCheckBox standardCheckBox = new JFXCheckBox("Normal");
		JFXCheckBox importantCheckBox = new JFXCheckBox("Important");
		JFXCheckBox criticalCheckBox = new JFXCheckBox("Urgent");
		JFXCheckBox completedCheckBox = new JFXCheckBox("Completed");

		optionalFilterProperty = optionalCheckBox.selectedProperty();
		standardFilterProperty = standardCheckBox.selectedProperty();
		importantFilterProperty = importantCheckBox.selectedProperty();
		criticalFilterProperty = criticalCheckBox.selectedProperty();
		completedFilterProperty = completedCheckBox.selectedProperty();

		optionalCheckBox.setStyle(
				"-jfx-checked-color : #4C95CE; -jfx-unchecked-color : #4C95CE ;");
		standardCheckBox.setStyle(
				"-jfx-checked-color : #81C457; -jfx-unchecked-color : #81C457 ;");
		importantCheckBox.setStyle(
				"-jfx-checked-color: #F7C531; -jfx-unchecked-color : #F7C531 ;");
		criticalCheckBox.setStyle(
				"-jfx-checked-color : #E85569; -jfx-unchecked-color : #E85569 ;");
		completedCheckBox.setStyle(
				"-jfx-checked-color : #344563; -jfx-unchecked-color : #344563 ;");

		optionalCheckBox.fire();
		standardCheckBox.fire();
		importantCheckBox.fire();
		criticalCheckBox.fire();
		completedCheckBox.fire();

		FontAwesomeIconView starView = new FontAwesomeIconView(FontAwesomeIcon.STAR);
		FontAwesomeIconView flagView = new FontAwesomeIconView(FontAwesomeIcon.FLAG);
		FontAwesomeIconView tagView = new FontAwesomeIconView(FontAwesomeIcon.TAG);

		starView.setFill(Color.web("#FFD907"));
		flagView.setFill(Color.web("#2C3AB8"));
		tagView.setFill(Color.web("#B55231"));

		starView.setSize("18");
		flagView.setSize("18");
		tagView.setSize("18");

		Label perWeekEventLabel = new Label(" Weekly");
		perWeekEventLabel.setGraphic(tagView);
		perWeekEventLabel.setPadding(new Insets(15, 0, 5, 0));

		Label perMonthEventLabel = new Label(" Monthly");
		perMonthEventLabel.setGraphic(flagView);
		perMonthEventLabel.setPadding(new Insets(0, 0, 5, 0));

		Label yearlyEventLabel = new Label(" Yearly");
		yearlyEventLabel.setGraphic(starView);
		yearlyEventLabel.setPadding(new Insets(0, 0, 5, 0));

		Separator separator = new Separator();
		VBox.setMargin(separator, new Insets(0, 0, 0, -45));

		checkBoxPane.getChildren().addAll(displayEventLabel, optionalCheckBox,
				standardCheckBox, importantCheckBox, criticalCheckBox, completedCheckBox,
				separator, perWeekEventLabel, perMonthEventLabel, yearlyEventLabel);

		leftPane.getChildren().add(checkBoxPane);

	}

	private void initMainPane() {
		calendarDayView = new CalendarDayView(this, eventManager);
		calendarWeekView = new CalendarWeekView(this, eventManager);
		calendarMonthView = new CalendarMonthView(this, eventManager);

		mainPane.setCenter(calendarDayView);
	}

	private void moveBackward() {
		if (currentView == DAY) {
			selectedDate = selectedDate.minusDays(1);
			calendarDayView.refreshCalendar(selectedDate);
		} else if (currentView == WEEK) {
			selectedDate = selectedDate.minusWeeks(1);
			calendarWeekView.refreshCalendar(selectedDate);
		} else if (currentView == MONTH) {
			selectedDate = selectedDate.minusMonths(1);
			calendarMonthView.refreshCalendar(selectedDate);
		}
		updateDate();
	}

	private void moveForward() {
		if (currentView == DAY) {
			selectedDate = selectedDate.plusDays(1);
			calendarDayView.refreshCalendar(selectedDate);
		} else if (currentView == WEEK) {
			selectedDate = selectedDate.plusWeeks(1);
			calendarWeekView.refreshCalendar(selectedDate);
		} else if (currentView == MONTH) {
			selectedDate = selectedDate.plusMonths(1);
			calendarMonthView.refreshCalendar(selectedDate);
		}
		updateDate();
	}

	public void removeEvent(CalendarEvent event) {
		eventManager.removeEvent(event);
	}

	public ObservableList<CalendarEvent> getEvents(int dayOfMonth, int month, int year) {
		return eventManager.getEventsOn(LocalDate.of(year, month, dayOfMonth));
	}

	public BooleanProperty getOptionalFilterProperty() {
		return optionalFilterProperty;
	}

	public BooleanProperty getStandardFilterProperty() {
		return standardFilterProperty;
	}

	public BooleanProperty getImportantFilterProperty() {
		return importantFilterProperty;
	}

	public BooleanProperty getCriticalFilterProperty() {
		return criticalFilterProperty;
	}

	public BooleanProperty getCompletedFilterProperty() {
		return completedFilterProperty;
	}

	public void selectDate(LocalDate date) {
		selectedDate = date;
		dayButton.fire();
	}

	public void refreshCalendar() {
		if (currentView == 0) {
			calendarDayView.refreshCalendar(selectedDate);
		} else {
			calendarWeekView.refreshCalendar(selectedDate);
		}
	}
}