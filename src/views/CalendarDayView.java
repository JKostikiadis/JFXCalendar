package views;

import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;
import model.CalendarEvent;
import model.CalendarEventManager;

public class CalendarDayView extends CalendarView {

	private ScrollPane scrollPane;
	private VBox eventPane;

	private CalendarEventManager eventManager;

	public LocalDate currectDate;

	public CalendarDayView(JFXCalendar parentPane, CalendarEventManager eventManager) {
		super(parentPane);

		this.eventManager = eventManager;
		addEventDialog.setCalendarEventManager(eventManager);

		scrollPane = new ScrollPane();
		eventPane = new VBox();
		eventPane.setSpacing(25);
		eventPane.setMaxHeight(Double.MAX_VALUE);
		eventPane.setPadding(new Insets(0, 0, 10, 0));
		scrollPane.setContent(eventPane);
		scrollPane.setFitToHeight(true);

		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

		eventPane.setStyle("-fx-background-color: WHITE");
		scrollPane.setStyle("-fx-background-color: WHITE");

		getChildren().add(0, scrollPane);

	}

	public void refreshCalendar(LocalDate selectedDate) {
		this.currectDate = selectedDate;
		clearEvents();

		String dayText[] = CalendarEvent.DAYS_FULL_NAMES;
		VBox columnEntry = new VBox();
		columnEntry.getStyleClass().add("calendar_cell");
		columnEntry.prefWidthProperty().bind(scrollPane.widthProperty());
		columnEntry.setPrefHeight(97);
		columnEntry.setPadding(new Insets(3, 0, 0, 10));

		Label dayName = new Label(dayText[selectedDate.getDayOfWeek().ordinal()]);
		dayName.setPadding(new Insets(5, 0, 0, 5));

		Label dayCount = new Label(String.valueOf(selectedDate.getDayOfMonth()));
		dayCount.getStyleClass().add("calendar_label_xl");

		columnEntry.getChildren().addAll(dayName, dayCount);
		eventPane.getChildren().add(columnEntry);

		addEvents(selectedDate);
	}

	private void addEvents(LocalDate selectedDate) {
		if (eventManager == null)
			return;

		ObservableList<CalendarEvent> eventList = eventManager.getEventsOn(selectedDate);

		for (CalendarEvent event : eventList) {
			int eventPriority = event.getPriority();


			EventPane eventBox = new EventPane(event);
			eventBox.setCalendarView(this);
			eventBox.setEventManager(eventManager);
			eventBox.setEventParentPane(eventPane);
			eventBox.setStackPaneRoot(rootParentPane);
			if (event.isCompleted()) {
				eventBox.visibleProperty()
						.bind(super.rootParentPane.getCompletedFilterProperty());
				eventBox.managedProperty()
						.bind(super.rootParentPane.getCompletedFilterProperty());
			} else {
				if (eventPriority == CalendarEvent.OPTIONAL) {
					eventBox.visibleProperty()
							.bind(super.rootParentPane.getOptionalFilterProperty());
					eventBox.managedProperty()
							.bind(super.rootParentPane.getOptionalFilterProperty());
				} else if (eventPriority == CalendarEvent.STANDARD) {
					eventBox.visibleProperty()
							.bind(super.rootParentPane.getStandardFilterProperty());
					eventBox.managedProperty()
							.bind(super.rootParentPane.getStandardFilterProperty());
				} else if (eventPriority == CalendarEvent.IMPORTANT) {
					eventBox.visibleProperty()
							.bind(super.rootParentPane.getImportantFilterProperty());
					eventBox.managedProperty()
							.bind(super.rootParentPane.getImportantFilterProperty());
				} else {
					eventBox.visibleProperty()
							.bind(super.rootParentPane.getCriticalFilterProperty());
					eventBox.managedProperty()
							.bind(super.rootParentPane.getCriticalFilterProperty());
				}
			}

			eventPane.getChildren().add(eventBox);
		}

	}

	private void clearEvents() {
		eventPane.getChildren().clear();
	}

	public void setEventManager(CalendarEventManager eventManager) {
		this.eventManager = eventManager;
		refreshCalendar(LocalDate.now());
	}

	public void setAddButtonEnable(boolean b) {
		addButtonBooleanProperty.set(b);
	}
}