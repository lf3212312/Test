package com.tarena.shoot;

import java.util.Random;

/** �л�: ���Ƿ�����,���ǵ��� */
public class Airplane extends FlyingObject implements Enemy {
	private int speed = 2;// �л��߲��Ĳ���

	public Airplane() {// �л��Ĺ��췽��
		image = ShootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);
		// y = -this.height;
		y = -this.height;
	}

	/** ��дscore���� */
	public int getScore() {
		return 5;
	}
	/** ��дstep */
	public void step(){
		y+=speed;
	}
	/** ��дoutOfBounds */
	public boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT; //�л���y����>=��Ļ�ĸ�,��ΪԽ��
	}
}
