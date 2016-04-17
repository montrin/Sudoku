import java.util.Comparator;

/**
 * Created by monicatrink on 17/04/16.
 * Comparator class to evaluate domains according to the size of possible valid values
 */
    public class DomainComparator implements Comparator<Domain> {
        @Override
        public int compare(Domain d1, Domain d2) {
            return (d1.getPossibleValues().size()) - (d2.getPossibleValues().size());
        }
    }
