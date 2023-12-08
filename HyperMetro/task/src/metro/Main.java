package metro;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Node head = null;
        Node tail = null;

        String filePath = "";
        if (args.length > 0) {
            filePath = args[0];
        }

        File file = new File(filePath);


        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String val = scanner.nextLine();
                Node station = new Node(val);

                if (head == null) {
                    head = station;
                    tail = station;
                } else {
                    tail.addTailNode(station);
                    tail = station;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error! Such a file doesn't exist!");
        }



        Node station1 = null;
        Node station2 = null;
        Node station3 = null;

        if (head != null) {
            Node newHead = new Node("depot");
            head.addHeadNode(newHead);
            head = newHead;

            Node newTail = new Node("depot");
            tail.addTailNode(newTail);
            tail = newTail;

            station1 = head;
            station2 = station1.getNext();
            station3 = station2.getNext();
        }



        while (station3 != null) {
            System.out.printf("%s - %s - %s", station1.getVal(), station2.getVal(), station3.getVal());
            System.out.println();

            station1 = station2;
            station2 = station3;
            station3 = station3.getNext();

        }
    }
}

