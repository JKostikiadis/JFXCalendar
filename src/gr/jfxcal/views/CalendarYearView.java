package gr.jfxcalendar.views;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class CalendarYearView extends CalendarView {
	private final String MONTHS[] = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

	private final int monthPageWidth = 200; // ~= width of each month's grid
	private final int monthsPageWidth = 230;

	private final int PREVIOUS = 0;
	private final int CURRENT = 1;
	private final int NEXT = 2;

	private JFXButton selectedCalendarCell;

	private LocalDate prevDate;

	private ScrollPane mainPane;

	private ArrayList<TilePane> monthsList = new ArrayList<TilePane>();

	public CalendarYearView(StackPane parentPane) {
		super(parentPane);

		mainPane = new ScrollPane();
		mainPane.setStyle("-fx-background-color: WHITE; -fx-border-width : 1 1 1 1; -fx-border-color : #E0E0E0;");
		getChildren().add(0, mainPane);

		mainPane.setPadding(new Insets(20));

		// holding each month's little calendar grid
		TilePane tilePane = new TilePane();
		tilePane.setStyle("-fx-background-color : white;");

		tilePane.setMinSize(monthPageWidth, monthsPageWidth);
		tilePane.setPrefWidth(monthPageWidth * 3);

		tilePane.setVgap(20);

		mainPane.widthProperty().addListener(e -> {

			double currentWidth = mainPane.getWidth() - 130;

			// set the width of our calendar pane
			tilePane.setPrefWidth(currentWidth);

			double hGap = 25;

			if (currentWidth > 4 * (monthPageWidth) + 60) {
				// we can have 4 pages in one row and we
				// do not want to display more than 4
				double sumGap = currentWidth - 4 * (monthPageWidth);
				hGap = sumGap / 3.0;
			} else {
				double count = Math.floor((currentWidth) / (monthPageWidth + 15));
				if (count == 4) {
					count = 3;
				}
				double sumGap = currentWidth - count * monthPageWidth;
				hGap = sumGap / (count - 1);
			}

			if (hGap < 25) {
				hGap = 25;
			}

			tilePane.setHgap(hGap);
		});

		for (int i = 1; i <= 12; i++) {
			VBox box = new VBox(10);
			Label l = new Label(MONTHS[i - 1]);
			l.getStyleClass().add("calendar_label_md");
			TilePane monthCalendar = createCalendarPage(i);
			monthsList.add(monthCalendar);
			box.getChildren().addAll(l, monthCalendar);
			tilePane.getChildren().add(box);
		}

		mainPane.setContent(tilePane);
	}

	private TilePane createCalendarPage(int month) {
		TilePane calendarPage = new TilePane();
		calendarPage.setTileAlignment(Pos.CENTER);
		calendarPage.setPrefColumns(7);
		calendarPage.setPadding(new Insets(10, 0, 0, 0));

		fillCalendarPage(calendarPage, month);

		return calendarPage;
	}

	private void fillCalendarPage(TilePane calendarPage, int month) {

		// Add headers (days) and their tooltips
		String tooltipText[] = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		String dayText[] = { "S", "M", "T", "W", "T", "F", "S" };
		for (int i = 0; i < 7; i++) {
			JFXButton dayButton = new JFXButton(dayText[i]);
			dayButton.setTooltip(new Tooltip(tooltipText[i]));
			dayButton.getStyleClass().add("flat_button_md");
			calendarPage.getChildren().add(dayButton);
		}

		LocalDate selectedDate = LocalDate.now();
		// Find selected date informations and update properties
		// used in date display
		int lenghtOfSelectedMonth = selectedDate.lengthOfMonth();
		int selectedYear = selectedDate.getYear();

		// in order to display the day correctly we need to find
		// the start of our calendar which is not always starts on Sunday
		// so find the previous month
		LocalDate prevMonthDate = selectedDate.minusMonths(1);
		int lenghtOfPrevMonth = prevMonthDate.lengthOfMonth();

		// Find the when (day) was the first day of the month ( ex. Friday)
		LocalDate firstOfMonthDate = LocalDate.of(selectedYear, month, 1);
		int firstDayIndex = findDayIndex(firstOfMonthDate.getDayOfWeek()) - 1;

		// Add the previous days until our current first day
		for (int i = 0; i <= firstDayIndex; i++) {
			String dayIndexStr = String.valueOf(lenghtOfPrevMonth - firstDayIndex + i);
			JFXButton dayButton = createCalendarCell(dayIndexStr, PREVIOUS);
			dayButton.getStyleClass().add("calendar_cell_inactive");
			calendarPage.getChildren().add(dayButton);
		}

		// Fill the 'Calendar' with the days of the month
		for (int i = 1; i <= lenghtOfSelectedMonth; i++) {
			JFXButton dayButton = createCalendarCell(String.valueOf(i), CURRENT);
			dayButton.getStyleClass().add("calendar_cell_active");
			calendarPage.getChildren().add(dayButton);
		}

		// Fill the rest of the calendar with the days of the next month
		// Until we have 7 rows in our calendar ( thus 49 = 7 days * 7 rows )
		int index = 1;
		for (int i = calendarPage.getChildren().size(); i < 49; i++) {
			JFXButton dayButton = createCalendarCell(String.valueOf(index++), NEXT);
			dayButton.getStyleClass().add("calendar_cell_inactive");
			calendarPage.getChildren().add(dayButton);
		}
	}

	public void refreshCalendar(LocalDate selectedDate) {

		// Don't update if nothing change
		if (prevDate == selectedDate) {
			return;
		}

		for (int month = 1; month <= 12; month++) {
			TilePane calendarPage = monthsList.get(month - 1);
			int index = 7;

			int lenghtOfSelectedMonth = selectedDate.lengthOfMonth();
			int selectedYear = selectedDate.getYear();

			LocalDate prevMonthDate = selectedDate.minusMonths(1);
			int lenghtOfPrevMonth = prevMonthDate.lengthOfMonth();

			LocalDate firstOfMonthDate = LocalDate.of(selectedYear, month, 1);
			int firstDayIndex = findDayIndex(firstOfMonthDate.getDayOfWeek()) - 1;

			for (int i = 0; i <= firstDayIndex; i++) {
				String dayIndexStr = String.valueOf(lenghtOfPrevMonth - firstDayIndex + i);
				JFXButton dayButton = (JFXButton) calendarPage.getChildren().get(index++);
				dayButton.setText(dayIndexStr);
			}

			for (int i = 1; i <= lenghtOfSelectedMonth; i++) {
				JFXButton dayButton = (JFXButton) calendarPage.getChildren().get(index++);
				dayButton.setText(String.valueOf(i));
			}

			for (int i = calendarPage.getChildren().size(); i < 49; i++) {
				JFXButton dayButton = (JFXButton) calendarPage.getChildren().get(index);
				dayButton.setText(String.valueOf(index++));
			}
		}

		prevDate = selectedDate;
	}

	private JFXButton createCalendarCell(String text, int monthIndex) {

		JFXButton button = new JFXButton(text);
		button.setPrefWidth(28);

		button.setOnAction(e -> {
			if (selectedCalendarCell != null) {
				selectedCalendarCell.setStyle("");
			}

			if (monthIndex == CURRENT) {
				button.setStyle("-fx-background-color : #4285F4; -fx-text-fill : WHITE");
				selectedCalendarCell = button;
				// show current day events
				// TODO :
			} else if (monthIndex == PREVIOUS) {
				// TODO :
			} else {
				// TODO :
			}
		});

		return button;
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
