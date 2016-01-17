/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

/**
 * Синглтон "Результат уровня"
 * @author iMacAverage
 */
public class DodgeResult {
    
    /**
     * объект "Результат уровня"
     */
    private static DodgeResult instance;
    
    /**
     * состояние
     */
    private int result;
    
    /**
     * Создать объект
     */
    private DodgeResult() {
        result = 0;
    }
    
    /**
     * Получить объект "Результат уровня"
     * @return объект "Результат уровня"
     */
    public static synchronized DodgeResult getInstance() {
        if (instance == null)
            instance = new DodgeResult();
        return instance;        
    }

    /**
     * Получить состояние
     * @return состояние
     */
    public synchronized int getResult() {
        return result;
    }

    /**
     * Задать состояние
     * @param result состояние
     */
    public synchronized void setResult(int result) {
        this.result = result;
    }
    
}
