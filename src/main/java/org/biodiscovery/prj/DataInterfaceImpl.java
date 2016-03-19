package org.biodiscovery.prj;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by sunkav01 on 3/17/2016.
 */
public class DataInterfaceImpl implements DataInterface{

    @Override
    public void persist(String filePath) {
        System.out.println ("Saving Data to Disk .. ");
        Path path = Paths.get (filePath);

        ByteBuffer buffer = null;
        //Path file = Paths.get("probestemp.txt");
        try
        {
            String fileName=null;
            String previousFileName=null;
            Long bytes = Files.size (path);
            buffer = ByteBuffer.allocate(Integer.valueOf (bytes.toString ()));
            //Java 8: Stream class File lines
            // skip the first line in the file
            Stream<String> lines = Files.lines( path, StandardCharsets.UTF_8 ).skip (1);

            for( String line : (Iterable<String>) lines::iterator )
            {
                //System.out.println(line);
                //Split each line into 2.  First part as fileName and second part content
                String tokens[] = line.split("\\s+", 2);
                // Start processing files only if the tokens after splitting the line have 2 parts
                if(tokens.length>1) {
                    fileName = tokens[0] + ".txt";
                    //Put into buffer until you find a different chromosome and then write to file with filename as the chromosome
                    if (previousFileName == null || fileName.equals (previousFileName)) {
                        Util.putIntoBuffer (buffer, tokens[1]);
                    } else {
                        Util.writeToFile (buffer, previousFileName);
                        Util.putIntoBuffer (buffer, tokens[1]);
                    }
                    previousFileName = fileName;
                } else {
                    System.err.println("Unable to parse line in the file.  LINE: " + line.toString ());
                }
            }
            Util.writeToFile (buffer, previousFileName);
            System.out.println (" COMPLETED ");
        } catch(NoSuchFileException nfe) {
            System.err.println ("Oops ! No such file exists. Reason : " + nfe.getReason () );
        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {
            buffer.clear();
        }
    }

    @Override
    public Query getQuery(String query) throws InvalidQueryException {
        Pattern p1 = Pattern.compile("chr\\d+:\\d+-\\d+");
        Pattern p2 = Pattern.compile("chr\\d+:\\d+-chr\\d+:\\d+");
        Matcher m1 = p1.matcher(query);
        Matcher m2 = p2.matcher (query);
        if(m1.matches()) {
            String tokens[] = query.split ("[^\\w]");
            String fileName = tokens[0]+".txt";
            //System.out.println (fileName);
            Integer startValue = Integer.parseInt (tokens[1]);
            Integer endValue = Integer.parseInt (tokens[2]);
            return new QuerySingle (fileName, startValue, endValue);
        } else if(m2.matches ()){
            String tokens[] = query.split ("[^\\w]");
            String firstFile = tokens[0];
            Integer firstFileNum = Integer.parseInt (firstFile.replaceAll ("[^0-9]", ""));
            String fileNameFirst = tokens[0]+".txt";
            //System.out.println (fileNameFirst);
            Integer startValue = Integer.parseInt (tokens[1]);
            String fileNameLast = tokens[2]+".txt";
            Integer endValue = Integer.parseInt (tokens[3]);
            List<String> fileNameList = getFileNameList(tokens[0], tokens[2]);
            return new QueryMulti (fileNameList, startValue, endValue);
        }
        throw new InvalidQueryException ();
    }

    public void search(Query query) {
        query.search ();
    }



    public List<String> getFileNameList(String firstFile, String lastFile) {
        String fileStr = firstFile.replaceAll ("[0-9]", "");
        Integer firstFileNum = Integer.parseInt (firstFile.replaceAll ("[^0-9]", ""));
        Integer lastFileNum = Integer.parseInt (lastFile.replaceAll ("[^0-9]", ""));
        List<String> fileNameList = new LinkedList<String> ();
        if(lastFileNum > firstFileNum && firstFileNum >0) {
            for (int i = firstFileNum; i <= lastFileNum; i++) {
                fileNameList.add (fileStr + i + ".txt");
            }
            return fileNameList;
        } else {
            throw new IllegalArgumentException ("Invalid Query");
        }
    }
}
