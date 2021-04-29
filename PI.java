import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;

class PI {
    static Object lock = new Object();
    static double pi = 0;

    static class Worker extends Thread {
        Socket conexion;
        
        Worker(Socket conexion){
            this.conexion = conexion; 
        }
        public void run(){
            //ALGORITMO 1
            try {
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                double x = entrada.readDouble();
                
                synchronized(lock){
                    pi = pi + x;
                }

                salida.close();
                entrada.close();
                conexion.close();
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public static void main (String [] args) throws Exception{
        if(args.length != 1){
            System.err.println("Uso");
            System.err.println("Java PI" + args[0]);
            System.exit(0);
        }
        int nodo = Integer.valueOf(args[0]);
        if (nodo  == 0){
            System.out.println("Esperando cliente...");
            //ALGORITMO 2 
            ServerSocket servidor = new ServerSocket(50000);
            Worker w [] = new Worker[4];

            for( int i = 0; i<4; i++){
                Socket conexion = servidor.accept();
                int ncliente = i + 1;
                System.out.println("Conectado cliente " + ncliente );
                w[i] = new Worker(conexion);
                w[i].start();
            }
            
            for (int j= 0; j<4; j++){
                w[j].join();
            }
            System.out.println("Valor de PI: " + pi);

        }else{
            //ALGORITMO 3   
            Socket conexion = null;
            
            for(int i = 0; i<5; i++){
                try {
                    conexion = new Socket("localhost",50000);
                    break;
                } catch (Exception e) {
                    Thread.sleep(100);
                }
            }
            
            System.out.println("Conectado a servidor");
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            double suma = 0;
            
            for(int i = 0; i<10000000; i++){
                suma = (4.0/(8*i+2*(nodo-2)+3) )+ suma;
                // System.out.println("calculo");
            }
            System.out.println("Se calcularon los 10 millones de terminos en serie ");
            System.out.println("Suma: " + suma);

            suma = nodo%2==0?-suma:suma;
            salida.writeDouble(suma);

            salida.close();
            entrada.close();
            conexion.close();
        }
    }
}