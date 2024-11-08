package com.parkinglot_backend.dataStructure;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-11-07
 * @Description:
 */

public class Node implements Comparable<Node> {
    public Point point;
    public Node parent;
    public double g = 0;
    public double h = 0;
    public double f = 0;

    public Node(Point point) {
        this.point = point;
    }

    public int compareTo(Node other) {
        return Double.compare(this.f, other.f);
    }

    public void updateNode(Node parent, double g, double h) {
        this.parent = parent;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }
}