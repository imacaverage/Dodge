/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.util.stream.Stream;

/**
 * Класс "Круг, который движется"
 * @author iMacAverage
 */
public class WheelMoves extends Wheel {
    
    /**
     * позиция до перемещения
     */
    private final Point2D oldPoint;

    /**
     * скорость
     */
    private Vector2D speed;

    /**
     * Создать объект
     * @param point объект "Точка"
     * @param radius радиус
     * @param speed скорость
     */
    public WheelMoves(Point2D point, double radius, Vector2D speed) {
        super(point, radius);
        this.oldPoint = new Point2D(this.getPoint().getX(), this.getPoint().getY());
        this.speed = speed;
    }
    
    /**
     * Получить скорость
     * @return скорость
     */
    public Vector2D getSpeed() {
        return speed;
    }

    /**
     * Задать скорость
     * @param speed скорость
     */
    public void setSpeed(Vector2D speed) {
        this.speed = speed;
    }

     /**
     * Получить позицию до перемещения
     * @return позиция до перемещения
     */
    public Point2D getOldPoint() {
        return oldPoint;
    }

   /**
     * Переместить на заданный вектор
     * @param vector вектор
     */
    public void move(Vector2D vector) {
        this.getOldPoint().setX(this.getPoint().getX());
        this.getOldPoint().setY(this.getPoint().getY());
        this.getPoint().setX(this.getPoint().getX() + vector.getX());
        this.getPoint().setY(this.getPoint().getY() + vector.getY());
    }

    /**
     * Проверить столкновение с объектом "Круг"
     * @param wheel объект "Круг"
     * @return true в случае успеха, иначе false
     */
    public boolean isCollision(Wheel wheel) {
        return this.getPoint().distance(wheel.getPoint()) <= (wheel.getRadius() + this.getRadius());
    }
    
    /**
     * Переместить круг на заданное время
     * @param time заданное время
     */
    public void move(double time) {
        this.move(this.speed.like(time));
    }
    
    /**
     * Проверить движется ли круг в сторону объекта "Линия"
     * @param line объект "Линия"
     * @return true в случае успеха, иначе false
     */
    public boolean isMovesTo(Line2D line) {
        Vector2D vector = new Vector2D(line.getPoint().getX() - this.getPoint().getX(), line.getPoint().getY() - this.getPoint().getY());
        Vector2D projection = vector.projectedVector(new Vector2D(-line.getVector().getY(), line.getVector().getX()));
        return (this.speed.scalarProduct(projection) > 0);
    }
    
    /**
     * Проверить движется ли круг в сторону объекта "Круг, который движется"
     * @param wheel объект "Круг, который движется"
     * @return true в случае успеха, иначе false
     */
    public boolean isMovesTo(WheelMoves wheel) {
        Vector2D v1 = new Vector2D(wheel.getPoint().getX() - this.getPoint().getX(), wheel.getPoint().getY() - this.getPoint().getY());
        Vector2D v2 = new Vector2D(-v1.getX(), -v1.getY());
        return (this.getSpeed().scalarProduct(v1) + wheel.getSpeed().scalarProduct(v2) > 0);
    }
    
    /**
     * Получить объект "Столкновение" с объектом "Линия"
     * @param line объект "Линия"
     * @return объект "Столкновение"
     */
    public Stream<Collision> getCollision(Line2D line) {
        double v = line.getA() * this.getSpeed().getX() + line.getB() * this.getSpeed().getY();
        double a = Math.pow(v, 2);
        double b = 2 * line.getValue(this.getPoint()) * v;
        double c = Math.pow(line.getValue(this.getPoint()), 2) - Math.pow(this.getRadius(), 2) * (Math.pow(line.getA(), 2) + Math.pow(line.getB(), 2));
        return new Quadratic(a, b, c).getRoots().filter(root -> root >= 0).map(root -> new Collision(this, line, root));
    }
    
    /**
     * Получить объект "Столкновение" с объектом "Круг, который движется"
     * @param wheel объект "Круг, который движется"
     * @return объект "Столкновение"
     */
    public Stream<Collision> getCollision(WheelMoves wheel) {
        double a = Math.pow(wheel.getSpeed().getX() - this.getSpeed().getX(), 2) + Math.pow(wheel.getSpeed().getY() - this.getSpeed().getY(), 2);
        double b = 2 * (
                (wheel.getSpeed().getX() - this.getSpeed().getX()) * (wheel.getPoint().getX() - this.getPoint().getX()) 
              + (wheel.getSpeed().getY() - this.getSpeed().getY()) * (wheel.getPoint().getY() - this.getPoint().getY())
                );
        double c = Math.pow(wheel.getPoint().getX() - this.getPoint().getX(), 2) + Math.pow(wheel.getPoint().getY() - this.getPoint().getY(), 2) - Math.pow(this.getRadius() + wheel.getRadius(), 2);
        return new Quadratic(a, b, c).getRoots().filter(root -> root >= 0).map(root -> new Collision(this, wheel, root));
    }

    /**
     * Обработать столкновение с объектом "Линия"
     * @param line объект "Линия"
     */
    public void processCollision(Line2D line) {
        Vector2D U1;
        Vector2D V1 = this.getSpeed();
        Vector2D V2 = line.getVector();
        double alpha = Math.acos(V1.scalarProduct(V2) / (V1.getLength() * V2.getLength()));
        if (V1.vectorProduct(V2) > 0)
            U1 = V1.rotateUnClockWise(2 * alpha);
        else
            U1 = V1.rotateClockWise(2 * alpha);
        this.setSpeed(U1);
    }
    
    /**
     * Обработать столкновение с объектом "Круг, который движется"
     * @param wheel объект "Круг, который движется"
     */
    public void processCollision(WheelMoves wheel) {
        Vector2D centerX = new Vector2D(wheel.getPoint().getX() - this.getPoint().getX(), wheel.getPoint().getY() - this.getPoint().getY());
        Vector2D centerY = new Vector2D(centerX.getY(), -centerX.getX());
        Vector2D V1 = this.getSpeed();
        Vector2D V2 = wheel.getSpeed();
        // векторы по направляющей оси центрального столкновения
        Vector2D V1_x = V1.projectedVector(centerX);
        Vector2D V2_x = V2.projectedVector(centerX);
        // векторы по перпендикулярной оси центрального столкновения
        Vector2D U1_y = V1.projectedVector(centerY);
        Vector2D U2_y = V2.projectedVector(centerY);
        // целочисленные проекции со знаком векторов на направляющую ось
        double v1_x = V1_x.scalarProduct(centerX) / centerX.getLength();
        double v2_x = V2_x.scalarProduct(centerX) / centerX.getLength();
        // массы
        double m1 = Math.pow(this.getRadius(), 2) * this.getHardness();
        double m2 = Math.pow(wheel.getRadius(), 2) * wheel.getHardness();
        // скалярные величины скоростей после столкновения (составляющая по оси соединяющей центры)
        double u1_x = ((m1 - m2) * v1_x + 2 * m2 * v2_x) / (m1 + m2);
        double u2_x = ((m2 - m1) * v2_x + 2 * m1 * v1_x) / (m1 + m2);
        // векторы по оси
        Vector2D U1_x = centerX.getVectorOfLength(u1_x);
        Vector2D U2_x = centerX.getVectorOfLength(u2_x);
        // складываю состовляющие по осям
        Vector2D U1 = U1_x.sum(U1_y);
        Vector2D U2 = U2_x.sum(U2_y);
        // задаю новые скорости кругам
        this.setSpeed(U1);
        wheel.setSpeed(U2);
    }

}
