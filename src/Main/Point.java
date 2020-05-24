package Main;

public class Point {

	public int x;
	public int y;
	
	public Point() {
		
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static Point from(String pointStr) {
		return setPoint(new Point(), pointStr);
	}
	
	public static Point setPoint(Point point, Point other) {
		if(point == null) {
			point = new Point();
		}
		
		point.x = other.x;
		point.y = other.y;
		
		return point;
	}
	
	public static Point setPoint(Point point, String pointStr) {
		if(point == null) {
			point = new Point();
		}
		
		if(pointStr == null) {
			return null;
		}
		pointStr = pointStr.replace("(", "");
		pointStr = pointStr.replace(")", "");
		
		String[] sides = pointStr.split(",");
		
		int x = Integer.parseInt(sides[0]);
		int y = Integer.parseInt(sides[1]);
		
		point.x = x;
		point.y = y;
		
		return point;
	}
	
	public static String stringFrom(java.awt.Point point) {
		return ("(" + point.x + "," + point.y + ")").intern();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		Point otherPoint = (Point)other;
		return this.x == otherPoint.x && this.y == otherPoint.y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
}
