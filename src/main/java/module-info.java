module org.example.vtaschannelclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires httpclient;
    requires httpcore;
    requires cn.hutool;


    opens org.example.vtaschannelclient to javafx.fxml;
    exports org.example.vtaschannelclient;
    exports org.example.vtaschannelclient.Controller;
    exports org.example.vtaschannelclient.Entity;
    opens org.example.vtaschannelclient.Controller to javafx.fxml;
}