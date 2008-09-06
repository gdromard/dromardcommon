/*
 * Created on 21 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.ldap.passwordchecker.novel;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import net.dromard.common.util.system.Console;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;

/**
 * This interface is a connection to a LDAP directory
 * All the implementation will be some connection to a specific directory
 * 
 * @author Gabriel DROMARD
 */
public abstract class LDAPDirectory extends LDAPConnection {
    public void connect() throws LDAPException, UnsupportedEncodingException {
        connect(getUserDn(), getUserPassword().getDecodedCredential().getBytes("UTF8"));
    }
    
    public void connect(String login, byte[] passwd) throws LDAPException {
        //System.out.println("Connecting to ldap://"+getHost()+":"+getPort());
        super.connect(getHost(), getPort());
        //System.out.println("Binding with "+login+" - "+passwd);
        super.bind(LDAPConnection.LDAP_V3, login, passwd);
    }
    
    public void disconnect() throws LDAPException {
        if(super.isConnected()) super.disconnect();
    }
    
    /**
     * @return the LDAP Directory name
     */
    public abstract String getName();

    /**
     * @return the LDAP host name
     */
    public abstract String getHost();

    /**
     * @return the LDAP port number
     */
    public abstract int    getPort();

    /**
     * @return the LDAP user's dn
     */
    public abstract String getUserDn();

    /**
     * @return the LDAP user's password
     */
    public abstract Credential getUserPassword();
    
    /**
     * This is needed to generate the filter to find a user.
     * (It can be sn, uid, id, user ...)
     * @return the LDAP user entry used
     */
    public abstract String getSearchBaseDn();
    
    /**
     * This is needed to generate the filter to find a user.
     * (It can be sn, uid, id, user ...)
     * @return the LDAP user entry used
     */
    public abstract String getUserEntry();
    
    public String getUserDn(String login) throws LDAPException {
        String dn = null;
        String filter = "("+getUserEntry()+"="+login+")";
        // Search
        LDAPSearchResults results = search(getSearchBaseDn(), LDAPConnection.SCOPE_SUB, filter, new String[] {"userPassword"}, false);
        // Verify number of results
        if(results.getCount() > 0) {
            // for each result check if password is correct, return true at the first rigth match.
            while(results.hasMore()) {
                dn = results.next().getDN();
                return dn;
            }
        } else {
            System.err.println("Login "+login+" was not found ! [searchBase: "+getSearchBaseDn()+", filter: ("+getUserEntry()+"="+login+") ]");
        }
        return null;
    }
    
    /**
     * This function verify a user password in the LDAP direcoty
     * @param dn		User's LDAP DN
     * @param password	User's password
     * @return True if the given password match with the given DN
     * @throws LDAPException
     * @throws UnsupportedEncodingException
     */
    public boolean verifyPassword(String dn, String password) throws LDAPException, UnsupportedEncodingException {
        LDAPAttribute pass = new LDAPAttribute("userPassword", password.getBytes("UTF8"));
        return compare(dn, pass);
    }
/*
#ldap.cimpa.user.password=MoedPl7_eC
#ldap.AEDS.internal.user.password=LLDllm2kBTJmmeP
#ldap.active_directory.user.password=echo0405
#ldap.aic.user.password=ZypoABWrUNQ64Wy
*/
    
    /**
     * Get the LDAP entry of a given DN 
     * @param dn The DN to get
     * @return The entry corresponding to the DN.
     * @throws LDAPException
     */
    public LDAPEntry getEntry(String dn) throws LDAPException {
        return read(dn);
    }
    
    /**
     * Utils class for printing the attributes of a DN
     * @param dn The DN to print
     * @throws LDAPException
     * @see LDAPDirectory#printEntry(LDAPEntry)
     */
    public void printEntry(String dn) throws LDAPException {
        printEntry(getEntry(dn));
    }
    
    /**
     * Utils class for printing the attributes of a DN
     * @param entry The LDAPEntry to print.
     */
    public static void printEntry(LDAPEntry entry) {
        System.out.println("+---------------------------------------");
        System.out.println("| DN : "+entry.getDN());
        System.out.println("+---------------------------------------");
        for(Iterator it = entry.getAttributeSet().iterator(); it.hasNext(); ) {
            LDAPAttribute att = (LDAPAttribute)it.next();
            System.out.println(att.getName() + " -> "+ att.getStringValue());
        }
    }
    
    /**
     * This main class allow you to generate a encoded password 
     * design to be filled in a property file.
     * 
     * @param args The password to encode.
     */
	public static void main(String[] args) {
	    //args = new String[] {"test"};
		try {
			if(args.length==1) System.out.println(new Credential(args[0], false));
            else System.out.println(new Credential(Console.ask("Password to encode:", null), false));
			//System.out.println("Usage: java LDAPDirectory [password to encode]");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);
	}
}

/**
 * This class is design to be able to manage password set in a property file.
 * Has a property file is vary easy to read, the idea is to encode the password in the file.
 * This is the main objective of this class.
 * 
 * @author Gabriel DROMARD
 */
class Credential {
	protected String encodedCredential;
	
	public Credential(String credential, boolean encrypted) {
		if(encrypted) this.encodedCredential = credential;
		else this.encodedCredential = encode(credential);
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

	public boolean equals(String other) {
		return this.getDecodedCredential().equalsIgnoreCase(other);
	}

	public boolean equals(Credential other) {
		return this.getCredential().equalsIgnoreCase(other.getCredential());
	}

	public String toString() {
		return getCredential();
	}

	/************************ Encrypting *****************************/
	// encode/decode bit stream by groups of 6 (total 64 combinations)
	private static final int MASK=0x3f;
	private static final int N_MASK=~MASK;

	private static char[] m_charSet=null;
	private static int[] m_Ascii=new int[256]; // reversed m_charSet

	static {
		if(m_charSet==null) {
			int i;
			//int n=0x40; // initial char
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

			m_charSet=new char[64];
			sb.getChars(0, 64, m_charSet, 0);
			m_Ascii=new int[256];
			for(i=0; i<256; i++) {
				m_Ascii[i]=-1;
			}
			for(i=0; i<m_charSet.length; i++) {
				m_Ascii[m_charSet[i]]=i;
			}
		}
	}

	private static final String encode(String s) {
		if(s==null) return null;
		byte[] b;

		b=s.getBytes();
		b=encode(b);
		for(int i=0; i<b.length; i++) b[i]=(byte)m_charSet[b[i]];

		return new String(b);
	}

	private static final byte[] encode(byte[] b) {
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
			bo[j]=w;
		}

		return bo;
	}

	private static final String decode(String credential) throws Exception {
		if(credential==null) return null;
		byte[] b=new byte[credential.length()];
		b=credential.getBytes();

		for(int i=0; i<b.length; i++) {
			int k=m_Ascii[b[i]];
			if(k<0) throw new Exception("Invalid data");
			b[i]=(byte)k;
		}
		b=decode(b);
		return new String(b);
	}

	private static final byte[] decode(byte[] credential) throws Exception {
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