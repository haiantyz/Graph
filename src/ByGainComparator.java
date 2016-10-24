import java.util.Comparator;

/**
 * Created by tyz on 2016/10/20.
 */
public class ByGainComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        int aFirstGain = ((MigrationCost) o1).getGain();
        int aSecondGain = ((MigrationCost) o2).getGain();
        int diff = Math.abs(aFirstGain-aSecondGain);
        if (diff>0)
            return 1;
        else if(diff<0)
            return -1;
        else
            return 0;


    }
}
