package flappy.bird.java;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;  // To create a array for the pipes
import java.util.Random; //To place the pipe at the random places
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image backImg;
    Image birdImg;
    Image toppipe;
    Image bottompipe;

    //Bird
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;


    class Bird{
        int x =birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

    //pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;    //scales by 1/6
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y= pipeY;
        int height = pipeHeight;
        int width = pipeWidth;
        Image  img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }

    //Game logic
    Bird bird;
    int velocityX = -4; //move pipes to the left speed (simulates bird moving right)
    int velocityY = 0; //move bird up/down speed
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //To load the images
        backImg  = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        toppipe = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottompipe = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //Place Pipe Timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        placePipesTimer.start();

        //game Timer
        gameLoop = new Timer(1400/60,this); //1000/60  = 16.6
        gameLoop.start();
    }

    public void placePipes(){

        //(0-1) * pipeHeight/2 -> (0-256)
        //128
        //0-128-(0-256) --> pipeHeight/4 -> 3/4th of PipeHeight
        int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;

        Pipe topPipe = new Pipe(toppipe);
        topPipe.y=randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottompipe);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent (Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        //backGround
        g.drawImage(backImg,0,0,this.boardWidth,this.boardHeight,null);

        //bird
        g.drawImage(birdImg,bird.x,bird.y,bird.width,bird.height,null);

        //pipes
        for(int i=0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
        }

        //score
        g.setColor(Color.white);

        g.setFont(new Font("Arial",Font.PLAIN,32));
        if (gameOver){
            g.drawString("Game Over: "+ String.valueOf((int) score), 10,35);
        }
        else{
            g.drawString(String.valueOf((int)score),10,35);
        }

    }

    public void move(){
        //bird
        velocityY+=gravity;
        bird.y +=velocityY;
        bird.y = Math.max(bird.y,0);

        //pipes
        for (int i =0; i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x+=velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width){
                score+=0.5; // 0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                pipe.passed=true;
            }

            if (collision(bird, pipe)){
                gameOver = true;
            }
        }
        if(bird.y>boardHeight){
            gameOver = true;
        }
    }

    public boolean collision(Bird a,Pipe b){
        return  a.x < b.x + b.width &&  // a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&  // a's top right corner passes b's top left corner
                a.y < b.y + b.height&&  // a's top left corner doesn't reach b's bottom right corner
                a.y + a.height > b.y;   // a's bottom left corner passes b's top left corner
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_UP);{
            velocityY = -9;

            if (gameOver){
                //Restart the game by resetting the conditions
                bird.y = birdY;
                velocityY =0;
                pipes.clear();
                gameOver =false;
                score = 0;
                gameLoop.start();
                placePipesTimer.start();

            }
        }

    }
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}
