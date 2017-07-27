package group4;

import global.Constant;
import group3.MovingBlob;
import group4.IMovingBlobReduction;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlobFilter implements IMovingBlobReduction
{
	public List<MovingBlob> reduce(List<MovingBlob> blobs){
		return filterUnifiedBlobs(blobs);
	}

	public List<MovingBlob> filterMovingBlobs(List<MovingBlob> blobs){
		List<MovingBlob> ret = new LinkedList<>();
		for(MovingBlob blob : blobs){
			if(filterMovingBlob(blob)) ret.add(blob);
		}
		return ret;
	}
	
	//returns false if blob should be filtered
	private boolean filterMovingBlob(MovingBlob blob){
		return blob.age >= Constant.AGE_MIN &&
				Math.abs(blob.velocityY) < Constant.VELOCITY_Y_MAX &&
				Math.abs(blob.velocityX) < Constant.VELOCITY_X_MAX &&
				blob.velocityChangeX < Constant.MAX_VELOCITY_CHANGE_X &&
				blob.velocityChangeY < Constant.MAX_VELOCITY_CHANGE_Y;
	}
	
	public List<MovingBlob> filterUnifiedBlobs(List<MovingBlob> blobs){
		List<MovingBlob> ret = new LinkedList<>();
		for(MovingBlob blob : blobs){
			if(filterUnifiedBlob(blob)) ret.add(blob);
		}
		return ret;
	}
	
	private boolean filterUnifiedBlob(MovingBlob blob){
		return (float)blob.width / (float)blob.height < Constant.MAX_WIDTH_HEIGHT_RATIO &&
				blob.width < Constant.MAX_WIDTH &&
				blob.height < Constant.MAX_HEIGHT &&
				blob.getScaledVelocityX() < Constant.MAX_SCALED_VELOCITY_X &&
				blob.getScaledVelocityY() < Constant.MAX_SCALED_VELOCITY_Y;
	}
	
}