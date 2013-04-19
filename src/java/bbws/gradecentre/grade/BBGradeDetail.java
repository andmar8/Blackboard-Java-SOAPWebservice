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

//blackboard - platform
import blackboard.platform.gradebook2.GradeDetail;

public class BBGradeDetail
{
    //Needs verbosity
    //private List BBAttemptDetail attempts
    private Double averageScore;
    private String calculatedGrade;
    private String courseUserId;
    private Boolean exempt;
    private String firstAttemptId;
    private String grade;
    //private BBGradableItem gradableItem;
    private String gradableItemId;
    private String gradeStatusKey;
    private Boolean gradingRequired;
    private String highestAttemptId;
    //private BBGradeHistoryEntry gradeHistoryEntry;
    private String id;
    private String instructorComments;
    private String lastAttemptId;
    private String lowestAttemptId;
    //private Boolean manualChangeIndicator;
    private String manualGrade;
    private Double manualScore;
    private Boolean nullGrade;
    private String shortInstructorComments;
    private String shortStudentComments;
    private String studentComments;

    public BBGradeDetail(){}
    public BBGradeDetail(GradeDetail gd) throws Exception
    {
        //this.attempts = BBListFactory --> gd.getAttempts();
        this.averageScore = gd.getAverageScore();
        this.calculatedGrade = gd.getCalculatedGrade();
        //Is this the courseMembership Id?
        try{this.courseUserId = gd.getCourseUserId().toExternalString();}catch(Exception e){this.courseUserId = "";}
        this.exempt = gd.isExempt();
        try{this.firstAttemptId = gd.getFirstAttemptId().toExternalString();}catch(Exception e){this.firstAttemptId = "";}
        //Not neccesary surely?
        //this.gradableItem = new BBGradableItem(gd.getGradableItem());
        try{this.gradableItemId = gd.getGradableItemId().toExternalString();}catch(Exception e){this.gradableItemId = "";}
        this.grade = gd.getGrade();
        this.gradeStatusKey = gd.getGradeStatusKey();
        this.gradingRequired = gd.isGradingRequired();
        try{this.highestAttemptId = gd.getHighestAttemptId().toExternalString();}catch(Exception e){this.highestAttemptId = "";}
        //Could be possible, but is it needed? - maaaybeeeeeee... :-/
        //this.history = gd.getHistory();
        try{this.id = gd.getId().toExternalString();}catch(Exception e){this.id = "";}
        this.instructorComments = gd.getInstructorComments();
        try{this.lastAttemptId = gd.getLastAttemptId().toExternalString();}catch(Exception e){this.lastAttemptId = "";}
        try{this.lowestAttemptId = gd.getLowestAttemptId().toExternalString();}catch(Exception e){this.lowestAttemptId = "";}
        //This is not in v9.1
        //this.manualChangeIndicator = gd.isManualChangeIndicator();
        this.manualGrade = gd.getManualGrade();
        this.manualScore = gd.getManualScore();
        this.nullGrade = gd.isNullGrade();
        this.shortInstructorComments = gd.getShortInstructorComments();
        this.shortStudentComments = gd.getShortStudentComments();
        this.studentComments = gd.getStudentComments();
   }

    public Double getAverageScore()
    {
        return averageScore;
    }

    public void setAverageScore(Double averageScore)
    {
        this.averageScore = averageScore;
    }

    public String getCalculatedGrade()
    {
        return calculatedGrade;
    }

    public void setCalculatedGrade(String calculatedGrade)
    {
        this.calculatedGrade = calculatedGrade;
    }

    public String getCourseUserId()
    {
        return courseUserId;
    }

    public void setCourseUserId(String courseUserId)
    {
        this.courseUserId = courseUserId;
    }

    public Boolean getExempt()
    {
        return exempt;
    }

    public void setExempt(Boolean exempt)
    {
        this.exempt = exempt;
    }

    public String getFirstAttemptId()
    {
        return firstAttemptId;
    }

    public void setFirstAttemptId(String firstAttemptId)
    {
        this.firstAttemptId = firstAttemptId;
    }

    public String getGradableItemId()
    {
        return gradableItemId;
    }

    public void setGradableItemId(String gradableItemId)
    {
        this.gradableItemId = gradableItemId;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    public String getGradeStatusKey()
    {
        return gradeStatusKey;
    }

    public void setGradeStatusKey(String gradeStatusKey)
    {
        this.gradeStatusKey = gradeStatusKey;
    }

    public Boolean getGradingRequired()
    {
        return gradingRequired;
    }

    public void setGradingRequired(Boolean gradingRequired)
    {
        this.gradingRequired = gradingRequired;
    }

    public String getHighestAttemptId()
    {
        return highestAttemptId;
    }

    public void setHighestAttemptId(String highestAttemptId)
    {
        this.highestAttemptId = highestAttemptId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getInstructorComments()
    {
        return instructorComments;
    }

    public void setInstructorComments(String instructorComments)
    {
        this.instructorComments = instructorComments;
    }

    public String getLastAttemptId()
    {
        return lastAttemptId;
    }

    public void setLastAttemptId(String lastAttemptId)
    {
        this.lastAttemptId = lastAttemptId;
    }

    public String getLowestAttemptId()
    {
        return lowestAttemptId;
    }

    public void setLowestAttemptId(String lowestAttemptId)
    {
        this.lowestAttemptId = lowestAttemptId;
    }

    public String getManualGrade()
    {
        return manualGrade;
    }

    public void setManualGrade(String manualGrade)
    {
        this.manualGrade = manualGrade;
    }

    public Double getManualScore()
    {
        return manualScore;
    }

    public void setManualScore(Double manualScore)
    {
        this.manualScore = manualScore;
    }

    public Boolean getNullGrade()
    {
        return nullGrade;
    }

    public void setNullGrade(Boolean nullGrade)
    {
        this.nullGrade = nullGrade;
    }

    public String getShortInstructorComments()
    {
        return shortInstructorComments;
    }

    public void setShortInstructorComments(String shortInstructorComments)
    {
        this.shortInstructorComments = shortInstructorComments;
    }

    public String getShortStudentComments()
    {
        return shortStudentComments;
    }

    public void setShortStudentComments(String shortStudentComments)
    {
        this.shortStudentComments = shortStudentComments;
    }

    public String getStudentComments()
    {
        return studentComments;
    }

    public void setStudentComments(String studentComments)
    {
        this.studentComments = studentComments;
    }
}
