// Clases usadas:
import java.io.*; 
import java.net.*; 
import java.util.Scanner;


public class Client    // Clase principal.
    {
        public static void main(String[] args)
            {
                try
                    {
                        Scanner scn = new Scanner(System.in);       
                        InetAddress ip = InetAddress.getByName("localhost");        // Local IP
                        Socket socket = new Socket(ip, 5156);                       // Entablish connection
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());       // Entablish communication with server.
                        
                        System.out.print("  > What is your name?: ");
                        String name = scn.nextLine();                             
                        output.writeUTF(name);                                    // Sends the user name to the server.
                        
                        System.out.print("  > Who do you want to talk to?: ");
                        String destiny = scn.nextLine();
                        output.writeUTF(destiny);                              

                        Thread thread1 = new ReadMessages (socket,destiny,name);    // Creating threading instances
                        Thread thread2 = new WriteMessages(socket,name);
                        
                        thread2.start();                                              
                        thread1.start();
                    }
                catch(Exception e)
                    {
                        System.out.println(">>>>>>>>> The connection to the server has failed <<<<<<<<<");
                    }
            }
    }               



class WriteMessages extends Thread
    {
        //      Atributos:
        private DataOutputStream output;
        private Socket CurrentCustomer;
        private String UserName;
        Scanner scn = new Scanner(System.in); 


        //                      Methods:
        // 1. Constructor:
        public WriteMessages(Socket CurrentCustomer, String UserName)
            {
                this.CurrentCustomer = CurrentCustomer;
                this.UserName = UserName;
            }
        
        
        // 2. Override run method
        public void run()
            {
                while(true)
                    {
                        try
                            {
                                System.out.print(UserName + ": ");
                                output = new DataOutputStream(CurrentCustomer.getOutputStream());
                                String message = scn.nextLine();
                                output.writeUTF(message);
                            }
                        catch (Exception e)
                            {
                                try {
                                    System.out.print("Logging out.");
                                    CurrentCustomer.close();
                                }
                                catch (Exception error){
                                    error.printStackTrace();
                                }
                            }
                    }
                
            }
    }               



class ReadMessages extends Thread
    {
        //      Atributos:
        private DataInputStream input;
        private Socket CurrentCustomer;
        private String RecipientName;
        private String UserName;
        Scanner scn = new Scanner(System.in); 


        //                      Methods:
        // 1. Constructor:
        public ReadMessages(Socket CurrentCustomer, String RecipientName, String UserName)
            {
                this.CurrentCustomer = CurrentCustomer;
                this.RecipientName = RecipientName;
                this.UserName = UserName;
            }
        

        // 2. Override run method
        public void run()
            {
                while(true)
                    {
                        try
                            {
                                input = new DataInputStream(CurrentCustomer.getInputStream());
                                System.out.print("\n"+RecipientName+ ": " + input.readUTF() + "\n" + UserName + ": ");
                            }
                        catch (Exception e)
                            {
                                try {
                                    System.out.print("Logging out.");
                                    CurrentCustomer.close();
                                }
                                catch (Exception error){
                                    error.printStackTrace();
                                }
                            }
                    }
            }
    }
