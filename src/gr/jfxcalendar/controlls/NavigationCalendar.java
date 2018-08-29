package gr.jfxcalendar.controlls;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.jfoenix.controls.JFXButton;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class NavigationCalendar extends VBox {

	private final int PREVIOUS = 0;
	private final int CURRENT = 1;
	private final int NEXT = 2;

	private final String MONTHS[] = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

	private TilePane navigationCalendarGrid;
	private JFXButton selectedCalendarCell;

	private IntegerProperty currentYearProperty = new SimpleIntegerProperty();
	private StringProperty currentMonthProperty = new SimpleStringProperty();
	private IntegerProperty currentDayProperty = new SimpleIntegerProperty();
	private LocalDate selectedDate;

	private IntegerProperty markedCell = new SimpleIntegerProperty();

	public StringProperty selectedDateProperty = new SimpleStringProperty();

	public NavigationCalendar() {
		// Calendar pane

		setId("navigation_calendar");
		setPadding(new Insets(15, 32, 15, 32));

		// Toolbar pane
		HBox navigationPane = new HBox(10);

		// initialize current selected date;
		selectedDate = LocalDate.now();

		// Calendar Grid
		createCalendarGrid();

		// Add all panes to main pane
		getChildren().addAll(navigationPane, navigationCalendarGrid);

		// tool bar controls initialization
		Label dateLabel = new Label("");
		dateLabel.textProperty().bind(new StringBinding() {
			{
				bind(currentMonthProperty);
				bind(currentYearProperty);
			}

			@Override
			protected String computeValue() {
				return currentMonthProperty.get() + " " + currentYearProperty.get();
			}

		});
		dateLabel.setId("calendar_label_sm");

		Pane emptyPane = new Pane();
		HBox.setHgrow(emptyPane, Priority.ALWAYS);

		FontAwesomeIconView previousMonthIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_LEFT);
		FontAwesomeIconView nextMonthIcon = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_RIGHT);

		JFXButton prevMonthButton = new JFXButton();
		JFXButton nextMonthButton = new JFXButton();

		prevMonthButton.getStyleClass().add("circle_buttom_sm");
		nextMonthButton.getStyleClass().add("circle_buttom_sm");

		prevMonthButton.setGraphic(previousMonthIcon);
		nextMonthButton.setGraphic(nextMonthIcon);

		prevMonthButton.setOnAction(e -> {
			moveMonthBackwardOnNavCalendar();
			clearSelection();
		});

		nextMonthButton.setOnAction(e -> {
			moveMonthForwardOnNavCalendar();
			clearSelection();
		});

		navigationPane.getChildren().addAll(dateLabel, emptyPane, prevMonthButton, nextMonthButton);
	}

	private void createCalendarGrid() {
		getStylesheets().add(this.getClass().getResource("/resources/styles/CalendarScheduler.css").toExternalForm());
		
		navigationCalendarGrid = new TilePane();
		navigationCalendarGrid.setTileAlignment(Pos.CENTER);
		navigationCalendarGrid.setPrefColumns(7);
		navigationCalendarGrid.setPadding(new Insets(10, 0, 0, 0));

		refreshCalendar();
	}
	
	

	public void refreshCalendar() {
		// Remove all the nodes inside the 'calendar'
		navigationCalendarGrid.getChildren().clear();

		// Add headers (days) and their tooltips
		String tooltipText[] = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		String dayText[] = { "S", "M", "T", "W", "T", "F", "S" };
		for (int i = 0; i < 7; i++) {
			JFXButton dayButton = new JFXButton(dayText[i]);
			dayButton.setTooltip(new Tooltip(tooltipText[i]));
			dayButton.getStyleClass().add("flat_button");
			navigationCalendarGrid.getChildren().add(dayButton);
		}

		// Find selected date informations and update properties
		// used in date display
		int selectedMonthIndex = selectedDate.getMonthValue();
		int lenghtOfSelectedMonth = selectedDate.lengthOfMonth();
		int selectedYear = selectedDate.getYear();

		currentMonthProperty.set(MONTHS[selectedMonthIndex - 1]);
		currentYearProperty.set(selectedYear);
		currentDayProperty.set(selectedDate.getDayOfMonth());

		// in order to display the day correctly we need to find
		// the start of our calendar which is not always starts on sunday
		// so find the previous month
		LocalDate prevMonthDate = selectedDate.minusMonths(1);
		int lenghtOfPrevMonth = prevMonthDate.lengthOfMonth();

		// Find the when (day) was the first day of the month ( ex. Friday)
		LocalDate firstOfMonthDate = LocalDate.of(selectedYear, selectedMonthIndex, 1);
		int firstDayIndex = findDayIndex(firstOfMonthDate.getDayOfWeek()) - 1;

		// Add the previous days until our current first day
		for (int i = 0; i <= firstDayIndex; i++) {
			String dayIndexStr = String.valueOf(lenghtOfPrevMonth - firstDayIndex + i);
			JFXButton dayButton = createCalendarCell(dayIndexStr, PREVIOUS);
			dayButton.getStyleClass().add("calendar_cell_inactive");
			navigationCalendarGrid.getChildren().add(dayButton);
		}

		// Fill the 'Calendar' with the days of the month
		for (int i = 1; i <= lenghtOfSelectedMonth; i++) {
			JFXButton dayButton = createCalendarCell(String.valueOf(i), CURRENT);
			dayButton.getStyleClass().add("calendar_cell_active");
			navigationCalendarGrid.getChildren().add(dayButton);

			if (i == markedCell.get()) {
				// it was clicked so marked it as selected
				dayButton.setStyle("-fx-background-color : #4285F4; -fx-text-fill : WHITE");
				selectedCalendarCell = dayButton;
			}
		}

		// Fill the rest of the calendar with the days of the next month
		// Until we have 7 rows in our calendar ( thus 49 = 7 days * 7 rows )
		int index = 1;
		for (int i = navigationCalendarGrid.getChildren().size(); i < 49; i++) {
			JFXButton dayButton = createCalendarCell(String.valueOf(index++), NEXT);
			dayButton.getStyleClass().add("calendar_cell_inactive");
			navigationCalendarGrid.getChildren().add(dayButton);
		}
	}

	public void select(int day) {
		markedCell.set(day);
	}
	
	public void setSelectedDate(LocalDate selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	private JFXButton createCalendarCell(String text, int monthIndex) {

		JFXButton button = new JFXButton(text);
		button.setPrefWidth(28);

		button.setOnAction(e -> {
			clearSelection();

			if (monthIndex == CURRENT) {
				button.setStyle("-fx-background-color : #4285F4; -fx-text-fill : WHITE");
				selectedCalendarCell = button;
				markedCell.set(Integer.parseInt(text));
				selectedDate = selectedDate.withDayOfMonth(markedCell.get());
				navigateToCurrent(text);
			} else if (monthIndex == PREVIOUS) {
				markedCell.set(Integer.parseInt(text));
				navigateToPrevious(text);
				selectedDate = selectedDate.withDayOfMonth(markedCell.get());
			} else {
				markedCell.set(Integer.parseInt(text));
				navigateToNext(text);
				selectedDate = selectedDate.withDayOfMonth(markedCell.get());
			}
			

			
			selectedDateProperty.set(getSelectedDate());
			// Notify for the change ( We are using a InvalidationListener )
			// TODO : Change to StringBinding or something in order to use
			// ChangeListener instead.
			selectedDateProperty.get();
	
		});

		return button;
	}

	private void clearSelection() {
		if (selectedCalendarCell != null) {
			selectedCalendarCell.setStyle("");
		}
	}

	private void moveMonthBackwardOnNavCalendar() {
		selectedDate = selectedDate.minusMonths(1);
		refreshCalendar();
	}

	private void moveMonthForwardOnNavCalendar() {
		selectedDate = selectedDate.plusMonths(1);
		refreshCalendar();
	}

	private int findDayIndex(DayOfWeek dayOfWeek) {
		switch (dayOfWeek) {
		case SUNDAY:
			return 0;
		case MONDAY:
			return 1;
		case TUESDAY:
			return 2;
		case WEDNESDAY:
			return 3;
		case THURSDAY:
			return 4;
		case FRIDAY:
			return 5;
		case SATURDAY:
			return 6;
		}
		return 0;
	}

	private void navigateToNext(String text) {

		// Move and refresh the navigation 'calendar' to previous month
		moveMonthForwardOnNavCalendar();

		// TODO : move the main calendar too.
	}

	private void navigateToPrevious(String text) {

		// Move and refresh the navigation 'calendar' to previous month
		moveMonthBackwardOnNavCalendar();

		// TODO : move the main calendar too.

	}

	private void navigateToCurrent(String text) {

	}

	public StringProperty getSelectedDateProperty() {
		return selectedDateProperty;
	}

	public String getSelectedDate() {
		String date = markedCell.get() + " " + MONTHS[selectedDate.getMonthValue()-1].substring(0, 3) + " " + selectedDate.getYear();
		return date;
	}
	
	public LocalDate getLocalDate() {
		return selectedDate;
	}
}
