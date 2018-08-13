package eu.heblich.tika;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.servlet.ServletContext;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.RecursiveParserWrapper;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BasicContentHandlerFactory;
import org.apache.tika.sax.BodyContentHandler;

import eu.heblich.db.FileElement;
import eu.heblich.db.FileElement.Status;
import eu.heblich.webSocekt.FileSessionHandler;
import eu.heblich.webSocekt.FilleProgress;
import eu.heblich.db.LocalDB;
import eu.heblich.db.WordElement;
import javafx.util.Pair;

public class TikaThread extends Thread {

	private ServletContext appScope;
	private int sleepTime;
	public FileSessionHandler fileSessionHandler;
	
	public String tesseractPath = "D:\\Program Files (x86)\\Tesseract-OCR";
	public String tessdataPath = "D:\\Program Files (x86)\\Tesseract-OCR\\tessdata";
	public String imageMagickPath = "C:\\Program Files\\ImageMagick-7.0.8-Q16";
	public String tikaLanguages = "eng+deu";
	
	
	private boolean interruped;
	
	
	public TikaThread(ServletContext appScope, int sleepTime, String tesseractPath, String tessdataPath, String imageMagickPath, String tikaLanguages) {
		super();
		this.appScope = appScope;
		this.sleepTime = sleepTime;
		fileSessionHandler = FileSessionHandler.getInstace(appScope);
		this.tessdataPath = tessdataPath;
		this.tesseractPath = tesseractPath;
		this.imageMagickPath = imageMagickPath;
		this.tikaLanguages = tikaLanguages;
		
		setDaemon(true);
	}


	
	

	@Override
	public void run() {
		while(!interruped){
			//make sure some old stuff gets cleand up. Fix restart of server
			FileElement[] processing = FileElement.getFileElementWithStatus(Status.PROCESSING);
			for (int i = 0; i < processing.length; i++) {
				if(!fileSessionHandler.hasFile(processing[i].getId())){
					processing[i].setStatus(Status.NONE);
					FileElement.update(processing[i]);
				}
			}
			
			FileElement[] possible = FileElement.getFileElementWithStatus(Status.NONE);
			for (int i = 0; i < possible.length; i++) {
				if(!fileSessionHandler.hasFile(possible[i].getId())){
					System.out.println("TikaThread.run: add"+possible[i].getFilename());
					fileSessionHandler.addFile(possible[i].getId(), Status.PROCESSING , possible[i].getName(), possible[i].getFilename(), possible[i].getMd5(), 0, null);
					//processingEle.put(possible[i].getId(), new Pair<FileElement, Integer>(possible[i],0));
					possible[i].setStatus(Status.PROCESSING);
					FileElement.update(possible[i]);
				}else{
					possible[i].setStatus(Status.PROCESSING);
					fileSessionHandler.updateStatus(possible[i].getId(), Status.PROCESSING);
					FileElement.update(possible[i]);
					
				}
			}
			analizeOne();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
			}
			super.run();
		}
	}
	
	public void analizeOne(){
		Set<Integer> keys =  fileSessionHandler.getFileProgress().keySet();
		FilleProgress ele = null;
		if(!keys.isEmpty()){
			ele = fileSessionHandler.get(keys.iterator().next());
			
			Set<String> words = new HashSet<>();
			try {
				System.out.println("TikaThread.analizeOne: processFile"+ele.getFilename());
				processFile(ele.getFilename(), words, ele.getId());
				fileSessionHandler.updateMaxWords(ele.getId(), words.size());
				int i = 0;
				for (String word : words) {
					WordElement.addOrUpdate(word, ele.getId());
					i++;
					if(i % 10 == 0){
						//System.out.println("TikaThread.analizeOne: processFile"+ele.getFilename() + " "+i+" words of "+words.size());
						fileSessionHandler.updateWordProcess(ele.getId(), i);
					}
				}
				System.out.println("TikaThread.analizeOne: done with "+ele.getFilename());
				ele.setStatus(Status.DONE);
				fileSessionHandler.updateStatus(ele.getId(), Status.DONE);
				
			} catch (Exception e) {
				ele = null;
				e.printStackTrace();
			}
		}
		if(ele != null){
			//processingEle.remove(fileElement.getId());
			fileSessionHandler.removeFile(ele.getId());
			System.out.println("TikaThread.analizeOne: remove "+ele.getFilename());
			FileElement fe = FileElement.get(ele.getId());
			fe.setStatus(ele.getFileEleemntStatus());
			FileElement.update(fe);
		}
	}
	
	public void processFile(String filename, Set<String> words, int id) throws Exception {
		
		//if(true)
		//	return runOCR(filename, BasicContentHandlerFactory.HANDLER_TYPE.TEXT, TesseractOCRConfig.OUTPUT_TYPE.HOCR);
		
		long fileSize = (new File(filename)).length();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		 
		BodyContentHandler handler = new BodyContentHandler(out);

		
		TikaConfig config = TikaConfig.getDefaultConfig();
		//InputStream stream = new FileInputStream(filename);
		fileSessionHandler.updateFileSize(id, fileSize);
        InputStream stream = new ProgressInputStream(new FileInputStream(filename),fileSize,appScope, id);
        

        AutoDetectParser parser = new AutoDetectParser(config);
        //Parser parser = new RecursiveParserWrapper(new AutoDetectParser(),
        //        new BasicContentHandlerFactory(
        //        		BasicContentHandlerFactory.HANDLER_TYPE.TEXT, -1));
        
        MimeTypes mimeRegistry = TikaConfig.getDefaultConfig()
                .getMimeRepository();
        
        MimeType mtype = mimeRegistry.getMimeType(new File(filename));
                
        Metadata metadata = new Metadata();
        ParseContext parsecontext = new ParseContext();
        PDFParserConfig pdfConfig = new PDFParserConfig();
        //ImageParser ip = new ImageParser();
        //ip.
        
		pdfConfig.setExtractInlineImages(true);
		 
		TesseractOCRConfig tesserConfig = new TesseractOCRConfig();
		tesserConfig.setLanguage(tikaLanguages);
		tesserConfig.setTesseractPath(tesseractPath);
		tesserConfig.setTessdataPath(tessdataPath);
		tesserConfig.setImageMagickPath(imageMagickPath);
	
		 
		parsecontext.set(Parser.class, parser);
		parsecontext.set(PDFParserConfig.class, pdfConfig);
		parsecontext.set(TesseractOCRConfig.class, tesserConfig);
        
        try {
            //parser.parse(stream, handler, metadata);
            parser.parse(stream, handler, metadata, parsecontext);
            
            String text = new String(out.toByteArray(), Charset.defaultCharset());
            
            String[] allWords = text.split("\\s+");
            
            for (int i = 0; i < allWords.length; i++) {
            	String currentWord = allWords[i];
            	
				if(currentWord.length() >=2 && !words.contains(currentWord))
					words.add(currentWord);
			}
            
            System.out.println(mtype.getName()+" Size: "+out.size()+" ");
            
            //return mtype.getName()
            //		.concat(" Size: "+out.size()+"/"+fileSize)
            //		.concat(new String(out.toByteArray(), Charset.defaultCharset())
            //);

            //return handler.toString();
            //out.println(handler.toString());
        } finally {
            stream.close();
        }
		

	}
}
