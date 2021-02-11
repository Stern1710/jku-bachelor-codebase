package employee;

public class WorkException extends Exception {
	
	private static final long serialVersionUID = -4431381387510816350L;

	private final Work work;

	public WorkException(String message, Work work) {
		super(message);
		this.work = work; 
	}

	public Work getWork() {
		return work;
	} 
	
}
