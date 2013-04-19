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

//bbws
import bbws.gradecentre.enums.BBAggregationModel;

//blackboard
import blackboard.data.gradebook.impl.OutcomeDefinition;

//java - text
import java.text.SimpleDateFormat;

//java - util
import java.util.Calendar;

public class BBOutcomeDefinition
{
    private BBAggregationModel aggregationModel;
    private String analysisUrl;
    private String asiDataBbId;
    private Boolean calculated;
    private String calculationType;
    private String category;
    private String categoryId;
    private String contentId;
    private String courseId;
    private String dateAdded;
    private String dateCreated;
    private String dateModified;
    private String description;
    private String dueDate;
    private Boolean dueDateInUse;
    private String handlerUrl;
    private Boolean hideAttempt;
    private String id;
    private Boolean ignoreUnscoredAttempts;
    private String linkId;
    private Integer numOfOutcomes;
    private String outcomeDefinitionBbId;
    private String persistentDescription;
    private String persistentTitle;
    private Integer position;
    private Float possible;
    private Boolean scorable;
    private String scoreProviderHandle;
    private String simpleDateCreated;
    private String simpleDueDate;
    private String title;
    private Boolean total;
    private Boolean visible;
    private Float weight;
    private Boolean weightedTotal;

    public BBOutcomeDefinition(){}
    public BBOutcomeDefinition(OutcomeDefinition od)
    {
        try{this.aggregationModel = BBAggregationModel.valueOfSafe(od.getAggregationModel().toFieldName());}catch(Exception e){this.aggregationModel = null;}
        this.analysisUrl = od.getAnalysisUrl();
        try{this.asiDataBbId = od.getAsiDataId().toExternalString();}catch(Exception e){this.asiDataBbId = "";}
        this.calculated = od.isCalculated();
        try{this.calculationType = od.getCalculationType().toFieldName();}catch(Exception e){this.calculationType = "";}
        try{this.category = od.getCategory().getDescription();}catch(Exception e){this.category = "";}
        try{this.categoryId = od.getCategoryId().toExternalString();}catch(Exception e){this.categoryId = "";}
        try{this.contentId = od.getContentId().toExternalString();}catch(Exception e){this.contentId = "";}
        this.courseId = od.getCourseId().toExternalString();
        this.dateAdded = getDateTimeFromCalendar(od.getDateAdded());
        this.dateCreated = getDateTimeFromCalendar(od.getDateCreated());
        this.dateModified = getDateTimeFromCalendar(od.getDateModified());
        this.description = od.getDescription();
        this.dueDate = getDateTimeFromCalendar(od.getDueDate());
        this.dueDateInUse = od.isDueDateInUse();
        this.handlerUrl = od.getHandlerUrl();
        this.hideAttempt = od.getHideAttempt();
        this.id = od.getId().toExternalString();
        this.ignoreUnscoredAttempts = od.isIgnoreUnscoredAttempts();
        this.linkId = od.getLinkId();
        this.numOfOutcomes = od.getOutcomeCount();
        //od.getOutcomes();
        //od.getOutcomes(onlyStudents);
        this.outcomeDefinitionBbId = od.getId().getExternalString();
        this.persistentDescription = od.getPersistentDescription();
        this.persistentTitle = od.getPersistentTitle();
        this.position = od.getPosition();
        this.possible = od.getPossible();
        //od.getScale();
        //od.getScaleId();
        this.scorable = od.isScorable();
        this.scoreProviderHandle = od.getScoreProviderHandle();
        this.simpleDateCreated = od.getSimpleDateCreated();
        this.simpleDueDate = od.getSimpleDueDate();
        this.title = od.getTitle();
        this.total = od.isTotal();
        this.visible = od.isVisible();
        this.weight = od.getWeight();
        this.weightedTotal = od.isWeightedTotal();
    }

    public String getAsiDataBbId()
    {
	return this.asiDataBbId;
    }

    public void setAsiDataBbId(String asiDataBbId)
    {
	this.asiDataBbId = asiDataBbId;
    }

