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
import blackboard.data.content.Content;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BBContent extends AbstractBBContent
{
    private String body;
    private String contentHandler;
    private Boolean described;
    private String endDate;
    private Boolean folder;
    private Boolean lesson;
    private String modifiedDate;
    private Integer numOfFiles;
    private String offlineName;
    private String offlinePath;
    private String parentContentBbId;
    private String persistentTitle;
    private String renderType;
    private String startDate;
    private String title;
    private String titleColour;

    public BBContent(){}
    public BBContent(Content c)
    {
	this.available = c.getIsAvailable();
	this.body = c.getBody().getText();
	this.contentBbId = c.getId().toExternalString();
	this.contentHandler = c.getContentHandler();
	this.dataType = c.getDataType().getName();
	this.described = c.getIsDescribed();
	this.endDate = getDateTimeFromCalendar(c.getEndDate());
	this.folder = c.getIsFolder();
	this.lesson = c.getIsLesson();
	this.modifiedDate = getDateTimeFromCalendar(c.getModifiedDate());
	this.numOfFiles = c.getContentFiles().size();
	this.offlineName = c.getOfflineName();
	this.offlinePath = c.getOfflinePath();
	this.parentContentBbId = c.getParentId().toExternalString();
	this.persistentTitle = c.getPersistentTitle();
	this.position = c.getPosition();
	this.renderType = c.getRenderType().toFieldName();
	this.startDate = getDateTimeFromCalendar(c.getStartDate());
	this.title = c.getTitle();
	this.titleColour = c.getTitleColor();
	this.url = c.getUrl();
    }

    public String getBody()
    {
	return this.body;
    }

    public void setBody(String body)
    {
	this.body = body;
    }

    public String getContentHandler()
    {
	return this.contentHandler;
    }

    public void setContentHandler(String contentHandler)
    {
	this.contentHandler = contentHandler;
    }

    public Boolean getDescribed()
    {
	return this.described;
    }

    public void setDescribed(Boolean described)
    {
	this.described = described;
    }

    public String getEndDate()
    {
	return this.endDate;
    }

    public void setEndDate(String endDate)
    {
	this.endDate = endDate;
    }

    public Boolean getFolder()
    {
	return this.folder;
    }

    public void setFolder(Boolean folder)
    {
	this.folder = folder;
    }

    public Boolean getLesson()
    {
	return this.lesson;
    }

    public void setLesson(Boolean lesson)
    {
	this.lesson = lesson;
    }

    public String getModifiedDate()
    {
	return this.modifiedDate;
    }

    public void setModifiedDate(String modifiedDate)
    {
	this.modifiedDate = modifiedDate;
    }

    public Integer getNumOfFiles()
    {
	return this.numOfFiles;
    }

    public void setNumOfFiles(Integer numOfFiles)
    {
	this.numOfFiles = numOfFiles;
    }

    public String getOfflineName()
    {
	return this.offlineName;
    }

    public void setOfflineName(String offlineName)
    {
	this.offlineName = offlineName;
    }

    public String getOfflinePath()
    {
	return this.offlinePath;
    }

    public void setOfflinePath(String offlinePath)
    {
	this.offlinePath = offlinePath;
    }

    public String getParentContentBbId()
    {
	return this.parentContentBbId;
    }

    public void setParentContentBbId(String parentContentBbId)
    {
	this.parentContentBbId = parentContentBbId;
    }

    public String getPersistentTitle()
    {
	return this.persistentTitle;
    }

    public void setPersistentTitle(String persistentTitle)
    {
	this.persistentTitle = persistentTitle;
    }

    public String getRenderType()
    {
	return this.renderType;
    }

    public void setRenderType(String renderType)
    {
	this.renderType = renderType;
    }

    public String getStartDate()
    {
	return this.startDate;
    }

    public void setStartDate(String startDate)
    {
	this.startDate = startDate;
    }

    public String getTitle()
    {
	return this.title;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public String getTitleColour()
    {
	return this.titleColour;
    }

    public void setTitleColour(String titleColour)
    {
	this.titleColour = titleColour;
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
