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

public class MainUI extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField cryptedMessage = new JTextField();

    private final Enigma enigma = new DLBEnigma(Rotor.NONE,Rotor.NONE,Rotor.NONE,Reflector.REFLECTOR_B);
    private KeyLabel[] keys = new KeyLabel[26];
    private UIRotor[] rotors = new UIRotor[3];

    public MainUI() {
        super("DUT 2015 Enigma");

        getContentPane().add(BorderLayout.CENTER, makeLetterPanel());
        getContentPane().add(BorderLayout.NORTH, makeRotorPanel());
        // getContentPane().add(BorderLayout.EAST, makeRotorList());
        getContentPane().add(BorderLayout.SOUTH, makeInfoPanel());
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
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
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
        JPanel subRotorPanel = new JPanel();
        subRotorPanel.add(rotors[0] = new UIRotor(enigma, Position.LEFT));
        subRotorPanel.add(rotors[1] = new UIRotor(enigma, Position.MIDDLE));
        subRotorPanel.add(rotors[2] = new UIRotor(enigma, Position.RIGHT));
        rotorPanel.add(subRotorPanel);
        rotorPanel.add(makeRotorList());
        return rotorPanel;
    }

    private JPanel makeRotorList() {
        JPanel listPanel = new JPanel();
        // listPanel.setLayout(new GridLayout(1,1));
        DefaultListModel<RealRotor> model = new DefaultListModel<RealRotor>();
        for (RealRotor rotor : RealRotor.rotors()) {
            model.addElement(rotor);
        }
        JList<RealRotor> list = new JList<RealRotor>(model);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setVisibleRowCount(3);
        ListCellRenderer<RealRotor> renderer = new ListCellRenderer<RealRotor>() {

            @Override
            public Component getListCellRendererComponent(
                    JList<? extends RealRotor> list, RealRotor value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JButton label = new JButton(value.toString() + "("
                        + value.number() + ")");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(100, 40));
                return label;
            }

        };
        list.setCellRenderer(renderer);
        // list.setPreferredSize(new Dimension(200,200));
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
        MainUI ui = new MainUI();
    }

}
