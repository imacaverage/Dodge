/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Класс "Представление объекта 'Уклонение'"
 * @author iMacAverage
 */
public class DodgeView extends JPanel implements Runnable, KeyListener {
    
    /**
     * размер игрового поля
     */
    private final int size;
    
    /**
     * отступ
     */
    private final int indent;
    
    /**
     * время уровня в секундах
     */
    private static final int TIME_LEVEL = 30;
    
    /**
     * время обновления
     */
    private static final int TIME_UPDATE = 20;
    
    /**
     * количество уровней
     */
    private static final int LEVEL_COUNT = 10;
    
    /**
     * начальная скорость Dodge
     */
    private static final double DODGE_SPEED = 145;

    /**
     * начальный радиус Dodge круга в процентах от размера поля
     */
    private static final double DODGE_SIZE = 3.;
    
    /**
     * процент уменьшения радиуса Dodge
     */
    private static final double DODGE_SIZE_LESS = 2.;
    
    /**
     * процент увеличения скорости Dodge круга
     */
    private static final double DODGE_SPEED_RAISE = 2.5;
    
    /**
     * начальный импульс круга
     */
    private final int impuls;

    /**
     * начальный радиус круга в процентах от размера поля
     */
    private static final double WHEEL_SIZE = 3.5;
    
    /**
     * процент уменьшения радиуса круга
     */
    private static final double WHEEL_SIZE_LESS = 1;
    
    /**
     * количество вершин фигуры
     */
    private static final int[] FIGURE_ROOT_COUNT  = {12, 11, 10, 9, 8, 7, 6, 5, 4, 3};

    /**
     * пассивные круги
     */
    private static final int[] WHEEL_PASSIVE      = {5, 4, 3, 2, 0, 0, 3, 2, 1, 1};
    
    /**
     * активные круги
     */
    private static final int[] WHEEL_ACTIVE       = {0, 1, 2, 3, 3, 2, 2, 3, 3, 1};
    
    /**
     * активные легкие круги
     */
    private static final int[] WHEEL_ACTIVE_LIGHT = {0, 0, 0, 0, 1, 2, 0, 0, 1, 1};

    /**
     * скорость перемещения круга
     */
    private double speed;
    
    /**
     * уровень
     */
    private int level;
        
    /**
     * граница
     */
    private Figure figure;
    
    /**
     * объект "Уклонение"
     */
    private final Dodge dodge;
    
    /**
     * круг Dodge
     */
    private WheelDodge wheelDodge;
    
    /**
     * объект "Множество объектов 'Круг', движущихся внутри объекта 'Фигура'"
     */
    private SetWheelsMovesInFigure setWheels;

    /**
     * нажата клавиша вверх
     */
    private boolean up;

    /**
     * нажата клавиша вниз
     */    
    private boolean down;
    
    /**
     * нажата клавиша влево
     */
    private boolean left;
    
    /**
     * нажата клавиша вправо
     */
    private boolean right;    
    
    /**
     * время на уровень
     */
    private int time;
    
    /**
     * таймер уровня
     */
    private Timer timer;
    
    /**
     * предыдущее время
     */
    private long oldTime;

    /**
     * текущее время
     */
    private long currTime;

    /**
     * признак изменения нажатых клавиш управления
     */
    private boolean changeKey;
    
    /**
     * Создать объект
     * @param dodge объект "Уклонение"
     */
    public DodgeView(Dodge dodge) {
        
        this.dodge = dodge;
        this.level = this.dodge.getLevel();
        this.down = false;
        this.up = false;
        this.left = false;
        this.right = false;
	    this.changeKey = false;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.size = screenSize.height * 3 / 4;
        this.indent = this.size / 60;
        this.impuls = this.size * 90;

        this.setPreferredSize(new Dimension(this.size + this.indent * 2, this.size + this.indent * 2));
        
}
        
