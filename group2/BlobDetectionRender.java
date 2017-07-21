package group2;

import java.util.List;

import group1.IImage;
import group1.IPixel;
import group1.Image;
import group1.Pixel;
import group3.IMovingBlobDetection;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group4.IMovingBlobReduction;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class BlobDetectionRender extends Application
{
    final boolean drawBlobs = true;
    final boolean filter = true;
    final boolean posterize = true;
    
    public static void main(String... args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // IImage image = new JpgImage("src/testImage1.png");
        IImage image = new Image();
        
        IPixel[][] pixels = image.getImage();
        final int scale = 1;

        final int width = pixels[0].length;
        final int height = pixels.length;

        Canvas canvas = new Canvas(width * scale, height * scale);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        AnimationTimer timer = new AnimationTimer() {
        	@Override
        	public void handle(long time)
        	{
		        image.readCam();
		        IPixel[][] pixels = image.getImage();
		
		        final int width = pixels[0].length;
		        final int height = pixels.length;
		
		        final float blockedOutArea = (0);
		        for (int i = 0; i < width; i++)
		        {
		            for (int j = 0; j < height; j++)
		            {
		                if (j < (height * blockedOutArea))
		                {
		                    gc.setFill(Color.RED);
		                    pixels[j][i] = new Pixel((short) 255, (short) 0, (short) 0);
		                }
		                else
		                {
		                    //@formatter:off
		                    IPixel p = pixels[j][i];
		                    Paint fill = Color.rgb(p.getRed(), p.getGreen(), p.getBlue());
		                    
		                    if(posterize)
		                    {
		                    	fill = getPaint(p);	
		                    }
		                    
		                    gc.setFill(fill);
		                    
		                    //@formatter:on
		                }
		
		                gc.fillRect(i * scale, j * scale, scale, scale);
		            }
		        }
		
		        IBlobDetection blobDetect = new BlobDetection3();
		        IMovingBlobDetection movingBlobDetect = new MovingBlobDetection();
		        IMovingBlobReduction blobFilter = new BlobFilter();
		
		        List<Blob> blobs = blobDetect.getBlobs(image);
		        List<MovingBlob> movingBlobs =
		       movingBlobDetect.getMovingBlobs(blobs);
		         
		         List<MovingBlob> filteredBlobs =   movingBlobDetect.getUnifiedBlobs(blobFilter.reduce(movingBlobs));
		
		        gc.setStroke(Color.DARKGOLDENROD);
		        gc.setLineWidth(4);
		        
		        if(drawBlobs)
		        {
		        	if(filter)
		        	{
				        for (Blob blob : filteredBlobs)
				        {
				            gc.strokeRect(blob.x * scale, blob.y * scale, blob.width * scale, blob.height * scale);
				        }
		        	}
		        	else
		        	{
				        for (Blob blob : blobs)
				        {
				            gc.strokeRect(blob.x * scale, blob.y * scale, blob.width * scale, blob.height * scale);
				        }
		        	}
		        }
	        }
        };
        
        
        timer.start();
        
        primaryStage.setTitle("JavaFX Window");

        Group rootNode = new Group();
        rootNode.getChildren().addAll(canvas);

        Scene myScene = new Scene(rootNode, width * scale, height * scale);
        primaryStage.setScene(myScene);

        primaryStage.show();
    }

    private static Paint getPaint(IPixel p)
    {
        switch (p.getColor())
        {
            case 0:
                return (Color.RED);
            case 1:
                return (Color.LIME);
            case 2:
                return (Color.BLUE);
            case 3:
                return (Color.GRAY);
            case 4:
                return (Color.BLACK);
            case 5:
                return (Color.WHITE);
            default:
                throw new IllegalStateException("Invalid color code " + p.getColor() + ".");
        }
    }
}
