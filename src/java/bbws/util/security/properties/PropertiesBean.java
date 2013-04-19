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
import blackboard.data.registry.Registry;
import blackboard.data.registry.SystemRegistryEntry;

//blackboard - persist
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.registry.SystemRegistryEntryDbLoader;
import blackboard.persist.registry.SystemRegistryEntryDbPersister;

//blackboard - platform
import blackboard.platform.BbServiceManager;

//java
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

//java
import javax.servlet.http.HttpServletRequest;

public class PropertiesBean implements java.io.Serializable
{
	private static PropertiesBean propertiesBean;
	private static String vendorId = "amnl";
	private static String applicationHandle = "BBWebService";
	private Properties p;

	public PropertiesBean()
	{
		super();
	}

	public void setProperties(HttpServletRequest request) throws Exception
	{
	    Enumeration params = request.getParameterNames();
	    SystemRegistryEntry sre = null;
	    String key = "";
	    String value = "";
	    //key not found
	    Boolean knf = false;

	    boolean me = params.hasMoreElements();
	    while(me)
	    {
		sre = new SystemRegistryEntry();
		//System.err.println("Start of while loop");
		knf = false;
	        key = params.nextElement().toString();
		//System.err.println("key "+key);
		value = request.getParameter(key);
		//System.err.println("value "+value);

		try
		{
		    sre = ((SystemRegistryEntryDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(SystemRegistryEntryDbLoader.TYPE)).loadByKey(vendorId+"-"+applicationHandle+"-"+key);
		}
		catch(KeyNotFoundException knfe){knf=true;}
		catch(Exception e){e.printStackTrace();}

		//System.err.println("knf="+knf+":sreValue="+sre.getValue()+":value="+value+"!sre==value="+!sre.getValue().equals(value));

		if(knf || !sre.getValue().equals(value))
		{
		    try
		    {
			sre.setKey(vendorId+"-"+applicationHandle+"-"+key);
			sre.setValue(value);
			sre.setDescription(vendorId+applicationHandle);
                        ((SystemRegistryEntryDbPersister)BbServiceManager.getPersistenceService().getDbPersistenceManager().getPersister(SystemRegistryEntryDbPersister.TYPE)).persist(sre);
			//System.err.println("persisting="+sre.getKey());
		    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
		}
		me = params.hasMoreElements();
		//System.err.println("boolean:"+me);
	    }
	}

	public static PropertiesBean getInstance()
	{
		   if (propertiesBean == null)
                   {
			   propertiesBean = new PropertiesBean();
		   }
		   //System.out.println( "PropertiesManager getInstance() Initialized" );
		   return propertiesBean;
	}

	// Reading property files.  Split into 2 methods.
	public Properties getProperties() throws Exception
	{
	    Properties properties = new Properties();
	    SystemRegistryEntry sre = null;
            try
	    {
		Registry r = ((SystemRegistryEntryDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(SystemRegistryEntryDbLoader.TYPE)).loadRegistry();
		Collection c = r.entries();
		Iterator i = c.iterator();
		while(i.hasNext())
		{
		    Object o = i.next();
		    if(o!=null)
		    {
			sre = (SystemRegistryEntry)o;
			if(sre!=null && sre.getKey().startsWith(vendorId+"-"+applicationHandle+"-"))
			{
			    //System.err.println("key found..."+sre.getKey());
			    properties.setProperty(sre.getKey().substring((vendorId+"-"+applicationHandle+"-").length()),sre.getValue());
			}
		    }
           	}
	    }
	    catch(KeyNotFoundException knfe){}
	    catch(NullPointerException npe){npe.printStackTrace();}
	    catch(Exception e){}//e.printStackTrace();}
	    return properties;
	}

	public String getProperty(String propertyName)
	{
            String _prop = "";
            //System.err.println("Attempting to get property "+propertyName);
            try
            {
                    Properties prop = this.getProperties();
                    if(prop!=null)
                    {
                        _prop = prop.getProperty(propertyName);
                        //System.err.println("getting property: "+propertyName+" returned="+_prop);
                    }
                    else
                    {
                        _prop = "";
                    }
            }
            catch (Exception e)
            {
                    //System.err.println(propertyName+" getProperty catch statement");
                    e.printStackTrace();
                    _prop = "";
            }
	    return _prop;
	}

	public void setProperty(String key, String value) throws Exception
	{
            this.p.setProperty(key, value);
	}
}
