package org.biodiscovery.prj;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunkav01 on 3/17/2016.
 */
public class MultiApp2 {

    public static DataInterface dataInterface = new DataInterfaceImpl ();

    public static void main( String[] args ) {
        Scanner scan = new Scanner (System.in);
        System.out.println ("Please enter the file path : ");
        String filePath = scan.nextLine ();

        //To calculate the total time taken by the program
        long startTime = System.nanoTime();

        dataInterface.persist (filePath);

        //To calculate the total time taken by the program
        long endTime = System.nanoTime();
        long elapsedTime = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
        System.out.println("Persist Time: " + elapsedTime + " ms");

        Query query;
        System.out.println ("Please enter the query to search for \n eg: \n chr18:0-6000000 \n chr1:0-chr22:51244045 \n");
        while(true) {
            String queryString = scan.nextLine ();
            //String query1 = "chr18:0-6000000";
            //String query2 = "chr1:0-chr22:51244045";
            System.out.println ("Query entered: " + queryString);
            try {
                query = dataInterface.getQuery (queryString);
                break;
            } catch (InvalidQueryException ive) {
                System.err.println ("Invalid Query ! Please reenter the query \n eg: \n chr18:0-6000000 \nchr1:0-chr22:51244045 \n");
            }
        }
        //To calculate the total time taken by the program
        long searchStartTime = System.nanoTime ();
        dataInterface.search(query);
        //To calculate the total time taken by the program
        long searchEndTime = System.nanoTime();
        long elapsedSearchTime = TimeUnit.MILLISECONDS.convert((searchEndTime - searchStartTime), TimeUnit.NANOSECONDS);
        System.out.println("Search time: " + elapsedSearchTime + " ms");
        System.out.println ("Please find the Results at :\n" + (Paths.get (Util.getResultsFileName ())).toAbsolutePath ().toString ());
    }
}


