/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

        private SET<Point2D> points;
        private Point2D first;


        public PointSET() {                  // construct an empty set of points
            points = new SET<Point2D>();
            first = null;
        }

        public boolean isEmpty() {                     // is the set empty?
            return size() == 0;
        }

        public int size() {
            return points.size();
        }

        public void insert(Point2D p) {
            if(p == null) throw new IllegalArgumentException();
            if(size()==0) first = p;
            points.add(p);
        }            // add the point to the set (if it is not already in the set)



        public boolean contains(Point2D p){
            if(p == null) throw new IllegalArgumentException();
            return points.contains(p);
        }           // does the set contain point p?


        public void draw(){
            for(Point2D x:points){
                x.draw();
            }
        }                       // draw all points to standard draw


        public Iterable<Point2D> range(RectHV rect){
            if(rect == null) throw new IllegalArgumentException();
            SET<Point2D> res = new SET<Point2D>();
            for(Point2D x: points){
                if(rect.contains(x)) res.add(x);
            }
            return res;
        }             // all points that are inside the rectangle (or on the boundary)

        public Point2D nearest(Point2D p){
            if(p == null) throw new IllegalArgumentException();
            Point2D res = first;
            for(Point2D y:points){
                if(y.distanceSquaredTo(p)<res.distanceSquaredTo(p)){
                    res = y;
                }
            }
            return res;
        }            // a nearest neighbor in the set to point p; null if the set is empty



        public static void main (String[] args){    // unit testing of the methods (optional)

        }



    }

