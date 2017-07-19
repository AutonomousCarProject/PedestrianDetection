package group1;

import java.io.*;
import java.util.*;
import javafx.scene.shape.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.Node.*;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Color.*;

public class testFX extends Application{
	

	public double width = 1000.0;
	public double height = 1000.0;
	public Group rootNode = new Group();

	public static void main(String args[]){
		
		launch(args);

	}

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

				pixel = new Rectangle((double)j, (double)i, 1, 1);

				switch(image.getImage()[j][i].getColor())
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

		
		/*
		for(int i = 0 ; i < image.height ; i++){
			
			for(int j = 0 ; j < image.width ; j++){

				pixel = new Rectangle((double)j*2, (double)i*2, 1, 1);
				pixel.setFill(Color.color((double)image.getImage()[j][i].getRed()/255.0, (double)image.getImage()[j][i].getGreen()/255.0, (double)image.getImage()[j][i].getBlue()/255.0));
				rootNode.getChildren().add(pixel);

			}

		}*/


		//rootNode.getChildren().add()
		stage.show();

	}

}