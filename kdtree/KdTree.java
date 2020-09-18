/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static final boolean VER = true;
    private static final boolean HOR = false;
    private static final int LEFT = 1; // point is on right of rectangle
    private static final int IN = 2; // point is in or on rectangle
    private static final int RIGHT  = 3; //point is on the left of the rectangle




    private Node root;
    private int n;

    private int side(Node n, RectHV rect){
        if(n.orient==VER){
            if(rect.xmax()<n.point.x()) return LEFT;
            if(rect.xmin()>n.point.x()) return RIGHT;
            return IN;
        }
        else {
            if(rect.ymax()<n.point.y()) return LEFT;
            if(rect.ymin()>n.point.y()) return RIGHT;
            return IN;
        }
    }

    private class Node {
        private Point2D point;
        private Node left;
        private Node right;
        private Node parent;
        private boolean orient;
        private double x11, x21, y11, y21;
        private RectHV bb;

        public Node(Point2D point, boolean orient, Node parent, double x11, double y11, double x21,
                    double y21) {
            this.point = point;
            left = null;
            right = null;
            this.parent = parent;
            this.orient = orient;
            this.x11 = x11;
            this.y11 = y11;
            this.x21 = x21;
            this.y21 = y21;

            double x1, y1, x2, y2;
            if (parent==null) bb = new RectHV(0, 0, 1, 1);
            else {
                if (parent.orient == VER) {
                    if (point.x()>= parent.point.x()) {
                        x1 = parent.point.x();
                        y1 = parent.bb.ymin();
                        x2 = parent.bb.xmax();
                        y2 = parent.bb.ymax();
                    }
                    else {
                        x1 = this.parent.bb.xmin();
                        y1 = this.parent.bb.ymin();
                        x2 = this.parent.point.x();
                        y2 = this.parent.bb.ymax();
                    }
                }
                else {
                    if (point.y()>=parent.point.y()) {
                        x1 = this.parent.bb.xmin();
                        y1 = this.parent.point.y();
                        x2 = this.parent.bb.xmax();
                        y2 = this.parent.bb.ymax();
                    }
                    else {
                        x1 = this.parent.bb.xmin();
                        y1 = this.parent.bb.ymin();
                        x2 = this.parent.bb.xmax();
                        y2 = this.parent.point.y();
                    }
                }
                //  System.out.println(x1+" "+y1+" "+x2+" "+y2);
                bb = new RectHV(x1, y1, x2, y2);
            }
        }
    }

    public KdTree(){
        root = null;
        n = 0;
    }                              // construct an empty set of points


    public boolean isEmpty(){
        return n==0;
    }                     // is the set empty?


    public int size() {
        return n;
    }                       // number of points in the set


    public void insert(Point2D p){
        if(p==null) throw new IllegalArgumentException();
        Node temp = locate(p);
        if(temp==null) {
            root = new Node(p,VER,null,p.x(),0,p.x(),1);
            n++;
        }
        else {
            if (!temp.point.equals(p)) {
                if (temp.orient == VER) {
                    if (p.x() >= temp.point.x())  temp.right = new Node(p, HOR, temp,temp.point.x(),p.y(),1,p.y());
                    else temp.left = new Node(p, HOR, temp,0,p.y(),temp.point.x(),p.y());
                }
                else {
                    if (p.y() >= temp.point.y())      temp.right = new Node(p, VER, temp,p.x(),temp.point.y(),p.x(),1);
                    else temp.left = new Node(p, VER, temp,p.x(),0,p.x(),temp.point.y());
                }
                n++;
            }
        }
    }             // add the point to the set (if it is not already in the set)


    public boolean contains(Point2D p){
        Node temp = locate(p);
        if(p==null) throw new IllegalArgumentException();
        if(temp==null) return false;
        if(temp.point.equals(p)) return true;
        return false;
    }            // does the set contain point p?

    private Node locate(Point2D p){
        int i = 0;
        Node j = root;
      //  
        while(j!=null){
            if (i%2==0) {
                if (p.x() < j.point.x()){
                    if(j.left!=null) j = j.left;
                    else {
                        return j;
                    }
                }
                else if (p.x() > j.point.x()){
                    if(j.right!=null) j = j.right;
                    else {
                        return j;
                    }
                }
                else if (p.y() != j.point.y()){
                    if(j.right!=null) j = j.right;
                    else {
                        return j;
                    }
                }
                else{
                    return j;
                }
            }
            else{
                if (p.y() < j.point.y()) {
                    if(j.left!=null) j = j.left;
                    else{
                        return j;
                    }
                }
                else if (p.y() > j.point.y()){
                    if(j.right!=null) j = j.right;
                    else {
                        return j;
                    }
                }
                else if (p.x() != j.point.x()){
                    if(j.right!=null) j = j.right;
                    else {
                        return j;
                    }
                }
                else {
                    return j;
                }
            }
            i++;
        }
        return j;
    }


    public void draw()
    {
        draw(root);
    }// draw all points to standard draw

    private void draw(Node node) {
        if(node!=null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.point.draw();
               if (node.orient == HOR) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(node.x11, node.y11, node.x21, node.y21);
            }
            else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(node.x11, node.y11, node.x21, node.y21);
            }

            draw(node.left);
            draw(node.right);
        }
    }

    public Iterable<Point2D> range(RectHV rect){
        if(rect==null) throw new IllegalArgumentException();
        return range(rect,root);
    }             // all points that are inside the rectangle (or on the boundary)


    private SET<Point2D> range(RectHV rect,Node node){
        if(node==null) return new SET<Point2D>();
        if(side(node,rect)==LEFT){
            return range(rect,node.left);
        }

        else if(side(node,rect)==IN){
            SET<Point2D> temp = new SET<Point2D>();
            temp = (range(rect, node.left).union(range(rect, node.right)));
            if(rect.contains(node.point)) temp.add(node.point);
            return temp;
        }

        else {
            return range(rect,node.right);
        }

    }             // all points that are inside the rectangle (or on the boundary)



    public Point2D nearest(Point2D p)  {
        if(p==null) throw new IllegalArgumentException();
        return nearest(p,root).point;
    }           // a nearest neighbor in the set to point p; null if the set is empty


    private double dist(Point2D p,Node node){
        return node.point.distanceSquaredTo(p);
    }

    private Node towards(Point2D p, Node node){
        if(node.orient==VER){
            if(p.x()<node.point.x()) return node.left;
            else return node.right;
        }
        else{
            if(p.y()<node.point.y()) return node.left;
            else return node.right;
        }
    }
    private Node away(Point2D p, Node node){
        if(node.orient==VER){
            if(p.x()<node.point.x()) return node.right;
            else return node.left;
        }
        else{
            if(p.y()<node.point.y()) return node.right;
            else return node.left;
        }
    }

    private Node nearest(Point2D p,Node node){
       Node min = node;
       double least = dist(p,min);
       if(towards(p,node)!=null) {
           Node a = nearest(p,towards(p,node));
           if(dist(p,a)<least) {
               min = a;
               least = dist(p,min);
           }
       }
       if(away(p,node)!=null){
           if(away(p,node).bb.distanceSquaredTo(p)<=least){
           		Node b = nearest(p,away(p,node));
               if(dist(p,b)<least) {
                   min = b;
               }
           }

       }


    return min;
    }


    public static void main(String[] args) {
        KdTree a = new KdTree();
        Point2D a1  = new Point2D(0.3,0.4);
        Point2D a2 = new Point2D(0.4,0.8);
        Point2D a3 = new Point2D(0.1,0.3);
        Point2D a4 = new Point2D(0.4,0.9);
        Point2D a5 = new Point2D(0.3,0.7);
        Point2D a6 = new Point2D(0,0.5);
        Point2D a7 = new Point2D(0.1,0.2);
       // Point2D b1 = new Point2D(3,5);


        a.insert(a1);
        a.insert(a2);
      /* a.insert(a3);
       a.insert(a4);
        a.insert(a5);
        a.insert(a6);
        a.insert(a7);

       */

       RectHV r = a.root.right.bb;
        StdDraw.setPenRadius(0.02);
       StdDraw.setPenColor(StdDraw.BLACK);
       r.draw();


     /*   System.out.println(a.locate(a2));
        System.out.println(a.locate(a4).parent);
        System.out.println(a.locate(a3));
        System.out.println(a.locate(a6).parent);
        System.out.println(a.locate(a7).right);



       */



// there is some problem with locate



    }
}
