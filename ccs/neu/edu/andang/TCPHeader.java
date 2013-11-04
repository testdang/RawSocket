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
	
	// calculating checksum on this byte array {assuming pseudo header + TCP header + TCP data}
	byte [] ipPacket = { (byte)0xFF, (byte)0xFF, 46, (byte)0xFF, (byte)0xFF, 45, 00, 00, (byte)0x3c, (byte)0x1c, 46, 40, 00, 40, 06, (byte)0xb1, (byte)0xe6 };
	
	// TODO: Parse a TCP Header for an incoming TCP packet
	public TCPHeader(){
		
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
		this.generateChecksum(ipPacket);
	}

	private void generateChecksum(byte[] byteArray) {
		int checksum = 0; 
		byteArray = new byte[] { (byte)0xFF, (byte)0xFF, 46, (byte)0xFF, (byte)0xFF, 45, 00, 00, (byte)0x3c, (byte)0x1c, 46, 40, 00, 40, 06, (byte)0xb1, (byte)0xe6 };
		
		// if the byte array has odd number of octets, padding a zero byte
		byte[] stream ;
		if (byteArray.length % 2 != 0) {
			stream = new byte[byteArray.length+1];
			for (int i=0; i< byteArray.length; i++) {
				stream[i] = byteArray[i];
			}
			stream[byteArray.length] = 0;
			System.out.println(stream.length);
		} else {
			stream = new byte[byteArray.length];
			for (int i=0; i< byteArray.length; i++)
				stream[i] = byteArray[i];
		}		
		

		// adjacent 8 bit words are stored as a short, 
		// sum up the 16 bit shorts and compute 1's complement for checksum
		for (int c=0; c < stream.length; c=c+2 ) {
			int firstByte = Byte.valueOf(stream[c]).intValue();
			
			// to convert it to unsigned value
			firstByte = firstByte&255;
			int shifted = (firstByte<<8);
			System.out.println("The shifted-->"+shifted +" "+Integer.toHexString(shifted));
			int nextbyte = stream[c+1]&255;
			System.out.println("The next byte-->"+Integer.toHexString(nextbyte));
			int twoBytesGrouping = (shifted + (stream[c+1]&255));
			System.out.println("The exor result-->"+Integer.toHexString(twoBytesGrouping));
			checksum = checksum + twoBytesGrouping;
			
			//adding the 17th odd bit to the checksum to keep it 16 bit word
			if (checksum > 65535)
				checksum = checksum - 65535 + 1;		
			System.out.println("The sum "+checksum);
		}
		System.out.println("The sum in hex--> "+Long.toHexString(checksum));
		//taking one's complement of the result
		System.out.println("The Checksum after one's complement-->"+Integer.toHexString(~checksum&0xFFFF));

		
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
	
	private void print() {
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
