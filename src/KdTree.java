import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private static final boolean VERTICAL = true;
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

	public void insert(Point2D p) { // add the point to the set (if it is not
									// already in the set)
		if (p == null) {
			throw new IllegalArgumentException("point is illegal");
		}
		if (!RECTHV_MAX.contains(p))
			return;
		if (root == null) {
			root = new Node(p, RECTHV_MAX);
			size++;
		} else {
			Node current = root;
			boolean orientation = VERTICAL;
			while (true) {
				if (p.equals(current.p))
					return;
				if (orientation == VERTICAL) {
					double compare = p.x() - current.p.x();
					if (compare >= 0) {
						if (current.rt == null) {
							Node add = new Node(p, new RectHV(current.p.x(), current.rect.ymin(), current.rect.xmax(),
									current.rect.ymax()));
							size++;
							current.rt = add;
							break;
						} else {
							current = current.rt;
							orientation = !orientation;
						}
					} else {
						if (current.lb == null) {
							Node add = new Node(p, new RectHV(current.rect.xmin(), current.rect.ymin(), current.p.x(),
									current.rect.ymax()));
							size++;
							current.lb = add;
							break;
						} else {
							current = current.lb;
							orientation = !orientation;
						}
					}
				} else {
					double compare = p.y() - current.p.y();
					if (compare >= 0) {
						if (current.rt == null) {
							Node add = new Node(p, new RectHV(current.rect.xmin(), current.p.y(), current.rect.xmax(),
									current.rect.ymax()));
							size++;
							current.rt = add;
							break;
						} else {
							current = current.rt;
							orientation = !orientation;
						}
					} else {
						if (current.lb == null) {
							Node add = new Node(p, new RectHV(current.rect.xmin(), current.rect.ymin(),
									current.rect.xmax(), current.p.y()));
							size++;
							current.lb = add;
							break;
						} else {
							current = current.lb;
							orientation = !orientation;
						}
					}
				}
			}
		}
	}

	public boolean contains(Point2D p) { // does the set contain point p?
		if (p == null) {
			throw new IllegalArgumentException("point is illegal");
		}
		if (root == null) {
			return false;
		} else {
			Node current = root;
			boolean orientation = VERTICAL;
			while (true) {
				if (current.p.equals(p))
					return true;
				if (orientation == VERTICAL) {
					double compare = p.x() - current.p.x();
					if (compare >= 0) {
						if (current.rt == null) {
							return false;
						} else {
							current = current.rt;
							orientation = !orientation;
						}
					} else {
						if (current.lb == null) {
							return false;
						} else {
							current = current.lb;
							orientation = !orientation;
						}
					}
				} else {
					double compare = p.y() - current.p.y();
					if (compare >= 0) {
						if (current.rt == null) {
							return false;
						} else {
							current = current.rt;
							orientation = !orientation;
						}
					} else {
						if (current.lb == null) {
							return false;
						} else {
							current = current.lb;
							orientation = !orientation;
						}
					}
				}
			}
		}
	}

	public void draw() { // draw all points to standard draw
		draw(root, VERTICAL);
	}

	private void draw(Node n, boolean orientation) {
		if (n == null)
			return;
		if (orientation == VERTICAL) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
		}
		if (n.lb != null) {
			draw(n.lb, !orientation);
		}
		if (n.rt != null) {
			draw(n.rt, !orientation);
		}

		StdDraw.setPenColor(StdDraw.BLACK);
		n.p.draw();
	}

	public Iterable<Point2D> range(RectHV rect) { // all points that are inside
													// the rectangle (or on the
													// boundary)
		if (root == null) {
			return null;
		}
		Set<Point2D> setPoint = new TreeSet<>();
		range(root, rect, setPoint);
		return setPoint;
	}

	private void range(Node n, RectHV rect, Set<Point2D> setPoint) {
		if (n == null)
			return;
		if (!n.rect.intersects(rect)) {
			return;
		}
		if (rect.contains(n.p))
			setPoint.add(n.p);
		range(n.lb, rect, setPoint);
		range(n.rt, rect, setPoint);
	}

	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to
										// point p; null if the set is empty
		if (root == null || root.p == null)
			return null;
		Node minNode = root;
		nearest(p, root, minNode);
		return minNode.p;
	}

	private void nearest(Point2D p, Node n, Node minNode) {
//		System.out.println(n);
		if (n == null || n.p == null)
			return;
		double minDistance = p.distanceSquaredTo(minNode.p);
		if (p.distanceSquaredTo(n.p) < minDistance) {
			minDistance = p.distanceSquaredTo(n.p);
			minNode.p = n.p;
		}
		if(n.lb != null && n.rt != null){
			double dis1 = p.distanceSquaredTo(n.lb.p);
			double dis2 = p.distanceSquaredTo(n.rt.p);
			if(dis1 < dis2){
				if(!ignoreBranch(n.lb.rect, p, minNode)){
					nearest(p, n.lb, minNode);
				}
				if(!ignoreBranch(n.rt.rect, p, minNode)){
					nearest(p, n.rt, minNode);
				}
			}else {
				if(!ignoreBranch(n.rt.rect, p, minNode)){
					nearest(p, n.rt, minNode);
				}
				if(!ignoreBranch(n.lb.rect, p, minNode)){
					nearest(p, n.lb, minNode);
				}
			}
		}else {
			if (n.lb != null && !ignoreBranch(n.lb.rect, p, minNode)) {
				nearest(p, n.lb, minNode);
			} else if (n.rt != null && !ignoreBranch(n.rt.rect, p, minNode)) {
				nearest(p, n.rt, minNode);
			}
		}
	}

	private boolean ignoreBranch(RectHV rect, Point2D p, Node minNode) {
		if (rect.contains(p))
			return false;
		return rect.distanceSquaredTo(p) > p.distanceSquaredTo(minNode.p);
	}

	public static void main(String[] args) { // unit testing of the methods
												// (optional)
		 KdTree kdtree = new KdTree();
		 kdtree.insert(new Point2D(0.7, 0.2));//A
		 kdtree.insert(new Point2D(0.5, 0.4));//B
		 kdtree.insert(new Point2D(0.2, 0.3));//C
		 kdtree.insert(new Point2D(0.4, 0.7));//D
		 kdtree.insert(new Point2D(0.9, 0.6));//E
//		String filename = "circle10.txt";
//		In in = new In(filename);
//		KdTree kdtree = new KdTree();
//		while (!in.isEmpty()) {
//			double x = in.readDouble();
//			double y = in.readDouble();
//			Point2D p = new Point2D(x, y);
//			kdtree.insert(p);
//		}
		StdDraw.clear();
		StdDraw.setPenRadius(0.02);
		StdDraw.setPenColor(StdDraw.BLACK);
		kdtree.draw();

		Point2D check = new Point2D(0.85, 0.23);
		StdDraw.setPenRadius(0.03);
		StdDraw.setPenColor(StdDraw.ORANGE);
		check.draw();
		
		Point2D nearest = kdtree.nearest(check);
		StdDraw.setPenRadius(0.03);
		StdDraw.setPenColor(StdDraw.GREEN);
		nearest.draw();
		
		System.out.println(nearest);
	}

	private static class Node {
		private Point2D p; // the point
		private RectHV rect; // the axis-aligned rectangle corresponding to this
								// node
		private Node lb; // the left/bottom subtreewd
		private Node rt; // the right/top subtree

		public Node(Point2D p, RectHV rect) {
			this.p = p;
			this.rect = rect;
		}

		@Override
		public String toString() {
			return "Node [p=" + p + "]";
		}

	}
}
