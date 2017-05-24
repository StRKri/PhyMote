package org.phymote.statics;

public class StringHelper {

	public static String join(String token, String[] strings) {
		if (strings.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer();

		for (int x = 0; x < (strings.length - 1); x++) {
			sb.append(strings[x]);
			sb.append(token);
		}
		sb.append(strings[strings.length - 1]);

		return (sb.toString());
	}

	public static final String accUnit = "m/s²";
	public static final String spdUnit = "m/s";
	public static final String posUnit = "m";

	public static String integrateUnit(String unit) {
		if (unit.equals(StringHelper.accUnit)) {
			return StringHelper.spdUnit;
		} else if (unit.equals(StringHelper.spdUnit)) {
			return StringHelper.posUnit;
		} else {
			return unit + "*";
		}
	}

	public static final String accSymbol = "a_";
	public static final String spdSymbol = "v_";
	public static final String posSymbol = "s_";

	public static String integrateSymbol(String symbol) {
		if (symbol.startsWith(StringHelper.accSymbol)) {
			return StringHelper.spdSymbol + symbol.substring(2);
		} else if (symbol.startsWith(StringHelper.spdSymbol)) {
			return StringHelper.posSymbol + symbol.substring(2);
		} else {
			return symbol + "*";
		}
	}
}
