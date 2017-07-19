package group2;

import java.util.List;

import group1.IImage;
import group1.IPixel;
import group1.Image;
import group3.IMovingBlobDetection;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group4.IMovingBlobReduction;
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
    public static void main(String... args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        IImage image = new Image();
        image.readCam();

        IPixel[][] pixels = image.getImage();

        final int scale = 3;

        final int width = pixels[0].length;
        final int height = pixels.length;

        Canvas canvas = new Canvas(width * scale, height * scale);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                IPixel p = pixels[j][i];
                // Paint fill = Color.rgb(p.getRed(), p.getGreen(),
                // p.getBlue());
                Paint fill = getPaint(p);
                gc.setFill(fill);
                gc.fillRect(i * scale, j * scale, scale, scale);
            }
        }

        IBlobDetection blobDetect = new BlobDetection2();
        IMovingBlobDetection movingBlobDetect = new MovingBlobDetection();
        IMovingBlobReduction blobFilter = new BlobFilter();

        List<Blob> blobs = blobDetect.getBlobs(image);
        List<MovingBlob> movingBlobs = movingBlobDetect.getMovingBlobs(blobs);
        List<MovingBlob> filteredBlobs = blobFilter.reduce(movingBlobs);

        gc.setStroke(Color.AZURE);
        gc.setLineWidth(1);

        for (Blob blob : filteredBlobs)
        {
            final float up = blob.centerY - (blob.height / 2f);
            final float left = blob.centerX - (blob.width / 2f);

            gc.strokeRect(left * scale, up * scale, blob.width * scale, blob.height * scale);
        }

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
                return (Color.GREEN);
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
