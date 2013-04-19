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

public enum BBCourseVerbosity
{
    /**
     * Makes methods returning {@link bbws.course.BBCourse} return objects with:</br>
     * courseId
     */
    minimal,
    /**
     * Makes methods returning {@link bbws.course.BBCourse} return objects with:</br>
     * (Everything in miminal)
     * courseBbId</br>
     * title</br>
     * description</br>
     * creationDate</br>
     * modifiedDate</br>
     * available
     */
    standard,
    /**
     * Makes methods returning {@link bbws.course.BBCourse} return objects with:</br>
     * (Everything in standard)</br>
     * absoluteLimit</br>
     * allowGuests</br>
     * allowObservers</br>
     * bannerImageFile</br>
     * batchUId</br>
     * buttonStyle</br>
     * cartridgeDescription</br>
     * classification</br>
     * durationType</br>
     * endDate</br>
     * enrollment</br>
     * institution</br>
     * localeEnforced</br>
     * lockedOut</br>
     * navigationCollapsible</br>
     * locale</br>
     * navigationBackgroundColour</br>
     * navigationForegroundColour</br>
     * navigationStyle</br>
     * numberOfDaysOfUse</br>
     * paceType</br>
     * serviceLevelType</br>
     * softLimit</br>
     * startDate</br>
     * uploadLimit
     */
    extended
}
