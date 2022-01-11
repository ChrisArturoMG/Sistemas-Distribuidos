import java.rmi.Naming;

public class ClienteRMI
{
    static int N = 4;

    static float[][] separa_matriz(float[][] A,int inicio) { 
        float[][] M = new float[N/2][N];
        for (int i = 0; i < N/2; i++) 
            for (int j = 0; j < N; j++) 
                M[i][j] = A[i + inicio][j];
                return M;
    }

    static void acomoda_matriz(float[][] C,float[][] A,int renglon,int columna) { 
        for (int i = 0; i < N/2; i++) 
            for (int j = 0; j < N/2; j++) 
            C[i + renglon][j + columna] = A[i][j];
    }

    static void mostrar_matriz(float [][] M1){
        for(int i = 0; i < N; i++){
            for(int j = 0; j<N; j++){
                System.out.print(M1[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    static double checksum(float [][]M1){
        double checksum = 0;

        for(int i = 0; i < N; i++){
            for(int j = 0; j<N; j++){
                checksum += M1[i][j];
            }
        }
        return checksum;
    }


  public static void main(String args[]) throws Exception
  {
    // en este caso el objeto remoto se llama "prueba", notar que se utiliza el puerto default 1099
    String url1 = "rmi://10.19.0.5/matriz";
    String url2 = "rmi://10.19.0.6/matriz";
    
    // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
    InterfaceRMI r1 = (InterfaceRMI)Naming.lookup(url1);
    InterfaceRMI r2 = (InterfaceRMI)Naming.lookup(url2);
    //Inicializamos las matrices 
    float A[][] = new float [N][N]; 
    float B[][] = new float [N][N]; 
    
    for(int i=0; i<N; i++){
        for (int j=0; j<N; j++){
            A[i][j]= i + 3 * j;
            B[i][j] = i - 3 * j;
        }
    }
    
    if( N <= 8){
        mostrar_matriz(A);
        mostrar_matriz(B);
    }

    //Transpone la matriz B, la matriz transpuesta queda en B
    for(int i=0; i<N; i++){
        for (int j=0; j<i; j++){
            float x = B[i][j];
            B[i][j]=B[j][i];
            B[j][i]=x;
        }
    }
    
    float[][] A1 = separa_matriz(A,0); 
    float[][] A2 = separa_matriz(A,N/2); 
    float[][] B1 = separa_matriz(B,0); 
    float[][] B2 = separa_matriz(B,N/2);
    
    
    float[][] C1 = r1.multiplica_matrices(A1,B1); 
    float[][] C2 = r1.multiplica_matrices(A1,B2); 
    float[][] C3 = r2.multiplica_matrices(A2,B1); 
    float[][] C4 = r2.multiplica_matrices(A2,B2);
    
    
    float[][] C = new float[N][N]; 
    acomoda_matriz(C,C1,0,0); 
    acomoda_matriz(C,C2,0,N/2); 
    acomoda_matriz(C,C3,N/2,0); 
    acomoda_matriz(C,C4,N/2,N/2);
    
    
    mostrar_matriz(C);
    System.out.println("checksum: " + checksum(C) );

    // System.out.println(r.mayusculas("hola"));
    // System.out.println("suma=" + r.suma(10,20));

    // int[][] m = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};
    // System.out.println("checksum=" + r.checksum(m));
  }
}
