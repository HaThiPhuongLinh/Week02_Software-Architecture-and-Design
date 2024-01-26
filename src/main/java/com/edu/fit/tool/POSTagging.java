package com.edu.fit.tool;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class POSTagging {
    private static final String MODEL_PATH = "src/main/resources/en-pos-maxent.bin";
    private static POSModel model;
    private static POSTaggerME tagger;

    public POSTagging() {
        try {
            InputStream inputStream = new FileInputStream(MODEL_PATH);
            model = new POSModel(inputStream);
            tagger = new POSTaggerME(model);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isNoun(String word) {
        String[] token = SimpleTokenizer.INSTANCE.tokenize(word);
        String[] tags = tagger.tag(token);
        for (String tag : tags) {
            if (tag.startsWith("NN")) {
                return true;
            }
        }
        return false;
    }

    public boolean isVerb(String word) {
        String[] token = SimpleTokenizer.INSTANCE.tokenize(word);
        String[] tags = tagger.tag(token);
        for (String tag : tags) {
            if (tag.startsWith("VB")) {
                return true;
            }
        }
        return false;
    }
}
