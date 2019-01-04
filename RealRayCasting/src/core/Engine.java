package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Engine extends javax.swing.JFrame implements Runnable{
	
	private static boolean gameRunning = true;
	
	public static InputHandler inputHandler;

	public static Player player;
	private GamePanel gamePanel;
	private GridPanel gridPanel;


	
	private static int[][] map = {
			
			{1,1,1,1,1,1,1,1,1,1},
			{0,0,0,0,0,0,0,0,1,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,1,1,0,0,0,1,0},
			{0,0,0,1,1,1,0,0,0,0},
			{0,0,0,0,0,1,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,0,1,1,0},
			{0,0,0,0,0,0,0,0,0,0},
			{1,1,1,1,1,1,1,1,1,1},
			
	};
	
	
	public static int getCellCollision(Point cellPoint) {
		
		//Rows are y's
		//Columns are x's
		return map[cellPoint.y][cellPoint.x];
	}
	
	public Engine(String title) {
		super(title);
		initComponents();
		setLocationRelativeTo(null);
	}
	
	
	public void initComponents() {
		setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
		inputHandler = new InputHandler(this);

		gamePanel = new GamePanel();
		gamePanel.setLayout(null);
		add(gamePanel);
		
		gridPanel = new GridPanel();
		gridPanel.setLocation(gamePanel.WIDTH - gridPanel.WIDTH, 0);
		gamePanel.add(gridPanel);
		
		
		player = new Player(new java.awt.Point(0, 0));
		
		
		pack();
	}
	
	public void run() {
		
		final int PREFERRED_FPS = 140;
		
		double ns = 1000000000 / PREFERRED_FPS;
		double deltaTime = 0;
		
		long last = System.nanoTime(), now;
		long fpsTimer = System.currentTimeMillis();
		int fpsCounter = 0;
		
		while (gameRunning) {
			
			now = System.nanoTime();
			deltaTime += (now - last)/ns;
			last = now;
			
			while (deltaTime >= 1) {
				deltaTime--;
				gridPanel.update();
				player.update();
			}
			
			gridPanel.repaint();
			
			fpsCounter++;
			
			if ((System.currentTimeMillis() - fpsTimer) >= 1000) {
				fpsTimer += 1000;
				//System.out.println(fpsCounter);
				fpsCounter = 0;
			}
			
		}
		
	}

	public class GamePanel extends javax.swing.JPanel {
		
		public static final int WIDTH = 300;
		public static final int HEIGHT = 300;
		
		public GamePanel() {
			setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
		}
	}
	
	public class GridPanel extends javax.swing.JPanel {
		
		public static final int gap = 30;
		
		private int WIDTH = gap * map.length;
		private final int HEIGHT = gap * map.length;
		
		public GridPanel() {
			setSize(new java.awt.Dimension(WIDTH, HEIGHT));
			setBackground(Color.BLACK);
		}
		
		public void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			
			//column
			g.setColor(java.awt.Color.WHITE);
			for (int i=1; i<=map.length; i++) {
				g.drawLine(gap*i, 0, gap*i, HEIGHT);
			}
			
			//rows
			for(int i=1; i<=map.length; i++) {
				g.drawLine(0, gap*i, WIDTH, gap*i);
			}
			
			//coloring
			g.setColor(Color.RED);
			for (int y=0; y<map.length; y++) {
				for (int x=0; x<map[0].length; x++) {
					if (map[y][x] == 1) {
						g.drawRect(x*gap, y*gap, gap, gap);
					}
				}
			}
			
			
			player.draw(g);
			
		}
		
		
		public void update() {
			
		}
		

		
	}
	
	
	
	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				Engine game = new Engine("Hello world");
				game.setVisible(true);
				Thread gameThread = new Thread(game);
				gameThread.start();
			}
			
		});
		
		
	}
	
}