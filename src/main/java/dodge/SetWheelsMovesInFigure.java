/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

/**
 * Класс "Множество объектов 'Круг', движущихся внутри объекта 'Фигура'"
 * @author iMacAverage
 */
public class SetWheelsMovesInFigure {

    /**
     * множество объектов "Круг, который движется"
     */
    private final ArrayList<WheelMoves> wheels;

    /**
     * объект "Фигура"
     */
    private final Figure figure;

    /**
     * Создать объект
     * @param wheelDodge объект "Круг Dodge"
     * @param figure объект "Фигура"
     * @param impulse импульс круга
     * @param maxRadius максимальный радиус круга
     * @param countPassive количество пассивных кругов
     * @param countActive количество активных кругов
     * @param countActiveLight количество легких активных кругов
     */
    public SetWheelsMovesInFigure(WheelDodge wheelDodge, Figure figure, double impulse, double maxRadius, int countPassive, int countActive, int countActiveLight) {
        WheelMoves wheel;
        Random rnd = new Random(System.currentTimeMillis());
        int countWheels = countPassive + countActive + countActiveLight;
        double alpha = (countWheels > 1? 2 * Math.PI / countWheels : 0);
        double l = 1.1 * Math.max(wheelDodge.getRadius() + maxRadius, (alpha > 0? maxRadius / Math.sin(alpha / 2) : 0));
        Vector2D vector = new Vector2D(0, -l).rotateClockWise(rnd.nextInt(21) * Math.PI / 10.);
        this.wheels = new ArrayList<>();
        this.figure = figure;
        for (int i = 0; i < countWheels; i++) {
            double radius = maxRadius * (5. / 6 + rnd.nextInt(11) / 60.);
            Vector2D v = vector.rotateClockWise(alpha * i);
            Point2D point = new Point2D(v.getX() + wheelDodge.getPoint().getX(), v.getY() + wheelDodge.getPoint().getY());
            Vector2D speed = v.getVectorOfLength(impulse / Math.pow(radius, 2));
            speed.rotateClockWise(alpha * (-1. / 2 + rnd.nextInt(101) / 100.));
            if (countActiveLight-- > 0) {
                wheel = new WheelMovesActive(point, radius, speed, wheelDodge);
                wheel.setColor(Color.decode("#F08080"));
                wheel.setHardness(.5);
            }
            else {
                if (countActive-- > 0) {
                    wheel = new WheelMovesActive(point, radius, speed, wheelDodge);
                    wheel.setColor(Color.decode("#FFA500"));
                }
                else {
                    wheel = new WheelMoves(point, radius, speed);
                    wheel.setColor(Color.decode("#6495ED"));
                }
            }
            this.wheels.add(wheel);
        }
    }

    /**
     * Проверить пересечение с объектом "Круг Dodge"
     * @param wheelDodge объект "Круг, который может перемещаться"
     * @return true в случае успеха, иначе false
     */
    public boolean isCollision(WheelDodge wheelDodge) {
        return this.wheels.stream().anyMatch(wheelDodge::isCollision);
    }

    /**
     * Получить объект "Столкновение" для первого столкновения в рамках заданного времени
     * @param time заданное время
     * @return объект "Столкновение"
     */
    private Optional<Collision> getFirstCollision(double time) {

        int i = 0;
        Optional<Collision> minCollision, firstCollision = Optional.empty();

        for (WheelMoves wheel : this.wheels) {
            Optional<Collision> collision = firstCollision;
            minCollision = this.figure.getLines()
                                .filter(wheel::isMovesTo)
                                .flatMap(wheel::getCollision)
                                .min(Collision::compareTo)
                                .filter(v -> v.getTime() < collision.map(Collision::getTime).orElse(time));
            if (minCollision.isPresent())
                firstCollision = minCollision;
        }

        for (WheelMoves wheel : this.wheels) {
            Optional<Collision> collision = firstCollision;
            minCollision = this.wheels.stream()
                                .skip(++i)
                                .filter(wheel::isMovesTo)
                                .flatMap(wheel::getCollision)
                                .min(Collision::compareTo)
                                .filter(v -> v.getTime() < collision.map(Collision::getTime).orElse(time));
            if (minCollision.isPresent())
                firstCollision = minCollision;
        }

        return firstCollision;

    }

    /**
     * Переместить все круги
     * @param time время
     */
    public void move(double time) {

        while (time > 0) {
            Optional<Collision> collision = this.getFirstCollision(time);
            double timeLeft = collision.map(Collision::getTime).orElse(time);
            this.wheels.stream().forEach(w -> w.move(timeLeft));
            collision.ifPresent(Collision::process);
            time -= timeLeft;
	}

    }
    
    /**
     * Обновить все круги
     * @param g объект "Рисование"
     * @param background цвет фона
     */
    public void refresh(Graphics g, Color background) {
        this.wheels.stream().forEach(w -> w.refresh(g, background));
    }
    
}
