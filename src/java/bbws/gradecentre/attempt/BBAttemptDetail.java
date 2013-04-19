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

//blackboard - platform
import blackboard.platform.gradebook2.AttemptDetail;

//java - util
import java.util.Calendar;

public class BBAttemptDetail extends AbstractAttempt
{
    private String displayGrade;
    private Boolean exempt;
    private String feedBackToUser;
    private Boolean feedBackToUserHidden;
    private String formattedAttemptedDate;
    private String formattedDateCreated;
    private String id;
    private String gradeId;
    private String shortFeedBackToUser;
    private Double score;
    private String statusKey;

    public BBAttemptDetail(){}
    public BBAttemptDetail(BBAttemptVerbosity verbosity)
    {
        this.verbosity = verbosity;
    }
    public BBAttemptDetail(AttemptDetail a,BBAttemptVerbosity verbosity) throws Exception
    {
        this.verbosity = verbosity;

        switch(this.verbosity)
        {
            case extended:
                try{this.attemptedDate = getDateTimeFromCalendar((Calendar)a.getClass().getDeclaredMethod("getAttemptDate",new Class[]{}).invoke(a, new Object[]{}));}//Version 9.1 call
                catch(Exception e){this.attemptedDate = getDateTimeFromCalendar(a.get_attemptDate());}//User version 8 call
                this.displayGrade = a.getDisplayGrade();
                this.formattedAttemptedDate = a.getFormattedAttemptDate();
                this.formattedDateCreated = a.getFormattedCreateDate();
                this.publicComments = a.isPublicFeedbackToUser();
                this.shortFeedBackToUser = a.getShortFeedbackToUser();
                this.statusKey = a.getStatusKey();
            case standard:
                this.attemptBbId = a.getId().getExternalString();
                this.id = this.attemptBbId;
                this.dateCreated = getDateTimeFromCalendar(a.getCreationDate());
                this.exempt = a.isExempt();
                this.feedBackToUser = a.getFeedBackToUser();
                this.feedBackToUserHidden = a.isFeedbackToUserHidden();
                this.grade = a.getGrade();
                this.gradeId = a.getGradeId().getExternalString();
                this.instructorNotes = a.getInstructorNotes();
                this.score = a.getScore();
                this.status = a.getStatus().name();
            return;
        }
        throw new Exception("Undefined verbosity of AttemptDetail");
    }

    public String getDisplayGrade()
    {
	return this.displayGrade;
    }

    public void setDisplayGrade(String displayGrade)
    {
	this.displayGrade = displayGrade;
    }

    public Boolean getExempt()
    {
	return this.exempt;
    }

    public void setExempt(Boolean exempt)
    {
	this.exempt = exempt;
    }

    public String getFeedBackToUser()
    {
	return this.feedBackToUser;
    }

    public void setFeedBackToUser(String feedBackToUser)
    {
	this.feedBackToUser = feedBackToUser;
    }

    public Boolean getFeedBackToUserHidden()
    {
	return this.feedBackToUserHidden;
    }

    public void setFeedBackToUserHidden(Boolean feedBackToUserHidden)
    {
	this.feedBackToUserHidden = feedBackToUserHidden;
    }

    public String getFormattedAttemptedDate()
    {
	return this.formattedAttemptedDate;
    }

    public void setFormattedAttemptedDate(String formattedAttemptedDate)
    {
	this.formattedAttemptedDate = formattedAttemptedDate;
    }

    public String getFormattedDateCreated()
    {
	return this.formattedDateCreated;
    }

    public void setFormattedDateCreated(String formattedDateCreated)
    {
	this.formattedDateCreated = formattedDateCreated;
    }

    public String getGradeId()
    {
	return this.gradeId;
    }

    public void setGradeId(String gradeId)
    {
	this.gradeId = gradeId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
        this.attemptBbId = id;
    }

    public Double getScore()
    {
	return this.score;
    }

    public void setScore(Double score)
    {
	this.score = score;
    }

    public String getShortFeedBackToUser()
    {
	return this.shortFeedBackToUser;
    }

    public void setShortFeedBackToUser(String shortFeedBackToUser)
    {
	this.shortFeedBackToUser = shortFeedBackToUser;
    }

    public String getStatusKey()
    {
	return this.statusKey;
    }

    public void setStatusKey(String statusKey)
    {
	this.statusKey = statusKey;
    }
}
