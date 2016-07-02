package Network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dkopljar on 4/2/16.
 */
public interface Layer extends Serializable{

    public void setInputData(List<double[][]> inputData);
    public List<double[][]> getResultData();
    public void calculateOutput();
    public List<double[][]> backpropagate();
    public String getType();
    public void setFunctionError(List<double[][]> functionError);
}
