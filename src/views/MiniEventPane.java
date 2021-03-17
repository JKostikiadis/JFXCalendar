package views;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.CalendarEvent;

public class MiniEventPane extends VBox {

	private Label titleLabel = new Label();
	private VBox priorityBox = new VBox();

	public MiniEventPane(CalendarEvent event) {
		VBox.setMargin(this, new Insets(0, 5, 0, 5));
		setPadding(new Insets(5, 5, 5, 5));

		setMinHeight(70);
		setPrefHeight(70);
		setMaxHeight(70);

		priorityBox = new VBox();
		priorityBox.setAlignment(Pos.CENTER);
		priorityBox.setMinSize(10, 10);
		priorityBox.setPrefSize(10, 10);
		priorityBox.setMaxSize(10, 10);

		getStylesheets().add(
				this.getClass().getResource("/style/EventPaneStyle.css")
						.toExternalForm());

		int priority = event.getPriority();
		String title = event.getTitle();

		if (event.isCompleted()) {
			priorityBox.setId("CompletedPriorityBox");
			setId("completedEvent");
		} else {
			if (priority == CalendarEvent.OPTIONAL) {
				priorityBox.setId("optionalPriorityBox");
				setId("optionalEvent");
			} else if (priority == CalendarEvent.STANDARD) {
				priorityBox.setId("standardPriorityBox");
				setId("standardEvent");
			} else if (priority == CalendarEvent.IMPORTANT) {
				priorityBox.setId("importantPriorityBox");
				setId("importandEvent");
			} else {
				priorityBox.setId("urgentPriorityBox");
				setId("urgentEvent");
			}
		}

		titleLabel.setText(title);
		titleLabel.setWrapText(true);

		titleLabel.setPadding(new Insets(0, 0, 0, 10));
		titleLabel.setId("titleLabelMini");

		if (event.getType() == CalendarEvent.RECURRING_EVENT) {
			FontAwesomeIconView view = null;

			if (event.getPeriodicType() == CalendarEvent.PER_WEEK) {
				view = new FontAwesomeIconView(FontAwesomeIcon.TAG);
				view.setFill(Color.web("#B55231"));
			} else if (event.getPeriodicType() == CalendarEvent.PER_MONTH) {
				view = new FontAwesomeIconView(FontAwesomeIcon.FLAG);
				view.setFill(Color.web("#2C3AB8"));
			} else {
				view = new FontAwesomeIconView(FontAwesomeIcon.STAR);
				view.setFill(Color.web("#FFD907"));
			}

			HBox box = new HBox(view);
			box.setAlignment(Pos.BOTTOM_RIGHT);

			VBox.setVgrow(box, Priority.ALWAYS);

			getChildren().addAll(priorityBox, titleLabel, box);
		} else {
			getChildren().addAll(priorityBox, titleLabel);
		}

	}
}