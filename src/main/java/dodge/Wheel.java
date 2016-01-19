/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Класс "Круг"
 * @author iMacAverage
 */
public class Wheel {

    /**
     * объект "Точка"
     */
    private Point2D point;

    /**
     * радиус
     */
    private double radius;

    /**
     * цвет
     */
    private Color color;

    /**
     * плотность
     */
    private double hardness;

    /**
     * область, в которой отображен круг
     */
    private final Rectangle rectangle;

    /**
     * Создать объект
     * @param point объект "Точка"
     * @param radius радиус
     */
    public Wheel(Point2D point, double radius) {
        this.point = point;
        this.radius = radius;
        this.rectangle = new Rectangle();
        this.color = Color.BLACK;
        this.hardness = 1.;

    }

    /**
     * Отобразить
     * @param g объект "Рисование"
     */
    public void show(Graphics g) {
        g.setColor(this.color);
        rectangle.setRect(this.point.getX() - this.radius, this.point.getY() - this.radius, this.radius * 2, this.radius * 2);
        g.fillOval((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }

    /**
     * Стереть (нарисовать цветом фона)
     * @param g объект "Рисование"
     * @param background цвет фона
     */
    public void clear(Graphics g, Color background) {
        g.setColor(background);
        g.fillOval((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }

    /**
     * Обновить
     * @param g объект "Рисование"
     * @param background цвет фона
     */
    public void refresh(Graphics g, Color background) {
        this.clear(g, background);
        this.show(g);
    }

    /**
     * Получить объект "Точка"
     * @return объект "Точка"
     */
    public Point2D getPoint() {
        return this.point;
    }

    /**
     * Получить радиус
     * @return радиус
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Получить плотность
     * @return плотность
     */
    public double getHardness() {
        return this.hardness;
    }

    /**
     * Задать цвет
     * @param color цвет
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Задать плотность
     * @param hardness плотность
     */
    public void setHardness(double hardness) {
        this.hardness = hardness;
    }

}
