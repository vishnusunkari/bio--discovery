package org.biodiscovery.prj;

/**
 * Created by sunkav01 on 3/17/2016.
 */
public interface DataInterface {
    public void persist(String fileName);
    public Query getQuery(String query) throws InvalidQueryException;
    /*
     * @Returns Resultset fileName
     */
    public void search(Query query);
}
