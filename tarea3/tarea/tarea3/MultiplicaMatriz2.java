import java.net.Socket;
import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;

class MultiplicaMatriz2{
    static int N = 1000;
    static int [][] A = new int [N][N];
    static int [][] B = new int [N][N];
    static int [][] C = new int [N][N];

    static long checksum= 0;
    static Object lock = new Object();

    static void mostrarMatriz(int m[][], int high, int low){
        for (int i = 0; i < high; i++) {
            for (int j = 0; j < low; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println(" ");
        }
    }
    static void enviarMatriz(DataOutputStream salidaServer, int m[][], int high, int low) throws Exception{
        for (int i = high; i < low; i++) {
            for (int j = 0; j < N; j++) {
                salidaServer.writeInt(m[i][j]);
            }
        }
    }
    static void recibirMatriz(DataInputStream entradaServer, int high, int low, int right, int left ) throws Exception{
        for (int i = high; i < low; i++) {
            for (int j = right; j < left; j++) {
                    // M[i][j] = mat[i][j];   
                C[i][j] = entradaServer.readInt();
            }
        }
    }

    static class Worker extends Thread {
        Socket conexion;

        Worker(Socket conexion) {
            this.conexion = conexion;
        }

        public void run(){
            try {
                DataOutputStream salidaServer = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entradaServer = new DataInputStream(conexion.getInputStream());
                
                int x = entradaServer.readInt();
                
                switch (x) {
                    case 1:
                        enviarMatriz(salidaServer, A, 0, N/2);
                        enviarMatriz(salidaServer, B, 0, N/2);

                        System.out.println("Matriz resultado nodo 1...");
                        recibirMatriz(entradaServer, 0, N/2, 0, N/2);

                        break;
                    case 2:
                        enviarMatriz(salidaServer, A, 0, N/2);
                        enviarMatriz(salidaServer, B, N/2,N);

                        System.out.println("Matriz resultado nodo 2...");
                        recibirMatriz(entradaServer, 0, N/2, N/2, N );
                        break;
                    case 3:
                        enviarMatriz(salidaServer, A, N/2, N);
                        enviarMatriz(salidaServer, B, 0,N/2);

                        System.out.println("Matriz resultado nodo 3...");
                        recibirMatriz(entradaServer, N/2, N, 0, N/2);
                        break;
                    case 4:
                        enviarMatriz(salidaServer, A, N/2, N);
                        enviarMatriz(salidaServer, B, N/2,N);

                        System.out.println("Matriz resultado nodo 4...");
                        recibirMatriz(entradaServer, N/2, N, N/2, N);
                        break;
                    default:
                        break;
                }
                salidaServer.close();
                entradaServer.close();
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
            //Inicializa las matrices A y B
            for(int i=0; i<N; i++){
                for (int j=0; j<N; j++){
                    A[i][j]=2*i + 3*j; 
                    B[i][j]= 2*i - 3*j;
                }
            }
            
            if(N <=4){
                System.out.println("Matriz A ");                          
                mostrarMatriz(A, N, N);
                System.out.println("Matriz B ");                          
                mostrarMatriz(B, N, N);
            }
            //Transpone la matriz B, la matriz transpuesta queda en B
            for(int i=0; i<N; i++){
                for (int j=0; j<i; j++){
                    int x = B[i][j];
                    B[i][j]=B[j][i];
                    B[j][i]=x;
                }
            }
            if(N <=4){
                System.out.println("Matriz B transpuesta ");                          
                mostrarMatriz(B, N, N);
            }
            
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

            for (int i = 0; i<N; i++){
                for (int j = 0; j<N; j++){
                    checksum += C[i][j];
                }
            }
            if(N <=4){
                System.out.println("Matriz C");
                mostrarMatriz(C, N, N);
            }

            System.out.println("checksum:  " + checksum);

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
            salida.writeInt(nodo);    

            int [][] M1 = new int [N/2][N];
            int [][] M2 = new int [N/2][N];
            int [][] R = new int [N/2][N];
            
            System.out.println(" Matriz 1 recibida...");              
            for (int i = 0; i < N/2; i++) {
                for (int j = 0; j < N; j++) {
                        // M[i][j] = mat[i][j];   
                    M1[i][j] = entrada.readInt();
                }
            }
            if(N <= 4){
                mostrarMatriz(M1, N/2, N/2);
            }
            System.out.println(" Matriz 2 recibida...");              

            for (int i = 0; i < N/2; i++) {
                for (int j = 0; j < N; j++) {
                        // M[i][j] = mat[i][j];   
                    M2[i][j] = entrada.readInt();
                }
            }
            if(N <= 4){
                mostrarMatriz(M2, N/2, N/2);
            }
            
            System.out.println("Resolviendo matriz...");

                //Multiplicar la matriz A por la matriz B, el resultado queda en la matriz C
                for (int i = 0; i<N/2; i++){
                    for (int j = 0; j<N/2; j++){
                        for (int k = 0; k<N; k++){
                            R[i][j] += M1[i][k] * M2[j][k];
                        }
                    }
                }

             for (int i = 0; i < N/2; i++) {
                 for (int j = 0; j < N/2; j++) {
                     // M[i][j] = mat[i][j];   
                     salida.writeInt(R[i][j]);                     
                    }
                }
                if(N <= 4){
                    mostrarMatriz(R, N/2, N/2);
               }
                System.out.println("Matriz resultado enviada...");
            // termina procesamienot de matrices 

            salida.close();
            entrada.close();
            conexion.close();
        }

    }

}