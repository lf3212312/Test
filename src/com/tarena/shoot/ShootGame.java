package com.tarena.shoot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;//����ͼƬ�Ķ�ȡ
import javax.swing.JFrame;//���
import javax.swing.JPanel;//����
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;

/** �������� */

public class ShootGame extends JPanel {
	public static final int WIDTH = 400;// ���ڿ�
	public static final int HEIGHT = 680;// ���ڸ�
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
	private int state = START;// ��ǰ��״̬(Ĭ��Ϊ����״̬)
	private Hero hero = new Hero();
	private FlyingObject[] flyings = {};
	private Bullet[] bullets = {};

	static {// ��ʼ����̬��Դ
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

	/** ���ɵ��˶��� */
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

	/** �����볡 */
	public void enterAction() {// 10ms��һ��
		// 1.�������˶���
		// 2.�����˶�����ӵ�flyings��
		flyEnteredIndex++;
		if (flyEnteredIndex % 40 == 0) {
			FlyingObject obj = nextOne();// ��ȡ���˶���
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;// ��obj��ֵ���������һ��
		}
	}

	/** ��������һ�� */
	public void stepAction() {// 10ms��һ��
		hero.step();
		for (int i = 0; i < flyings.length; i++) {
			flyings[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
	}

	int shootIndex = 0;// �ӵ��볡����

	/** �ӵ��볡 */
	public void shootAction() {
		// 1.��ȡ�ӵ�����
		// 2.��ӵ�bullets������
		shootIndex++;
		if (shootIndex % 30 == 0) {
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);// ����
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);// �����׷��
		}
	}

	/** outOfBounds ɾ��Խ����˺��ӵ� */
	public void outOfBoundsAction() {
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];// ��Խ���������
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];// ��ȡÿһ������
			if (!f.outOfBounds()) {// ��Խ��
				flyingLives[index] = f;// ����Խ�������ӵ���Խ������
				index++;// �±��һ
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);
		// System.out.println(flyings.length);
		index = 0;// ����
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {// ��Խ��
				bulletLives[index] = b;// ����Խ�������ӵ���Խ������
				index++;// �±��һ
			}
		}

	}

	/** �����ӵ������е��˵���ײ */
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) {
			bang(bullets[i]);// һ���ӵ������е���ײ
		}
	}

	int score = 0;// �÷�

	/** һ���ӵ������е���ײ */
	public void bang(Bullet b) {
		int index = -1;// ��ײ���˵��±�
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (f.shootBy(b)) {// ײ����
				index = i;// ��¼��ײ���˵��±�
				break;// ����ĵ��˲��ڱȽ�
			}
		}
		if (index != -1) {// ��ײ�ϵ�
			FlyingObject one = flyings[index];// ��ȡ��ײ���˶���
			if (one instanceof Enemy) {// �ǵ���
				Enemy e = (Enemy) one;// ǿתΪ����
				score += e.getScore();// �������
			}
			if (one instanceof Award) {// �ǽ���
				Award a = (Award) one;// ǿתΪ����
				int type = a.getType();// ��ȡ��������
				switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
			}
			// ����ײ���˶�����������һ��Ԫ�ؽ���
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = t;
			// ����, ȥ�����һ��Ԫ��,����ײ�ĵ��˶���
			flyings = Arrays.copyOf(flyings, flyings.length - 1);
		}

	}

	/** �����Ϸ���� */
	public void checkGameOverAction() {
		if (isGameOver()) {// ��Ϸ����
			state = GAME_OVER;
		}
	}

	/** �ж���Ϸ�Ƿ���� ����true��Ϊ���� */
	public boolean isGameOver() {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (hero.hit(f)) { // ײ����
				hero.subtractLife();// Ӣ�ۻ�����
				hero.clearDoubleFire();
				FlyingObject t = flyings[i];
				flyings[i] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length - 1);
			}
		}
		return hero.getLife() <= 0;
	}

	/** ���������ִ�� */
	public void action() {
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}

			/** ��д����¼� */
			public void mouseClicked(MouseEvent e) {
				switch (state) {// ���ݵ�ǰ״̬����ͬ����
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					state = START;
					// �����ֳ�
					score = 0;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					break;
				}
			}

			public void mouseExited(MouseEvent e) {
				if (state == RUNNING) {//����״̬��Ϊ��ͣ
					state = PAUSE;
				}
			}

			public void mouseEntered(MouseEvent e) {
				if (state == PAUSE) {//��ͣ״̬��Ϊ����
					state = RUNNING;
				}

			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		Timer timer = new Timer();// ������ʱ������
		int intervel = 10;// ��ʱ���(msΪ��λ)
		timer.schedule(new TimerTask() {
			public void run() { // 10����ɵ���
				if (state == RUNNING) {
					enterAction();// �����볡
					stepAction();// ��������һ��
					shootAction();// �ӵ��볡(Ӣ�ۻ�)
					outOfBoundsAction();// ɾ��Խ��
					bangAction();// �ӵ�����˵���ײ
					checkGameOverAction();// �����Ϸ����
				}
				repaint();// �ػ�
			}
		}, intervel, intervel);
	}

	/** ��дpaint���� g:���� */
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintHero(g);// ��Ӣ�ۻ�����
		paintFlyingObjects(g);// �����˶���
		paintBullets(g);// ���ӵ�����
		paintScoreAndLife(g);// ���ֺͻ���
		paintState(g);// ��״̬
	}

	/** ��Ӣ�ۻ����� */
	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.x, hero.y, null);
	}

	/** �����˶��� */
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.image, f.x, f.y, null);
		}
	}

	/** ���ӵ����� */
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}
	}

	/** ���ֺͻ��� */
	public void paintScoreAndLife(Graphics g) {
		g.setColor(new Color(0xFF0000));// ������ɫ
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
		g.drawString("SCOEE:" + score, 10, 25);
		g.drawString("LIFE:" + hero.getLife(), 10, 50);
		g.drawString("FIRE=" + hero.getDoubleFire(), 10, 75);
	}

	/** ��״̬ */
	public void paintState(Graphics g) {
		switch (state) {// ����״̬��ͼ
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
		frame.setSize(WIDTH, HEIGHT);// ���ڴ�С
		frame.setAlwaysOnTop(true);// ���Ǿ���
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ����Ĭ�Ϲرշ�ʽ(���ڹر����˳�����)
		frame.setLocationRelativeTo(null);// ���ھ���
		frame.setVisible(true);// 1.���ô��ڿɼ� 2.�������paint()

		game.action();// ���������ִ��
	}

}
