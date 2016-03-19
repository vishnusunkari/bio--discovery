package org.biodiscovery.prj;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by sunkav01 on 3/16/2016.
 */
public abstract class Query {

    protected String fileNameFirst;
    protected Integer startValue;
    protected Integer endValue;
    protected String value;

    Query(){
    }

    Query(String fileName, Integer startValue, Integer endValue) {
        this.fileNameFirst = fileName;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    protected abstract void search();

}

