package edu.iastate.cs228.hw4;
import java.util.Stack;

/**
 * @author Lucas Sorge
 */

/**
 * The MsgTree class represents a node in a binary tree used for decoding encoded messages.
 * 
 * Each node holds a character (payloadChar) and references to its left and right child nodes.
 */
public class MsgTree {
	
	/**
	 * The character stored in this node
	 */
	public char payloadChar;
	
	/**
	 * Reference to the left child
	 */
	public MsgTree left;
	
	/**
	 * Reference to the right child
	 */
	public MsgTree right;

	/**
	 * Static index used in tree construction
	 */
	private static int staticCharIdx = 0;
	
	/**
	 * Retrieves the current value of the static character index.
	 * 
	 * @return The current value of the static character index.
	 */
	public static int getStaticCharIdx() {
		return staticCharIdx;
	}

	/**
	 * Sets the value of the static character index.
	 * 
	 * @param staticCharIdx The new value to set for the static character index.
	 */
	public static void setStaticCharIdx(int staticCharIdx) {
		MsgTree.staticCharIdx = staticCharIdx;
	}

	/**
	 * Constructor building the tree from a string
	 * 
	 * @param encodingString string representing the tree structure and characters.
	 */
	public MsgTree(String encodingString) {
		
		if (encodingString == null || encodingString.length() < 2) {
			return;
		}
		Stack <MsgTree> stack = new Stack<>();
		
		int index = 0;
		
		this.payloadChar = encodingString.charAt(index++);
		stack.push(this);
		
		MsgTree current = this;
		String lastOpt = "in";
		
		while (index < encodingString.length()) {
			
			MsgTree node = new MsgTree(encodingString.charAt(index++));
			
			if (lastOpt.equals("in")) {
				current.left = node;
				if (node.payloadChar == '^') {
					current = stack.push(node);
					lastOpt = "in";
				} else {
					if (!stack.empty())
						current = stack.pop();
					lastOpt = "out";
				}
			} else {
				current.right = node;
				if (node.payloadChar == '^') {
					current = stack.push(node);
					lastOpt = "in";
				} else {
					if (!stack.empty())
						current = stack.pop();
					lastOpt = "out";
				}
			}
		}
	}

	/**
	 * Constructs a single node with a specified character and null children
	 * 
	 * @param payloadChar The character to be stored in the node
	 */
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}

	/**
	 * Prints the codes for each character in the provided string.
	 * 
	 * @param root The root of the tree
	 * @param code The string containing characters for which codes are to be printed.
	 */
	public static void printCodes(MsgTree root, String code) {
		System.out.println("character code\n-------------------------");
		for (char ch : code.toCharArray()) {
			getCode(root, ch, binaryCode = "");
			System.out.println("    " + (ch == '\n' ? "\\n" : ch + " ") + "    " + binaryCode);
		}
	}

	private static String binaryCode;

	/**
	 * Recursively searches the tree to find the code for a given character.
	 * 
     * @param root The current node in the tree.
     * @param ch The character for which the code is sought.
     * @param path The path taken in the tree to reach the current node.
     * @return True if the character is found, otherwise false.
	 */
	private static boolean getCode(MsgTree root, char ch, String path) {
		
		if (root != null) {
			
			if (root.payloadChar == ch) {
				binaryCode = path;
				return true;
			}	
			return getCode(root.left, ch, path + "0") || getCode(root.right, ch, path + "1");
		}	
		return false;
	}

	/**
	 * Decodes message using the pulled code alphabet
	 * 
     * @param codes The tree used for decoding.
     * @param msg The encoded message to be decoded.
	 */
	public void decode(MsgTree codes, String msg) {
		
		System.out.println("MESSAGE:");
		
		MsgTree cur = codes;
		
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < msg.length(); i++) {
			char ch = msg.charAt(i);
			cur = (ch == '0' ? cur.left : cur.right);
			if (cur.payloadChar != '^') {
				getCode(codes, cur.payloadChar, binaryCode = "");
				stringBuilder.append(cur.payloadChar);
				cur = codes;
			}
		}
		System.out.println(stringBuilder.toString());
		statistc(msg, stringBuilder.toString());
	}

	/**
     * Calculates and prints statistics about the encoding process.
     * 
	 * @param encodeStr The encoded string
	 * @param decodeStr The decoded string
	 */
	private void statistc(String encodeStr, String decodeStr) {
		System.out.println("\nSTATISTICS:");
		System.out.println(String.format("Avg bits/char: %.1f", encodeStr.length() / (double) decodeStr.length()));
		System.out.println("Total Characters: " + decodeStr.length());
		System.out.println(String.format("Space Saving: %.1f%%", (1d - decodeStr.length() / (double) encodeStr.length()) * 100));
	}
}
