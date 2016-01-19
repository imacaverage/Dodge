/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

/**
 * Класс "Точка"
 * @author iMacAverage
 */
public class Point2D {
    
    /**
     * абсцисса
     */
    private double x;
    
    /**
     * ордината
     */
    private double y;
    
    /**
     * Создать объект
     * @param x обсцисса
     * @param y ордината
     */
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Получить абсциссу
     * @return абсцисса
     */
    public double getX() {
        return x;
    }

    /**
     * Задать абсциссу
     * @param x абсцисса
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Получить ординату
     * @return ордината
     */
    public double getY() {
        return y;
    }

    /**
     * Задать ординату
     * @param y ордината
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Расстояние до объекта "Точка"
     * @param point объект "Точка"
     * @return расстояние
     */
    public double distance(Point2D point) {
        return Math.sqrt(Math.pow(point.getX() - x, 2) + Math.pow(point.getY() - y, 2));
    }

    /**
     * Расстояние до объекта "Линия"
     * @param line объект "Линия"
     * @return расстояние
     */
    public double distance(Line2D line) {
        return Math.abs(line.getValue(this)) / Math.sqrt(Math.pow(line.getA(), 2) + Math.pow(line.getB(), 2));
    }
    
    /**
     * Вычесть объект "Точка"
     * @param point объект "Точка"
     * @return объект "Вектор"
     */
    public Vector2D sub(Point2D point) {
        return new Vector2D(this.getX() - point.getX(), this.getY() - point.getY());
    }
    
    /**
     * Сдвинуть на объект "Вектор"
     * @param vector объект "Вектор"
     * @return объект "Точка"
     */
    public Point2D shift(Vector2D vector) {
        return new Point2D(this.getX() + vector.getX(), this.getY() + vector.getY());
    }

}
