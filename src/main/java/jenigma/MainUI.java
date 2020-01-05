package jenigma;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

public class MainUI extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField cryptedMessage = new JTextField();

    private final transient Enigma enigma = new DLBEnigma(Rotor.NONE,Rotor.NONE,Rotor.NONE,Reflector.REFLECTOR_B);
    private KeyLabel[] keys = new KeyLabel[26];
    private UIRotor[] rotors = new UIRotor[3];

    public MainUI() {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, makeLetterPanel());
        add(BorderLayout.NORTH, makeRotorPanel());
        add(BorderLayout.SOUTH, makeInfoPanel());
        setFocusable(true);
        cryptedMessage.setEditable(false);
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (enigmaReady() && e.getID() == KeyEvent.KEY_TYPED) {
                    char c = e.getKeyChar();
                    if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
                        char d = enigma.encode(c);
                        String prev = cryptedMessage.getText();
                        cryptedMessage.setText(prev + d);
                        keys[d - 'A'].hightlight();
                    }
                    for (UIRotor uirotor : rotors) {
                        uirotor.update();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // do nothing
            }
        });
        
    }

    private boolean enigmaReady() {
        if (enigma.getRotor(Position.LEFT) == 0
                || enigma.getRotor(Position.MIDDLE) == 0
                || enigma.getRotor(Position.RIGHT) == 0) {
            JOptionPane.showMessageDialog(null,
                    "Please set up the rotors first!");
            return false;
        }
        return true;
    }

    private JPanel makeLetterPanel() {
        JPanel letterPanel = new JPanel();
        letterPanel.setLayout(new GridLayout(3, 9));
        String letters = "QWERTZUIOASDFGHJKPYXCVBNML";
        char c;
        int j;
        for (int i = 0; i < letters.length(); i++) {
            c = letters.charAt(i);
            j = c - 'A';
            keys[j] = new KeyLabel("" + c);
            letterPanel.add(keys[j]);
        }
        return letterPanel;
    }

    private JPanel makeRotorPanel() {
        JPanel rotorPanel = new JPanel();
        rotorPanel.setLayout(new GridLayout(1, 3));
        JLabel logo = new JLabel(new ImageIcon(
                MainUI.class.getResource("/enigma.jpg")));
        logo.setHorizontalAlignment(SwingConstants.LEFT);
        rotorPanel.add(logo);
        rotors[0] = new UIRotor(enigma, Position.LEFT);
        rotors[1] = new UIRotor(enigma, Position.MIDDLE);
        rotors[2] = new UIRotor(enigma, Position.RIGHT);
        JPanel subRotorPanel = new JPanel();
        for (UIRotor uirotor : rotors) {
            subRotorPanel.add(uirotor);
        }
        rotorPanel.add(subRotorPanel);
        rotorPanel.add(makeRotorList());
        return rotorPanel;
    }

    private JPanel makeRotorList() {
        JPanel listPanel = new JPanel();
        DefaultListModel<RealRotor> model = new DefaultListModel<>();
        for (RealRotor rotor : RealRotor.rotors()) {
            model.addElement(rotor);
        }
        JList<RealRotor> list = new JList<>(model);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setVisibleRowCount(3);
        ListCellRenderer<RealRotor> renderer = (aList, value,
            index, isSelected, cellHasFocus) -> {
                JButton label = new JButton(value.toString() + "("
                        + value.number() + ")");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(100, 40));
                return label;
        };
        list.setCellRenderer(renderer);
        listPanel.add(new JScrollPane(list));
        TransferHandler provideHandler = new TransferHandler() {

            @Override
            public int getSourceActions(JComponent c) {
                return MOVE;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                return ((JList<RealRotor>) c).getSelectedValue();
            }

            @Override
            protected void exportDone(JComponent source, Transferable data,
                    int action) {
                if (action == MOVE) {
                    JList<RealRotor> alist = (JList<RealRotor>) source;
                    ((DefaultListModel) alist.getModel()).removeElement(data);
                }
                requestFocusInWindow();
            }

        };
        list.setTransferHandler(provideHandler);
        MouseListener mouselistener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                JComponent comp = (JComponent) me.getSource();
                TransferHandler handler = comp.getTransferHandler();
                handler.exportAsDrag(comp, me, TransferHandler.MOVE);
            }
        };
        list.addMouseListener(mouselistener);
        return listPanel;
    }

    private JPanel makeInfoPanel() {
        JPanel info = new JPanel();
        info.setLayout(new BorderLayout());
        info.add(BorderLayout.CENTER, cryptedMessage);
        JButton clear = new JButton("clear");
        clear.addActionListener(e -> cryptedMessage.setText(""));
        info.add(BorderLayout.EAST, clear);
        return info;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DUT 2015 Enigma");
        MainUI ui = new MainUI();
        frame.getContentPane().add(BorderLayout.CENTER,ui);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
