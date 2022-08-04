// Clases usadas:
import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 

public class Server     // Main class. 
    {
        public static void main(String[] args)
            {
                //                                          This code will generate exceptions
                try     
                    {
                        ServerSocket server = new ServerSocket(5156);       // Creating the server instance, Port number 5156.
                        System.out.println("\n  >>>-------- THE SERVER STARTED --------<<<");
                        while (true)                                        // The server is always alive.
                            {
                                Socket socket_Cliente = server.accept();    // The server accept clients.
                                System.out.println("\t###  New online user  ###  ");                
                                DataInputStream entrada = new DataInputStream(socket_Cliente.getInputStream()); // Entablish user input flow 
                                String name = entrada.readUTF();                                               // Get the user name.
                                System.out.println(" > Online user: "+name);                     // User connect.

                                //      Creating a Thread instance to serve multiples messages and users.
                                Thread hilo = new ClientHandler(socket_Cliente, name);                              
                                hilo.start();                                                                       
                            }
                    }        
                catch(Exception e)
                    {
                        System.out.println(">>>>>>>>> It's not possible to get a connection <<<<<<<<<");
                    }
            }
    }                   



class ClientHandler extends Thread      // Customer management
    {
        
        private DataInputStream input;
        final String User_name;    
        final Socket Customer_socket;
        
        //  Each user had a socket 
        static Map<String, Socket> mapa = new HashMap<String, Socket>();        


        //                      Methods:
        // 1. Constructor:
        public ClientHandler(Socket Customer_socket, String name)
            {
                this.Customer_socket = Customer_socket;
                this.User_name = name;
            }
        

        
        public void run()
            {
                // Exceptions:
                try 
                    {
                        mapa.put(User_name,Customer_socket);                            // Keep the custome socket.

                        input = new DataInputStream(Customer_socket.getInputStream());     
                        String destiny =  input.readUTF();                           // get the flow with the receipt
                        Socket socketD = mapa.get(destiny);                     
                        DataOutputStream outputD = new DataOutputStream(socketD.getOutputStream());        
                        System.out.println("\t\t Entablish communication with users: "+User_name+" - "+destiny);

                        while(true)     
                            {
                                String message = input.readUTF();         // User message
                                outputD.writeUTF(message);   // The receipt received the message
                            }
                    }
                catch (Exception e)
                    {
                        try
                            {
                                System.out.println(">>>>>>>>> A user left <<<<<<<<<");
                                Customer_socket.close();
                            }
                        catch (Exception error) 
                            {
                                error.printStackTrace();
                            }
                    }
            }
    }                   