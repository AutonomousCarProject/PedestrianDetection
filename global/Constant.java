package global;

public class Constant {

    /**
	 * MovingBlobDetection
	 */

	//maximum time before unmatched MovingBlob is deleted
	public static int MAX_TIME_OFF_SCREEN = 0;
	
	//maximum distance in pixels between blobs that can be matched
	public static int DISTANCE_LIMIT_X = 40;
	public static int DISTANCE_LIMIT_Y = 20;
	
	//maximum size difference in pixels between blobs that can be matched
	public static int MAX_CHANGE_WIDTH = 29;
	public static int MAX_CHANGE_HEIGHT = 34;
	
	//maximum distance between edges to unify
	public static int X_EDGE_DISTANCE_LIMIT = 6;
	public static int Y_EDGE_DISTANCE_LIMIT = 10;
	public static float X_OVERLAP_PERCENT = 0.1f;
	public static float Y_OVERLAP_PERCENT = 0.1f;
	
	//maximum difference in velocity to unify
	public static int UNIFY_VELOCITY_LIMIT_X = 17;
	public static int UNIFY_VELOCITY_LIMIT_Y = 30;
	public static float VELOCITY_LIMIT_INCREASE_X = 0.5f;
	public static float VELOCITY_LIMIT_INCREASE_Y = 0.5f;

	/**
	 * BlobFilter
	 */

	//regular filters

	//Minimum age to not be filtered
	public static int AGE_MIN = 3;
	
	//Maximum 
	public static int VELOCITY_X_MAX = 100;
	public static int VELOCITY_Y_MAX = 50;
	public static float MAX_VELOCITY_CHANGE_X = 5;
	public static float MAX_VELOCITY_CHANGE_Y = 20;
	//Unified Blob filters

	//stuff
	public static float MAX_WIDTH_HEIGHT_RATIO = .75f;
	public static int MAX_WIDTH = 100000;
	public static int MAX_HEIGHT = 100000;
	public static float MIN_SCALED_VELOCITY_X = 0.75f;
	public static int MIN_SCALED_VELOCITY_Y = 0;
	
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
	
	public static void setVariable(int index, double a){
		switch(index){
		case 1: MAX_TIME_OFF_SCREEN = (int)a; break;
		case 2: DISTANCE_LIMIT_X = (int)a; break;
		case 3: DISTANCE_LIMIT_Y = (int)a; break;
		case 4: MAX_CHANGE_WIDTH  = (int)a; break;
		case 5: MAX_CHANGE_HEIGHT = (int)a; break;
		case 6: X_EDGE_DISTANCE_LIMIT = (int)a; break;
		case 7: Y_EDGE_DISTANCE_LIMIT = (int)a; break;
		case 8: X_OVERLAP_PERCENT = (float)a; break;
		case 9: Y_OVERLAP_PERCENT  = (float)a; break;
		case 10: UNIFY_VELOCITY_LIMIT_X = (int)a; break;
		case 11: UNIFY_VELOCITY_LIMIT_Y = (int)a; break;
		case 12: VELOCITY_LIMIT_INCREASE_X = (float)a; break;
		case 13: VELOCITY_LIMIT_INCREASE_Y = (float)a; break;
		case 14: AGE_MIN  = (int)a; break;
		case 15: VELOCITY_X_MAX = (int)a; break;
		case 16: VELOCITY_Y_MAX = (int)a; break;
		case 17: MAX_VELOCITY_CHANGE_X = (float)a; break;
		case 18: MAX_VELOCITY_CHANGE_Y = (float)a; break;
		case 19: MAX_WIDTH_HEIGHT_RATIO  = (float)a; break;
		case 20: MAX_WIDTH = (int)a; break;
		case 21: MAX_HEIGHT = (int)a; break;
		case 22: MIN_SCALED_VELOCITY_X = (int)a; break;
		case 23: MIN_SCALED_VELOCITY_Y = (int)a; break;
		}
	}
	
}
