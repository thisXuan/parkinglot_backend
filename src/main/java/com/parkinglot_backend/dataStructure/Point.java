package com.parkinglot_backend.dataStructure;

import java.util.Objects;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-11-07
 * @Description:
 */
public class Point {
    public double x;
    public double y;
    public String floor;        // 楼层信息
    public boolean isElevator;  // 是否是电梯

    public Point(double x, double y, String floor, boolean isElevator) {
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.isElevator = isElevator;
    }

    // 获取楼层信息
    public String getFloor() {
        return floor;
    }
    public boolean getIsElevator() {return isElevator;}

    // 设置楼层信息
    public void setFloor(String floor) {
        this.floor = floor;
    }

    // 获取电梯信息
    public boolean isElevator() {
        return isElevator;
    }

    // 设置电梯信息
    public void setElevator(boolean elevator) {
        isElevator = elevator;
    }

    public double distance(Point other) {
        return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y &&
                Objects.equals(floor, point.floor) &&
                isElevator == point.isElevator;
    }


    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}