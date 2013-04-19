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

package bbws.course;

import blackboard.data.course.CourseQuota;

public class BBCourseQuota
{
    private Long courseAbsoluteLimit;
    private Long courseAbsoluteLimitRemainingSize;
    private Long courseSize;
    private Long courseSoftLimit;
    private Long courseUploadLimit;
    private Boolean enforceQuota;
    private Boolean enforceUploadLimit;
    private Long systemAbsoluteLimit;
    private Long systemSoftLimit;
    private Long systemUploadLimit;

    public BBCourseQuota(){}
    public BBCourseQuota(CourseQuota courseQuota)
    {
	this.courseAbsoluteLimit = courseQuota.getCourseAbsoluteLimit();
	this.courseAbsoluteLimitRemainingSize = courseQuota.getCourseAbsoluteLimitRemainingSize();
	this.courseSize = courseQuota.getCourseSize();
	this.courseSoftLimit = courseQuota.getCourseSoftLimit();
	this.courseUploadLimit = courseQuota.getCourseUploadLimit();
	this.enforceQuota = courseQuota.getEnforceQuota();
	this.enforceUploadLimit = courseQuota.getEnforceUploadLimit();
	this.systemAbsoluteLimit = courseQuota.getSystemAbsoluteLimit();
	this.systemSoftLimit = courseQuota.getSystemSoftLimit();
	this.systemUploadLimit = courseQuota.getSystemUploadLimit();
    }

    public Long getCourseAbsoluteLimit()
    {
	return this.courseAbsoluteLimit;
    }

    public void setCourseAbsoluteLimit(Long courseAbsoluteLimit)
    {
	this.courseAbsoluteLimit = courseAbsoluteLimit;
    }

    public Long getCourseAbsoluteLimitRemainingSize()
    {
	return this.courseAbsoluteLimitRemainingSize;
    }

    public void setCourseAbsoluteLimitRemainingSize(Long courseAbsoluteLimitRemainingSize)
    {
	this.courseAbsoluteLimitRemainingSize = courseAbsoluteLimitRemainingSize;
    }

    public Long getCourseSize()
    {
	return this.courseSize;
    }

    public void setCourseSize(Long courseSize)
    {
	this.courseSize = courseSize;
    }

    public Long getCourseSoftLimit()
    {
	return this.courseSoftLimit;
    }

    public void setCourseSoftLimit(Long courseSoftLimit)
    {
	this.courseSoftLimit = courseSoftLimit;
    }

    public Long getCourseUploadLimit()
    {
	return this.courseUploadLimit;
    }

    public void setCourseUploadLimit(Long courseUploadLimit)
    {
	this.courseUploadLimit = courseUploadLimit;
    }

    public Boolean getEnforceQuota()
    {
	return this.enforceQuota;
    }

    public void setEnforceQuota(Boolean enforceQuota)
    {
	this.enforceQuota = enforceQuota;
    }

    public Boolean getEnforceUploadLimit()
    {
	return this.enforceUploadLimit;
    }

    public void setEnforceUploadLimit(Boolean enforceUploadLimit)
    {
	this.enforceUploadLimit = enforceUploadLimit;
    }

    public Long getSystemAbsoluteLimit()
    {
	return this.systemAbsoluteLimit;
    }

    public void setSystemAbsoluteLimit(Long systemAbsoluteLimit)
    {
	this.systemAbsoluteLimit = systemAbsoluteLimit;
    }

    public Long getSystemSoftLimit()
    {
	return this.systemSoftLimit;
    }

    public void setSystemSoftLimit(Long systemSoftLimit)
    {
	this.systemSoftLimit = systemSoftLimit;
    }

    public Long getSystemUploadLimit()
    {
	return this.systemUploadLimit;
    }

    public void setSystemUploadLimit(Long systemUploadLimit)
    {
	this.systemUploadLimit = systemUploadLimit;
    }
}
