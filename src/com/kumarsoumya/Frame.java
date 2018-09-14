package com.kumarsoumya;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Frame extends JFrame {

    private static final long serialVersionUID = 1L;
    private Panel panel;

    public Frame() {
        setTitle("American Checkers");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(Constant.size);

        panel = new Panel();
        add(panel, BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setVisible(true);
    }

}