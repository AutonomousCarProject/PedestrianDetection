package group3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import org.treez.javafxd3.d3.D3;
import org.treez.javafxd3.d3.JavaFxD3DemoSuite.DemoMenuButton;
import org.treez.javafxd3.d3.core.Selection;
import org.treez.javafxd3.d3.demo.DemoCase;
import org.treez.javafxd3.d3.demo.DemoFactory;
import org.treez.javafxd3.d3.democases.axis.AxisComponent;
import org.treez.javafxd3.d3.scales.LinearScale;

import org.treez.javafxd3.javafx.JavaFxD3Browser;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
public class JavaFxD3SingleDemo extends Application implements IMovingBlobDetection {

	//#region ATTRIBUTES

	/**
	 * The JavaFx scene
	 */
	private Scene scene;
	
	private StackPane sceneContent;

	private JavaFxD3Browser browser;
	
	private DemoCase currentDemo;
	
	private final static int DEMO_BUTTON_WIDTH = 180;
	
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
	
	public JavaFxD3SingleDemo() {
		movingBlobs = new LinkedList<>();
	}

	public void start(Stage stage) {
		
		//set state title
		stage.setTitle("Single javafx-d3 demo");
		
		// create content node
		sceneContent = new StackPane();
		HBox hBox = new HBox();
		sceneContent.getChildren().add(hBox);
		List<Node> hBoxChildren = hBox.getChildren();
		
		// create box for demo case menu buttons
		VBox demoMenuBox = new VBox();
		demoMenuBox.setPrefWidth(DEMO_BUTTON_WIDTH);
		hBoxChildren.add(demoMenuBox);

		// create box for preferences of active demo
		VBox demoPreferenceBox = new VBox();
		// demoPreferenceBox.setPrefWidth(100);
		demoPreferenceBox.setStyle("-fx-background-color: steelblue");
		hBoxChildren.add(demoPreferenceBox);

		//define d3 content as post loading hook
		Runnable postLoadingHook = () -> {
			System.out.println("Initial loading of browser is finished");

			//do some d3 stuff
			createD3Example();
			
			runDemoSuite(stage, demoMenuBox, demoPreferenceBox);
			
		};
				
		//create browser
		browser = new JavaFxD3Browser(postLoadingHook, true);
		
		hBoxChildren.add(browser);

		//create the scene
		scene = new Scene(sceneContent, 750, 500, Color.web("#666970"));
		stage.setScene(scene);
		stage.show();
		
		
	}
	
