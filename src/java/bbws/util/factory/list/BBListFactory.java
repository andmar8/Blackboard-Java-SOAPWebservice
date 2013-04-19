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
package bbws.util.factory.list;

//bbws
import bbws.util.factory.object.BBObjectFactory;
import bbws.course.BBCourse;
import bbws.course.coursemembership.BBCourseMembership;
import bbws.course.enums.verbosity.BBCourseMembershipVerbosity;
import bbws.course.enums.verbosity.BBCourseVerbosity;
import bbws.util.exception.EmptyListException;
import bbws.gradecentre.attempt.BBAttempt;
import bbws.gradecentre.column.BBLineitem;
import bbws.gradecentre.enums.verbosity.BBAttemptVerbosity;
import bbws.gradecentre.grade.BBScore;
import bbws.user.BBUser;

//blackboard - data
import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.gradebook.impl.Attempt;
import blackboard.data.gradebook.Lineitem;
import blackboard.data.gradebook.Score;
import blackboard.data.user.User;

//java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BBListFactory
{
    private static void checkListSize(List l) throws EmptyListException
    {
        if(l==null || l.size()<1)
        {
            throw new EmptyListException();
        }
    }

    public static List getNonVerboseBBList(List l) throws EmptyListException, Exception
    {
        checkListSize(l);
        List rl = new ArrayList();
        Iterator i = l.iterator();
        String type = l.get(0).getClass().getSimpleName();
        while(i.hasNext())
        {
            rl.add(BBObjectFactory.getBBObject(i.next(),type));
        }
        return rl;
    }

    /** Change this so it's one method that gets Verbose BBLists, will probably need a "verbosity factory" ***/
    public static List<BBAttempt> getBBAttemptListFromList(List<Attempt> al, BBAttemptVerbosity verbosity) throws EmptyListException, Exception
    {
        checkListSize(al);
        List<BBAttempt> l = new ArrayList<BBAttempt>();
        Iterator<Attempt> i = al.iterator();
        while(i.hasNext())
        {
            l.add(new BBAttempt(i.next(),verbosity));
        }
        return l;
    }

    /** Change this so it's one method that gets Verbose BBLists, will probably need a "verbosity factory" ***/
    public static List<BBCourse> getBBCourseListFromList(List<Course> cl, BBCourseVerbosity verbosity) throws EmptyListException, Exception
    {
        checkListSize(cl);
        List<BBCourse> l = new ArrayList<BBCourse>();
        Iterator<Course> i = cl.iterator();
        while(i.hasNext())
        {
            l.add(new BBCourse(i.next(),verbosity));
        }
        return l;
    }

    /** Change this so it's one method that gets Verbose BBLists, will probably need a "verbosity factory" ***/
    public static List<BBCourseMembership> getBBCourseMembershipListFromList(List<CourseMembership> cml, BBCourseMembershipVerbosity verbosity) throws EmptyListException, Exception
    {
        checkListSize(cml);
        List<BBCourseMembership> l = new ArrayList<BBCourseMembership>();
        Iterator<CourseMembership> i = cml.iterator();
        while(i.hasNext())
        {
            l.add(new BBCourseMembership(i.next(),verbosity));
        }
        return l;
    }

    /** Change this so it's one method that gets Verbose BBLists, will probably need a "verbosity factory" ***/
    public static List<BBLineitem> getBBLineitemListFromList(List<Lineitem> lil, BBLineitem.BBLineitemVerbosity verbosity) throws EmptyListException, Exception
    {
        checkListSize(lil);
        List<BBLineitem> l = new ArrayList<BBLineitem>();
        Iterator<Lineitem> i = lil.iterator();
        while(i.hasNext())
        {
            l.add(new BBLineitem(i.next(),verbosity));
        }
        return l;
    }

    /** Change this so it's one method that gets Verbose BBLists, will probably need a "verbosity factory" ***/
    public static List<BBUser> getBBUserListFromList(List<User> ul, BBUser.BBUserVerbosity verbosity) throws EmptyListException, Exception
    {
        checkListSize(ul);
        List<BBUser> l = new ArrayList<BBUser>();
        Iterator<User> i = ul.iterator();
        while(i.hasNext())
        {
            l.add(new BBUser(i.next(),verbosity));
        }
        return l;
    }

    /** Change this so it's one method that gets Verbose BBLists, will probably need a "verbosity factory" ***/
    public static List<BBScore> getBBScoreListFromList(List<Score> sl, BBScore.BBScoreVerbosity verbosity) throws EmptyListException, Exception
    {
        checkListSize(sl);
        List<BBScore> l = new ArrayList<BBScore>();
        Iterator<Score> i = sl.iterator();
        while(i.hasNext())
        {
            l.add(new BBScore(i.next(),verbosity));
        }
        return l;
    }
}
