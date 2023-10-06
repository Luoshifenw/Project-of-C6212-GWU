import java.util.*;
public class Solution{
    private static class Point{
        private int x;
        private int y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }
        public double distanceTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        long startTime = System.nanoTime();
        //We use n to set out input size
        int n = 8000;
        Point[] points = new Point[n];
        for(int i = 0; i < n; i++){
            points[i] = new Point(random.nextInt(1000), random.nextInt(1000));
        }
        Point[] result = closestPairOfPoints(points);
        long endTime = System.nanoTime();

        // print time needed to finish this program
        System.out.println("n = " + n + ", time = " + (endTime - startTime)+ " nanoseconds");
        System.out.println("The closest pair of points is " + result[0].x + "," + result[0].y + " and " + result[1].x + "," + result[1].y);
    }
    public static Point[] closestPairOfPoints(Point[] input){
        //input: array of Point
        //out put: closest pair of point in this array(Point[])
        //base case: if input.length < 2 return null
        //sub-problem: closestPairInLeftHalf , closestPairInRightHalf
        //Recursion rule: compare minimum distance of points in two halves and minimum distance of points in strip
        if(input.length <= 1){
            System.out.println("Invalid Input");
            return null;
        }
        if (input.length <= 3) {
            return findClosestPointsByBruteForce(input); // Two points, return them as closest pair
        }
        Arrays.sort(input, (p1, p2) -> Double.compare(p1.x, p2.x));
        int mid = input.length / 2;

        Point[] closestPairInLeftHalf = closestPairOfPoints(Arrays.copyOfRange(input, 0,  mid));
        Point[] closestPairInRightHalf = closestPairOfPoints(Arrays.copyOfRange(input, mid, input.length));

        return closestPairBetweenHalves(input, closestPairInLeftHalf, closestPairInRightHalf);
    }

    private static Point[] closestPairBetweenHalves(Point[] points, Point[] closestPairInLeftHalf, Point[] closestPairInRightHalf) {
        //In this method, we need to find the closest pair of points in left side, right side, and strip
        //return Minimum value of them
        // Find the median x-coordinate.
        double medianX = points[points.length / 2].x;
        double leftDistance = findDistanceBetweenTwoPoints(closestPairInLeftHalf[0], closestPairInLeftHalf[1]);
        double rightDistance = findDistanceBetweenTwoPoints(closestPairInRightHalf[0], closestPairInRightHalf[1]);
        double stripScope = Math.min(leftDistance, rightDistance) * 2;
        Point[] closestPairInStrip = closestPairInStrip(points, medianX, stripScope);
        //It's possible that there's no points in strip
        if(closestPairInStrip == null){
            return leftDistance < rightDistance? closestPairInLeftHalf : closestPairInRightHalf;
        }
        double stripDistance = findDistanceBetweenTwoPoints(closestPairInStrip[0], closestPairInStrip[1]);
        if(stripDistance < leftDistance && stripDistance < rightDistance){
            return closestPairInStrip;
        }
        return leftDistance < rightDistance? closestPairInLeftHalf : closestPairInRightHalf;

    }
    private static Point[] closestPairInStrip(Point[] points, double medianX, double stripScope) {
        // Create a list to store the points within the strip.
        List<Point> pointsInStrip = new ArrayList<>();

        // Add the points within the strip to the list.
        for (Point point : points) {
            if (Math.abs(point.x - medianX) < stripScope / 2) {
                pointsInStrip.add(point);
            }
        }

        // Sort the points in the strip by their y-coordinate.
        pointsInStrip.sort(Comparator.comparingDouble(p -> p.y));

        // Find the closest pair of points in the strip.
        Point[] closestPairInStrip = null;
        double closestDistanceInStrip = Double.MAX_VALUE;

        for (int i = 0; i < pointsInStrip.size(); i++) {
            for (int j = i + 1; j < pointsInStrip.size(); j++) {
                if (findDistanceBetweenTwoPoints(pointsInStrip.get(i), pointsInStrip.get(j)) < closestDistanceInStrip) {
                    closestPairInStrip = new Point[]{pointsInStrip.get(i), pointsInStrip.get(j)};
                    closestDistanceInStrip = findDistanceBetweenTwoPoints(pointsInStrip.get(i), pointsInStrip.get(j));
                }
            }
        }
        return closestPairInStrip;
    }
    //find the closest points when pairs of points are less than three
    private static Point[] findClosestPointsByBruteForce(Point[] points){
        Point[] result = new Point[2];
        double globalMin = Double.MAX_VALUE;
        for(int i = 0; i < points.length; i++){
            for(int j = i + 1; j < points.length; j++){
                if(findDistanceBetweenTwoPoints(points[i], points[j]) < globalMin){
                    globalMin = findDistanceBetweenTwoPoints(points[i], points[j]);
                    result[0] = points[i];
                    result[1] = points[j];
                }
            }
        }
        return result;
    }

    //calculate distance between two points
    private static double findDistanceBetweenTwoPoints(Point one, Point two){
        int dx = two.x -one.x;
        int dy = two.y - one.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}