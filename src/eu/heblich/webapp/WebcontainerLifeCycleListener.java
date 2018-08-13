package eu.heblich.webapp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.annotation.WebListener;

import eu.heblich.db.FileElement;
import eu.heblich.db.LocalDB;
import eu.heblich.db.WordElement;
import eu.heblich.db.WordFileConnector;
import eu.heblich.tika.TikaThread;
import eu.heblich.tika.file.FileThread;

@WebListener
public class WebcontainerLifeCycleListener implements ServletContextListener {

	public static final String DRIVER_NAME_CONTEXT_NAME = "db.driverName";
	public static final String DB_URL_CONTEXT_NAME = "db.url";
	public static final String DB_HOME_CONTEXT_NAME = "db.home";
	public static final String DB_USER_CONTEXT_NAME = "db.user";
	public static final String DB_PASSWORD_CONTEXT_NAME = "db.password";
	public static final String DB_REDEPLOY = "db.tables.redeploy";
	public static final String FILE_FOLDER_CONTEXT_NAME = "file.folder";
	public static final String FILE_SLEEP_CONTEXT_NAME = "file.sleep";
	public static final String TIKA_TESSERACT_PATH_CONTEXT_NAME = "tika.tesseract.path";
	public static final String TIKA_TESSDATA_PATH_CONTEXT_NAME = "tika.tessdata.path";
	public static final String TIKA_IMAGEMAGICK_PATH_CONTEXT_NAME = "tika.imageMagick.path";
	public static final String TIKA_LANFUAGES_PATH_CONTEXT_NAME = "tika.languages";
	public static final String TIKA_SLEEP_CONTEXT_NAME = "tika.sleep";
	
    public void contextInitialized(ServletContextEvent event) {
    	ServletContext app = event.getServletContext();
    	
    	String driverName      = app.getInitParameter(DRIVER_NAME_CONTEXT_NAME);
    	String url             = app.getInitParameter(DB_URL_CONTEXT_NAME);
    	String user            = app.getInitParameter(DB_USER_CONTEXT_NAME);
    	String pass            = app.getInitParameter(DB_PASSWORD_CONTEXT_NAME);
    	String derbyHome       = app.getInitParameter(DB_HOME_CONTEXT_NAME);
    	String fileFolder      = app.getInitParameter(FILE_FOLDER_CONTEXT_NAME);
    	String tesseractPath   = app.getInitParameter(TIKA_TESSERACT_PATH_CONTEXT_NAME);
    	String tessdataPath    = app.getInitParameter(TIKA_TESSDATA_PATH_CONTEXT_NAME);
    	String imageMagickPath = app.getInitParameter(TIKA_IMAGEMAGICK_PATH_CONTEXT_NAME);
    	String tikaLanguages   = app.getInitParameter(TIKA_LANFUAGES_PATH_CONTEXT_NAME);
    	String tikaSleep	   = app.getInitParameter(TIKA_SLEEP_CONTEXT_NAME);
    	String fileSleep	   = app.getInitParameter(FILE_SLEEP_CONTEXT_NAME);
    	boolean redeploy	   = Boolean.parseBoolean(app.getInitParameter(DB_REDEPLOY));
    	
    	System.out.println("Config:");
    	System.out.println("driverName:      \"".concat(driverName).concat("\""));
    	System.out.println("url:             \"".concat(url).concat("\""));
    	System.out.println("user:            \"".concat(user).concat("\""));
    	System.out.println("password:        \"".concat(pass).concat("\""));
    	System.out.println("derbyHome:       \"".concat(derbyHome).concat("\""));
    	System.out.println("redeploy:        \"".concat(String.valueOf(redeploy)).concat("\""));
    	System.out.println("fileFolder:      \"".concat(fileFolder).concat("\""));
    	System.out.println("fileSleep:       \"".concat(fileSleep).concat("\""));
    	System.out.println("tesseractPath:   \"".concat(tesseractPath).concat("\""));
    	System.out.println("tessdataPath:    \"".concat(tessdataPath).concat("\""));
    	System.out.println("imageMagickPath: \"".concat(imageMagickPath).concat("\""));
    	System.out.println("tikaLanguages:   \"".concat(tikaLanguages).concat("\""));
    	System.out.println("tikaSleep:       \"".concat(tikaSleep).concat("\""));
    	
    	//default values
    	driverName 		  = (driverName == null)?"org.apache.derby.jdbc.EmbeddedDriver":driverName;
    	url 			  = (url ==null)?"jdbc:derby:D:\\derbyTest\\derbyTest.de;create=true":url;
    	user 			  = (user == null)?"App":user;
    	pass 			  = (pass == null)?"":pass;
    	fileFolder 		  = (fileFolder == null)?"D:\\Files":fileFolder;
    	tesseractPath 	  = (tesseractPath == null)?"D:\\Program Files (x86)\\Tesseract-OCR":tesseractPath;
    	tessdataPath 	  = (tessdataPath == null)?"D:\\Program Files (x86)\\Tesseract-OCR\\tessdata":tessdataPath;
    	imageMagickPath   = (imageMagickPath == null)?"C:\\Program Files\\ImageMagick-7.0.8-Q16":imageMagickPath;
    	tikaLanguages     = (tikaLanguages == null)?"eng":tikaLanguages;
    	
    	Map<String, String> probs = new HashMap<>();
    	probs.put("derby.system.home", (derbyHome == null)?"D:\\derbyTest":derbyHome);
				
    	int tikaSleepInt = (tikaSleep == null)?3000:Integer.parseInt(tikaSleep);
    	int fileSleepInt = (fileSleep == null)?6000:Integer.parseInt(fileSleep);
    	
    	LocalDB.INSTANCE.Init(driverName, url, user, pass, probs);
    	if(redeploy)
    		LocalDB.INSTANCE.reDeplyAll(FileElement.class, WordElement.class, WordFileConnector.class);
    	
    	FileThread ft = new FileThread(fileFolder, fileSleepInt);
    	TikaThread tt = new TikaThread(event.getServletContext(), tikaSleepInt, tesseractPath, tessdataPath, imageMagickPath, tikaLanguages);
    		
    	ft.start();
    	tt.start();   	
    }

    public void contextDestroyed(ServletContextEvent event) {
        LocalDB.INSTANCE.shutdownConnection();
    }
    
    
}
