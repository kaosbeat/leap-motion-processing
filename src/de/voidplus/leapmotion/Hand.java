package de.voidplus.leapmotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;


public class Hand implements PConstants {
	

	private PApplet parent;
	private com.leapmotion.leap.Hand hand;
	private LeapMotion leap;
	private ArrayList<Finger> fingers;
	private ArrayList<Tool> tools;
	
	
	public Hand(PApplet parent, LeapMotion leap, com.leapmotion.leap.Hand hand){
		this.parent = parent;
		this.leap = leap;
		this.hand = hand;
		this.fingers = new ArrayList<Finger>();
		this.tools = new ArrayList<Tool>();
	}

	
	/* ------------------------------------------------------------------------ */
	/* HAND */
	
	/**
	 * Get the unique ID.
	 * @return
	 */
	public int getId(){
		return this.hand.id();
	}
	
	/**
	 * The center position of the palm in millimeters.
	 * @return
	 */
	public PVector getPosition(){
		return this.leap.map( this.hand.palmPosition() );
	}
	
	/**
	 * The center position of the palm in millimeters.
	 * @return
	 */
	public PVector getPalmPosition(){
		return this.getPosition();
	}
	
	/**
	 * The stabilized center position of the palm in millimeters.
	 * @return
	 */
	public PVector getStabilizedPosition(){
		return this.leap.map( this.hand.stabilizedPalmPosition() );
	}
	
	/**
	 * The stabilized center position of the palm in millimeters.
	 * @return
	 */
	public PVector getStabilizedPalmPosition(){
		return this.getStabilizedPosition();
	}
	
	/**
	 * The direction from the palm position toward the fingers.
	 * @return
	 */
	public PVector getDirection(){
		return this.leap.map(this.hand.direction());
	}
	
	/**
	 * The duration of time this Hand has been visible to the Leap Motion Controller.
	 * @return
	 */
	public float getTimeVisible(){
		return this.hand.timeVisible();
	}
	
	/**
	 * Draw the hand with all details.
	 * @param radius	The radius of the ellipse (2D) or sphere (3D).
	 */
	public void draw(float radius){
		PVector position = this.getPosition();
		if(this.parent.g.is2D()){
			this.parent.ellipseMode(PConstants.CENTER);
			this.parent.ellipse(position.x, position.y, radius, radius);
		} else {
			this.parent.pushMatrix();
				this.parent.translate(position.x, position.y, position.z);
				this.parent.sphereDetail(20);
				this.parent.sphere(5);
			this.parent.popMatrix();
		}
	}
	public void draw(){
		this.draw(5);
	}

	
	/* ------------------------------------------------------------------------ */
	/* FLIGHT-DYNAMICS */
	
	/**
	 * Get the angles of the hand (x=roll, y=pich, z=yaw).
	 * @return
	 */
	public PVector getDynamics(){
		return new PVector(this.getRoll(), this.getPitch(), this.getYaw());
	}
	
	/**
	 * Get the roll angle (x).
	 * @return
	 */
	public float getRoll(){
		return this.parent.degrees(this.hand.direction().pitch());
	}
	
	/**
	 * Get the pitch angle (y).
	 * @return
	 */
	public float getPitch(){
		return -this.parent.degrees(this.hand.palmNormal().roll());
	}
	
	/**
	 * Get the yaw angle (z).
	 * @return
	 */
	public float getYaw(){
		return this.parent.degrees(this.hand.direction().yaw());
	}
	
	
	/* ------------------------------------------------------------------------ */
	/* SPHERE */
	
	/**
	 * The center of a sphere fit to the curvature of this hand. 
	 * @return
	 */
	public PVector getSpherePosition(){
		return this.leap.map( this.hand.sphereCenter() );
	}
	
	/**
	 * The radius of a sphere fit to the curvature of this hand. 
	 * @return
	 */
	public float getSphereRadius(){
		return this.hand.sphereRadius();
	}
	

	/* ------------------------------------------------------------------------ */
	/* FINGERS */
	
	/**
	 * Get all detected fingers of the hand.
	 * @return
	 */
	public ArrayList<Finger> getFingers(){
		fingers.clear();
		if(this.hasFingers()){
			for(com.leapmotion.leap.Finger finger : this.hand.fingers()){
				this.fingers.add(new Finger(this.parent, this.leap, finger));
		    }
		}
		return fingers;
	}
	
	/**
	 * Check if there is any finger.
	 * @return
	 */
	public boolean hasFingers(){
		if(this.leap.isConnected()){
			return !this.hand.fingers().empty();
		}
		return false;
	}
	
	/**
	 * Get the number of fingers.
	 * @return
	 */
	public int countFingers(){
		if(this.leap.isConnected()){
			return this.hand.fingers().count();
		}
		return 0;
	}

	
	/* ------------------------------------------------------------------------ */
	/* TOOLS */
	
	/**
	 * Get all detected tools of the hand.
	 * @return
	 */
	public ArrayList<Tool> getTools(){
		tools.clear();
		if(this.hasTools()){
			for(com.leapmotion.leap.Tool tool : this.hand.tools()){
				tools.add(new Tool(this.parent, this.leap, tool));
		    }
		}
		return tools;
	}
	
	/**
	 * Check if there is any tool.
	 * @return
	 */
	public boolean hasTools(){
		if(this.leap.isConnected()){
			return !this.hand.tools().empty();
		}
		return false;
	}
	
	/**
	 * Get the number of tools.
	 * @return
	 */
	public int countTools(){
		if(this.leap.isConnected()){
			return this.hand.tools().count();
		}
		return 0;
	}
	
}