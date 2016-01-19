/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Класс "Квадратное уравнение"
 * @author iMacAverage
 */
public class Quadratic {

    /**
     * коэффициент при второй степени
     */
    private final double a; 
    
    /**
     * коэффициент при первой степени
     */
    private final double b; 
    
    /**
     * коэффициент при нулевой степени
     */
    private final double c;
    
    /**
     * дискриминант
     */
    private final double d;
    
    /**
     * Создать объект
     * @param a коэффициент при второй степени
     * @param b коэффициент при первой степени
     * @param c коэффициент при нулевой степени 
     */
    public Quadratic(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = Math.pow(this.b, 2) - 4 * this.a * this.c;
    }

    /**
     * Получить поток корней уравнения
     * @return поток корней уравнения
     */
    public Stream<Double> getRoots() {
        ArrayList<Double> roots = new ArrayList<>();
        if (this.a == 0 && this.b != 0)
            roots.add(-this.c / this.b);
        if (this.a != 0 && this.d >= 0)
            roots.add((-this.b + Math.sqrt(this.d)) / (2 * this.a));
        if (this.a != 0 && this.d > 0)
            roots.add((-this.b - Math.sqrt(this.d)) / (2 * this.a));
        return roots.stream();
    }
    
}
