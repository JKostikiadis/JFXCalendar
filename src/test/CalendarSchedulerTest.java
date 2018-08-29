package test;

import gr.jfxcalendar.views.JFXCalendar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalendarSchedulerTest extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		stage.setTitle("JFXCalendar Testing");
		JFXCalendar scheduler = new JFXCalendar();

		stage.setScene(new Scene(scheduler));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
