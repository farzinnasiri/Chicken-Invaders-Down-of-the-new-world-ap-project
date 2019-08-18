package ir.farzinnasiri.Client;


import ir.farzinnasiri.Client.stateMachine.StateMachine;
import ir.farzinnasiri.Utils.Constants;

import java.awt.*;
import java.awt.image.BufferStrategy;


public class Client extends Canvas implements Runnable {


    private boolean running;
    private static StateMachine stateMachine;
    private Thread thread;


    Client() {


        setSize(Constants.WIDTH, Constants.HEIGHT);
        setFocusable(true);
        stateMachine = new StateMachine(this);


    }

     void start(){
        if(running){
            return;
        }
        running = true;
        thread = new Thread(this,"Client Thread");
        thread.start();


    }
    public  void stop(){
        if(!running){
            return;
        }
        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (true) {
            long timer = System.currentTimeMillis();
            long lastLoopTime = System.nanoTime();
            final int Target_fps = 60;
            /*we need 1/fps to determine the time for each loop,for 60fps this is approximately 16ms
             * if all the updates take place under 16ms than we have a steady fps!
             *
             * */
            final long Optimal_Time = 1_000_000_000 / Target_fps;
            int frames = 0;

            //setting buffer strategy
            createBufferStrategy(3);
            BufferStrategy bs = getBufferStrategy();

            while (true) {
                long now = System.nanoTime();
                long updateLength = now - lastLoopTime;
                lastLoopTime = now;

                //to not jump and run smooth
                double elapsed = updateLength / ((double) Optimal_Time);
                //each loop adds one frame
                frames++;
                //check if one second has passed or not
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    frames = 0;
                }

                render(bs);
                update(elapsed);


                //this part controls the program if it gets very fast
                try {
                    Thread.sleep(Math.abs((lastLoopTime - System.nanoTime()) + Optimal_Time) / 1_000_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    private void render(BufferStrategy bs){
        do {
            do {
                Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                stateMachine.draw(g2d);

                g2d.dispose();

            } while (bs.contentsRestored());
            bs.show();
        }while (bs.contentsLost());
    }

    public void update(double elapsed){
        //game updates here
        stateMachine.update(elapsed);
    }


}
