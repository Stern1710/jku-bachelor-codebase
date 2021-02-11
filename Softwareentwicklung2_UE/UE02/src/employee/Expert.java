package employee;

import inout.Out;

public class Expert extends Subordinate {

	public Expert(String name, Manager manager, double salary) {
		super(name, salary, manager);
	}

	/**
	 * Implementation of work method for the Expert
	 * Will do Design and Implementation by himself, but pass signing contracts through to his manager and is not responsible for any testing
	 */
	@Override
	public void doWork(Work work) throws WorkException {
		switch(work) {
		case SignContract:
			this.getManager().doWork(work);
			break;
		case Test:
			throw new WorkException("I, an Expert, am not responsible for this: " + work.toString(), work);
		default:
			Out.println("I, an Expert, will perfectly execute this job");
			break;
		} 
	}
	
	/**
	 * Overrides the standard implementation in Employee to tell it is a expert
	 */
	@Override
	public String toString() {
		return "Expert: " + getName();
	}
	
}
