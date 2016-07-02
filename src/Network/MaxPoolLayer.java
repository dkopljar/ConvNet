package Network;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dkopljar on 3/28/16.
 */
public class MaxPoolLayer implements Layer {

    /**
     * Size of filter (eg. 5x5)
     */
    int sx;
    /**
     * Depth of input
     */
    int depth;
    /**
     * List of all inputs
     */
    List<double[][]> allInputs ;
    /**
     * List of all results
     */
    List<double[][]> allResults = new LinkedList<>();

    /**
     * List of all max locations
     */
    List<Node[][]> maxLocations = new LinkedList<>();

    /**
     * List of all errors that will be propagated
     */
    List<double[][]> functionError = new LinkedList<>();

    /**
     * Constructor for max pool layer
     * @param sx size of filter
     * @param depth depth of input
     */
    public MaxPoolLayer(int sx, int depth){
        this.sx=sx;
        this.depth=depth;

    }

    /**
     * Setter for inputs
     * @param allInputs list of inputs
     */
    public void setInputData(List<double[][]> allInputs) {
        this.allInputs = allInputs;
        if (depth != allInputs.size()) {
            System.err.println("Input size is not same as depth!");
            System.exit(1);
        }
        calculateOutput();
    }

    /**
     * Getter for results
     * @return list of all results
     */
    public List<double[][]> getResultData() {
        return allResults;
    }

    /**
     * Method is calculating output of max pool layer.
     * 1. Loop depth iteration
     * 2. & 3. loops are used to iterate through all filter combinations in matrix
     * 4. & 5. loops are used to iterate through all elements in filter
     */
    public void calculateOutput(){
        allResults = new LinkedList<>();
        int sw = allInputs.get(0)[0].length;
        int sh = allInputs.get(0).length;
        maxLocations = new LinkedList<>();
        //Depth iteration
        for (int m = 0; m < depth; m++) {
            double[][] results = new double[sw / sx][sh / sx];
            Node[][] maxLocation = new Node[sw / sx][sh / sx];

            //tempi is counter used for indexing results, because i is incremented by stride
            //Finding all "boxes" (eg. 5x5) in matrix
            for (int i = 0,tempi=0; i <= sw - sx; i += sx) {

                //tempj is also used for indexing results
                for (int j = 0,tempj=0; j <= sh - sx; j += sx) {

                    double maxVal = allInputs.get(m)[i][j];
                    Node maxLoc=new Node(0,0);
                    //Finding max value in box
                    for (int k = 0; k < sx; k++) {
                        for (int l = 0; l < sx; l++) {
                            double val = allInputs.get(m)[i + k][j + l];
                            if(maxVal< val){
                                maxVal=val;
                                maxLoc=new Node(k,l);
                            }

                        }

                    }
                    maxLocation[tempi][tempj] = maxLoc;
                    results[tempi][tempj] = maxVal;
                    tempj++;

                }
                tempi++;
            }
            allResults.add(results);
            maxLocations.add(maxLocation);

        }



    }

    /**
     * Method is propagating errors to previous layer.
     * While doing forwardpass max locations are stored,
     * so that we can put errors on correct locations.
     * Example:
     * |(1,0) (0,1)|      | 0 1   0 0|
     * |(0,0) (0,1)| ---->| 0 0   1 0|
     *                    | 1 0   0 0|
     *                    | 0 0   1 0|
     * @return list of errors for previous layer
     */
    public List<double[][]> backpropagate(){
        List<double[][]> results = new LinkedList<>();
        int size = allInputs.get(0).length;
        for (int k = 0; k < allInputs.size(); k++) {
            double[][] result = new double[size][size];
            int tmpi=0;
            for (int i = 0; i <= size-sx; i+=sx) {
                int tmpj=0;
                for (int j = 0; j <= size-sx; j+=sx) {
                    Node loc = maxLocations.get(k)[tmpi][tmpj];
                    result[i+loc.x][j+loc.y]=functionError.get(k)[tmpi][tmpj];
                    tmpj++;
                }
                tmpi++;
            }
            results.add(result);
        }
        return results;
    }

    @Override
    public void setFunctionError(List<double[][]> functionError) {
        this.functionError = functionError;
    }

    @Override
    public String getType() {
        return "maxpool";
    }
}
