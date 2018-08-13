package eu.heblich.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.language.LanguageProfile;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

/**
 * Demonstrates how to call the different components within Tika: its
 * {@link Detector} framework (aka MIME identification and repository), its
 * {@link Parser} interface, its {@link LanguageIdentifier} and other goodies.
 */

public class MyFirstTika {

    public static void test(PrintWriter out) throws Exception {
        String filename = "D:\\Files\\bla\\bbb.pdf";//args[0];
        
        BodyContentHandler handler = new BodyContentHandler(-1);

        InputStream stream = new FileInputStream(filename);//ContentHandlerExample.class.getResourceAsStream("test.doc");
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try {
            parser.parse(stream, handler, metadata);
            out.println(handler.toString());
        } finally {
            stream.close();
        }
        /*
        MimeTypes mimeRegistry = TikaConfig.getDefaultConfig()
                .getMimeRepository();

        out.println("Examining: [" + filename + "]");

        out.println("The MIME type (based on filename) is: ["
                + mimeRegistry.getMimeType(filename) + "]");

        out.println("The MIME type (based on MAGIC) is: ["
                + mimeRegistry.getMimeType(new File(filename)) + "]");

        Detector mimeDetector = (Detector) mimeRegistry;
        out
                .println("The MIME type (based on the Detector interface) is: ["
                        + mimeDetector.detect(new File(filename).toURI().toURL()
                                .openStream(), new Metadata()) + "]");

        LanguageIdentifier lang = new LanguageIdentifier(new LanguageProfile(
                FileUtils.readFileToString(new File(filename))));

        out.println("The language of this content is: ["
                + lang.getLanguage() + "]");

        Parser parser = TikaConfig.getDefaultConfig().getParser(
                MediaType.parse(mimeRegistry.getMimeType(filename).getName()));

    Metadata parsedMet = new Metadata();
        ContentHandler handler = new BodyContentHandler();
        parser.parse(new File(filename).toURI().toURL().openStream(), handler,
                parsedMet, new ParseContext());

        out.println("Parsed Metadata: ");
        out.println(parsedMet);
        out.println("Parsed Text: ");
        out.println(handler.toString());

    }
    /*
    BodyContentHandler handler = new BodyContentHandler(-1);

    InputStream stream = ContentHandlerExample.class.getResourceAsStream("test.doc");
    AutoDetectParser parser = new AutoDetectParser();
    Metadata metadata = new Metadata();
    try {
        parser.parse(stream, handler, metadata);
        return handler.toString();
    } finally {
        stream.close();
    }
    */
    }
}