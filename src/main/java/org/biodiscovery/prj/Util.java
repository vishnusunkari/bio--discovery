package org.biodiscovery.prj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by sunkav01 on 3/18/2016.
 */
public class Util {

    private static final String RESULTSET_FILENAME="Results.txt";

    public static void putIntoBuffer(ByteBuffer buffer, String token) throws UnsupportedEncodingException {
        if(null !=token && token.length ()>0) {
            buffer.put ((token + System.getProperty ("line.separator")).getBytes ("utf-8"));
        }
    }

    public static void putIntoStringBuilder(StringBuilder strBuilder, String str) {
        strBuilder.append (str);
    }

    public static void writeToFile(ByteBuffer buffer, String fileName) {
        FileChannel wChannel = null;
        try {
            buffer.flip ();
            //Appends content to the file if the file already exists
            wChannel = new FileOutputStream (new File (fileName), true).getChannel ();
            wChannel.write (buffer);
            wChannel.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        } finally {
            buffer.clear ();
        }
    }

    public static String getResultsFileName(){
        return RESULTSET_FILENAME;
    }

}
