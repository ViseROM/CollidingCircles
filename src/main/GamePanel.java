package main;

import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.*;
import java.awt.Dimension;
import java.awt.Graphics;

import manager.StateManager;

/**
 * GamePanel class is the panel (screen) that will be drawn on
 * @author Vachia Thoj
 *
 */
public class GamePanel extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	//width and height of gamePanel
	public static final int WIDTH = 900;
	public static final int HEIGHT = 700;
	
	//Thread to run the game
	private Thread thread;
	private boolean running;
	
	//To render graphics
	private BufferedImage image;
	private Graphics2D g;
	
	//Game framerate
	private static final int FPS = 120;
	private static final int TARGET_TIME = 1000 / FPS;
	
	//To manage different states
	private StateManager stateManager;
	
	public GamePanel()
	{
		super();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setFocusable(true);
		this.requestFocus();
		
		stateManager = StateManager.instance();
	}
	
	public void addNotify()
	{
		super.addNotify();
		
		if(thread == null)
		{
			//Create thread and start thread
			thread = new Thread(this);
			thread.start();
		}
	}
	
	/**
	 * Method used to run the thread
	 */
	public void run()
	{
		running = true;
        
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        
        //Add anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        long start;
        long elapsed;
        long wait;
        
        //Events to do while thread is running
        while(running == true)
        {
        	start = System.nanoTime();
        	
            update();
            draw();
            drawToScreen();
            
            //******* (START) Frame counting *******
            
            elapsed = (System.nanoTime() - start);
            wait = TARGET_TIME - (elapsed / 1000000);
            
            if(wait <= 0)
            {
            	wait = 5;
            }
            
            try{
                Thread.sleep(wait);
            }catch(Exception e){
                e.printStackTrace();
            }
            //******* (END) Frame counting *******
        }
	}
	
	/**
	 * Method to update screen
	 */
	private void update()
	{
		stateManager.update();
	}
	
	/**
	 * Method to draw on screen
	 */
	private void draw()
	{
		stateManager.draw(g);
	}
	
	/**
	 * Method for double buffering
	 */
	private void drawToScreen()
	{
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
}