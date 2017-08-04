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

	/**
	 * BlobFilter
	 */

	//regular filters

	//Minimum age to not be filtered
	public static int AGE_MIN = 0;
	
	//Maximum velocity of MovingBlob
	public static int VELOCITY_X_MAX = 150;
	public static int VELOCITY_Y_MAX = 20;
	//minimum velocity of MovingBlob
	public static int VELOCITY_X_MIN = 2;
	public static int VELOCITY_Y_MIN = 0;
	//max change in velocity between frames of MovingBlob
	public static float MAX_VELOCITY_CHANGE_X = 15;
	public static float MAX_VELOCITY_CHANGE_Y = 20;
	
	//Unified Blob filters

	//maximum ratio of width divided by height of unifiedblob
	public static float MAX_WIDTH_HEIGHT_RATIO = .8f;
	//maximum width of unified blob
	public static int MAX_WIDTH = 1300;
	//maximum height of unified blob
	public static int MAX_HEIGHT = 1200;
	//minimum scaled velocity of unified blob
	public static float MIN_SCALED_VELOCITY_X = 1f;
	public static int MIN_SCALED_VELOCITY_Y = 0;
	
	//Unified Blob matching
	//bandwidth of kernel used for gaussian curve in mean shift
	public static float KERNEL_BANDWIDTH = 15;
	//maximum distance between points that can be clustered together
	public static float MAX_DIST_BETWEEN_POINTS_CLUSTER = 80;
	//weight of x distance for mean shift
	public static float X_DIST_WEIGHT = 1f;
	//weight of x distance for mean shift
	public static float Y_DIST_WEIGHT = 0.25f;
	//weight of x velocity for mean shift
	public static float V_X_WEIGHT = 1.5f;
	//weight of y velocity for mean shift
	public static float V_Y_WEIGHT = 0.25f;
	
	//distance that unified blobs can be matched
	public static float DISTANCE_MATCH_UNIFY_LIMIT = 60;
	//percent that matched unified blob width can change
	public static float PERCENT_WIDTH_CHANGE_UNIFIED_MATCH_LIMIT = 0.3f;
	//percent that matched unified blob width can change
	public static float PERCENT_HEIGHT_CHANGE_UNIFIED_MATCH_LIMIT = 0.3f;
	
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
        public static double getVariable(int index){
		switch(index){
		case 1: return MAX_TIME_OFF_SCREEN ;
		case 2: return DISTANCE_LIMIT_X;
		case 3: return DISTANCE_LIMIT_Y;
		case 4: return MAX_CHANGE_WIDTH;
		case 5: return MAX_CHANGE_HEIGHT;
		case 14: return AGE_MIN;
		case 15: return VELOCITY_X_MAX;
		case 16: return VELOCITY_Y_MAX;
		case 17: return MAX_VELOCITY_CHANGE_X;
		case 18: return MAX_VELOCITY_CHANGE_Y;
		case 19: return MAX_WIDTH_HEIGHT_RATIO;
		case 20: return MAX_WIDTH;
		case 21: return MAX_HEIGHT;
		case 22: return MIN_SCALED_VELOCITY_X;
		case 23: return MIN_SCALED_VELOCITY_Y;
                
                
		}
                return 0;
        }
	
}
