
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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Peer3 extends Thread{
	public static int i=1;
	public ServerSocket severSocket = null;
    public static Socket socket = null;
    public InputStream inStream = null;
    public OutputStream outStream = null;
	public static int peers = 1;
	public static int clients=peers*10+1;

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
	


	/**
	 * Create the application.
	 */
	public Peer3(final int port, String ipAddress,String sharedFilePath,String outputPath) {
		
		
		this.port = port;
	   	this.ipAddress = ipAddress;
    	this.sharedFilePath = sharedFilePath;
    	this.outputPath = outputPath;

    	
    	this.neighborInfoList = new ArrayList<RegisterInfo>();
    	
    	
		frame = new JFrame("PEER3");
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
		//textArea2.setEnabled(false);
		textArea1.setEditable(false);
		//textArea1.setEnabled(false);
		textArea1.setBounds(10, 290, 385, 131);
		panel.add(textArea1);
		
		button5 = new JButton("Help");
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea1.setText("");
				textArea1.append("Client Server Started at socket "+port);
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
	    		txtrEnterYourOption.setText("Enter your option for Help command\r\nFor REGISTER         \t\t- 1\r\nFor QUERY \t\t\t- 2\r\nFor NEIGHBOUR\t\t- 3\r\nFor PING\t\t\t- 4\r\nFor FILE DOWNLOAD \t\t -5");
	    		txtrEnterYourOption.setBounds(0, 11, 424, 129);
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
	    					case 3:textArea1.append("\nNEIGHBOUR\t\n Just Click Neighbours button");
	    						break;
	    					case 4:textArea1.append("\nPING\t\n Enter the Client Id,IpAddress, Port No");
	    						break;
	    					case 5:textArea1.append("\nDOWNLOAD\t\n Enter the port number, IP address, required File\n");
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
			        		registerToMainServer();
			             		
			        			
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
					query();
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
			
			}
		});
		button1.setBounds(10, 393, 156, 23);
		panel1.add(button1);
		button1.setActionCommand("query");
		
		button2 = new JButton("Neighbours");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					listNeighbors();
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
	        	try {
					try {
						textArea.append(" Connecting to client " +clientId + " at Port " + split[1] +"\n");
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
    	textArea1.append("Client Server Started at socket "+port);
		
		
	}
	
	
	public void query()
	{
		boolean found = false;
		String output ="";
		inputString = textField.getText();
		try 
		{
			for(int i=0;i<neighborInfoList.size();i++)
			{
				if(neighborInfoList.get(i).searchforFile(inputString))
				{
					found = true;
					//out1.println(neighborInfoList.get(i).getIpAddress() + neighborInfoList.get(i).getPort());
					output = neighborInfoList.get(i).getClientId() + "," + neighborInfoList.get(i).getIpAddress() +","+ neighborInfoList.get(i).getPort();
					String[] querySplit = output.split(",");
					textArea2.append("Query Hit From Server is:  " );
					textArea2.append("\n----------------------------------------");
					textArea2.append("\n|  Client Id |  IP Address | Server Port");
					textArea2.append("\n----------------------------------------");
					textArea2.append(querySplit[0]+" | "+querySplit[1]+" | "+querySplit[2]);		
				}
			}
			if(!found)
			{
				for(int i=0;i<neighborInfoList.size();i++)
				{
					Socket s = new Socket(neighborInfoList.get(i).getIpAddress(),neighborInfoList.get(i).getPort());
					BufferedReader queryIn = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));
					PrintWriter queryOut = new PrintWriter(connectedSocket.getOutputStream(), true);
				//	queryIn.wait();
					String response = queryIn.readLine();
					queryOut.println(response);
					s.close();
					String[] querySplit = response.split(",");
					textArea2.append("Query Hit From Server is:  " );
					textArea2.append("\n----------------------------------------");
					textArea2.append("\n|  Client Id |  IP Address | Server Port");
					textArea2.append("\n----------------------------------------");
					textArea2.append(querySplit[0]+" | "+querySplit[1]+" | "+querySplit[2]);
					break;
				}
			}
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
			    textArea.append("Adding as a Neighbor to Client With Ip Address: "+ regi[0]+" and Port: "+regi[1]);
			    s.close();
			    registered = true;
	        }
	        catch(Exception e)
	        {
	        	
	        	textArea.append("Connection failed Server with such port no. is not available\n");
	        	e.printStackTrace();
	        }
	            return clientId;
	    }
	  
	  public void run()
      {
      	 System.out.println("Client Server Started at socket "+port);
      	 String fileToSearch = "";
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
			RegisterInfo register = new RegisterInfo();
			if(split[0].equals("register"))
			{
				
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
		
              	 new FileDownload(connectedSocket, split[3],sharedFilePath).start();
			} 
			else if(split[0].equals("query"))
			{
				boolean found = false;
				String output ="";
				for(int i=0;i<neighborInfoList.size();i++)
				{
					if(neighborInfoList.get(i).searchforFile(split[1]))
					{
						found = true;
						//out1.println(neighborInfoList.get(i).getIpAddress() + neighborInfoList.get(i).getPort());
						output = neighborInfoList.get(i).getClientId() + "," + neighborInfoList.get(i).getIpAddress() +","+ neighborInfoList.get(i).getPort();
						String[] querySplit = output.split(",");
						textArea2.append("Query Hit From Server is:  " );
						textArea2.append("\n----------------------------------------");
						textArea2.append("\n|  Client Id |  IP Address | Server Port");
						textArea2.append("\n----------------------------------------");
						textArea2.append(querySplit[0]+" | "+querySplit[1]+" | "+querySplit[2]);
						in1.notifyAll();
					}
				}
				if(!found)
				{
					for(int i=0;i<neighborInfoList.size();i++)
					{
						Socket s = new Socket(neighborInfoList.get(i).getIpAddress(),neighborInfoList.get(i).getPort());
						BufferedReader queryIn = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));
						PrintWriter queryOut = new PrintWriter(connectedSocket.getOutputStream(), true);
					//	queryIn.wait();
						queryOut.println("query,"+split[1]);
						InputStream waitForStream = s.getInputStream();
						while(waitForStream.available()==0);
						String response = queryIn.readLine();
						queryOut.println(response);
						s.close();
						String[] querySplit = response.split(",");
						textArea2.append("Query Hit From Server is:  " );
						textArea2.append("\n----------------------------------------");
						textArea2.append("\n|  Client Id |  IP Address | Server Port");
						textArea2.append("\n----------------------------------------");
						textArea2.append(querySplit[0]+" | "+querySplit[1]+" | "+querySplit[2]);
						break;
					}
				}
				out1.println(output);
				System.out.println("Neighbor FOund");
			}
		}
           }
  		 catch(Exception e)
  		 {
  			 e.printStackTrace();
  		 }
  		 	
  		 
  }
	  
	  public void connectToServer(int portNo,String ServerAddress) throws IOException {
			Socket socket = new Socket(ServerAddress, portNo);
	        BufferedReader in1 = new BufferedReader(
	                new InputStreamReader(socket.getInputStream()));
	        PrintWriter out1 = new PrintWriter(socket.getOutputStream(), true);
	        
	        try {
	        	//inputString = textField.getText();
	        	 String split[] = inputString.split(",");
			        System.out.println("Enter File You need");
			      
				        out1.println(inputString);
				        String response = in1.readLine();
				        if(response.contains("No Such File With Me"))
				        {
				        	System.out.println("No Such File With Me");
				        	textArea.append("No Such File With Me");
				        //	System.exit(0);
				        }
			        else 
			        {
				        File file = new File(outputPath+"\\"+split[3]);
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
				        textArea.append("File is Successfully Downloaded\n");
				        
			        }
			        socket.close();
	        }
	        catch(Exception e)
	        {
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
				
				textArea.append("\nPing To Client with IPAddress is:" + ipAddress +" Port is: "+ port+"\n");
				
				testSocket = new Socket(ipAddress,port);
				testSocket.close();
				textArea.append("Client is Available\n");
				}
		   catch(Exception e)
		   {
			   e.printStackTrace();
			   textArea.append("No Such Client Available");
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
	public static void main(String[] args) {
		
					Peer3 window = new Peer3(3333,"localhost","input3","output3");
					window.frame.setVisible(true);
	}
 }
        
