package ir.farzinnasiri.Utils;

public class Timer {
    private long previouseTime;

    public Timer(){
        this.previouseTime = System.currentTimeMillis();
    }

    public void setPreviousTime(long time) {
        this.previouseTime = time;
    }

    public long getPreviousTime() {
        return previouseTime;
    }

    public void resetTimer(){
        setPreviousTime(System.currentTimeMillis());
    }

    public boolean timeEvent(int timer){
        if(isTimerReady(timer)){
            resetTimer();
            return true;
        }else {
            return false;
        }
    }
    public boolean isTimerReady(int timer){
        if(System.currentTimeMillis() - getPreviousTime() > timer){
            return true;
        }
        return false;
    }
}
