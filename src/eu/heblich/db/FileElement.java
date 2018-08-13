package eu.heblich.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import eu.heblich.db.annotation.Id;
import eu.heblich.db.annotation.StringSize;

public class FileElement {

	public static enum Status{
		FOUND,
		PROCESSING,
		DONE,
		NONE
	}
	
	@Id(autogenerated = true)
	protected Integer id;
	@StringSize(100)
	protected String name;
	@StringSize(255)
	protected String filename;
	@StringSize(32)
	protected String md5;
	
	//Size of the longes element PROCESSING 
	@StringSize(10)
	protected String status = "NONE";
	
	public FileElement() {
		super();
	}
	
	public FileElement(String name, String filename) {
		super();
		this.name = name;
		this.filename = filename;
	}
	
	public FileElement(String name, String filename, String md5) {
		super();
		this.name = name;
		this.filename = filename;
		this.md5 = md5;
	}

	public FileElement(int id, String name, String filename) {
		super();
		this.id = id;
		this.name = name;
		this.filename = filename;
	}
	
	public FileElement(int id, String name, String filename, String md5) {
		super();
		this.id = id;
		this.name = name;
		this.filename = filename;
		this.md5 = md5;
	}

	public int getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	

	public Status getStatus() {
		return Status.valueOf(status);
	}

	public void setStatus(Status status) {
		this.status = status.toString();
	}
	
	protected void setStatus(String status) {
		this.status = status;
	}
	
	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public static FileElement update(FileElement fe){
		LocalDB.INSTANCE.update(fe);
		return fe;
	}
	
	public static FileElement get(int id){
		return LocalDB.INSTANCE.get(FileElement.class, id);
		/*
		String sql = "Select id, name, filename, md5, status from FileElement where id = ".concat(String.valueOf(id));
		final FileElement succeeded = new FileElement();
		
		LocalDB.INSTANCE.select(sql, new Consumer<ResultSet>() {
			
			@Override
			public void accept(ResultSet t) {
				try {
					while (t.next()) {
						succeeded.setId(t.getInt(1));
						succeeded.setName(t.getString(2));
						succeeded.setFilename(t.getString(3));
						succeeded.setMd5(t.getString(4));
						succeeded.status = t.getString(5);
						return;
					}
				} catch (SQLException e) {
					LocalDB.printSQLException(e);
				}
				succeeded.setId(-1);
			}
		});
		if(succeeded.getId() == -1)
			return null;
		return succeeded;*/
	}
	
	public static FileElement[] getFileElementWithStatus(Status s) {
		List<Integer> intList = getIdsWithStatus(s);
		FileElement[] back = new FileElement[intList.size()];
		for (int i = 0; i < back.length; i++) {
			back[i] = get(intList.get(i));
		}
		return back;
		
	}
	
	private static List<Integer> getIdsWithStatus(Status s){
		String sql = "Select id from FileElement where status like '".concat(s.toString()).concat("'");
		final List<Integer> succeeded = new ArrayList<>();
		
		LocalDB.INSTANCE.select(sql, new Consumer<ResultSet>() {
			
			@Override
			public void accept(ResultSet t) {
				try {
					while (t.next()) {
						succeeded.add(t.getInt(1));
					}
				} catch (SQLException e) {
					LocalDB.printSQLException(e);
				}
			}
		});
		return succeeded;
	}
	
	public static int containsFilename(String s){
		String sql = "Select id from FileElement where filename like '".concat(s).concat("'");
		final AtomicInteger succeeded = new AtomicInteger();
		
		LocalDB.INSTANCE.select(sql, new Consumer<ResultSet>() {
			
			@Override
			public void accept(ResultSet t) {
				try {
					while (t.next()) {
						succeeded.set(t.getInt(1));
						return;
					}
				} catch (SQLException e) {
					LocalDB.printSQLException(e);
				}
				succeeded.set(-1);
			}
		});
		//System.out.println("FileElement.containsFilename: "+succeeded.get());
		return succeeded.get();
	}
	
	public static int containsMD5(String s){
		String sql = "Select id from FileElement where md5 like '".concat(s).concat("'");
		final AtomicInteger succeeded = new AtomicInteger();
		
		LocalDB.INSTANCE.select(sql, new Consumer<ResultSet>() {
			
			@Override
			public void accept(ResultSet t) {
				try {
					while (t.next()) {
						succeeded.set(t.getInt(1));
						return;
					}
				} catch (SQLException e) {
					LocalDB.printSQLException(e);
				}
				succeeded.set(-1);
			}
		});
		//System.out.println("FileElement.containsMD5: "+succeeded.get());
		return succeeded.get();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((md5 == null) ? 0 : md5.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileElement other = (FileElement) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (md5 == null) {
			if (other.md5 != null)
				return false;
		} else if (!md5.equals(other.md5))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

		
}
