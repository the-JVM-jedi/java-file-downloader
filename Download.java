import java.io.*; //find out what all these libraries do
import java.net.*; //Find out what import actually does
import java.util.*;

//Workhorse of the project. This code does the actual downloading of a file
public class Download extends Observable implements Runnable {
	//max size of download buffer
	private static final int MAX_BUFFER_SIZE = 1024;
	
	//These are the status names.
	public static final String STATUSES[] = {"Downloading", "Paused", "Complete", "Cancelled", "Error"};
	
	//The status codes
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	private URL url; //download url
	private int size; //size of download in bytes
	private int downloaded; //number of bytes downloaded
	private int status; //current status of download
	
	//Constructor for Download.
	public Download(URL url) {
		this.url = url;
		size = -1;
		downloaded = 0;
		status = DOWNLOADING;
		
		//Begin the download.
		download();
	}
	
	//Get this download's URL
	public String getUrl() {
		return url.toString();
	}
	
	//Get this download size.
	public int getSize() {
		return size;
	}
	
	//Get this download's progress.
	public float getProgress() {
		return ((float) downloaded/size) * 100;
	}
	
	//Get this download's status.
	public int getStatus() {
		return status;
	}
	
	//pause this download.
	public void pause() {
		status = PAUSED;
		stateChanged();
	}
	
	//resume this download
	public void resume() {
		status = DOWNLOADING;
		stateChanged();
		download();
	}
	//Cancel this download.
	public void cancel() {
		status = CANCELLED;
		stateChanged();
	}
	//Mark this download as having an error.
	private void error() {
		status = ERROR;
		stateChanged();
	}
	
	//Start or resume downloading 
	private void download() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	//Get fle name portion of URL.
	private String getFileName(URL url) {
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}
	
	//Download file.
	public void run() {
		RandomAccessFile file = null;
		InputStream stream = null;
		
		try {
			// open URL connection.
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			//Specify what portion of a file to download.
			connection.setRequestProperty("Range","bytes=" + downloaded + "-");
			
			//connect to server.
			connection.connect();
			
			//make sure response code is in the 200 range.
			if (connection.getResponseCode() / 100 != 2) {
				error();
			}
			
			//Check for valid content length.
			int contentLength = connection.getContentLength();
			if(contentLength < 1) {
				error();
			}
			
			/*Set the size for this download if it hasn't been already set. */
			if (size == -1) {
				size = contentLength;
				stateChanged();
			}
			//Open file and seek to the end of it.
			file = new RandomAccessFile(getFileName(url), "rw");
			file.seek(downloaded);
			
			stream = connection.getInputStream();
			while (status == DOWNLOADING) {
				//Size buffer according to how much of the file is left to be downloaded.
				byte buffer[];
				if (size - downloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				}
				else {
					buffer = new byte[size - downloaded];
				}
				
				//Read from the server into buffer.
				int read = stream.read(buffer);
				if (read == -1) {
					break;
				}
				
				//write buffer to file.
				file.write(buffer, 0, read);
				downloaded += read;
				stateChanged();
			}
			
			//Change status to complete if this point was reached because downloading has finished
			if (status == DOWNLOADING) {
				status = COMPLETE;
				stateChanged();
			}
		}
		catch (Exception e) {
			error();
		}
		finally {
			//close file.
			if (file != null) { 
				try {
					file.close();
				}
				catch (Exception e) {
					
				}
			}
			
			//Close connection to server.
			if (stream != null) {
				try {
					stream.close();
				}
				catch (Exception e) {
					
				}
			}
		}
	}
	
	//Notify observers that this downlaod's status has changed.
	private void stateChanged() {
		setChanged();
		notifyObservers();
	}
}
