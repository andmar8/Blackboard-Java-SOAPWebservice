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

package bbws.gradecentre.attempt;

//bbws
import bbws.gradecentre.enums.verbosity.BBAttemptVerbosity;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class AbstractAttempt
{
    protected BBAttemptVerbosity verbosity;
    //standard details
    protected String attemptBbId;
    protected String attemptedDate;
    protected String dateCreated;
    protected String grade;
    protected String status;
    //extended details
    protected String instructorNotes;
    protected Boolean publicComments;
    protected String studentComments;

    public String getAttemptBbId()
    {
	return this.attemptBbId;
    }

    public void setAttemptBbId(String attemptBbId)
    {
	this.attemptBbId = attemptBbId;
    }

    public String getAttemptedDate()
    {
	return this.attemptedDate;
    }

    public void setAttemptedDate(String attemptedDate)
    {
	this.attemptedDate = attemptedDate;
    }

    public String getGrade()
    {
	return this.grade;
    }

    public void setGrade(String grade)
    {
	this.grade = grade;
    }

    public String getStatus()
    {
	return this.status;
    }

    public void setStatus(String status)
    {
	this.status = status;
    }

    public String getDateCreated()
    {
	return this.dateCreated;
    }

    public void setDateCreated(String dateCreated)
    {
	this.dateCreated = dateCreated;
    }

    public String getInstructorNotes()
    {
	return this.instructorNotes;
    }

    public void setInstructorNotes(String instructorNotes)
    {
	this.instructorNotes = instructorNotes;
    }

    public Boolean getPublicComments()
    {
	return this.publicComments;
    }

    public void setPublicComments(Boolean publicComments)
    {
	this.publicComments = publicComments;
    }

    public String getStudentComments()
    {
	return this.studentComments;
    }

    public void setStudentComments(String studentComments)
    {
	this.studentComments = studentComments;
    }

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
}
