import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.lang.*;

class Servidor
{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
  {
    while (longitud > 0)
    {
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception
  {
    ServerSocket servidor = new ServerSocket(50001);

    Socket conexion = servidor.accept();

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());
    
    // recibe un numero punto flotante

    long time1 = System.currentTimeMillis();
    for(int i = 1; i<=10000; i++){
       double x = entrada.readDouble();
      //  System.out.println(x);
     }

     long time2 = System.currentTimeMillis();
     long total = time2 - time1;
     System.out.println( "tiempo writeDouble " + total);
    
    // recibe un entero de 32 bits
    // int n = entrada.readInt();
    // System.out.println(n);
    
    // recibe una cadena
    // byte[] buffer = new byte[4];
    // read(entrada,buffer,0,4);
    // System.out.println(new String(buffer,"UTF-8"));

    // envia una cadena
    // salida.write("HOLA".getBytes());

    // recibe 5 numeros punto flotante

    time1 = System.currentTimeMillis();

    int longitud = 10000;
    byte[] a = new byte[longitud*8];
    read(entrada,a,0,longitud*8);

    ByteBuffer b = ByteBuffer.wrap(a);

    time2 = System.currentTimeMillis();    
    total = time2 - time1;

    // for (int i = 0; i < longitud; i++)
    //   System.out.println(b.getDouble());    
    
    System.out.println("Tiempo con buffer " + total);

    salida.close();
    entrada.close();
    conexion.close();
  }
}
