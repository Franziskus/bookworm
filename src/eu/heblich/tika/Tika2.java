package eu.heblich.tika;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.RecursiveParserWrapper;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BasicContentHandlerFactory;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class Tika2 {

	public static String test(String filename) throws Exception {
		
		//if(true)
		//	return runOCR(filename, BasicContentHandlerFactory.HANDLER_TYPE.TEXT, TesseractOCRConfig.OUTPUT_TYPE.HOCR);
		
		long fileSize = (new File(filename)).length();
		
		//InputStream pdf = Files.newInputStream(Paths.get("src/test/resources/testpdf.pdf"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		 
		BodyContentHandler handler = new BodyContentHandler(out);

		
		//TikaConfig config = new TikaConfig("d:\\tika-config.xml");
		TikaConfig config = TikaConfig.getDefaultConfig();
		//InputStream stream = new FileInputStream(filename);
        InputStream stream = null;// new ProgressInputStream(new FileInputStream(filename),fileSize ,0, null);
        //ContentHandlerExample.class.getResourceAsStream("test.doc");
        AutoDetectParser parser = new AutoDetectParser(config);
        
        
        
        MimeTypes mimeRegistry = TikaConfig.getDefaultConfig()
                .getMimeRepository();
        
        MimeType mtype = mimeRegistry.getMimeType(new File(filename));
                
        Metadata metadata = new Metadata();
        ParseContext parsecontext = new ParseContext();
        PDFParserConfig pdfConfig = new PDFParserConfig();
		pdfConfig.setExtractInlineImages(true);
		 
		TesseractOCRConfig tesserConfig = new TesseractOCRConfig();
		tesserConfig.setLanguage("eng+deu");
		tesserConfig.setTesseractPath("D:\\Program Files (x86)\\Tesseract-OCR");
		 
		parsecontext.set(Parser.class, parser);
		parsecontext.set(PDFParserConfig.class, pdfConfig);
		parsecontext.set(TesseractOCRConfig.class, tesserConfig);
        
        try {
            //parser.parse(stream, handler, metadata);
            parser.parse(stream, handler, metadata, parsecontext);
            
            System.out.println(mtype.getName()+" Size: "+out.size()+" ");
            
            return mtype.getName()
            		.concat(" Size: "+out.size()+"/"+fileSize)
            		.concat(new String(out.toByteArray(), Charset.defaultCharset())
            );

            //return handler.toString();
            //out.println(handler.toString());
        } finally {
            stream.close();
        }
		
		//TikaConfig config = TikaConfig.getDefaultConfig();
		// TikaConfig fromFile = new TikaConfig("/path/to/file");
		//BodyContentHandler handler = new BodyContentHandler(out);
		//Parser parser = new AutoDetectParser(config);
		//Metadata meta = new Metadata();
		
		
        
		
		
		
	}
	
	private String runOCR(String resource, 
            BasicContentHandlerFactory.HANDLER_TYPE handlerType,
            TesseractOCRConfig.OUTPUT_TYPE outputType) throws Exception {
		
			MimeTypes mimeRegistry = TikaConfig.getDefaultConfig()
	                .getMimeRepository();
	        
	        MimeType mtype = mimeRegistry.getMimeType(new File(resource));
	                
	        Metadata metadata = new Metadata();
		
			TesseractOCRConfig config = new TesseractOCRConfig();
			config.setOutputType(outputType);
			
			BodyContentHandler handler = new BodyContentHandler(-1);
			
			Parser parser = new RecursiveParserWrapper(new AutoDetectParser(),
			  new BasicContentHandlerFactory(
			          handlerType, -1));
			
			PDFParserConfig pdfConfig = new PDFParserConfig();
			pdfConfig.setExtractInlineImages(true);
			
			ParseContext parseContext = new ParseContext();
			parseContext.set(TesseractOCRConfig.class, config);
			parseContext.set(Parser.class, parser);
			parseContext.set(PDFParserConfig.class, pdfConfig);
			
			try (InputStream stream = new FileInputStream(resource)) {
				parser.parse(stream, handler, metadata, parseContext);
			}
			List<Metadata> metadataList = ((RecursiveParserWrapper) parser).getMetadata();
	
			return mtype.getName().concat(handler.toString());
			
			//StringBuilder contents = new StringBuilder();
			//for (Metadata m : metadataList) {
			//	ontents.append(m.get(AbstractRecursiveParserWrapperHandler.TIKA_CONTENT));
			//}
			
			//for (String needle : nonOCRContains) {
			//assertContains(needle, contents.toString());
			//}
			//assertTrue(metadataList.get(0).names().length > 10);
			//assertTrue(metadataList.get(1).names().length > 10);
			//test at least one value
			//assertEquals("deflate", metadataList.get(1).get("Compression CompressionTypeName"));
			
			//return contents.toString();
}
}
