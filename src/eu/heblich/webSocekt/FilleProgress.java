package eu.heblich.webSocekt;

import eu.heblich.db.FileElement;

public class FilleProgress {

    private int id;
    private String name;
    private FileElement.Status status;
    private long progress;
    private Long size;
    private int processedWords;
    private Integer maxWords;
    private String filename;
    private String md5;

    public FilleProgress() {
    }
    
    public FilleProgress(int id, String name, FileElement.Status status, long progress, String filename, String md5, Long size) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.progress = progress;
		this.filename = filename;
		this.md5 = md5;
		this.size = size;
	}

	public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public String getStatus() {
        return status.toString();
    }
    
    public FileElement.Status getFileEleemntStatus() {
        return status;
    }

    public long getProgress() {
        return progress;
    }
    
    public String getFilename() {
        return filename;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(FileElement.Status status) {
        this.status = status;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getProcessedWords() {
		return processedWords;
	}

	public void setProcessedWords(int processedWords) {
		this.processedWords = processedWords;
	}

	public Integer getMaxWords() {
		return maxWords;
	}

	public void setMaxWords(int maxWords) {
		this.maxWords = maxWords;
	}
    
    
}
