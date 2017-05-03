package com.tarena.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Hero extends FlyingObject {
	private int life; // ����
	private int doubleFire; // ����ֵ
	private BufferedImage[] images;// ͼƬ����
	private int index;// Э��ͼƬ�л�

	public Hero() {
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;// ����̶�
		y = 400;// ����̶�
		life = 3;// ��ʼ����Ϊ3
		doubleFire = 0;// Ĭ��Ϊ0 ����������
		images = new BufferedImage[] { ShootGame.hero0, ShootGame.hero1 };// Ӣ�ۻ������ʼ��
		index = 0;// Э���л� ��ʼΪ0

	}

	public void step() {// 10ms��һ��
		image = images[index++ / 10 % 2];
	}

	/** Ӣ�ۻ������ӵ� */
	public Bullet[] shoot() {// �����ӵ�
		int xStep = this.width / 4;// ��Ӣ�ۻ���Ϊ4��
		int yStep = 20;
		if (doubleFire > 0) {// ˫��
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x + 1 * xStep, this.y - yStep);
			bs[1] = new Bullet(this.x + 3 * xStep, this.y - yStep);
			doubleFire -= 2;
			return bs;
		} else {// ����
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x + 2 * xStep, this.y - yStep);
			return bs;
		}
	}

	/** Ӣ�ۻ���������ƶ� */
	public void moveTo(int x, int y) {
		this.x = x - this.width / 2;// Ӣ�ۻ���x = ����x-1/2��Ӣ�ۻ��Ŀ�
		this.y = y - this.height / 2;// Ӣ�ۻ���y = ����y-1/2��Ӣ�ۻ� �ĸ�
	}

	/** ��дoutOfBounds */
	public boolean outOfBounds() {
		return false;// ����Խ��
	}

	public void addDoubleFire() {
		doubleFire += 40;
	}

	public void addLife() {
		life++;
	}

	public void subtractLife() {
		life--;
	}

	public void clearDoubleFire() {
		doubleFire = 0;
	}

	public int getLife() {
		return life;
	}

	public int getDoubleFire() {
		return doubleFire;
	}

	/** Ӣ�ۻ�ײ���� */
	public boolean hit(FlyingObject other) {
		int x1 = other.x - this.width / 2;
		int x2 = other.x + other.width - this.width / 2;
		int y1 = other.y - this.height / 2;
		int y2 = other.y + other.height + this.height / 2;
		int x = this.x + this.width / 2;
		int y = this.y + this.height / 2;
		return x > x1 && x < x2 && y > y1 && y < y2;
	}
}
