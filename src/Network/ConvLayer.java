package Network;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dkopljar on 3/21/16.
 */


public class ConvLayer implements Layer{
    /**
     * Size of filter (eg. 5x5)
     */
    int sx;

    /**
     * Number of filters
     */
    int filters;

    /**
     * Stride size
     */
    int stride;

    /**
     * Name of activation
     */
    String activation;

    Random rand = new Random();

    /**
     * List of all inputs
     */
    List<double[][]> inputData;

    /**
     * List of all results before activation
     */
    List<double[][]> rawResult = new LinkedList<>();

    /**
     * List of all results
     */
    List<double[][]> allResultData = new LinkedList<>();

    /**
     * Weights for biases
     */
    double[] biases;


    double[] biasesErrors;

    /**
     * List of all weights
     * Weights should be sorted like this:
     * filter1: depth1,depth2,depth3 filter2:depth1,depth2,depth3...
     */
    List<double[][]> allWeights = new LinkedList<>();

    /**
     * Depth of input
     */
    int inputDepth;

    /**
     * List of all weight errors
     */
    List<double[][]> allWeightErrors = new LinkedList<>();

    List<double[][]> functionError = new LinkedList<>();

    public ConvLayer(int sx, int filters, int stride, String activation, int inputDepth) {
        this.sx = sx;
        this.filters = filters;
        this.stride = stride;
        this.activation = activation;
        this.inputDepth = inputDepth;
        this.biases = new double[filters];
        this.biasesErrors = new double[filters];
        randomizeWeights();
    }

    /**
     * This method is randomizing all weights (-1 to 1).
     * Biases are set to 0.01;
     */
    public void randomizeWeights() {

        allWeights = new LinkedList<>();
        for (int i = 0; i < filters; i++) {
            biases[i] = rand.nextBoolean() ? rand.nextDouble() : rand.nextDouble() * -1;
            //biases[i] =0.01;
        }

        //For every filter
        for (int i = 0; i < filters; i++) {

            //Every "depth"
            for (int ii = 0; ii < inputDepth; ii++) {

                //Make weights
                double[][] weights = new double[sx][sx];
                for (int j = 0; j < weights.length; j++) {
                    for (int k = 0; k < weights[0].length; k++) {
                        weights[j][k] = rand.nextBoolean() ? rand.nextDouble(): rand.nextDouble() * -1;
                    }
                }
                allWeights.add(weights);
            }
        }
    }


    /**
     * Setter for input data
     *
     * @param inputData input data
     */
    public void setInputData(List<double[][]> inputData) {
        this.inputData = inputData;
        if (inputDepth != inputData.size()) {
            System.err.println("Input size is not same as depth! (inputDepth: " +inputDepth+" ,Input size: "+ inputData.size()+")");
            System.exit(1);
        }
        calculateOutput();
    }

    /**
     * Getter for result data list
     *
     * @return result data list
     */
    public List<double[][]> getResultData() {
        return allResultData;
    }


    /**
     * Core method that is calculating output of layer.
     * Method is first calculating size of results array.
     * 1. loop is iterating through all filters
     * 2. & 3. loops are used to iterate through all filter combinations in matrix
     * 4. & 5. loops are used to iterate through all elements in filter
     * 6. loop is used for iterating through all depths
     *
     * @see <a href="http://cs231n.github.io/convolutional-networks/">Example of core part</a>
     */
    public void calculateOutput() {
        allResultData = new LinkedList<>();
        int sw = inputData.get(0)[0].length;
        int w2 = (sw - sx) / stride + 1;
        int sh = inputData.get(0).length;
        int h2 = (sh - sx) / stride + 1;

        for (int n = 0; n < filters; n++) {
            double[][] results = new double[h2][w2];
            //tempi is counter used for indexing results, because i is incremented by stride
            //Finding all "boxes" (eg. 5x5) in matrix
            for (int i = 0, tempi=0; i <= sw - sx; i += stride) {

                //tempj is also used for indexing results
                for (int j = 0, tempj=0; j <= sh - sx; j += stride) {

                    //Adding all numbers in matrix
                    for (int k = 0; k < sx; k++) {
                        for (int l = 0; l < sx; l++) {

                            //Depth iteration
                            for (int m = 0; m < inputDepth; m++) {
                                results[tempi][tempj] += inputData.get(m)[tempi + k][tempj + l] * allWeights.get(n * inputDepth + m)[k][l];
                            }
                        }

                    }
                    results[tempi][tempj] += biases[n];
                    tempj++;

                }
                tempi++;
            }
            allResultData.add(results);
        }

        rawResult=new LinkedList<>();
        for (int i = 0; i < allResultData.size(); i++) {
            double[][] raw = new double[w2][w2];
            for (int j = 0; j < w2; j++) {
                for (int k = 0; k < w2; k++) {
                    raw[j][k]=allResultData.get(i)[j][k];
                }
            }
            rawResult.add(raw);
        }

        //TODO:Add other functions
        if(activation.equals("exp")){
            activateNeurons();
        }


    }


