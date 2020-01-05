package jenigma;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.datatransfer.Transferable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

public class UIRotor extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JLabel letterl = new JLabel("-");
    private JLabel rotorl = new JLabel("-");
    private transient Enigma enigma;
    private Position pos;

    public UIRotor(Enigma enigma, Position pos) {
        this.enigma = enigma;
        this.pos = pos;
        letterl.setText("" + enigma.getRotorLetter(pos));
        letterl.setHorizontalAlignment(SwingConstants.CENTER);
        letterl.setVerticalAlignment(SwingConstants.CENTER);
        letterl.setBackground(Color.WHITE);
        letterl.setOpaque(true);
        letterl.setFont(letterl.getFont().deriveFont(18f));
        rotorl.setText("" + enigma.getRotor(pos));
        rotorl.setHorizontalAlignment(SwingConstants.CENTER);
        rotorl.setVerticalAlignment(SwingConstants.CENTER);
        setLayout(new GridLayout(4, 1));
        JButton plus = new JButton("+");
        plus.addActionListener(e -> plus());
        JButton minus = new JButton("-");
        minus.addActionListener(e -> minus());
        add(plus);
        add(letterl);
        add(minus);
        add(rotorl);
        TransferHandler labelHandler = new TransferHandler() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(RealRotor.ROTOR_FLAVOR);
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (support.isDrop()) {
                    try {
                        int rotorNumber = (Integer) support.getTransferable().getTransferData(RealRotor.ROTOR_FLAVOR);
                        enigma.setRotor(pos, rotorNumber);
                        letterl.setText("" + enigma.getRotorLetter(pos));
                        rotorl.setText("" + enigma.getRotor(pos));
                        return true;
                    } catch (Exception e) {
                        return false;
                    }                
                } else {
                    return false;
                }
            }
        };
        setTransferHandler(labelHandler);
    }

    private void plus() {
        char current = enigma.getRotorLetter(pos);
        enigma.moveRotorToLetter(pos, next(current));
        letterl.setText("" + enigma.getRotorLetter(pos));
    }

    private char next(char current) {
        if (current == 'Z') {
            return 'A';
        }
        return ++current;
    }
    
    private void minus() {
        char current = enigma.getRotorLetter(pos);
        enigma.moveRotorToLetter(pos, prev(current));
        letterl.setText("" + enigma.getRotorLetter(pos));
    }
    private char prev(char current) {
        if (current == 'A') {
            return 'Z';
        }
        return --current;
    }
    
    public void update() {
        letterl.setText("" + enigma.getRotorLetter(pos));
    }
}
