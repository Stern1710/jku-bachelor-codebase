package network;

public enum LinkType {
	LANDESSTRASSE(50), BUNDESSTRASSE(100), AUTOBAHN(130);

	private final int velocity;

	LinkType (int velocity) {
		this.velocity = velocity;
	}

	public int getVelocity() {
		return velocity;
	}
}
