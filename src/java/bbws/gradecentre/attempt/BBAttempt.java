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

//blackboard - data
import blackboard.data.gradebook.impl.Attempt;

public class BBAttempt extends AbstractAttempt
{
    //standard
    private String dateModified;
    private String outcomeBbId;
    private Float score;
    //extended
    private String instructorComments;
    private String linkRefBbId;
    private String resultObjectBbId;

    public BBAttempt(){}
    public BBAttempt(BBAttemptVerbosity verbosity)
    {
        this.verbosity = verbosity;
    }
    public BBAttempt(Attempt a,BBAttemptVerbosity verbosity) throws Exception
    {
        this.verbosity = verbosity;

        switch(this.verbosity)
        {
            case extended:
                //this.dataType = a.getDataType().getName();
                this.instructorComments = a.getInstructorComments();
                this.instructorNotes = a.getInstructorNotes();
                this.linkRefBbId = a.getLinkRefId();
                this.publicComments = a.getCommentIsPublic();
                try{this.resultObjectBbId = a.getResultObjectId().getExternalString();}catch(Exception e){this.resultObjectBbId = "";}
                this.studentComments = a.getStudentComments();
            case standard:
                this.attemptBbId = a.getId().getExternalString();
                this.attemptedDate = getDateTimeFromCalendar(a.getAttemptedDate());
                this.dateCreated = getDateTimeFromCalendar(a.getDateCreated());
                this.dateModified = getDateTimeFromCalendar(a.getDateModified());
                this.grade = a.getGrade();
                this.outcomeBbId = a.getOutcomeId().getExternalString();
                this.score = a.getScore();
                this.status = a.getStatus().getDisplayName();
            return;
        }
        throw new Exception("Undefined verbosity of Attempt");
    }

    public String getDateModified()
    {
	return this.dateModified;
    }

    public void setDateModified(String dateModified)
    {
	this.dateModified = dateModified;
    }

    public String getInstructorComments()
    {
	return this.instructorComments;
    }

    public void setInstructorComments(String instructorComments)
    {
	this.instructorComments = instructorComments;
    }

    public String getLinkRefBbId()
    {
	return this.linkRefBbId;
    }

    public void setLinkRefBbId(String linkRefBbId)
    {
	this.linkRefBbId = linkRefBbId;
    }

    public String getOutcomeBbId()
    {
	return this.outcomeBbId;
    }

    public void setOutcomeBbId(String outcomeBbId)
    {
	this.outcomeBbId = outcomeBbId;
    }

    public String getResultObjectBbId()
    {
	return this.resultObjectBbId;
    }

    public void setResultObjectBbId(String resultObjectBbId)
    {
	this.resultObjectBbId = resultObjectBbId;
    }

    public Float getScore()
    {
	return this.score;
    }

    public void setScore(Float score)
    {
	this.score = score;
    }
}
