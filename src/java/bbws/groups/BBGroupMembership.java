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
package bbws.groups;

import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.data.course.GroupMembership;

public class BBGroupMembership
{
    private String userId;

    public BBGroupMembership(){}
    public BBGroupMembership(GroupMembership gm) throws Exception
    {
        this.userId = CourseMembershipDbLoader.Default.getInstance().loadById(gm.getCourseMembershipId(),null,true).getUser().getUserName();
    }

    public String getUserId()
    {
        return this.userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

}
