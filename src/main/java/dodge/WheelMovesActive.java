/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

/**
 * Класс "Активно движущийся Круг"
 * @author iMacAverage
 */
public class WheelMovesActive extends WheelMoves {
    
    /**
     * объект "Круг Dodge"
     */
    private final Wheel wheelDodge;

    /**
     * Создать объект
     * @param point объект "Точка"
     * @param radius радиус
     * @param speed скорость
     * @param wheelDodge объект "Круг-Dodge"
     */
    public WheelMovesActive(Point2D point, double radius, Vector2D speed, Wheel wheelDodge) {
        super(point, radius, speed);
        this.wheelDodge = wheelDodge;
    }
    
    @Override
    public void processCollision(Line2D line) {
        // отодвигаю от линии на радиус + 1
        Vector2D vector = line.getPoint().sub(this.getPoint()).projectedVector(new Vector2D(-line.getVector().getY(), line.getVector().getX())).getVectorOfLength(this.getRadius() - this.getPoint().distance(line) + 1);
        this.getPoint().setX(this.getPoint().getX() - vector.getX());
        this.getPoint().setY(this.getPoint().getY() - vector.getY());
        // курс на круг Dodge
        this.setSpeed(this.wheelDodge.getPoint().sub(this.getPoint()).getVectorOfLength(this.getSpeed().getLength()));
        // если вектор направления на Dodge снова ведет в сторону линии, то двигаюсь вдоль ее  
        if (this.isMovesTo(line))
            this.setSpeed(this.getSpeed().projectedVector(line.getVector()).getVectorOfLength(this.getSpeed().getLength()));
    }
    
}
