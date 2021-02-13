package common.TrafficControlDetect;

/**
 * Streetnames of the City Project
 * @author Christopher Holzweber, repair time by Sternbauer
 */
public enum StreetName {
	Street0 (5000),
	Street1 (7000),
	Street2 (3000),
	Street3 (8000),
	Street4 (2000),
	Street5 (3000),
	Street6 (9000),
	Street7 (4500),
	Street8 (3750),
	Street9 (2050);

	/**
	 * Public property to tell the maintenance subsystem how long it will take to repair an accident in a specified street
	 */
	public final int repairTime;

	/**
	 * Private constructor of this enum which sets the needed time for repairs in this street
	 * @param repairTime
	 */
	StreetName(int repairTime) {
		this.repairTime = repairTime;
	}
}
