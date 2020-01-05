package jenigma;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class KeyLabel extends JLabel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private transient Timer timer = new Timer(); 
    
    public KeyLabel(String str) {
        super(str);
        setOpaque(true);
        setBackground(Color.WHITE);
        setFont(getFont().deriveFont(32f));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(150, 150));
    }
    
    public void hightlight() {
        setBackground(Color.YELLOW);
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                setBackground(Color.WHITE);
            }
        }, 1000);
    }
}
