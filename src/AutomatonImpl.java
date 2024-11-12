import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class AutomatonImpl implements Automaton {

    class StateLabelPair {
        int state;
        char label;
        public StateLabelPair(int state_, char label_) { state = state_; label = label_; }

        @Override
        public int hashCode() {
            return Objects.hash((Integer) state, (Character) label);
        }

        @Override
        public boolean equals(Object o) {
            StateLabelPair o1 = (StateLabelPair) o;
            return (state == o1.state) && (label == o1.label);
        }
    }

    HashSet<Integer> start_states;
    HashSet<Integer> accept_states;
    HashSet<Integer> current_states = new HashSet<>();;
    HashMap<StateLabelPair, HashSet<Integer>> transitions;

    private HashSet<Integer> all_states = new HashSet<>();
    private Map<Integer, Boolean> stateAcceptanceMap = new HashMap<>();

    public AutomatonImpl() {
        start_states = new HashSet<Integer>();
        accept_states = new HashSet<Integer>();
        transitions = new HashMap<StateLabelPair, HashSet<Integer>>();
    }

    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
        all_states.add(s);
        stateAcceptanceMap.put(s, is_accept);
        if (is_start) {
            current_states.add(s);  
        }
    }

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
        StateLabelPair pair = new StateLabelPair(s_initial, label);
        transitions.computeIfAbsent(pair, k -> new HashSet<>()).add(s_final);
    }

    @Override
    public void reset() {
        current_states.clear();
        for (int state : all_states) {
            if (stateAcceptanceMap.get(state) == false) {  
                current_states.add(state);
            }
        }
    }

    @Override
    public void apply(char input) {
        HashSet<Integer> next_states = new HashSet<>();

        for (int state : current_states) {
            StateLabelPair pair = new StateLabelPair(state, input);
            HashSet<Integer> possible_next_states = transitions.get(pair);
            if (possible_next_states != null) {
                next_states.addAll(possible_next_states);
            }
        }

        if (!next_states.isEmpty()) {
            current_states = next_states;
        } else {
            current_states.clear();
        }
    }

    @Override
    public boolean accepts() {
        for (int state : current_states) {
            if (stateAcceptanceMap.getOrDefault(state, false)) {
                return true;  
            }
        }
        return false;  
    }

    @Override
    public boolean hasTransitions(char label) {
        for (int state : current_states) {
            StateLabelPair pair = new StateLabelPair(state, label);
            if (transitions.containsKey(pair)) {
                return true;
            }
        }
        return false;
    }

}
