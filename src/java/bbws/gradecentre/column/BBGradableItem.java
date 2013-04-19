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

package bbws.gradecentre.column;

//bbws
import bbws.gradecentre.enums.BBAggregationModel;

//blackboard - platform
import blackboard.platform.gradebook2.GradableItem;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BBGradableItem
{
    private BBAggregationModel aggregationModel;
    private String assessmentId;
    private Boolean calculated;
    private String calculatedInd;
    private String category;
    private String categoryId;
    private String courseContentId;
    private String courseId;
    private String dateAdded;
    private String dateModified;
    private Boolean deleted;
    private String description;
    private String descriptionForDisplay;
    private String displayColumnName;
    private String displayPoints;
    private String displayTitle;
    private String dueDate;
    private String externalAnalysisUrl;
    private String externalAttemptHandlerUrl;
    private String externalId;
    private String formattedDateAdded;
    private String formattedDueDate;
    private Boolean gradeItem;
    private String gradingPeriodId;
    private String gradingSchema;
    private String gradingSchemaId;
    private Boolean hideAttempt;
    private String id;
    private String linkId;
    private Boolean manual;
    private Double points;
    private Integer position;
    private String schemaValue;
    private Boolean scorable;
    private Double score;
    private String scoreProvider;
    private String scoreProviderHandle;
    private String secondaryGradingSchemaId;
    private Boolean showStatsToStudent;
    private Boolean singleAttempt;
    private String title;
    private Boolean usedInCalculation;
    private Long version;
    private Boolean visibleInAllTerms;
    private Boolean visibleInBook;
    private Boolean visibleToStudents;
    private Double weight;

    public BBGradableItem(){}
    public BBGradableItem(GradableItem gi) throws Exception
    {

        try{this.aggregationModel = BBAggregationModel.valueOfSafe(gi.getAggregationModel().name());}catch(Exception e){this.aggregationModel = null;}
        try
        {
            Object o = gi.getAssessmentId();
            if(o!=null)
            {
                this.assessmentId = o.getClass().getName();
                if(this.assessmentId.equalsIgnoreCase("java.lang.String"))
                {
                    this.assessmentId = o.toString();
                }
                else if(this.assessmentId.equalsIgnoreCase("blackboard.persist.Id"))
                {
                    this.assessmentId = ((blackboard.persist.Id)o).getExternalString();
                }
                else if(this.assessmentId.equalsIgnoreCase("blackboard.persist.PkId"))
                {
                    this.assessmentId = ((blackboard.persist.PkId)o).getExternalString();
                }
            }
        }
        catch(Exception e)
        {
            this.assessmentId = "";
        }
        this.calculated = gi.isCalculated();
        this.calculatedInd = gi.getCalculatedInd().toString();
        this.category = gi.getCategory();
        try{this.categoryId = gi.getCategoryId().toExternalString();}catch(Exception e){this.categoryId = "";}
        try{this.courseContentId = gi.getCourseContentId().toExternalString();}catch(NullPointerException npe){this.courseContentId = "";}
        this.courseId = gi.getCourseId().toExternalString();
        this.dateAdded = getDateTimeFromCalendar(gi.getDateAdded());
        this.dateModified = getDateTimeFromCalendar(gi.getDateModified());
        this.deleted = gi.isDeleted();
        try{this.description = gi.getDescription().getText();}catch(NullPointerException npe){this.description = "";}
        this.descriptionForDisplay = gi.getDescriptionForDisplay().getText();
        this.displayColumnName = gi.getDisplayColumnName();
        this.displayPoints = gi.getDisplayPoints();
        this.displayTitle = gi.getDisplayTitle();
        this.dueDate = getDateTimeFromCalendar(gi.getDueDate());
        this.externalAnalysisUrl = gi.getExternalAnalysisUrl();
        this.externalAttemptHandlerUrl = gi.getExternalAttemptHandlerUrl();
        this.externalId = gi.getExternalId();
        this.formattedDateAdded = gi.getFormattedDateAdded();
        this.formattedDueDate = gi.getFormattedDueDate();
        //V9.1 doesn't like this call?
        //this.gradeItem = gi.isGradeItem();
        try{this.gradingPeriodId = gi.getGradingPeriodId().toExternalString();}catch(NullPointerException npe){this.gradingPeriodId = "";}
        try{this.gradingSchema = gi.getGradingSchema().getTitle();}catch(NullPointerException npe){this.gradingSchema = "";}
        this.gradingSchemaId = gi.getGradingSchemaId().toExternalString();
        this.hideAttempt = gi.isHideAttempt();
        this.id = gi.getId().toExternalString();
        this.linkId = gi.getLinkId();
        this.manual = gi.isManual();
        this.points = gi.getPoints();
        this.position = gi.getPosition();
        //this.schemaValue = gi.getSchemaValue(score); //WTH is the parameter for?
        this.scorable = gi.isScorable();
        //this.score = gi.getScore(""); //Another weird parameter?
        try{this.scoreProvider = gi.getScoreProvider().getName();}catch(NullPointerException npe){this.scoreProvider = "";}
        this.scoreProviderHandle = gi.getScoreProviderHandle();
        try{this.secondaryGradingSchemaId = gi.getSecondaryGradingSchemaId().toExternalString();}catch(NullPointerException npe){this.secondaryGradingSchemaId = "";}
        this.showStatsToStudent = gi.isShowStatsToStudent();
        this.singleAttempt = gi.isSingleAttempt();
        this.title = gi.getTitle();
        this.usedInCalculation = gi.isUsedInCalculation();
        this.version = gi.getVersion();
        this.visibleInAllTerms = gi.isVisibleInAllTerms();
        this.visibleInBook = gi.isVisibleInBook();
        this.visibleToStudents = gi.isVisibleToStudents();
        //V9.1 doesn't like this call?
        //this.weight = gi.getWeight();
    }

    public BBAggregationModel getAggregationModel()
    {
	return this.aggregationModel;
    }

    public void setAggregationModel(BBAggregationModel aggregationModel)
    {
	this.aggregationModel = aggregationModel;
    }

    public String getAssessmentId()
    {
	return this.assessmentId;
    }

    public void setAssessmentId(String assessmentId)
    {
	this.assessmentId = assessmentId;
    }

    public Boolean getCalculated()
    {
        return this.calculated;
    }

    public void setCalculated(Boolean calculated)
    {
        this.calculated = calculated;
    }

    public String getCalculatedInd()
    {
        return this.calculatedInd;
    }

    public void setCalculatedInd(String calculatedInd)
    {
        this.calculatedInd = calculatedInd;
    }

    public String getCategory()
    {
        return this.category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategoryId()
    {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCourseContentId()
    {
        return this.courseContentId;
    }

    public void setCourseContentId(String courseContentId)
    {
        this.courseContentId = courseContentId;
    }

    public String getCourseId()
    {
        return this.courseId;
    }

    public void setCourseId(String courseId)
    {
        this.courseId = courseId;
    }

    public String getDateAdded()
    {
	return this.dateAdded;
    }

    public void setDateAdded(String dateAdded)
    {
	this.dateAdded = dateAdded;
    }

    public String getDateModified()
    {
	return this.dateModified;
    }

    public void setDateModified(String dateModified)
    {
	this.dateModified = dateModified;
    }

    public Boolean getDeleted()
    {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }

    public String getDescription()
    {
	return this.description;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

    public String getDescriptionForDisplay()
    {
	return this.descriptionForDisplay;
    }

    public void setDescriptionForDisplay(String descriptionForDisplay)
    {
	this.descriptionForDisplay = descriptionForDisplay;
    }

    public String getDisplayColumnName()
    {
	return this.displayColumnName;
    }

    public void setDisplayColumnName(String displayColumnName)
    {
	this.displayColumnName = displayColumnName;
    }

    public String getDisplayPoints()
    {
	return this.displayPoints;
    }

    public void setDisplayPoints(String displayPoints)
    {
	this.displayPoints = displayPoints;
    }

    public String getDisplayTitle()
    {
	return this.displayTitle;
    }

    public void setDisplayTitle(String displayTitle)
    {
	this.displayTitle = displayTitle;
    }

    public String getDueDate()
    {
	return this.dueDate;
    }

    public void setDueDate(String dueDate)
    {
	this.dueDate = dueDate;
    }

    public void setExternalAnalysisUrl(String externalAnalysisUrl)
    {
	this.externalAnalysisUrl = externalAnalysisUrl;
    }

    public String getExternalAnalysisUrl()
    {
	return this.externalAnalysisUrl;
    }

    public void setExternalAttemptHandlerUrl(String externalAttemptHandlerUrl)
    {
	this.externalAttemptHandlerUrl = externalAttemptHandlerUrl;
    }

    public String getExternalAttemptHandlerUrl()
    {
	return this.externalAttemptHandlerUrl;
    }

    public void setExternalId(String externalId)
    {
	this.externalId = externalId;
    }

    public String getExternalId()
    {
	return this.externalId;
    }

    public String getFormattedDateAdded()
    {
	return this.formattedDateAdded;
    }

    public void setFormattedDateAdded(String formattedDateAdded)
    {
	this.formattedDateAdded = formattedDateAdded;
    }

    public String getFormattedDueDate()
    {
	return this.formattedDueDate;
    }

    public void setFormattedDueDate(String formattedDueDate)
    {
	this.formattedDueDate = formattedDueDate;
    }

    public String getSchemaValue()
    {
	return this.schemaValue;
    }

    public void setSchemaValue(String schemaValue)
    {
	this.schemaValue = schemaValue;
    }

    public Boolean getGradeItem()
    {
	return this.gradeItem;
    }

    public void setGradeItem(Boolean gradeItem)
    {
	this.gradeItem = gradeItem;
    }

    public String getGradingPeriodId()
    {
	return this.gradingPeriodId;
    }

    public void setGradingPeriodId(String gradingPeriodId)
    {
	this.gradingPeriodId = gradingPeriodId;
    }

    public String getGradingSchema()
    {
	return this.gradingSchema;
    }

    public void setGradingSchema(String gradingSchema)
    {
	this.gradingSchema = gradingSchema;
    }

    public String getGradingSchemaId()
    {
	return this.gradingSchemaId;
    }

    public void setGradingSchemaId(String gradingSchemaId)
    {
	this.gradingSchemaId = gradingSchemaId;
    }

    public Boolean getHideAttempt()
    {
	return this.hideAttempt;
    }

    public void setHideAttempt(Boolean hideAttempt)
    {
	this.hideAttempt = hideAttempt;
    }

    public String getId()
    {
	return this.id;
    }

    public void setId(String id)
    {
	this.id = id;
    }

    public String getLinkId()
    {
	return this.linkId;
    }

    public void setLinkId(String linkId)
    {
	this.linkId = linkId;
    }

    public Boolean getManual()
    {
	return this.manual;
    }

    public void setManual(Boolean manual)
    {
	this.manual = manual;
    }

    public Double getPoints()
    {
	return this.points;
    }

    public void setPoints(Double points)
    {
	this.points = points;
    }

    public Integer getPosition()
    {
	return this.position;
    }

    public void setPosition(Integer position)
    {
	this.position = position;
    }

    public Boolean getScorable()
    {
	return this.scorable;
    }

    public void setScorable(Boolean scorable)
    {
	this.scorable = scorable;
    }

    public Double getScore()
    {
	return this.score;
    }

    public void setScore(Double score)
    {
	this.score = score;
    }

    public String getScoreProvider()
    {
	return this.scoreProvider;
    }

    public void setScoreProvider(String scoreProvider)
    {
	this.scoreProvider = scoreProvider;
    }

    public String getScoreProviderHandle()
    {
	return this.scoreProviderHandle;
    }

    public void setScoreProviderHandle(String scoreProviderHandle)
    {
	this.scoreProviderHandle = scoreProviderHandle;
    }
    public String getSecondaryGradingSchemaId()
    {
	return this.secondaryGradingSchemaId;
    }

    public void setSecondaryGradingSchemaId(String secondaryGradingSchemaId)
    {
	this.secondaryGradingSchemaId = secondaryGradingSchemaId;
    }

    public Boolean getShowStatsToStudent()
    {
	return this.showStatsToStudent;
    }

    public void setShowStatsToStudent(Boolean showStatsToStudent)
    {
	this.showStatsToStudent = showStatsToStudent;
    }

    public Boolean getSingleAttempt()
    {
	return this.singleAttempt;
    }

    public void setSingleAttempt(Boolean singleAttempt)
    {
	this.singleAttempt = singleAttempt;
    }

    public String getTitle()
    {
	return this.title;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public Boolean getUsedInCalculation()
    {
	return this.usedInCalculation;
    }

    public void setUsedInCalculation(Boolean usedInCalculation)
    {
	this.usedInCalculation = usedInCalculation;
    }

    public Long getVersion()
    {
	return this.version;
    }

    public void setVersion(Long version)
    {
	this.version = version;
    }

    public Boolean getVisibleInAllTerms()
    {
	return this.visibleInAllTerms;
    }

    public void setVisibleInAllTerms(Boolean visibleInAllTerms)
    {
	this.visibleInAllTerms = visibleInAllTerms;
    }

    public Boolean getVisibleInBook()
    {
	return this.visibleInBook;
    }

    public void setVisibleInBook(Boolean visibleInBook)
    {
	this.visibleInBook = visibleInBook;
    }

    public Boolean getVisibleToStudents()
    {
	return this.visibleToStudents;
    }

    public void setVisibleToStudents(Boolean visibleToStudents)
    {
	this.visibleToStudents = visibleToStudents;
    }

    public Double getWeight()
    {
	return this.weight;
    }

    public void setWeight(Double weight)
    {
	this.weight = weight;
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
