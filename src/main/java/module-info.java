module com.java.revaniexchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.java.revaniexchange.model to javafx.base;

    opens com.java.revaniexchange to javafx.fxml;
    opens com.java.revaniexchange.controller to javafx.fxml;
    exports com.java.revaniexchange;
}
