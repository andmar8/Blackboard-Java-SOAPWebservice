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
package bbws.gradecentre.enums.verbosity;

public enum BBAttemptVerbosity
{
    /**
     * Makes methods returning {@link bbws.gradecentre.attempt.BBAttempt} or {@link bbws.gradecentre.attempt.BBAttemptDetail} return objects with:</br>
     * attemptBbId</br>
     * attemptedDate</br>
     * dateCreated</br>
     * dateModified</br>
     * (AttemptDetail Only) exempt</br>
     * (AttemptDetail Only) feedBackToUser</br>
     * (AttemptDetail Only) exempt</br>
     * (AttemptDetail Only) feedBackToUser</br>
     * (AttemptDetail Only) feedBackToUserHidden</br>
     * grade</br>
     * (AttemptDetail Only) gradeId</br>
     * (AttemptDetail Only) id</br>
     * (AttemptDetail Only) instructorNotes</br>
     * outcomeBbId</br>
     * score</br>
     * status
     */
    standard,
    /**
     * Makes methods returning {@link bbws.gradecentre.attempt.BBAttempt} or {@link bbws.gradecentre.attempt.BBAttemptDetail} return objects with:</br>
     * (Everything in standard)</br>
     * (AttemptDetail Only) attemptedDate</br>
     * (AttemptDetail Only) displayGrade</br>
     * (AttemptDetail Only) formattedAttemptedDate</br>
     * (AttemptDetail Only) formattedDateCreated</br>
     * instructorComments</br>
     * instructorNotes</br>
     * linkRefBbId</br>
     * publicComments</br>
     * resultObjectBbId</br>
     * studentComments</br>
     * (AttemptDetail Only) shortFeedBackToUser</br>
     * (AttemptDetail Only) statusKey
     */
    extended;
}