package org.pac4j.http.credentials.authenticator;

import org.junit.Test;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.util.TestsHelper;
import org.pac4j.http.profile.IpProfile;

import static org.junit.Assert.*;

/**
 * This class tests the {@link IpRegexpAuthenticator}.
 * 
 * @author Jerome Leleu
 * @since 1.8.0
 */
public final class IpRegexpAuthenticatorTests implements TestsConstants {

    private final static String GOOD_IP = "goodIp";
    private final static String BAD_IP = "badIp";

    private final static IpRegexpAuthenticator authenticator = new IpRegexpAuthenticator(GOOD_IP);

    @Test(expected = TechnicalException.class)
    public void testNoPattern() throws HttpAction {
        IpRegexpAuthenticator authenticator = new IpRegexpAuthenticator();
        authenticator.validate(null, null);
    }

    @Test
    public void testValidateGoodIP() throws HttpAction {
        final TokenCredentials credentials = new TokenCredentials(GOOD_IP, CLIENT_NAME);
        authenticator.validate(credentials, null);
        final IpProfile profile = (IpProfile) credentials.getUserProfile();
        assertEquals(GOOD_IP, profile.getId());
    }

    @Test
    public void testValidateBadIP() throws HttpAction {
        final TokenCredentials credentials = new TokenCredentials(BAD_IP, CLIENT_NAME);
        TestsHelper.expectException(() -> authenticator.validate(credentials, null), CredentialsException.class, "Unauthorized IP address: " + BAD_IP);
    }
}
