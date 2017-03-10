import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int i=1;
	public ServerSocket severSocket = null;
    public static Socket socket = null;
    public InputStream inStream = null;
    public OutputStream outStream = null;
	private static JFrame frame;
	private static JPanel panel;
    public static JTextArea textArea;
    public static int clients = 1;
	static ArrayList <RegisterInfo> registerInfoList= new ArrayList<RegisterInfo>();
	
	
	public static void register() throws IOException
	{
		int serverPort = 7787; 
		ServerSocket serverSocket =null;
		try 
		{
			serverSocket = new ServerSocket(serverPort);
			System.out.println("Server socket created waiting for clients");
			textArea.append("Server socket created waiting for clients\n");
		while(true) {
			
			Socket connectedSocket = serverSocket.accept();
			BufferedReader in1 = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));
			PrintWriter out1 = new PrintWriter(connectedSocket.getOutputStream(), true);
			String responeFromClient = in1.readLine();
			System.out.println(responeFromClient);
			
			String[] split = responeFromClient.split(",");
			RegisterInfo register = new RegisterInfo();
			if(split[0].equals("register"))
			{
				register.setClientId(clients++);
				register.setPort(Integer.parseInt(split[1]));
				register.setIpAddress(split[2]);
				register.setSharedFilePath(split[3]);
				register.setFileList(split[4]);
				registerInfoList.add(register);
				System.out.println(register.getClientId() + register.getIpAddress() + register.getPort() + register.getSharedFilePath());
				textArea.append(" Connected Client with information:\n");
				textArea.append(" Client Id : " +register.getClientId() +" Client IP Address : " +register.getIpAddress() +" Client Port : " +register.getPort() +" Client Shared File Path : "+ register.getSharedFilePath()+"\n");
				out1.println(register.getClientId());

			}
			else if(split[0].equals("query"))
			{
				String searchFile = split[1];
				int ClientId = Integer.parseInt(split[2]);
				boolean found = false;
				if(searchFile.contains("all files"))
				{
					textArea.append("\n\n \t********** Query from Client "+split[3]+" **************");
					textArea.append("\n Required all file for Client with ID : "+ClientId);
					for( int i=0;i<registerInfoList.size();i++)
					{
						if(registerInfoList.get(i).getClientId() == ClientId)
						{
							out1.println(registerInfoList.get(i).getFilesList());
							String fileName = registerInfoList.get(i).getFilesList().replace("---", "");
							textArea.append("\n "+fileName);
							found = true;
						}
					}
					if(!found)
						out1.println("No Such Client Available With Client Id: "+ClientId);
				}
				else {

					textArea.append("\n\n \t********** Query from Client "+split[2]+" **************");
					int NumofHits = 0;
					String queryHit = "";
					textArea.append("\n In Server Query Hit for File:"+  searchFile);
					textArea.append("\n --------------------------------------------------------------------------------------------");
					textArea.append("\n |  Client Id |  IP Address | Server Port |     fileName   |           Size            |");
					textArea.append("\n --------------------------------------------------------------------------------------------");
					
					for(int i=0;i<registerInfoList.size();i++)
					{
						if(registerInfoList.get(i).getClientId() != Integer.parseInt(split[2]))
						if(registerInfoList.get(i).searchforFile(searchFile))
						{
							NumofHits++;
							String file = registerInfoList.get(i).getSharedFilePath() +"\\" + searchFile;
							File openFile = new File(file);
							double fileSize = openFile.length();
							double kb = fileSize/1024;
							String a="|       " +registerInfoList.get(i).getClientId() + "         |  " + registerInfoList.get(i).getIpAddress() + "    |       " +
									registerInfoList.get(i).getPort() + "      |  " + file + "   |  " +kb+"KB" + " |";
							textArea.append("\n "+a);
							queryHit+=a+"-----";
						}
					}
					textArea.append("\n--------------------------------------------------------------------------------------------");
					if(NumofHits ==0)
					{
					
					textArea.append("No Client with Such a File" + searchFile);	
					queryHit="No Client with Such a File" + searchFile;
					}	
					else
					{
					textArea.append("\n Number of Hits:"+NumofHits);
					queryHit += Integer.toString(NumofHits);
					System.out.println("In Server Query Hit" +NumofHits);
					
					}
					out1.println(queryHit);
				}
			}
			else if(split[0].equals("update"))
			{
				try
				{
					//String file = split[1];
					for(int i=0;i<registerInfoList.size();i++)
					{
						if(registerInfoList.get(i).getClientId()==Integer.parseInt(split[2]))
						{
							if(!registerInfoList.get(i).searchforFile(split[1]))
							{
								registerInfoList.get(i).addFile(split[1]);
								out1.println("Update ClientId: "+registerInfoList.get(i).getClientId()+ "Shared File List");
								textArea.append("Update ClientId: "+registerInfoList.get(i).getClientId()+ "Shared File List");
							}
							else
							{
								out1.println("Already Existing with Client " +registerInfoList.get(i).getClientId());
								textArea.append("Already Existing with Client " +registerInfoList.get(i).getClientId());
							}
							
						}
						break;
								
					}
				}
				
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else if(split[0].equals("list"))
			{
				try
				{
					String listClients = "";
					for(int i=0;i<registerInfoList.size();i++)
					{
						listClients +=registerInfoList.get(i).getClientId() +"-"+registerInfoList.get(i).getIpAddress()+",";
					}
					out1.println(listClients);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else if(split[0].equals("ping"))
			{
				try {
				int port = Integer.parseInt(split[4]);
				String ipAddress = split[3];
				int requestId = Integer.parseInt(split[2]);
				textArea.append("ping received from Client Id:" + split[1]);
				textArea.append("Ping Information is: Connecting Id:"+requestId + "port and Ip are"+ipAddress+port);
				
				Socket testSocket = new Socket(ipAddress,port);
				testSocket.close();
				String pong = "";
				boolean notfound = true;
				for(int i=0;i<registerInfoList.size();i++)
				{
					if(registerInfoList.get(i).getClientId() == requestId)
					{
						pong = requestId +"," + ipAddress + "," + port + ",";
						
						String filesList = registerInfoList.get(i).getFilesList();
						int filesCount = registerInfoList.get(i).getFilesCount();
						pong += filesCount + "," + filesList;
						notfound = false;
					}
				
				}
				if(notfound)
					out1.println("No such Client Available");
				else
					out1.println(pong);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					out1.println("Requested Client Not available for Download");
				}
			}
			
		}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		finally{
			serverSocket.close();
		}
	}
	


    public Server() {
    	frame = new JFrame("Server");
    	frame.setSize(750, 450);
    	panel = new JPanel();
    	panel.setLayout(new FlowLayout());
    	textArea = new JTextArea(25, 66);
    	textArea.setEditable(false);
    	panel.add(textArea);
    	panel.add(new JScrollPane(textArea));
    	frame.add(panel);
    	frame.setLocationRelativeTo(null);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
      }
 
    public static void main(String args[]) throws IOException
	{
    	new Server();
		Server.register();
	}

}
