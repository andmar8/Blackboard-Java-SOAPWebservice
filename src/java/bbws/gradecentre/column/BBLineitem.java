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

//blackboard - data
import bbws.gradecentre.grade.BBScore;
import blackboard.data.gradebook.Lineitem;
import blackboard.data.gradebook.Lineitem.AssessmentLocation;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BBLineitem
{
    public enum BBLineitemVerbosity{WithScores,WithoutScores}

    private String assessmentBbId;
    private String assessmentLocation;
    private Boolean available;
    private Integer columnPosition;
    private String dateAdded;
    private String dateChanged;
    private BBLineitemVerbosity verbosity;
    private String lineItemBbId;
    private String name;
    private String outcomeDefBbId;
    private Float pointsPossible;
    //private List<BBScore> scores;
    private String title;
    private String type;
    private Float weight;

    public BBLineitem(){}
    public BBLineitem(Lineitem li,BBLineitemVerbosity verbosity) throws Exception
    {
        this.verbosity = verbosity;

        switch(this.verbosity)
        {
            case WithScores:
                /*try
                {
                    this.scores = bbws.util.factory.list.BBListFactory.getBBScoreListFromList(li.getScores(),bbws.gradecentre.grade.BBScore.BBScoreVerbosity.extended);
                }
                catch(Exception e)
                {
                    this.scores = null;
                }*/
            case WithoutScores:
                Object o = li.getAssessmentId();
                if(o!=null)
                {
                    this.assessmentBbId = o.getClass().getName();
                    if(this.assessmentBbId.equalsIgnoreCase("java.lang.String"))
                    {
                        this.assessmentBbId = o.toString();
                    }
                    else if(this.assessmentBbId.equalsIgnoreCase("blackboard.persist.Id"))
                    {
                        this.assessmentBbId = ((blackboard.persist.Id)o).getExternalString();
                    }
                }
                if(li.getAssessmentLocation().equals(AssessmentLocation.EXTERNAL)){this.assessmentLocation = "EXTERNAL";}
                else if(li.getAssessmentLocation().equals(AssessmentLocation.INTERNAL)){this.assessmentLocation = "INTERNAL";}
                else if(li.getAssessmentLocation().equals(AssessmentLocation.UNSET)){this.assessmentLocation = "UNSET";}
                this.available = li.getIsAvailable();
                this.columnPosition = li.getColumnOrder();
                this.dateAdded = getDateTimeFromCalendar(li.getDateAdded());
                this.dateChanged = getDateTimeFromCalendar(li.getDateChanged());
                this.lineItemBbId = li.getId().toExternalString();
                this.name = li.getName();
                this.outcomeDefBbId = li.getOutcomeDefinition().getId().toExternalString();
                this.pointsPossible = li.getPointsPossible();
                this.type = li.getType();
                this.weight = li.getWeight();
            return;
        }
        throw new Exception("Undefined verbosity of line item");
    }

    public String getAssessmentBbId()
    {
	return this.assessmentBbId;
    }

    public void setAssessmentBbId(String assessmentBbId)
    {
	this.assessmentBbId = assessmentBbId;
    }

    public String getAssessmentLocation()
    {
	return this.assessmentLocation;
    }

    public void setAssessmentLocation(String assessmentLocation)
    {
	this.assessmentLocation = assessmentLocation;
    }

    public Boolean getAvailable()
    {
	return this.available;
    }

    public void setAvailable(Boolean available)
    {
	this.available = available;
    }

    public Integer getColumnPosition()
    {
	return this.columnPosition;
    }

    public void setColumnPosition(Integer columnPosition)
    {
	this.columnPosition = columnPosition;
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

    public String getLineItemBbId()
    {
	return this.lineItemBbId;
    }

    public void setLineItemBbId(String lineItemBbId)
    {
	this.lineItemBbId = lineItemBbId;
    }

    public String getName()
    {
	return this.name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public String getOutcomeDefBbId()
    {
	return this.outcomeDefBbId;
    }

    public void setOutcomeDefBbId(String outcomeDefBbId)
    {
	this.outcomeDefBbId = outcomeDefBbId;
    }

    public Float getPointsPossible()
    {
        return this.pointsPossible;
    }

    public void setPointsPossible(Float pointsPossible)
    {
        this.pointsPossible = pointsPossible;
    }

    /*public List<BBScore> getScores()
    {
        return this.scores;
    }

    public void setScores(List<BBScore> scores)
    {
        this.scores = scores;
    }*/

    public String getTitle()
    {
	return this.title;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public String getType()
    {
	return this.type;
    }

    public void setType(String type)
    {
	this.type = type;
    }

    public Float getWeight()
    {
        return this.weight;
    }

    public void setWeight(Float weight)
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
