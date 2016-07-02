package Network;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by Dkopljar on 4/20/16.
 */
public class Util {

    /**
     * Method is calculating output of tanh function
     * @param input input
     * @return output of tanh function
     */
    public static double tanh(double input){
        return Math.tanh(input);
    }

    /**
     * Method is calculating output of tanh derivative function
     * @param input input
     * @return output of tanh function
     */
    public static double tanhDerivate(double input){
        double res= tanh(input);
        return 1-res*res;
    }


    /**
     * Method is calculating output of sigmoid function
     * @param input input
     * @return output of sigmoid function
     */
    public static double sigmoid(double input){
        return 1 / (1 + Math.exp(-input));
    }

    /**
     * Method is calculating output of sigmoid derivative function
     * @param input input
     * @return output of sigmoid function
     */
    public static double sigmoidDerivative(double input){
        return sigmoid(input)*(1-sigmoid(input));
    }

    /**
     * Method is rotating for 180 degrees given matrix
     * @param matrix input matrix
     * @return rotated matrix
     */
    public static double[][] rotate(double[][] matrix){
        double[][] a = new double[matrix.length][matrix.length];
        for (int i = 0; i <matrix.length ; i++) {
            for (int j = 0; j < matrix.length; j++) {
                a[i][j]=matrix[i][j];
            }
        }
        int n = a.length;
        for (int k = 0; k <2; k++) {
            for (int i = 0; i < n/2; i++) {
                for (int j=i; j<n-i-1; j++){
                    double tmp=a[i][j];
                    a[i][j]=a[j][n-i-1];
                    a[j][n-i-1]=a[n-i-1][n-j-1];
                    a[n-i-1][n-j-1]=a[n-j-1][i];
                    a[n-j-1][i]=tmp;
                }
            }
        }
        return  a;

    }

    /**
     * Method is padding matrix from top and left
     * @param matrix matrix to be padded
     * @param pad size of padding
     * @return padded matrix
     */
    public static double[][] padding (double[][] matrix, int pad){
        double[][] result = new double [matrix.length+pad][matrix.length+pad];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                if(i<pad || j <pad){
                    continue;
                }else{
                    result[i][j]=matrix[i-pad][j-pad];
                }
            }
        }
        return result;

    }

    /**
     * Method will rotate all weights for 180 degrees
     *
     */
    public static LinkedList<double[][]> rotateEveryMatrix(LinkedList<double[][]> list){
        LinkedList<double[][]> allFliped = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            allFliped.add(Util.rotate(list.get(i)));
        }
        return allFliped;
    }

    /**
     * Method is converting byte matrix to double matrix
     * @param a byte matrix
     * @return double matrix
     */
    static double[][] byteToDouble(byte[][] a){
        double[][] result = new double[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                result[i][j] = ( a[j][i])& 0xFF;
            }
        }
        return result;
    }

    /**
     * Method is saving network to given path
     * @param network neural network
     * @param path path
     */
    static void saveToFile(NeuralNetwork network, String path){
        try
        {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(network);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in "+path);
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    static NeuralNetwork loadFromFile(String path){
        NeuralNetwork net = null;
        try
        {
            FileInputStream fileIn = new FileInputStream("network.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            net = (NeuralNetwork) in.readObject();
            in.close();
            fileIn.close();
            return net;
        }catch(IOException i)
        {
            i.printStackTrace();
            return null;
        }catch(ClassNotFoundException c)
        {
            System.out.println("NeuralNetwork class not found");
            c.printStackTrace();
            return null;
        }
    }
}
