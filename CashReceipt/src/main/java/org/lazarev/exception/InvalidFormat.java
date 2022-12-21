package org.lazarev.exception;

public class InvalidFormat extends Exception{
    public InvalidFormat() {
        super("Invalid format.Please use the following format 'item id'-'item weight'space'card number'." +
                " The list of goods can be unlimited. For example 11-5.32 3-1.323  card 1000");
    }
}
