package views;

import java.io.IOException;

import org.controlsfx.glyphfont.FontAwesome;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import controllers.EventDetailPaneController;
import dialog.DialogHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.CalendarEvent;
import model.CalendarEventManager;

public class EventPane extends VBox {

	private Label fromLabel = new Label();
	private Label titleLabel = new Label();
	private Label messageLabel = new Label();
	private VBox priorityBox = new VBox();

	private CalendarEventManager eventManager;

	private VBox parentPane;
	private CalendarEvent event;

	private StackPane eventStackPane;

	private CalendarDayView calendarDayView;
	private Label priorityLabel;

	public EventPane(CalendarEvent event) {
		this.event = event;

		fromLabel.setVisible(false);

		String title = event.getTitle();
		String text = event.getDescription();

		VBox.setMargin(this, new Insets(0, 40, 0, 20));
		setPadding(new Insets(15, 10, 15, 10));

		setSpacing(10);
		setPrefHeight(100);

		JFXCheckBox completedCheckButton = new JFXCheckBox();
		completedCheckButton.setSelected(event.isCompleted());
		completedCheckButton.setStyle(
				"-jfx-checked-color : #344563; -jfx-unchecked-color : #344563 ;");
		HBox.setMargin(completedCheckButton, new Insets(3, 0, 0, 0));

		completedCheckButton.setOnAction(e -> {
			event.setCompleted(completedCheckButton.isSelected());
			if (calendarDayView != null) {
				calendarDayView.refreshCalendar(calendarDayView.currectDate);
			} else {
				refreshStyle();
				JFXCalendar.todayButton.fire();
			}
		});

		priorityBox = new VBox();
		priorityBox.setAlignment(Pos.CENTER);
		priorityBox.setMinSize(125, 25);
		priorityBox.setPrefSize(125, 25);
		priorityBox.setMaxSize(125, 25);

		HBox topPane = new HBox(priorityBox, completedCheckButton);
		topPane.setSpacing(7);

		getStylesheets().add(this.getClass().getResource("/style/EventPaneStyle.css")
				.toExternalForm());

		priorityLabel = new Label("");
		priorityLabel.setId("priorityLabel");

		priorityBox.getChildren().add(priorityLabel);

		if (event.isCompleted()) {
			priorityLabel.setText("Completed");
			priorityBox.setId("CompletedPriorityBox");
			setId("completedEvent");
		} else {
			refreshStyle();
		}

		titleLabel.setText("Title : " + title);
		messageLabel.setText("Description : " + text);

		messageLabel.maxWidthProperty().bind(this.widthProperty());

		titleLabel.setPadding(new Insets(0, 0, 0, 10));
		messageLabel.setPadding(new Insets(0, 0, 0, 10));
		fromLabel.setPadding(new Insets(0, 0, 0, 10));

		titleLabel.setId("titleLabel");
		messageLabel.setId("messageLabel");
		fromLabel.setId("fromLabel");

		JFXButton removeButton = new JFXButton();
		JFXButton editButton = new JFXButton();

		removeButton
				.setGraphic(new FontAwesome().create(FontAwesome.Glyph.REMOVE).size(18));
		editButton
				.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PENCIL).size(18));

		removeButton.setOnAction(e -> {
			removeEvent();
		});

		editButton.setOnAction(e -> {
			editEvent();
		});

		removeButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		editButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		HBox buttonPane = new HBox(editButton, removeButton);
		buttonPane.setSpacing(10);
		buttonPane.setAlignment(Pos.CENTER);

		HBox emptyPane = new HBox();
		HBox.setHgrow(emptyPane, Priority.ALWAYS);

		HBox topBox = new HBox();
		topBox.setSpacing(10);
		topBox.getChildren().addAll(topPane, emptyPane, buttonPane);

		getChildren().addAll(topBox, titleLabel, messageLabel);

	}

	private void refreshStyle() {
		titleLabel.setText("Title : " + event.getTitle());
		messageLabel.setText("Description : " + event.getDescription());

		if (event.isCompleted()) {
			priorityLabel.setText("Completed");
			priorityBox.setId("CompletedPriorityBox");
			setId("completedEvent");
		} else {
			int priority = event.getPriority();
			if (priority == CalendarEvent.OPTIONAL) {
				priorityLabel.setText("Optional");
				priorityBox.setId("optionalPriorityBox");
				setId("optionalEvent");
			} else if (priority == CalendarEvent.STANDARD) {
				priorityLabel.setText("Normal");
				priorityBox.setId("standardPriorityBox");
				setId("standardEvent");
			} else if (priority == CalendarEvent.IMPORTANT) {
				priorityLabel.setText("Important");
				priorityBox.setId("importantPriorityBox");
				setId("importandEvent");
			} else {
				priorityLabel.setText("Urgent");
				priorityBox.setId("urgentPriorityBox");
				setId("urgentEvent");
			}
		}
	}

	private void editEvent() {
		JFXDialog dialog = new JFXDialog();
		JFXDialogLayout content = new JFXDialogLayout();

		FXMLLoader eventDetailsPaneLoader = new FXMLLoader(this.getClass()
				.getResource("/views/fxml/EventDetailPane.fxml"));

		try {
			HBox mainPane = eventDetailsPaneLoader.load();
			content.setBody(mainPane);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		EventDetailPaneController controller = eventDetailsPaneLoader.getController();
		controller.loadEvent(event);
		controller.setDialog(dialog);

		dialog.getStylesheets().add(this.getClass()
				.getResource("/style/DialogStyle.css").toExternalForm());

		dialog.setDialogContainer(this.eventStackPane);
		dialog.setContent(content);

		dialog.show();

		dialog.setOnDialogClosed(e -> {
			if (controller.hasUpdates()) {
				if (calendarDayView != null) {
					calendarDayView.refreshCalendar(calendarDayView.currectDate);
				} else {
					if (event.isCompleted()) {
						priorityLabel.setText("Ολοκληρωμένο");
						priorityBox.setId("CompletedPriorityBox");
						setId("completedEvent");
					} else {
						refreshStyle();
					}
					JFXCalendar.todayButton.fire();
				}
			}
		});
	}

	private void removeEvent() {
		if (DialogHandler.showConfirmationDialog("Delete selected event")) {
			eventManager.removeEvent(event);
			parentPane.getChildren().remove(this);
		}
	}

	public void setEventParentPane(VBox parentPane) {
		this.parentPane = parentPane;
	}

	public void setEventManager(CalendarEventManager manager) {
		this.eventManager = manager;
	}

	public void setStackPaneRoot(StackPane eventStackPane) {
		this.eventStackPane = eventStackPane;
	}

	void setCalendarView(CalendarDayView calendarDayView) {
		this.calendarDayView = calendarDayView;
	}
}