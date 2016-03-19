package org.biodiscovery.prj;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by sunkav01 on 3/17/2016.
 */
public class QueryMulti extends Query {
    protected List<String> fileNameList;
    protected String fileNameLast;
    protected static int count =0;

    QueryMulti(List<String>  fileNameList, Integer startValue, Integer endValue) {
        this.fileNameList = fileNameList;
        this.fileNameFirst = fileNameList.get (0);
        this.fileNameLast = fileNameList.get(fileNameList.size ()-1);
        this.startValue = startValue;
        this.endValue = endValue;
    }

    protected void search() {
        try {
            //Delete existing Results file
            Files.deleteIfExists (Paths.get (Util.getResultsFileName ()));
        } catch(IOException e) {
            e.printStackTrace ();
        }
        for (String fileName : fileNameList){
            Path path = Paths.get (fileName);
            if (fileName.equals (fileNameFirst)) {
                //System.out.println ("Search Results of First File : " + fileName);
                searchFirst (path);
            } else if (fileName.equals (fileNameLast)) {
                //System.out.println ("Search Results of Last File : " + fileName);
                searchLast (path);
            } else {
                //System.out.println ("Search Results of Intermediate Files : " + fileName);
                searchIntermediate (path);
            }
         }
    }

    public void searchFirst(Path path){
        //Path path
        Integer fstartValue, fendValue;
        String fvalue;
        int fcount=0;
        //StringBuilder strBuilder = new StringBuilder();
        try {
            /* Process First File */
            Long bytes = Files.size (path);
            System.out.println ("Filesize: " + bytes);
            ByteBuffer buffer = ByteBuffer.allocate (Integer.valueOf (bytes.toString ()));
            Stream<String> lines = Files.lines (path, StandardCharsets.UTF_8);
            for (String line : (Iterable<String>) lines::iterator) {
                String ftokens[] = line.split ("\\s+", 3);
                // Start processing files only if the tokens after splitting the line have 2 parts
                if (ftokens.length > 2) {
                    fstartValue = Integer.parseInt (ftokens[0]);
                    fendValue = Integer.parseInt (ftokens[1]);
                    fvalue = ftokens[2];
                    //Put into buffer until you find a different chromosome and then write to file with filename as the chromosome
                    if (startValue <= fstartValue) {
                        //Util.putIntoStringBuilder (strBuilder, line+"\n");
                        Util.putIntoBuffer (buffer, line);
                        //System.out.println (line);
                        fcount++;
                        count++;
                    }
                } else {
                    System.err.println ("Unable to parse line in the file.  LINE: " + line.toString ());
                }
            }
            Util.writeToFile (buffer, Util.getResultsFileName());
            //System.out.println ("Count First : " + fcount);
            //System.out.println ("Count upto current: " + count);
            //System.out.println (strBuilder.toString ());
        } catch(NoSuchFileException nfe) {
            System.err.println ("Oops ! Unable to get data from datalocation : " + nfe.getReason () );
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }


    private void searchIntermediate(Path path) {
        //Path path
        Integer fstartValue, fendValue;
        String fvalue;
        //System.out.println (Arrays.asList (tokens));
        int scount=0;
        //StringBuilder strBuilder = new StringBuilder();
        try {
            /* Process First File */
            Long bytes = Files.size (path);
            System.out.println ("Filesize: " + bytes);
            ByteBuffer buffer = ByteBuffer.allocate (Integer.valueOf (bytes.toString ()));
            Stream<String> lines = Files.lines (path, StandardCharsets.UTF_8);

            for (String line : (Iterable<String>) lines::iterator) {
                //Util.putIntoStringBuilder (strBuilder, line+"\n");
                Util.putIntoBuffer (buffer, line);
                scount++;
                count++;
            }
            Util.writeToFile (buffer, Util.getResultsFileName());
        } catch(NoSuchFileException nfe) {
            System.err.println ("Oops ! Unable to get data from datalocation : " + nfe.getReason () );
        } catch (IOException e) {
            e.printStackTrace ();
        }

        //System.out.println ("Count Last : " + lcount);
        //System.out.println (strBuilder.toString ());
        //System.out.println ("Count upto current: " + count);
    }

    private void searchLast(Path path) {
        //Path path
        Integer fstartValue, fendValue;
        String fvalue;
        int lcount=0;
        //System.out.println (Arrays.asList (tokens));
        //StringBuilder strBuilder = new StringBuilder();
        try {
            /* Process First File */
            Long bytes = Files.size (path);
            System.out.println ("Filesize: " + bytes);
            ByteBuffer buffer = ByteBuffer.allocate (Integer.valueOf (bytes.toString ()));
            Stream<String> lines = Files.lines (path, StandardCharsets.UTF_8);
            for (String line : (Iterable<String>) lines::iterator) {
                String ftokens[] = line.split ("\\s+", 3);
                //System.out.println (Arrays.asList (ftokens));
                // Start processing files only if the tokens after splitting the line have 2 parts
                if (ftokens.length > 2) {
                    fstartValue = Integer.parseInt (ftokens[0]);
                    fendValue = Integer.parseInt (ftokens[1]);
                    fvalue = ftokens[2];
                    //Put into buffer until you find a different chromosome and then write to file with filename as the chromosome
                    if(endValue >= fendValue) {
                        //System.out.println (line);
                        //Util.putIntoStringBuilder (strBuilder, line+"\n");
                        Util.putIntoBuffer (buffer, line);
                        lcount++;
                        count++;
                    }
                } else {
                    System.err.println ("Unable to parse line in the file.  LINE: " + line.toString ());
                }
            }
            Util.writeToFile (buffer, Util.getResultsFileName());
            //System.out.println ("Count Last : " + lcount);
            //System.out.println (strBuilder.toString ());
            System.out.println ("Total Results Count: " + count);

        } catch(NoSuchFileException nfe) {
            System.err.println ("Oops ! Unable to get data from datalocation : " + nfe.getReason () );
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
}
