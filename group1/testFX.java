package group1;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class testFX extends Application{

	public double width = 1000.0;
	public double height = 1000.0;
	public Group rootNode = new Group();
	public Image image = new Image();
	
	
    public static void main(String args[]){

        launch(args);

    }

    
    public void start(Stage stage){

    		stage.setTitle("JavaFX Window");

        Scene myScene = new Scene(rootNode, width, height);
        stage.setScene(myScene);


		image.readCam();	
		
		for(int i = 0; i < 20; i++) {	
						
			image.readCam();
		
		}
		
		print();
		
				
		//Event e = new MouseEvent
		
		Label r = new Label();
		Label g = new Label();
		Label b = new Label();
		Label h = new Label();
		Label location = new Label();
		
		Button nextFrameButton = new Button("Next Frame");
		nextFrameButton.relocate(10, 450);
		
		r.relocate(10, 300);
		g.relocate(10, 350);
		b.relocate(10, 400);
		h.relocate(10, 500);
		location.relocate(10, 600);
		
		
		
		stage.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>(){
			
			@Override
			public void handle(MouseEvent event) {
				
				final double x, y;
				
				x = event.getSceneX();
				y = event.getSceneY();
				
				if(x < image.width && y < image.height){
				
					r.setText("Red: "  +Short.toString(image.getImage()[(int)x][(int)y].getRed()));
					g.setText("Green: "  +Short.toString(image.getImage()[(int)x][(int)y].getGreen()));
					b.setText("Blue: "  + Short.toString(image.getImage()[(int)x][(int)y].getBlue()));
					h.setText("Color: "  + Short.toString(image.getImage()[(int)x][(int)y].getColor()));
					location.setText("Location: " + x + " , " + y);
				}
				
			}
		});
		
		 nextFrameButton.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {

	            		image.readCam();
	            		print();

	            }
	        });


		rootNode.getChildren().addAll(r, g, b, nextFrameButton, h, location);
		stage.show();

	}

	private void print() {
			
			Rectangle pixel;
		
			for(int i = 0 ; i < image.height ; i++){
				
				for(int j = 0 ; j < image.width ; j++){

					pixel = new Rectangle((double)j, (double)i, 1, 1);

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
			
			
			
			//real image (rgb)
			
			for(int i = 0 ; i < image.height ; i++){
				
				for(int j = 320 ; j < image.width + 320 ; j++){

					pixel = new Rectangle((double)j, (double)i, 1, 1);
					pixel.setFill(Color.color((double)image.getImage()[i][j - 320].getRed()/255.0, (double)image.getImage()[i][j - 320].getGreen()/255.0, (double)image.getImage()[i][j - 320].getBlue()/255.0));
					rootNode.getChildren().add(pixel);

				}

			}
			
		
	}

}
