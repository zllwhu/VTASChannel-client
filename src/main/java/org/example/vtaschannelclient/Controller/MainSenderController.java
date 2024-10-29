package org.example.vtaschannelclient.Controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.vtaschannelclient.Entity.Account;
import org.example.vtaschannelclient.HelloApplication;
import org.example.vtaschannelclient.Utils.HttpClientUtil;
import org.example.vtaschannelclient.Utils.PageUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.vtaschannelclient.HelloApplication.stage;

public class MainSenderController implements Initializable {
    public TableView<Account> assetTable;
    public TableColumn<Account, String> columnAddress;
    public TableColumn<Account, String> columnAmount;
    public Pagination assetPagination;
    public List<Account> tableData;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        tableData = getTableData();
        tableConfig(tableData);
    }

    private List<Account> getTableData() {
        String response = HttpClientUtil.doGet("http://localhost:8080/asset/all");
        System.out.println(response);
        String responseModified = response.substring(1, response.length() - 1);
        String[] responseArray = responseModified.split(",\\{");
        int num = responseArray.length;
        for (int i = 1; i < num; i++) {
            responseArray[i] = "{".concat(responseArray[i]);
        }
        List<Account> tableData = new ArrayList<>();
        for (String s : responseArray) {
            Account account = new Account();
            JSONObject jsonObject = JSONUtil.parseObj(s);
            account.setId(jsonObject.get("id").toString());
            account.setAmount(jsonObject.get("amount").toString());
            tableData.add(account);
        }
        return tableData;
    }

    private void tableConfig(List<Account> tableData) {
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        assetTable.setItems(FXCollections.observableList(tableData));
        PageUtil<Account> pageUtil = new PageUtil<>(tableData, 6);
        assetPagination.pageCountProperty().bindBidirectional(pageUtil.getTotalPage());
        assetPagination.setPageFactory(pageIndex -> {
            assetTable.setItems(FXCollections.observableList(pageUtil.getCurrentPageDataList(pageIndex)));
            return assetTable;
        });
    }

    @FXML
    protected void menuQuit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    @FXML
    protected void menuPreferences() throws IOException {
    }

    @FXML
    protected void menuAbout() throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("基于可验证定时适配器签名的高效可扩展支付系统 v1.0");
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
    }
}
