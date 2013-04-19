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
package bbws.gradecentre;

//blackboard
import blackboard.data.gradebook.impl.GradeBookSettings;

public class BBGradeCentreSettings
{
    private Boolean averageDisplayed;
    private Boolean commentsDisplayed;
    private Boolean firstLastDisplayed;
    private String gradeBookSettingBbId;
    private Boolean lastFirstDisplayed;
    private Boolean studentIdDisplayed;
    private Boolean userIdDisplayed;
    private String weightType;

    public BBGradeCentreSettings(){}
    public BBGradeCentreSettings(GradeBookSettings gbs)
    {
        this.averageDisplayed = gbs.isAverageDisplayed();
        this.commentsDisplayed = gbs.areCommentsDisplayed();
        this.firstLastDisplayed = gbs.isFirstLastDisplayed();
        this.gradeBookSettingBbId = gbs.getId().toExternalString();
        this.lastFirstDisplayed = gbs.isLastFirstDisplayed();
        this.studentIdDisplayed = gbs.isStudentIdDisplayed();
        this.userIdDisplayed = gbs.isUserIdDisplayed();
        this.weightType = gbs.getWeightType().toFieldName();
    }

    public Boolean getAverageDisplayed()
    {
	return this.averageDisplayed;
    }

    public void setAverageDisplayed(Boolean averageDisplayed)
    {
	this.averageDisplayed = averageDisplayed;
    }

    public Boolean getCommentsDisplayed()
    {
	return this.commentsDisplayed;
    }

    public void setCommentsDisplayed(Boolean commentsDisplayed)
    {
	this.commentsDisplayed = commentsDisplayed;
    }

    public Boolean getFirstLastDisplayed()
    {
	return this.firstLastDisplayed;
    }

    public void setFirstLastDisplayed(Boolean firstLastDisplayed)
    {
	this.firstLastDisplayed = firstLastDisplayed;
    }

    public String getGradeBookSettingBbId()
    {
	return this.gradeBookSettingBbId;
    }

    public void setGradeBookSettingBbId(String gradeBookSettingBbId)
    {
	this.gradeBookSettingBbId = gradeBookSettingBbId;
    }

    public Boolean getLastFirstDisplayed()
    {
	return this.lastFirstDisplayed;
    }

    public void setLastFirstDisplayed(Boolean lastFirstDisplayed)
    {
	this.lastFirstDisplayed = lastFirstDisplayed;
    }

    public Boolean getStudentIdDisplayed()
    {
	return this.studentIdDisplayed;
    }

    public void setStudentIdDisplayed(Boolean studentIdDisplayed)
    {
	this.studentIdDisplayed = studentIdDisplayed;
    }

    public Boolean getUserIdDisplayed()
    {
	return this.userIdDisplayed;
    }

    public void setUserIdDisplayed(Boolean userIdDisplayed)
    {
	this.userIdDisplayed = userIdDisplayed;
    }

    public String getWeightType()
    {
	return this.weightType;
    }

    public void setWeightType(String weightType)
    {
	this.weightType = weightType;
    }

}
