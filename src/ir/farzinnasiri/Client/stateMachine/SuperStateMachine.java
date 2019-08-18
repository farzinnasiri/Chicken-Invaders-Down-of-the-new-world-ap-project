package ir.farzinnasiri.Client.stateMachine;

import ir.farzinnasiri.Client.ClientConnectionHandler;

import java.awt.*;

public abstract class SuperStateMachine {

    private StateMachine stateMachine;

    public void setState(int state) {
        stateMachine.setState(state);
    }
    public void pause(ClientConnectionHandler clientConnectionHandler){
        stateMachine.pause(clientConnectionHandler);

    }
    public void startGameScreen( ClientConnectionHandler clientConnectionHandler){stateMachine.
            startGameScreen(clientConnectionHandler);}

    public SuperStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public abstract void update(double delta);
    public abstract void draw(Graphics2D g2d);
    public abstract void init(Canvas canvas);

    public StateMachine getStateMachine() {
        return stateMachine;
    }



}
