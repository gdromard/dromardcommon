package net.dromard.common.util.brackets;

import java.util.*;

/**
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.0
 */
public class Bracket {
	public static int ROUND_BRACKET        = 0;
	public static int BRACED_CURLY_BRACKET = 1;
	public static int SQUARE_BRACKET       = 2;

	protected char[][] brackets = { {'(', ')'}, {'{', '{'}, {'[',']'} };
	protected Vector content = new Vector();
	protected int type;

	 public Bracket() {}

	 public Bracket(String source, int type) { this.type = type; setTextInside(source); }

	protected void addBracket(String contends) {
		//debug("Adding Bracket ... "+contends);
		addObject(new Bracket(contends, type));
	}

	protected void addText(String contends) {
		//debug("Adding Text ... "+contends);
		addObject(contends);
	}

	protected void addObject(Object o) { content.add(o); }

	protected void setTextInside(String inside) {
		IndexVector index = new IndexVector();
		Pile pile = new Pile();

		int begin = inside.indexOf(brackets[type][0]);
		int end   = inside.lastIndexOf(brackets[type][1]);

		// il n'y a pas de parenthése pas la peine de s'embêter
		if(begin == -1 || end == -1) {
			addText(inside);
			return;
		}

		// Récupération de la sous chaine possédant des parenthéses
		char[] chars = inside.substring(begin, end+1).toCharArray();

		//debug("chars : "+new String(chars));

		// Association des index des ouvrante et des fermantes
		for(int i=0; i<chars.length; ++i) {
			// ouvrante ?
			if(chars[i] == brackets[type][0]) {
				pile.add(i);
			}
			// Fermante ?
			else if(chars[i] == brackets[type][1]) {
				index.add(pile.remove(), i);
			}
		}

		// Récupération de la première ouvrante et de sa fermante
		int first = index.getIndexOfFirst(0);
		int last  = index.getIndexOfLast(0);

		// Ajout des élément avant la chaine entouré
		if(inside.length() > (begin+first) && (begin+first) > -1) {
			addText(inside.substring(0, (begin+first)));
		}

		// Ajout de la permiére chaine entourée
		if((begin+first+1) < (begin+last) && (begin+first) > -1) {
			 addBracket(inside.substring((begin+first+1), (begin+last)));
		}

		// Ajout des élément après la chaine entouré
		if((begin+last+1) > 1 && (begin+last+1) < inside.length()) {
			setTextInside(inside.substring(begin+last+1, inside.length()));
		}
	}

	public String toString() {
		String toReturn = "(";
		for(int i=0; i<content.size(); ++i) {
			toReturn += content.get(i).toString();
		}
		return toReturn+")";
	}

	public static void main(String[] args) {
		String test = "(( 1 + ( 3 - 4 )) - (3 + 8))*12";
		debug(test);
		Bracket bracket = new Bracket(test, Bracket.ROUND_BRACKET);
		debug(bracket.toString());
	}

	public int size() { return content.size(); }

	public static void debug(String msg) { 	if(true) System.out.println("[BracketManager] - "+msg); }
}

class IndexVector {
	OrderedVector first = new OrderedVector();
	Vector last  = new Vector();
	int lastFirst = 0;
	int lastLast  = 0;

	public void add(int indexOfFirst, int indexOfLast) {
		last.add(first.addOrderered(indexOfFirst+""), new Integer(indexOfLast));
	}

	public int getIndexOfFirst(int index) {
		if(first.size() <= index) return -1;
		return Integer.parseInt((String)first.get(index));
	}

	public int getIndexOfLast(int index) {
		if(last.size() <= index) return -1;
		return ((Integer)last.get(index)).intValue();
	}

	public int size() { return first.size(); }
}

class Pile {
	Vector pile = new Vector();

	public void add(int value) { pile.add(new Integer(value)); }

	public int remove() {
		if(pile.size() == 0) return -1;
		int value = ((Integer)pile.lastElement()).intValue();
		pile.removeElementAt(pile.size()-1);
		return value;
	}
}

