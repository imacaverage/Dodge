/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

/**
 * Синглтон "Пауза"
 * @author iMacAverage
 */
public class DodgePause {
    
    /**
     * объект "Пауза"
     */
    private static DodgePause instance;
    
    /**
     * состояние 
     */
    private boolean result;
    
    /**
     * Создать объект
     */
    private DodgePause() {
        result = false;
    }
    
    /**
     * Получить объект "Пауза"
     * @return объект "Пауза"
     */
    public static synchronized DodgePause getInstance() {
        if (instance == null)
            instance = new DodgePause();
        return instance;        
    }

    /**
     * Проверить состояние паузы
     * @return true в случае успеха, иначе false
     */
    public synchronized boolean isResult() {
        return result;
    }

    /**
     * Задать состояние
     * @param result состояние
     */
    public synchronized void setResult(boolean result) {
        this.result = result;
    }
   
}
