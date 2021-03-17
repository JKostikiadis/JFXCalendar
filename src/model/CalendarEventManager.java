package model;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CalendarEventManager {

	private final ObservableList<CalendarEvent> eventList;

	public CalendarEventManager() {
		eventList = FXCollections.observableArrayList();
	}

	public void addEvent(CalendarEvent event) {
		eventList.add(event);
	}

	public void removeEvent(CalendarEvent event) {
		for (CalendarEvent e : eventList) {
			if (e.getId() == event.getId()) {
				eventList.remove(e);
				break;
			}
		}

	}

	public ObservableList<CalendarEvent> getEventsOn(LocalDate localDate) {

		ObservableList<CalendarEvent> currentEventList = FXCollections
				.observableArrayList();

		for (CalendarEvent event : eventList) {
			if (event.isOnDate(localDate)) {
				currentEventList.add(event);
			}
		}

		return currentEventList;
	}
}