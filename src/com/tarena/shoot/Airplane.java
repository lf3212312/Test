package com.tarena.shoot;

import java.util.Random;

/** 敌机: 既是飞行物,又是敌人 */
public class Airplane extends FlyingObject implements Enemy {
	private int speed = 2;// 敌机走步的步数

	public Airplane() {// 敌机的构造方法
		image = ShootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);
		// y = -this.height;
		y = -this.height;
	}

	/** 重写score方法 */
	public int getScore() {
		return 5;
	}
	/** 重写step */
	public void step(){
		y+=speed;
	}
	/** 重写outOfBounds */
	public boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT; //敌机的y坐标>=屏幕的高,即为越界
	}
}
