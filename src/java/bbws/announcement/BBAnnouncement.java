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

package bbws.announcement;

//blackboard - data
import blackboard.data.announcement.Announcement;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BBAnnouncement
{
    private String announcementBbId;
    private String body;
    private String endDate;
    private Boolean permanent;
    private String startDate;
    private String title;
    private String type;

    public BBAnnouncement(){}
    public BBAnnouncement(Announcement a)
    {
	this.announcementBbId = a.getId().toExternalString();
	this.body = a.getBody().getText();
	this.endDate = getDateTimeFromCalendar(a.getRestrictionEndDate());
	this.permanent = a.getIsPermanent();
	this.startDate = getDateTimeFromCalendar(a.getRestrictionStartDate());
	this.title = a.getTitle();
	this.type = a.getType().toFieldName();
    }

    public String getAnnouncementBbId()
    {
	return this.announcementBbId;
    }

    public void setAnnouncementBbId(String announcementBbId)
    {
	this.announcementBbId = announcementBbId;
    }

    public String getBody()
    {
	return this.body;
    }

    public void setBody(String body)
    {
	this.body = body;
    }

    public String getEndDate()
    {
	return this.endDate;
    }

    public void setEndDate(String endDate)
    {
	this.endDate = endDate;
    }

    public Boolean getPermanent()
    {
	return this.permanent;
    }

    public void setPermanent(Boolean permanent)
    {
	this.permanent = permanent;
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
	return title;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public String getType()
    {
	return this.type;
    }

    public void setType(String type)
    {
	this.type = type;
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
