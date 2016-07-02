package Network;

import java.io.Serializable;

/**
 * Created by Dkopljar on 4/8/16.
 * Class is used for genetic algorithm
 */
class Node implements Serializable{
    public int x;
    public int y;
    public Node(int x, int y){
        this.x=x;
        this.y=y;
    }
}