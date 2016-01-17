/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.util.Optional;

/**
 * Класс "Круг Dodge"
 * @author iMacAverage
 */
public class WheelDodge extends WheelMoves {
    
    /**
     * объект "Фигура"
     */
    private final Figure figure;
    
    /**
     * Создать объект
     * @param point объект "Точка"
     * @param radius радиус
     * @param speed скорость сдвига
     * @param figure объек "Фигура"
     */
    public WheelDodge(Point2D point, double radius, Vector2D speed, Figure figure) {
        super(point, radius, speed);
        this.figure = figure;
    }
    
    @Override
    public void processCollision(Line2D line) {
        // отодвигаю от линии на радиус + 1
        Vector2D vector = line.getPoint().sub(this.getPoint()).projectedVector(new Vector2D(-line.getVector().getY(), line.getVector().getX())).getVectorOfLength(this.getRadius() - this.getPoint().distance(line) + 1);
        this.getPoint().setX(this.getPoint().getX() - vector.getX());
        this.getPoint().setY(this.getPoint().getY() - vector.getY());
        // задаю направление вдоль линии
        this.setSpeed(this.getSpeed().projectedVector(line.getVector()).getVectorOfLength(this.getSpeed().getLength()));
    }

    /**
     * Получить объект "Столкновение" для первого столкновения в рамках заданного времени
     * @param time заданное время
     * @return объект "Столкновение"
     */
    private Optional<Collision> getFirstCollision(double time) {
        
        return this.figure.getLines()
                .filter(this::isMovesTo)
                .flatMap(this::getCollision)
                .min(Collision::compareTo)
                .filter(v -> v.getTime() < time);

    }
    
    @Override
    public void move(double time) {
        
        while (time > 0) {
            Optional<Collision> collision = this.getFirstCollision(time);
            double timeLeft = collision.map(Collision::getTime).orElse(time);
            super.move(timeLeft);
            collision.ifPresent(Collision::process);
            time -= timeLeft;
	}
         
    }

}
