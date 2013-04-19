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

package bbws.content;

//blackboard - data
import blackboard.data.content.ContentFile;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BBContentFile
{
    private String action;
    private String contentFileBbId;
    private String dataType;
    private String linkName;
    private String modifiedDate;
    private String name;
    private Long size;
    private String storageType;

    public BBContentFile(){}
    public BBContentFile(ContentFile cf)
    {
	this.action = cf.getAction().toFieldName();
	this.contentFileBbId = cf.getId().toExternalString();
	this.dataType = cf.getDataType().getName();
	this.linkName = cf.getLinkName();
	this.modifiedDate = getDateTimeFromCalendar(cf.getModifiedDate());
	this.name = cf.getName();
	this.size = cf.getSize();
	this.storageType = cf.getStorageType().toFieldName();
    }

    public String getAction()
    {
	return this.action;
    }

    public void setAction(String action)
    {
	this.action = action;
    }

    public String getContentFileBbId()
    {
	return this.contentFileBbId;
    }

    public void setContentFileBbId(String contentFileBbId)
    {
	this.contentFileBbId = contentFileBbId;
    }

    public String getDataType()
    {
	return this.dataType;
    }

    public void setDataType(String dataType)
    {
	this.dataType = dataType;
    }

    public String getLinkName()
    {
	return this.linkName;
    }

    public void setLinkName(String linkName)
    {
	this.linkName = linkName;
    }

    public String getModifiedDate()
    {
	return this.modifiedDate;
    }

    public void setModifiedDate(String modifiedDate)
    {
	this.modifiedDate = modifiedDate;
    }

    public String getName()
    {
	return this.name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public Long getSize()
    {
	return this.size;
    }

    public void setSize(Long size)
    {
	this.size = size;
    }

    public String getStorageType()
    {
	return this.storageType;
    }

    public void setStorageType(String storageType)
    {
	this.storageType = storageType;
    }

    private String getDateTimeFromCalendar(Calendar c)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(c.getTime());
        }
        catch(Exception e)
        {
            return "";
        }
    }
}
