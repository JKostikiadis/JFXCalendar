package gr.jfxcalendar.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import gr.jfxcalendar.controlls.EventDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

public abstract class CalendarView extends StackPane {

	public CalendarView(StackPane parentPane) {

		FontAwesomeIconView addEventIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
		addEventIcon.setFill(Paint.valueOf("WHITE"));
		addEventIcon.setSize("28px");

		JFXButton addEventButton = new JFXButton();
		addEventButton.setPrefSize(55, 55);
		addEventButton.setGraphic(addEventIcon);
		addEventButton.setTooltip(new Tooltip("Add Event"));
		addEventButton.setButtonType(ButtonType.RAISED);
		addEventButton.setId("eventButton");

		EventDialog dialog = new EventDialog(parentPane);

		addEventButton.setOnAction(e -> {
			// TODO : clear dialog like
			dialog.clear();
			dialog.show();
		});

		StackPane.setAlignment(addEventButton, Pos.BOTTOM_RIGHT);
		StackPane.setMargin(addEventButton, new Insets(0, 40, 30, 0));
		getChildren().add(addEventButton);
	}
}
