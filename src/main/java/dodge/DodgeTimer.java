/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.util.TimerTask;

/**
 *
 * @author iMacAverage
 */
public class DodgeTimer extends TimerTask {

    /**
     * объект "Dodge"
     */
    private final Dodge dodge;
    
    /**
     * время уровня
     */
    private final int time;
    
    /**
     * потрачено времени
     */
    private long lostTime;

    /**
     * начало измерения
     */
    private long startTime;

    /**
     * Создать объект
     * @param dodge
     * @param time
     */
    public DodgeTimer(Dodge dodge, int time) {
        this.dodge = dodge;
        this.time = time;
        this.lostTime = 0;
        this.startTime = System.currentTimeMillis(); 
    }
    
    @Override
    public void run() {

        long curTime = System.currentTimeMillis(); 
        
        if (!DodgePause.getInstance().isResult())
            this.lostTime += (curTime - this.startTime);
        
        this.startTime = curTime;

        int time = this.time - (int) (this.lostTime / 1000);                
    
        if(time <= 0)
            time = 0;
        
        this.dodge.showTime(time);
        
        if (time == 0) {
            this.cancel();
            DodgeResult.getInstance().setResult(2);
        }
    
    }
    
}
