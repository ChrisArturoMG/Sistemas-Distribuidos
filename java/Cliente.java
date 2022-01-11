import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.lang.*;

class Cliente
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
    Socket conexion = new Socket("localhost",50001);

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());
    
    // envia un numero punto flotante
    long time1 = System.currentTimeMillis(); 
     for(int i = 1; i<=10000; i++){
       salida.writeDouble(i + 0.0);
     }
     long time2 = System.currentTimeMillis(); 
     long total = time2 - time1;
     System.out.println( "tiempo writeDouble " + total);
    
    // enva un entero de 32 bits
    // salida.writeInt(123);
    
    // envia una cadena
    // salida.write("hola".getBytes());

    // recibe una cadena
    // byte[] buffer = new byte[4];
    // read(entrada,buffer,0,4);
    // System.out.println(new String(buffer,"UTF-8"));

    // envia 5 numeros punto flotante
    int longitud = 10000;
    time1 = System.currentTimeMillis(); 
    ByteBuffer b = ByteBuffer.allocate(longitud*8);
    for(int i = 1; i<=longitud; i++){
      b.putDouble(i + 0.0);
    }
    
    byte[] a = b.array();
    salida.write(a);
    time2 = System.currentTimeMillis(); 

    total = time2 - time1;

    System.out.println("Tiempo con buffer " + total);

    salida.close();
    entrada.close();
    conexion.close();    
  }
}