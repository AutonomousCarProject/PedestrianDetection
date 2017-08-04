package group3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.treez.javafxd3.d3.D3;
import org.treez.javafxd3.d3.core.Selection;
import org.treez.javafxd3.d3.scales.LinearScale;

import org.treez.javafxd3.javafx.JavaFxD3Browser;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;

import group1.FileImage;
import group1.IImage;
import group1.IPixel;
import group1.Image;
import group1.Pixel;
import group2.Blob;
import group2.BlobDetection;
import group2.TestBlobDetection;
import group2.TestPixel;
import group2.IBlobDetection;
import group3.IMovingBlobDetection;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group3.MovingBlobDetectionTest;
import group4.BlobFilter;
import group4.IMovingBlobReduction;

/**
 * Demonstrates how d3.js can be used with a JavaFx WebView
 *
 */
public class JavaFxD3SingleDemo extends Application {

	//#region ATTRIBUTES

	/**
	 * The JavaFx scene
	 */
	private Scene scene;

	private JavaFxD3Browser browser;
	
	private List<MovingBlob> movingBlobs;
			
	//#end region

	//#region METHODS

	/**
	 * Main
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {

		//set state title
		stage.setTitle("Single javafx-d3 demo");

		//define d3 content as post loading hook
		Runnable postLoadingHook = () -> {
			System.out.println("Initial loading of browser is finished");

			//do some d3 stuff
			createD3Example();
			
		};
		
		//create browser
		browser = new JavaFxD3Browser(postLoadingHook, true);

		//create the scene
		scene = new Scene(browser, 750, 500, Color.web("#666970"));
		stage.setScene(scene);
		stage.show();
		
		/*IImage image = new FileImage();
		image.setAutoFreq(15);
		
		image.readCam();
		
		ArrayList<Integer> blobX = new ArrayList<Integer>();
		ArrayList<Integer> blobY = new ArrayList<Integer>();
		ArrayList<Integer> blobWidth = new ArrayList<Integer>();
		ArrayList<Integer> blobHeight = new ArrayList<Integer>();
				
		ArrayList<ArrayList<Integer>> blobValues = new ArrayList<ArrayList<Integer>>();
		
        IBlobDetection blobDetect = new BlobDetection();
        IMovingBlobDetection movingBlobDetect = new MovingBlobDetection();
        IMovingBlobReduction blobFilter = new BlobFilter();

        List<Blob> blobs = blobDetect.getBlobs(image);
        List<MovingBlob> movingBlobs = movingBlobDetect.getMovingBlobs(blobs);
        
		for (Blob blob : blobs)
        {
			
			blobX.add(blob.x);
			blobY.add(blob.y);
			blobWidth.add(blob.width);
			blobHeight.add(blob.height);
			
        }
		
		blobValues.add(blobX);
		blobValues.add(blobY);
		blobValues.add(blobWidth);
		blobValues.add(blobHeight);
		
		System.out.println(blobValues.toArray()[0]);*/
		
		
	}
	
	private Object[] blobData() {
		
		IImage image = new FileImage();
		image.setAutoFreq(15);
		
		image.readCam();
		
		ArrayList<Integer> blobX = new ArrayList<Integer>();
		ArrayList<Integer> blobY = new ArrayList<Integer>();
		ArrayList<Integer> blobWidth = new ArrayList<Integer>();
		ArrayList<Integer> blobHeight = new ArrayList<Integer>();
				
		ArrayList<ArrayList<Integer>> blobValues = new ArrayList<ArrayList<Integer>>();
		
        IBlobDetection blobDetect = new BlobDetection();
        IMovingBlobDetection movingBlobDetect = new MovingBlobDetection();
        IMovingBlobReduction blobFilter = new BlobFilter();

        List<Blob> blobs = blobDetect.getBlobs(image);
        List<MovingBlob> movingBlobs = movingBlobDetect.getMovingBlobs(blobs);
        
		for (Blob blob : blobs)
        {
			
			blobX.add(blob.x);
			blobY.add(blob.y);
			blobWidth.add(blob.width);
			blobHeight.add(blob.height);
			
        }
		
		blobValues.add(blobX);
		blobValues.add(blobY);
		blobValues.add(blobWidth);
		blobValues.add(blobHeight);
		
		//Ob[] test = {blobValues.toArray()};
 		
		Object[] blobArray = blobValues.toArray();
		
		//System.out.println(blobArray);
		
		//System.out.println(Arrays.toString(blobValues.toArray()));

		/*MovingBlobDetection movingBlobsDetection = new MovingBlobDetection();
		MovingBlob movingBlobs = new MovingBlob();
		
		List<MovingBlob> movingBlobsList = new LinkedList<>();*/
		
		/*System.out.println(movingBlobsDetection.distanceLimitX);
		
		System.out.println(movingBlobs.x);
		
		float[] point = {movingBlobs.x, movingBlobs.y, movingBlobs.velocityX, movingBlobs.velocityY};
		
		System.out.println(Arrays.toString(point));
		
		float[][] finalPoints = new float[movingBlobsList.size()][4];
		
		System.out.println(Arrays.toString(finalPoints));*/
		
		/*float values[] = {movingBlobsDetection.distanceLimitX, movingBlobsDetection.distanceLimitY, 
						  movingBlobsDetection.velocityLimitIncreaseX, movingBlobsDetection.velocityLimitIncreaseY};*/
		
		return blobArray;
	}

	private void createD3Example() {

		D3 d3 = browser.getD3();
		
		System.out.println(Arrays.toString(blobData()));
		
		//Object blobX = blobData();
		
		//Object[] blobX = {blobData(0)};
		
		//Object[] test = blobData();
		//Object[] arr = blobData();
		//System.out.println(arr[0]);
		
		//System.out.println(Arrays.deepToString(blobData()));
						
		Object[] data = { 4.0, 8.0, 15.0, 16.0, 23.0, 42.0 };
						
		Double width = 420.0;
		Double barHeight = 20.0;
		Double maxX = 700.0;//Collections.max(Arrays.asList(blobData()[0].toString()));

		LinearScale x = d3.scale() //
				.linear() //
				.domain(0.0, maxX) //
				.range(0.0, width);

		d3.createJsVariable("x", x);

		Selection svg = d3.select(".svg") //
				.attr("width", width) //
				.attr("height", barHeight * blobData()[0].toString().length());

		String transformExpression = "function(d, i) { return \"translate(0,\" + i *" + barHeight + " + \")\"; }";

		Selection bar = svg.selectAll("g") //
				.data(blobData()[0].toString()) //
				.enter() //
				.append("g") //
				.attrExpression("transform", transformExpression);

		String rectStyle = "fill: green;";

		bar.append("rect") //
				.attr("width", x) //
				.attr("height", barHeight - 1) //
				.attr("style", rectStyle);

		String textStyle = "fill: white; font: 10px sans-serif; text-anchor: end;";

		bar.append("text") //
				.attrExpression("x", "function(d) { return x(d) - 3; }") //
				.attr("y", barHeight / 2) //
				.attr("dy", ".35em") //
				.textExpression("function(d) { return d; }") //
				.attr("style", textStyle);
		
		//d3.saveSvg("C:/test.svg");

	}
	
	public List<MovingBlob> getMovingBlobs(List<Blob> blobList){
		for (Blob blob:blobList){
			this.movingBlobs.add(new MovingBlob(blob));
		}
		//System.out.println(this.movingBlobs);
		return movingBlobs;
	}

	public List<MovingBlob> getUnifiedBlobs(List<MovingBlob> blobs) {return blobs;}

	//#end region

}
