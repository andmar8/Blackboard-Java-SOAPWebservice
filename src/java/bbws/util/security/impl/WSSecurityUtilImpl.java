/*
    Blackboard SOAP WebService
    Copyright (C) 2007-2013 Andrew Martin, Newcastle University

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bbws.util.security.impl;

//bbws
import bbws.util.security.DefaultWSSecurity;
import bbws.util.security.oauth.OAuth;
import bbws.util.security.properties.WebServiceProperties;
import bbws.util.security.WSSecurityUtil;

//com - sun - xml
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.developer.JAXWSProperties;

//java
import java.net.URI;
import java.util.Map;

//javax
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;

public class WSSecurityUtilImpl extends DefaultWSSecurity implements WSSecurityUtil
{
//    private static WebServiceProperties wsp = new WebServiceProperties("amnl","BBWebService");
//    private static long oauthRequestTimeout = 60; //seconds

    private WSSecurityUtilImpl(){}
    public WSSecurityUtilImpl(long timeout,String vendorId,String applicationHandle)
    {
        this.oauthRequestTimeout = timeout;
        this.wsp = new WebServiceProperties(vendorId,applicationHandle);
    }

    /**
     * Ignores the possibility of the magKey being an OAuthSOAP header, i.e.
     * the magKey MUST be a magKey
     * @param wsContext
     * @param magKey
     * @param methodName
     * @throws WebServiceException
     */
    private String authenticateRequest(String magKey, String methodName) throws Exception
    {
        String methodAccessGroup;
        try{methodAccessGroup = wsp.whichMAGMatchesMAGKey(magKey);}
        catch(Exception e)
        {
            System.err.println("Access Denied: "+magKey+"/"+methodName+", no Method Access Group found (MAG key doesn't match anything!)");
            throw new Exception("Access Denied");
        }
        return methodAccessGroup;
    }

//    private static boolean usingSSL(WebServiceContext wsContext)
//    {
//        return ((HttpServletRequest)wsContext.getMessageContext().get(MessageContext.SERVLET_REQUEST)).isSecure();
//    }

    //derive if a user gets access to method above using consumer key and method called above
    //keep mags, but allow user to choose between submitting the key across the wire or using an oauth header? per request!

    public static String getAuthorizationHeader(WebServiceContext wsContext)
    {
        HeaderList hl = (HeaderList)wsContext.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
        java.util.Iterator<Header> i = hl.iterator();
        Header h;
        while(i.hasNext())
        {
            h = i.next();
            if(h.getLocalPart().matches("Authorization"))
            {
                //System.out.println("Authorization: "+h.getStringContent());
                return "Authorization: "+h.getStringContent();
            }
        }
        return "";
    }

    public void authnAndAuthzRequest(URI baseuri, String auth, String methodName, String requestMethod, String resource) throws Exception
    {
        String methodAccessGroup = "";
        if(auth!=null&&!auth.matches("")) //auth is either a magkey or oauth header string
        {
            if(auth.startsWith("Authorization")) //OAuthSOAP header
            {
                auth = auth.substring(15); //Strip "Authorization: " off front of header
                Map<String,String> oAuthParams = ProviderImpl.getOAuthParams(auth);
                //String consumer_key = "";
                String consumer_secret = "";
                try
                {
                    methodAccessGroup = oAuthParams.get(OAuth.consumer_key_url_key);
                    consumer_secret = wsp.getMAGKeyForMAG(methodAccessGroup);
                    //System.out.println("CS: "+consumer_secret);
                }
                catch(Exception e)
                {
                    System.err.println("Access Denied: "+resource);
                    throw new Exception("Access Denied");
                }
                authenticateRequest(new ProviderImpl(oauthRequestTimeout,baseuri.toString(),auth,consumer_secret),requestMethod,methodName);
            }
            else //Authorise using plain text magkey
            {
                methodAccessGroup = authenticateRequest(auth,methodName);
            }
        }
        else
        {
            throw new Exception("UNAUTHORISED");
        }
        authoriseMethodUse(usingSSL(baseuri),methodAccessGroup,methodName);
    }
}
