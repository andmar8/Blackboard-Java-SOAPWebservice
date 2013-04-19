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
package bbws;

/******** Documented API ********/

//bbws
import bbws.entity.enums.verbosity.BBCourseMembershipVerbosity;
import bbws.entity.enums.verbosity.BBCourseVerbosity;
import bbws.entity.enums.verbosity.BBLineitemVerbosity;
import bbws.entity.enums.verbosity.BBScoreVerbosity;
import bbws.entity.enums.verbosity.BBUserVerbosity;
import bbws.resource.announcement.BBAnnouncement;
import bbws.resource.calendar.BBCalendarEntry;
import bbws.resource.course.BBCourse;
import bbws.resource.course.BBCourseQuota;
import bbws.resource.coursemembership.BBCourseMembership;
import bbws.resource.coursemembership.BBCourseMembershipRole;
import bbws.resource.course.BBEnrollment;
import bbws.resource.gradecentre.attempt.BBAttempt;
import bbws.resource.gradecentre.attempt.BBAttemptDetail;
//Changed 9.1.40071.3 on
import bbws.resource.gradecentre.BBGradeCentreSettings;
import bbws.resource.gradecentre.grade.BBGradeDetail;
import bbws.resource.gradecentre.column.BBLineitem;
import bbws.entity.enums.verbosity.BBAttemptVerbosity;
import bbws.resource.gradecentre.column.BBGradableItem;
import bbws.resource.gradecentre.grade.BBGradingSchema;
import bbws.resource.gradecentre.grade.BBScore;
import bbws.resource.gradecentre.outcome.BBOutcome;
import bbws.resource.gradecentre.outcome.BBOutcomeDefinition;
import bbws.resource.groups.BBGroup;
import bbws.resource.groups.BBGroupMembership;
import bbws.util.Util;
import bbws.util.helper.AnnouncementHelper;
import bbws.util.helper.CalendarHelper;
import bbws.util.helper.CourseHelper;
import bbws.util.helper.CourseMembershipHelper;
import bbws.util.helper.GradeCentreHelper;
import bbws.util.helper.GroupHelper;
import bbws.util.helper.RoleHelper;
import bbws.util.helper.UserHelper;
import bbws.util.security.WSSecurityUtil;
import bbws.util.security.impl.WSSecurityUtilImpl;
import bbws.resource.user.BBUser;
import bbws.resource.user.BBRole;

//java
import java.net.URI;
import java.util.List;

//javax
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceContext;

/**
 * <p>When using OAuth, you need to state a request "method" for each web method
 * used. This is quite simple to work out...
 * </p>
 * <p>
 * Create methods = POST<br/>
 * Delete methods = DELETE<br/>
 * Read methods = GET<br/>
 * Update methods = PUT<br/>
 * </p>
 *
 */
@WebService(name="BBWebService", serviceName="BBWebService", targetNamespace="")
public class BBWebService
{
    @Resource
    private WebServiceContext wsContext;

    private void doSecurity(String magKey,String methodName,String requestMethod) throws WebServiceException
    {
        try
        {
            URI baseuri = new URI(((HttpServletRequest)wsContext.getMessageContext().get(MessageContext.SERVLET_REQUEST)).getRequestURL().toString());
            String auth = magKey;
            if(auth==null||auth.matches("")) //If we're not using mag keys try oauth
            {
                auth = WSSecurityUtilImpl.getAuthorizationHeader(wsContext);
            }
            WSSecurityUtil wsSecurity = new WSSecurityUtilImpl(60,"amnl","BBWebService");
            wsSecurity.authnAndAuthzRequest(baseuri,auth,methodName,requestMethod,methodName);
        }
        catch(Exception e)
        {
            throw new WebServiceException("Access Denied");
        }
    }

