package views;

import java.time.DayOfWeek;
import java.time.LocalDate;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import model.CalendarEvent;
import model.CalendarEventManager;

public class CalendarMonthView extends CalendarView {

	private final int INACTIVE = 0;
	private final int ACTIVE = 1;

	private TilePane calendarPage;

	private ScrollPane mainPane;

	private CalendarEventManager eventManager;

	public CalendarMonthView(JFXCalendar parentPane, CalendarEventManager eventManager) {
		super(parentPane);

		this.eventManager = eventManager;

		mainPane = new ScrollPane();

		StackPane.setMargin(mainPane, new Insets(0, 10, 5, 0));
		mainPane.setStyle(
				"-fx-background-color: WHITE; -fx-border-width : 1 1 0 0; -fx-border-color : #E0E0E0;");
		getChildren().add(0, mainPane);

		mainPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		mainPane.setVbarPolicy(ScrollBarPolicy.NEVER);

		calendarPage = new TilePane();
		calendarPage.setPrefColumns(7);
		calendarPage.setStyle("-fx-background-color: WHITE");

		mainPane.setContent(calendarPage);

		for (int i = 0; i < 42; i++) {
			VBox entryPane = new VBox(5);

			entryPane.setPadding(new Insets(5, 5, 5, 5));
			entryPane.setId("calendaryEntryPane");

			entryPane.prefWidthProperty()
					.bind(mainPane.widthProperty().divide(7.0).subtract(1.3));
			entryPane.prefHeightProperty()
					.bind(mainPane.heightProperty().divide(6.0).subtract(1.3));
			calendarPage.getChildren().add(entryPane);
		}

	}

	public void refreshCalendar(LocalDate selectedDate) {

		// clear previous entries
		for (int i = 0; i < 42; i++) {
			VBox columnEntry = (VBox) calendarPage.getChildren().get(i);
			columnEntry.getChildren().clear();
		}

		// Add headers (days)
		String dayText[] = CalendarEvent.DAYS_SHORT_NAMES;
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
		// the start of our calendar which is not always starts on monday
		// so find the previous month
		LocalDate prevMonthDate = selectedDate.minusMonths(1);
		int lenghtOfPrevMonth = prevMonthDate.lengthOfMonth();

		// Find the "when' (day) was the first day of the month ( ex. Friday)
		LocalDate firstOfMonthDate = LocalDate.of(selectedYear, month, 1);
		int firstDayIndex = findDayIndex(firstOfMonthDate.getDayOfWeek());

		// Add the previous days until our current first day
		for (int i = 1; i <= firstDayIndex; i++) {
			VBox columnEntry = (VBox) calendarPage.getChildren().get(index++);

			int day = lenghtOfPrevMonth - firstDayIndex + i;
			String dayIndexStr = String.valueOf(day);
			Label dayEntry = createCalendarEntry(dayIndexStr, INACTIVE);

			LocalDate currentDayDate;
			if (month - 1 == 0) {
				currentDayDate = LocalDate.of(selectedYear - 1, 12, day);
			} else {
				currentDayDate = LocalDate.of(selectedYear, month - 1, day);
			}

			columnEntry.getChildren().add(dayEntry);
			ObservableList<CalendarEvent> eventList = eventManager
					.getEventsOn(currentDayDate);

			columnEntry.setOnMouseClicked(e -> {
				rootParentPane.selectDate(currentDayDate);
			});

			if (!eventList.isEmpty()) {
				Node eventPane = createCustomPane(columnEntry, eventList);
				eventPane.setOpacity(0.7);
				columnEntry.getChildren().add(eventPane);
			}
		}

		// Fill the 'Calendar' with the days of the month
		for (int i = 1; i <= lenghtOfSelectedMonth; i++) {
			VBox columnEntry = (VBox) calendarPage.getChildren().get(index);
			Label dayEntry = createCalendarEntry(String.valueOf(i), ACTIVE);
			columnEntry.getChildren().add(dayEntry);
			index++;

			LocalDate currentDayDate = LocalDate.of(selectedYear, month, i);
			ObservableList<CalendarEvent> eventList = eventManager
					.getEventsOn(currentDayDate);

			columnEntry.setOnMouseClicked(e -> {
				rootParentPane.selectDate(currentDayDate);
			});

			if (!eventList.isEmpty()) {
				Node eventPane = createCustomPane(columnEntry, eventList);
				columnEntry.getChildren().add(eventPane);
			}

		}

		int dayIndex = 1;
		for (int i = index; i < 42; i++) {

			VBox columnEntry = (VBox) calendarPage.getChildren().get(index++);
			Label dayEntry = createCalendarEntry(String.valueOf(dayIndex), INACTIVE);
			columnEntry.getChildren().add(dayEntry);

			LocalDate currentDayDate;
			if (month + 1 > 12) {
				currentDayDate = LocalDate.of(selectedYear + 1, 1, dayIndex);
			} else {
				currentDayDate = LocalDate.of(selectedYear, month + 1, dayIndex);
			}

			ObservableList<CalendarEvent> eventList = eventManager
					.getEventsOn(currentDayDate);

			columnEntry.setOnMouseClicked(e -> {
				rootParentPane.selectDate(currentDayDate);
			});

			if (!eventList.isEmpty()) {
				Node eventPane = createCustomPane(columnEntry, eventList);
				columnEntry.getChildren().add(eventPane);
			}

			dayIndex++;
		}
	}

