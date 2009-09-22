/*
 * Created on 21 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.ldap.passwordchecker;

import java.io.IOException;
import java.io.InputStream;

import javax.naming.NamingException;

import net.dromard.common.ldap.LDAP;
import net.dromard.common.util.system.Console;


/**
 * @author Gabriel DROMARD
 *
 * TODO Change this comment
 */
public class PasswordChecker {
    LDAPConnectionParameters parameters;
    
    public PasswordChecker(InputStream properties) throws IOException {
        parameters = new LDAPConnectionParameters(properties);
    }
    
    /**
     * Check a connection.
     * @param user     The user account id
     * @param password The user password
     * @return True if the couple (user, password) is valid.
     */
    public boolean check(String user, String password) {
        boolean correct = false;
        try {
            // Direct check using LDAP connection
            System.out.println("Connecting to "+parameters.getLdapConnectionUrl());
            LDAP ldap = new LDAP(parameters.getLdapConnectionUrl());
            System.out.println("Connecting user "+parameters.getLdapUserDnEntry(user)+"@"+password);
            ldap.connect(parameters.getLdapUserDnEntry(user), password);
            ldap.disconnect();
            correct = true;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return correct;
    }
    
    /**
     * Test entry.
     */
    public static void main(String[] args) throws IOException {
        PasswordChecker checker = new PasswordChecker(PasswordChecker.class.getResourceAsStream("ldap.properties"));
        // Verify imputs
        if (args.length != 2) {
            System.out.println("Usage:   java LDAPUtil <login> <password>");
            System.out.println("Example: java LDAPUtil JSmith SecretPassword");
            String login = Console.ask("LDAP Login:", null);
            String password = Console.ask("LDAP Password:", null);
            System.out.println(checker.check(login, password) ? "The password is correct.":"The password is incorrect.\n");
        } else {
            System.out.println(checker.check(args[0], args[1]) ? "The password is correct.":"The password is incorrect.\n");
        }
    }
}
