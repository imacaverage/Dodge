/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodge;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.*;
import java.util.Optional;
import java.util.Properties;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Класс "Уклонение"
 * @author iMacAverage
 */
public class Dodge extends JFrame {
    
    /**
     * объект "Представление Dodge"
     */
    private final DodgeView dodgeView;
    
    /**
     * номер уровня
     */
    private final JLabel jLevel;

    /**
     * оставшееся время уровня
     */
    private final JLabel jTime;
        
    /**
     * Создать объект
     */
    public Dodge() {                
        
        this.dodgeView = new DodgeView(this);
        
        JPanel jMain = new JPanel();
                
        jMain.setLayout(new BoxLayout(jMain, BoxLayout.Y_AXIS));
        
        jTime = new JLabel(); 
        jLevel = new JLabel("1"); 

        JPanel jGrid = new JPanel(new GridLayout(1, 4, 5, 0));
        jGrid.add(new JLabel("Уровень: "));
        jGrid.add(jLevel);
        jGrid.add(new JLabel("Время: "));        
        jGrid.add(jTime);
       
        jTime.setFont(this.jTime.getFont().deriveFont(Font.BOLD)); 
        jLevel.setFont(this.jLevel.getFont().deriveFont(Font.BOLD)); 
        
        JPanel jFlow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jFlow.add(jGrid);

        jMain.add(jFlow);

        JPanel jGridBag = new JPanel();
        jGridBag.setLayout(new GridBagLayout());

        GridBagConstraints gbCon = new GridBagConstraints();

        gbCon.gridx = 0;    // столбец
        gbCon.gridy = 0;    // ряд
        gbCon.anchor = GridBagConstraints.CENTER;
        gbCon.fill = GridBagConstraints.NONE;   
        gbCon.weightx = 1;  // для соотношения ячеек
        gbCon.weighty = 1;
        gbCon.gridwidth = 1;
        gbCon.gridheight = 1;
        gbCon.insets = new Insets(5, 5, 5, 5);
        
        jGridBag.add(this.dodgeView);

        jMain.add(jGridBag);
        
        this.getContentPane().add(jMain);
        
        jMain.addKeyListener(this.dodgeView);
        jMain.setFocusable(true);
        jMain.requestFocusInWindow();
                
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);   
    }
    
    /**
     * Преобразовать время (секунд) в строку для вывода на экран
     * @param time время (секунд)
     * @return строка для вывода на экран
     */
    private String viewTime(int time) {
        return String.format("%1d", time / 60) + ":" + String.format("%02d", time % 60);
    }
    
    /**
     * Вывести время уровня
     * @param time время уровня
     */
    public void showTime(int time) {
        this.jTime.setText(this.viewTime(time));
    }
    
    /**
     * Вывести номер уроня
     * @param level номер уровня
     */
    public void showLevel(int level) {
        this.jLevel.setText(level + "");
    }
    
    /**
     * Отобразить окно
     */
    public void display() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Получить объект ini файла
     * @return объект ini файла
     */
    private Optional<File> getIniFile() {
        File file = null;
        File currentDir = new File(".");
        try {
            String filePath = currentDir.getCanonicalPath() + System.getProperty("file.separator") + "Dodge.ini";
            file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(file);
    }

    /**
     * Получить уровень
     * @return уровень
     */
    public int getLevel() {
        int level = 0;
        Optional<File> file = this.getIniFile();
        if (!file.isPresent())
            return level;
        try {
            FileInputStream fileStream = new FileInputStream(file.get());
            Properties ini = new Properties();
            ini.load(fileStream);
            level = Integer.parseInt(ini.getProperty("level", "0"));
            fileStream.close();
        }
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        return level;
    }

    /**
     * Сохранить уровень
     * @param level уровень
     */
    public void setLevel(int level) {
        Optional<File> file = this.getIniFile();
        if (!file.isPresent())
            return;
        try {
            FileOutputStream fileStream = new FileOutputStream(file.get());
            Properties ini = new Properties();
            ini.setProperty("level", String.valueOf(level));
            ini.store(fileStream, "current level");
            fileStream.close();
        }
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(
            
            new Runnable() {

                @Override
                public void run() {     
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    DodgePause.getInstance().setResult(true);
                    Dodge dodge = new Dodge();
                    new Thread(dodge.dodgeView).start();   
                    dodge.display();
                    JOptionPane.showMessageDialog(null, "Добро пожаловать!", "Игра Dodge", JOptionPane.INFORMATION_MESSAGE);
                    DodgePause.getInstance().setResult(false);
                }
            
            }
        
        );
    }

}
