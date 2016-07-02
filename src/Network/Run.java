package Network;

import Gui.Frame;
import MNISTReader.Image;
import MNISTReader.ReadingDataSet;

import java.util.LinkedList;

/**
 * Created by Dkopljar on 4/12/16.
 */
public class Run {

    public static void main(String[] args) {


        double learningRate = 0.1;
        int numOfPhotos = 10000;
        int testSize= (int) (numOfPhotos*0.3);
        int miniBatchSize = 5;
        int epochs=100;

        NeuralNetwork network = new NeuralNetwork();
        network.setLearningRate(learningRate);
        ConvLayer layer = new ConvLayer(5, 5, 1, "exp", 1);
        MaxPoolLayer layer2 = new MaxPoolLayer(2, 5);
        ConvLayer layer3 = new ConvLayer(4, 6, 1, "exp", 5);
        MaxPoolLayer layer4 = new MaxPoolLayer(3, 6);
        ConvLayer layer5 = new ConvLayer(3, 15, 1, "exp", 6);
        ConvLayer layer6 = new ConvLayer(1, 10, 1, "exp", 15);

        network.addLayer(layer);
        network.addLayer(layer2);
        network.addLayer(layer3);
        network.addLayer(layer4);
        network.addLayer(layer5);
        network.addLayer(layer6);

        ReadingDataSet dataSet = new ReadingDataSet();
        LinkedList<Image> photos = dataSet.getExamples(numOfPhotos);

        LinkedList<Image> trainingSet = new LinkedList<>();
        LinkedList<Image> testSet = new LinkedList<>();


        for (int i = 0; i < numOfPhotos-testSize; i++) {
            trainingSet.add(photos.get(i));
        }

        for (int i = numOfPhotos-testSize; i < numOfPhotos; i++) {
            testSet.add(photos.get(i));
        }
        TrainNetwork.train(network,trainingSet,miniBatchSize,epochs);
        //NeuralNetwork net = Util.loadFromFile("network94.ser");
        LinkedList<Image> errorPhotos=TestNetwork.test(network,testSet);
        Frame frame = new Frame(errorPhotos);



        Util.saveToFile(network,"network.ser");

        //NeuralNetwork net = Util.loadFromFile("network.ser");
        //TestNetwork.testNetwork(net,testSet);
    }


}
