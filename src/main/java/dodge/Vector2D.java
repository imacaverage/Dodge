/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

/**
 * Класс "Вектор"
 * @author iMacAverage
 */
public class Vector2D {
    
    /**
     * абсцисса
     */
    private final double x;
    
    /**
     * ордината
     */
    private final double y;
    
    /**
     * длина
     */
    private final double length;
    
    /**
     * Создать объект
     * @param x абсцисса
     * @param y ордината
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
        this.length = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     * Получить абсциссу
     * @return абсцисса
     */
    public double getX() {
        return x;
    }

    /**
     * Получить ординату
     * @return ордината
     */
    public double getY() {
        return y;
    }

    /**
     * Получить длину вектора
     * @return длину вектора
     */
    public double getLength() {
        return this.length;
    }

    /**
     * Сложить векторы
     * @param vector объект "Вектор"
     * @return сумма векторов
     */
    public Vector2D sum(Vector2D vector) {
        return new Vector2D(this.x + vector.getX(), this.y + vector.getY());
    }    
    
    /** 
     * Получить объект "Вектор" по длине в направлении текущего объекта
     * @param length длина вектора
     * @return объект "Вектор"
     */
    public Vector2D getVectorOfLength(double length) {
        double k = 0;
        if (this.length != 0)
            k = length / this.length;
        return new Vector2D(k * this.x, k * this.y);
    }
    
    /**
     * Получить подобный вектор
     * @param d коэффициент подобия
     * @return подобный вектор
     */
    public Vector2D like(double d) {
        return new Vector2D(this.x * d, this.y * d);
    }
    
    /**
     * Скалярное произведение векторов
     * @param vector объект "Вектор"
     * @return скалярное произведение
     */
    public double scalarProduct(Vector2D vector) {
        return this.x * vector.getX() + this.y * vector.getY();
    }
    
    /**
     * Векторное произведение векторов
     * @param vector объект "Вектор"
     * @return векторное произведение
     */
    public double vectorProduct(Vector2D vector) {
        return this.x * vector.getY() - vector.getX() * this.y;
    }

    /**
     * Повернуть на угол alpha против часовой стрелки
     * @param alpha угол поворота
     * @return объект "Вектор"
     */
    public Vector2D rotateUnClockWise(double alpha) {
        return new Vector2D(Math.cos(alpha) * this.x - Math.sin(alpha) * this.y, Math.sin(alpha) * this.x + Math.cos(alpha) * this.y);
    }

    /**
     * Повернуть на угол alpha по часовой стрелке
     * @param alpha угол поворота
     * @return объект "Вектор"
     */
    public Vector2D rotateClockWise(double alpha) {
        return new Vector2D(Math.cos(alpha) * this.x + Math.sin(alpha) * this.y, -Math.sin(alpha) * this.x + Math.cos(alpha) * this.y);
    }
    
    /**
     * Спроецировать на вектор
     * @param vector объект "Вектор"
     * @return проекция
     */
    public Vector2D projectedVector(Vector2D vector) {
        double length = 0;
        if (vector.getLength() != 0)
            length = this.scalarProduct(vector) / vector.getLength();
        return vector.getVectorOfLength(length);
    }
    
}