    @Override
    public String getType() {
        return "conv";
    }

    /**
     * This method is "activating" every neuron.
     * Activation function in this case is sigmoid.
     * It is hardcoded because it should be best for this type of problem,
     * and by hardcoding it we are skipping all overhead and speeding up the process.
     */
    public void activateNeurons(){
        for (int k = 0; k <allResultData.size(); k++) {
            double[][] result = allResultData.get(k);
             for (int i = 0; i <result.length; i++) {
                for (int j = 0; j < result[0].length; j++) {
                    result[i][j] = Util.sigmoid(result[i][j]);
                }
            }

        }
    }
    @Override
    public List<double[][]> backpropagate(){
        int sw = inputData.get(0)[0].length;
        int w2 = (sw - sx) / stride + 1;
        int sh = inputData.get(0).length;
        int h2 = (sh - sx) / stride + 1;

        if(allWeightErrors.size()==0){
            allWeightErrors = new LinkedList<>();
            for (int i = 0; i < filters; i++) {
                for (int j = 0; j < inputDepth; j++) {
                    double[][] weightErrors = new double[sx][sx];
                    allWeightErrors.add(weightErrors);
                }
            }
        }

        LinkedList<double[][]> previousFunError = new LinkedList<>();
        for (int i = 0; i < inputDepth; i++) {
            double[][] previousError = new double[sh][sw];
            previousFunError.add(previousError);
        }


        for (int n = 0; n < filters; n++) {

            //tempi is counter used for indexing results, because i is incremented by stride
            //Finding all "boxes" (eg. 5x5) in matrix
            for (int i = 0, tempi=0; i <= sw - sx; i += stride) {

                //tempj is also used for indexing results
                for (int j = 0, tempj=0; j <= sh - sx; j += stride) {

                    double chainGradient = functionError.get(n)[tempi][tempj]*Util.sigmoidDerivative(rawResult.get(n)[tempi][tempj]);
                    //Adding all numbers in matrix
                    for (int k = 0; k < sx; k++) {
                        for (int l = 0; l < sx; l++) {
                            //Depth iteration
                            for (int m = 0; m < inputDepth; m++) {
                                allWeightErrors.get(n*inputDepth+m)[k][l] += inputData.get(m)[tempi + k][j + l] * chainGradient;
                                previousFunError.get(m)[tempi+k][tempj+l] += allWeights.get(n * inputDepth + m)[k][l] * chainGradient;
                            }

                        }
                    }
                    biasesErrors[n]+=chainGradient;
                    tempj++;

                }
                tempi++;
            }

        }
        return previousFunError;
    }

    @Override
    public void setFunctionError(List<double[][]> functionError) {
        this.functionError = functionError;
    }

    /**
     * Getter for list of weights
     * @return weights
     */
    public List<double[][]> getAllWeights() {
        return allWeights;
    }



    /**
     * Method is updating all weights using their gradient.
     * New weight -= eta* weightGradient
     * @param eta learning rate
     */
    public void updateWeights(double eta){

        for (int i = 0; i < allWeightErrors.get(0).length; i++) {
            for (int j = 0; j < allWeightErrors.get(0).length; j++) {
                for (int k = 0; k < allWeightErrors.size(); k++) {
                    allWeights.get(k)[i][j]-=eta*allWeightErrors.get(k)[i][j];
                }
            }
        }
        for (int i = 0; i <biasesErrors.length; i++) {
            biases[i]-=eta*biasesErrors[i];
        }

        allWeightErrors = new LinkedList<>();
        biasesErrors = new double[filters];
    }
    /**
     * Setter for allWeights
     * @param allWeights list of all weights
     */
    public void setAllWeights(List<double[][]> allWeights) {
        this.allWeights = allWeights;
    }



}
