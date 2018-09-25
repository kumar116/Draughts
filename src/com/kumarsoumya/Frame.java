package com.kumarsoumya;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JFileChooser;
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

        JMenuItem saveGame = new JMenuItem("Save Game");
        saveGame.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        saveGame.addActionListener(this);
        fileMenu.add(saveGame);

        JMenuItem loadGame = new JMenuItem("Load Game");
        loadGame.addActionListener(this);
        fileMenu.add(loadGame);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        add(menuBar, BorderLayout.NORTH);
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
        String actionComm = e.getActionCommand();
        Logic logic = panel.getLogic();

        if (actionComm.equals("New Game")) {
            logic.clearSelection();
            logic.clearMoves();
            logic.clearTurns();
            logic.getBoard().setupBoard();
        } else if (actionComm.equals("Save Game")) {
            saveGame();
        } else if (actionComm.equals("Load Game")) {
            loadGame();
        } else if (actionComm.equals("Undo")) {
            logic.undoMoves();
        } else if (actionComm.equals("Redo")) {
            logic.redoMoves();
        }
        panel.repaint();
    }

}