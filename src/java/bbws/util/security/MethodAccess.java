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

package bbws.util.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Now uses reflection to retrieve webmethods, you just "don't" need to use this.
 * @deprecated Use WSMethods instead
 */
@Deprecated
public class MethodAccess
{
    @Deprecated
    private List<String> methods;

    @Deprecated
    public MethodAccess()
    {
        this.methods = new ArrayList<String>();
        this.methods.add("bbAnnouncementCreate");
        this.methods.add("bbAnnouncementDelete");
        this.methods.add("bbAnnouncementReadByAvailableAnnouncementAndUserId");
        this.methods.add("bbAnnouncementReadByCourseId");
        this.methods.add("bbAnnouncementUpdate");
        this.methods.add("bbCalendarEntryCreate");
        this.methods.add("bbCalendarEntryDelete");
        this.methods.add("bbCalendarEntryRead");
        this.methods.add("bbCalendarEntryReadAllForGivenCourse");
        this.methods.add("bbCalendarEntryReadAllForGivenCourseAndUserWithinDates");
        this.methods.add("bbCalendarEntryReadAllForGivenCourseWithinDates");
        this.methods.add("bbCalendarEntryReadAllForGivenType");
        this.methods.add("bbCalendarEntryReadAllForGivenTypeWithinDates");
        this.methods.add("bbCalendarEntryReadAllForGivenUser");
        this.methods.add("bbCalendarEntryReadAllForGivenUserWithinDates");
        this.methods.add("bbCalendarEntryReadAllPersonalForGivenUserWithinDates");
        this.methods.add("bbCourseCreate");
        this.methods.add("bbCourseDelete");
        this.methods.add("bbCourseMembershipCreate");
        this.methods.add("bbCourseMembershipDelete");
        this.methods.add("bbCourseMembershipRead");
        this.methods.add("bbCourseMembershipReadByCourseId");
        this.methods.add("bbCourseMembershipReadByUserIdAndCourseId");
        this.methods.add("bbCourseQuotaRead");
        this.methods.add("bbCourseQuotaUpdate");
        this.methods.add("bbCourseRead");
        this.methods.add("bbCourseReadAll");
        this.methods.add("bbCourseReadSearchByRegex");
        this.methods.add("bbCourseReadByUserIdAndCMRole");
        this.methods.add("bbEnrollmentReadByUserId");
        this.methods.add("bbGradeCentreAttemptDelete");
        this.methods.add("bbGradeCentreAttemptDetailRead");
        this.methods.add("bbGradeCentreAttemptDetailReadLastAttempByGradeDetail");
        this.methods.add("bbGradeCentreAttemptReadByOutcomeDefinitionId");
        this.methods.add("bbGradeCentreAttemptReadByOutcomeId");
        this.methods.add("bbGradeCentreGradableItemRead");
        this.methods.add("bbGradeCentreGradableItemReadByCourseId");
        this.methods.add("bbGradeCentreGradeDetailRead");
        this.methods.add("bbGradeCentreGradeDetailReadByGradableItem");
        this.methods.add("bbGradeCentreGradeDetailReadByGradableItemAndCourseMembership");
        this.methods.add("bbGradeCentreGradeDetailReadByGradableItemAndUserId");
        //this.methods.add("bbGradeCentreGradeDetailReadByGradableItemIdAndUserId");
        this.methods.add("bbGradeCentreGradingSchemaReadByCourseId");
        this.methods.add("bbGradeCentreLineitemAdd");
        this.methods.add("bbGradeCentreLineitemDelete");
        this.methods.add("bbGradeCentreLineitemRead");
        this.methods.add("bbGradeCentreLineitemReadByCourseId");
        this.methods.add("bbGradeCentreOutcomeDefinitionDelete");
        this.methods.add("bbGradeCentreOutcomeDefinitionRead");
        this.methods.add("bbGradeCentreOutcomeDefinitionReadByCourseId");
        this.methods.add("bbGradeCentreOutcomeRead");
        this.methods.add("bbGradeCentreOutcomeReadByOutcomeDefinitionId");
        this.methods.add("bbGradeCentreScoreRead");
        this.methods.add("bbGradeCentreScoreReadByLineitemId");
        this.methods.add("bbGradeCentreScoreReadByLineitemIdAndCourseMembershipId");
        this.methods.add("bbGradeCentreScoreReadByLineitemIdAndUserId");
        this.methods.add("bbGradeCentreSettingsRead");
        this.methods.add("bbGroupAdd");
        this.methods.add("bbGroupDelete");
        this.methods.add("bbGroupMembershipCreateByUserIdAndGroupId");
        this.methods.add("bbGroupMembershipDeleteByUserIdAndGroupId");
        this.methods.add("bbGroupMembershipReadByGroupId");
        this.methods.add("bbGroupRead");
        this.methods.add("bbGroupUpdate");
        this.methods.add("bbRoleSecondaryPortalReadByUserId");
        this.methods.add("bbRoleSecondaryPortalUpdate");
        this.methods.add("bbRoleSecondarySystemReadByUserId");
        this.methods.add("bbRoleUserReadByUserIdAndCourseId");
        this.methods.add("bbUserCreate");
        this.methods.add("bbUserDelete");
        this.methods.add("bbUserRead");
        this.methods.add("bbUserReadAll");
        this.methods.add("bbUserReadByCourseId");
        this.methods.add("bbUserReadByCourseIdAndCMRole");
        this.methods.add("bbUserUpdate");
    }

    @Deprecated
    public List<String> getMethods()
    {
        return this.methods;
    }

    @Deprecated
    public void setMethods(List<String> methods)
    {
        this.methods = methods;
    }

    @Deprecated
    public Iterator<String> getMethodsIterator()
    {
        return this.methods.iterator();
    }
}
