package Network;

import MNISTReader.Image;

import java.util.LinkedList;

/**
 * Created by Dkopljar on 5/6/16.
 */
public class TrainNetwork {

    /**
     * Method is training neural network for given set of images and params
     * @param network neural network
     * @param photos training set
     * @param miniBatchSize mini batch size
     * @param epochs number of epochs
     */
    static void train(NeuralNetwork network, LinkedList<Image> photos , int miniBatchSize, int epochs){
        int trainingSize= photos.size();

        LinkedList<LinkedList<double[][]>> listOfPhotosData = new LinkedList<>();
        for (int i = 0; i < trainingSize; i++) {
            double[][] input = Util.byteToDouble(photos.get(i).getData());
            LinkedList<double[][]> inputList = new LinkedList<>();
            inputList.add(input);
            listOfPhotosData.add(inputList);
        }

        System.out.println("Training set size: "+ trainingSize);
        System.out.println("Settings: miniBatch= "+miniBatchSize + " epochs= "+epochs );

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < epochs; i++) {
            double err=0;
            int err2=0;
            network.setLearningRate(network.learningRate*0.993);
            for (int j = 0; j < trainingSize; j++) {
                network.calculateOutput(listOfPhotosData.get(j), photos.get(j).getLabel());
                network.backward(photos.get(j).getLabel(),j%miniBatchSize==0?true:false);
                err+=network.getError();
                if(network.getResult()!=photos.get(j).getLabel()){
                	err2++;
                }
            }
            System.out.println("Epoch: "+i + " Total error:" + err +" Err2:"+(double)(err2)/trainingSize +"  Elapsed time (min): "+ (System.currentTimeMillis()-startTime)/(60000.0));
        }

        System.out.println("Total time: "+(System.currentTimeMillis()-startTime));

    }
}
