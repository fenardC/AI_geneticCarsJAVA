package com.newgameplus.frameworkdemo.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.misc.BezierSpline2D;
import com.newgameplus.framework.misc.Triple;
import com.newgameplus.framework.misc.Vector2D;

public class Track {
	
	protected BezierSpline2D spline;
	
	protected double splineWidth = 60;
	
	protected double maxLapMillis = 0;
	
	//protected List<Quadruple<Vector2D, Vector2D, Vector2D, Vector2D>> listQuad = new ArrayList<Quadruple<Vector2D, Vector2D, Vector2D, Vector2D>>();
	protected List<Triple<Vector2D, Vector2D, Vector2D>> listTriangle = new ArrayList<Triple<Vector2D, Vector2D, Vector2D>>();
	
	public Track(BezierSpline2D spline, double splineWidth, double maxLapMillis) {
		this.spline = spline;
		this.splineWidth = splineWidth;
		this.maxLapMillis = maxLapMillis;
		init();
	}
	
	public void init() {
		
		//listQuad.clear();
		listTriangle.clear();
		
		List<Vector2D> listPoint = spline.getListResultPoint();
    	List<Vector2D> listPerp = spline.getListResultPerpendicular();
		
		Vector2D perp1, perp2, v11, v12, v21, v22;
		
		for (int i = 0 ; i < listPoint.size() ; i++) {
			
			if (i < listPoint.size() - 1 || spline.isClose()) {
				
				if (i == listPoint.size() - 1 && spline.isClose()) {
					perp1 = listPerp.get(i).clone().multiply(splineWidth / 2);
		    		perp2 = listPerp.get(0).clone().multiply(splineWidth / 2);
		    		v11 = Vector2D.add(listPoint.get(i), perp1);
		    		v12 = Vector2D.substract(listPoint.get(i), perp1);
		    		v21 = Vector2D.add(listPoint.get(0), perp2);
		    		v22 = Vector2D.substract(listPoint.get(0), perp2);
				} else {
					perp1 = listPerp.get(i).clone().multiply(splineWidth / 2);
		    		perp2 = listPerp.get(i + 1).clone().multiply(splineWidth / 2);
		    		v11 = Vector2D.add(listPoint.get(i), perp1);
		    		v12 = Vector2D.substract(listPoint.get(i), perp1);
		    		v21 = Vector2D.add(listPoint.get(i + 1), perp2);
		    		v22 = Vector2D.substract(listPoint.get(i + 1), perp2);
				}
				
				//listQuad.add(new Quadruple<Vector2D, Vector2D, Vector2D, Vector2D>(v11.clone(), v12.clone(), v21.clone(), v22.clone()));
				
				listTriangle.add(new Triple<Vector2D, Vector2D, Vector2D>(v11.clone(), v12.clone(), v21.clone()));
				listTriangle.add(new Triple<Vector2D, Vector2D, Vector2D>(v22.clone(), v12.clone(), v21.clone()));
				
				/*d.drawLine(v11.x, v11.y, v12.x, v12.y);
				d.drawLine(v21.x, v21.y, v22.x, v22.y);
				d.drawLine(v11.x, v11.y, v21.x, v21.y);
				d.drawLine(v12.x, v12.y, v22.x, v22.y);*/
				
				/*GL11.glBegin(GL_QUADS);
				
				GL11.glVertex2d(v11.x, v11.y);
				GL11.glVertex2d(v12.x, v12.y);
				GL11.glVertex2d(v22.x, v22.y);
				GL11.glVertex2d(v21.x, v21.y);
				
				GL11.glEnd();*/
				
			}
		}
		
	}
	
	public void tick(double millis) {
		
	}
	
	public void renderDebug(Drawer d) {
		for (int i = 0 ; i < listTriangle.size() ; i++) {
			Triple<Vector2D, Vector2D, Vector2D> tri = listTriangle.get(i);
			
			d.setColor(Color.GREEN);
			
			d.drawLine(tri.a.x, tri.a.y, tri.b.x, tri.b.y);
			d.drawLine(tri.b.x, tri.b.y, tri.c.x, tri.c.y);
			d.drawLine(tri.a.x, tri.a.y, tri.c.x, tri.c.y);
			
		}
	}
	
	public int isPointInTrack(Vector2D pos) {
		
		for (int i = 0 ; i < listTriangle.size() ; i++) {
			Triple<Vector2D, Vector2D, Vector2D> tri = listTriangle.get(i);
			if (pointInTriangle(pos, tri.a, tri.b, tri.c)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public double sign(Vector2D p1, Vector2D p2, Vector2D p3) {
		return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}
	
	public boolean pointInTriangle(Vector2D p, Vector2D p1, Vector2D p2, Vector2D p3) {
		boolean b1 = sign(p, p1, p2) < 0;
		boolean b2 = sign(p, p2, p3) < 0;
		boolean b3 = sign(p, p3, p1) < 0;
		return ((b1 == b2) && (b2 == b3));
	}
	
	public double getSplineWidth() {
		return splineWidth;
	}
	
	public void setSplineWidth(double splineWidth) {
		this.splineWidth = splineWidth;
	}
	
	public BezierSpline2D getSpline() {
		return spline;
	}
	
	public List<Triple<Vector2D, Vector2D, Vector2D>> getListTriangle() {
		return listTriangle;
	}
	
	public double getMaxLapMillis() {
		return maxLapMillis;
	}
	
	public void setMaxLapMillis(double maxLapMillis) {
		this.maxLapMillis = maxLapMillis;
	}
	
}
