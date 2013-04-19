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

package bbws.content;

import blackboard.data.navigation.CourseToc;

public class BBCourseToc extends AbstractBBContent
{
    private Boolean entryPoint;
    private String internalHandle;
    private String label;
    private String targetType;

    public BBCourseToc(){}
    public BBCourseToc(CourseToc ct)
    {
	this.available = ct.getIsEnabled();
	this.contentBbId = ct.getContentId().toExternalString();
	this.dataType = ct.getDataType().toString();
	this.entryPoint = ct.getIsEntryPoint();
	this.internalHandle = ct.getInternalHandle();
	this.label = ct.getLabel();
	this.position = ct.getPosition();
	this.targetType = ct.getTargetType().toFieldName();
	this.url = ct.getUrl();
    }

    public Boolean getEntryPoint()
    {
	return this.entryPoint;
    }

    public void setEntryPoint(Boolean entryPoint)
    {
	this.entryPoint = entryPoint;
    }

    public String getInternalHandle()
    {
	return this.internalHandle;
    }

    public void setInternalHandle(String internalHandle)
    {
	this.internalHandle = internalHandle;
    }

    public String getLabel()
    {
	return this.label;
    }

    public void setLabel(String label)
    {
	this.label = label;
    }

    public String getTargetType()
    {
	return this.targetType;
    }

    public void setTargetType(String targetType)
    {
	this.targetType = targetType;
    }

    private String[] getCourseTocDetails()
    {
	return new String[]{this.contentBbId,
			    this.dataType,
			    this.internalHandle,
			    Boolean.toString(this.available),
			    Boolean.toString(this.entryPoint),
			    this.label,
			    Integer.toString(this.position),
			    this.targetType,
			    this.url
	};
    }

}
