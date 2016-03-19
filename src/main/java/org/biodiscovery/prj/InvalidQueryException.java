package org.biodiscovery.prj;

/**
 * Created by sunkav01 on 3/18/2016.
 */
public class InvalidQueryException extends Exception {
    public InvalidQueryException() {}
    public InvalidQueryException(String msg) {
        super(msg);
    }
}
