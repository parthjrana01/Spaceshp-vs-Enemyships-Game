import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.Timer;
import javax.swing.*;

// class to paint the frame
class PaintFrame{

	final int OriginalTileSIze = 16;// 16*16
	final int scale = 3;

	final int tileSize = OriginalTileSIze * scale;// 48
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int WIDTH = maxScreenCol * tileSize;// 768
	final int HEIGHT = maxScreenRow * tileSize;// 576

	Renderer renderer;
	Random rand;

	//boolean running=true;
	int xMotion, shotMotion, score = 0, life = 5,level;

	Font maruMonica, purisaB, prStart;
	final int titleScreen = 0;
	final int started = 1;
	final int gameOver = 2;
	final int gameLevels=-1;
	int gameState=0;

	int commandNum = 0;

	Rectangle ship;

	boolean addlife=true;

	Image bg; // declared object of Image class for background image
	Image heart;// heart image
	Image rocket; // enemy ships image
	Image rocket1; // spaceship image 
	Image shot1;
	ArrayList<Rectangle> enemies;
	ArrayList<Rectangle> shots;
	ArrayList<Rectangle>lives;
	JFrame jframe = new JFrame();

	public PaintFrame() throws IOException{
		bg = ImageIO.read(new File("bg3.jpg")); // initialized bg object with image-file name
		heart = ImageIO.read(new File("heart.gif"));// heart image
		rocket = ImageIO.read(new File("rocket.png"));
		rocket1 = ImageIO.read(new File("rocket1.png"));
		shot1 = ImageIO.read(new File("shot.png"));

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		jframe.setTitle("Spaceship v/s Alien Ships");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);	
		jframe.setResizable(false);
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true);

		ship = new Rectangle(WIDTH / 2 - 30, HEIGHT - 100, tileSize, tileSize);
		enemies = new ArrayList<Rectangle>();
		shots = new ArrayList<Rectangle>();
		lives=new ArrayList<Rectangle>();
	}

	public int getXforCenteredText(Graphics g, String text) {
		int length = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
		int x = WIDTH / 2 - length / 2;
		return x;
	}

	public void addEnemy() {
		int width = 50;
		int height = 50; // 50 x 50 box is enemy
		int xPos = 5 + rand.nextInt(WIDTH - 100);
		int yPos = rand.nextInt(HEIGHT / 3);

		enemies.add(new Rectangle(xPos, yPos, width, height));
	}

	public void paintEnemy(Graphics g, Rectangle enemy) {
		g.drawImage(rocket, enemy.x, enemy.y, tileSize, tileSize, renderer);
	}

	public void addShot() {
		int xPos = ship.x;
		int YPos = ship.y;

		shots.add(new Rectangle(xPos, YPos, tileSize, tileSize));
	}

	public void paintShot(Graphics g, Rectangle shot) {
		//g.setColor(Color.ORANGE);
		g.drawImage(shot1, shot.x, shot.y, tileSize, tileSize, renderer);
	}

	public void addLife(){
		int width = 50;
		int height = 50; 
		int xPos = 5 + rand.nextInt(WIDTH - 100);
		int yPos = rand.nextInt(HEIGHT / 3);

		lives.add(new Rectangle(xPos,yPos,width,height));
	}

	public void paintLife(Graphics g, Rectangle newLife){
		g.drawImage(heart, newLife.x, newLife.y, tileSize, tileSize, renderer);
	}

	public void repaint(Graphics g) {
		g.drawImage(bg, 0, 0, renderer); // set the backgroung of frame

		g.drawImage(rocket1, ship.x, ship.y, ship.width, ship.height, renderer);

		for (Rectangle enemy : enemies) {
			paintEnemy(g, enemy);
		}

		for (Rectangle shot : shots) {
			paintShot(g, shot);
		}

		for(Rectangle newLife : lives){
			paintLife(g,newLife);
		}

		if (gameState == titleScreen) {
			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			String text = "SpaceShip X War";
			g.setFont(new Font("prStart", 1 | 2, (tileSize * 2) - 20));
			int x = getXforCenteredText(g, text);
			int y = tileSize * 3;

			// text Shadow
			g.setColor(Color.gray);
			g.drawString(text, x -65, y);
			// textcolor
			g.setColor(Color.white);
			g.drawString(text, x-60, y);

			// display Ship
			x = text.length() * (tileSize-5);
			y = tileSize  + 35;
			g.drawImage(rocket1, x, y, tileSize * 2, tileSize * 2, renderer);

			g.setFont(new Font("prStart", 1, (tileSize) - 20));

			text = "LAUNCH GAME";
			x = getXforCenteredText(g, text);
			y += tileSize * 3.5;
			g.drawString(text, x, y);

			if (commandNum == 0) {
				g.drawString(">>", x - tileSize, y);
			}

			text = "Quit";
			x = getXforCenteredText(g, text);
			y += tileSize;
			g.drawString(text, x, y);
			if (commandNum == 1) {
				g.drawString(">>", x - tileSize, y);
			}
		}

		if(gameState == gameLevels){
			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			String text = "SpaceShip X War";
			g.setFont(new Font("prStart", 1 | 2, (tileSize * 2) - 20));

			int x = getXforCenteredText(g, text);
			int y = tileSize * 3;

			// text Shadow
			g.setColor(Color.gray);
			g.drawString(text, x -65, y);
			// textcolor
			g.setColor(Color.white);
			g.drawString(text, x-60, y);

			// display Ship
			x = text.length() * (tileSize-5);
			y = tileSize  + 35;
			g.drawImage(rocket1, x, y, tileSize * 2, tileSize * 2, renderer);

			g.setFont(new Font("prStart", 1, (tileSize) - 20));

			text = "EASY";
			x = getXforCenteredText(g, text);
			y += tileSize * 3.5;
			g.drawString(text, x, y);

			if (commandNum == 0) {
				g.drawString(">>", x - tileSize, y);
			}

			text = "MEDIUM";
			x = getXforCenteredText(g, text);
			y += tileSize;
			g.drawString(text, x, y);
			if (commandNum == 1) {
				g.drawString(">>", x - tileSize, y);
			}

			text = "HARD";
			x = getXforCenteredText(g, text);
			y += tileSize;
			g.drawString(text, x, y);
			if (commandNum == 2) {
				g.drawString(">>", x - tileSize, y);
			}
		}

		if (gameState == gameOver) {
			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			String text = "GAME OVER!!!";
			g.setFont(new Font("prStart", 1 | 2, (tileSize * 2) - 20));

			int x = getXforCenteredText(g, text);
			int y = tileSize * 3;

			g.setColor(Color.gray); // for shadow
			g.drawString(text, x + 5, y);
			g.setColor(Color.white);
			g.drawString(text, x, y);

			// printing score
			text = "YOUR SCORE = ";
			x = WIDTH / 2 - tileSize * 4;
			y += tileSize * 2;
			g.setFont(new Font("prStart", 1, (tileSize) - 20));
			g.drawString(text, x, y);
			text = String.valueOf(score);
			g.drawString(text, x + 5 * tileSize, y);

			x = WIDTH / 2 - tileSize;
			y += tileSize * 2;
			g.setFont(new Font("prStart", 1, (tileSize) - 20));
			text = "NEW GAME";
			x = getXforCenteredText(g, text);
			y += tileSize * 2;
			g.drawString(text, x, y);

			if (commandNum == 0) {
				g.drawString(">>", x - tileSize, y);
			}

			text = "Quit";
			x = getXforCenteredText(g, text);
			y += tileSize;
			g.drawString(text, x, y);
			if (commandNum == 1) {
				g.drawString(">>", x - tileSize, y);
			}
		}

		if ((gameState != gameOver) && (gameState == started)) {
			g.setColor(Color.gray);
			g.setFont(new Font("prStart", 1 | 2, tileSize));
			g.drawString(String.valueOf(score), tileSize, tileSize * 2);
			g.setColor(Color.white);
			g.setFont(new Font("prStart", 1 | 2, tileSize));
			g.drawString(String.valueOf(score), tileSize + 5, tileSize * 2);
			for (int i = life; i != 0; i--) {
				g.drawImage(heart, tileSize * i + 400, tileSize, tileSize, tileSize, renderer);
			}

		}
	}
}

