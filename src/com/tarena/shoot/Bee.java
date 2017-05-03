package com.tarena.shoot;

import java.util.Random;

/** �۷�: ���Ƿ�����,���ǽ��� */
public class Bee extends FlyingObject implements Award {
	private int xSpeed = 1;// xÿ����һ��
	private int ySpeed = 2;// yÿ��������
	private int awardType;// ����������

	/** ���췽�� */
	public Bee() {
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);// ���ڿ�-�۷��
		// y = -this.height;
		y = -this.height;
		awardType = rand.nextInt(2);// 0-1֮��������
	}

	/** ��дgetType���� */
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
	/** ��дoutOfBounds */
	public boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT; //�л���y����>=��Ļ�ĸ�,��ΪԽ��
	}

}
