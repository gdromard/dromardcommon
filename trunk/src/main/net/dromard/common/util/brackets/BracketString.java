package net.dromard.common.util.brackets;

import java.util.*;

import net.dromard.common.util.StringHelper;

/**
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.0
 */
public class BracketString {
	public static int ROUND_BRACKET       = 0;
	public static int BRACE_CURLY_BRACKET = 1;
	public static int SQUARE_BRACKET      = 2;
	public static int OPEN_BRACKET        = 0;
	public static int CLOSE_BRACKET       = 1;
	private static String[][] bracketTypes = {  { "(", ")" }, { "{", "}" }, { "[", "]" }  };


	private String content;

	private boolean[] hasBracket = new boolean[3];
	private int[]     numberOfBracket = new int[3];
	private Vector[][] bracketIndex   = new Vector[3][2];

	public BracketString(String content) {
		for (int i=0; i<bracketTypes.length; ++i) {
			numberOfBracket[i] = StringHelper.countOccurences(content, bracketTypes[i][0]);
			if(StringHelper.countOccurences(content, bracketTypes[i][1]) != numberOfBracket[i]) {
				hasBracket[i] = false;
				numberOfBracket[i] = 0;
				for (int j=0;j<bracketTypes[i].length; ++j) {
					bracketIndex[i][j]  = null;
				}
			} else {
				hasBracket[i] = true;

				// Gestion des ouvrantes
				int index = content.indexOf(bracketTypes[i][0], -1);

				while(index > -1) {
					bracketIndex[i][0]  = new Vector();
					bracketIndex[i][0].add(new Integer(index));
					index = content.indexOf(bracketTypes[i][0], index);
				}

				// Gestion des fermantes
				index = content.indexOf(bracketTypes[i][1], -1);

				while(index > -1) {
					bracketIndex[i][1]  = new Vector();
					//bracketIndex[i][1].add(countHowManyBefore(i, index), new Integer(index));
					index = content.indexOf(bracketTypes[i][1], index);
				}
			}
		}
	}

	private int countHowManyBefore(int type, int openOrCloseBracket, int position) {
		int count = 0;
		//while(getBracketIndex(type, openOrCloseBracket).get(count) < position) count++;
		return count;
	}

	 public boolean getHasBracket(int bracketType) {
		  return hasBracket[bracketType];
	 }

	 public int getNumberOfBracket(int bracketType) {
		  return numberOfBracket[bracketType];
	 }

	 public String getContent() {
		  return content;
	 }

	 public Vector getBracketIndex(int bracketType, int openOrCloseBracket) {
		  return bracketIndex[bracketType][openOrCloseBracket];
	 }
}