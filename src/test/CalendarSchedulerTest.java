package test;

import gr.jfxcalendar.views.CalendarSchedulerView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalendarSchedulerTest extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		CalendarSchedulerView scheduler = new CalendarSchedulerView();

		stage.setScene(new Scene(scheduler));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
