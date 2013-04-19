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

package bbws.course;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class AbstractBBCourse
{
    protected String courseId;
    protected String creationDate;
    protected Boolean available;

    protected String getDateTimeFromCalendar(Calendar c)
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

    public String getCourseId()
    {
	return this.courseId;
    }

    public void setCourseId(String courseId)
    {
	this.courseId = courseId;
    }

    public String getCreationDate()
    {
	return this.creationDate;
    }

    public void setCreationDate(String creationDate)
    {
	this.creationDate = creationDate;
    }

    public Boolean getAvailable()
    {
	return this.available;
    }

    public void setAvailable(Boolean available)
    {
	this.available = available;
    }
}
