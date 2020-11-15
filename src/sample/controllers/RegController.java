package sample.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DBconnection;
import sample.User;

import java.awt.event.ActionEvent;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class RegController {
    DBconnection db = new DBconnection();

    @FXML
    private TextField login_reg;

    @FXML
    private TextField email_reg;

    @FXML
    private PasswordField pass_reg;

    @FXML
    private CheckBox confidentials;

    @FXML
    private Button btn_reg;

    @FXML
    private TextField log_auth;

    @FXML
    private PasswordField pass_auth;

    @FXML
    private Button btn_auth;

    @FXML
    private void initialize() {
        btn_reg.setOnAction(Event -> {
            login_reg.setStyle("-fx-border-color: #fafafa");
            email_reg.setStyle("-fx-border-color: #fafafa");
            pass_reg.setStyle("-fx-border-color: #fafafa");
            btn_reg.setText("Зарегистрироваться");

            if (login_reg.getCharacters().length() < 3) {
                login_reg.setStyle("-fx-border-color: red");
                return;
            } else if (email_reg.getCharacters().length() < 3) {
                email_reg.setStyle("-fx-border-color: red");
                return;
            } else if (pass_reg.getCharacters().length() < 3) {
                pass_reg.setStyle("-fx-border-color: red");
                return;
            } else if (!confidentials.isSelected()) {
                btn_reg.setText("Поставьте галочку");
                return;
            }


            String pass = md5String(pass_reg.getCharacters().toString());
            boolean isReg = db.regUser(login_reg.getCharacters().toString(), email_reg.getCharacters().toString(), pass);
            if (isReg) {
                login_reg.setText("");
                email_reg.setText("");
                pass_reg.setText("");
                btn_reg.setText("Готово");
            } else {
                btn_reg.setText("Введите другой логин");
            }
        });
        btn_auth.setOnAction(e -> {
            log_auth.setStyle("-fx-border-color: #fafafa");
            pass_auth.setStyle("-fx-border-color: #fafafa");

            if (log_auth.getCharacters().length() < 3) {
                log_auth.setStyle("-fx-border-color: red");
                return;

            } else if (pass_auth.getCharacters().length() < 3) {
                pass_auth.setStyle("-fx-border-color: red");
                return;
            }


            String pass = md5String(pass_auth.getCharacters().toString());


            try {
              boolean  isAuth = db.authUser(log_auth.getCharacters().toString(), pass);
                if (isAuth) {
                    FileOutputStream fos= new FileOutputStream("user.settings");
                    ObjectOutputStream oos= new ObjectOutputStream(fos);
                    oos.writeObject(new User(log_auth.getCharacters().toString()));

                    fos.close();
                    oos.close();

                    log_auth.setText("");
                    pass_auth.setText("");
                    btn_auth.setText("Готово");

                    Parent root = FXMLLoader.load(getClass().getResource("/sample/scenes/main.fxml"));
                    Stage primaryStage= (Stage) ((Node) e.getSource()).getScene().getWindow();
                    primaryStage.setTitle("Программа-проект на ФХ");
                    primaryStage.setScene(new Scene(root, 600, 400));
                    primaryStage.show();
                } else {
                    btn_auth.setText("Не найден");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


        });
    }

    public static String md5String(String pass) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(pass.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger bigInteger = new BigInteger(1, digest);
        String m5dHex = bigInteger.toString(16);

        while (m5dHex.length() < 32)
            m5dHex = "0" + m5dHex;

        return m5dHex;
    }

}
