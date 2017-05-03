package com.tarena.shoot;

import java.util.Random;

/** �ӵ�:ֻ�Ƿ����� */
public class Bullet extends FlyingObject {
	private int speed = 3;// �ӵ��߲��Ĳ���

	public Bullet(int x, int y) {
		image = ShootGame.bullet;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x;
		this.y = y;
	}

	public void step() {
		y -= speed;// y-������
	}

	/** ��дoutOfBounds */
	public boolean outOfBounds() {
		return this.y<=-this.height;
	}
}
