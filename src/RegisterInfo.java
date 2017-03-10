
import java.util.ArrayList;
import java.util.List;

public class RegisterInfo {
	private int clientId;
	private int port;
	private String ipAddress;
	private  String sharedFilePath;
	private List<String> filesList;
	public void setClientId(int clientId)
	{
		this.clientId = clientId;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	
	public void setSharedFilePath(String sharedFilePath)
	{
		this.sharedFilePath = sharedFilePath;
	}
	
	public void setFileList(String fileList)
	{
		System.out.println("setFileList");
		filesList = new ArrayList<String>();
		  String split[] = fileList.split("---")  ;
		 for (int i = 0; i < split.length; i++) 
		  {	   
		   filesList.add(split[i]);
		 //  System.out.println(filesList.get(i));
		  }
		 System.out.println(filesList);
	}
	
	public void addFile(String file)
	{
		filesList.add(file);
	}
	
	
	public int getClientId()
	{
		return this.clientId;
	}
	
	public int getPort()
	{
		return this.port;
	}
	
	public String getIpAddress()
	{
		return this.ipAddress;
	}
	
	public String getSharedFilePath()
	{
		return this.sharedFilePath;
	}
	
	public boolean searchforFile(String filename)
	{
		for(int i=0;i<filesList.size();i++)
		{
			if(filename.equals(filesList.get(i)))
				return true;
		}
		return false;
	}

	public String getFilesList()
	{
		String fileList ="";
		
		for(int i=0;i<filesList.size();i++)
		{
			fileList += filesList.get(i) + "---";
		}
		return fileList;
	}
	
	public int getFilesCount()
	{
		filesList.size();
		return filesList.size();
	}
	
}
