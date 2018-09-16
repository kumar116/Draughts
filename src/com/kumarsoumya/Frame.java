package com.kumarsoumya;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Frame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private Panel panel;

    public Frame() {
        setTitle("American Checkers");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(Constant.size);
        initializeMenu();

        panel = new Panel();
        add(panel, BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setVisible(true);
    }

    public void initializeMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem gameStart = new JMenuItem("New Game");
        gameStart.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        gameStart.addActionListener(this);
        fileMenu.add(gameStart);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        undo.addActionListener(this);
        editMenu.add(undo);

        JMenuItem redo = new JMenuItem("Redo");
        redo.addActionListener(this);
        editMenu.add(redo);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        add(menuBar, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionComm = e.getActionCommand();
        Logic logic = panel.getLogic();

        if (actionComm.equals("New Game")) {
            logic.clearSelection();
            logic.clearMoves();
            logic.clearTurns();
            logic.getBoard().setupBoard();
        } else if (actionComm.equals("Undo")) {
            logic.undoMoves();
        } else if (actionComm.equals("Redo")) {
            logic.redoMoves();
        }
        panel.repaint();
    }

}