    /**
     * Not specifying a course will lead to a system announcement
     * @param magKey The key assigned to the method access group
     * @param announcement
     * @param course
     * @param textType
     * @return Boolean - whether the announcement was created or not
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbAnnouncementCreate(@WebParam(name = "magKey") String magKey, @WebParam(name = "announcement") BBAnnouncement announcement, @WebParam(name = "course") BBCourse course, @WebParam(name = "textType") String textType) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"POST");
        return AnnouncementHelper.announcementCreate(announcement,course,textType);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param announcement
     * @return Boolean - whether the announcement was deleted or not
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbAnnouncementDelete(@WebParam(name = "magKey") String magKey, @WebParam(name = "announcement") BBAnnouncement announcement) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"DELETE");
        return AnnouncementHelper.announcementDelete(announcement);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @return List of BBAnnouncements
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBAnnouncement> bbAnnouncementReadByAvailableAnnouncementAndUserId(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return AnnouncementHelper.announcementReadByAvailableAnnouncementAndUserId(user);
    }

    /**
     * System announcement can be retrieved by specifying "SYSTEM" as a courseId
     * @param magKey The key assigned to the method access group
     * @param course
     * @return List of BBAnnouncements
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBAnnouncement> bbAnnouncementReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return AnnouncementHelper.announcementReadByCourseId(course);
    }

    /**
     * Not specifying a course will lead to a system announcement
     * @param magKey The key assigned to the method access group
     * @param announcement
     * @param course
     * @param textType
     * @return Boolean - whether the announcement was updated or not
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbAnnouncementUpdate")
    public boolean bbAnnouncementUpdate(@WebParam(name = "magKey") String magKey, @WebParam(name = "announcement") BBAnnouncement announcement, @WebParam(name = "course") BBCourse course, @WebParam(name = "textType") String textType) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"PUT");
        return AnnouncementHelper.announcementUpdate(announcement,course,textType);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param ce
     * @param ceType
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryCreate")
    public boolean bbCalendarEntryCreate(@WebParam(name = "magKey") String magKey, @WebParam(name = "calendarEntry") BBCalendarEntry ce, @WebParam(name = "calendarEntryType") BBCalendarEntry.BBCalendarEntryType ceType) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"POST");
        return CalendarHelper.calendarEntryCreate(ce,ceType);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param ce
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryDelete")
    public boolean bbCalendarEntryDelete(@WebParam(name = "magKey") String magKey, @WebParam(name = "calendarEntry") BBCalendarEntry ce) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"DELETE");
        return CalendarHelper.calendarEntryDelete(ce);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param ce
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryRead")
    public BBCalendarEntry bbCalendarEntryRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "calendarEntry") BBCalendarEntry ce) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CalendarHelper.calendarEntryRead(ce);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param c
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryReadAllForGivenCourse")
    public List<BBCalendarEntry> bbCalendarEntryReadAllForGivenCourse(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse c) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CalendarHelper.calendarEntryReadAllForGivenCourse(c);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param c
     * @param u
     * @param start
     * @param end
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryReadAllForGivenCourseAndUserWithinDates")
    public List<BBCalendarEntry> bbCalendarEntryReadAllForGivenCourseAndUserWithinDates(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse c, @WebParam(name = "user") BBUser u, @WebParam(name = "startDateTime") String start, @WebParam(name = "endDateTime") String end) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CalendarHelper.calendarEntryReadAllForGivenCourseAndUserWithinDates(c,u,start,end);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param c
     * @param start
     * @param end
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryReadAllForGivenCourseWithinDates")
    public List<BBCalendarEntry> bbCalendarEntryReadAllForGivenCourseWithinDates(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse c, @WebParam(name = "startDateTime") String start, @WebParam(name = "endDateTime") String end) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CalendarHelper.calendarEntryReadAllForGivenCourseWithinDates(c,start,end);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param ce
     * @param ceType
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryReadAllForGivenType")
    public List<BBCalendarEntry> bbCalendarEntryReadAllForGivenType(@WebParam(name = "magKey") String magKey, @WebParam(name = "calendarEntry") BBCalendarEntry ce, @WebParam(name = "calendarEntryType") BBCalendarEntry.BBCalendarEntryType ceType) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CalendarHelper.calendarEntryReadAllForGivenType(ceType);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param ceType
     * @param c
     * @param start
     * @param end
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryReadAllForGivenTypeWithinDates")
    public List<BBCalendarEntry> bbCalendarEntryReadAllForGivenTypeWithinDates(@WebParam(name = "magKey") String magKey, @WebParam(name = "calendarEntryType") BBCalendarEntry.BBCalendarEntryType ceType, @WebParam(name = "course") BBCourse c, @WebParam(name = "startDateTime") String start, @WebParam(name = "endDateTime") String end) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CalendarHelper.calendarEntryReadAllForGivenTypeWithinDates(ceType,start,end);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param u
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryReadAllForGivenUser")
    public List<BBCalendarEntry> bbCalendarEntryReadAllForGivenUser(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser u) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CalendarHelper.calendarEntryReadAllForGivenUser(u);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param u
     * @param start
     * @param end
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryReadAllForGivenUserWithinDates")
    public List<BBCalendarEntry> bbCalendarEntryReadAllForGivenUserWithinDates(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser u, @WebParam(name = "startDateTime") String start, @WebParam(name = "endDateTime") String end) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CalendarHelper.calendarEntryReadAllForGivenUserWithinDates(u,start,end);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param u
     * @param start
     * @param end
     * @return
     * @throws WebServiceException
     */
    @WebMethod(operationName = "bbCalendarEntryReadAllPersonalForGivenUserWithinDates")
    public List<BBCalendarEntry> bbCalendarEntryReadAllPersonalForGivenUserWithinDates(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser u, @WebParam(name = "startDateTime") String start, @WebParam(name = "endDateTime") String end) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CalendarHelper.calendarEntryReadAllPersonalForGivenUserWithinDates(u,start,end);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbCourseCreate(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"POST");
        return CourseHelper.courseCreate(course);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbCourseDelete(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"DELETE");
        return CourseHelper.courseDelete(course);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param courseMembership
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbCourseMembershipCreate(@WebParam(name = "magKey") String magKey, @WebParam(name = "courseMembership") BBCourseMembership courseMembership) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"POST");
        return CourseMembershipHelper.courseMembershipCreate(courseMembership);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param courseMembership
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbCourseMembershipDelete(@WebParam(name = "magKey") String magKey, @WebParam(name = "courseMembership") BBCourseMembership courseMembership) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"DELETE");
        return CourseMembershipHelper.courseMembershipDelete(courseMembership);
    }

    /**
     * Returns a fully populated {@link BBCourseMembership} for the specified user Id in the {@link BBUser} and course Id in the
     * {@link BBCourse} or the specified courseMembership Id, to the verbosity specified by the {@link BBCourseMembershipVerbosity}
     * and optionally loads the user object for the course membership if you specify loadUser as true
     * @param magKey The key assigned to the method access group
     * @param courseMembership
     * @param verbosity Used to determine how much detail is returned in each object
     * @param loadUser
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBCourseMembership bbCourseMembershipRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "courseMembership") BBCourseMembership courseMembership, @WebParam(name = "verbosity") BBCourseMembershipVerbosity verbosity, @WebParam(name = "loadUser") Boolean loadUser) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CourseMembershipHelper.courseMembershipRead(courseMembership,verbosity,loadUser);
    }

    /**
     * Returns a List of {@link BBCourseMembership}s for the specified course Id in the given {@link BBCourse}
     * to the level of detail as specified in {@link BBCourseMembershipVerbosity}, you can also specify whether
     * you would like to load the {@link BBUser} objects for each returned {@link BBCourseMembership} by setting
     * loadUser to true
     * @param magKey The key assigned to the method access group
     * @param course
     * @param verbosity Used to determine how much detail is returned in each object
     * @param loadUser
     * @return
     */
    @WebMethod
    public List<BBCourseMembership> bbCourseMembershipReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "verbosity") BBCourseMembershipVerbosity verbosity, @WebParam(name = "loadUser") Boolean loadUser)
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CourseMembershipHelper.courseMembershipReadByCourseId(course,verbosity,loadUser);
    }

    /**
     * Returns a {@link BBCourseMembership} for the specified course Id in the given {@link BBCourse} and specified
     * user Id in the given {@link BBUser} to the level of detail as specified in {@link BBCourseMembershipVerbosity},
     * you can also specify whether you would like to load the {@link BBUser} objects for each returned
     * {@link BBCourseMembership} by setting loadUser to true
     * @param magKey The key assigned to the method access group
     * @param course
     * @param user
     * @param verbosity Used to determine how much detail is returned in each object
     * @param loadUser
     * @return
     */
    @WebMethod
    public BBCourseMembership bbCourseMembershipReadByUserIdAndCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "user") BBUser user, @WebParam(name = "verbosity") BBCourseMembershipVerbosity verbosity, @WebParam(name = "loadUser") Boolean loadUser)
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CourseMembershipHelper.courseMembershipReadByUserIdAndCourseId(user,course,verbosity,loadUser);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBCourseQuota bbCourseQuotaRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CourseHelper.courseQuotaRead(course);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Updates a {@link BBCourseQuota} for the given {@link BBCourse}
     * @param magKey The key assigned to the method access group
     * @param course
     * @param courseQuota
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbCourseQuotaUpdate(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "courseQuota") BBCourseQuota courseQuota) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"PUT");
        return CourseHelper.courseQuotaUpdate(course,courseQuota);
    }

    /**
     * Returns a {@link BBCourse} based on the Id or bbID of the specified {@link BBCourse}, to the level of detail specified by {@link BBCourseVerbosity}
     * @param magKey The key assigned to the method access group
     * @param course
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBCourse bbCourseRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "verbosity") BBCourseVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CourseHelper.courseRead(course,verbosity);
    }

    /**
     * Returns a List of {@link BBCourse}s in the current system to the level of detail specified by {@link BBCourseVerbosity}
     * @param magKey The key assigned to the method access group
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBCourse> bbCourseReadAll(@WebParam(name = "magKey") String magKey, @WebParam(name = "verbosity") BBCourseVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CourseHelper.courseReadAll(verbosity);
    }

    /**
     * Returns a List of {@link BBCourse}s based on the specified regular expression string, to the specified {@link BBCourseVerbosity}
     * @param magKey The key assigned to the method access group
     * @param regex
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBCourse> bbCourseReadSearchByRegex(@WebParam(name = "magKey") String magKey, @WebParam(name = "regex") String regex, @WebParam(name = "verbosity") BBCourseVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CourseHelper.courseReadSearchByRegex(regex, verbosity);
    }

    /**
     * Returns a List of {@link BBCourse}s based on the specified {@link BBUser}'s Id and {@link BBCourseMembershipRole} to the specified {@link BBCourseVerbosity}
     * @param magKey The key assigned to the method access group
     * @param user
     * @param cmRole
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBCourse> bbCourseReadByUserIdAndCMRole(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user, @WebParam(name = "") BBCourseMembershipRole cmRole, @WebParam(name = "verbosity") BBCourseVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CourseHelper.courseReadByUserIdAndCMRole(user,cmRole,verbosity);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBEnrollment> bbEnrollmentReadByUserId(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return CourseMembershipHelper.enrollmentReadByUserId(user);
    }

    //Changed 9.1.40071.3 on
    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Deletes the given {@link BBAttempt}
     * @param magKey The key assigned to the method access group
     * @param attempt
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public boolean bbGradeCentreAttemptDelete(@WebParam(name = "magKey") String magKey, @WebParam(name = "attempt") BBAttempt attempt) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"DELETE");
        return GradeCentreHelper.gradeCentreAttemptDelete(attempt);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Return a populated {@link BBAttemptDetail} for a given {@link BBAttemptDetail} Id to the specified verbosity in {@link BBAttemptVerbosity}
     * @param magKey The key assigned to the method access group
     * @param attemptDetail
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBAttemptDetail bbGradeCentreAttemptDetailRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "attemptDetail") BBAttemptDetail attemptDetail, @WebParam(name = "verbosity") BBAttemptVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreAttemptDetailRead(attemptDetail,verbosity);
    }

    //@WebMethod
    //public BBAttemptDetail gradeCentreAttemptDetailReadLastAttempByGrade() throws WebServiceException
    //{
    //
    //}

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Return the latest {@link BBAttemptDetail} for a given {@link BBGradeDetail}
     * @param magKey The key assigned to the method access group
     * @param gradeDetail
     * @param verbosity
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBAttemptDetail bbGradeCentreAttemptDetailReadLastAttempByGradeDetail(@WebParam(name = "magKey") String magKey, @WebParam(name = "gradeDetail") BBGradeDetail gradeDetail, @WebParam(name = "verbosity") BBAttemptVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreAttemptDetailReadLastAttempByGradeDetail(gradeDetail,verbosity);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Gets a List of {@link BBAttempt}s based on the given {@link BBOutcomeDefinition}, with a verbosity level as requested using {@link BBAttemptVerbosity}
     * @param magKey The key assigned to the method access group
     * @param outcomeDef
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBAttempt> bbGradeCentreAttemptReadByOutcomeDefinitionId(@WebParam(name = "magKey") String magKey, @WebParam(name = "outcomeDefinition") BBOutcomeDefinition outcomeDef, @WebParam(name = "verbosity") BBAttemptVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreAttemptReadByOutcomeDefinitionId(outcomeDef,verbosity);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Get a list of {@link BBAttempt}s based on outcome id specified in the given {@link BBOutcome}, with the verbosity level for the given {@link bbws.gradecentre.enums.verbosity.BBAttemptVerbosity}
     * @param magKey The key assigned to the method access group
     * @param outcome
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBAttempt> bbGradeCentreAttemptReadByOutcomeId(@WebParam(name = "magKey") String magKey, @WebParam(name = "outcome") BBOutcome outcome, @WebParam(name = "verbosity") BBAttemptVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreAttemptReadByOutcomeId(outcome,verbosity);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a populated {@link BBGradableItem} based on the Id set inside the given {@link BBGradableItem}
     * @param magKey The key assigned to the method access group
     * @param gradableItem
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBGradableItem bbGradeCentreGradableItemRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "gradableItem") BBGradableItem gradableItem) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreGradableItemRead(gradableItem);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a List of {@link BBGradableItem}s based on a course Id set inside the given {@link BBCourse}
     * @param magKey The key assigned to the method access group
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBGradableItem> bbGradeCentreGradableItemReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreGradableItemReadByCourseId(course);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a populated {@link BBGradeDetail} object for the id set in the given {@link BBGradeDetail}
     * @param magKey The key assigned to the method access group
     * @param gradeDetail
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBGradeDetail bbGradeCentreGradeDetailRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "gradeDetail") BBGradeDetail gradeDetail) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreGradeDetailRead(gradeDetail);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a List of {@link BBGradeDetail}s for a given {@link BBGradableItem}
     * @param magKey The key assigned to the method access group
     * @param gradableItem
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBGradeDetail> bbGradeCentreGradeDetailReadByGradableItem(@WebParam(name = "magKey") String magKey, @WebParam(name = "gradableItem") BBGradableItem gradableItem) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreGradeDetailReadByGradableItem(gradableItem);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a {@link BBGradeDetail} for the given {@link BBGradableItem} filtered by
     * the user belonging to the given {@link BBCourseMembership}
     * @param magKey The key assigned to the method access group
     * @param gradableItem
     * @param courseMembership
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBGradeDetail bbGradeCentreGradeDetailReadByGradableItemAndCourseMembership(@WebParam(name = "magKey") String magKey, @WebParam(name = "gradableItem") BBGradableItem gradableItem, @WebParam(name = "courseMembership") BBCourseMembership courseMembership) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreGradeDetailReadByGradableItemAndCourseMembership(gradableItem,courseMembership);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a {@link BBGradeDetail} for the given {@link BBGradableItem} filtered by
     * {@link BBUser}'s userBbId and the BBGradableItem's courseBbId
     * @param magKey The key assigned to the method access group
     * @param gradableItem
     * @param user
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBGradeDetail bbGradeCentreGradeDetailReadByGradableItemAndUserId(@WebParam(name = "magKey") String magKey, @WebParam(name = "gradableItem") BBGradableItem gradableItem,@WebParam(name = "user") BBUser user) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreGradeDetailReadByGradableItemAndUserId(gradableItem,user);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * @param magKey The key assigned to the method access group
     * @param gradableItem
     * @param user
     * @return
     * @throws WebServiceException
     */
    /*@WebMethod
    public BBGradeDetail bbGradeCentreGradeDetailReadByGradableItemIdAndUserId(@WebParam(name = "magKey") String magKey, @WebParam(name = "gradableitem") BBGradableItem gradableItem, @WebParam(name = "user") BBUser user) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreGradeDetailReadByGradableItemIdAndUserId(gradableItem,user);
    }*/

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a List of {@link BBGradingSchema}s based on a course Id set inside the given {@link BBCourse}
     * @param magKey The key assigned to the method access group
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBGradingSchema> bbGradeCentreGradingSchemaReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreGradingSchemaReadByCourseId(course);
    }

    /**
     * Adds a {@link BBLineitem} to the gradecentre in the given {@link bbws.course.BBCourse}
     * @param magKey The key assigned to the method access group
     * @param lineitem
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public boolean bbGradeCentreLineitemAdd(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"POST");
        return GradeCentreHelper.gradeCentreLineitemAdd(lineitem,course);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param lineitem
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbGradeCentreLineitemDelete(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"DELTE");
        return GradeCentreHelper.gradeCentreLineitemOrOutcomeDefinitionDelete(lineitem.getLineItemBbId());
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param lineitem
     * @param verbosity
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBLineitem bbGradeCentreLineitemRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem, @WebParam(name = "verbosity") BBLineitemVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreLineitemRead(lineitem,verbosity);
    }

    /**
     * Return a List of {@link BBLineitem}s for the courseId specified inside the given {@link BBCourse}
     * @param magKey The key assigned to the method access group
     * @param course
     * @param verbosity
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBLineitem> bbGradeCentreLineitemReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "verbosity")BBLineitemVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreLineitemReadByCourseId(course,verbosity);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Deletes an {@link BBOutcomeDefinition}
     * @param magKey The key assigned to the method access group
     * @param outcomeDef
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbGradeCentreOutcomeDefinitionDelete(@WebParam(name = "magKey") String magKey, @WebParam(name = "outcomeDef") BBOutcomeDefinition outcomeDef) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"DELETE");
        return GradeCentreHelper.gradeCentreLineitemOrOutcomeDefinitionDelete(outcomeDef.getOutcomeDefinitionBbId());
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a fully populated {@link BBOutcomeDefinition} for the id specified in the given {@link BBOutcomeDefinition}
     * @param magKey The key assigned to the method access group
     * @param outcomeDef
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBOutcomeDefinition bbGradeCentreOutcomeDefinitionRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "outcomeDef") BBOutcomeDefinition outcomeDef) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreOutcomeDefinitionRead(outcomeDef);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a List of {@link BBOutcomeDefinition}s for the given {@link bbws.course.BBCourse}
     * @param magKey The key assigned to the method access group
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBOutcomeDefinition> bbGradeCentreOutcomeDefinitionReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreOutcomeDefinitionReadByCourseId(course);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a fully populated {@link BBOutcome} for the id specified in the given {@link BBOutcome}
     * @param magKey The key assigned to the method access group
     * @param outcome
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBOutcome bbGradeCentreOutcomeRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "outcome") BBOutcome outcome) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreOutcomeRead(outcome);
    }

    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a List of {@link BBOutcome}s for the outcomeDefinition id as specified in the given {@link BBOutcomeDefinition}
     * @param magKey The key assigned to the method access group
     * @param outcomeDef
     * @return List<BBOutcome>
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBOutcome> bbGradeCentreOutcomeReadByOutcomeDefinitionId(@WebParam(name = "magKey") String magKey, @WebParam(name = "outcomeDef") BBOutcomeDefinition outcomeDef) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreOutcomeReadByOutcomeDefinitionId(outcomeDef);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param score
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBScore bbGradeCentreScoreRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "score") BBScore score, @WebParam(name = "verbosity") BBScoreVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreScoreRead(score,verbosity);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param lineitem
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBScore> bbGradeCentreScoreReadByLineitemId(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem, @WebParam(name = "verbosity") BBScoreVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreScoreReadByLineitemId(lineitem,verbosity);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param lineitem
     * @param courseMembership
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBScore bbGradeCentreScoreReadByLineitemIdAndCourseMembershipId(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem, @WebParam(name = "courseMembership") BBCourseMembership courseMembership, @WebParam(name = "verbosity") BBScoreVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreScoreReadByLineitemIdAndCourseMembershipId(lineitem,courseMembership,verbosity);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param lineitem
     * @param user
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBScore bbGradeCentreScoreReadByLineitemIdAndUserId(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem, @WebParam(name = "user") BBUser user, @WebParam(name = "verbosity") BBScoreVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreScoreReadByLineitemIdAndUserId(lineitem,user,verbosity);
    }

     //Changed 9.1.40071.3 on
    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a {@link BBGradeCentreSettings} object which contains settings for the gradecentre for the given {@link bbws.course.BBCourse}
     * @param magKey The key assigned to the method access group
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBGradeCentreSettings bbGradeCentreSettingsRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GradeCentreHelper.gradeCentreSettingsRead(course);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param group
     * @param course
     * @param descriptionTextType
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbGroupAdd(@WebParam(name = "magKey") String magKey, @WebParam(name = "group") BBGroup group, @WebParam(name = "course") BBCourse course, @WebParam(name = "") String descriptionTextType) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"POST");
        return GroupHelper.groupAdd(group,course,descriptionTextType);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param group
     * @param course
     * @param descriptionTextType
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbGroupDelete(@WebParam(name = "magKey") String magKey, @WebParam(name = "group") BBGroup group, @WebParam(name = "course") BBCourse course, @WebParam(name = "") String descriptionTextType) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"DELETE");
        return GroupHelper.groupDelete(group);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param group
     * @return BBGroup
     * @throws WebServiceException
     */
    @WebMethod
    public BBGroup bbGroupRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "group") BBGroup group) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GroupHelper.groupRead(group);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param group
     * @param descriptionTextType
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public boolean bbGroupUpdate(@WebParam(name = "magKey") String magKey, @WebParam(name = "group") BBGroup group, @WebParam(name = "descriptionTextType") String descriptionTextType) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"PUT");
        return GroupHelper.groupUpdate(group, descriptionTextType);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBGroup> bbGroupReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GroupHelper.groupReadByCourseId(course);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param group
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBGroupMembership> bbGroupMembershipReadByGroupId(@WebParam(name = "magKey") String magKey, @WebParam(name = "group") BBGroup group) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return GroupHelper.groupMembershipReadByGroupId(group);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @param group
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public boolean bbGroupMembershipCreateByUserIdAndGroupId(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user, @WebParam(name = "group") BBGroup group) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"POST");
        return GroupHelper.groupMembershipCreateByUserIdAndGroupId(user,group);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @param group
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public boolean bbGroupMembershipDeleteByUserIdAndGroupId(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user, @WebParam(name = "group") BBGroup group) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"DELETE");
        return GroupHelper.groupMembershipDeleteByUserIdAndGroupId(user,group);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBRole> bbRoleSecondaryPortalReadByUserId(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return RoleHelper.roleSecondaryPortalReadByUserId(user);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @param roles
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbRoleSecondaryPortalUpdate(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user, @WebParam(name = "roles") List<BBRole> roles) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"PUT");
        return RoleHelper.roleSecondaryPortalUpdate(user,roles);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBRole> bbRoleSecondarySystemReadByUserId(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return RoleHelper.roleSecondarySystemReadByUserId(user);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @param course
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBRole bbRoleUserReadByUserIdAndCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user, @WebParam(name = "course") BBCourse course) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return RoleHelper.roleUserReadByUserIdAndCourseId(user,course);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @param portalRole
     * @param secondaryPortalRoles
     * @param systemRole
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbUserCreate(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user, @WebParam(name = "portalRole") BBRole portalRole, @WebParam(name = "secondaryPortalRoles") List<BBRole> secondaryPortalRoles, @WebParam(name = "systemRole") BBRole systemRole) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"POST");
        return UserHelper.userCreateOrUpdate(user,portalRole,secondaryPortalRoles,systemRole,false);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @return
     */
    @WebMethod
    public Boolean bbUserDelete(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user)
    {
        doSecurity(magKey,Util.getMethodName(),"DELETE");
        return UserHelper.userDelete(user);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public BBUser bbUserRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user, @WebParam(name = "verbosity") BBUserVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return UserHelper.userRead(user,verbosity);
    }

    //Changed 9.1.40071.3 on
    /**
     * WARNING: USES UNDOCUMENTED BLACKBOARD API's</br>
     * Returns a List<{@link BBUser}> object containing all users for the system with the given verbosity
     * @param magKey The key assigned to the method access group
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBUser> bbUserReadAll(@WebParam(name = "magKey") String magKey, @WebParam(name = "verbosity") BBUserVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return UserHelper.userReadAll(verbosity);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param course
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBUser> bbUserReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "verbosity") BBUserVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return UserHelper.userReadByCourseId(course,verbosity);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param course
     * @param cmRole
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBUser> bbUserReadByCourseIdAndCMRole(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "") BBCourseMembershipRole cmRole, @WebParam(name = "verbosity") BBUserVerbosity verbosity) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"GET");
        return UserHelper.userReadByCourseIdAndCMRole(course,cmRole,verbosity);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param user
     * @param portalRole
     * @param secondaryPortalRoles
     * @param systemRole
     * @return Boolean
     * @throws WebServiceException
     */
    @WebMethod
    public Boolean bbUserUpdate(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user, @WebParam(name = "portalRole") BBRole portalRole, @WebParam(name = "secondaryPortalRoles") List<BBRole> secondaryPortalRoles, @WebParam(name = "systemRole") BBRole systemRole) throws WebServiceException
    {
        doSecurity(magKey,Util.getMethodName(),"PUT");
        return UserHelper.userCreateOrUpdate(user,portalRole,secondaryPortalRoles,systemRole,true);
    }
}