//class to handle all the actions which extends"PaintFrame" class
class ActionsHandling extends PaintFrame implements ActionListener, KeyListener{

	public ActionsHandling() throws IOException{
		super();

		jframe.addKeyListener(this);

		for (int i = 1; i <= 10; i++) {
			addEnemy();
		}
	}

	Sound sound = new Sound();
	
	public void moveRight() {
		if (gameState == gameOver) {

			ship = new Rectangle(WIDTH / 2 - 30, HEIGHT - 100, 60, 60);
			xMotion = 0;
		}

		if ((gameState != started)) {
			gameState = started;
		} else if (gameState != gameOver) {
			if (xMotion < 0) {
				xMotion = 0;
			}

			xMotion += 5;
		}
	}

	public void moveLeft() {
		if (gameState == gameOver) {

			ship = new Rectangle(WIDTH / 2 + 30, HEIGHT - 100, 60, 60);
			xMotion = 0;
		}

		if (gameState != started) {
			gameState = started;
		} else if (gameState != gameOver) {
			if (xMotion > 0) {
				xMotion = 0;
			}

			xMotion -= 5;
		}
	}

	public void releaseShot() {
		shotMotion = 5;
	}

	public void resetGame(){
		enemies.clear();
		shots.clear();
		lives.clear();
		addlife=true;
		score = 0;
		life = 5;

		for (int i = 1; i <= 10; i++) {
			addEnemy();
		}

		gameState = gameLevels;
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if(gameState == gameLevels){
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				commandNum--;
				if (commandNum < 0) {
					commandNum = 2;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				commandNum++;
				if (commandNum > 2) {
					commandNum = 0;
				}

			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if (commandNum == 0) {
					level=1;
					commandNum=0;
					gameState = started;
				} else if (commandNum == 1) {
					level=2;
					commandNum=0;
					gameState = started;
				} else if(commandNum == 2){
					level=3;
					commandNum=0;
					gameState = started;
				}
			}
		}

		if (gameState == titleScreen) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				commandNum--;
				if (commandNum < 0) {
					commandNum = 1;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				commandNum++;
				if (commandNum > 1) {
					commandNum = 0;
				}

			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (commandNum == 0) {
					gameState = gameLevels;
				} else if (commandNum == 1) {
					System.exit(0);
				}
			}
		}

