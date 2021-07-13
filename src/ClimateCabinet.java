
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class ClimateCabinet extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    sceneController x = new sceneController();

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Scenes/Login.fxml"));

        stage.initStyle(StageStyle.DECORATED);
        stage.setMaximized(false);

        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        stage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setMinWidth(942);
        stage.setMinHeight(641);
        stage.setMaxWidth(942);
        stage.setMaxHeight(641);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
