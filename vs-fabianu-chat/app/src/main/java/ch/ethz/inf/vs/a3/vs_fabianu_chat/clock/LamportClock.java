package ch.ethz.inf.vs.a3.vs_fabianu_chat.clock;

/**
 * Created by Caroline on 10/31/15.
 */
public class LamportClock implements ch.ethz.inf.vs.a3.vs_fabianu_chat.clock.Clock{

    private int time;

    /**
     * Update the current clock with a new one, taking into
     * account the values of the incoming clock.
     *
     * E.g. for vector clocks, c1 = [2 1 0], c2 = [1 2 0],
     * the c1.update(c2) will lead to [2 2 0].
     * @param other
     */
    @Override
    public void update(Clock other) {

        if (happenedBefore(other)) {
            setClock(other);
        }
    }

    /**
     * Change the current clock with a new one, overwriting the
     * old values.
     * @param other
     */
    @Override
    public void setClock(Clock other) {

        if (other instanceof LamportClock) {

            LamportClock otherL = (LamportClock) other;

            this.time = otherL.getTime();

        }
    }

    /**
     * Tick a clock given the process id.
     *
     * For Lamport timestamps, since there is only one logical time,
     * the method can be called with the "null" parameter. (e.g.
     * clock.tick(null).
     * @param pid
     */
    @Override
    public void tick(Integer pid) {
        //call with clock.tick(null);
        time ++;
    }

    /**
     * Check whether a clock has happened before another one.
     *
     * @param other
     * @return True if a clock has happened before, false otherwise.
     */
    @Override
    public boolean happenedBefore(Clock other) {
        LamportClock lamportClock = (LamportClock) other;
        if(time < lamportClock.getTime()){
            return true;
        }
        else
            return false;
    }

    /**
     * toString
     *
     * @return String representation of the clock.
     */
    public String toString(){

        String stringValue;

        stringValue = String.valueOf(this.getTime());
        return stringValue;
    }

    /**
     * Set a clock given it's string representation.
     *
     * @param clock
     */
    @Override
    public void setClockFromString(String clock) {
        //20:15
        int stringTime;
        stringTime = Integer.valueOf(clock);
        time = stringTime;
    }

    public void setTime(int time){
        //overrides the current clock value with the one provided as input.
        this.time = time;
    }

    public int getTime(){
        //returns the current clock value
        return time;
    }
}
