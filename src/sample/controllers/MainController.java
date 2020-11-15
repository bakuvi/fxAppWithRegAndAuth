package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.DBconnection;
import sample.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {

    @FXML
    private Button btn_exit;

    @FXML
    private Button btn_add_article;

    @FXML
    private VBox paneVBox;

    private DBconnection db = new DBconnection();


    public void initialize() throws SQLException {
        ResultSet res = db.getArticles();

        while (res.next()) {
            Node node = null;
            try {
                node = FXMLLoader.load(getClass().getResource("/sample/scenes/article.fxml"));
                Label title = (Label) node.lookup("#title");
                title.setText(res.getString("title"));

                Label intro = (Label) node.lookup("#intro");
                intro.setText(res.getString("intro"));

                final Node nodeSet = node;

                node.setOnMouseEntered(e -> {
                    nodeSet.setStyle("-fx-background-color: #707173");
                });

                node.setOnMouseExited(e -> {
                    nodeSet.setStyle("-fx-background-color: #343434");
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
            HBox hBox = new HBox();
            hBox.getChildren().add(node);
            hBox.setAlignment(Pos.BASELINE_CENTER);
            paneVBox.getChildren().add(hBox);
            paneVBox.setSpacing(10);

        }


        btn_exit.setOnAction(e -> {
            Parent root = null;
            try {
                FileOutputStream fos = new FileOutputStream("user.settings");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(new User(""));
                fos.close();
                oos.close();

                root = FXMLLoader.load(getClass().getResource("/sample/scenes/sample.fxml"));
                Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                primaryStage.setTitle("Программа-проект на ФХ");
                primaryStage.setScene(new Scene(root, 600, 400));
                primaryStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        btn_add_article.setOnAction(e->{
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/sample/scenes/addArticle.fxml"));
                Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                primaryStage.setTitle("Программа-проект на ФХ");
                primaryStage.setScene(new Scene(root, 600, 400));
                primaryStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
    }
}
