import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class FileDownload extends Thread {
        private Socket socket;
        private String fileName;
        private String sharedFilePath;

        public FileDownload(Socket socket, String fileName,String sharedFilePath) {
        	System.out.println("Server with" + socket +"Started");
            this.socket = socket;
            this.fileName = fileName;
            this.sharedFilePath = sharedFilePath;
        //    log("New connection with client# " + clientNumber + " at " + socket);
        }

        
        public void run() {
            try {

            	//System.out.println("Bharat testing");
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                   while (true) {
                //    String input = in.readLine();
                    
                    
                    File file = new File(sharedFilePath+"\\"+fileName);
                    if(file.exists())
                    {
                    	FileInputStream fis = new FileInputStream(file);
                    	byte[] data = new byte[(int) file.length()];
                    	
                    	fis.read(data);
                    	fis.close();
                    	String str = new String(data, "UTF-8");
                    	out.println(str);
                    }
                    else
                    	out.println("No Such File With Me");
                }
            } catch (IOException e) {
               // log("Error handling client# " + clientNumber + ": " + e);
            	e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
              //  log("Connection with client# " + clientNumber + " closed");
            }
        }
        
        private String log(String message) {
            return message;
        }


		
}
