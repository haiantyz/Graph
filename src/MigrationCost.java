/**
 * Created by tyz on 2016/10/19.
 */
public class MigrationCost {
    private int gain,targetId,id;
    public MigrationCost(int gain,int targetId,int id){
        this.gain = gain;
        this.targetId = targetId;
        this.id = id;
    }

    public int getGain() {
        return gain;
    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
