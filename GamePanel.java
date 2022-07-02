import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 1300;
	static final int SCREEN_HEIGHT = 750;
	static final int UNIT_SIZE = 50;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	
	int characterX;
	int characterY;
	static int gold;
	char direction = 0;
	static int attackDamage = 5;

	int enemyX;
	int enemyY;
	int enemyHP;
	int numberOfEnemies=5;

	int merchantX;
	int merchantY;
	int knivesQty;
	int swordsQty;
	int talisman = 1;

	Random random;
	String s;
	boolean chance;
	boolean fightState=true;


	ArrayList<Integer> enemiesX = new ArrayList<Integer>();
	ArrayList<Integer> enemiesY = new ArrayList<Integer>();
	ArrayList<Integer> enemiesHP = new ArrayList<Integer>();
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newPlayer();
		newMerchant();
		drawEnemies();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
	
		if(fightState==true&&numberOfEnemies>0){
			for(int x = 0;x<enemiesX.size();x++){

				g.setColor(Color.red);
				g.fillOval(enemiesX.get(x), enemiesY.get(x), UNIT_SIZE, UNIT_SIZE);

				s = String.valueOf(enemiesHP.get(x));

				g.setColor(Color.pink);
				g.setFont( new Font("Ink Free",Font.BOLD, UNIT_SIZE));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString(s, (enemiesX.get(x) - metrics.stringWidth(s))+UNIT_SIZE-9, enemiesY.get(x)+UNIT_SIZE-6);
			}
			//g.fillOval(enemyX, enemyY, UNIT_SIZE, UNIT_SIZE);

			g.setColor(Color.green);
			g.fillRect(characterX, characterY, UNIT_SIZE, UNIT_SIZE);

			g.setColor(Color.YELLOW);
			g.fillOval(merchantX, merchantY, UNIT_SIZE, UNIT_SIZE);

			

			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("Gold: "+gold+ " AD: "+attackDamage, (SCREEN_WIDTH - metrics2.stringWidth("Gold: "+gold+ " AD: "+attackDamage))/2, g.getFont().getSize());

		
		}
		else if(fightState==true&&numberOfEnemies==0){
			gameWon(g);
		}
		else{
			gameOver(g);
		}
	}

		public void newEnemy(){
			enemyHP = random.nextInt(0,9);
			enemiesHP.add(enemyHP);
			//s = String.valueOf(enemyHP);
			enemyX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
			enemiesX.add(enemyX);
			enemyY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
			enemiesY.add(enemyY);

		}

		public void deleteEnemy(int pos){
			enemiesHP.remove(pos);
			enemiesX.remove(pos);
			enemiesY.remove(pos);
			numberOfEnemies--;
			repaint();
		}

		public void newPlayer(){
			characterX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
			characterY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		}

		public void newMerchant(){
			merchantX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
			merchantY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
			knivesQty = random.nextInt(1,5);
			swordsQty = random.nextInt(1,5);
		}

		public void checkCollisions() {
			//check if character touches the enemy
			for(int i=0;i<numberOfEnemies;i++){
				if(characterX==enemiesX.get(i) && characterY==enemiesY.get(i)){
					switch(direction){
					case 'U':
						characterY+=UNIT_SIZE;
						repaint();
						break;
					case 'D':
						characterY-=UNIT_SIZE;
						repaint();
						break;
					case 'L':
						characterX+=UNIT_SIZE;
						repaint();
						break;
					case 'R':
						characterX-=UNIT_SIZE;
						repaint();
						break;
					}
				}
			
			}
			//check if character touches the merchant
			if(characterX==merchantX && characterY==merchantY){
				switch(direction){
				case 'U':
					characterY+=UNIT_SIZE;
					repaint();
					break;
				case 'D':
					characterY-=UNIT_SIZE;
					repaint();
					break;
				case 'L':
					characterX+=UNIT_SIZE;
					repaint();
					break;
				case 'R':
					characterX-=UNIT_SIZE;
					repaint();
					break;
				}
			}
		}

		public int getAD(){
			return attackDamage;
		}

		public void attack(){
			for(int i=0;i<numberOfEnemies;i++){
				if(((characterX == enemiesX.get(i) + UNIT_SIZE || characterX == enemiesX.get(i) - UNIT_SIZE)&&(characterY == enemiesY.get(i)))||((characterY == enemiesY.get(i) - UNIT_SIZE || characterY == enemiesY.get(i)+UNIT_SIZE)&&(characterX == enemiesX.get(i)))){
					if(attackDamage>=enemiesHP.get(i)*2){
						gold++;
						deleteEnemy(i);
					}
					else if(attackDamage>=enemiesHP.get(i)){
						if(chance = new Random().nextInt(4)!=3){
							gold++;
							deleteEnemy(i);
						}
						else{
							fightState = false;
						}
					}
					else{
						if(attackDamage<enemiesHP.get(i)){
							if(chance = new Random().nextInt(4)<=1){
								gold++;
								deleteEnemy(i);
							}

							
							else{
								fightState=false;
							}
						}
					}
				}
			}
		}

		public void drawEnemies(){
			for(int x = 0;x<numberOfEnemies;x++){
				newEnemy();
			}
			repaint();
		}
