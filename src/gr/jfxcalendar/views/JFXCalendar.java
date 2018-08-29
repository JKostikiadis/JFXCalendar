package gr.jfxcalendar.views;

import java.time.LocalDate;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDrawer;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import gr.jfxcalendar.controlls.NavigationCalendar;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class JFXCalendar extends StackPane {

	private final int DAY = 0;
	private final int WEEK = 1;
	private final int MONTH = 2;
	private final int YEAR = 3;

	private final String MONTHS[] = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

	private JFXButton menuButton;

	private LocalDate selectedDate;

	private CalendarYearView calendarYearView;
	private CalendarMonthView calendarMonthView;
	private CalendarWeekView calendarWeekView;
	private CalendarDayView calendarDayView;

	private BorderPane mainPane;
	private int currentView;
	private NavigationCalendar navigationCalendar;
	private Label dateLabel;

	public JFXCalendar() {
		getStylesheets().add(this.getClass().getResource("/resources/styles/CalendarScheduler.css").toExternalForm());

		selectedDate = LocalDate.now();

		mainPane = new BorderPane();
		getChildren().add(mainPane);

		initMainPane();
		initToolBar();
		initLeftPane();
	}

	private void initToolBar() {
		HBox toolPane = new HBox(15);
		toolPane.setAlignment(Pos.CENTER_LEFT);
		toolPane.setPadding(new Insets(10));

		String iconPaint = "#767676";

		// Initialize all the icons
		FontAwesomeIconView hideButtonIcon = new FontAwesomeIconView(FontAwesomeIcon.NAVICON);
		FontAwesomeIconView prevWeekButtonIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_LEFT);
		FontAwesomeIconView nextWeekButtonIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_RIGHT);
		FontAwesomeIconView viewButtonIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOWN);

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

		menuButton.setOnAction(e -> {
			
		});

		// The Calendar Label
		Label calendarLabel = new Label("Calendar");
		calendarLabel.setId("calendar_label");

		// An Empty Space
		Pane spacerPane = new Pane();
		spacerPane.setPrefWidth(60);

		// Today button with the date navigation controls
		JFXButton todayButton = new JFXButton("TODAY");
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
		toolPane.getChildren().addAll(menuButton, calendarLabel, spacerPane, todayButton, prevButton,
				nextButton, dateLabel, grownedEmptyPane, viewPane);

		// Set toolPane on top of the BorderPane
		mainPane.setTop(toolPane);

		todayButton.setOnAction(e -> {
			// re-init the date
			selectedDate = LocalDate.now();
			
			if(navigationCalendar != null) {
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
		if(currentView == YEAR) {
			dateLabel.setText(String.valueOf(selectedDate.getYear()));
		}else {
			dateLabel.setText(MONTHS[selectedDate.getMonthValue()-1] + " " + selectedDate.getYear());
		}
		
	}
	
private HBox createViewPane() {

		ToggleGroup group = new ToggleGroup();
		ToggleButton dayButton = new ToggleButton("Day");
		ToggleButton weekButton = new ToggleButton("Week");
		ToggleButton monthButton = new ToggleButton("Month");
		ToggleButton yearButton = new ToggleButton("Year");

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

		yearButton.setOnAction(e -> {
			if (!yearButton.isSelected()) {
				yearButton.setSelected(true);
				e.consume();
			} else {
				displayCalendarBy(YEAR);
				currentView = YEAR;
			}
		});

		weekButton.fire(); // selected by default

		dayButton.setToggleGroup(group);
		weekButton.setToggleGroup(group);
		monthButton.setToggleGroup(group);
		yearButton.setToggleGroup(group);

		dayButton.setStyle("-fx-background-radius: 5 0 0 5");
		weekButton.setStyle("-fx-background-radius: 0");
		monthButton.setStyle("-fx-background-radius: 0");
		yearButton.setStyle("-fx-background-radius: 0 5 5 0");

		final int size = 27;

		dayButton.setPrefHeight(size);
		weekButton.setPrefHeight(size);
		monthButton.setPrefHeight(size);
		yearButton.setPrefHeight(size);

		HBox viewPane = new HBox();
		viewPane.setAlignment(Pos.CENTER);
		viewPane.getChildren().addAll(dayButton, weekButton, monthButton, yearButton);

		return viewPane;
	}

	private void displayCalendarBy(int displayMode) {
		this.currentView = displayMode;
		if (displayMode == DAY) {
			mainPane.setCenter(calendarDayView);
			calendarDayView.refreshCalendar(selectedDate);
		} else if (displayMode == WEEK) {
			mainPane.setCenter(calendarWeekView);
			calendarWeekView.refreshCalendar(selectedDate);
		} else if (displayMode == MONTH) {
			mainPane.setCenter(calendarMonthView);
			calendarMonthView.refreshCalendar(selectedDate);
		} else if (displayMode == YEAR) {
			mainPane.setCenter(calendarYearView);
			calendarYearView.refreshCalendar(selectedDate);
		}
		updateDate();
	}

	private StringProperty navigationDateProperty;
	
	private void initLeftPane() {

		VBox leftPane = new VBox(15);

		navigationCalendar = new NavigationCalendar();
		
		navigationDateProperty = navigationCalendar.getSelectedDateProperty();
		
		navigationDateProperty.addListener(e->{
			selectedDate = navigationCalendar.getLocalDate();
			displayCalendarBy(currentView);
		});

		mainPane.setLeft(leftPane);

		leftPane.getChildren().add(navigationCalendar);

		// CheckBox initiliazation
		VBox checkBoxPane = new VBox(15);
		checkBoxPane.setAlignment(Pos.CENTER_LEFT);
		VBox.setMargin(checkBoxPane, new Insets(0, 0, 0, 30));

		Label displayEventLabel = new Label("Events");
		displayEventLabel.setStyle("-fx-font-size : 16;");
		displayEventLabel.setPadding(new Insets(0, 0, 5, 0));

		JFXCheckBox optionalCheckBox = new JFXCheckBox("Optional");
		JFXCheckBox standardCheckBox = new JFXCheckBox("Standard");
		JFXCheckBox importantCheckBox = new JFXCheckBox("Important");
		JFXCheckBox criticalCheckBox = new JFXCheckBox("Critical");

		optionalCheckBox.setStyle("-jfx-checked-color : #4C95CE; -jfx-unchecked-color : #4C95CE ;");
		standardCheckBox.setStyle("-jfx-checked-color : #81C457; -jfx-unchecked-color : #81C457 ");
		importantCheckBox.setStyle("-jfx-checked-color: #F8D566; -jfx-unchecked-color : #F8D566 ");
		criticalCheckBox.setStyle("-jfx-checked-color : #E85569; -jfx-unchecked-color : #E85569 ");

		optionalCheckBox.fire();
		standardCheckBox.fire();
		importantCheckBox.fire();
		criticalCheckBox.fire();

		checkBoxPane.getChildren().addAll(displayEventLabel, optionalCheckBox, standardCheckBox, importantCheckBox,
				criticalCheckBox);

		leftPane.getChildren().add(checkBoxPane);

	}

	private void initMainPane() {
		calendarDayView = new CalendarDayView(this);
		calendarWeekView = new CalendarWeekView(this);
		calendarMonthView = new CalendarMonthView(this);
		calendarYearView = new CalendarYearView(this);
		mainPane.setCenter(new CalendarWeekView(this));
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
		} else {
			selectedDate = selectedDate.minusYears(1);
			calendarYearView.refreshCalendar(selectedDate);
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
		} else {
			selectedDate = selectedDate.plusYears(1);
			calendarYearView.refreshCalendar(selectedDate);
		}
		updateDate();
	}

}
