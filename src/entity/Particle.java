package entity;

/**
 * Class that represents a Particle
 * @author Vachia Thoj
 *
 */
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

import main.GamePanel;



public class Particle 
{
	//x and y coordinate of Particle
	private double x;
	private double y;
	
	//change in x and y coordinate of Particle
	private double dx;
	private double dy;
	
	//radius of Particle
	private int radius;
	
	//direction of Particle
	private double radians;
	
	//mass of Particle
	private int mass;
	
	//speed of Particle
	private int speed;
	
	//color of particle
	private Color color;
	
	//used to aid in changing Particle color
	private int colorCount;
	
	//flag to see if Particle is touching another Particle
	private boolean touchingParticle;
	
	private static final int MIN_MASS = 1;
	private static final int MAX_MASS = 3;
	private static final int DEFAULT_SPEED = 2;
	private static final Color DEFAULT_COLOR = Color.RED;
	
	/**
	 * Constructor
	 * @param x (double) x coordinate of Particle 
	 * @param y (double) y coordinate of Particle
	 * @param radius (integer) radius of the Particle 
	 */
	public Particle(double x, double y, int radius)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
		
		this.speed = DEFAULT_SPEED;
		this.mass = MIN_MASS;
		
		double angle = (Math.random() * 140) + 20;
		this.radians = Math.toRadians(angle);
		
		this.dx = Math.cos(radians) * speed;
		this.dy = Math.sin(radians) * speed;
		
		this.color = DEFAULT_COLOR;
		this.colorCount = 1;
		
		this.touchingParticle = false;
	}
	
	//Getter methods
	public double getX() {return x;}
	public double getY() {return y;}
	public double getDx() {return dx;}
	public double getDy() {return dy;}
	public int getRadius() {return radius;}
	public double getRadians() {return radians;}
	public int getMass() {return mass;}
	public int getSpeed() {return speed;}
	public Color getColor() {return color;}
	public boolean isTouchingParticle() {return touchingParticle;}
	
	//Setter methods
	public void setX(double x) {this.x = x;}
	public void setY(double y) {this.y = y;}
	public void setDx(double dx) {this.dx = dx;}
	public void setDy(double dy) {this.dy = dy;}
	public void setRadius(int radius) {this.radius = radius;}
	public void setMass(int mass) {this.mass = mass;}
	public void setSpeed(int speed) {this.speed = speed;}
	public void setColor(Color color) {this.color = color;}
	public void setTouchingParticle(boolean b) {this.touchingParticle = b;}
	
	/**
	 * Method that moves the Particle; changing x and y coordinate
	 */
	private void move()
	{
		x += dx;
		y += dy;
	}
	
	/**
	 * Method that checks if Particle has touched a boundary
	 * If Particle is touching a boundary, change direction that Particle is moving in
	 */
	private void checkBoundary()
	{
		//left boundary
		if(x < radius && dx < 0 )
		{
			dx = -dx;
		}
		
		//top boundary
		if(y < radius && dy < 0)
		{
			dy = -dy;
		}
				
		//right boundary
		if(x > (GamePanel.WIDTH - radius)  && dx > 0)
		{
			dx = -dx;
		}
				
		//bottom boundary
		if(y > (GamePanel.HEIGHT - radius) && dy > 0)
		{
			dy = -dy;
		}
	}
	
	/**
	 * Method that changes the color of the Particle
	 */
	private void changeColor()
	{
		switch(colorCount)
		{
			case 1:
				color = Color.RED;
				break;
			case 2:
				color = Color.BLUE;
				break;
			case 3:
				color = Color.GREEN;
				break;
			case 4:
				color = Color.WHITE;
				break;
			default:
				color = DEFAULT_COLOR;
				break;
		}
	}
	
	/**
	 * Method that updates the Particle
	 */
	public void update()
	{
		//move Particle
		move();
		
		//check if Particle has touched a boundary
		checkBoundary();
		
		//Check if touchingParticle flag is true
		if(touchingParticle == true)
		{
			++colorCount;
			touchingParticle = false;
			
			if(colorCount > 4)
			{
				colorCount = 1;
			}
		}
		
		changeColor();
	}
	
	/**
	 * Method that draws the Particle
	 * @param g the Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		//Draw Particle
		g.setColor(color);
		g.setStroke(new BasicStroke(1));
		g.fillOval((int) (x - radius), (int) (y - radius), 2 * radius, 2 * radius);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		g.drawOval((int) (x - radius), (int) (y - radius), 2 * radius, 2 * radius);
	}
}
