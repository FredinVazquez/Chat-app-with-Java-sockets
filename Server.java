// Clases usadas:
import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 

public class Server     // Clase main. 
    {
        public static void main(String[] args)
            {
                //                                      Las instrucciones siguientes pueden generar excepciones.
                try     
                    {
                        ServerSocket server = new ServerSocket(5156);       // Instanciando el server, donde 5156 es el número del puerto.
                        System.out.println("\n  >>>-------- EL SERVIDOR SE HA INICIADO --------<<<");
                        while (true)                                        // El servidor debe estar siempre activo, por ende se colocar un while(true).
                            {
                                Socket socket_Cliente = server.accept();    // Se aceptará a los clientes que soliciten conectarse al servidor usando el mismo puerto.
                                System.out.println("\t###  Nuevo cliente conectado:  ###  ");                // Notificación que un cliente se conectó.
                                DataInputStream entrada = new DataInputStream(socket_Cliente.getInputStream());// Se establece el flujo de entrada para información del cliente.
                                String name = entrada.readUTF();                                               // Consiguiendo el nombre que ingrese el usuario.
                                System.out.println(" > Usuario conectado: "+name);                     // Notiicación que se ha conectado un usuario al servidor.

                                //      Instanciando hilos para atender las conexiones y mensajes de los clientes.
                                Thread hilo = new ClientHandler(socket_Cliente, name);                              // Explotación del Polimorfismo.
                                hilo.start();                                                                       // Ejecutando el hilo.
                            }
                    }        
                catch(Exception e)
                    {
                        System.out.println(">>>>>>>>> Se ha generado un error al momento de conectar un cliente <<<<<<<<<");
                    }
            }
    }                   // Fin de la clase Server - main.



class ClientHandler extends Thread      // Clase para gestionar los mensajes entre los clientes.
    {
        //                     Atributos: 
        private DataInputStream entrada;
        final String nombre_Cliente;    // No serán modificarlos durante la ejecución del programa.
        final Socket socket_Cliente;
        
        //  Estructura de datos para controlar la propiedad de los sockets de cada cliente para realizar el envío de mensajes.
        static Map<String, Socket> mapa = new HashMap<String, Socket>();        


        //                      Métodos:
        // 1. Constructor:
        public ClientHandler(Socket socket_Cliente, String nombre)
            {
                this.socket_Cliente = socket_Cliente;
                this.nombre_Cliente = nombre;
            }
        

        // 2. Sobrescritura el método run:
        public void run()
            {
                // Manejo de excepciones:
                try 
                    {
                        mapa.put(nombre_Cliente,socket_Cliente);                            // Se guardará el socket de cada cliente.

                        entrada = new DataInputStream(socket_Cliente.getInputStream());     // Estableciendo el flujo de entrada.
                        String destinatario =  entrada.readUTF();                           // Obteniendo el destinatario de los mensajes:
                        Socket socketDestino = mapa.get(destinatario);                      // Socket para establecer el flujo de salida.
                        DataOutputStream salidaParaDestinatario = new DataOutputStream(socketDestino.getOutputStream());        
                        System.out.println("\t\t Se entablo la conexion de los usuarios: "+nombre_Cliente+" - "+destinatario);

                        while(true)     // Ciclo para el envío de mensajes.
                            {
                                String mensaje = entrada.readUTF();         // Mensaje del cliente.
                                salidaParaDestinatario.writeUTF(mensaje);   // Se envía el mensaje al destino.
                            }
                    }
                catch (Exception e)
                    {
                        try
                            {
                                System.out.println(">>>>>>>>> Un usuario salió <<<<<<<<<");
                                socket_Cliente.close();
                            }
                        catch (Exception error) 
                            {
                                error.printStackTrace();
                            }
                    }
            }
    }                   // Fin de la clase ClientHandler.