package com.kumarsoumya;

import javax.swing.JFrame;

public class Frame extends JFrame {

    private static final long serialVersionUID = 1L;

    public Frame() {
        setTitle("American Checkers");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(Constant.size);

        setResizable(false);
        setVisible(true);
    }

}