		if (gameState == started) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				moveRight();
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				moveLeft();
			}

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				addShot();
				releaseShot();
			}
		}
		if (gameState == gameOver) {

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				commandNum--;
				if (commandNum < 0) {
					commandNum = 1;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				commandNum++;
				if (commandNum > 1) {
					commandNum = 0;
				}

			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (commandNum == 0) {

					resetGame();
					
				} else if (commandNum == 1) {
					System.exit(0);
				}

			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (gameState == started) {

			// for enemy ships going down
			for (int i = 0; i < enemies.size(); i++) {
				Rectangle enemy = enemies.get(i);
				rand = new Random();

				//int speed = rand.nextInt(0 | 8);
				//enemy.y += speed;

				int speed=6;

				if(level == 1){
					speed=6;
					if(score>50){
						level=2;
						addEnemy();
						addEnemy();
					}
				}else if(level == 2){
					speed = 10;
					if(score>100){
						level=3;
						addEnemy();
						addEnemy();
					}
				} else if(level == 3){
					speed = 14;
				}

				
				enemy.y += speed;	
			}

			// for shots going up
			for (int i = 0; i < shots.size(); i++) {
				Rectangle shot = shots.get(i);
				shot.y -= shotMotion;
			}

			// removes enemy when they go out of frame
			for (int i = 0; i < enemies.size(); i++) {
				Rectangle enemy = enemies.get(i);

				if (enemy.y > HEIGHT) {

					enemies.remove(i);
					addEnemy(); // as well as one new enemy is added
				}
			}

			// removes shots when they go out of frame
			for (int i = 0; i < shots.size(); i++) {
				Rectangle shot = shots.get(i);

				if (shot.y < 0) {
					shots.remove(shot);
				}
			}

			ship.x += xMotion;

			if (ship.x + ship.width > WIDTH) {
				ship.x = WIDTH - ship.width - 20;
				xMotion = 0;
			}

			if (ship.x < 0) {
				ship.x = 5;
				xMotion = 0;
			}

			// check whether enemy and shot are being intersected
			for (int i = 0; i < enemies.size(); i++) {
				Rectangle enemy = enemies.get(i);

				for (Rectangle shot : shots) {
					if (enemy.intersects(shot)) {

						enemies.remove(i--);
						sound.setFile(0);
						sound.play();
						shots.remove(shot);

						score += 1; // if YES SCORE+=1

						addEnemy();

						break;
					}
				}
			}

			// check whether enemy and spaceship are being intersected
			for (int i = 0; i < enemies.size(); i++) {
				Rectangle enemy = enemies.get(i);

				if (enemy.intersects(ship)) {
					enemies.remove(i--);
					life--; // if YES LIFE--

					if(life==2){
						addlife=true;
					}

					if (life == 0) {
						sound.setFile(1);
						sound.play();
					} else {
						sound.setFile(2);
						sound.play();
					}
					addEnemy();
				}
			}

			// for new life coming down
			for(int i=0;i<lives.size();i++){
				Rectangle newLife=lives.get(i);

				newLife.y+=5;
			}

			// if ship intersects newLife than life++
			for(int i=0;i<lives.size();i++){
				Rectangle newLife=lives.get(i);

				if(ship.intersects(newLife)){
					addlife=false;
					life++;
					lives.remove(i);
					i--;
				}
			}

			for (int i = 0; i < lives.size(); i++) {
				Rectangle newLife = lives.get(i);

				if (newLife.y > HEIGHT) {

					lives.remove(i);
					addLife();
				}
			}

			// if life<=2 newLife will be given
			if(life==2 && addlife==true){
				addlife=false;
				addLife();
			}

			if (life <= 0) {
				gameState = gameOver;
				xMotion = 0;
				enemies.clear();
				shots.clear();
			}
		}

		renderer.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}

// class containing "main" method
public class SpaceShip extends ActionsHandling{

	public static SpaceShip spaceship;

	public SpaceShip() throws IOException {

		super();

		Timer timer = new Timer(40, this);
		timer.start();
	}

	public static void main(String[] args) throws IOException {
		spaceship = new SpaceShip();
	}
}