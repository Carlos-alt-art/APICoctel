module com.carlos.apijavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.carlos.apijavafx to javafx.fxml;
    exports com.carlos.apijavafx;
}