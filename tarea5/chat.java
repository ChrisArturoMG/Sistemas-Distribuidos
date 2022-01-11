import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;


class chat {

    static void enviar_mensaje_multicast(byte[] buffer, String ip, int puerto ) throws IOException{
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
        socket.close();
        // System.out.println("mensaje enviado");
    }

    static byte [] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException{
        // System.out.println("mensaje recibido");
        byte [] buffer= new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }

    static class Worker extends Thread {
        public void run () {
            //En un ciclo infinito se recibe los mensajes enviados al grupo 
            // 230.0.0.0 a traves del puerto 50000 y se desplegaran en la pantalla 
            try {
                MulticastSocket s = new MulticastSocket(50000);
                s.joinGroup(InetAddress.getByName("230.0.0.0"));
                while(true){
                    String mensaje = new String(recibe_mensaje_multicast(s, 10000));
                    System.out.println(mensaje);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void main( String []args )throws Exception{
        Worker w = new Worker ();
        w.start();
        String nombre = args[0];
        String ipGroup = "230.0.0.0";
        int puerto = 50000;

        //  En un ciclo infinito se leera cada mensaje del teclado y se enviara el mensaje la 
        // grupo 230.0.0.0 a traves del puerto 50000
        Scanner lector = new Scanner(System.in);
        String buffer;
        while(true){
            System.out.println("Inserte mensaje a enviar: ");
            buffer = nombre +" : " + lector.nextLine();
            enviar_mensaje_multicast(buffer.getBytes(), ipGroup, puerto);
        }
    }

}