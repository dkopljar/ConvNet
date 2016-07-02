package Network;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dkopljar on 4/2/16.
 */
public class NeuralNetwork implements Serializable{
    double learningRate;

    /**
     * List of all layers
     */
    LinkedList<Layer> layers = new LinkedList<>();
    /**
     * List of all outputs
     */
    List<double[][]> output = new LinkedList<>();
    /**
     * Network error
     */
    private double error = 0;


    public NeuralNetwork(){

    }

    /**
     * Method adds layer to list
     * @param layer new layer
     */
    void addLayer(Layer layer){
        layers.add(layer);
    }

    /**
     * Method calculates output of neural network for given input and label
     * @param input list of inputs (1 photo)
     * @param label label of photo
     * @return list of outputs
     */
    List<double[][]> calculateOutput(LinkedList<double[][]> input,int label){
        layers.get(0).setInputData(input);
        for (int i = 1; i <layers.size(); i++) {
            layers.get(i).setInputData(layers.get(i-1).getResultData());
        }
        output=layers.getLast().getResultData();
        calculateError(label);
        return layers.getLast().getResultData();
    }

    /**
     * Method that calulates error of network
     * @param number label
     */
    void calculateError(int number){
        error=0;

        for (int d = 0; d < output.size(); d++) {
            if (d==number){
                error+=(output.get(d)[0][0]-1)*(output.get(d)[0][0]-1);
            }else{
                error+=output.get(d)[0][0]*output.get(d)[0][0];
            }
        }
        error/=2;
    }

    /**
     * Method is looking for all layers with weights
     * @return list of layers with weights
     */
    LinkedList<Layer> getAllLayersWithWeights(){
        LinkedList<Layer> result = new LinkedList<>();
        for (Layer layer:layers) {
            if (layer.getType().equals("conv")){
                result.add(layer);
            }
        }
        return result;
    }

    /**
     * Getter for error
     * @return
     */
    public double getError() {
        return error;
    }


    void backward(int label, boolean batch){
        //First error
        List<double[][]> previousError = new LinkedList<>();

        for (int d = 0; d < output.size(); d++) {
            int size = 1;
            double[][] lastError = new double[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (d==label){
                        lastError[i][j]=output.get(d)[0][0]-1;
                    }else{
                        lastError[i][j]=output.get(d)[0][0];
                    }

                }
            }
            previousError.add(lastError);
        }

        for (int i = layers.size()-1; i >= 0; i--) {
            layers.get(i).setFunctionError(previousError);
            previousError = layers.get(i).backpropagate();

        }
        if(batch){
            LinkedList<Layer> layersWithWeights =  this.getAllLayersWithWeights();
            for (int i = 0; i < layersWithWeights.size(); i++) {
                ConvLayer lay = (ConvLayer) layersWithWeights.get(i);
                lay.updateWeights(learningRate);
            }
        }


    }

    int getResult(){
        double max = -1;
        int result=0;
        for (int i = 0; i < output.size(); i++) {
            if(output.get(i)[0][0]>max){
                max=output.get(i)[0][0];
                result=i;
            }
        }
        return result;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }
}
