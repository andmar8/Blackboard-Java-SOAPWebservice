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

package bbws.course.enums.verbosity;

public enum BBCourseMembershipVerbosity
{
    /**
     * Makes methods returning {@link bbws.course.coursemembership.BBCourseMembership} return objects with:</br>
     * courseMembershipBbId
     */
    minimal,
    /**
     * Makes methods returning {@link bbws.course.coursemembership.BBCourseMembership} return objects with:</br>
     * (Everything in Minimal)</br>
     * available</br>
     * cartridgeAccess</br>
     * courseBbId</br>
     * dataSourceBbId</br>
     * enrollmentDate</br>
     * introduction</br>
     * lastAccessDate</br>
     * modifiedDate</br>
     * notes</br>
     * personalInfo</br>
     * role</br>
     * userBbId</br>
     * user
     */
    standard
}
