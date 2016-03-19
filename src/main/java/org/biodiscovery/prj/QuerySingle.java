package org.biodiscovery.prj;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by sunkav01 on 3/17/2016.
 */
public class QuerySingle extends Query {
    QuerySingle(String fileName, Integer startValue, Integer endValue) {
        super (fileName, startValue, endValue);
    }

    @Override
    protected void search() {
        Path path = Paths.get (fileNameFirst);
        Integer fstartValue, fendValue;
        String fvalue;
        //System.out.println (Arrays.asList (tokens));
        try {
            //Delete existing Results file
            Files.deleteIfExists (Paths.get (Util.getResultsFileName ()));

            Long bytes = Files.size (path);
            System.out.println ("Filesize: " + bytes);
            ByteBuffer buffer = ByteBuffer.allocate (Integer.valueOf (bytes.toString ()));
            //StringBuilder strBuilder = new StringBuilder();
            //Java 8: Stream class File lines
            // skip the first line in the file
            Stream<String> lines = Files.lines (path, StandardCharsets.UTF_8);
            int count =0;
            for( String line : (Iterable<String>) lines::iterator ){
                //System.out.println(line);
                //Split each line into 3.  First part as fileName and second part content
                String ftokens[] = line.split("\\s+", 3);
                //System.out.println (Arrays.asList (ftokens));
                // Start processing files only if the tokens after splitting the line have 2 parts
                if(ftokens.length>1) {
                    fstartValue = Integer.parseInt (ftokens[0]);
                    fendValue = Integer.parseInt (ftokens[1]);
                    fvalue = ftokens[2];
                    //Put into buffer until you find a different chromosome and then write to file with filename as the chromosome
                    if (startValue <= fstartValue && fendValue <= endValue) {
                        Util.putIntoBuffer (buffer, line);
                        //Util.putIntoStringBuilder (strBuilder, line+"\n");
                        count++;
                    }
                }else {
                    System.err.println("Unable to parse line in the file.  LINE: " + line.toString ());
                }
            }
            Util.writeToFile (buffer, Util.getResultsFileName());
            //System.out.println (strBuilder.toString ());
            System.out.println ("Count : " + count);
        }catch(NoSuchFileException nfe) {
            System.err.println ("Oops ! No such file exists. Reason : " + nfe.getReason () );
        }catch (IOException e) {
            e.printStackTrace ();
        }
    }
}