    @Override
    public void paint(Graphics g) {
        
        g.setColor(this.getBackground());
        g.clearRect(0, 0, this.indent * 2 + this.size, this.indent * 2 + this.size);
        g.fillRect(0, 0, this.indent * 2 + this.size, this.indent * 2 + this.size);
        
        this.figure.show(g, Color.BLACK);

        // текущее время
        this.currTime = System.currentTimeMillis();

        double time = (this.currTime - this.oldTime) / 1000.;

        if (DodgePause.getInstance().isResult())
            time = 0.;
        
        this.oldTime = this.currTime;

        if (this.changeKey) {
            this.changeKey = false;
            Vector2D speed = new Vector2D(0, 0);
            if (this.isUp())
                speed = speed.sum(new Vector2D(0, -1));
            if (this.isDown()) 
                speed = speed.sum(new Vector2D(0, 1));
            if (this.isLeft())
                speed = speed.sum(new Vector2D(-1, 0));
            if (this.isRight())
                speed = speed.sum(new Vector2D(1, 0));
            this.wheelDodge.setSpeed(speed.getVectorOfLength(this.speed));
        }
        
        this.wheelDodge.move(time);
        this.wheelDodge.refresh(g, this.getBackground());
       
        if (this.setWheels.isCollision(wheelDodge))
            DodgeResult.getInstance().setResult(-1);

        this.setWheels.move(time);
        this.setWheels.refresh(g, this.getBackground());
        
        if (this.setWheels.isCollision(wheelDodge))
            DodgeResult.getInstance().setResult(-1);

    }
    
    /**
     * Задать границу
     */
    private void setFigure() {

        int countRoot = DodgeView.FIGURE_ROOT_COUNT[this.level];
        Point2D point1, point2, center = new Point2D(this.indent + this.size / 2, this.indent + this.size / 2);
        ArrayList<Line2D> lines = new ArrayList<>();
        
        Random rnd = new Random(System.currentTimeMillis());
        
        Vector2D vector = new Vector2D(0, -this.size / 2).rotateClockWise(2 * Math.PI * rnd.nextInt(11) / 10.);
        point1 = center.shift(vector);
        
        for (int i = 0; i < countRoot; i++) {
            vector = vector.rotateClockWise(2 * Math.PI / countRoot);
            point2 = center.shift(vector);
            lines.add(new Line2D(point1, point2.sub(point1)));
            point1 = point2;
        }
        
        this.figure = new Figure(lines);

    }
    
    /**
     * Задать круг
     */
    private void setWheel() {
        double x, y;
        Point2D point;
        x = (this.size + this.indent) / 2;
        y = (this.size + this.indent) / 2;
        point = new Point2D(x, y);
        this.wheelDodge = new WheelDodge(point, (1 - this.level * DodgeView.DODGE_SIZE_LESS / 100.) * this.size * DodgeView.DODGE_SIZE / 100., new Vector2D(0, 0), this.figure);
        this.wheelDodge.setColor(Color.decode("#7CFC00"));

    }
    
    /**
     * Задать коллекцию кругов
     */
    private void setWheels() {
        
        int countPassive = DodgeView.WHEEL_PASSIVE[this.level];
        int countActive = DodgeView.WHEEL_ACTIVE[this.level];
        int countActiveLight = DodgeView.WHEEL_ACTIVE_LIGHT[this.level];
                
        double maxRadius = (1 - this.level * DodgeView.WHEEL_SIZE_LESS / 100.) * this.size * DodgeView.WHEEL_SIZE / 100.;
    
        this.setWheels = new SetWheelsMovesInFigure(this.wheelDodge, this.figure, this.impuls, maxRadius, countPassive, countActive, countActiveLight);
                        
    }
    
