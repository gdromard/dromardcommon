package net.dromard.common.util;

/**
 * Credential helper class.
 * @author Gabriel Dromard
 */
public final class Credential {
	protected String encodedCredential="";

    /** Helper class must not be instanciated. */
    private Credential() {
        // Private Helper constructor
    }

	public Credential(final String credential, final boolean encrypted) {
		if(encrypted) {
			this.encodedCredential=credential;
		} else {
			this.encodedCredential=encode(credential);
		}
	}

	public String getCredential() {
		return this.encodedCredential;
	}

	public String getDecodedCredential() {
		try {
			return decode(this.encodedCredential);
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public boolean equals(final String other) {
		return this.getDecodedCredential().equalsIgnoreCase(other);
	}

	public boolean equals(final Credential other) {
		return this.getCredential().equalsIgnoreCase(other.getCredential());
	}

	public String toString() {
		return getCredential();
	}

	public static void main(final String[] args) {
		try {
			if(args.length==2) {
				if(args[0].equalsIgnoreCase("-decode")) {
					System.out.println(decode(args[1]));
					System.exit(0);
				} else if(args[0].equalsIgnoreCase("-encode")) {
					System.out.println(encode(args[1]));
					System.exit(0);
				}
			}
			System.out.println("Usage: java Credential [-encode/-decode] [password]");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

    // Encode bit stream by groups of 6 (total 64 combinations)
    private static int MASK = 0x3f;
    private static int N_MASK=~MASK;
    private static char[] mCharSet = null;
    private static int[] mAscii; // reversed mCharSet

    static {
        if (mCharSet == null) {
            mAscii = new int[256];
			int i;
			StringBuffer sb=new StringBuffer(64);

			for(i=0; i<='z'-'a'; i++) {
				sb.append((char)(i+'a'));
				sb.append((char)(i+'A'));
			}

			sb.setCharAt(2*('I'-'A')+1, '%');
			sb.setCharAt(2*('l'-'a'), '$');
			sb.setCharAt(2*('O'-'A')+1, 'o');
			sb.setCharAt(2*('O'-'A'), '@');
			sb.append('#');

			for(i=0; i<=9; i++) {
				sb.append((char)(i+'0'));

			}
			sb.append('+');

            mCharSet = new char[64];
            sb.getChars(0, 64, mCharSet, 0);
            mAscii = new int[256];
			for(i=0; i<256; i++) {
                mAscii[i] = -1;
			}
            for (i = 0; i < mCharSet.length; i++) {
                mAscii[mCharSet[i]] = i;
			}
		}
	}

    /**
     * Encode a credential.
     * @param credential The credential that have to be encoded.
     * @return The encoded credential.
     */
    public static String encode(final String credential) {
        if (credential == null) {
            return null;
        }
		byte[] b;
        b = credential.getBytes();
		b=encode(b);
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) mCharSet[b[i]];
        }
		return new String(b);
	}

    /**
     * Encode bytes.
     * @param b An array of byte.
     * @return An encoded array of bytes.
     */
    protected static byte[] encode(final byte[] b) {
		int size=b.length;
		int outSize=(size*8+5)/6;
		byte[] bo=new byte[outSize];
		byte w=0;
		int offs=0, j=0;

		for(int i=0; i<size; i++) {
			if(offs>0) {
				w<<=(byte)(6-offs);
				w=(byte)(w&MASK); // cleanup
			}

			int z=b[i]&0xff;
			z>>=2+offs;
			bo[j++]=(byte)(w|(byte)z);
			offs+=2;

			if(offs==6) {
				bo[j++]=(byte)(b[i]&MASK);
				offs=0;
				w=0;
			} else {
				w=b[i];
			}
		}

		if(offs!=0) {
			w=(byte)(w<<(6-offs));
			w=(byte)(w&MASK); // cleanup
            bo[j] = w;
		}

		return bo;
	}

	protected static String decode(final String credential) throws Exception {
		if (credential == null)
			return null;
		byte[] b = new byte[credential.length()];
		b = credential.getBytes();

		for (int i = 0; i < b.length; i++) {
			int k = mAscii[b[i]];
			if (k < 0) {
				throw new Exception("Invalid data");
			}
			b[i] = (byte) k;
		}
		b = decode(b);
		return new String(b);
	}

	protected static byte[] decode(final byte[] credential) throws Exception {
		int size=credential.length;

		if((size%4)==1) {
			throw new Exception("Invalid Data");
		}

		int inSize=(size*3)/4;
		byte[] bi=new byte[inSize];
		byte w=0;
		int offs=0, j=0;

		for(int i=0; i<size; i++) {
			if((credential[i]&N_MASK)!=0) {
				throw new Exception("Invalid Data");
			}

			if(offs!=0) {
				w=(byte)(w<<offs);
				bi[j++]=(byte)(w|(credential[i]>>>(6-offs)));
			}

			w=credential[i];
			offs=(offs+2)%8;
		}
		return bi;
	}
}