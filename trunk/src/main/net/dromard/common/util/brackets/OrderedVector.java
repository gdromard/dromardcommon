package net.dromard.common.util.brackets;

import java.util.Vector;

import net.dromard.common.util.StringHelper;

/**
 * Classe dérivée de la classe <b>Vector</b>.
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.0
 */
public class OrderedVector extends Vector {
	private boolean chronologicalOrder = true;

	public OrderedVector() {}
	public OrderedVector(Vector v) {
		for(int i=0; i< v.size(); ++i) {
			int insert = addOrderered((String)v.get(i));
			if(insert <= i) {
				v.remove(i+1);
			}
			else {
				v.remove(i);
				i = 0;
			}
		}
	}

	public boolean isChronologicalOrder() { return chronologicalOrder; }
	public void setChronologicalOrder(boolean chronologicalOrder) { this.chronologicalOrder = chronologicalOrder; }

	public int addOrderered(Object o) {
		String value = o.toString();
		String element=null;

		int whereToAdd = 0;
		for(int i=0; i<super.size(); ++i) {
			element = this.get(i).toString()+" ";
			if(element.length() > 0) {
				if(chronologicalOrder) {
					if(StringHelper.isNumeric(element.trim()) && StringHelper.isNumeric(value)) {
						Double doubleValue   = new Double(value);
						Double doubleElement = new Double(element);
						if(doubleElement.compareTo(doubleValue) < 0 ) {
							whereToAdd = i+1;
						}
					} else {
						if(element.toLowerCase().compareTo((value+" ").toLowerCase()) < 0 ) {
							whereToAdd = i+1;
						}
					}
				} else {
					if(StringHelper.isNumeric(element.trim()) && StringHelper.isNumeric(value)) {
						Double doubleValue   = new Double(value);
						Double doubleElement = new Double(element);
						if(doubleElement.compareTo(doubleValue) > 0 ) {
							whereToAdd = i+1;
						}
					} else {
						if(element.compareToIgnoreCase(value+" ") > 0 ) {
							whereToAdd = i+1;
						}
					}
				}
			}
		}
		super.add(whereToAdd, o);
		return whereToAdd;
	}

	public void order() {
		for(int i=0; i< size(); ++i) {
			int insert = addOrderered(get(i));
			if(insert <= i) {
				remove(i+1);
			}
			else {
				remove(i);
				i = 0;
			}
		}
	}

	public static Vector setOrdered(Vector v, boolean chronologicalOrder) {
		OrderedVector v2 = new OrderedVector(v);
		v2.setChronologicalOrder(chronologicalOrder);
		return v2;
	}

	public static void setOrdered(OrderedVector[] v, int witchToOrder) {
		for(int i=0; i< v[witchToOrder].size(); ++i) {
			// Ajout de maniére triée
			int insert = v[witchToOrder].addOrderered(v[witchToOrder].get(i));

			// Ajout à l'index trouvé
			for(int j=0; j<v.length; ++j) if(j != witchToOrder) v[j].add(insert, v[j].get(i));

			// Suppression de la ligne ajoutée
			if(insert < i) {
				for(int j=0; j< v.length; ++j) v[j].remove(i+1);
				//if(!v[witchToOrder].isChronologicalOrder())
				//i--;
			} else if(insert > i) {
				for(int j=0; j< v.length; ++j) v[j].remove(i);
				i--;
			} else if(insert == i) {
				for(int j=0; j< v.length; ++j) v[j].remove(i+1);
				//if(v[witchToOrder].isChronologicalOrder()) i--;
			}
		}
	}


	public static void debug(String msg) {
		if(true)
			//JOptionPane.showMessageDialog(null, msg, "Infos de Debogage :", JOptionPane.INFORMATION_MESSAGE);
			System.out.println("[OrderedVector] - "+msg);
	}

	public static void main(String[] args) {
		OrderedVector v1 = new OrderedVector();
		OrderedVector v2 = new OrderedVector();
		//Vector v = new Vector();

		v1.setChronologicalOrder(false);
		v2.setChronologicalOrder(false);

		v1.add("2");v2.add("b");
		v1.add("3");v2.add("c");
		v1.add("13");v2.add("m");
		v1.add("1");v2.add("a");
		v1.add("4");v2.add("d");
		v1.add("11");v2.add("k");
		v1.add("6");v2.add("f");
		v1.add("7");v2.add("g");
		v1.add("10");v2.add("j");
		v1.add("5");v2.add("e");
		v1.add("14");v2.add("n");
		v1.add("8");v2.add("h");
		v1.add("9");v2.add("i");
		v1.add("12");v2.add("l");

		debug(v1.toString());
		debug(v2.toString());

		OrderedVector[] v = new OrderedVector[2];
		v[0] = v1;
		v[1] = v2;

		//setOrdered(v, 1);

		debug("");
		debug(v[0].toString());
		debug(v[1].toString());

		//etOrdered(v, 0);
		debug("");
		debug(v[0].toString());
		debug(v[1].toString());
	}
}