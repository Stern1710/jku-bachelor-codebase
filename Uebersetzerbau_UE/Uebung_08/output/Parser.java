

import java.util.*;



public class Parser {
	public static final int _EOF = 0;
	public static final int _text = 1;
	public static final int _country_id = 2;
	public static final int _location = 3;
	public static final int _number = 4;
	public static final int maxT = 11;

	static final boolean _T = true;
	static final boolean _x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;

	public class Phonebook {
	private final List<Entry> entries = new ArrayList<>();

	public boolean addEntry (Entry entry) {
		for (Entry e : entries) {
			if (e.getName().equals(entry.getName())) {
				return false; 
			}
		}
		entries.add(entry);
		return true;
	}

	int countPhoneNumbers() {
		return entries.stream().mapToInt(e -> e.getNumberSize()).sum();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Phonebook with ").append(countPhoneNumbers()).append(" phone numbers\n\n");

		for (Entry e : entries) {
			sb.append(e.toString());
		}

		return sb.toString();
	}
}

public class Entry {
	private String name;
	private final List<PhoneNumber> numbers = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addNumber (PhoneNumber number) {
		numbers.add(number);
	}

	public int getNumberSize() {
		return numbers.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(":\n");

		for (PhoneNumber number : numbers) {
			sb.append("  ").append(number.toString()).append("\n");
		}

		return sb.toString();
	}
}

public class PhoneNumber {
	private String number;
	private String infoText;
	
	public PhoneNumber(String number) {
		this.number = number;
		this.infoText = null;
	}

	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(number);

		if (infoText != null) {
			sb.append(" ");
			sb.append(infoText);
		}

		return sb.toString();
	}
}





	public Parser(Scanner scanner) {
		this.scanner = scanner;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	boolean StartOf (int s) {
		return set[s][la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}
	
	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}
	
	void Phonebook() {
		Entry entry = Entry();
		Phonebook phoneBook = new Phonebook();
		phoneBook.addEntry(entry); 
		while (la.kind == 1) {
			entry = Entry();
			if (!phoneBook.addEntry(entry)) { 
			SemErr("Phonebook entries must have unique names");  
			}
			
		}
		System.out.println(phoneBook.toString()); 
	}

	Entry  Entry() {
		Entry  entry;
		entry = new Entry(); 
		String word = Words();
		entry.setName(word); 
		Expect(5);
		PhoneNumber number = PhoneNumber();
		entry.addNumber(number); 
		while (la.kind == 6) {
			Get();
			number = PhoneNumber();
			entry.addNumber(number); 
		}
		Expect(7);
		return entry;
	}

	String  Words() {
		String  word;
		StringBuilder sb = new StringBuilder(); 
		Expect(1);
		sb.append(t.val); 
		while (la.kind == 1) {
			Get();
			sb.append(" ").append(t.val); 
		}
		word = sb.toString(); 
		return word;
	}

	PhoneNumber  PhoneNumber() {
		PhoneNumber  pn;
		StringBuilder sb = new StringBuilder(); 
		if (la.kind == 8) {
			Get();
		} else if (la.kind == 2) {
			Get();
		} else SynErr(12);
		sb.append(t.val); 
		Expect(3);
		sb.append(t.val); 
		Expect(4);
		sb.append(t.val);
		pn = new PhoneNumber(sb.toString());
		
		if (la.kind == 9) {
			sb = new StringBuilder(); 
			Get();
			sb.append(t.val); 
			String word = Words();
			sb.append(word); 
			Expect(10);
			sb.append(t.val); 
			pn.setInfoText(sb.toString());
			
		}
		return pn;
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		Phonebook();
		Expect(0);

	}

	private static final boolean[][] set = {
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x}

	};
} // end Parser


class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text
	
	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}
	
	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "text expected"; break;
			case 2: s = "country_id expected"; break;
			case 3: s = "location expected"; break;
			case 4: s = "number expected"; break;
			case 5: s = "\":\" expected"; break;
			case 6: s = "\",\" expected"; break;
			case 7: s = "\".\" expected"; break;
			case 8: s = "\"0\" expected"; break;
			case 9: s = "\"(\" expected"; break;
			case 10: s = "\")\" expected"; break;
			case 11: s = "??? expected"; break;
			case 12: s = "invalid PhoneNumber"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {	
		printMsg(line, col, s);
		count++;
	}
	
	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}
	
	public void Warning (int line, int col, String s) {	
		printMsg(line, col, s);
	}
	
	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}
