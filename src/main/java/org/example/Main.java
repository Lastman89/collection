package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args)  {

        final int lenght = 10_000;
        final int lenghtText = 1_000;

        BlockingQueue<String> textA = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> textB = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> textC = new ArrayBlockingQueue<>(100);
        String[] texts = new String[lenghtText];
        Thread stack = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                texts[i] = generateText("abc", lenght);
                try {
                    textA.put(texts[i]);
                    textB.put(texts[i]);
                    textC.put(texts[i]);
                } catch (InterruptedException e) {
                    return;
                }

            }

        });
        stack.start();

        new Thread(() -> {
            System.out.println("Максимальное значение символа а: " + counterSymbol(textA, texts, 'a'));
        }).start();

        new Thread(() -> {
            System.out.println("Максимальное значение символа b: " + counterSymbol(textB, texts, 'b'));
        }).start();

        new Thread(() -> {
            System.out.println("Максимальное значение символа c: " + counterSymbol(textC, texts, 'c'));
        }).start();


    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int counterSymbol(BlockingQueue<String> text2, String[] text, char symbol) {
        int max = 0;
        for (int i = 0; i < text.length; i++) {
            int counter = 0;
            String c;
            try {
                c = text2.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int j = 0; j < c.length(); j++) {
                if (c.charAt(j) == symbol) {
                    counter++;
                }
                if (counter > max) {
                    max = counter;
                }
            }

        }
        return max;
    }
}

