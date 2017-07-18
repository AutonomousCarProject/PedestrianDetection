/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram;


public class MovingBlob extends Blob {
    public float velocityX, velocityY;
    public int age;
    public int ageOffScreen;
    public int predictedX;
    public int predictedY;
	
	public MovingBlob(Blob b){
		super(b.width, b.height, b.centerX, b.centerY, b.color);
                
		
		this.velocityX = 0;
		this.velocityY = 0;
		this.age = 0;
		this.ageOffScreen = 0;
	}
}