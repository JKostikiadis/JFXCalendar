package views;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;

import controllers.EventDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

public abstract class CalendarView extends StackPane {

	protected JFXCalendar rootParentPane;
	protected EventDialog addEventDialog;
	protected BooleanProperty addButtonBooleanProperty = new SimpleBooleanProperty();

	public CalendarView(JFXCalendar parentPane) {
		rootParentPane = parentPane;

		FontAwesomeIconView addEventIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
		addEventIcon.setFill(Paint.valueOf("WHITE"));
		addEventIcon.setSize("28px");

		JFXButton addEventButton = new JFXButton();
		addEventButton.setPrefSize(55, 55);
		addEventButton.setGraphic(addEventIcon);
		addEventButton.setTooltip(new Tooltip("Add Event"));
		addEventButton.setButtonType(ButtonType.RAISED);
		addEventButton.setId("eventButton");

		addEventButton.visibleProperty().bind(addButtonBooleanProperty.not());

		addEventDialog = new EventDialog(parentPane);

		addEventButton.setOnAction(e -> {
			addEventDialog.clear();
			addEventDialog.show();
			addEventDialog.setOnDialogClosed(ex -> {
				parentPane.refreshCalendar();
			});
		});

		StackPane.setAlignment(addEventButton, Pos.BOTTOM_RIGHT);
		StackPane.setMargin(addEventButton, new Insets(0, 40, 30, 0));
		getChildren().add(addEventButton);
	}
}