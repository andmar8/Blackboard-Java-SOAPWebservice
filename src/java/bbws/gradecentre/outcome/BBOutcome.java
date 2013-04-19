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

package bbws.gradecentre.outcome;

//blackboard
import blackboard.data.gradebook.impl.Outcome;

public class BBOutcome
{
    private String averageGrade;
    private Float averageScore;
    private String courseMembershipBbId;
    private String firstAttemptBbId;
    private String grade;
    private String gradebookStatus;
    private String highestAttemptBbId;
    private String instructorComments;
    private String lastAttemptBbId;
    private String lowestAttemptBbId;
    private String manualGrade;
    private Float manualScore;
    private String outcomeBbId;
    private String outcomeDefinitionBbId;
    private Float score;
    private String studentComments;
    private Float totalScore;

    public BBOutcome(){}
    public BBOutcome(Outcome o)
    {
        this.averageGrade = o.getAverageGrade(false);
        try{this.averageScore = o.getAverageScore();}catch(Exception e){this.averageScore = new Float(0);}
        this.courseMembershipBbId = o.getCourseMembershipId().toExternalString();
        try{this.firstAttemptBbId = o.getFirstAttemptId().toExternalString();}catch(Exception e){this.firstAttemptBbId = "";}
        this.grade = o.getGrade();
        this.gradebookStatus = o.getGradebookStatus().getDisplayName();
        try{this.highestAttemptBbId = o.getHighestAttemptId().toExternalString();}catch(Exception e){this.highestAttemptBbId = "";}
        this.instructorComments = o.getInstructorComments();
        try{this.lastAttemptBbId = o.getLastAttemptId().toExternalString();}catch(Exception e){this.lastAttemptBbId = "";}
        try{this.lowestAttemptBbId = o.getLowestAttemptId().toExternalString();}catch(Exception e){this.lowestAttemptBbId = "";}
        this.manualGrade = o.getManualGrade();
        try{this.manualScore = o.getManualScore();}catch(Exception e){this.manualScore = new Float(0);}
        this.outcomeBbId = o.getId().toExternalString();
        this.outcomeDefinitionBbId = o.getOutcomeDefinitionId().toExternalString();
        try{this.score = o.getScore();}catch(Exception e){this.score = new Float(0);}
        this.studentComments = o.getStudentComments();
        try{this.totalScore = o.totalScore();}catch(Exception e){this.totalScore = new Float(0);}
    }

    public String getAverageGrade()
    {
	return this.averageGrade;
    }

    public void setAverageGrade(String averageGrade)
    {
	this.averageGrade = averageGrade;
    }

    public Float getAverageScore()
    {
	return this.averageScore;
    }

    public void setAverageScore(Float averageScore)
    {
	this.averageScore = averageScore;
    }

    public String getCourseMembershipBbId()
    {
	return this.courseMembershipBbId;
    }

    public void setCourseMembershipBbId(String courseMembershipBbId)
    {
	this.courseMembershipBbId = courseMembershipBbId;
    }

    public String getFirstAttemptBbId()
    {
	return this.firstAttemptBbId;
    }

    public void setFirstAttemptBbId(String firstAttemptBbId)
    {
	this.firstAttemptBbId = firstAttemptBbId;
    }

    public String getGrade()
    {
	return this.grade;
    }

    public void setGrade(String grade)
    {
	this.grade = grade;
    }

    public String getGradebookStatus()
    {
	return this.gradebookStatus;
    }

    public void setGradebookStatus(String gradebookStatus)
    {
	this.gradebookStatus = gradebookStatus;
    }

    public String getHighestAttemptBbId()
    {
	return this.highestAttemptBbId;
    }

    public void setHighestAttemptBbId(String highestAttemptBbId)
    {
	this.highestAttemptBbId = highestAttemptBbId;
    }

    public String getInstructorComments()
    {
	return this.instructorComments;
    }

    public void setInstructorComments(String instructorComments)
    {
	this.instructorComments = instructorComments;
    }

    public String getLastAttemptBbId()
    {
	return this.lastAttemptBbId;
    }

    public void setLastAttemptBbId(String lastAttemptBbId)
    {
	this.lastAttemptBbId = lastAttemptBbId;
    }

    public String getLowestAttemptBbId()
    {
	return this.lowestAttemptBbId;
    }

    public void setLowestAttemptBbId(String lowestAttemptBbId)
    {
	this.lowestAttemptBbId = lowestAttemptBbId;
    }

    public String getManualGrade()
    {
	return this.manualGrade;
    }

    public void setManualGrade(String manualGrade)
    {
	this.manualGrade = manualGrade;
    }

    public Float getManualScore()
    {
	return this.manualScore;
    }

    public void setManualScore(Float manualScore)
    {
	this.manualScore = manualScore;
    }

    public String getOutcomeBbId()
    {
	return this.outcomeBbId;
    }

    public void setOutcomeBbId(String outcomeBbId)
    {
	this.outcomeBbId = outcomeBbId;
    }

    public String getOutcomeDefinitionBbId()
    {
	return this.outcomeDefinitionBbId;
    }

    public void setOutcomeDefinitionBbId(String outcomeDefinitionBbId)
    {
	this.outcomeDefinitionBbId = outcomeDefinitionBbId;
    }

    public Float getScore()
    {
	return this.score;
    }

    public void setScore(Float score)
    {
	this.score = score;
    }

    public String getStudentComments()
    {
	return this.studentComments;
    }

    public void setStudentComments(String studentComments)
    {
	this.studentComments = studentComments;
    }

    public Float getTotalScore()
    {
	return this.totalScore;
    }

    public void setTotalScore(Float totalScore)
    {
	this.totalScore = totalScore;
    }

}
