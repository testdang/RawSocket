package ccs.neu.edu.andang;

import ccs.neu.edu.andang.Util;

public class IPHeader {
	private static final byte[] byteArray = new byte[] { (byte)0x45, (byte)0x00, (byte)0x00, (byte)0x3c,  (byte)0x1c, (byte)0x46, (byte)0x40, (byte)0x00, (byte)0x40, (byte)0x06, (byte)0x00, (byte)0x00, (byte)0xac, (byte)0x10, (byte)0x0a, (byte)0x63, (byte)0xac, (byte)0x10, (byte)0x0a, (byte)0x0c };
	private long checksum;

	
	public long getChecksum() {
		return checksum;
	}


	public void setChecksum(long checksum) {
		this.checksum = checksum;
	}

	public static void main(String args[]){
		IPHeader ipHeader = new IPHeader();
		ipHeader.setChecksum(Util.generateChecksum(byteArray));
	}
	
}
