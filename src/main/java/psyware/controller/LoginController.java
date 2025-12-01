package psyware.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import psyware.dao.UsuarioDAO;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;

    @FXML
    private void login() {
        String usuario = txtUsuario.getText().trim();
        String senha = txtSenha.getText();

        UsuarioDAO dao = new UsuarioDAO();
        if (dao.validarLogin(usuario, senha)) {
            try {
                Stage stage = (Stage) txtUsuario.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
                Scene scene = new Scene(loader.load(), 1200, 700);
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                stage.setScene(scene);
                stage.setTitle("PsyWare - Dashboard");
                stage.centerOnScreen();
                stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/icon.png")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Usuário ou senha incorretos!", ButtonType.OK).show();
        }
    }
}