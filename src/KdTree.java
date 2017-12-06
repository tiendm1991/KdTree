import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private static final boolean VERTICAL = true;
	private static final boolean HORIZONTAL = false;
	private static final RectHV RECTHV_MAX = new RectHV(0.0, 0.0, 1.0, 1.0);
	private Node root;
	private int size;
	
	public KdTree() {
		root = null;
		size = 0;
	}

	public boolean isEmpty() { // is the set empty?
		return size == 0;
	}

	public int size() { // number of points in the set
		return size;
	}

	public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		if(p == null){
			throw new IllegalArgumentException("point is illegal");
		}
		if(root == null){
			root = new Node(p, RECTHV_MAX);
			size++;
		}else {
			Node current = root;
			boolean orientation = VERTICAL;
			while (true) {
				if(orientation == VERTICAL){
					double compare = p.x() - current.p.x();
					if(compare >= 0){
						if(current.rt == null){
							Node add = new Node(p, new RectHV(current.p.x(), current.rect.ymin(), 
																current.rect.xmax(), current.rect.ymax()));
							size++;
							current.rt = add;
							break;
						}else {
							current = current.rt;
							orientation = !orientation;
						}
					}else {
						if(current.lb == null){
							Node add = new Node(p, new RectHV(current.rect.xmin(), current.rect.ymin(), 
																current.p.x(), current.rect.ymax()));
							size++;
							current.lb = add;
							break;
						}else {
							current = current.lb;
							orientation = !orientation;
						}
					}
				}else {
					double compare = p.y() - current.p.y();
					if(compare >= 0){
						if(current.rt == null){
							Node add = new Node(p, new RectHV(current.rect.xmin(), current.p.y(), 
																current.rect.xmax(), current.rect.ymax()));
							size++;
							current.rt = add;
							break;
						}else {
							current = current.rt;
							orientation = !orientation;
						}
					}else {
						if(current.lb == null){
							Node add = new Node(p, new RectHV(current.rect.xmin(), current.rect.ymin(), 
																current.rect.xmax(), current.p.y()));
							size++;
							current.lb = add;
							break;
						}else {
							current = current.lb;
							orientation = !orientation;
						}
					}
				}
			}
		}
	}

	public boolean contains(Point2D p) { // does the set contain point p?
		if(p == null){
			throw new IllegalArgumentException("point is illegal");
		}
		if(root == null){
			return false;
		}else {
			Node current = root;
			boolean orientation = VERTICAL;
			while (true) {
				if(current.equals(p)) return true;
				if(orientation == VERTICAL){
					double compare = p.x() - current.p.x();
					if(compare >= 0){
						if(current.rt == null){
							return false;
						}else {
							current = current.rt;
							orientation = !orientation;
						}
					}else {
						if(current.lb == null){
							return false;
						}else {
							current = current.lb;
							orientation = !orientation;
						}
					}
				}else {
					double compare = p.y() - current.p.y();
					if(compare >= 0){
						if(current.rt == null){
							return false;
						}else {
							current = current.rt;
							orientation = !orientation;
						}
					}else {
						if(current.lb == null){
							return false;
						}else {
							current = current.lb;
							orientation = !orientation;
						}
					}
				}
			}
		}
	}

	public void draw() { // draw all points to standard draw
		if(root == null){
			return ;
		}else {
			draw(root, VERTICAL);
		}
	}
	
	public void draw(Node n, boolean orientation) {
		if(orientation == VERTICAL){
			StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
		if(n.lb != null){
			draw(n.lb, !orientation);
		}
		if(n.rt != null){
			draw(n.rt, !orientation);
		}
		
        StdDraw.setPenColor(StdDraw.BLACK);
        n.p.draw();
	}

	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
		if(root == null){
			return null;
		}
		Set<Point2D> setPoint = new TreeSet<>();
		range(root, rect, setPoint);
		return setPoint;
	}

	private void range(Node n, RectHV rect, Set<Point2D> setPoint) {
		if(n == null) return;
		if(rect.contains(n.p)) setPoint.add(n.p);
		if(!n.rect.intersects(rect)){
			return;
		}
		range(n.lb, rect, setPoint);
		range(n.rt, rect, setPoint);
	}

	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
		
	}

	public static void main(String[] args) { // unit testing of the methods (optional)
	}
	
	private static class Node {
		private Point2D p; // the point 
		private RectHV rect; // the axis-aligned rectangle corresponding to this node
		private Node lb; // the left/bottom subtree
		private Node rt; // the right/top subtree
		public Node(Point2D p, RectHV rect) {
			this.p = p;
			this.rect = rect;
		}
	}
}
