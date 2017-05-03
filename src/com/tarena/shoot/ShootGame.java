package com.tarena.shoot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;//操作图片的读取
import javax.swing.JFrame;//框架
import javax.swing.JPanel;//画板
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;

/** 主程序类 */

public class ShootGame extends JPanel {
	public static final int WIDTH = 400;// 窗口宽
	public static final int HEIGHT = 680;// 窗口高
	private static final Color Color = null;
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bee_r;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	private int state = START;// 当前的状态(默认为启动状态)
	private Hero hero = new Hero();
	private FlyingObject[] flyings = {};
	private Bullet[] bullets = {};

	static {// 初始化静态资源
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 生成敌人对象 */
	public FlyingObject nextOne() {
		Random random = new Random();
		int type = random.nextInt(20);
		if (type == 0) {
			return new Bee();
		} else {
			return new Airplane();
		}
	}

	int flyEnteredIndex = 0;

	/** 敌人入场 */
	public void enterAction() {// 10ms走一次
		// 1.创建敌人对象
		// 2.将敌人对象添加到flyings中
		flyEnteredIndex++;
		if (flyEnteredIndex % 40 == 0) {
			FlyingObject obj = nextOne();// 获取敌人对象
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;// 将obj赋值给数组最后一个
		}
	}

	/** 飞行物走一步 */
	public void stepAction() {// 10ms走一次
		hero.step();
		for (int i = 0; i < flyings.length; i++) {
			flyings[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
	}

	int shootIndex = 0;// 子弹入场计数

	/** 子弹入场 */
	public void shootAction() {
		// 1.获取子弹对象
		// 2.添加到bullets数组中
		shootIndex++;
		if (shootIndex % 30 == 0) {
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);// 扩容
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);// 数组的追加
		}
	}

	/** outOfBounds 删除越界敌人和子弹 */
	public void outOfBoundsAction() {
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];// 不越界敌人数组
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];// 获取每一个敌人
			if (!f.outOfBounds()) {// 不越界
				flyingLives[index] = f;// 将不越界敌人添加到不越界数组
				index++;// 下标加一
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);
		// System.out.println(flyings.length);
		index = 0;// 归零
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {// 不越界
				bulletLives[index] = b;// 将不越界敌人添加到不越界数组
				index++;// 下标加一
			}
		}

	}

	/** 所有子弹与所有敌人的碰撞 */
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) {
			bang(bullets[i]);// 一个子弹与所有敌人撞
		}
	}

	int score = 0;// 得分

	/** 一个子弹和所有敌人撞 */
	public void bang(Bullet b) {
		int index = -1;// 被撞敌人的下标
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (f.shootBy(b)) {// 撞上了
				index = i;// 记录被撞敌人的下标
				break;// 其余的敌人不在比较
			}
		}
		if (index != -1) {// 有撞上的
			FlyingObject one = flyings[index];// 获取被撞敌人对象
			if (one instanceof Enemy) {// 是敌人
				Enemy e = (Enemy) one;// 强转为敌人
				score += e.getScore();// 玩家增分
			}
			if (one instanceof Award) {// 是奖励
				Award a = (Award) one;// 强转为奖励
				int type = a.getType();// 获取奖励类型
				switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
			}
			// 将被撞敌人对象和数组最后一个元素交换
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = t;
			// 缩容, 去掉最后一个元素,即被撞的敌人对象
			flyings = Arrays.copyOf(flyings, flyings.length - 1);
		}

	}

	/** 检查游戏结束 */
	public void checkGameOverAction() {
		if (isGameOver()) {// 游戏结束
			state = GAME_OVER;
		}
	}

	/** 判断游戏是否结束 返回true即为结束 */
	public boolean isGameOver() {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (hero.hit(f)) { // 撞上了
				hero.subtractLife();// 英雄机减命
				hero.clearDoubleFire();
				FlyingObject t = flyings[i];
				flyings[i] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length - 1);
			}
		}
		return hero.getLife() <= 0;
	}

	/** 启动程序的执行 */
	public void action() {
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}

			/** 重写鼠标事件 */
			public void mouseClicked(MouseEvent e) {
				switch (state) {// 根据当前状态做不同的事
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					state = START;
					// 清理现场
					score = 0;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					break;
				}
			}

			public void mouseExited(MouseEvent e) {
				if (state == RUNNING) {//运行状态改为暂停
					state = PAUSE;
				}
			}

			public void mouseEntered(MouseEvent e) {
				if (state == PAUSE) {//暂停状态改为运行
					state = RUNNING;
				}

			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		Timer timer = new Timer();// 创建定时器对象
		int intervel = 10;// 定时间隔(ms为单位)
		timer.schedule(new TimerTask() {
			public void run() { // 10毫秒干的事
				if (state == RUNNING) {
					enterAction();// 敌人入场
					stepAction();// 飞行物走一步
					shootAction();// 子弹入场(英雄机)
					outOfBoundsAction();// 删除越界
					bangAction();// 子弹与敌人的碰撞
					checkGameOverAction();// 检查游戏结束
				}
				repaint();// 重画
			}
		}, intervel, intervel);
	}

	/** 重写paint方法 g:画笔 */
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintHero(g);// 画英雄机对象
		paintFlyingObjects(g);// 画敌人对象
		paintBullets(g);// 画子弹对象
		paintScoreAndLife(g);// 画分和画命
		paintState(g);// 画状态
	}

	/** 画英雄机对象 */
	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.x, hero.y, null);
	}

	/** 画敌人对象 */
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.image, f.x, f.y, null);
		}
	}

	/** 画子弹对象 */
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}
	}

	/** 画分和画命 */
	public void paintScoreAndLife(Graphics g) {
		g.setColor(new Color(0xFF0000));// 设置颜色
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
		g.drawString("SCOEE:" + score, 10, 25);
		g.drawString("LIFE:" + hero.getLife(), 10, 50);
		g.drawString("FIRE=" + hero.getDoubleFire(), 10, 75);
	}

	/** 画状态 */
	public void paintState(Graphics g) {
		switch (state) {// 根据状态画图
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("fly");
		ShootGame game = new ShootGame();
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);// 窗口大小
		frame.setAlwaysOnTop(true);// 总是居上
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置默认关闭方式(窗口关闭则退出程序)
		frame.setLocationRelativeTo(null);// 窗口居中
		frame.setVisible(true);// 1.设置窗口可见 2.尽快调用paint()

		game.action();// 启动程序的执行
	}

}