/* 
		public static void updateStats(){

			if(MerchantFrame.getGoldState()<=gold){

				gold=gold+MerchantFrame.getGoldState();

				attackDamage=attackDamage+MerchantFrame.getAdState();
			}
			else{
				attackDamage=attackDamage+(MerchantFrame.getAdState()+gold);
				gold=0;
			}
				
			MerchantFrame.setGoldState(0);
			MerchantFrame.setAdState(0);

				
		}
        */

		public void gameOver(Graphics g) {
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics1 = getFontMetrics(g.getFont());
			g.drawString("Score: "+attackDamage, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+attackDamage))/2, g.getFont().getSize());
			//Game Over text
			if(numberOfEnemies>0){
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 75));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
			}
			}
		

		public void gameWon(Graphics g){

			//Game Won text
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 75));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("You won! ", (SCREEN_WIDTH - metrics2.stringWidth("You won! "))/2, SCREEN_HEIGHT/2);
		}


		@Override
	public void actionPerformed(ActionEvent e) {
		checkCollisions();
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				direction = 'L';
				if(characterX > 0){
					characterX-=UNIT_SIZE;
					checkCollisions();
					repaint();
				}
				break;
			case KeyEvent.VK_RIGHT:
				direction = 'R';
				if(characterX<SCREEN_WIDTH-UNIT_SIZE){
					characterX+=UNIT_SIZE;
					checkCollisions();
					repaint();
				}
				break;
			case KeyEvent.VK_UP:
				direction = 'U';
				if(characterY>0){
					characterY-=UNIT_SIZE;
					checkCollisions();
					repaint();
				}
				break;
			case KeyEvent.VK_DOWN:
				direction = 'D';
				if(characterY<SCREEN_HEIGHT-UNIT_SIZE){
					characterY+=UNIT_SIZE;
					checkCollisions();
					repaint();
				}
				break;
			case KeyEvent.VK_SPACE:
				for(int i=0;i<numberOfEnemies;i++){
				if(((characterX == enemiesX.get(i) + UNIT_SIZE || characterX == enemiesX.get(i) - UNIT_SIZE)&&(characterY == enemiesY.get(i)))||((characterY == enemiesY.get(i) - UNIT_SIZE || characterY == enemiesY.get(i)+UNIT_SIZE)&&(characterX == enemiesX.get(i)))){
					attack();
					checkCollisions();
					repaint();
					}
				}
				if(((characterX == merchantX + UNIT_SIZE || characterX == merchantX - UNIT_SIZE)&&(characterY == merchantY))||((characterY == merchantY - UNIT_SIZE || characterY == merchantY+UNIT_SIZE)&&(characterX == merchantX))){
					//new MerchantFrame();
				}
				break;
			}
			//updateStats();
			repaint();

		}
		
	}
	}

/* Action plan:


*/
