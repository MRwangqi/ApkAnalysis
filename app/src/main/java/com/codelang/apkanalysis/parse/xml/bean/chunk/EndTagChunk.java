package com.codelang.apkanalysis.parse.xml.bean.chunk;

import static com.codelang.apkanalysis.parse.xml.bean.Xml.BLANK;
import static com.codelang.apkanalysis.parse.xml.bean.Xml.blank;

import com.codelang.apkanalysis.parse.xml.XmlParser;
import com.codelang.apkanalysis.parse.xml.bean.Xml;

/**
 * Created by luyao
 * on 2018/12/14 16:27
 */
public class EndTagChunk extends Chunk {
    private int nameSpaceUri;
    private String name;

    public EndTagChunk(int nameSpaceUri, String name) {
        super(Xml.END_TAG_CHUNK_TYPE);
        this.nameSpaceUri = nameSpaceUri;
        this.name = name;
    }


    public int getNameSpaceUri() {
        return nameSpaceUri;
    }

    public void setNameSpaceUri(int nameSpaceUri) {
        this.nameSpaceUri = nameSpaceUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toXmlString() {
        if (name.equals("manifest"))
            BLANK.setLength(0);
        else
            BLANK.setLength(BLANK.length() - blank.length());

        return XmlParser.format("\n%s</%s>", BLANK, name);
    }
}
