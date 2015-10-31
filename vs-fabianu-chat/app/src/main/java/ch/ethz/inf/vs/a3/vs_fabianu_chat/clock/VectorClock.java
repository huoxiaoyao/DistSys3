package ch.ethz.inf.vs.a3.vs_fabianu_chat.clock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Caroline on 10/31/15.
 */
public class VectorClock implements ch.ethz.inf.vs.a3.vs_fabianu_chat.clock.Clock{

    private Map<Integer, Integer> vector;

    /**
     * Update the current clock with a new one, taking into
     * account the values of the incoming clock.
     * <p/>
     * E.g. for vector clocks, c1 = [2 1 0], c2 = [1 2 0],
     * the c1.update(c2) will lead to [2 2 0].
     *
     * @param other
     */
    @Override
    public void update(Clock other) {

        Map<Integer, Integer> otherVector = vector;

        Iterator it = otherVector.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            int pid = (int) pair.getKey();
            int otherval = (int) pair.getValue();

            if (vector.containsKey(pid)) {
                int val = vector.get(pid);
                if (otherval > val) {
                    vector.put(pid, otherval);
                }
            } else {
                addProcess(pid, otherval);
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

    }


    /**
     * Change the current clock with a new one, overwriting the
     * old values.
     *
     * @param other
     */
    @Override
    public void setClock(Clock other) {

        setClockFromString(other.toString());
    }

    /**
     * Tick a clock given the process id.
     * <p/>
     * For Lamport timestamps, since there is only one logical time,
     * the method can be called with the "null" parameter. (e.g.
     * clock.tick(null).
     *
     * @param pid
     */
    @Override
    public void tick(Integer pid) {

        int value = getTime(pid);
        vector.put(pid, value + 1);
    }

    /**
     * Check whether a clock has happened before another one.
     *
     * @param other
     * @return True if a clock has happened before, false otherwise.
     */
    @Override
    public boolean happenedBefore(Clock other) {

        if (other.equals(this)) return false;

        VectorClock otherClock = (VectorClock) other;
        Map<Integer, Integer> otherVector = vector;

        Iterator it = vector.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            int pid = (int) pair.getKey();
            int val = (int) pair.getValue();

            if (otherVector.containsKey(pid)) {
                int otherVal = otherVector.get(pid);
                if (val > otherVal) {
                    return false;
                }
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

        return true;

    }




    /**
     * toString
     *
     * @return String representation of the clock.
     */
    public String toString(){

        StringBuilder stringBuilder = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<Integer, Integer> entry : vector.entrySet()) {

            if (!first) stringBuilder.append(",");
            else first = false;

            stringBuilder.append("\"");
            stringBuilder.append(entry.getKey());
            stringBuilder.append("\"");
            stringBuilder.append(":");
            stringBuilder.append(String.valueOf(entry.getValue()));

        }

        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * Set a clock given it's string representation.
     *
     * @param clock
     */
    @Override
    public void setClockFromString(String clock) {

        String withoutWhiteSpace = clock.replaceAll("\\s+", "");

        String regex = "\\{((\"[0-9]+\":[0-9]+)(,\"[0-9]+\":[0-9]+)*)?\\}";

        if (withoutWhiteSpace.matches(regex)) {

            vector = new HashMap<Integer, Integer>();
            String cleanClockString = withoutWhiteSpace.replace("{", "").replace("}", "").replace("\"", "");

            if (cleanClockString.length() > 2) {

                String[] kvPairs = cleanClockString.split(",");

                for (String kvPair : kvPairs) {
                    String[] kv = kvPair.split(":");
                    String key = kv[0];
                    String value = kv[1];

                    addProcess(Integer.valueOf(key), Integer.valueOf(value));

                }
            }
        }

    }

    public int getTime(Integer pid){
        if (vector.containsKey(pid)) {
            return vector.get(pid);
        }
        else {
            return 0;
        }
        //l return the current clock for the given process id
    }

    public void addProcess(Integer pid, int time){
        //adds a new process and its vector clock to the current clock
        vector.put(pid, time);
    }
}
