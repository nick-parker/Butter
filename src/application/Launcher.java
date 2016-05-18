package application;


import java.io.IOException;








import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;


public class Launcher extends Application {

	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Launcher.class.getResource("gui.fxml"));
        SplitPane primarySp = null;
        try{
        	primarySp = (SplitPane) loader.load();
        } catch(IOException exc){
        	
        }
        
        Scene scene = new Scene(primarySp);
        primaryStage.setScene(scene);
        
        new Butter();
		primaryStage.setTitle("Butter");
        primaryStage.show();
	}	

	public static void main(String[] args) {
		Application.launch(args);
	}
}