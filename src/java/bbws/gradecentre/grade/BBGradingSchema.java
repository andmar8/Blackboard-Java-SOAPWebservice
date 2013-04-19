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

// blackboard - platform
import blackboard.platform.gradebook2.GradingSchema;

public class BBGradingSchema
{
    private Boolean canRemove;
    private String courseId;
    private String description;
    //private String externalId; // -- seem to be the same as the gradingSchemaId
    private String gradingSchemaId;
    //private String id; // -- seem to be the same as the gradingSchemaId
    private Boolean inUse;
    private String localisedTitle;
    private Boolean numeric;
    private Boolean percentage;
    private String scaleType;
    private Boolean tabular;
    private String title;
    private Boolean userDefined;
    private Long version;

    public BBGradingSchema(){}
    public BBGradingSchema(GradingSchema gs)
    {
        this.canRemove = gs.getCanRemove();
        this.courseId = gs.getCourseId().toExternalString();
        this.description = gs.getDescription();
        //this.externalId = gs.getExternalId();
        this.gradingSchemaId = gs.getGradingSchemaId().toExternalString();
        //this.id = gs.getId().toExternalString();
        this.inUse = gs.isInuse();
        this.localisedTitle = gs.getLocalizedTitle();
        this.numeric = gs.isNumeric();
        this.percentage = gs.isPercentage();
        this.scaleType = gs.getScaleType().name();
        //gs.getSchemaValue(Double.NaN, pointsPossible); //?
        //gs.getScore(courseId, pointsPossible); //?
        //gs.getSymbols(); //?
        this.tabular = gs.isTabular();
        this.title = gs.getTitle();
        this.userDefined = gs.isUserDefined();
        this.version = gs.getVersion();
    }

    public Boolean getCanRemove()
    {
        return this.canRemove;
    }

    public void setCanRemove(Boolean canRemove)
    {
        this.canRemove = canRemove;
    }

    public String getCourseId()
    {
        return this.courseId;
    }

    public void setCourseId(String courseId)
    {
        this.courseId = courseId;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /*public String getExternalId()
    {
        return this.externalId;
    }

    public void setExternalId(String externalId)
    {
        this.externalId = externalId;
    }*/

    public String getGradingSchemaId()
    {
        return this.gradingSchemaId;
    }

    public void setGradingSchemaId(String gradingSchemaId)
    {
        this.gradingSchemaId = gradingSchemaId;
    }

    /*public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }*/

    public Boolean getInUse()
    {
        return this.inUse;
    }

    public void setInUse(Boolean inUse)
    {
        this.inUse = inUse;
    }

    public String getLocalisedTitle()
    {
        return this.localisedTitle;
    }

    public void setLocalisedTitle(String localisedTitle)
    {
        this.localisedTitle = localisedTitle;
    }

    public Boolean getNumeric()
    {
        return this.numeric;
    }

    public void setNumeric(Boolean numeric)
    {
        this.numeric = numeric;
    }

    public Boolean getPercentage()
    {
        return this.percentage;
    }

    public void setPercentage(Boolean percentage)
    {
        this.percentage = percentage;
    }

    public String getScaleType()
    {
        return this.scaleType;
    }

    public void setScaleType(String scaleType)
    {
        this.scaleType = scaleType;
    }

    public Boolean getTabular()
    {
        return this.tabular;
    }

    public void setTabular(Boolean tabular)
    {
        this.tabular = tabular;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Boolean getUserDefined()
    {
        return this.userDefined;
    }

    public void setUserDefined(Boolean userDefined)
    {
        this.userDefined = userDefined;
    }

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }
}
