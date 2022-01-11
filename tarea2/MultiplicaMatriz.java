class MultiplicaMatriz{
    static int N = 1000;
    static int [][] A = new int [N][N];
    static int [][] B = new int [N][N];
    static int [][] C = new int [N][N];
    
    public static void main (String [] args){
        long t1 = System.currentTimeMillis();

        //Inicializa las matrices A y B
        for(int i = 0; i<N; i++){
            for (int j =0; j<N; j++){
                A[i][j]=2*i - j; 
                A[i][j]= i + 2*j;
                A[i][j]= 0;
            }

            //Multiplicar la matriz A por la matriz B, el resultado queda en la matriz C
        for ( i = 0; i<N; i++){
            for (int j = 0; j<N; j++){
                for (int k = 0; k<N; k++){
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Tiempo: " + (t2 -t1) + "ms");

        }

    }

}