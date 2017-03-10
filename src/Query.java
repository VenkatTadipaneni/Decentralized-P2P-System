import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
public class Query {
	
	private String ipAddress;
	private int port;
	public Query(String ipAddress,int port)
	{
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void run()
	{
		try
		{
			Socket s = new Socket(ipAddress,port);
			BufferedReader queryIn = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));
			PrintWriter queryOut = new PrintWriter(connectedSocket.getOutputStream(), true);
			//queryIn.wait();
		
			queryOut.println("query,"+inputString);
			InputStream waitForStream = s.getInputStream();
			while(waitForStream.available()==0);
			String response = queryIn.readLine();
			queryOut.println(response);
			s.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
*/