package ir.farzinnasiri.Server;


public class Server implements Runnable {
    private GameState gameState;


    public Server() {
        gameState = GameState.getInstance();

    }

    @Override
    public void run() {

        while (true) {
            long timer = System.currentTimeMillis();
            long lastLoopTime = System.nanoTime();
            final int Target_fps = 100;
            /*we need 1/fps to determine the time for each loop,for 60fps this is approximately 16ms
             * if all the updates take place under 16ms than we have a steady fps!
             *
             * */
            final long Optimal_Time = 1_000_000_000 / Target_fps;
            int frames = 0;


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

                update(elapsed);
                if(gameState.isGameFinished()){
                    break;
                }


                //this part controls the program if it gets very fast
                try {
                    Thread.sleep(Math.abs((lastLoopTime - System.nanoTime()) + Optimal_Time) / 1_000_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void update(double elapsed) {

        if (gameState.isPlaying() ) {
            gameState.update(elapsed);

        }

    }


}
