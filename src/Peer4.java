
import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Peer4 extends Thread{
	public static int i=1;
	public ServerSocket severSocket = null;
    public static Socket socket = null;
    public InputStream inStream = null;
    public OutputStream outStream = null;
	public static int peers = 1;
	public static int clients=peers;
	private JFrame frame;
	private JTextField textField;
	private static TextArea textArea2;
	private static TextArea textArea;
	private static TextArea textArea1;
	private JPanel panel,panel1; 
	private JButton button,button1,button2,button3,button4,button5;
	
	
	ServerSocket listener = null;
	
	
	String sharedFilePath;
    String outputPath;
    String ipAddress;
    public int port;
    int filePort;
    int clientId;
    int serverPort;
    String serverAddress;
    RegisterInfo register;
	@SuppressWarnings("unused")

	private static int var;
	boolean registered = false;
	
	
	List<RegisterInfo> neighborInfoList;
	
	String inputString;
	private Socket connectedSocket;
	private int mc;
	private String filesList="";
	
	private String downloadSeq="";

	/**
	 * Create the application.
	 */
	public Peer4(final int port, final String ipAddress,String sharedFilePath,String outputPath) {
		
		
		this.port = port;
	   	this.ipAddress = ipAddress;
    	this.sharedFilePath = sharedFilePath;
    	this.outputPath = outputPath;
    	register = new RegisterInfo();
        register.setIpAddress(ipAddress);
        register.setPort(port);
        register.setSharedFilePath(sharedFilePath);
        register.setClientId(port);
        
      File folder = new File(sharedFilePath);
		File[] listOfFiles = folder.listFiles(); 	  
		 for (int i = 0; i < listOfFiles.length; i++) 
		  {
			 filesList += listOfFiles[i].getName() + "---";
		  }
		 	System.out.println(filesList);
		 	register.setFileList(filesList);

    	
    	this.neighborInfoList = new ArrayList<RegisterInfo>();
    	
    	
    	frame = new JFrame("Peer running at "+ipAddress+" and "+port);
    	frame.setBounds(300, 500, 850, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 405, 450);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		textArea1 = new TextArea();
		textArea1.setForeground(Color.BLACK);
		
		textArea1.setBackground(Color.WHITE);
		textArea1.setEditable(false);
		textArea1.setBounds(10, 290, 385, 131);
		panel.add(textArea1);
		
		button5 = new JButton("Help");
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea1.setText("");
				textArea1.append("Peer's ServerSocket is created at \nIP ADDRESS \t: "+ipAddress+"\nPORT \t\t:"+port);
				final JFrame help1 = new JFrame("HELP");
	        	help1.setVisible(true);
	    		help1.setBounds(100, 100, 450, 300);
	    		JPanel contentPane = new JPanel();
	    		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    		help1.setContentPane(contentPane);
	    		contentPane.setLayout(null);
	    		
	    		JPanel panel = new JPanel();
	    		panel.setBounds(0, 0, 434, 250);
	    		contentPane.add(panel);
	    		panel.setLayout(null);
	    		
	    		JTextArea txtrEnterYourOption = new JTextArea();
	    		txtrEnterYourOption.setText("Enter your option for Help command\r\nFor REGISTER AS NEIGHBOUR \t- 1\r\nFor QUERY \t\t\t- 2\r\nFor QUERY ALL\t\t\t- 3\r\nFor NEIGHBOUR OF THIS PEER\t- 4\r\nFor NEIGHBOURS OF ANOTHER PEER\t- 5\r\nFor PING\t\t\t- 6\r\nFor FILE DOWNLOAD \t\t -7");
	    		txtrEnterYourOption.setBounds(0, 11, 424, 129);
	    		txtrEnterYourOption.setEditable(false);
	    		panel.add(txtrEnterYourOption);
	    		
	    		textField = new JTextField();
	    		textField.setToolTipText("Enter Here");
	    		textField.setBounds(10, 159, 386, 20);
	    		panel.add(textField);
	    		textField.setColumns(10);
	    		
	    		JButton btnOk = new JButton("OK");
	    		btnOk.addActionListener(new ActionListener() {
	    			public void actionPerformed(ActionEvent e) {
	    				String input=textField.getText();
	    				
	    				int in = Integer.parseInt(input);
	    				switch(in)
	    				{
	    					case 1:textArea1.append("\nCONNECTING TO PEER\n Enter the IP Address, Port number of peer");
	    						break;
	    					case 2:textArea1.append("\nQUERY  FOR SPECIFIED FILE\n Enter the file name");
	    						break;
	    					case 3:textArea1.append("\nQUERY FOR ALL FILES OF ANOTHER PEER\n Enter the keyword  'queryall', IP Address, Port No");
	    						break;
	    					case 4:textArea1.append("\nNEIGHBOUR\n Just Click Neighbours button");
	    						break;
	    					case 5:textArea1.append("\nNEIGHBOUR LIST OF ANOTHER PEER\n Enter the Keyword 'listNeighbors', IP Address, Port No");
    							break;
	    					case 6:textArea1.append("\nPING\n Enter the IP Address, Port No");
							break;
	    					case 7:textArea1.append("\nDOWNLOAD\t\n Enter the IP address, port number, required File\n");
	    						break;
	    				}
	    				
	    				help1.dispose();
	    			}
	    		});
	    		btnOk.setBounds(156, 200, 130, 23);
	    		panel.add(btnOk);
			}
		});
		button5.setBounds(92, 427, 239, 23);
		panel.add(button5);
	//	button5.setActionCommand("help");
		
		
		
		textArea2 = new TextArea();
		//textArea2.setEnabled(false);
		textArea2.setForeground(Color.BLACK);
		textArea2.setBackground(Color.WHITE);
		//textArea2.setEnabled(false);
		textArea2.setEditable(false);
		textArea2.setBounds(10, 29, 385, 233);
		panel.add(textArea2);
		
		JLabel lblhelp = new JLabel("-----------------HELP----------------");
		lblhelp.setBounds(127, 268, 162, 23);
		panel.add(lblhelp);
		
		JLabel lblUpdatingFromOther = new JLabel("                 Updating from other");
		lblUpdatingFromOther.setBounds(68, 3, 188, 23);
		panel.add(lblUpdatingFromOther);
		
				panel1 = new JPanel();
		panel1.setBounds(415, 0, 419, 450);
		frame.getContentPane().add(panel1);
		panel1.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 328, 394, 20);
		panel1.add(textField);
		textField.setColumns(10);
		
		button = new JButton("Register");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			        	try {
			        	//	textArea1.append("Connecting to Server With");
			        		textArea.append("\n\t*********** REGISTER ************\n");
			        		registerToMainServer();
			        		textArea.append("\n\t*********** END REGISTER ************\n");
			        				
			        	} 
			        	catch (UnknownHostException u) {
			        		textArea1.append("Connection failed Server with such port no. is not available\n");
			        		u.printStackTrace();
			        	//	textArea1.append("Connection failed Server with such port no. is not available\n");
			        		}
			        	
			        	catch (IOException io) {
			        		io.printStackTrace();
			        		}	
			        }
		});
		button.setBounds(10, 359, 394, 23);
		panel1.add(button);
				
		button1 = new JButton("Query");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					if(textField.getText().contains("queryall"))
					{
						textArea.append("\n\t*********** QEURY ALL ************\n");
		        		queryAll();
		        		textArea.append("\n\t*********** END QUERY ************\n");
					}
						
					else{
						textArea.append("\n\t*********** QUERY FILE ************\n");
						query();
						textArea.append("\n\t*********** END QUERY  ************\n");
						}
					}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
			
			}
		});
		button1.setBounds(10, 393, 156, 23);
		panel1.add(button1);
		button2 = new JButton("Neighbours");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					if(textField.getText().contains("listNeighbors"))
					{
						textArea.append("\n\t*********** LISTS OF NEIGHBOURS OF ANOTHER ************\n");
						list();
						textArea.append("\n\t*********** END NEIGHBOR LIST ************\n");
					}
					else
					{
						//textArea.append("\n\t*********** LIST OF ITS NEIGHBORS ************");
					listNeighbors();
					//textArea.append("\n\t*********** END NEIGHBOR LIST ************");
					}
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
			
			}
		});
		button2.setBounds(258, 393, 146, 23);
		panel1.add(button2);
		button2.setActionCommand("neighbours");
		
		button3 = new JButton("Ping");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					checkPing();
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
			
			}
		});
		button3.setBounds(10, 427, 156, 23);
		panel1.add(button3);
		button3.setActionCommand("ping");
		
		button4 = new JButton("File Download");
		button4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				inputString = textField.getText() ;
	        	textField.setText("") ;
	        	inputString= "download,"+inputString;
	        	textArea.append("\t******** DOWNLOAD START*******\n");
	        	
	        	
	        	  
	        	String split[] = inputString.split(",");
	        	if(split.length==2)
	        	{
	        		String[] downloadSplit = downloadSeq.split("-");
	        		for(int i=0;i<downloadSplit.length;i++)
	        		{
	        			if(i==Integer.parseInt(split[1]))
	        			{
		        			String[] download = downloadSplit[i].split(",");
		        			textArea.append(" Connecting to client " +download[1] + " at Port " + download[2] +"\n");
		        			try {
		        				inputString = "download,"+download[1]+","+download[2]+","+download[3];
								connectToServer(Integer.parseInt(download[2]),download[1]);
							} catch (NumberFormatException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							textArea.append("\t********** DOWNLOAD END*******\n");
	        			}

	        			
	        			
	        		}
	        			
	        	}
	        	else {
	        	try {
					try {
						textArea.append(" Connecting to client " +split[1] + " at Port " + split[2] +"\n");
						connectToServer(Integer.parseInt(split[2]),split[1]);
						textArea.append("\t********** DOWNLOAD END*******\n");
					//	update(split[2]);
					} catch (IOException e2) {
							e2.printStackTrace();
					}
				} catch (NumberFormatException e2) {
							e2.printStackTrace();
				}
			}
			}
			});
		
		button4.setBounds(258, 427, 146, 23);
		panel1.add(button4);
		button4.setActionCommand("download");
		
		textArea = new TextArea();
		//textArea.setEnabled(false);
		textArea.setBounds(10, 27, 394, 290);
		panel1.add(textArea);
		
		JLabel lblNewLabel = new JLabel("Peer's view");
		lblNewLabel.setBounds(183, 2, 77, 23);
		panel1.add(lblNewLabel);
		start();
		textArea1.setText("");
		textArea1.append("Peer's ServerSocket is created at \nIP ADDRESS \t: "+ipAddress+"\nPORT \t\t: "+port);
		
	}
	public void queryAll()
	{
		inputString = textField.getText();
		textField.setText("");
		String[] split = inputString.split(",");
		Socket s;
		try {
			s= new Socket(split[1],Integer.parseInt(split[2]));
			BufferedReader queryIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter queryOut = new PrintWriter(s.getOutputStream(), true);
			
			queryOut.println("queryAll");
			InputStream is = s.getInputStream();
			while(is.available() == 0);
			String response = queryIn.readLine();
			String[] split1 = response.split("---");
			textArea.append("Query All Response\n");
			s.close();
			for(int i=0;i<split1.length;i++)
			{
				textArea.append(split1[i]+"\n");
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void list()
	{
		try
		{
			inputString = textField.getText();
			textField.setText("");
			String[] split = inputString.split(",");
			textArea.append("Neighbors of With Ip Address"+split[1]+" and Port is "+split[2]+"\n");
			
			Socket s= new Socket(split[1],Integer.parseInt(split[2]));
			BufferedReader queryIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter queryOut = new PrintWriter(s.getOutputStream(), true);
			
			queryOut.println("listNeighbors,");
			InputStream waitForStream = s.getInputStream();
			while(waitForStream.available()==0);
			String response = queryIn.readLine();
			if(response.contains("No Neighbors"))
			{
				textArea.append("No Neighbors\n");
			}
			else
			{
				String[] neighborSplit = response.split(",");
				
				for(int i=0;i<neighborSplit.length;i++)
				{
					if(i==0)
					{
					 textArea.append("\t*********** LIST NEIGHBORS ************");
			    	 textArea.append("\n------------------------------------------------------------");
					 textArea.append("\n|  Neighbor Id              |     IP Address                  |");
					 textArea.append("\n-------------------------------------------------------------\n");
					}
					String[] neighbor = neighborSplit[i].split("-");
					textArea.append(neighbor[0]+"              "+"  " + neighbor[1]+"\n");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void query()
	{
		boolean found = false;
		//String output ="";
		inputString = textField.getText();
		textField.setText("");
		downloadSeq="";
		try 
		{
			
			if(!found)
			{
			//	System.out.println("Not found with in Neighbors");
				for(int i=0;i<neighborInfoList.size();i++)
				{
					Socket s = new Socket(neighborInfoList.get(i).getIpAddress(),neighborInfoList.get(i).getPort());
					BufferedReader queryIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintWriter queryOut = new PrintWriter(s.getOutputStream(), true);
			
				
					queryOut.println("query,"+inputString);
					InputStream waitForStream = s.getInputStream();
					while(waitForStream.available()==0);
					String response = queryIn.readLine();
					
					s.close();
					if(response.contains("Not Available"))
					{
						System.out.println("Neighbor Neighbor also does not have");
						
						
					}
					else 
					{
						downloadSeq += response+","+inputString +"-";
						if(found==false){
							textArea.append("Query Hit From Peer is:  " );
							textArea.append("\n----------------------------------------");
							textArea.append("\n|  Client Id |  IP Address | Server Port");
							textArea.append("\n----------------------------------------\n");
							}
						found = true;
						String[] querySplit = response.split(",");
						
						textArea.append(querySplit[0]+" | "+querySplit[1]+" | "+querySplit[2]+"\n");
						
					}
					if(found)
					{
						//break;
					}
				}
			}
			if(!found)
				textArea.append("\nNo Client Has Requested File");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		

	
	
	  public int registerToMainServer() throws UnknownHostException, IOException {
	    	inputString = textField.getText();
	    	textField.setText("");
	    	String inputToRegister=inputString;
	    	String[] regi= inputToRegister.split(",");
	    	
	    	serverPort = Integer.parseInt(regi[1]);
	    	serverAddress = regi[0];
	        Socket s = new Socket(serverAddress, serverPort);
	        BufferedReader in1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
	       PrintWriter out1 = new PrintWriter(s.getOutputStream(), true);
	       try {
	    	   String filesList = "";
	    	   File folder = new File(sharedFilePath);
	 		   File[] listOfFiles = folder.listFiles(); 	  
	 		 for (int i = 0; i < listOfFiles.length; i++) 
	 		  {
	 			 filesList += listOfFiles[i].getName() + "---";
	 		  }
	 		 	System.out.println(filesList);
	            String inputToServer = "register,"+port+","+ipAddress+","+sharedFilePath +"," + filesList;
			    out1.println(inputToServer);
			    String responseFromServer = in1.readLine();
			    clientId = Integer.parseInt(responseFromServer);
			    System.out.println("Neighbor Id is:"+clientId);
			    textArea.append("Adding as a Neighbour to Peer\nIP ADDRESS \t: "+regi[0]+"\nPORT \t\t:"+regi[1]);
			    textArea.append("\nNeighbor Id is:"+clientId);
			    s.close();
			    registered = true;
	        }
	        catch(Exception e)
	        {
	        	
	        	textArea.append("\nOOPS!!!  \nConnection failed peer is not avaliable at such IP address and port number\n");
	        	e.printStackTrace();
	        }
	            return clientId;
	    }
	  
	  public void run()
      {
		  System.out.println("Peer's ServerSocket Started at IP address "+ipAddress+" and Port "+port);
	 		// String fileToSearch = "";
      	String output ="";
 		 try 
           {
  			listener = new ServerSocket(port);
  		while(true)
		{
  			
			connectedSocket = listener.accept();
			BufferedReader in1 = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));
			PrintWriter out1 = new PrintWriter(connectedSocket.getOutputStream(), true);
			String responeFromClient = in1.readLine();
			System.out.println(responeFromClient);
			
			String[] split = responeFromClient.split(",");
			
			if(split[0].equals("register"))
			{
			
				RegisterInfo register = new RegisterInfo();
				register.setClientId(clients++);
				register.setPort(Integer.parseInt(split[1]));
				register.setIpAddress(split[2]);
				register.setSharedFilePath(split[3]);
				register.setFileList(split[4]);
				neighborInfoList.add(register);
				System.out.println(register.getClientId() + register.getIpAddress() + register.getPort() + register.getSharedFilePath());
				textArea2.append(" Neighbor is added newly with information:\n");
				textArea2.append(" ID: " +register.getClientId() +" IP Address : " +register.getIpAddress() +" Port : " +register.getPort() +" Shared File Path : "+ register.getSharedFilePath()+"\n");
				out1.println(register.getClientId());
			}
			
			else if(split[0].equals("download"))
			{
				textArea2.append("\n Peer is connected to download the file : "+ split[3]);
              	 new FileDownload(connectedSocket, split[3],sharedFilePath).start();
			} 
			else if(split[0].equals("query"))
			{
				boolean found = false;
				//String output ="";
				/*
				for(int i=0;i<neighborInfoList.size();i++)
				{
				*/
				textArea2.append("Request for Query Received from another peer for file : "+split[1]+"\n");
				textArea2.append("\nSearching file at this peer \n");	
				if(register.searchforFile(split[1]))
					{
						found = true;
						//out1.println(neighborInfoList.get(i).getIpAddress() + neighborInfoList.get(i).getPort());
						
						output = register.getClientId() + "," + register.getIpAddress() +","+ register.getPort();
						String[] querySplit = output.split(",");
						textArea2.append("\nFile found at this peer\n");
						textArea2.append("Query Hit From Peer is:  " );
						textArea2.append("\n---------------------------------------------------------");
						textArea2.append("\n|  Peer Id\t |  IP Address \t| Server Port\t");
						textArea2.append("\n---------------------------------------------------------\n");
						textArea2.append(querySplit[0]+"\t | "+querySplit[1]+" \t| "+querySplit[2]+"\t\n");
						//in1.notifyAll();
						out1.println(output);
						
					}
					/*
				}
				*/
				if(!found)
				{
					
					for(int i=0;i<neighborInfoList.size();i++)
					{
						textArea2.append("\nChecking with Neighbor \n"+neighborInfoList.get(i).getIpAddress() +" " + neighborInfoList.get(i).getPort()+"\n");
						Socket s = new Socket(neighborInfoList.get(i).getIpAddress(),neighborInfoList.get(i).getPort());
						BufferedReader queryIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
						PrintWriter queryOut = new PrintWriter(s.getOutputStream(), true);
				
					
						queryOut.println("query,"+split[1]);
						InputStream waitForStream = s.getInputStream();
						while(waitForStream.available()==0);
						String response = queryIn.readLine();
						
						if(response.contains("Not Available"))
						{
							textArea2.append("Not available at Peer\n");
						}
						else 
						{
							textArea2.append("Available at Peer\n"+ response);
							String[] querySplit = response.split(",");
							textArea2.append("\nFile found at this peer\n");
							textArea2.append("Query Hit From Peer is:  " );
							textArea2.append("\n---------------------------------------------------------");
							textArea2.append("\n|  Peer Id\t |  IP Address \t| Server Port\t");
							textArea2.append("\n---------------------------------------------------------\n");
							textArea2.append(querySplit[0]+"\t | "+querySplit[1]+" \t| "+querySplit[2]+"\t\n");
							//in1.notifyAll();
							//out1.println(re);
							found = true;
							out1.println(response);
							s.close();
						}
						if(found){}
							
							
					}
				}
				if(!found)
				{
					out1.println("Not Available");
					textArea2.append("Not available with Me and My Neighbors\n");
				}
				//	out1.println(output);
				System.out.println("Neighbor Found");
			}
			else if(split[0].contains("queryAll"))
			{
				out1.println(filesList);
			}
			else if(split[0].equals("ping"))
			{
				out1.println(filesList);
			}
			else if(split[0].contains("listNeighbors"))
			{
				String neighbor ="";
				if(neighborInfoList.size()==0)
				{
					out1.println("No Neighbors");
				}
				else
				{
					for(int i=0;i<neighborInfoList.size();i++)
					{
						neighbor += neighborInfoList.get(i).getClientId() +"-"+ neighborInfoList.get(i).getIpAddress() +","; 
					}
					out1.println(neighbor);
				}
			}
			//connectedSocket.close();
			}
           }
  		 catch(Exception e)
  		 {
  			textArea.append("Peers socket was not listening for any call");
  			textArea1.append("Peers socket was not listening for any call");
  			textArea2.append("Peers socket was not listening for any call");
 			e.printStackTrace();
  		 }
  		 	
  		 
  }
	  
	  public void connectToServer(int portNo,String ServerAddress) throws IOException {
			Socket socket = new Socket(ServerAddress, portNo);
	        BufferedReader in1 = new BufferedReader(
	                new InputStreamReader(socket.getInputStream()));
	        PrintWriter out1 = new PrintWriter(socket.getOutputStream(), true);
	        
	        try {
	        	 String split[] = inputString.split(",");
			        System.out.println("Enter File You need");
			        File file = new File(outputPath+"\\"+split[3]);
			        
				        out1.println(inputString);
				     //   InputStream is = socket.getInputStream();
				       // while(is.available()==0);
				        String response = in1.readLine();
				        
						if(response.contains("No Such File With Me"))
				        {
				        	System.out.println("No Such File With Me");
				        	textArea.append("No Such File With Me");
				        //	System.exit(0);
				        }
			        
				        else if(file.exists())
				        mc = JOptionPane.QUESTION_MESSAGE;
						textArea.append("File with this name already exists !!!");
				        String[] opts = {"YES", "NO", "RENAME"};
				        
				        int ch = JOptionPane.showOptionDialog (null, "File with same already exist!!! Do you want to replace it?", "??? ALREADY EXIST???", 0, mc, null, opts, opts[1]);
				        if(ch==0)
				        {
				        
			            BufferedWriter output = new BufferedWriter(new FileWriter(file));
			            output.write(response);
			            output.flush();
			            output.close();
			            /*
				        FileWriter writer = new FileWriter(outputPath+"\\"+split[2]);
				        writer.append(response);
				        writer.flush();
				        writer.close();
				        */
				        System.out.println("File is Successfully Downloaded" + response);
				        textArea.append("\nFile is Successfully Downloaded and replaced the pervious one\n");
				        
				        
			        }
				        else if (ch==2)
				        {
				        String	newFile = 
				        	        JOptionPane.showInputDialog ( "Enter the file name" ); 
				        File newFileName = new File(outputPath+"\\"+ newFile);
				        textArea.append("\n"+newFile+"\n");
				        BufferedWriter output = new BufferedWriter(new FileWriter(newFileName));
			            output.write(response);
			            output.flush();
			            output.close();
			            textArea.append("\nFile is Successfully Downloaded and saved in new file :"+newFile+"\n");
				         
				        }
				        else
				        	textArea.append("\nFile was not Downloaded because you are not interested to replace the file\n");
				        
			        socket.close();
	        }
	        catch(Exception e)
	        {
	        	textArea.append("Peer was not active right now. You may try later");
	        	e.printStackTrace();
	        }
	        
	    }  

	  
	  
	  public void listNeighbors()
		 {
			 try {   
		    	 textArea.append("\t*********** LIST NEIGHBORS ************");
		    	 textArea.append("\n------------------------------------------------------------");
				 textArea.append("\n|  Neighbor Id              |     IP Address                  |");
				 textArea.append("\n-------------------------------------------------------------");
			
		    	 for(int i=0;i<neighborInfoList.size();i++)
		    	 {
		    		 textArea.append("\n|  "+ neighborInfoList.get(i).getClientId()+"            |     "+neighborInfoList.get(i).getIpAddress()+ "     |");
		    	 }
		    	 textArea.append("\n-------------------------------------------------------------\n");
		    	 textArea.append("\t******** END LIST NEIGHBORS *******\n");
			 }
		    	catch(Exception e)
		    	{
		    		textArea.append("OOPS!!!  \nConnection failed peer is not avaliable at such IP address and port number\n");
		    		e.printStackTrace();
		    	}
		 }

	   public void checkPing()
	   {
		   
		   Socket testSocket = null;
		   try {
			    inputString = textField.getText();
			    textField.setText("");
			    String[] split = inputString.split(",");
				int port = Integer.parseInt(split[1]);
				String ipAddress = split[0];
				textArea.append("*********** PING ************\n");
				textArea.append("\nPing To Client with IPAddress is:" + ipAddress +" Port is: "+ port+"\n");
				testSocket = new Socket(ipAddress,port);
				BufferedReader queryIn = new BufferedReader(new InputStreamReader(testSocket.getInputStream()));
				PrintWriter queryOut = new PrintWriter(testSocket.getOutputStream(), true);
				queryOut.println("ping,");
				String response = queryIn.readLine();
				String[] file = response.split("---");
				textArea.append("File List is:\n");
				for(int i=0;i<file.length;i++)
					textArea.append(file[i]+"\n");
				testSocket.close();
				textArea.append("Client is Available\n");
				textArea.append("*********** END OF PING ************\n");
				}
		   catch(Exception e)
		   {
			   e.printStackTrace();
			  
			   textArea.append("OOPS!!!  \nConnection failed peer is not avaliable at such IP address and port number\n");
			   textArea.append("*********** END OF PING ************\n");
			   try {
				testSocket.close();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		   }

	   }
	  
	/**
	 * Launch the application.
	 */
	   public static void main(String[] args) throws UnknownHostException {
			InetAddress ip;
			ip = InetAddress.getLocalHost();
			Peer4 window = new Peer4(1111,ip.getHostAddress(),"input4","output4");
			window.frame.setVisible(true);
		}
 }
        
