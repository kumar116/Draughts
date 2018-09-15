package com.kumarsoumya;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Panel extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;

    private Logic game = new Logic();

    public Panel() {
        addMouseListener(this);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return Constant.size;
    }

    public Logic getLogic() {
        return game;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Board board = game.getBoard();
        for (int rank = 0; rank < Constant.ranks; rank++) {
            for (int file = 0; file < Constant.files; file++) {
                Tile tile = board.getTile(rank, file);

                Color saveColor = g.getColor();
                g.setColor(tile.getColor());
                g.fillRect(file * tile.getWidth(), rank * tile.getHeight(),
                        tile.getWidth(), tile.getHeight());

                if (tile.isOccupied()) {
                    Piece piece = tile.getPiece();
                    g.setColor(piece.getColor());
                    g.fillOval(file * piece.getWidth(), rank * piece.getHeight(),
                            piece.getWidth(), piece.getHeight());
                }

                if (tile.isSelected()) {
                    g.setColor(Color.WHITE);
                    g.drawRect(file * tile.getWidth(), rank * tile.getHeight(), tile.getWidth() - 1, tile.getHeight() - 1);
                }

                g.setColor(saveColor);
            }
        }
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public void mouseClicked(MouseEvent e) {
        int rank = game.getRank(e.getY());
        int file = game.getFile(e.getX());

        game.clearSelection();
        game.clearMoves();
        game.selectMoves(rank, file);

        repaint();
    }

}