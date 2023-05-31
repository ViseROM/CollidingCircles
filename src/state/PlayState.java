package state;

import java.awt.Graphics2D;

import main.GamePanel;

import java.awt.Color;
import java.util.ArrayList;

import entity.Particle;

/**
 * The PlayState class
 * @author Vachia Thoj
 *
 */
public class PlayState extends State
{
	//Array list to store many Particles
	private ArrayList<Particle> particles;
	
	//Maximum number of Circle objects
	private static final int MAX_CIRCLES = 60;
	
	/**
	 * Constructor
	 */
	public PlayState()
	{
		//Call init() method
		init();
	}
	
	/**
	 * Method to be used in constructor
	 * Generates/initialized Circle objects
	 */
	private void init()
	{
		//Initialize the particles ArrayList
		particles = new ArrayList<Particle>();
		
		//To keep track of Particle variables
		int radius;
		double x;
		double y;
		
		for(int i = 0; i < MAX_CIRCLES; i++)
		{
			//Generate a random radius, mass, x and y coordinate for a Circle
			radius = (int) generateNumber(20, 10);
			x = generateNumber((GamePanel.WIDTH - (2 * radius)), radius);
			y = generateNumber((GamePanel.HEIGHT - (2 * radius)), radius);
			
			Particle tempParticle = new Particle(x, y, radius);
			
			//Make sure tempCircle is not overlapping with any other Circle
			for(int j = 0; j < particles.size(); j++)
			{
				//If tempCircle is overlapping with another Circle...
				if(particleCollision(tempParticle, particles.get(j)) == true)
				{
					//generate new x and y coordinate for tempCircle
					x = generateNumber((GamePanel.WIDTH - (2 * radius)), radius);
					y = generateNumber((GamePanel.HEIGHT - (2 * radius)), radius);
					tempParticle.setX(x);
					tempParticle.setY(y);
					
					j = -1;
				}
			}
			
			//Add tempCircle to ArrayList of circles
			particles.add(tempParticle);
		}
	}
	
	/**
	 * Method that generates a random number within a certain range
	 * @param max (integer) the highest possible number to randomly generate 
	 * @param min (integer) the lowest possible number to randomly generate
	 * @return (integer) returns a randomly generated number
	 */
	private double generateNumber(int max, int min)
    {
        return ((Math.random() * max) + min);
    }
	
	/**
	 * Method that calculates and determines what happens next to the Particles
	 * that have collided
	 * @param particle (Particle) a Particle object
	 * @param otherParticle (Particle) another Particle object
	 */
	private void resolveCollision(Particle particle, Particle otherParticle)
    {
		//Difference in x and y velocity of the two Particles
        double xVelocityDiff = particle.getDx() - otherParticle.getDx();
        double yVelocityDiff = particle.getDy() - otherParticle.getDy();
        
        //x and y distance between the two Particles
        double xDist = otherParticle.getX() - particle.getX();
        double yDist = otherParticle.getY() - particle.getY();
        
        //Prevent accidental overlap of particles
        if(xVelocityDiff * xDist + yVelocityDiff * yDist >= 0)
        {
            //Grab angle between the two colliding particles
            double angle = -Math.atan2(otherParticle.getY() - particle.getY(), otherParticle.getX() - particle.getX());
            
            //Get mass of particles
            double m1 = particle.getMass();
            double m2 = otherParticle.getMass();
            
            //Calculate x and y coordinate rotation of Particles
            double u1X = rotateX(particle.getDx(), particle.getDy(), angle);
            double u1Y = rotateY(particle.getDx(), particle.getDy(), angle);
            double u2X = rotateX(otherParticle.getDx(), otherParticle.getDy(), angle);
            double u2Y = rotateY(otherParticle.getDx(), otherParticle.getDy(), angle);
            
            double v1X = u1X * (m1 - m2) / (m1 + m2) + u2X * 2 * m2 / (m1 + m2);
            double v1Y = u1Y;
            double v2X = u2X * (m1 - m2) / (m1 + m2) + u1X * 2 * m2 / (m1 + m2);
            double v2Y = u2Y;
            
            double vparticleFinalx = rotateX(v1X, v1Y, -angle);
            double vparticleFinaly = rotateY(v1X, v1Y, -angle);
            double vOtherParticleFinalx = rotateX(v2X, v2Y, -angle);
            double vOtherParticleFinaly = rotateY(v2X, v2Y, -angle);
            
            particle.setDx(vparticleFinalx);
            particle.setDy(vparticleFinaly);
            otherParticle.setDx(vOtherParticleFinalx);
            otherParticle.setDy(vOtherParticleFinaly);
        }
    }
	
	/**
	 * Method that determines if two Particles have collided
	 * @param p1 (Particle) a Particle Object
	 * @param p2 (Particle) another Particle Object
	 * @return returns true if Particle p1 and p2 have collided otherwise false
	 */
	private boolean particleCollision(Particle p1, Particle p2)
	{
		double dx = p1.getX() - p2.getX();
		double dy = p1.getY() - p2.getY();
		double distance = Math.sqrt((dx * dx) + (dy * dy));
		if(distance < p1.getRadius() + p2.getRadius())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method that "rotates" the x position of a Particle
	 * @param dx (double) change in x position of a Particle
	 * @param dy (double) change in y position of a Particle
	 * @param angle (double) the angle between two Particles
	 * @return returns a rotated x position
	 */
	private double rotateX(double dx, double dy, double angle)
    {
        double rotatedVelocity = dx * Math.cos(angle) - dy * Math.sin(angle);
        return rotatedVelocity;
    }
    
	/**
	 * Method that "rotates" the y position of a Particle
	 * @param dx (double) change in x position of a Particle
	 * @param dy (double) change in y position of a Particle
	 * @param angle (double) the angle between two Particles
	 * @return returns a rotated y position
	 */
    private double rotateY(double dx, double dy, double angle)
    {
        double rotatedVelocity = dx * Math.sin(angle) + dy * Math.cos(angle);
        return rotatedVelocity;
    }
	
    /**
     * Method that updates the PlayState
     */
	public void update()
	{
		//Update particles
		for(int i = 0; i < particles.size(); i++)
		{
			particles.get(i).update();
		}
		
		//Loop to see if Particles have collided
		for(int i = 0; i < particles.size(); i++)
		{
			Particle p1 = particles.get(i);
			for(int j = 0; j < particles.size(); j++)
			{
				if(i == j)
				{
					continue;
				}
				
				Particle p2 = particles.get(j);
				
				//if p1 and p2 have collided...
				if(particleCollision(p1, p2) == true)
				{
					p1.setTouchingParticle(true);
					p2.setTouchingParticle(true);
					
					//resolve collision for p1 and p2
					resolveCollision(p1, p2);
					
					break;
				}
			}
		}
	}
	
	/**
	 * Method that draws the PlayState
	 * @param g the Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		//Draw background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		//Draw particles
		for(int i = 0; i < particles.size(); i++)
		{
			particles.get(i).draw(g);
		}
	}
}
