package employee.app;

import employee.*;

import inout.Out;

public class TestEmployee {

	public static void main(String[] args) {
		Manager wendy = new Manager("Wendy M. Grace", 7000, 5000);
		Manager lee = new Manager("Lee C. Norris", 6000, 10000);

		Expert raquel = new Expert("Raquel A. Swanson", wendy, 3500);
		Expert mary = new Expert("Mary C. Lucas", wendy, 3000);
		Expert yvonne = new Expert("Yvonne S. Hamilton", lee, 4000);
		Expert michael = new Expert("Michael D. Madden", lee, 2000);

		Assistant joe = new Assistant("Joe S. Burder", raquel, 2000);
		Assistant cliff = new Assistant("Cliff T. Smith", mary, 1800);
		Assistant julia = new Assistant("Julia Ginswein", yvonne, 1900);
		Assistant susan = new Assistant("Susan McIllroy", michael, 2100);
						
		Employee[] employees = new Employee[] { wendy, lee, raquel, mary, 
				yvonne, michael, joe, cliff, julia, susan };
		
		Out.println("Current salaries:");
		for (Employee employee : employees) {
			Out.println(employee.toString());
			Out.println("  Salary/month: " + employee.getMonthlySalary());
			Out.println("  Salary/year: " + employee.getAnnualSalary());
		}

		Out.println(); 
		
		for (Employee employee : employees) {
			Out.println(employee.toString() + " working: "); 
			for (Work work : Work.values()) {
				try {
					employee.doWork(work);
				} catch (WorkException excpt) {
					Out.format("Exception: %s %n", excpt.getMessage());
				}
			}
			Out.println(); 
		}

	}

}
