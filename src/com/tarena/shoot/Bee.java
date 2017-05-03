package com.tarena.shoot;

import java.util.Random;

/** 蜜蜂: 既是飞行物,又是奖励 */
public class Bee extends FlyingObject implements Award {
	private int xSpeed = 1;// x每次走一步
	private int ySpeed = 2;// y每次走两步
	private int awardType;// 奖励的类型

	/** 构造方法 */
	public Bee() {
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);// 窗口宽-蜜蜂宽
		// y = -this.height;
		y = -this.height;
		awardType = rand.nextInt(2);// 0-1之间的随机数
	}

	/** 重写getType方法 */
	public int getType() {
		return awardType;
	}

	public void step() {
		x += xSpeed;
		y += ySpeed;
		if (x > ShootGame.WIDTH - this.width) {
			xSpeed = -1;
		}
		if (x <= 0) {
			xSpeed = 1;
		}
	}
	/** 重写outOfBounds */
	public boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT; //敌机的y坐标>=屏幕的高,即为越界
	}

}
