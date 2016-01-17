/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Класс "Фигура"
 * @author iMacAverage
 */
public class Figure {
    
    /**
     * коллекция объектов "Линия"
     */
    private final ArrayList<Line2D> lines;
    
    /**
     * Создать объект
     * @param lines коллекция объектов "Линия"
     */
    public Figure(ArrayList<Line2D> lines) {
        this.lines = lines;
    }
    
    /**
     * Отобразить фигуру
     * @param g объект "Рисование"
     * @param color объект "Цвет"
     */
    public void show(Graphics g, Color color) {
        g.setColor(color);
        this.lines.forEach(line -> line.show(g, color));
    }

    /**
     * Получить поток объектов "Линия"
     * @return поток объектов "Линия"
     */
    public Stream<Line2D> getLines() {
        return lines.stream();
    }
    
}
