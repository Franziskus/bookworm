package eu.heblich.tika.file;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Predicate;

public class FileFilter<T extends Path> implements Predicate {

	@Override
	public boolean test(Object t) {
		if(t instanceof Path){
			File f = ((Path) t).toFile();
			if(f.isDirectory())
				return false;
			String name = f.getName();
			if(name.substring(0,2).equals("~$"))
				return false;
			else if(name.substring(name.length()-5).equalsIgnoreCase(".docx"))
				return true;
			else if(name.substring(name.length()-4).equalsIgnoreCase(".pdf"))
				return true;
			else if(name.substring(name.length()-4).equalsIgnoreCase(".doc"))
				return true;
			else if(name.substring(name.length()-4).equalsIgnoreCase(".jpg"))
				return true;
			else if(name.substring(name.length()-4).equalsIgnoreCase(".jpeg"))
				return true;
			else if(name.substring(name.length()-4).equalsIgnoreCase(".png"))
				return true;
			else
				return false;
		}
		return false;
	}

	
	
}