	private Object[] blobData() {
		
		IImage image = new FileImage();
		image.setAutoFreq(15);
		
		image.readCam();
				
		//HashSet<Ex> someEx - new HashSet<>();
		
		//HashSet<MovingBlob> movingBlobSet = new HashSet<>(getMovingBlobs());
		
		HashSet<MovingBlob> movingBlobSet = new HashSet<>(getMovingBlobs());
		
		//System.out.println(movingBlobSet);
				
		ArrayList<Float> blobX = new ArrayList<Float>();
		ArrayList<Float> blobY = new ArrayList<Float>();
		ArrayList<Float> blobPredictedX = new ArrayList<Float>();
		ArrayList<Float> blobPredictedY = new ArrayList<Float>();
		ArrayList<Float> blobVelocityX = new ArrayList<Float>();
		ArrayList<Float> blobVelocityY = new ArrayList<Float>();
		ArrayList<Float> blobVelocityChangeX = new ArrayList<Float>();
		ArrayList<Float> blobVelocityChangeY = new ArrayList<Float>();
		ArrayList<Float> blobWidth = new ArrayList<Float>();
		ArrayList<Float> blobHeight = new ArrayList<Float>();
		ArrayList<Float> blobAge = new ArrayList<Float>();
		ArrayList<Float> blobAgeOffScreen = new ArrayList<Float>();
				
		ArrayList<ArrayList<Float>> blobValues = new ArrayList<ArrayList<Float>>();
		
		ArrayList<String> blobStrings = new ArrayList<String>();
		
        IBlobDetection blobDetect = new BlobDetection();
        IMovingBlobDetection movingBlobDetect = new MovingBlobDetection();
        IMovingBlobReduction blobFilter = new BlobFilter();
        
        MovingBlob movingBlob = new MovingBlob();

        List<Blob> blobs = blobDetect.getBlobs(image);
        List<MovingBlob> movingBlobs = movingBlobDetect.getMovingBlobs(blobs);
                
        //System.out.println(movingBlobs);
        
        //System.out.println(movingBlobs.toArray().length);
        
        for(int i = 0; i < movingBlobs.toArray().length; i++) {
        	//blobX.add(movingBlobs)
        	//System.out.println(movingBlobs.get(i));
        	
        	blobX.add((float)movingBlobs.get(i).x);
        	blobY.add((float)movingBlobs.get(i).y);
        	blobPredictedX.add(movingBlobs.get(i).predictedX);
        	blobPredictedY.add(movingBlobs.get(i).predictedY);
        	blobVelocityX.add(movingBlobs.get(i).velocityX);
        	blobVelocityY.add(movingBlobs.get(i).velocityY);
        	blobVelocityChangeX.add(movingBlobs.get(i).velocityChangeX);
        	blobVelocityChangeY.add(movingBlobs.get(i).velocityChangeY);
        	blobWidth.add((float)movingBlobs.get(i).width);
        	blobHeight.add((float)movingBlobs.get(i).height);
        	blobAge.add((float)movingBlobs.get(i).age);
        	blobAgeOffScreen.add((float)movingBlobs.get(i).ageOffScreen);
        	
        	//arr = "X"+movingBlobs.get(i).x;
        	
        	//System.out.println(arr);
        	
        	String blobString = "\n"+movingBlobs.get(i).x + "," + movingBlobs.get(i).y + "," + movingBlobs.get(i).predictedX + "," + movingBlobs.get(i).predictedY + "," + movingBlobs.get(i).velocityX + "," + movingBlobs.get(i).velocityY + "," + movingBlobs.get(i).velocityChangeX + "," + movingBlobs.get(i).velocityChangeY + "," + movingBlobs.get(i).width + "," + movingBlobs.get(i).height + "," + movingBlobs.get(i).age + "," + movingBlobs.get(i).ageOffScreen;
        	
        	blobStrings.add(blobString);
        	
        	//System.out.println(blobString);
        }
        
        //System.out.println(Arrays.toString(blobStrings.toArray()));
              
        
		for (Blob blob : blobs)
        {
			
			/*blobX.add(blob.x);
			blobY.add(blob.y);
			blobWidth.add(blob.width);
			blobHeight.add(blob.height);*/
					
        }
		
		blobValues.add(blobX);
		blobValues.add(blobY);
		blobValues.add(blobPredictedX);
		blobValues.add(blobPredictedY);
		blobValues.add(blobVelocityX);
		blobValues.add(blobVelocityY);
		blobValues.add(blobVelocityChangeX);
		blobValues.add(blobVelocityChangeY);
		blobValues.add(blobWidth);
		blobValues.add(blobHeight);
		blobValues.add(blobAge);
		blobValues.add(blobAgeOffScreen);
		
		
		//System.out.println(blobValues);
		
		String blobString = "X, Y, Predicted X, Predicted Y, Velocity X, Velocity Y, Velocity Change X, Velocity Change Y, Width, Height, Age, Age Off Screen" +
							blobStrings.toString().substring(1, blobStrings.toString().length()-1);
		//					movingBlobs.get(0).x + "," + movingBlobs.get(0).y + "," + movingBlobs.get(0).predictedX + "," + movingBlobs.get(0).predictedY + "," + movingBlobs.get(0).velocityX + "," + movingBlobs.get(0).velocityY + ","	+ movingBlobs.get(0).velocityChangeX + ", " + movingBlobs.get(0).velocityChangeY + "," + movingBlobs.get(0).width + "," + movingBlobs.get(0).height + "," + movingBlobs.get(0).age + "," + movingBlobs.get(0).ageOffScreen + "\n";
				
		System.out.println(blobString);
		
		
		Object[] blobArray = blobValues.toArray();
		
		//System.out.println(Arrays.toString(blobArray));
				
		return blobArray;
	}
	
