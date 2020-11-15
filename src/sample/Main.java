package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String scene = "sample.fxml";
        File file = new File("user.settings");
        boolean exist = file.exists();
        if (exist) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            User user = (User) ois.readObject();
            if (!user.getLogin().equals("")) {
                scene = "main.fxml";
            }
            fis.close();
            ois.close();
        }

        Parent root = FXMLLoader.load(getClass().getResource("scenes/"+scene));
        primaryStage.setTitle("Программа-проект на ФХ");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
