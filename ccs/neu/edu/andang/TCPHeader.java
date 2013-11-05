package ccs.neu.edu.andang ;

import java.nio.ByteBuffer ;
import java.util.Arrays ;
// TODO
// TCPHeader: represent the header of a TCP packet
// it also allow packets with optional fields
public class TCPHeader{

	private final int BASE_HEADER_SIZE = 20 ;

	int source_port;
	int destination_port;
	long seq_num;
	long ack_num;
	byte data_offset;
	byte flags;
	int win_size;
	int checksum;
	int urg_point;
	
	// TODO: Parse a TCP Header for an incoming TCP packet
	public TCPHeader(byte[] header){
		if (header.length == 20) {
			source_port = (int)(((header[0]<<8)&65280)|(header[1]&255));
			destination_port = (int)(((header[2]<<8)&65280)|(header[3]&255));
			seq_num = (long)(((header[4]<<24)&4278190080l)|((header[5]<<16)&16711680l)|((header[6]<<8)&65280)|(header[7]&255));
			ack_num = (long)(((header[8]<<24)&4278190080l)|((header[9]<<16)&16711680l)|((header[10]<<8)&65280)|(header[11]&255));
			data_offset = (byte)((header[12]>>4)&15);
			flags = (byte)(header[13]&63);
			win_size = (int)(((header[14]<<8)&65280)|(header[15]&255));
			checksum = (int)(((header[16]<<8)&65280)|(header[17]&255));
			urg_point = (int)(((header[18]<<8)&65280)|(header[19]&255));
		}
	}

	// Create a TCP Header for an outgoing TCP packet
	public TCPHeader(int source_port, int destination_port, long seq_num, long ack_num, byte flags, int win_size) {
		this.source_port = source_port;
		this.destination_port = destination_port;
		this.seq_num = seq_num;
		this.ack_num = ack_num;
		this.data_offset = 5;
		this.flags = flags;
		this.win_size = win_size;
		this.checksum = 0;
		this.urg_point = 0;
	}

	// Generate the TCP Header in a byte array format
	public byte[] getHeader() {
		byte[] header = new byte[BASE_HEADER_SIZE];
		header[0] = (byte)((source_port>>8)&255);
		header[1] = (byte)(source_port&255);
		header[2] = (byte)((destination_port>>8)&255);
		header[3] = (byte)(destination_port&255);
		header[4] = (byte)((seq_num>>24)&255);
		header[5] = (byte)((seq_num>>16)&255);
		header[6] = (byte)((seq_num>>8)&255);
		header[7] = (byte)(seq_num&255);
		header[8] = (byte)((ack_num>>24)&255);
		header[9] = (byte)((ack_num>>16)&255);
		header[10] = (byte)((ack_num>>8)&255);
		header[11] = (byte)(ack_num&255);
		header[12] = (byte)((data_offset&15)<<4);
		header[13] = (byte)(flags&63);
		header[14] = (byte)((win_size>>8)&255);
		header[15] = (byte)(win_size&255);
		header[16] = (byte)((checksum>>8)&255);
		header[17] = (byte)(checksum&255);
		header[18] = (byte)((urg_point>>8)&255);
		header[19] = (byte)(urg_point&255);
		return header;
	}

	public void setCheckSum(int checksum){this.checksum = checksum;}

	public int getSourcePort(){return this.source_port;}

	public int getDestinationPort(){return this.destination_port;}

	public long getSequenceNumber(){return seq_num;}

	public long getACKNumber(){return ack_num;}

	// return the number of words in the TCP header 
	// including any 'options' fields.
	public byte getHeaderLength(){return data_offset;}

	public int getWindowSize(){return win_size;}

	public int getChecksum(){return checksum;}

	public int getUrgentPointer(){return urg_point;}

	public boolean isURGFlagOn(){return (boolean)((flags&32) == 32);}

	public boolean isACKFlagOn(){return (boolean)((flags&16) == 16);}

	public boolean isPSHFlagOn(){return (boolean)((flags&8) == 8);}

	public boolean isRSTFlagOn(){return (boolean)((flags&4) == 4);}	

	public boolean isSYNFlagOn(){return (boolean)((flags&2) == 2);}

	public boolean isFINFlagOn(){return (boolean)((flags&1) == 1);}
	
	public void print() {
		byte[] head = getHeader();
		for (int j=0; j<head.length; j++) {
			System.out.format("%02X ", head[j]);
		}
		System.out.println();
	}
	
	public static void main(String args[]){
		TCPHeader test = new TCPHeader(32769, 32768, 3758096384l, 3758096385l, (byte)18, 32769);
		test.print();
		System.out.println(test.getSourcePort());
	}
}
