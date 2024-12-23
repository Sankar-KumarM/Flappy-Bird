package flappy.bird.java;

import javax.swing.*;


public class MainBird {
    public static void main(String[] args) {
        int boardwidth = 360;
        int boardheight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        //frame.setVisible(true); // To make the frame visible
        frame.setSize(boardwidth,boardheight); // To pass the size of the frame
        frame.setLocationRelativeTo(null); //To place the window at the center
        frame.setResizable(false); // To not make it resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To give the closing operation

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); // To make the color not enter the title bar
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}