    public String getCategory()
    {
	return this.category;
    }

    public void setCategory(String category)
    {
	this.category = category;
    }

    public String getDescription()
    {
	return this.description;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.outcomeDefinitionBbId = id;
    }

    public Integer getNumOfOutcomes()
    {
	return this.numOfOutcomes;
    }

    public void setNumOfOutcomes(Integer numOfOutcomes)
    {
	this.numOfOutcomes = numOfOutcomes;
    }

    public String getOutcomeDefinitionBbId()
    {
	return this.outcomeDefinitionBbId;
    }

    public void setOutcomeDefinitionBbId(String outcomeDefinitionBbId)
    {
	this.outcomeDefinitionBbId = outcomeDefinitionBbId;
    }

    public Integer getPosition()
    {
	return this.position;
    }

    public void setPosition(Integer position)
    {
	this.position = position;
    }

    public String getTitle()
    {
	return this.title;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public Float getWeight()
    {
	return this.weight;
    }

    public void setWeight(Float weight)
    {
	this.weight = weight;
    }

    public Float getPossible() {
        return possible;
    }

    public void setPossible(Float possible) {
        this.possible = possible;
    }

    public Boolean getTotal() {
        return total;
    }

    public void setTotal(Boolean total) {
        this.total = total;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getWeightedTotal() {
        return weightedTotal;
    }

    public void setWeightedTotal(Boolean weightedTotal) {
        this.weightedTotal = weightedTotal;
    }

    public BBAggregationModel getAggregationModel() {
        return aggregationModel;
    }

    public void setAggregationModel(BBAggregationModel aggregationModel) {
        this.aggregationModel = aggregationModel;
    }

    public String getAnalysisUrl() {
        return analysisUrl;
    }

    public void setAnalysisUrl(String analysisUrl) {
        this.analysisUrl = analysisUrl;
    }

    public Boolean getCalculated() {
        return calculated;
    }

    public void setCalculated(Boolean calculated) {
        this.calculated = calculated;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Boolean getDueDateInUse() {
        return dueDateInUse;
    }

    public void setDueDateInUse(Boolean dueDateInUse) {
        this.dueDateInUse = dueDateInUse;
    }

    public String getHandlerUrl() {
        return handlerUrl;
    }

    public void setHandlerUrl(String handlerUrl) {
        this.handlerUrl = handlerUrl;
    }

    public Boolean getHideAttempt() {
        return hideAttempt;
    }

    public void setHideAttempt(Boolean hideAttempt) {
        this.hideAttempt = hideAttempt;
    }

    public Boolean getIgnoreUnscoredAttempts() {
        return ignoreUnscoredAttempts;
    }

    public void setIgnoreUnscoredAttempts(Boolean ignoreUnscoredAttempts) {
        this.ignoreUnscoredAttempts = ignoreUnscoredAttempts;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getPersistentDescription() {
        return persistentDescription;
    }

    public void setPersistentDescription(String persistentDescription) {
        this.persistentDescription = persistentDescription;
    }

    public String getPersistentTitle() {
        return persistentTitle;
    }

    public void setPersistentTitle(String persistentTitle) {
        this.persistentTitle = persistentTitle;
    }

    public Boolean getScorable() {
        return scorable;
    }

    public void setScorable(Boolean scorable) {
        this.scorable = scorable;
    }

    public String getScoreProviderHandle() {
        return scoreProviderHandle;
    }

    public void setScoreProviderHandle(String scoreProviderHandle) {
        this.scoreProviderHandle = scoreProviderHandle;
    }

    public String getSimpleDateCreated() {
        return simpleDateCreated;
    }

    public void setSimpleDateCreated(String simpleDateCreated) {
        this.simpleDateCreated = simpleDateCreated;
    }

    public String getSimpleDueDate() {
        return simpleDueDate;
    }

    public void setSimpleDueDate(String simpleDueDate) {
        this.simpleDueDate = simpleDueDate;
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