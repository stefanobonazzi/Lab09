package it.polito.tdp.borders.model;

public class Border {
	
	private int state1no;
	private int stateno2;
	
	public Border(int state1no, int stateno2) {
		this.state1no = state1no;
		this.stateno2 = stateno2;
	}

	public int getState1no() {
		return state1no;
	}

	public void setState1no(int state1no) {
		this.state1no = state1no;
	}

	public int getStateno2() {
		return stateno2;
	}

	void setStateno2(int stateno2) {
		this.stateno2 = stateno2;
	}
	
}
