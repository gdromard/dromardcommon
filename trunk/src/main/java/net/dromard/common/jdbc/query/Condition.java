package net.dromard.common.jdbc.query;
import net.dromard.common.util.StringHelper;

/**
 * Classe de Gestion d'une condition SQL (... WHERE condition AND condtition ...)
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.0
 */
public class Condition {
	private static String[] operators = { ">", ">=", "<", "<=", " LIKE ", "=", " IS " ," IS NOT ", " NOT ", " IN " };

	public static int MORE = 0;
	public static int MORE_OR_EQUALS = 1;
	public static int LESS = 2;
	public static int LESS_OR_EQUALS = 3;
	public static int LIKE = 4;
	public static int EQUALS = 5;
	public static int IS = 6;
	public static int IS_NOT = 7;
	public static int NOT = 8;
	public static int IN = 9;

	private Object first;
	private Object second;
	private int    operator = EQUALS;

	public Condition(String field, int operator, String value)           { setCondition(field, operator, value); }
	public Condition(Condition condition, int operator, String value)    { setCondition(condition, operator, value); }
	public Condition(String field, int operator, Condition condition)    { setCondition(field, operator, condition); }
	public Condition(String field, String operator, String value)        { setCondition(field, mapStringToInt(operator), value); }
	public Condition(Condition condition, String operator, String value) { setCondition(condition, mapStringToInt(operator), value); }
	public Condition(String field, String operator, Condition condition) { setCondition(field, mapStringToInt(operator), condition); }
	public Condition(String condition)                                   { setCondition(condition); }

	public void setCondition(String field, String operator, String value)        { setCondition(field, mapStringToInt(operator), value); }
	public void setCondition(Condition condition, String operator, String value) { setCondition(condition, mapStringToInt(operator), value); }
	public void setCondition(String field, String operator, Condition condition) { setCondition(field, mapStringToInt(operator), condition); }

	public void setCondition(Condition condition, int operator, String value) {
		setFirstElement(condition);
		setOperator(operator);
		setSecondElement(value);
	}

	public void setCondition(String field, int operator, Condition condition) {
		setFirstElement(field);
		setOperator(operator);
		setSecondElement(condition);
	}

	public void setCondition(String field, int operator, String value) {
		setFirstElement(field);
		setOperator(operator);
		setSecondElement(value);
	}

	public boolean setCondition(String condition) {
		for(int i=0; i<operators.length; ++i) {
			 if(condition.indexOf(operators[i]) > -1) {
				first    = StringHelper.subStringBefore(condition, operators[i]);
				second   = StringHelper.subStringAfter( condition, operators[i]);
				if( first.toString().startsWith("SELECT")) first  = new SelectQuery(first.toString() );
				if(second.toString().startsWith("SELECT")) second = new SelectQuery(second.toString());
				operator = i;
				return true;
			 }
		}
		return false;
	}

	public String toString() {
		String toReturn="";
		if(first != null) toReturn += first.toString();
		toReturn += operators[operator];
		if(second != null) toReturn += second.toString();

		return toReturn;
	}

	public void setFirstElement(Condition element)  { this.first = element; }
	public void setFirstElement(String element)     { this.first = element; }
	public void setSecondElement(Condition element) { this.second = element; }
	public void setSecondElement(String element)    { this.second = element; }
	public void setOperator(String operator) { mapStringToInt(operator); }
	public void setOperator(int operator)    { this.operator = operator; }


	/**
	 * Si ça plante ça renvoi le Type Par default 'EQUALS'
	 * @param operator operateur de comparaison des deux éléments
	 * @return le type d'opétateur trouvé
	 */
	private int mapStringToInt(String operator) {
		for(int i=0; i<operators.length; ++i)
			 if(operator.equalsIgnoreCase(operators[i])) return i;

		return EQUALS;
	}
}
