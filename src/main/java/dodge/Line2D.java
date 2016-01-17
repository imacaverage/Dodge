/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Класс линия
 * @author iMacAverage
 */
public class Line2D {
    
    /**
     * объект "Точка"
     */
    private Point2D point;
    
    /**
     * объект "Вектор"
     */
    private Vector2D vector;
    
    /**
     * Создать объект
     * @param point объект "Точка"
     * @param vector объект "Вектор"
     */
    public Line2D(Point2D point, Vector2D vector) {
        this.point = point;
        this.vector = vector;
    }
    
    /**
     * Получить коэффициент A в общем виде уравнения прямой
     * @return коэффициент A в общем виде уравнения прямой
     */
    public double getA() {
        return (this.vector.getY() == 0? 0 : 1);
    }

    /**
     * Получить коэффициент B в общем виде уравнения прямой
     * @return коэффициент B в общем виде уравнения прямой
     */
    public double getB() {
        return (this.getA() == 0? 1 : -this.vector.getX() / this.vector.getY());
    }
    
    /**
     * Получить коэффициент C в общем виде уравнения прямой
     * @return коэффициент C в общем виде уравнения прямой
     */
    public double getC() {
        return -this.getB() * this.getPoint().getY() - this.getA() * this.getPoint().getX();
    }

    /**
     * Получить объект "Точка"
     * @return объект "Точка"
     */
    public Point2D getPoint() {
        return this.point;
    }

    /**
     * Задать объект "Точка"
     * @param point объект "Точка"
     */
    public void setPoint(Point2D point) {
        this.point = point;
    }

    /**
     * Получить объект "Вектор"
     * @return объект "Вектор"
     */
    public Vector2D getVector() {
        return this.vector;
    }

    /**
     * Задать объект "Вектор"
     * @param vector объект "Вектор"
     */
    public void setVector(Vector2D vector) {
        this.vector = vector;
    }
    
    /**
     * Получить значение уравнения в точке
     * @param point объект "Точка"
     * @return значение уравнения
     */    
    public double getValue(Point2D point) {
        return this.getA() * point.getX() + this.getB() * point.getY() + this.getC();
    }
    
    /**
     * Отобразить отрезок прямой из точки на длину вектора
     * @param g объект "Рисование"
     * @param color цвет
     */
    public void show(Graphics g, Color color) {
        g.setColor(color);
        g.drawLine((int) this.point.getX(), (int) this.point.getY(), (int) (this.point.getX() + this.vector.getX()), (int) (this.point.getY() + this.vector.getY()));
    }

}
