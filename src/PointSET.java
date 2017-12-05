import java.util.Iterator;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
	private TreeSet<Point2D> _setPoint;
	
	public PointSET() { // construct an empty set of points
		_setPoint = new TreeSet<>();
	}

	public boolean isEmpty() { // is the set empty?
		return _setPoint.isEmpty();
	}

	public int size() { // number of points in the set
		return _setPoint.size();
	}

	public void insert(Point2D p) { // add the point to the set (if it is not
									// already in the set)
		if(p == null){
			throw new IllegalArgumentException("point is illegal");
		}
		_setPoint.add(p);
	}

	public boolean contains(Point2D p) { // does the set contain point p?
		if(p == null){
			throw new IllegalArgumentException("point is illegal");
		}
		return _setPoint.contains(p);
	}

	public void draw() { // draw all points to standard draw
		 for(Point2D point : _setPoint) {
			 point.draw();
		 }
	}

	public Iterable<Point2D> range(RectHV rect) { // all points that are insidethe rectangle (or on the boundary)
		if(rect == null){
			throw new IllegalArgumentException("rect is illegal");
		}
		TreeSet<Point2D> setRange = new TreeSet<>();
		for(Iterator<Point2D> it = _setPoint.iterator(); it.hasNext();){
			Point2D point = it.next();
			if(rect.contains(point)) setRange.add(point);
		}
		return setRange;
	}

	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
		if(p == null){
			throw new IllegalArgumentException("rect is illegal");
		}
		if(size() <= 1) return null;
		double min = 2.0;
		Point2D pMin = null;
		for(Iterator<Point2D> it = _setPoint.iterator(); it.hasNext();){
			Point2D point = it.next();
			if(p.distanceTo(point) < min && !p.equals(point)) {
				min = p.distanceTo(point);
				pMin = point;
			}
		}
		return pMin;
	}

	public static void main(String[] args) { // unit testing of the methods
												// (optional)
	}
}
