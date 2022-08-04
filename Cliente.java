// Clases usadas:
import java.io.*; 
import java.net.*; 
import java.util.Scanner;


public class Cliente    // Clase principal.
    {
        public static void main(String[] args)
            {
                try
                    {
                        Scanner scn = new Scanner(System.in);       
                        InetAddress ip = InetAddress.getByName("localhost");        // La IP será la local.
                        Socket socket = new Socket(ip, 5156);                       // Estableciendo conexión.
                        DataOutputStream salida = new DataOutputStream(socket.getOutputStream());       // Comunicación con el servidor.
                        
                        System.out.print("  > Introduzca su nombre de usuario: ");
                        String nombre = scn.nextLine();                             
                        salida.writeUTF(nombre);                                    // Se manda el nombre al servidor.
                        
                        System.out.print("  > Con quien desea comunicarse: ");
                        String destinatario = scn.nextLine();
                        salida.writeUTF(destinatario);                              // Se manda con quien se desea hablar.

                        Thread hilo1 = new LecturaMensajes (socket,destinatario,nombre);    // Instancia de los hilos.
                        Thread hilo2 = new EscrituraMensajes(socket,nombre);
                        
                        hilo2.start();                                              // Ejecución de los hilos.
                        hilo1.start();
                    }
                catch(Exception e)
                    {
                        System.out.println(">>>>>>>>> Se ha generado un error al momento de conectarse al servidor <<<<<<<<<");
                    }
            }
    }               // Fin de la clase principal del cliente.



class EscrituraMensajes extends Thread
    {
        //      Atributos:
        private DataOutputStream salida;
        private Socket ClienteActual;
        private String nombreUsuario;
        Scanner scn = new Scanner(System.in); 


        //                      Métodos:
        // 1. Constructor:
        public EscrituraMensajes(Socket ClienteActual, String nombre)
            {
                this.ClienteActual = ClienteActual;
                this.nombreUsuario = nombre;
            }
        
        
        // 2. Sobrescritura el método run:
        public void run()
            {
                while(true)
                    {
                        try
                            {
                                System.out.print(nombreUsuario + ": ");
                                salida = new DataOutputStream(ClienteActual.getOutputStream());
                                String mensaje = scn.nextLine();
                                salida.writeUTF(mensaje);
                            }
                        catch (Exception e)
                            {
                                try {
                                    System.out.print("Cerrando sesión.");
                                    ClienteActual.close();
                                }
                                catch (Exception error){
                                    error.printStackTrace();
                                }
                            }
                    }
                
            }
    }               // Fin del hilo escritura de mensajes.



class LecturaMensajes extends Thread
    {
        //      Atributos:
        private DataInputStream entrada;
        private Socket ClienteActual;
        private String nombreRemitente;
        private String nombreUsuario;
        Scanner scn = new Scanner(System.in); 


        //                      Métodos:
        // 1. Constructor:
        public LecturaMensajes(Socket ClienteActual, String Destinatario, String nombre)
            {
                this.ClienteActual = ClienteActual;
                this.nombreRemitente = Destinatario;
                this.nombreUsuario = nombre;
            }
        

        // 2. Sobrescritura el método run:
        public void run()
            {
                while(true)
                    {
                        try
                            {
                                entrada = new DataInputStream(ClienteActual.getInputStream());
                                System.out.print("\n"+nombreRemitente+ ": " + entrada.readUTF() + "\n" + nombreUsuario + ": ");
                            }
                        catch (Exception e)
                            {
                                try {
                                    System.out.print("Cerrando sesión.");
                                    ClienteActual.close();
                                }
                                catch (Exception error){
                                    error.printStackTrace();
                                }
                            }
                    }
            }
    }
