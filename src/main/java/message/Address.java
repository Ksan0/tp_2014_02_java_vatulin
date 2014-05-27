package message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * oppa google style
 */
public class Address {
    static private AtomicInteger abonentIDCreator = new AtomicInteger();
    private int abonentID;

    public Address(){
        this.abonentID = abonentIDCreator.incrementAndGet();
    }

    public int getAddress(){
        return this.abonentID;
    }
}
