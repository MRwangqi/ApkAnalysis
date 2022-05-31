package com.codelang.apkanalysis.parse.xml.bean.chunk;


import com.codelang.apkanalysis.parse.xml.bean.Xml;

/**
 * Created by luyao
 * on 2018/12/14 16:23
 */
public class EndNameSpaceChunk extends Chunk {

    private int prefix;
    private int uri;

    public EndNameSpaceChunk(int chunkSize, int lineNumber, int prefix, int uri) {
        super(Xml.END_NAMESPACE_CHUNK_TYPE);
        this.chunkSize = chunkSize;
        this.lineNumber = lineNumber;
        this.prefix = prefix;
        this.uri = uri;
    }

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    public int getUri() {
        return uri;
    }

    public void setUri(int uri) {
        this.uri = uri;
    }

    @Override
    public String toXmlString() {
        return "";
    }
}
