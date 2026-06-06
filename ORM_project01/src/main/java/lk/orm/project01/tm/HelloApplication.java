package lk.orm.project01.tm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.orm.project01.util.DatabaseSeeder;

import java.io.IOException;


public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Seed initial data (skips silently if already seeded)
        DatabaseSeeder.seed();

        FXMLLoader fxmlLoader = new FXMLLoader(
            HelloApplication.class.getResource("/lk/orm/project01/login.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        stage.setTitle("Serenity Mental Health Therapy Center");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
