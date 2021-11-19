import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanAlgorithm {

    static HashMap<Character,String> encodedTable = new HashMap<>();
    static HuffmanNode root = new HuffmanNode();


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

    public static void createEncodedString (char[] st,BufferedWriter writer) throws IOException {
        writer.write("Encoded String : ");
        writer.flush();
        for (char c : st) {
            encodedTable.forEach(((character, s) -> {
                if (c == character) {
                    try {
                        writer.write(s);
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
    }

    public static void decodeCompressedFile(){

    }

    public static void main(String[] args) throws IOException {

        File file = new File(Util.FOLDER_PATH);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        //Read The File and Create A HashMap with Frequency
        String st = "";
        char [] initialStringArray = {};
        HashMap<Character,Integer> keyAndFrequencyMap = new HashMap<>();
        while ((st = bufferedReader.readLine()) != null){
            initialStringArray =st.toCharArray();
            System.out.println(st);
            for (char value : initialStringArray) {
                int number = 0;
                char c = value;
                for (int j = 0; j < st.length(); j++) {
                    if (c == initialStringArray[j]) number += 1;
                }
                keyAndFrequencyMap.put(c, number);
            }
        }

        File resultFile = new File("Result.txt");
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(resultFile));

        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(keyAndFrequencyMap.size(),new CustomComparator());
        //Queue is ready now

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

    }


}