	private Node createCustomPane(VBox eventRootPane,
			ObservableList<CalendarEvent> eventList) {

		double vGap = 5;
		double hGap = 5;

		double extraSpacing = eventRootPane.getPadding().getLeft()
				+ eventRootPane.getPadding().getRight() + vGap * 2;

		TilePane eventCountPane = new TilePane();
		eventCountPane.setVgap(vGap);
		eventCountPane.setHgap(hGap);

		int type1Sum = 0;
		int type2Sum = 0;
		int type3Sum = 0;
		int type4Sum = 0;
		int type5Sum = 0;

		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).isCompleted()) {
				type5Sum++;
			} else {
				if (eventList.get(i).getPriority() == CalendarEvent.OPTIONAL) {
					type1Sum++;
				} else if (eventList.get(i).getPriority() == CalendarEvent.STANDARD) {
					type2Sum++;
				} else if (eventList.get(i).getPriority() == CalendarEvent.IMPORTANT) {
					type3Sum++;
				} else {
					type4Sum++;
				}
			}

		}

		if (type1Sum > 0) {
			VBox box = createResizableBox(eventRootPane, extraSpacing, type1Sum,
					CalendarEvent.OPTIONAL);
			box.visibleProperty().bind(super.rootParentPane.getOptionalFilterProperty());
			box.managedProperty().bind(super.rootParentPane.getOptionalFilterProperty());

			eventCountPane.getChildren().add(box);
		}

		if (type2Sum > 0) {
			VBox box = createResizableBox(eventRootPane, extraSpacing, type2Sum,
					CalendarEvent.STANDARD);
			box.visibleProperty().bind(super.rootParentPane.getStandardFilterProperty());
			box.managedProperty().bind(super.rootParentPane.getStandardFilterProperty());
			eventCountPane.getChildren().add(box);
		}

		if (type3Sum > 0) {
			VBox box = createResizableBox(eventRootPane, extraSpacing, type3Sum,
					CalendarEvent.IMPORTANT);
			box.visibleProperty().bind(super.rootParentPane.getImportantFilterProperty());
			box.managedProperty().bind(super.rootParentPane.getImportantFilterProperty());
			eventCountPane.getChildren().add(box);
		}

		if (type4Sum > 0) {
			VBox box = createResizableBox(eventRootPane, extraSpacing, type4Sum,
					CalendarEvent.URGENT);
			box.visibleProperty().bind(super.rootParentPane.getCriticalFilterProperty());
			box.managedProperty().bind(super.rootParentPane.getCriticalFilterProperty());
			eventCountPane.getChildren().add(box);
		}

		if (type5Sum > 0) {
			VBox box = createResizableBox(eventRootPane, extraSpacing, type5Sum,
					CalendarEvent.COMPLETED);
			box.visibleProperty().bind(super.rootParentPane.getCompletedFilterProperty());
			box.managedProperty().bind(super.rootParentPane.getCompletedFilterProperty());
			eventCountPane.getChildren().add(box);
		}

		return eventCountPane;
	}

	private VBox createResizableBox(VBox eventRootPane, double padding, int eventCount,
			int priority) {
		VBox box = new VBox();

		box.prefWidthProperty().bind(new DoubleBinding() {
			{
				bind(eventRootPane.widthProperty());
			}

			@Override
			protected double computeValue() {
				double width = (eventRootPane.getWidth() - padding - 5);

				if (width < 160) {
					return width / 2.0;
				} else {
					return width / 3.0;
				}

			}

		});

		box.setAlignment(Pos.CENTER);
		box.setPrefHeight(25);

		Label eventCountLabel = new Label(String.valueOf(eventCount));
		eventCountLabel.setStyle("-fx-text-fill:white; -fx-font-weight:bold;");

		eventCountLabel.setAlignment(Pos.CENTER);
		eventCountLabel.setPrefWidth(22);
		box.getChildren().add(eventCountLabel);

		if (priority == CalendarEvent.OPTIONAL) {
			box.setStyle("-fx-background-color : #4C95CE");
		} else if (priority == CalendarEvent.STANDARD) {
			box.setStyle("-fx-background-color : #81C457");
		} else if (priority == CalendarEvent.IMPORTANT) {
			box.setStyle("-fx-background-color : #F7C531");
		} else if (priority == CalendarEvent.URGENT) {
			box.setStyle("-fx-background-color : #E85569");
		} else {
			box.setStyle("-fx-background-color : #344563");
		}

		return box;
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
		case MONDAY:
			return 0;
		case TUESDAY:
			return 1;
		case WEDNESDAY:
			return 2;
		case THURSDAY:
			return 3;
		case FRIDAY:
			return 4;
		case SATURDAY:
			return 5;
		case SUNDAY:
			return 6;
		}
		return 0;
	}

	public void setAddButtonEnable(boolean b) {
		addButtonBooleanProperty.set(b);
	}

	public void setEventManager(CalendarEventManager eventManager) {
		this.eventManager = eventManager;
	}

}