	public List<MovingBlob> getMovingBlobs(List<Blob> blobList){return movingBlobs;}
	
	private void runDemoSuite(Stage stage, VBox buttonPane, VBox demoPreferenceBox) {
		// get d3 wrapper
		D3 d3 = browser.getD3();

		// set stage title
		String versionString = "D3 API version: " + d3.version();
		String title = "Welcome to javax-d3 : A thin Java wrapper around d3." + versionString;
		stage.setTitle(title);

		// create demo menu buttons
		createDemoSuiteMenu(d3, buttonPane, demoPreferenceBox);

	}

	private void createD3Example() {

		D3 d3 = browser.getD3();
		
		blobData();
						
		Object[] data = { 4.0, 8.0, 15.0, 16.0, 23.0, 42.0 };
						
		Double width = 420.0;
		Double barHeight = 20.0;
		Double maxX = 500.0;//Collections.max(Arrays.asList(blobData()[0].toString()));

		LinearScale x = d3.scale() //
				.linear() //
				.domain(0.0, maxX) //
				.range(0.0, width);

		d3.createJsVariable("x", x);

		Selection svg = d3.select(".svg") //
				.attr("width", width) //
				.attr("height", barHeight * data.length);//blobData()[0].toString().length());
				//.attr("style", "bar");
		

		String transformExpression = "function(d, i) { return \"translate(0,\" + i *" + barHeight + " + \")\"; }";

		Selection bar = svg.selectAll("g") //
				.data(data)//blobData().get(0).toString())//blobData()[0].toString()) //
				.enter() //
				.append("g") //
				.attrExpression("transform", transformExpression);

		String rectStyle = "fill: green;";

		bar.append("rect") //
				.attr("width", x) //
				.attr("height", barHeight - 1) //
				.attr("style", rectStyle);
				//.on(mouseover, listener)
		
		//d3.select("rect").on("mouseover", public void(){});//"function(d){  }");
		
		//d3.select("g").attr("style","rect:hover {fill: blue}");

		String textStyle = "fill: white; font: 10px sans-serif; text-anchor: end;";

		bar.append("text") //
				.attrExpression("x", "function(d) { return x(d) - 3; }") //
				.attr("y", barHeight / 2) //
				.attr("dy", ".35em") //
				.textExpression("function(d) { return d; }") //
				.attr("style", textStyle);
		
		//d3.saveSvg("C:/test.svg");

	}
	
	private void createDemoSuiteMenu(D3 d3, VBox demoMenu, VBox prefBox) {

		List<Node> menuChildren = demoMenu.getChildren();
		
		menuChildren.add(new DemoMenuButton("Axis Component", AxisComponent.factory(d3, prefBox)));
		
	}
	
	public List<MovingBlob> getMovingBlobs() {
		return movingBlobs;
	}

	public List<MovingBlob> getUnifiedBlobs(List<MovingBlob> blobs) {return blobs;}

	//#end region
	
	public Selection clearContent() {

		D3 d3 = browser.getD3();
		d3.selectAll("svg").remove();
		d3.select("#root").selectAll("*").remove();
		d3.select("head").selectAll("link").remove();

		Selection svg = d3.select("#root") //
				.append("svg") //
				.attr("id", "svg");
		return svg;
	}
	
	public class DemoMenuButton extends Button {

		//#region CONSTRUCTORS

		public DemoMenuButton(final String title, final DemoFactory demoClass/*, final Object data*/) {
			super(title);

			this.onMouseClickedProperty().set((event) -> {
				stopCurrentDemo();
				createAndStartNewDemo(demoClass);
			});
		}

		//#end region

		//#region METHODS

		private void stopCurrentDemo() {
			if (currentDemo != null) {
				currentDemo.stop();
				currentDemo = null;

			}
		}

		private void createAndStartNewDemo(final DemoFactory demoClass) {
			clearContent();
			DemoCase demo = demoClass.newInstance();
			currentDemo = demo;
			demo.start();
		}

		//#end region
	}

}
