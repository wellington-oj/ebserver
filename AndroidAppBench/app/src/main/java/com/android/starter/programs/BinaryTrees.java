package com.android.starter.programs;

import java.io.FileOutputStream;
import java.io.IOException;

/* The Computer Language Benchmarks Game
 * https://salsa.debian.org/benchmarksgame-team/benchmarksgame/
 *
 * contributed by Jarkko Miettinen
 * modified by Daryl Griffith
 * *reset*
 */

// 27/05/2022
// https://benchmarksgame-team.pages.debian.net/benchmarksgame/program/binarytrees-java-3.html

public class BinaryTrees {

    public void runCode(int maxDepth, FileOutputStream writer) throws IOException {
        int stretchDepth = maxDepth + 1;
        writer.write(("stretch tree of depth " + stretchDepth +
                "\t check: " + checkTree(createTree(stretchDepth)) + "\n").getBytes());
        trees(maxDepth, writer);
    }

    public static void trees(int maxDepth, FileOutputStream writer) throws IOException {
        TreeNode longLastingNode = createTree(maxDepth);
        int depth = 4;

        do {
            int iterations = 16 << (maxDepth - depth);

            loops(iterations, depth, writer);
            depth += 2;
        } while (depth <= maxDepth);
        writer.write(("long lived tree of depth " + maxDepth
                + "\t check: " + checkTree(longLastingNode) + "\n").getBytes());
    }

    public static void loops(int iterations, int depth, FileOutputStream writer) throws IOException {
        int check = 0;
        int item = 0;

        do {
            check += checkTree(createTree(depth));
            item++;
        } while (item < iterations);
        writer.write((iterations + "\t trees of depth " +
                depth + "\t check: " + check + "\n").getBytes());
    }

    public static TreeNode createTree(int depth) {
        TreeNode node = new TreeNode();

        if (depth > 0) {
            depth--;
            node.left = createTree(depth);
            node.right = createTree(depth);
        }
        return node;
    }

    public static int checkTree(TreeNode node) {
        if (node.left == null) {
            return 1;
        }
        return checkTree(node.left) + checkTree(node.right) + 1;
    }

    public static class TreeNode {

        private int item;
        private TreeNode left, right;
    }
}