    /**
     * Запустить уровень
     */
    private void startLevel() {
        this.setUp(false);
        this.setDown(false);
        this.setLeft(false);
        this.setRight(false);
        this.dodge.showLevel(this.level + 1);
        this.speed = DodgeView.DODGE_SPEED * (1 + this.level * DodgeView.DODGE_SPEED_RAISE / 100.);
        this.setFigure();
        this.setWheel();
        this.setWheels();        
        this.time = DodgeView.TIME_LEVEL;
        this.timer = new Timer();
        DodgeTimer dodgeTimer = new DodgeTimer(this.dodge, this.time);
        timer.schedule(dodgeTimer, 0, 100);
        DodgeResult.getInstance().setResult(1);
        this.oldTime = System.currentTimeMillis();
    }

    @Override
    public void run() {

        while (this.level < DodgeView.LEVEL_COUNT) {
            this.startLevel();
            while (DodgeResult.getInstance().getResult() == 1) {
                repaint();
                try {
                    Thread.sleep(DodgeView.TIME_UPDATE);
                } 
                catch (InterruptedException ex) {}
            }
            if (DodgeResult.getInstance().getResult() == -1) {
                this.timer.cancel();
                this.restart();
            }
            else
                this.nextLevel();
	}    
    }

    /**
     * Получить признак нажата ли клавиша вверх
     * @return признак нажата ли клавиша вверх
     */
    public boolean isUp() {
        return up;
    }

    /**
     * Задать признак нажата ли клавиша вверх
     * @param up признак нажата ли клавиша вверх
     */
    public void setUp(boolean up) {
        if (this.up != up) 
            this.changeKey = true;
        this.up = up;
    }

    /**
     * Получить признак нажата ли клавиша вниз
     * @return признак нажата ли клавиша вниз
     */
    public boolean isDown() {
        return down;
    }

    /**
     * Задать признак нажата ли клавиша вниз
     * @param down признак нажата ли клавиша вниз
     */
    public void setDown(boolean down) {
        if (this.down != down) 
            this.changeKey = true;
        this.down = down;
    }

    /**
     * Получить признак нажата ли клавиша влево
     * @return признак нажата ли клавиша влево
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * Задать признак нажата ли клавиша влево
     * @param left признак нажата ли клавиша влево
     */
    public void setLeft(boolean left) {
        if (this.left != left) 
            this.changeKey = true;
        this.left = left;
    }

    /**
     * Получить признак нажата ли клавиша вправо
     * @return признак нажата ли клавиша вправо
     */
    public boolean isRight() {
        return right;
    }

    /**
     * Задать признак нажата ли клавиша вправо
     * @param right признак нажата ли клавиша вправо
     */
    public void setRight(boolean right) {
        if (this.right != right) 
            this.changeKey = true;
        this.right = right;
    }

    /**
     * Перезапуск уровня
     */
    public void restart() {
        DodgeResult.getInstance().setResult(0);
        JOptionPane.showMessageDialog(null, "Попробуйте еще раз!", "В этот раз не получилось", JOptionPane.ERROR_MESSAGE);
    }
        
    /**
     * Переход на следующий уровень
     */
    public void nextLevel() {
        if (++this.level == DodgeView.LEVEL_COUNT) {
            JOptionPane.showMessageDialog(null, "Это победа!", "Поздравляю!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        DodgeResult.getInstance().setResult(0);
        this.dodge.setLevel(this.level);
        JOptionPane.showMessageDialog(null, "Вы перешли на следующий уровень!", "Поздравляю!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            DodgePause.getInstance().setResult(!DodgePause.getInstance().isResult());
        }
        if (DodgePause.getInstance().isResult())
            return;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                this.setUp(true);
                break;
            case KeyEvent.VK_DOWN:
                this.setDown(true);
                break;
            case KeyEvent.VK_LEFT:
                this.setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                this.setRight(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                this.setUp(false);
                break;
            case KeyEvent.VK_DOWN:
                this.setDown(false);
                break;
            case KeyEvent.VK_LEFT:
                this.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                this.setRight(false);
                break;
        }
    }

}

