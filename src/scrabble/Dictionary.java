package scrabble;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Dictionary {
    protected DictNode root;
    public enum Condition {
        PREFIX, SUFFIX, SUBSTRING;

        public boolean satisfies(String str1, String str2) {
            switch (this) {
                case PREFIX:
                    return str1.startsWith(str2);
                case SUFFIX:
                    return str1.endsWith(str2);
                case SUBSTRING:
                    return str1.contains(str2);
            }

            return false;
        }
    }

    abstract public boolean search(String word);

    abstract public void insert(File dict);

    private void getStrings(Set<String> strings, Condition condition, String prefixString, String conditionString, Map<Character, DictNode> transitionMap) {
        for (Map.Entry<Character, DictNode> entry : transitionMap.entrySet()) {
            String newString = prefixString + entry.getKey();
            DictNode current = entry.getValue();
            if (current.isWord() && condition.satisfies(newString, conditionString)) {
                strings.add(newString);
            }
            getStrings(strings, condition, newString, conditionString, current.getCharacterNodeMap());
        }
    }

    public Set<String> endsWith(String suffix) {
        Set<String> strings = new HashSet<>();
        getStrings(strings, Condition.SUFFIX, "", suffix, root.getCharacterNodeMap());
        return strings;
    }

    public Set<String> startsWith(String prefix) {
        Set<String> strings = new HashSet<>();
        DictNode temp = root.transition(prefix);
        if (temp != null) {
            if (temp.isWord()) strings.add(prefix);
            getStrings(strings, Condition.PREFIX, prefix, prefix, temp.getCharacterNodeMap());
        }
        return strings;
    }

    public Set<String> contains(String contains) {
        Set<String> strings = new HashSet<>();
        getStrings(strings, Condition.SUBSTRING, "", contains, root.getCharacterNodeMap());
        return strings;
    }

    public DictNode getRootNode() {
        return root;
    }
}
