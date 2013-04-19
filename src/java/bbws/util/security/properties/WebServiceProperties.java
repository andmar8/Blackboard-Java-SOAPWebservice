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

package bbws.util.security.properties;

//blackboard - data
import blackboard.data.registry.SystemRegistryEntry;

//blackboard - persist
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.registry.SystemRegistryEntryDbLoader;

//blackboard - platform
import blackboard.platform.BbServiceManager;

public class WebServiceProperties
{
    private String vendorId;
    private String applicationHandle;

    private WebServiceProperties(){}
    public WebServiceProperties(String vendorId, String applicationHandle)
    {
	this.vendorId = vendorId;
	this.applicationHandle = applicationHandle;
    }

    public String getConfigProperty(String param)
    {
	SystemRegistryEntry sre = new SystemRegistryEntry();

	try
	{
	    //BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
	    //SystemRegistryEntryDbLoader srdbl = (SystemRegistryEntryDbLoader)bbPm.getLoader(SystemRegistryEntryDbLoader.TYPE);
	    sre = ((SystemRegistryEntryDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(SystemRegistryEntryDbLoader.TYPE)).loadByKey(vendorId+"-"+applicationHandle+"-"+param);
	}
	catch(KeyNotFoundException knfe){return "Error: Given parameter not found";}
	catch(Exception e){return e.toString();}

	if(sre.getDescription().contains("amnl"))
	{
	    return sre.getValue();
	}
	else
	{
	    return "Error: Access denied on property";
	}
    }

    public String whichMAGMatchesMAGKey(String mag) throws Exception
    {
        PropertiesBean pb = new PropertiesBean();
        java.util.Enumeration e = pb.getProperties().propertyNames();
        String prop;
        while(e.hasMoreElements())
        {
            prop = (String)e.nextElement();
            //System.err.println("temp:"+temp);
            if(prop.contains(".magkey"))
            {
                //System.err.println("Property containing \".magkey\": "+temp+", attempting to retrieve magKey");
                if(pb.getProperty(prop).equals(mag))
                {
                    //System.err.println("Property's magkey matches given magkey, returning magName: \""+temp.substring(0,temp.indexOf(".magkey"))+"\"");
                    return prop.substring(0,prop.indexOf(".magkey"));
                }
            }
        }
        throw new Exception("MAG Key is null");
    }

    public boolean doesMethodRequireSSL(String mag, String method)
    {
        return getConfigProperty(mag+"."+method+".sslReq").equalsIgnoreCase("Yes")?true:false;
    }

    public boolean isMethodAccessible(String method)
    {
	String isMethodAccessible = getConfigProperty(method+".access");

	if(isMethodAccessible!=null && isMethodAccessible.equalsIgnoreCase("Yes"))
	{
	    return true;
	}
	return false;
    }

    public boolean isMethodAccessible(String mag, String method)
    {
	String isMethodAccessible = getConfigProperty(mag+"."+method+".methodAccess");

	if(isMethodAccessible!=null && isMethodAccessible.equalsIgnoreCase("Yes"))
	{
	    return true;
	}
	return false;
    }

    @Deprecated
    public boolean usingPassword()
    {
	String usingPwd = getConfigProperty("pwd.use");

	if(usingPwd!=null && usingPwd.equalsIgnoreCase("Yes"))
	{
	    return true;
	}
	return false;
    }

    @Deprecated
    public boolean passwordMatches(String pwd)
    {
	String configPwd = getConfigProperty("pwd.pwd");

	if(configPwd!=null && pwd!=null && configPwd.equals(pwd))
	{
	    return true;
	}
	return false;
    }
}
