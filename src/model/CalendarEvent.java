package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

import validation.DataValidator;

public class CalendarEvent {

	public static final String MONTHS[] = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October", "November",
			"December" };

	public static final String[] DAYS_FULL_NAMES = { "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday", "Sunday" };

	public static final String[] DAYS_SHORT_NAMES = { "Mon", "Tue", "Wed", "Thu", "Fri",
			"Sat", "Sun" };

	public static final String[] DAYS_NAMES_LETTERS = { "M", "T", "W", "T", "F", "S",
			"S", };

	public static final int COMPLETED = 0;
	public static final int OPTIONAL = 1;
	public static final int STANDARD = 2;
	public static final int IMPORTANT = 3;
	public static final int URGENT = 4;

	public static final int ONE_TIME_EVENT = 5;
	public static final int RECURRING_EVENT = 6;

	public static final int PER_WEEK = 7;
	public static final int PER_MONTH = 8;
	public static final int PER_YEAR = 9;

	public static final int START_OF_MONTH = 10;
	public static final int END_OF_MONTH = 11;

	private int id = -1;

	private String title = "";
	private int priority;
	private String description = "";
	private int type;

	private int periodicType = -1;

	// in SINGLE
	private LocalDate date;

	// in PERIODIC occurrences
	private String daysInWeek = "";
	private int placeInMonth = -1;
	private LocalDate yearlyDate;

	// if task is completed
	private int isCompleted = -1;

	/**
	 * Constructor
	 *
	 * @param title
	 * @param priority    constants {OPTIONAL , STANDARD , IMPORTANT , URGENT}
	 * @param description
	 */
	public CalendarEvent(String title, int priority, String description) {
		super();
		this.title = title;
		this.priority = priority;
		this.description = description;
		type = ONE_TIME_EVENT; // by default
	}

	/**
	 * Constructor
	 *
	 * @param title
	 * @param priority     constants = {OPTIONAL , STANDARD , IMPORTANT , URGENT}
	 * @param description
	 * @param type         constants = {ONE_TIME_EVENT , RECURRING_EVENT}
	 * @param date         NULL if the event is RECURRING_EVENT + every year
	 * @param daysInWeek   null or empty if the event is "ONE_OFF_EVENT" otherwise
	 *                     all the days from 1 to 7 separated with ',' like ex.
	 *                     1,2,5,7
	 * @param placeInMonth -1 if the event is "ONE_OFF_EVENT" otherwise constants =
	 *                     {START_OF_MONTH , END_OF_MONTH}
	 * @param yearlyDate   null or empty if the event is "ONE_OFF_EVENT"
	 */
	public CalendarEvent(int id, String title, int priority, String description, int type,
			int periodicType, LocalDate date, String daysInWeek, int placeInMonth,
			LocalDate yearlyDate) {
		this.id = id;
		this.title = title;
		this.priority = priority;
		this.description = description;
		this.type = type;
		this.periodicType = periodicType;
		this.date = date;
		this.daysInWeek = daysInWeek == null ? "" : daysInWeek;
		this.placeInMonth = placeInMonth;
		this.yearlyDate = yearlyDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPeriodicType() {
		return periodicType;
	}

	public void setPeriodicType(int periodicType) {
		this.periodicType = periodicType;
	}

	public String getDaysInWeek() {
		return daysInWeek;
	}

	public void setDaysInWeek(String daysInWeek) {
		this.daysInWeek = daysInWeek;
	}

	public int getPlaceInMonth() {
		return placeInMonth;
	}

	public void setPlaceInMonth(int placeInMonth) {
		this.placeInMonth = placeInMonth;
	}

	public LocalDate getYearlyDate() {
		return yearlyDate;
	}

	public void setYearlyDate(LocalDate yearlyDate) {
		this.yearlyDate = yearlyDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOnDate(LocalDate localDate) {
		if (type == ONE_TIME_EVENT) {
			return date.equals(localDate);
		} else {
			if (periodicType == CalendarEvent.PER_WEEK) {
				return isDateInDays(localDate.getDayOfWeek());
			} else if (periodicType == CalendarEvent.PER_MONTH) {
				if (placeInMonth == CalendarEvent.START_OF_MONTH) {
					return localDate.getDayOfMonth() == 1;
				} else {
					YearMonth month = YearMonth.from(localDate);
					return localDate.getDayOfMonth() == month.atEndOfMonth()
							.getDayOfMonth();
				}
			} else if (periodicType == CalendarEvent.PER_YEAR) {
				return localDate.getDayOfMonth() == getYearlyDateDay()
						&& localDate.getMonthValue() == getYearlyDateMonth();
			}
		}
		return false;
	}

	private int getYearlyDateDay() {
		return yearlyDate.getDayOfMonth();
	}

	private int getYearlyDateMonth() {
		return yearlyDate.getMonthValue();
	}

	private boolean isDateInDays(DayOfWeek currentDay) {
		String days[] = daysInWeek.split(",");
		for (String index : days) {
			if (currentDay.getValue() == DataValidator.getInt(index)) {
				return true;
			}
		}
		return false;
	}

	public void setCompleted(boolean selected) {
		isCompleted = selected ? 1 : -1;
	}

	public boolean isCompleted() {
		return isCompleted == 1 ? true : false;
	}
}