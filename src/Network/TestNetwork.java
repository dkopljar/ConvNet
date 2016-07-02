package Network;


import MNISTReader.Image;

import java.util.LinkedList;

/**
 * Created by Dkopljar on 4/25/16.
 */
public class TestNetwork {

    /**
     * Method is testing neural network for given test set
     * @param neuralNetwork neural network
     * @param photos test set
     * @return list of photos that were not labeled correctly
     */
    static LinkedList<Image> test(NeuralNetwork neuralNetwork, LinkedList<Image> photos){
        int correctCategory = 0;
        int testSize= photos.size();

        LinkedList<Image> errorPhotos = new LinkedList<>();
        for (int i = 0; i < testSize; i++) {
            LinkedList<double[][]> photoDataList = new LinkedList<>();
            photoDataList.add(Util.byteToDouble(photos.get(i).getData()));
            neuralNetwork.calculateOutput(photoDataList,photos.get(i).getLabel());
            if(neuralNetwork.getResult()==photos.get(i).getLabel()){
                correctCategory++;
            }else{
                photos.get(i).setPredicted(neuralNetwork.getResult());
                errorPhotos.add(photos.get(i));
            }
        }

        System.out.println("Predicted correctly: "+ correctCategory+" / "+testSize);
        System.out.println(correctCategory*1.0/testSize);
        return errorPhotos;
    }




}
