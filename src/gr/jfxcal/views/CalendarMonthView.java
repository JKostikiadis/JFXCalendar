package gr.jfxcal.views;

import java.time.DayOfWeek;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class CalendarMonthView extends CalendarView {

	private final int INACTIVE = 0;
	private final int ACTIVE = 1;

	private TilePane calendarPage;

	private LocalDate prevDate;

	private ScrollPane mainPane;

	public CalendarMonthView(StackPane parentPane) {
		super(parentPane);

		mainPane = new ScrollPane();
		mainPane.setPrefSize(650, 500);
		mainPane.setStyle("-fx-background-color: WHITE; -fx-border-width : 1 1 0 0; -fx-border-color : #E0E0E0;");
		getChildren().add(0, mainPane);

//		mainPane.setHbarPolicy(ScrollBarPolicy.NEVER);
//		mainPane.setVbarPolicy(ScrollBarPolicy.NEVER);

		calendarPage = new TilePane();
		calendarPage.setPrefColumns(7);
		calendarPage.setStyle("-fx-background-color: WHITE");

		mainPane.setContent(calendarPage);

		for (int i = 0; i < 42; i++) {
			VBox entryPane = new VBox(5);
			entryPane.setOnMouseClicked(e -> {
				// Add event
				// TODO :
			});
			entryPane.setPadding(new Insets(5, 0, 0, 5));
			entryPane.setId("calendaryEntryPane");

			entryPane.prefWidthProperty().bind(mainPane.widthProperty().divide(7.0).subtract(3));
			entryPane.prefHeightProperty().bind(mainPane.heightProperty().divide(6.0));
			calendarPage.getChildren().add(entryPane);
		}

		refreshCalendar(LocalDate.now());
	}

	public void refreshCalendar(LocalDate selectedDate) {

		// Don't update if nothing change
		if (prevDate == selectedDate) {
			return;
		}

		// clear previous entries
		for (int i = 0; i < 42; i++) {
			VBox columnEntry = (VBox) calendarPage.getChildren().get(i);
			columnEntry.getChildren().clear();
		}

		// Add headers (days)
		String dayText[] = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		for (int i = 0; i < 7; i++) {
			VBox columnEntry = (VBox) calendarPage.getChildren().get(i);
			columnEntry.getChildren().add(createCalendarEntry(dayText[i], INACTIVE));
		}

		int index = 0;

		// Find selected date informations and update properties
		// used in date display
		int lenghtOfSelectedMonth = selectedDate.lengthOfMonth();
		int selectedYear = selectedDate.getYear();
		int month = selectedDate.getMonthValue();

		// in order to display the day correctly we need to find
		// the start of our calendar which is not always starts on sunday
		// so find the previous month
		LocalDate prevMonthDate = selectedDate.minusMonths(1);
		int lenghtOfPrevMonth = prevMonthDate.lengthOfMonth();

		// Find the when (day) was the first day of the month ( ex. Friday)
		LocalDate firstOfMonthDate = LocalDate.of(selectedYear, month, 1);
		int firstDayIndex = findDayIndex(firstOfMonthDate.getDayOfWeek());

		
		// Add the previous days until our current first day
		for (int i = 1; i <= firstDayIndex; i++) {
			VBox columnEntry = (VBox) calendarPage.getChildren().get(index++);
			String dayIndexStr = String.valueOf(lenghtOfPrevMonth - firstDayIndex + i);
			Label dayEntry = createCalendarEntry(dayIndexStr, INACTIVE);
			columnEntry.getChildren().add(dayEntry);
		}
		
		// Fill the 'Calendar' with the days of the month
		for (int i = 1; i <= lenghtOfSelectedMonth; i++) {
			VBox columnEntry = (VBox) calendarPage.getChildren().get(index);
			Label dayEntry = createCalendarEntry(String.valueOf(i), ACTIVE);
			columnEntry.getChildren().add(dayEntry);
			index++;
		}

		int dayIndex = 1;
		for (int i = index; i < 42; i++) {

			VBox columnEntry = (VBox) calendarPage.getChildren().get(index++);
			Label dayEntry = createCalendarEntry(String.valueOf(dayIndex++), INACTIVE);
			columnEntry.getChildren().add(dayEntry);
		}

		prevDate = selectedDate;
	}

	private Label createCalendarEntry(String dayText, int state) {

		Label label = new Label(dayText);
		if (state == ACTIVE) {
			label.getStyleClass().add("label_entry_active");
		} else {
			label.getStyleClass().add("label_entry_inactive");
		}

		return label;
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
}
