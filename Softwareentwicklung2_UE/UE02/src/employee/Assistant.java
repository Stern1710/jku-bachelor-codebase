package employee;

import inout.Out;

public class Assistant extends Subordinate {
	
	/**
	 * Constructor for the Assistant
	 * @param name	The name of the assistant
	 * @param expert The expert the assistant is subordinate
	 * @param salary The monthly salary of the assistant
	 */
	public Assistant(String name, Expert expert, double salary) {
		super(name, salary, expert);
	}

	/**
	 * Implementation of work for the Assistant
	 * Will work if he has to do tests or implementations, but will give all other forms of work to his manager
	 */
	@Override
	public void doWork(Work work) throws WorkException {
		switch (work) {
		case Test:
			Out.println("I, an Assistent, will do this test perfectly!");
			break;
		case Implement:
			Out.println("I, an Assistent, will do this implementation perfectly!");
			break;
		default:
			this.getManager().doWork(work);
			break;
		}
	}
	
	/**
	 * Overrides the standard implementation in Employee to tell it is a assistant
	 */
	@Override
	public String toString() {
		return "Assistant: " + getName();
	}
}
