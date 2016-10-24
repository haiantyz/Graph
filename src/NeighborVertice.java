/**
 * Created by tyz on 2016/10/13.
 */
public class NeighborVertice {

        private int neighborverticeId,edgeWeight;

        public NeighborVertice(int verticeId, int edgeWeight){
            this.neighborverticeId = verticeId;
            this.edgeWeight = edgeWeight;
        }

    public NeighborVertice(){

    }


    public int getEdgeWeight() {
            return edgeWeight;
        }

        public void setEdgeWeight(int edgeWeight) {
            this.edgeWeight = edgeWeight;
        }


        public int getNeighborverticeId() {
            return neighborverticeId;
        }

        public void setNeighborverticeId(int neighborverticeId) {
            this.neighborverticeId = neighborverticeId;
        }
}

