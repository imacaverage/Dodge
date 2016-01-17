/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.util.Optional;

/**
 * Класс "Столкновение"
 * @author iMacAverage
 */
public class Collision {
    
    /**
     * объект "Круг, который движется"
     */
    private final WheelMoves wheel;
        
    /**
     * объект "Круг, который движется", с которым происходит столкновение (в случае если оно происходит с кругом)
     */
    private final Optional<WheelMoves> wheelTo;
    
    /**
     * объект "Линия", с которым происходит столкновение (в случае если оно происходит с линией)
     */
    private final Optional<Line2D> line;

    /**
     * время до столкновения
     */
    private final double time;
    
    /**
     * Создать объект
     * @param wheel объект "Круг, который движется"
     * @param wheelTo объект "Круг, который движется", с которым происходит столкновение
     * @param time время до столкновения
     */
    public Collision(WheelMoves wheel, WheelMoves wheelTo, double time) {
        this.wheel = wheel;
        this.wheelTo = Optional.of(wheelTo);
        this.line = Optional.empty();
        this.time = time;
    }
    
    /**
     * Создать объект
     * @param wheel объект "Круг, который движется"
     * @param line объект "Линия", с которым происходит столкновение
     * @param time время до столкновения
     */
    public Collision(WheelMoves wheel, Line2D line, double time) {
        this.wheel = wheel;
        this.wheelTo = Optional.empty();
        this.line = Optional.of(line);
        this.time = time;
    }
    
    /**
     * Получить объект "Круг, который движется"
     * @return объект "Круг, который движется"
     */
    public WheelMoves getWheel() {
        return wheel;
    }

    /**
     * Получить объект "Круг, который движется", с которым происходит столкновение
     * @return объект "Круг, который движется", с которым происходит столкновение
     */
    public Optional<WheelMoves> getWheelTo() {
        return wheelTo;
    }

    /**
     * Получить объект "Линия", с которым происходит столкновение
     * @return объект "Линия", с которым происходит столкновение
     */
    public Optional<Line2D> getLine() {
        return line;
    }

    /**
     * @return the time
     */
    public double getTime() {
        return time;
    }

    /**
     * Сравнить с объектом "Столкновение"
     * @param collision объект "Столкновение"
     * @return положительное число если больше, отрицательное если меньше, 0 если равные
     */
    public int compareTo(Collision collision) {
        return Double.compare(this.getTime(), collision.getTime());
    }

    /**
     * Обработать столкновение
     */
    public void process() {
        this.getLine().ifPresent(this.wheel::processCollision);
        this.getWheelTo().ifPresent(this.wheel::processCollision);
    }

}
