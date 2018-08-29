package gr.jfxcalendar.controlls;

import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class CalendarTextField extends JFXTextField {
	private static final int PICKER_PADDING = 0;
	private static final int ARROW_X = 100;

	private Popup popup = new Popup();

	public CalendarTextField() {

		popup.setAutoHide(true);

		NavigationCalendar navCalendar = new NavigationCalendar();
		VBox box = new VBox();
		box.getChildren().add(navCalendar);
		box.setStyle(
				"-fx-background-color : white; -fx-border-color : grey; -fx-border-width : 0.3; -fx-effect: dropshadow(three-pass-box, derive(grey, -20%), 10, 0, 4, 4); ");
		popup.getContent().add(box);

		setOnMouseClicked(e -> {
			show(this);
		});

		StringProperty dateProperty = navCalendar.getSelectedDateProperty();
		dateProperty.addListener(e -> {
			setText(dateProperty.get());
			popup.hide();
		});

	}

	public void show(Control ownerControl) {
		Point2D point = ownerControl.localToScene(ownerControl.getWidth() / 2, ownerControl.getHeight());
		double x = point.getX() + ownerControl.getScene().getX() + ownerControl.getScene().getWindow().getX();
		double y = point.getY() + ownerControl.getScene().getY() + ownerControl.getScene().getWindow().getY();

		popup.show(ownerControl, x - getPopoverPointX(), y);
	}

	private static double getPopoverPointX() {
		return PICKER_PADDING + ARROW_X;
	}
}
