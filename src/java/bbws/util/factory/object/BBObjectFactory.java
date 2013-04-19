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
package bbws.util.factory.object;

//bbws
import bbws.util.factory.BBObjectsWithoutVerbosity;
import bbws.announcement.BBAnnouncement;
import bbws.calendar.BBCalendarEntry;
import bbws.course.BBEnrollment;
import bbws.gradecentre.column.BBGradableItem;
import bbws.gradecentre.grade.BBGradeDetail;
import bbws.gradecentre.grade.BBGradingSchema;
import bbws.gradecentre.outcome.BBOutcome;
import bbws.gradecentre.outcome.BBOutcomeDefinition;
import bbws.groups.BBGroup;
import bbws.groups.BBGroupMembership;

//blackboard - admin
import blackboard.admin.data.course.Enrollment;

//blackboard - data
import blackboard.data.announcement.Announcement;
import blackboard.data.calendar.CalendarEntry;
import blackboard.data.course.Group;
import blackboard.data.course.GroupMembership;
import blackboard.data.gradebook.impl.Outcome;
import blackboard.data.gradebook.impl.OutcomeDefinition;

//blackboard - platform
import blackboard.platform.gradebook2.GradableItem;
import blackboard.platform.gradebook2.GradeDetail;
import blackboard.platform.gradebook2.GradingSchema;

public class BBObjectFactory
{
    public static Object getBBObject(Object o, String type) throws Exception
    {
        switch(BBObjectsWithoutVerbosity.valueOfSafe(type))
        {
            case Announcement: return new BBAnnouncement((Announcement)o);
            case CalendarEntry: return new BBCalendarEntry((CalendarEntry)o);
            case Enrollment: return new BBEnrollment((Enrollment)o);
            case GradableItem: return new BBGradableItem((GradableItem)o);
            case GradeDetail: return new BBGradeDetail((GradeDetail)o);
            case GradingSchema: return new BBGradingSchema((GradingSchema)o);
            case Group: return new BBGroup((Group)o);
            case GroupMembership: return new BBGroupMembership((GroupMembership)o);
            case Outcome: return new BBOutcome((Outcome)o);
            case OutcomeDefinition: return new BBOutcomeDefinition((OutcomeDefinition)o);
            default: throw new Exception("List type '"+type+"' not found");
        }
    }
}
