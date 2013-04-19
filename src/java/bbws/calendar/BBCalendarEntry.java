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

package bbws.calendar;

//blackboard - data
import blackboard.data.calendar.CalendarEntry;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BBCalendarEntry
{
    public enum BBCalendarEntryType{COURSE,INSTITUTION,PERSONAL}

    private String calendarEntryBbId;
    private String courseBbId;
    private String description;
    private String endDateTime;
    private String externalType;
    private String startDateTime;
    private String title;
    private String userBbId;

    public BBCalendarEntry(){}
    public BBCalendarEntry(CalendarEntry ce)
    {
	this.calendarEntryBbId = ce.getId().toExternalString();
        this.courseBbId = ce.getCourseId().toExternalString();
	this.description = ce.getDescription().getText();
	this.endDateTime = getDateTimeFromCalendar(ce.getEndDate());
	this.externalType = ce.getType().toFieldName();
	this.startDateTime = getDateTimeFromCalendar(ce.getStartDate());
        this.title = ce.getTitle();
        this.userBbId = ce.getUserId().toExternalString();
    }

    public String getCalendarEntryBbId()
    {
	return this.calendarEntryBbId;
    }

    public void setCalendarEntryBbId(String calendarEntryBbId)
    {
	this.calendarEntryBbId = calendarEntryBbId;
    }

    public String getCourseBbId()
    {
	return this.courseBbId;
    }

    public void setCourseBbId(String courseBbId)
    {
	this.courseBbId = courseBbId;
    }

    public String getDescription()
    {
	return this.description;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

    public String getEndDateTime()
    {
	return this.endDateTime;
    }

    public void setEndDateTime(String endDateTime)
    {
	this.endDateTime = endDateTime;
    }

    public String getExternalType()
    {
	return this.externalType;
    }

    public void setExternalType(String externalType)
    {
	this.externalType = externalType;
    }

    public String getStartDateTime()
    {
	return this.startDateTime;
    }

    public void setStartDateTime(String startDateTime)
    {
	this.startDateTime = startDateTime;
    }

    public String getUserBbId()
    {
	return this.userBbId;
    }

    public void setUserBbId(String userBbId)
    {
	this.userBbId = userBbId;
    }

    public String getTitle()
    {
	return this.title;
    }

    public void setTitle(String title)
    {
	this.title = title;
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
