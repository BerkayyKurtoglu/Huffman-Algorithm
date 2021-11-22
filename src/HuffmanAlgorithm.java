import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanAlgorithm {

    static HashMap<Character,String> encodedTable = new HashMap<>();
    static HuffmanNode root = new HuffmanNode();
    static String encodedString = "";

    public static void createEncodedTable(@NotNull HuffmanNode root, String s, BufferedWriter writer) throws IOException {
        if (root.left == null && root.right == null && Character.isLetter(root.c)) {
            writer.write(root.c + " -> "+s+"\n");
            writer.flush();
            System.out.println(root.c + "   ->  " + s);
            encodedTable.put(root.c,s);
            return;
        }
        createEncodedTable(root.left, s + "0",writer);
        createEncodedTable(root.right, s + "1",writer);
    }

    public static void createEncodedString (char @NotNull [] st, @NotNull BufferedWriter writer) throws IOException {
        writer.write("Encoded String : ");
        writer.flush();
        for (char c : st) {
            encodedTable.forEach(((character, s) -> {
                if (c == character) {
                    encodedString = encodedString+s+" ";
                }
            }));
        }
        writer.write(encodedString);
        writer.flush();
    }

    public static void decodeCompressedFile(BufferedWriter writer) throws IOException {
        char[] array = encodedString.toCharArray();
        StringBuilder rangeString = new StringBuilder();
        StringBuilder completedSentence = new StringBuilder();
        for (int i = 0; i < encodedString.length(); i++) {
            while (array[i] != ' '){
                rangeString.append(array[i]);
                i++;
            }
            StringBuilder finalRangeString = rangeString;
            encodedTable.forEach(((character, s) -> {
                if (finalRangeString.toString().equals(s)){
                    completedSentence.append(character);
                }
            }));
            System.out.println(rangeString);
            rangeString = new StringBuilder();
        }
        writer.write("\nDecoded sentence -> "+ completedSentence.toString());
        writer.flush();
        System.out.println(completedSentence);
    }

    public static void main(String[] args) throws IOException {

        File file = new File(Util.FOLDER_PATH); //Read File
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        File resultFile = new File("Result.txt"); //Create to write result
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(resultFile));

        //Read The File and Create A HashMap with Frequency
        String st;
        char [] initialStringArray = {};
        HashMap<Character,Integer> keyAndFrequencyMap = new HashMap<>();
        while ((st = bufferedReader.readLine()) != null){
            initialStringArray =st.toCharArray();
            fileWriter.write("String that is going to be encoded : "+st+"\n");
            fileWriter.flush();
            for (char value : initialStringArray) {
                int number = 0;
                for (int j = 0; j < st.length(); j++) {
                    if (value == initialStringArray[j]) number += 1;
                }
                keyAndFrequencyMap.put(value, number);
            }
        }

        //Queue is ready
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(keyAndFrequencyMap.size(),new CustomComparator());
        keyAndFrequencyMap.forEach((c,f)->{
            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.c = c;
            huffmanNode.frequency = f;
            huffmanNode.left = null;
            huffmanNode.right = null;
            queue.add(huffmanNode);
        });

        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();

            HuffmanNode f = new HuffmanNode();
            f.frequency = left.frequency + right.frequency;
            f.c = '-';
            f.left = left;
            f.right = right;
            root = f;
            queue.add(f);
        }

        fileWriter.write("Encoded Table : \n");
        System.out.println("Encoded Table : ");
        createEncodedTable(root,"",fileWriter);
        createEncodedString(initialStringArray,fileWriter);
        decodeCompressedFile(fileWriter);
    }

}
