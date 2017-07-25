package global;

public class CONSTANTS {
	
	//maximum time before unmatched MovingBlob is deleted
	public static final int MAX_TIME_OFF_SCREEN = 2;
	
	//maximum distance in pixels between blobs that can be matched
	public static final int DISTANCE_LIMIT = 20;
	
	//maximum distance between edges to unify
	public static final int X_EDGE_DISTANCE_LIMIT = 25;
	public static final int Y_EDGE_DISTANCE_LIMIT = 30;
	public static final float X_OVERLAP_PERCENT = 0.4f;
	public static final float Y_OVERLAP_PERCENT = 0.4f;
	
	//maximum difference in velocity to unify
	public static final int UNIFY_VELOCITY_LIMIT_X = 20;
	public static final int UNIFY_VELOCITY_LIMIT_Y = 30;
	public static final float VELOCITY_LIMIT_INCREASE_X = 0.5f;
	public static final float VELOCITY_LIMIT_INCREASE_Y = 0.5f;
	
	//Minimum age to not be filtered
	public static final short AGE_MIN = 3;
	
	//Maximum 
	public static final short VELOCITY_X_MAX = 20;
	public static final short VELOCITY_Y_MAX = 10;
	public static final float MAX_VELOCITY_CHANGE_X = 3;
	public static final float MAX_VELOCITY_CHANGE_Y = 2;
	
	//Unified Blob filters
	public static final short MAX_WIDTH_HEIGHT_RATIO = 1;
	public static final short MAX_WIDTH = 100;
	public static final short MAX_HEIGHT = 200;
	public static final short MAX_SCALED_VELOCITY_X = 10;
	public static final short MAX_SCALED_VELOCITY_Y = 10;
	
}
