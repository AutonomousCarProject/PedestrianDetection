package global;

public class Constant {
	/*
	    Image
	    FileImage
	 */

	//constants for color margin calibrations
	//ratio of absolute average deviation to greyMargin
    public static float GREY_RATIO = 0.75f;

    //how far to set black and white margins from mean
    public static int BLACK_RANGE = 100;
    public static int WHITE_RANGE = 200;

    /**
	 * MovingBlobDetection
	 */

	//maximum time before unmatched MovingBlob is deleted
	public static int MAX_TIME_OFF_SCREEN = 0;
	
	//maximum distance in pixels between blobs that can be matched
	public static int DISTANCE_LIMIT_X = 13;
	public static int DISTANCE_LIMIT_Y = 10;
	
	//maximum size difference in pixels between blobs that can be matched
	public static int MAX_CHANGE_WIDTH = 14;
	public static int MAX_CHANGE_HEIGHT = 13;
	
	//maximum distance between edges to unify
	public static int X_EDGE_DISTANCE_LIMIT = 25;
	public static int Y_EDGE_DISTANCE_LIMIT = 30;
	public static float X_OVERLAP_PERCENT = 0.4f;
	public static float Y_OVERLAP_PERCENT = 0.4f;
	
	//maximum difference in velocity to unify
	public static int UNIFY_VELOCITY_LIMIT_X = 20;
	public static int UNIFY_VELOCITY_LIMIT_Y = 30;
	public static float VELOCITY_LIMIT_INCREASE_X = 0.5f;
	public static float VELOCITY_LIMIT_INCREASE_Y = 0.5f;

	/**
	 * BlobFilter
	 */

	//regular filters

	//Minimum age to not be filtered
	public static short AGE_MIN = 3;
	
	//Maximum 
	public static short VELOCITY_X_MAX = 21;
	public static short VELOCITY_Y_MAX = 12;
	public static float MAX_VELOCITY_CHANGE_X = 11;
	public static float MAX_VELOCITY_CHANGE_Y = 4;
	//Unified Blob filters

	//stuff
	public static float MAX_WIDTH_HEIGHT_RATIO = 1;
	public static short MAX_WIDTH = 100;
	public static short MAX_HEIGHT = 200;
	public static short MAX_SCALED_VELOCITY_X = 10;
	public static short MAX_SCALED_VELOCITY_Y = 10;
	
}
