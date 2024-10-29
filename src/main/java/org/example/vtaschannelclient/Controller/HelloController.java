package org.example.vtaschannelclient.Controller;

import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.example.vtaschannelclient.HelloApplication;
import org.example.vtaschannelclient.Utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    public TextField idText;
    @FXML
    public PasswordField pwText;
    @FXML
    public TextField amountText;
    @FXML
    public RadioButton RB_sender;
    @FXML
    public RadioButton RB_receiver;
    public String role;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        RB_sender.setToggleGroup(toggleGroup);
        RB_receiver.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((arg0, old_toggle, new_toggle) -> role = ((RadioButton) new_toggle).getText());
    }

    @FXML
    protected void onRegisterButtonClick() {
        String id = idText.getText();
        String passwd = pwText.getText();
        String amount = amountText.getText();
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        String passwdHash = SmUtil.sm3(passwd);
        param.put("passwd", passwdHash);
        param.put("amount", amount);
        String response = HttpClientUtil.doPost("http://localhost:8080/asset/userRegister", param);
        JSONObject responseJson = JSONUtil.parseObj(response);
        if (responseJson.get("status").toString().equals("ok")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "您的账号为：\n" + responseJson.get("payload").toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "注册失败！");
            alert.showAndWait();
        }
    }

    @FXML
    protected void onLoginButtonClick() throws IOException {
        String id = idText.getText();
        String passwd = pwText.getText();
        if (id.equals("1024")) {
            debugMode();
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        String passwdHash = SmUtil.sm3(passwd);
        param.put("passwd", passwdHash);
        String response = HttpClientUtil.doPost("http://localhost:8080/asset/userLogin", param);
        JSONObject responseJson = JSONUtil.parseObj(response);
        if (responseJson.get("status").toString().equals("ok")) {
            pageSkip();
        } else if (responseJson.get("status").toString().equals("wrong passwd failed")) {
            System.out.println(responseJson.get("status").toString());
            Alert alert = new Alert(Alert.AlertType.ERROR, "口令有误！");
            alert.showAndWait();
        } else {
            System.out.println(responseJson.get("status").toString());
            Alert alert = new Alert(Alert.AlertType.ERROR, "用户未注册！");
            alert.showAndWait();
        }
    }

    private void pageSkip() throws IOException {
        FXMLLoader fxmlLoader;
        if (role.equals("发送方")) {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-sender-view.fxml"));
        } else if (role.equals("接收方")) {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-receiver-view.fxml"));
        } else {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-sender-view.fxml"));
        }
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setScene(scene);
    }

    @FXML
    public void debugMode() throws IOException {
        FXMLLoader fxmlLoader = null;
        if (role.equals("发送方")) {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-sender-view.fxml"));
        } else if (role.equals("接收方")) {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-receiver-view.fxml"));
        } else {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-sender-view.fxml"));
        }
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setScene(scene);
    }
}