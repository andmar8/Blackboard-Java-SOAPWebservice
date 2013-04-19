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
package bbws.gradecentre.grade;

//blackboard
import blackboard.data.gradebook.Score;
import blackboard.data.gradebook.Score.AttemptLocation;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BBScore
{
    public enum BBScoreVerbosity{standard,extended}

    private BBScoreVerbosity verbosity;
    //standard details
    private String dateAdded;
    private String dateChanged;
    private String dateModified;
    private String grade;
    private String outcomeDefBbId;
    private String scoreBbId;
    //extended details
    private String attemptBbId;
    private String attemptLocation;
    private String courseMembershipBbId;
    private String dataType;
    private String lineItemBbId;

    public BBScore(){}
    public BBScore(BBScoreVerbosity verbosity)
    {
        this.verbosity = verbosity;
    }
    public BBScore(Score s,BBScoreVerbosity verbosity) throws Exception
    {
        this.verbosity = verbosity;

        switch(this.verbosity)
        {
            case extended:
                this.attemptBbId = "";
                Object o = s.getAttemptId();
                if(o!=null)
                {
                    if(o.getClass().getName().equalsIgnoreCase("java.lang.String"))
                    {
                     this.attemptBbId = o.toString();
                    }
                    //Need to test
                    /*if(attmptId=="blackboard.persist.Id")
                    {
                    scr[5] = ((Id)o).getExternalString();
                    }*/
                }
                AttemptLocation al = s.getAttemptLocation();
                if(al.equals(AttemptLocation.EXTERNAL))	{this.attemptLocation = "EXTERNAL";}
                else if(al.equals(AttemptLocation.INTERNAL)){this.attemptLocation = "INTERNAL";}
                else{this.attemptLocation = "UNSET";}
                this.courseMembershipBbId = s.getCourseMembershipId().toExternalString();
                this.dataType = s.getDataType().getName();
                this.lineItemBbId = s.getLineitemId().toExternalString();
            case standard:
                this.dateAdded = getDateTimeFromCalendar(s.getDateAdded());
                this.dateChanged = getDateTimeFromCalendar(s.getDateChanged());
                this.dateModified = getDateTimeFromCalendar(s.getModifiedDate());
                this.grade = s.getGrade();
                this.outcomeDefBbId = s.getOutcome().getOutcomeDefinitionId().getExternalString();
                this.scoreBbId = s.getId().getExternalString();
            return;
        }
        throw new Exception("Undefined verbosity of score");
    }

    public String getDateAdded()
    {
	return this.dateAdded;
    }

    public void setDateAdded(String dateAdded)
    {
	this.dateAdded = dateAdded;
    }

    public String getDateChanged()
    {
	return this.dateChanged;
    }

    public void setDateChanged(String dateChanged)
    {
	this.dateChanged = dateChanged;
    }

    public String getDateModified()
    {
	return this.dateModified;
    }

    public void setDateModified(String dateModified) {
	this.dateModified = dateModified;
    }

    public String getGrade()
    {
	return this.grade;
    }

    public void setGrade(String grade)
    {
	this.grade = grade;
    }

    public String getOutcomeDefBbId()
    {
	return this.outcomeDefBbId;
    }

    public void setOutcomeDefBbId(String outcomeDefBbId) {
	this.outcomeDefBbId = outcomeDefBbId;
    }

    public String getScoreBbId()
    {
	return this.scoreBbId;
    }

    public void setScoreBbId(String scoreBbId)
    {
	this.scoreBbId = scoreBbId;
    }

    public String getAttemptBbId()
    {
	return this.attemptBbId;
    }

    public void setAttemptBbId(String attemptBbId)
    {
	this.attemptBbId = attemptBbId;
    }

    public String getAttemptLocation()
    {
	return this.attemptLocation;
    }

    public void setAttemptLocation(String attemptLocation)
    {
	this.attemptLocation = attemptLocation;
    }

    public String getCourseMembershipBbId()
    {
	return this.courseMembershipBbId;
    }

    public void setCourseMembershipBbId(String courseMembershipBbId)
    {
	this.courseMembershipBbId = courseMembershipBbId;
    }

    public String getDataType()
    {
	return this.dataType;
    }

    public void setDataType(String dataType)
    {
	this.dataType = dataType;
    }

    public String getLineItemBbId()
    {
	return this.lineItemBbId;
    }

    public void setLineItemBbId(String lineItemBbId)
    {
	this.lineItemBbId = lineItemBbId;
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
