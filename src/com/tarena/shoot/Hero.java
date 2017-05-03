package com.tarena.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Hero extends FlyingObject {
	private int life; // 生命
	private int doubleFire; // 火力值
	private BufferedImage[] images;// 图片数组
	private int index;// 协助图片切换

	public Hero() {
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;// 坐标固定
		y = 400;// 坐标固定
		life = 3;// 初始生命为3
		doubleFire = 0;// 默认为0 即单倍火力
		images = new BufferedImage[] { ShootGame.hero0, ShootGame.hero1 };// 英雄机数组初始化
		index = 0;// 协助切换 初始为0

	}

	public void step() {// 10ms走一次
		image = images[index++ / 10 % 2];
	}

	/** 英雄机发射子弹 */
	public Bullet[] shoot() {// 发射子弹
		int xStep = this.width / 4;// 将英雄机分为4份
		int yStep = 20;
		if (doubleFire > 0) {// 双倍
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x + 1 * xStep, this.y - yStep);
			bs[1] = new Bullet(this.x + 3 * xStep, this.y - yStep);
			doubleFire -= 2;
			return bs;
		} else {// 单倍
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x + 2 * xStep, this.y - yStep);
			return bs;
		}
	}

	/** 英雄机随着鼠标移动 */
	public void moveTo(int x, int y) {
		this.x = x - this.width / 2;// 英雄机的x = 鼠标的x-1/2的英雄机的宽
		this.y = y - this.height / 2;// 英雄机的y = 鼠标的y-1/2的英雄机 的高
	}

	/** 重写outOfBounds */
	public boolean outOfBounds() {
		return false;// 永不越界
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

	/** 英雄机撞敌人 */
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
