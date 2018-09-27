package com.kumarsoumya;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Frame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private HashMap<Logic, Integer> sessions = new HashMap<Logic, Integer>();
    private HashMap<String, JMenuItem> sessionItems = new HashMap<String, JMenuItem>();
    private Panel panel;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu sessionMenu = new JMenu("Sessions");

    public Frame() {
        setTitle("Draughts");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(Constant.size);

        panel = new Panel();
        add(panel, BorderLayout.SOUTH);

        initializeMenu();

        pack();
        setResizable(false);
        setVisible(true);
    }

    public void setupSessionMenu() {
        menuBar.repaint();
    }

    private void initializeMenuItem(JMenu menu, String[] menuItemNames) {
        for (String menuItemName : menuItemNames) {
            JMenuItem subMenuItem = new JMenuItem(menuItemName);
            subMenuItem.addActionListener(this);

            switch (menuItemName) {
                case "New Game":
                    subMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
                    break;
                case "Save Game":
                    subMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
                    break;
                case "Undo":
                    subMenuItem.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
                    break;
                case "Exit":
                    subMenuItem.setAccelerator(KeyStroke.getKeyStroke('s', KeyEvent.ALT_MASK));
                    break;
                case "Size":
                    String[] subMenuItemNames = {"8", "12", "16"};
                    JMenu subMenu = new JMenu(menuItemName);
                    menu.add(subMenu);
                    initializeMenuItem(subMenu, subMenuItemNames);
                default:
                    break;
            }
            if (menuItemName != "Size") {
                menu.add(subMenuItem);
            }
        }
    }

    public void initializeMenu() {
        String[] menuNames = {"File", "Edit", "Settings"};
        String[] menuItemNames;
        for (String menuName : menuNames) {
            switch (menuName) {
                case "File":
                    menuItemNames = "New Game,Reset,Save Game,Load Game,Resign,Exit".split(",");
                    break;
                case "Edit":
                    menuItemNames = "Redo,Undo".split(",");
                    break;
                case "Settings":
                    menuItemNames = new String[] {"Size"};
                    break;
                default:
                    menuItemNames = new String[] { };
                    break;
            }
            JMenu menu = new JMenu(menuName);
            initializeMenuItem(menu, menuItemNames);
            menuBar.add(menu);
        }

        menuBar.add(sessionMenu);

        add(menuBar, BorderLayout.NORTH);
    }

    private void removeSession(Logic logic) {
        sessions.remove(logic);
        sessionMenu.remove(sessionItems.get(logic.toString()));
    }

    private void saveSession() {
        Logic currLogic = panel.getLogic();
        sessions.put(currLogic, Constant.ranks);

        JMenuItem sessionItem = new JMenuItem(currLogic.toString());
        sessionItems.put(currLogic.toString(), sessionItem);
        sessionItem.addActionListener(this);
        sessionMenu.add(sessionItem);
    }

    private void prepareSession(int multiple) {
        saveSession();
        Constant.ranks = Constant.files = multiple;
    }

    private void setSession(Logic logic) {
        panel.setLogic(logic);
        setupSessionMenu();
    }

    public void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          Logic logic = panel.getLogic();
          try {
              ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file));
              outStream.writeObject(logic.getUndoMoves());
              outStream.writeObject(logic.getRedoMoves());
              outStream.writeObject(logic.getRemovedPieces());
              outStream.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
        }
    }

    @SuppressWarnings("unchecked")
    public void loadGame() {
        JFileChooser fileChooser=new JFileChooser();
        if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Logic logic = panel.getLogic();
            try {
                ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(file));
                logic.setUndoMoves((LinkedList<LinkedList<Point>>) inStream.readObject());
                logic.setRedoMoves((LinkedList<LinkedList<Point>>) inStream.readObject());
                logic.setRemovedPieces((Stack<Piece>) inStream.readObject());
                inStream.close();

                LinkedList<LinkedList<Point>> diagonal = logic.getUndoMoves();
                for (int i = 0; i < diagonal.size(); i++) {
                    LinkedList<Point> moves = diagonal.get(i);
                    logic.playMove(moves, false, false);
                }
                panel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logic logic = panel.getLogic();

        String actionComm = e.getActionCommand();
        if (actionComm.equals("New Game")) {
            prepareSession(Constant.ranks);
            setSession(new Logic());
        } else if (actionComm.equals("Reset")) {
            logic.clearSelection();
            logic.clearMoves();
            logic.clearTurns();
            logic.getBoard().setupBoard();
        } else if (actionComm.equals("Save Game")) {
            saveGame();
        } else if (actionComm.equals("Load Game")) {
            loadGame();
        } else if (actionComm.equals("Resign")) {
            logic.setGameOver(true);
        } else if (actionComm.equals("Exit")) {
            dispose();
        } else if (actionComm.equals("Undo")) {
            logic.undoMoves();
        } else if (actionComm.equals("Redo")) {
            logic.redoMoves();
        } else if (actionComm.equals("8") || actionComm.equals("12") || actionComm.equals("16")) {
            int multiple = Integer.valueOf(actionComm);
            prepareSession(multiple);
            setSession(new Logic());
        } else {
            Logic useLogic = null;
            Set<Logic> logics = sessions.keySet();
            for (Logic storedLogic: logics) {
                if (actionComm.equals(storedLogic.toString())) {
                    useLogic = storedLogic;
                }
            }
            if (useLogic != null) {
                int multiple = sessions.get(useLogic);
                removeSession(useLogic);
                prepareSession(multiple);
                setSession(useLogic);
            }
        }

        panel.repaint();
    }

}