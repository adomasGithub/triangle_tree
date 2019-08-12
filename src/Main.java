import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        List<List<Integer>> sequencies = findPathsOfOddEvenNumbers("binary_triangle_2.txt");

        List<Integer> biggestSequence = sequencies
                .stream()
                .max(Comparator.comparingInt(s -> s.stream().mapToInt(Integer::intValue).sum()))
                .orElseThrow(NoSuchElementException::new);

        System.out.println(biggestSequence.stream().mapToInt(Integer::intValue).sum());
        biggestSequence.forEach(pathNode -> System.out.print(pathNode.toString() + " "));
    }

    // other way to solve this task is to use recursion function, but it will fail with StackOverflow on big input
    static List<List<Integer>> findPathsOfOddEvenNumbers(String filename) throws IOException {
        File file = new File(filename);

        BufferedReader br = new BufferedReader(new FileReader(file));

        // pair key is index of node, value - node value, array will store all possible paths with it's values
        List<List<Pair<Integer, Integer>>> paths = new ArrayList<>();

        // define first path with root node
        List<Pair<Integer, Integer>> firstPath = new ArrayList<>();
        firstPath.add(new Pair<>(0, Integer.parseInt(br.readLine())));
        paths.add(firstPath);

        // starting from second level of triangle three and going through by each node finding if node is matching parent by (parent index = child index or parent index = child index - 1),
        // if node matches then new path created with new node appended.
        // existing paths which was not appended with new node will not remain for next level of triangle tree

        String numbersLine = null;
        while ((numbersLine = br.readLine()) != null) {
            List<Integer> nodes = Arrays
                    .stream(numbersLine.split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<List<Pair<Integer, Integer>>> newPaths = new ArrayList<>();
            for (int nodeIndex = 0; nodeIndex < nodes.size(); nodeIndex++) {
                Integer currentNode = nodes.get(nodeIndex);
                for (int pathIndex = 0; pathIndex < paths.size(); pathIndex++) {

                    List<Pair<Integer, Integer>> path = paths.get(pathIndex);
                    Pair<Integer, Integer> lastPathNode = path.get(path.size() - 1);
                    if ((lastPathNode.getKey() == nodeIndex || lastPathNode.getKey() == nodeIndex - 1) && (lastPathNode.getValue() % 2 != currentNode % 2)) {
                        List<Pair<Integer, Integer>> newPath = new ArrayList<>(path);
                        newPath.add(new Pair<>(nodeIndex, currentNode));
                        newPaths.add(newPath);
                    }
                }
            }
            paths = newPaths;
        }

        return paths
                .stream()
                .map(s -> s.stream().map(Pair::getValue).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}


