import java.lang.*;
import java.io.*;
import java.util.*;
import javafx.scene.shape.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.Node.*;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.paint.Color;

public class testFX extends Application{
	

	public double width = 1000.0;
	public double height = 1000.0;
	public Group rootNode = new Group();


	public void start(Stage stage){

		stage.setTitle("JavaFX Window");

 
   		Scene myScene = new Scene(rootNode, width, height);
		stage.setScene(myScene);
		//Canvas myCanvas = new Canvas(width, height);


		Image image = new Image();
		image.readCam();

		Rectangle pixel;


		for(int i = 0 ; i < image.height ; i++){
			
			for(int j = 0 ; j < image.width ; j++){

				pixel = new Rectangle((double)i, (double)j, 1, 1);

				switch(image.getImage()[i][j].getColor())
				{

					case 0:
						pixel.setFill(Color.RED);
						break;

					case 1:
						pixel.setFill(Color.GREEN);
						break;

					case 2:
						pixel.setFill(Color.BLUE);
						break;

					case 3:
						pixel.setFill(Color.GRAY);
						break;

					case 4:
						pixel.setFill(Color.BLACK);
						break;

					case 5:
						pixel.setFill(Color.WHITE);
						break;

					default:
						System.out.println("Something is broken...");
						break;

				}
				rootNode.getChildren().add(pixel);

			}

		}

		//rootNode.getChildren().add()
		stage.show();

	}

}