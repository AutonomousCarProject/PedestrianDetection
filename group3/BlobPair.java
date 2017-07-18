package group3;

import group2.Blob;

public class BlobPair implements Comparable<BlobPair>{
	
	float distance;
	Blob newBlob;
	MovingBlob oldBlob;
	
	
	public BlobPair(float distance, Blob newBlob,MovingBlob oldBlob) {
		this.distance = distance;
		this.newBlob = newBlob;
		this.oldBlob = oldBlob;
	}

	public int compareTo(BlobPair o) {
		return (int)Math.signum(distance-o.distance);
	}
	
	
}
