import java.util.Comparator;

public class CustomComparator implements Comparator<HuffmanNode> {
    @Override
    public int compare(HuffmanNode o1, HuffmanNode o2) {
        return o1.frequency-o2.frequency;
    }
}
