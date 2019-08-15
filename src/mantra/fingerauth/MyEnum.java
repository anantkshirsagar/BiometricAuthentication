package mantra.fingerauth;

import java.io.Serializable;



public abstract class MyEnum implements Comparable, Serializable {
	public static void main(String[] args) {
		My[] values = My.values();

	}
}

enum My implements MyInterface {
	;

	@Override
	public void add() {
		// TODO Auto-generated method stub

	}

}

interface MyInterface {
	void add();
}
