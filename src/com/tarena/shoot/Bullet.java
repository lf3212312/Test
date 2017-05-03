package com.tarena.shoot;

import java.util.Random;

/** 子弹:只是飞行物 */
public class Bullet extends FlyingObject {
	private int speed = 3;// 子弹走步的步数

	public Bullet(int x, int y) {
		image = ShootGame.bullet;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x;
		this.y = y;
	}

	public void step() {
		y -= speed;// y-是向上
	}

	/** 重写outOfBounds */
	public boolean outOfBounds() {
		return this.y<=-this.height;
	}
}
