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

/******* Undocumented API *******/

//blackboard - admin
import blackboard.admin.persist.user.impl.PersonDbLoader;
import blackboard.admin.persist.user.impl.PersonDbPersister;

//blackboard - data
import blackboard.data.gradebook.impl.Attempt;
import blackboard.data.gradebook.impl.Outcome;
import blackboard.data.gradebook.impl.OutcomeDefinition;
import blackboard.data.registry.CourseRegistryEntry;

//blackboard - db
import blackboard.db.ConstraintViolationException;

//blackboard - persist
import blackboard.persist.gradebook.impl.AttemptDbLoader;
import blackboard.persist.gradebook.impl.AttemptDbPersister;
import blackboard.persist.gradebook.impl.GradeBookSettingsDbLoader;
import blackboard.persist.gradebook.impl.OutcomeDbLoader;
import blackboard.persist.gradebook.impl.OutcomeDefinitionDbLoader;
import blackboard.persist.gradebook.impl.OutcomeDefinitionDbPersister;
import blackboard.persist.registry.CourseRegistryEntryDbLoader;
import blackboard.persist.registry.CourseRegistryEntryDbPersister;

//blackboard - platform
import blackboard.platform.gradebook2.AttemptDetail;
import blackboard.platform.gradebook2.GradableItem;
import blackboard.platform.gradebook2.GradeDetail;
import blackboard.platform.gradebook2.GradebookManagerFactory;
import blackboard.platform.gradebook2.impl.AttemptDAO;
import blackboard.platform.gradebook2.impl.GradeDetailDAO;
import blackboard.platform.gradebook2.impl.GradableItemDAO;
import blackboard.platform.security.DomainManagerFactory;
import blackboard.platform.security.SystemRole;

/******** Documented API ********/

//bbws
import bbws.announcement.BBAnnouncement;
import bbws.calendar.BBCalendarEntry;
import bbws.course.BBCourse;
import bbws.course.BBCourseQuota;
import bbws.course.coursemembership.BBCourseMembership;
import bbws.course.coursemembership.BBCourseMembershipRole;
import bbws.course.BBEnrollment;
import bbws.course.enums.verbosity.BBCourseMembershipVerbosity;
import bbws.course.enums.verbosity.BBCourseVerbosity;
import bbws.util.exception.EmptyListException;
import bbws.util.factory.list.BBListFactory;
import bbws.gradecentre.attempt.BBAttempt;
import bbws.gradecentre.attempt.BBAttemptDetail;
import bbws.gradecentre.BBGradeCentreSettings;
import bbws.gradecentre.grade.BBGradeDetail;
import bbws.gradecentre.column.BBLineitem;
import bbws.gradecentre.enums.verbosity.BBAttemptVerbosity;
import bbws.gradecentre.column.BBGradableItem;
import bbws.gradecentre.grade.BBGradingSchema;
import bbws.gradecentre.grade.BBScore;
import bbws.gradecentre.outcome.BBOutcome;
import bbws.gradecentre.outcome.BBOutcomeDefinition;
import bbws.groups.BBGroup;
import bbws.groups.BBGroupMembership;
import bbws.util.security.properties.WebServiceProperties;
import bbws.user.BBUser;
import bbws.user.BBRole;

//blackboard - admin
import blackboard.admin.data.course.Enrollment;
import blackboard.admin.data.IAdminObject.RecStatus;
import blackboard.admin.data.IAdminObject.RowStatus;
import blackboard.admin.data.user.Person;
import blackboard.admin.persist.course.EnrollmentLoader;

//blackboard - base
import blackboard.base.FormattedText;
import blackboard.base.FormattedText.Type;

//blackboard - data
import blackboard.data.announcement.Announcement;
import blackboard.data.calendar.CalendarEntry;
import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.course.CourseMembership.Role;
import blackboard.data.course.CourseQuota;
import blackboard.data.course.Group;
import blackboard.data.course.GroupMembership;
import blackboard.data.gradebook.Lineitem;
import blackboard.data.gradebook.Score;
import blackboard.data.role.PortalRole;
import blackboard.data.user.User;
import blackboard.data.user.User.EducationLevel;
import blackboard.data.user.User.Gender;
import blackboard.data.user.UserRole;

//blackboard - persist
import blackboard.persist.announcement.AnnouncementDbLoader;
import blackboard.persist.announcement.AnnouncementDbPersister;
import blackboard.persist.calendar.CalendarEntryDbLoader;
import blackboard.persist.calendar.CalendarEntryDbPersister;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseDbPersister;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.course.CourseMembershipDbPersister;
import blackboard.persist.course.GroupDbLoader;
import blackboard.persist.course.GroupMembershipDbLoader;
import blackboard.persist.course.GroupMembershipDbPersister;
import blackboard.persist.course.GroupDbPersister;
import blackboard.persist.gradebook.LineitemDbLoader;
import blackboard.persist.gradebook.LineitemDbPersister;
import blackboard.persist.gradebook.ScoreDbLoader;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.role.PortalRoleDbLoader;
import blackboard.persist.user.UserDbLoader;
import blackboard.persist.user.UserDbPersister;
import blackboard.persist.user.UserRoleDbPersister;

//blackboard - platform
import blackboard.platform.BbServiceManager;
import blackboard.platform.security.SecurityUtil;

//java
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//javax
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceContext;

@WebService(name="BBWebService", serviceName="BBWebService", targetNamespace="")
public class BBWebService
{
    private WebServiceProperties wsp = new WebServiceProperties("amnl","BBWebService");

    @Resource
    private WebServiceContext wsContext;

    /*********************
     * Announcement posting really should have userID's passed
     * so you can check if user is allowed to post where they
     * want to but this would require authentication, passing
     * the userid and trust of that authentication.
     *
     * textType(null) = HTML / TEXT - DEFAULT TEXT
     * courseID(null) = e.g. bbd510
     * permanent(null) = true / false - DEFAULT false
     * type(null) = COURSE / SYSTEM - DEFAULT COURSE - what happens if -
     *					  no courseid? user not allowed to post system ann.?
     * title(!null) = Title of announcement
     * body(!null) = message to announce
     * startDay/Month/Year(null) = Date to make available - DEFAULT Today -
     *						    startDay 1-31, startMonth 1-12
     * endDay/Month/Year(null) = Date to make unavailable - DEFAULT Always available -
     *						    startDay 1-31, startMonth 1-12
     *******************/
    private Boolean announcementCreate(BBAnnouncement announcement, BBCourse course, String textType) throws WebServiceException
    {
        try
        {
            Announcement a = new Announcement();
            //We can't use checkAnnouncementDetail as this mustn't throw an error as null or ""
            //is valid when posting message, but not when modifying them.
            if(announcement.getAnnouncementBbId()!=null && !announcement.getAnnouncementBbId().trim().equalsIgnoreCase(""))
            {
                announcement.setAnnouncementBbId(announcement.getAnnouncementBbId().trim());
                //We are modifying an announcement
                a.setId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Announcement.DATA_TYPE,announcement.getAnnouncementBbId()));
            }
            //else we are creating an announcement
            a.setTitle(checkAndTrimParam(announcement.getTitle()));
            announcement.setBody(checkAndTrimParam(announcement.getBody()));
            FormattedText ft = null;
            if(textType.equalsIgnoreCase("HTML"))
            {
                ft = new FormattedText(announcement.getBody(),Type.HTML);
            }
            else
            {
               ft = new FormattedText(announcement.getBody(),Type.PLAIN_TEXT);
            }
            a.setBody(ft);
            //Assume type is course unless specifically set as SYSTEM
            a.setType(blackboard.data.announcement.Announcement.Type.COURSE);
            try
            {
                announcement.setType(checkAndTrimParam(announcement.getType()));
                //may be course or system
                if(announcement.getType().equalsIgnoreCase("SYSTEM"))
                {
                    //it's def a system
                    course.setCourseId("SYSTEM");
                    a.setType(blackboard.data.announcement.Announcement.Type.SYSTEM);
                }
                //it's def a course
            }catch(Exception e){/*it's def a course*/}
            course.setCourseId(checkAndTrimParam(course.getCourseId()));
            a.setCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId());
            a.setCreatorUserId(UserDbLoader.Default.getInstance().loadByUserName("administrator").getId());
            if (announcement.getPermanent()==null)
            {
                announcement.setPermanent(Boolean.FALSE);
            }
            a.setIsPermanent(announcement.getPermanent());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(sdf.parse(announcement.getStartDate()));
            try{a.setRestrictionStartDate(gc);}catch(Exception e){}
            //else don't set a start date at all.
            gc.setTime(sdf.parse(announcement.getEndDate()));
            try{a.setRestrictionEndDate(gc);}catch(Exception e){}
            //else don't set an end date at all.
            AnnouncementDbPersister.Default.getInstance().persist(a);
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
        return true;
    }

    private boolean announcementDelete(BBAnnouncement announcement) throws WebServiceException
    {
        try
        {
            AnnouncementDbPersister.Default.getInstance().deleteById(AnnouncementDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Announcement.DATA_TYPE,announcement.getAnnouncementBbId())).getId());
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: Please provide a valid dbid "+e.getMessage());
        }
        return true;
    }

    private List<BBAnnouncement> announcementReadByAvailableAnnouncementAndUserId(BBUser user) throws WebServiceException
    {
        List<Announcement> al = null;
        try
        {
            return BBListFactory.getNonVerboseBBList(AnnouncementDbLoader.Default.getInstance().loadAvailableByUserId(UserDbLoader.Default.getInstance().loadByUserName(user.getUserName()).getId()));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No announcements found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private List<BBAnnouncement> announcementReadByCourseId(BBCourse course) throws WebServiceException
    {
        try
        {
             return BBListFactory.getNonVerboseBBList(AnnouncementDbLoader.Default.getInstance().loadByCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId()));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No announcements found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private boolean announcementUpdate(BBAnnouncement announcement, BBCourse course, String textType) throws WebServiceException
    {
        try
        {
            announcement.setAnnouncementBbId(checkAndTrimParam(announcement.getAnnouncementBbId()));
            announcement.setType("COURSE");
            try
            {
                course.setCourseId(checkAndTrimParam(course.getCourseId()));
            }
            catch(Exception e)
            {
                course = null;
                announcement.setType("SYSTEM");
            }
            return announcementCreate(announcement,course,textType);
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: "+e.toString()+" "+e.getMessage());
        }
    }

    @Deprecated
    private void authoriseMethodUse(String methodPwd, String methodName) throws WebServiceException
    {
        if(!wsp.isMethodAccessible(methodName) || (wsp.usingPassword() && !wsp.passwordMatches(methodPwd)))
        {
            System.err.println("Access Denied: "+methodName+" (Method Accessible? "+wsp.isMethodAccessible(methodName)+", Service using passwords? "+wsp.usingPassword()+", Provided Password matches? "+wsp.passwordMatches(methodPwd)+")");
            throw new WebServiceException("Access Denied");
        }
    }


    private void authoriseMethod(String magKey, String methodName) throws WebServiceException
    {
        String methodAccessGroup;
        try{methodAccessGroup = wsp.whichMAGMatchesMAGKey(magKey);}
        catch(Exception e)
        {
            System.err.println("Access Denied: "+magKey+"/"+methodName+", no Method Access Group found (MAG key doesn't match anything!)");
            throw new WebServiceException("Access Denied");
        }
        Boolean usingSSL = ((HttpServletRequest)wsContext.getMessageContext().get(MessageContext.SERVLET_REQUEST)).isSecure();
        //If we're not using ssl and the method requires it OR the method is not accessible, then disallow access
        if((!usingSSL&&wsp.doesMethodRequireSSL(methodAccessGroup,methodName))||!wsp.isMethodAccessible(methodAccessGroup,methodName))
        {
            System.err.println("Access Denied: "+methodName+" for "+methodAccessGroup);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbAnnouncementCreate");
        return announcementCreate(announcement,course,textType);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbAnnouncementDelete");
        return announcementDelete(announcement);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbAnnouncementReadByAvailableAnnouncementAndUserId");
        return announcementReadByAvailableAnnouncementAndUserId(user);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbAnnouncementReadByCourseId");
        return announcementReadByCourseId(course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbAnnouncementUpdate");
        return announcementUpdate(announcement,course,textType);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryCreate");
        return calendarEntryCreate(ce,ceType);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryDelete");
        return calendarEntryDelete(ce);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryRead");
        return calendarEntryRead(ce);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryReadAllForGivenCourse");
        return calendarEntryReadAllForGivenCourse(c);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryReadAllForGivenCourseAndUserWithinDates");
        return calendarEntryReadAllForGivenCourseAndUserWithinDates(c,u,start,end);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryReadAllForGivenCourseWithinDates");
        return calendarEntryReadAllForGivenCourseWithinDates(c,start,end);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryReadAllForGivenType");
        return calendarEntryReadAllForGivenType(ceType);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryReadAllForGivenTypeWithinDates");
        return calendarEntryReadAllForGivenTypeWithinDates(ceType,start,end);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryReadAllForGivenUser");
        return calendarEntryReadAllForGivenUser(u);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryReadAllForGivenUserWithinDates");
        return calendarEntryReadAllForGivenUserWithinDates(u,start,end);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCalendarEntryReadAllPersonalForGivenUserWithinDates");
        return calendarEntryReadAllPersonalForGivenUserWithinDates(u,start,end);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseCreate");
        return courseCreate(course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseDelete");
        return courseDelete(course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseMembershipCreate");
        return courseMembershipCreate(courseMembership);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseMembershipDelete");
        return courseMembershipDelete(courseMembership);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseMembershipRead");
        return courseMembershipRead(courseMembership,verbosity,loadUser);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseMembershipReadByCourseId");
        return courseMembershipReadByCourseId(course,verbosity,loadUser);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseMembershipReadByUserIdAndCourseId");
        return courseMembershipReadByUserIdAndCourseId(user,course,verbosity,loadUser);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseQuotaRead");
        return courseQuotaRead(course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseQuotaUpdate");
        return courseQuotaUpdate(course,courseQuota);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseRead");
        return courseRead(course,verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseReadAll");
        return courseReadAll(verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseReadSearchByRegex");
        return courseReadSearchByRegex(regex, verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbCourseReadByUserIdAndCMRole");
        return courseReadByUserIdAndCMRole(user,cmRole,verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbEnrollmentReadByUserId");
        return enrollmentReadByUserId(user);
    }

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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreAttemptDelete");
        return gradeCentreAttemptDelete(attempt);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreAttemptDetailRead");
        return gradeCentreAttemptDetailRead(attemptDetail,verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreAttemptDetailReadLastAttempByGradeDetail");
        return gradeCentreAttemptDetailReadLastAttempByGradeDetail(gradeDetail,verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreAttemptReadByOutcomeDefinitionId");
        return gradeCentreAttemptReadByOutcomeDefinitionId(outcomeDef,verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreAttemptReadByOutcomeId");
        return gradeCentreAttemptReadByOutcomeId(outcome,verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreGradableItemRead");
        return gradeCentreGradableItemRead(gradableItem);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreGradableItemReadByCourseId");
        return gradeCentreGradableItemReadByCourseId(course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreGradeDetailRead");
        return gradeCentreGradeDetailRead(gradeDetail);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreGradeDetailReadByGradableItem");
        return gradeCentreGradeDetailReadByGradableItem(gradableItem);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreGradeDetailReadByGradableItemAndCourseMembership");
        return gradeCentreGradeDetailReadByGradableItemAndCourseMembership(gradableItem,courseMembership);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreGradeDetailReadByGradableItemAndUserId");
        return gradeCentreGradeDetailReadByGradableItemAndUserId(gradableItem,user);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreGradeDetailReadByGradableItemIdAndUserId");
        return gradeCentreGradeDetailReadByGradableItemIdAndUserId(gradableItem,user);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreGradingSchemaReadByCourseId");
        return gradeCentreGradingSchemaReadByCourseId(course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreLineitemAdd");
        return gradeCentreLineitemAdd(lineitem,course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreLineItemDelete");
        return gradeCentreLineitemOrOutcomeDefinitionDelete(lineitem.getLineItemBbId());
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
    public BBLineitem bbGradeCentreLineitemRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem, @WebParam(name = "verbosity") BBLineitem.BBLineitemVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreLineitemRead");
        return gradeCentreLineitemRead(lineitem,verbosity);
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
    public List<BBLineitem> bbGradeCentreLineitemReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "verbosity") BBLineitem.BBLineitemVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreLineitemReadByCourseId");
        return gradeCentreLineitemReadByCourseId(course,verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreOutcomeDefinitionDelete");
        return gradeCentreLineitemOrOutcomeDefinitionDelete(outcomeDef.getOutcomeDefinitionBbId());
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreOutcomeDefinitionRead");
        return gradeCentreOutcomeDefinitionRead(outcomeDef);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreOutcomeDefinitionReadByCourseId");
        return gradeCentreOutcomeDefinitionReadByCourseId(course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreOutcomeRead");
        return gradeCentreOutcomeRead(outcome);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreOutcomeReadByOutcomeDefinitionId");
        return gradeCentreOutcomeReadByOutcomeDefinitionId(outcomeDef);
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
    public BBScore bbGradeCentreScoreRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "score") BBScore score, @WebParam(name = "verbosity") BBScore.BBScoreVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreScoreRead");
        return gradeCentreScoreRead(score,verbosity);
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
    public List<BBScore> bbGradeCentreScoreReadByLineitemId(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem, @WebParam(name = "verbosity") BBScore.BBScoreVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreScoreReadByLineitemId");
        return gradeCentreScoreReadByLineitemId(lineitem,verbosity);
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
    public BBScore bbGradeCentreScoreReadByLineitemIdAndCourseMembershipId(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem, @WebParam(name = "courseMembership") BBCourseMembership courseMembership, @WebParam(name = "verbosity") BBScore.BBScoreVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreScoreReadByLineitemIdAndCourseMembershipId");
        return gradeCentreScoreReadByLineitemIdAndCourseMembershipId(lineitem,courseMembership,verbosity);
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
    public BBScore bbGradeCentreScoreReadByLineitemIdAndUserId(@WebParam(name = "magKey") String magKey, @WebParam(name = "lineitem") BBLineitem lineitem, @WebParam(name = "user") BBUser user, @WebParam(name = "verbosity") BBScore.BBScoreVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreScoreReadByLineitemIdAndUserId");
        return gradeCentreScoreReadByLineitemIdAndUserId(lineitem,user,verbosity);
    }

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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGradeCentreSettingsRead");
        return gradeCentreSettingsRead(course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGroupAdd");
        return groupAdd(group,course,descriptionTextType);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGroupDelete");
        return groupDelete(group);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGroupRead");
        return groupRead(group);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGroupUpdate");
        return groupUpdate(group, descriptionTextType);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGroupReadByCourseId");
        return groupReadByCourseId(course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGroupMembershipReadByGroupId");
        return groupMembershipReadByGroupId(group);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGroupMembershipCreateByUserIdAndGroupId");
        return groupMembershipCreateByUserIdAndGroupId(user,group);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbGroupMembershipCreateByUserIdAndGroupId");
        return groupMembershipDeleteByUserIdAndGroupId(user,group);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbRoleSecondaryPortalReadByUserId");
        return roleSecondaryPortalReadByUserId(user);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbRoleSecondaryPortalUpdate");
        return roleSecondaryPortalUpdate(user,roles);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbRoleSecondarySystemReadByUserId");
        return roleSecondarySystemReadByUserId(user);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbUserReadByUserIdAndCourseId");
        return roleUserReadByUserIdAndCourseId(user,course);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbUserCreate");
        return userCreateOrUpdate(user,portalRole,secondaryPortalRoles,systemRole,false);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbUserDelete");
        return userDelete(user);
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
    public BBUser bbUserRead(@WebParam(name = "magKey") String magKey, @WebParam(name = "user") BBUser user, @WebParam(name = "verbosity") BBUser.BBUserVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbUserRead");
        return userRead(user,verbosity);
    }

    /**
     *
     * @param magKey The key assigned to the method access group
     * @param verbosity Used to determine how much detail is returned in each object
     * @return
     * @throws WebServiceException
     */
    @WebMethod
    public List<BBUser> bbUserReadAll(@WebParam(name = "magKey") String magKey, @WebParam(name = "verbosity") BBUser.BBUserVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbUserReadAll");
        return userReadAll(verbosity);
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
    public List<BBUser> bbUserReadByCourseId(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "verbosity") BBUser.BBUserVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbUserByCourseId");
        return userReadByCourseId(course,verbosity);
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
    public List<BBUser> bbUserReadByCourseIdAndCMRole(@WebParam(name = "magKey") String magKey, @WebParam(name = "course") BBCourse course, @WebParam(name = "") BBCourseMembershipRole cmRole, @WebParam(name = "verbosity") BBUser.BBUserVerbosity verbosity) throws WebServiceException
    {
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbUserReadByCourseIdAndCMRole");
        return userReadByCourseIdAndCMRole(course,cmRole,verbosity);
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
        //authoriseMethod(magKey,getMethodName());
        authoriseMethod(magKey,"bbUserUpdate");
        return userCreateOrUpdate(user,portalRole,secondaryPortalRoles,systemRole,true);
    }

    private Boolean calendarEntryCreate(BBCalendarEntry bbce, BBCalendarEntry.BBCalendarEntryType bbceType) throws WebServiceException
                                    /*(String courseId, String userId, String description, String title,
                                    BBCalendarEntry.BBCalendarEntryType type, int startDay, int startMonth, int startYear,
                                    int startHour, int startMinute, int startSecond, int endDay,
                                    int endMonth, int endYear, int endHour, int endMinute, int endSecond) throws WebServiceException*/
    {
        //course - course,user,desc,title,type
        //inst - user,desc,title,type (course must not be set? is this system wide calendar?)
        //personal - user,desc,title,type (course must not be set)

        CalendarEntry.Type ceType = null;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = null;

        ceType = calenderEntryTypeFactory(bbceType);
        startCal = getCalendarObjFromDateTimeString(bbce.getStartDateTime());
        endCal = getCalendarObjFromDateTimeString(bbce.getEndDateTime());

        try
        {
            CalendarEntry ce = new CalendarEntry();
            ce.setCourseId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Course.DATA_TYPE, bbce.getCourseBbId()));
            ce.setCreatorUserId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(User.DATA_TYPE, bbce.getUserBbId()));
            ce.setDescription(new FormattedText(bbce.getDescription(),FormattedText.Type.PLAIN_TEXT));
            if(endCal!=null)
            {
                ce.setEndDate(endCal);
            }
            //ce.setId();
            //ce.setModifiedDate();
            if(startCal!=null)
            {
                ce.setStartDate(startCal);
            }
            ce.setTitle(bbce.getTitle());
            //ce.setType(CalendarEntry.Type.fromExternalString(type));
            ce.setType(ceType);
            //Course Institution personal
            CalendarEntryDbPersister.Default.getInstance().persist(ce);
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error while trying to add calendar entry: "+e.toString());
        }

        return true;
    }

    private boolean calendarEntryDelete(BBCalendarEntry ce) throws WebServiceException
    {
        try
        {
            CalendarEntryDbPersister.Default.getInstance().deleteById(CalendarEntryDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(CalendarEntry.DATA_TYPE,ce.getCalendarEntryBbId())).getId());
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: Please provide a CalendarEntry object with a valid Id");//+e.toString();
        }
        return true;
    }

    private BBCalendarEntry calendarEntryRead(BBCalendarEntry ce) throws WebServiceException
    {
        String error = "";
        try
        {
            if(ce.getCalendarEntryBbId()!=null && !ce.getCalendarEntryBbId().equalsIgnoreCase(""))
            {
                return new BBCalendarEntry(CalendarEntryDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(CalendarEntry.DATA_TYPE, ce.getCalendarEntryBbId())));
            }
            error = "You must specify a calendar BbId";
        }
        catch(KeyNotFoundException knfe)
        {
            error = "No matching calender entry";
        }
        catch(Exception e)
        {
            error = "Error whilst searching to see if a calendar entry exists: "+e.toString();
        }
        throw new WebServiceException(error);
    }

    private List<BBCalendarEntry> calendarEntryReadAllForGivenCourse(BBCourse c) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(CalendarEntryDbLoader.Default.getInstance().loadByCourseId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Course.DATA_TYPE, c.getCourseBbId())));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No calendar entries found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private List<BBCalendarEntry> calendarEntryReadAllForGivenCourseAndUserWithinDates(BBCourse c, BBUser u, String start, String end) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(CalendarEntryDbLoader.Default.getInstance().loadByCourseIdAndUserId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Course.DATA_TYPE, c.getCourseBbId()),BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(User.DATA_TYPE, u.getBbId()),getCalendarObjFromDateTimeString(start),getCalendarObjFromDateTimeString(end)));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No calendar entries found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private List<BBCalendarEntry> calendarEntryReadAllForGivenCourseWithinDates(BBCourse c, String start, String end) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(CalendarEntryDbLoader.Default.getInstance().loadByCourseId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Course.DATA_TYPE, c.getCourseBbId()),getCalendarObjFromDateTimeString(start),getCalendarObjFromDateTimeString(end)));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No calendar entries found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private List<BBCalendarEntry> calendarEntryReadAllForGivenType(BBCalendarEntry.BBCalendarEntryType ceType) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(CalendarEntryDbLoader.Default.getInstance().loadByType(calenderEntryTypeFactory(ceType)));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No calendar entries found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private List<BBCalendarEntry> calendarEntryReadAllForGivenTypeWithinDates(BBCalendarEntry.BBCalendarEntryType ceType, String start, String end) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(CalendarEntryDbLoader.Default.getInstance().loadByType(calenderEntryTypeFactory(ceType),getCalendarObjFromDateTimeString(start),getCalendarObjFromDateTimeString(end)));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No calendar entries found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private List<BBCalendarEntry> calendarEntryReadAllForGivenUser(BBUser u) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(CalendarEntryDbLoader.Default.getInstance().loadByUserId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(User.DATA_TYPE, u.getBbId())));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No calendar entries found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private List<BBCalendarEntry> calendarEntryReadAllForGivenUserWithinDates(BBUser u, String start, String end) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(CalendarEntryDbLoader.Default.getInstance().loadByUserId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(User.DATA_TYPE, u.getBbId()),getCalendarObjFromDateTimeString(start),getCalendarObjFromDateTimeString(end)));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No calendar entries found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private List<BBCalendarEntry> calendarEntryReadAllPersonalForGivenUserWithinDates(BBUser u, String start, String end) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(CalendarEntryDbLoader.Default.getInstance().loadPersonalByUserId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(User.DATA_TYPE, u.getBbId()),getCalendarObjFromDateTimeString(start),getCalendarObjFromDateTimeString(end)));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No calendar entries found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private CalendarEntry.Type calenderEntryTypeFactory(BBCalendarEntry.BBCalendarEntryType ceType)
    {
        CalendarEntry.Type type;
        switch(ceType)
        {
            case COURSE: type = CalendarEntry.Type.COURSE; break;
            case INSTITUTION: type = CalendarEntry.Type.INSTITUTION; break;
            case PERSONAL: type = CalendarEntry.Type.PERSONAL; break;
            default: throw new WebServiceException("Invalid CalendarType");
        }
        return type;
    }

    private String checkAndTrimParam(String courseDetail) throws Exception
    {
	if(courseDetail!=null && !courseDetail.equalsIgnoreCase(""))
	{
	    return courseDetail.trim();
	}
	throw new Exception("Invalid course detail: '"+courseDetail+"'");
    }

    private Boolean checkParam(String param)
    {
	if(param!=null && !param.equalsIgnoreCase(""))
	{
	    return true;
	}
	return false;
    }

    private boolean courseCreate(BBCourse course)
    {
        try
        {
            CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId());
            throw new WebServiceException("Error: Course may already exist");
        }
        catch(KeyNotFoundException knfe){}
        catch(Exception e)
        {
            throw new WebServiceException("Error while trying to check if course already exists: "+e.toString()+" "+e.getMessage());
        }

        Course c = new Course();
        try
        {
            c.setBatchUid(course.getBatchUId());
            c.setCourseId(course.getCourseId());
            c.setDescription(course.getDescription());
            c.setTitle(course.getTitle());
            c.setAllowGuests(course.getAllowGuests());
            c.setAllowObservers(course.getAllowObservers());
            c.setIsAvailable(course.getAvailable());
            CourseDbPersister.Default.getInstance().persist(c);
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error while trying to add course: "+e.getMessage());
        }
        return true;
    }

    private boolean courseDelete(BBCourse course)
    {
        String error = "";
        try
        {
            if(checkParam(course.getCourseId()))
            {
                CourseDbPersister.Default.getInstance().deleteById(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId());
                return true;
            }
            else if(checkParam(course.getCourseBbId()))
            {
                CourseDbPersister.Default.getInstance().deleteById(CourseDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Course.DATA_TYPE,course.getCourseBbId())).getId());
                return true;
            }
            error = "You must specify either courseId or courseBBId";
        }
        catch(KeyNotFoundException knfe)
        {
            error = "No matching course";
        }
        catch(Exception e)
        {
            error = "Error whilst deleting course: "+e.toString();
        }
        throw new WebServiceException(error);
    }

    private Boolean courseMembershipCreate(BBCourseMembership courseMembership) throws WebServiceException
    {
        Id uid = null;
        Id cid = null;

        try
        {
            uid = UserDbLoader.Default.getInstance().loadByUserName(courseMembership.getUser().getUserName()).getId();
        }
        catch(Exception e)
        {
            throw new WebServiceException("Please provide a valid username "+e.toString());
        }

        try
        {
            cid = CourseDbLoader.Default.getInstance().loadByCourseId(courseMembership.getCourse().getCourseId()).getId();
        }
        catch(Exception e)
        {
            throw new WebServiceException("Please provide a valid courseId "+e.toString());
        }

        try
        {
            CourseMembership cm = new CourseMembership();
            cm.setCourseId(cid);
            cm.setUserId(uid);
            cm.setIsAvailable(courseMembership.getAvailable());
            cm.setRole(Role.fromFieldName(courseMembership.getRole().name()));
            CourseMembershipDbPersister.Default.getInstance().persist(cm);
        }
        catch(IllegalArgumentException iae)
        {
            throw new WebServiceException("Problem while trying to set role, does role exist? "+iae.toString());
        }
        catch(PersistenceException e)
        {
            throw new WebServiceException("Enrollment may already exist?");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Problem while trying to update coursemembership details "+e.toString());
        }
        return true;
    }

    private Boolean courseMembershipDelete(BBCourseMembership courseMembership) throws WebServiceException
    {
        Course c = null;
        User u = null;
        CourseMembership cm = null;

        if(checkParam(courseMembership.getCourseMembershipBbId()))
        {
            try
            {
                //get enrollment id
                cm = CourseMembershipDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(CourseMembership.DATA_TYPE, courseMembership.getCourseMembershipBbId()));
            }
            catch(Exception e)
            {
                throw new WebServiceException("Error: Given courseMembership doesn't seem to exist "+e.getMessage());
            }
        }
        else
        {
            try
            {
                c = CourseDbLoader.Default.getInstance().loadByCourseId(courseMembership.getCourse().getCourseBbId());
            }
            catch(Exception e)
            {
                throw new WebServiceException("Error: Course Id is invalid or does not exist "+e.getMessage());
            }

            try
            {
                u = UserDbLoader.Default.getInstance().loadByUserName(courseMembership.getUser().getUserName());
            }
            catch(Exception e)
            {
                throw new WebServiceException("Error: User Id is invalid or does not exist "+e.getMessage());
            }

            try
            {
                //get enrollment id
                cm = CourseMembershipDbLoader.Default.getInstance().loadByCourseAndUserId(c.getId(),u.getId());
            }
            catch(Exception e)
            {
                throw new WebServiceException("Error: Given user does not appear to be enrolled on given course "+e.getMessage());
            }
        }

        try
        {
            //then delete
            CourseMembershipDbPersister.Default.getInstance().deleteById(cm.getId());
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error whilst trying to unenroll given user from give course "+e.toString());
        }
        return true;
    }

    private BBCourseQuota courseQuotaRead(BBCourse course) throws WebServiceException
    {
        try
        {
            return new BBCourseQuota(CourseQuota.createInstance(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId())));
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: Could not find course with that Id "+e.getMessage()+" "+e.toString());
        }
    }

    private Boolean courseQuotaUpdate(BBCourse course, BBCourseQuota courseQuota) throws WebServiceException
    {
        if(courseQuota.getEnforceQuota()!=null || courseQuota.getEnforceUploadLimit()!=null || courseQuota.getSystemUploadLimit()!=null || courseQuota.getSystemSoftLimit()!=null || courseQuota.getSystemAbsoluteLimit()!=null)
        {
            Course c = null;
            try
            {
                c = CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId());
            }
            catch(Exception e)
            {
                throw new WebServiceException("Error: Could not load course to modify quota for, does it exist? "+e.getMessage());
            }

            try
            {
                setOrModifyCourseRegistryValue(c.getId(),"quota_override",courseQuota.getEnforceQuota()?"Y":"N");
                setOrModifyCourseRegistryValue(c.getId(),"quota_upload_override",courseQuota.getEnforceUploadLimit()?"Y":"N");

                if(courseQuota.getCourseAbsoluteLimit()!=null)
                {
                    c.setAbsoluteLimit(courseQuota.getCourseAbsoluteLimit());
                }

                if(courseQuota.getCourseSoftLimit()!=null)
                {
                    c.setSoftLimit(courseQuota.getCourseSoftLimit());
                }

                if(courseQuota.getCourseUploadLimit()!=null)
                {
                    c.setUploadLimit(courseQuota.getCourseUploadLimit());
                }

                CourseDbPersister.Default.getInstance().persist(c);
            }
            catch(Exception e)
            {
                throw new WebServiceException( "Error: Could not modify course quota settings - "+e.getMessage());
            }
            return true;
        }
        return false;
    }

    private BBCourseMembership courseMembershipRead(BBCourseMembership courseMembership, BBCourseMembershipVerbosity verbosity, Boolean loadUser) throws WebServiceException
    {
        String error = "";
        try
        {
            if(courseMembership.getUser()!=null && courseMembership.getCourse()!=null)
            {
                if(checkParam(courseMembership.getUser().getBbId()) && checkParam(courseMembership.getCourse().getCourseBbId()))
                {
                    try
                    {
                        return new BBCourseMembership(CourseMembershipDbLoader.Default.getInstance().loadByCourseAndUserId(CourseDbLoader.Default.getInstance().loadByCourseId(courseMembership.getCourse().getCourseBbId()).getId(),UserDbLoader.Default.getInstance().loadByUserName(courseMembership.getUser().getBbId()).getId(),null,true),verbosity);
                    }
                    catch(Exception e)
                    {
                        throw new Exception("null in user or course?");
                    }
                }
            }
            else if(checkParam(courseMembership.getCourseMembershipBbId()))
            {
                try
                {
                    return new BBCourseMembership(CourseMembershipDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(CourseMembership.DATA_TYPE, courseMembership.getCourseMembershipBbId()),null,true),verbosity);
                }
                catch(Exception e)
                {
                    throw new Exception("null in coursemem?");
                }
            }
            error = "You must specify either user with userId and course with courseId, or, a courseMembershipBbId";
        }
        catch(NullPointerException npe)
        {
            throw new WebServiceException("null somewhere else");
        }
        catch(KeyNotFoundException knfe)
        {
            error = "No matching course";
        }
        catch(Exception e)
        {
            error = "Error whilst searching to see if courseMembership exists: "+e.toString();
        }
        throw new WebServiceException(error);
    }

    private List<BBCourseMembership> courseMembershipReadByCourseId(BBCourse course,BBCourseMembershipVerbosity verbosity, Boolean loadUser)
    {
        try
        {
            if(verbosity == null)
            {
                throw new Exception("You must specify a verbosity level");
            }
            return BBListFactory.getBBCourseMembershipListFromList(CourseMembershipDbLoader.Default.getInstance().loadByCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId(),null,loadUser),verbosity);
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No course memberships found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private BBCourseMembership courseMembershipReadByUserIdAndCourseId(BBUser user, BBCourse course,BBCourseMembershipVerbosity verbosity, Boolean loadUser)
    {
        try
        {
            if(verbosity == null)
            {
                throw new Exception("You must specify a verbosity level");
            }
            return new BBCourseMembership(CourseMembershipDbLoader.Default.getInstance().loadByCourseAndUserId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId(),UserDbLoader.Default.getInstance().loadByUserName(user.getUserName()).getId(),null,loadUser),verbosity);
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private BBCourse courseRead(BBCourse course, BBCourseVerbosity verbosity) throws WebServiceException
    {
        String error = "";
        try
        {
            if(verbosity == null)
            {
                throw new Exception("You must specify a verbosity level");
            }
            if(course.getCourseId()!=null && !course.getCourseId().equalsIgnoreCase(""))
            {
                return new BBCourse(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()),verbosity);
            }
            else if(course.getCourseBbId()!=null && !course.getCourseBbId().equalsIgnoreCase(""))
            {
                return new BBCourse(CourseDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Course.DATA_TYPE, course.getCourseBbId())),verbosity);
            }
            error = "You must specify either courseId or courseBBId";
        }
        catch(KeyNotFoundException knfe)
        {
            error = "No matching course";
        }
        catch(Exception e)
        {
            error = "Error whilst searching to see if course exists: "+e.toString();
        }
        throw new WebServiceException(error);
    }

    private List<BBCourse> courseReadAll(BBCourseVerbosity verbosity)
    {
        try
        {
            if(verbosity == null)
            {
                throw new Exception("You must specify a verbosity level");
            }
            //return getBBCourseListFromList(CourseDbLoader.Default.getInstance().loadAllCourses(),verbosity);
            return BBListFactory.getBBCourseListFromList(CourseDbLoader.Default.getInstance().loadAllCourses(),verbosity);
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No courses found");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error whilst searching to see if course exists: "+e.toString());
        }
    }

    private List<BBCourse> courseReadByUserIdAndCMRole(BBUser user, BBCourseMembershipRole cmRole, BBCourseVerbosity verbosity) throws WebServiceException
    {
        try
        {
            if(verbosity == null)
            {
                throw new Exception("You must specify a verbosity level");
            }
            //return getBBCourseListFromList(CourseDbLoader.Default.getInstance().loadByUserIdAndCourseMembershipRole(UserDbLoader.Default.getInstance().loadByUserName(user.getUserName()).getId(),CourseMembership.Role.fromExternalString(cmRole.name())),verbosity);
            return BBListFactory.getBBCourseListFromList(CourseDbLoader.Default.getInstance().loadByUserIdAndCourseMembershipRole(UserDbLoader.Default.getInstance().loadByUserName(user.getUserName()).getId(),CourseMembership.Role.fromExternalString(cmRole.name())),verbosity);
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No courses found");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error whilst searching to see if course exists: "+e.toString());
        }
    }

    private List<BBCourse> courseReadSearchByRegex(String regex, BBCourseVerbosity verbosity) throws WebServiceException
    {
        List<BBCourse> rl = new ArrayList<BBCourse>();
        List<Course> cl = null;
        try
        {
            cl = CourseDbLoader.Default.getInstance().loadAllCourses();
        }
        catch(Exception e)
        {
            throw new WebServiceException("Could not load all courses to search: "+e.getMessage());
        }

        if(cl!=null && cl.size()>0)
        {
            Pattern pattern =  Pattern.compile(regex);
            Course c = null;
            Iterator i = cl.iterator();

            while(i.hasNext())
            {
                c = ((Course)i.next());
                if(isAMatch(pattern,c.getCourseId()))
                {
                    //The verbosity exception here "should" NEVER happen
                    try{rl.add(new BBCourse(c,verbosity));}catch(Exception e){System.err.println("Error while instantiating course "+c.getCourseId()+": "+e.getMessage());}
                }
            }

            if(rl.size()<1)
            {
                throw new WebServiceException("No matches found");
            }
            return rl;
        }
        throw new WebServiceException("No courses found");
    }

    private List<BBEnrollment> enrollmentReadByUserId(BBUser user) throws WebServiceException
    {
        try
        {
            Enrollment enrollment = new Enrollment();
            enrollment.setPersonBatchUid(user.getUserName());
            return BBListFactory.getNonVerboseBBList(EnrollmentLoader.Default.getInstance().load(enrollment));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No enrollments found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private Calendar getCalendarObjFromDateTimeString(String dateTime)
    {
        Calendar cal = Calendar.getInstance();
        if(dateTime!=null && dateTime.equals(""));
        {
            try
            {
                cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
            }
            catch(Exception e)
            {
                throw new WebServiceException("Error: Invalid date specified");
            }
        }
        return cal;
    }

    private Score getScoreObjForGivenCourseMembershipBbIdAndLineItemBbId(String courseMembershipBbId, String lineItemBbId) throws Exception
    {
	    return ((ScoreDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(ScoreDbLoader.TYPE)).loadByCourseMembershipIdAndLineitemId
		(
		    CourseMembershipDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(CourseMembership.DATA_TYPE,courseMembershipBbId)).getId(),
		    ((LineitemDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(LineitemDbLoader.TYPE)).loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Lineitem.LINEITEM_DATA_TYPE,lineItemBbId)).getId()
		);
    }

    private boolean gradeCentreAttemptDelete(BBAttempt attempt) throws WebServiceException
    {
        try
        {
            AttemptDbPersister.Default.getInstance().deleteById(AttemptDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Attempt.DATA_TYPE,attempt.getAttemptBbId())).getId());
        }
        catch(Exception e)
        {
            throw new WebServiceException( "Error: Could not delete attempt - "+e.getMessage());
        }
        return true;
    }

    private BBAttemptDetail gradeCentreAttemptDetailRead(BBAttemptDetail ad, BBAttemptVerbosity verbosity) throws WebServiceException
    {
        try
        {
            return new BBAttemptDetail(AttemptDAO.get().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(AttemptDetail.DATA_TYPE, ad.getId())),verbosity);
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private BBAttemptDetail gradeCentreAttemptDetailReadLastAttempByGradeDetail(BBGradeDetail gd, BBAttemptVerbosity verbosity) throws WebServiceException
    {
        BBAttemptDetail bbad = new BBAttemptDetail();
        bbad.setId(gd.getLastAttemptId());
        return gradeCentreAttemptDetailRead(bbad,verbosity);
    }

    private List<BBAttempt> gradeCentreAttemptReadByOutcomeDefinitionId(BBOutcomeDefinition outcomeDef, BBAttemptVerbosity verbosity) throws WebServiceException
    {
        try
        {
            return BBListFactory.getBBAttemptListFromList(AttemptDbLoader.Default.getInstance().loadByOutcomeDefinitionId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(OutcomeDefinition.DATA_TYPE,outcomeDef.getOutcomeDefinitionBbId())),verbosity);
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No attempts found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private List<BBAttempt> gradeCentreAttemptReadByOutcomeId(BBOutcome outcome, BBAttemptVerbosity verbosity) throws WebServiceException
    {
        try
        {
            return BBListFactory.getBBAttemptListFromList(((AttemptDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(AttemptDbLoader.TYPE)).loadByOutcomeId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Outcome.DATA_TYPE, outcome.getOutcomeBbId())),verbosity);
            //return BBListFactory.getBBAttemptListFromList(AttemptDbLoader.Default.getInstance().loadByOutcomeId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Outcome.DATA_TYPE, outcome.getOutcomeBbId())),verbosity);
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No attempts found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private BBGradeDetail gradeCentreGradeDetailRead(BBGradeDetail gd) throws WebServiceException
    {
        try
        {
            return new BBGradeDetail(GradeDetailDAO.get().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(GradeDetail.DATA_TYPE, gd.getId())));
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error while retrieving GradeDetail: "+e.toString());
        }
    }

    private List<BBGradeDetail> gradeCentreGradeDetailReadByGradableItem(BBGradableItem bbgi) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(GradeDetailDAO.get().getGradeDetails(GradableItemDAO.get().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(GradableItem.DATA_TYPE, bbgi.getId())).getId()));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No grade details found");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error while retrieving GradeDetails: "+e.toString());
        }
    }

    private BBGradeDetail gradeCentreGradeDetailReadByGradableItemAndCourseMembership(BBGradableItem gi, BBCourseMembership cm) throws WebServiceException
    {
        Iterator<BBGradeDetail> gdi = gradeCentreGradeDetailReadByGradableItem(gi).iterator();
        BBGradeDetail gd;
        while(gdi.hasNext())
        {
            gd = gdi.next();
            if(gdi.next().getCourseUserId().equalsIgnoreCase(cm.getCourseMembershipBbId()))
            {
                return gd;
            }
        }
        throw new WebServiceException("Grade Detail not found");
    }

    private BBGradeDetail gradeCentreGradeDetailReadByGradableItemAndUserId(BBGradableItem gi, BBUser u) throws WebServiceException
    {
        BBCourse c = new BBCourse();
        c.setCourseBbId(gi.getCourseId());
        return gradeCentreGradeDetailReadByGradableItemAndCourseMembership(gi, courseMembershipReadByUserIdAndCourseId(u, courseRead(c, BBCourseVerbosity.minimal), BBCourseMembershipVerbosity.minimal, false));
    }

    /*private boolean gradeCentreGradableItemAdd(BBGradableItem bbgi) throws WebServiceException
    {
        try
        {
            blackboard.persist.BbPersistenceManager bbpm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
            GradableItem gi = new GradableItem();
            gi.setAggregationModel(GradableItem.AttemptAggregationModel.valueOf(bbgi.getAggregationModel().name()));
            try{gi.setAssessmentId(bbpm.generateId(blackboard.data.assessment.CourseAssessment, bbgi.getAssessmentId()));}catch(Exception e){}
            try{gi.setCalculatedInd(GradableItem.CalculationType.valueOf(bbgi.getCalculatedInd()));}catch(Exception e){}
            gi.setCategory(bbgi.getCategory());
            try{gi.setCategoryId(bbpm.generateId(blackboard.data.category.Category.DATA_TYPE, bbgi.getCategoryId()));}catch(Exception e){}
            gi.setCourseContentId(Id.UNSET_ID);
            gi.setCourseId(Id.UNSET_ID);
            gi.setDateAdded(bbgi.getDateAdded());
            gi.setDateModified(bbgi.getDateModified());
            gi.setDescription(bbgi.getDescription());
            gi.setDisplayTitle(bbgi.getDisplayTitle());
            gi.setDueDate(bbgi.getDueDate());
            gi.setExternalAnalysisUrl(bbgi.getExternalAnalysisUrl());
            gi.setExternalAttemptHandlerUrl(bbgi.getExternalAttemptHandlerUrl());
            gi.setGradingPeriodId(Id.UNSET_ID);
            gi.setGradingSchema(bbgi.getGradingSchema());
            gi.setGradingSchemaId(Id.UNSET_ID);
            gi.setHideAttempt(bbgi.getHideAttempt());
            //gi.setId(Id.UNSET_ID); //You shouldn't need to set this for creating a new item as blackboard should assign one
            gi.setLinkId(bbgi.getLinkId());
            gi.setPoints(bbgi.getPoints());
            //gi.setPosition(bbgi.getPosition()); //Not sure if bb will work this out too, probs better used for updating when changing position
            gi.setScorable(bbgi.getScorable());
            gi.setScoreProviderHandle(bbgi.getScoreProviderHandle());
            gi.setSecondaryGradingSchemaId(Id.UNSET_ID);
            gi.setShowStatsToStudent(bbgi.getShowStatsToStudent());
            gi.setSingleAttempt(bbgi.getSingleAttempt());
            gi.setTitle(bbgi.getTitle());
            try{gi.setVersion(new blackboard.persist.RowVersion(bbgi.getVersion()));}catch(Exception e){}
            gi.setVisibleInAllTerms(bbgi.getVisibleInAllTerms());
            gi.setVisibleInBook(bbgi.getVisibleInBook());
            gi.setVisibleToStudents(bbgi.getVisibleToStudents());
            try{gi.setWeight(Float.parseFloat(""+bbgi.getWeight()));}catch(Exception e){}
            GradebookManagerFactory.getInstanceWithoutSecurityCheck().persistGradebookItem(gi);
            return true;
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error whilst adding GradableItem: "+e.toString());
        }
    }*/

    private BBGradableItem gradeCentreGradableItemRead(BBGradableItem gradableItem) throws WebServiceException
    {
        try
        {
            return new BBGradableItem(GradableItemDAO.get().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(GradableItem.DATA_TYPE, gradableItem.getId())));
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error while retrieving GradableItem: "+e.toString());
        }
    }

    private List<BBGradableItem> gradeCentreGradableItemReadByCourseId(BBCourse course) throws WebServiceException
    {
        try
        {
            //return BBListFactory.getNonVerboseBBList(GradebookManagerFactory.getInstanceWithoutSecurityCheck().getGradebookItems(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId()));
            return BBListFactory.getNonVerboseBBList(GradableItemDAO.get().getGradableItemByCourse(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId()));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No gradable items found");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error while retrieving GradableItems: "+e.toString());
        }
    }

    /*private BBGradeDetail gradeCentreGradeDetailReadByGradableItemIdAndUserId(BBGradableItem gradableItem, BBUser user) throws WebServiceException
    {
        try
        {
            blackboard.platform.gradebook2.GradeDetail gd = blackboard.platform.gradebook2.impl.GradeDetailDAO.get().getGradeDetail(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(GradableItem.DATA_TYPE, gradableItem.getId()), BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(User.DATA_TYPE, user.getBbId()));
            //blackboard.platform.gradebook2.GradeWithAttemptScore gWAS = blackboard.platform.gradebook2.impl.GradeDAO.get().getGrade(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(GradableItem.DATA_TYPE, gradableItem), BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(User.DATA_TYPE, user.getBbId()));
            return new BBGradeDetail(gd);
            //gWAS.getAttemptScore();
        }
        catch(KeyNotFoundException e)
        {
            throw new WebServiceException("Error: No grade detail found, has user taken test and has it been marked? Is user on course? Does gradable item exist?");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: While retrieving grade detail... "+e.toString());
        }
    }*/

    private List<BBGradingSchema> gradeCentreGradingSchemaReadByCourseId(BBCourse course) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(GradebookManagerFactory.getInstanceWithoutSecurityCheck().getGradingSchemaForCourse(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId()));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No gradable items found");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: "+e.getMessage());
        }
    }

    private boolean gradeCentreLineitemAdd(BBLineitem lineitem, BBCourse course) throws WebServiceException
    {
        try
        {
            Lineitem li = new Lineitem();
            li.setCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId());
            //li.setAssessmentLocation(Lineitem.AssessmentLocation.INTERNAL);
            li.setName(lineitem.getName());
            li.setIsAvailable(lineitem.getAvailable());
            li.setPointsPossible(lineitem.getPointsPossible());
            li.setType(lineitem.getType());
            li.setWeight(lineitem.getWeight());
            ((LineitemDbPersister)BbServiceManager.getPersistenceService().getDbPersistenceManager().getPersister(LineitemDbPersister.TYPE)).persist(li);
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: Could not add lineitem "+e.toString());
        }
        return true;
    }

    private boolean gradeCentreLineitemOrOutcomeDefinitionDelete(String Id)
    {
    	try
        {
            OutcomeDefinitionDbPersister.Default.getInstance().deleteById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(OutcomeDefinition.DATA_TYPE,Id));
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: Does that item/outcome exist? "+e.toString());
        }
        return true;
    }

    private BBLineitem gradeCentreLineitemRead(BBLineitem lineitem, BBLineitem.BBLineitemVerbosity verbosity) throws WebServiceException
    {
        try
        {
            return new BBLineitem(((LineitemDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(LineitemDbLoader.TYPE)).loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Lineitem.LINEITEM_DATA_TYPE, lineitem.getLineItemBbId())),verbosity);
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: Could not retrieve lineitem details "+e.toString());
        }
    }

    private List<BBLineitem> gradeCentreLineitemReadByCourseId(BBCourse course, BBLineitem.BBLineitemVerbosity verbosity) throws WebServiceException
    {
        try
        {
            return BBListFactory.getBBLineitemListFromList((((LineitemDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(LineitemDbLoader.TYPE)).loadByCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId())),verbosity);
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No line items found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private BBOutcomeDefinition gradeCentreOutcomeDefinitionRead(BBOutcomeDefinition outcomeDef) throws WebServiceException
    {
        try
        {
            return new BBOutcomeDefinition(((OutcomeDefinitionDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(OutcomeDefinitionDbLoader.TYPE)).loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(OutcomeDefinition.DATA_TYPE,outcomeDef.getOutcomeDefinitionBbId())));
            //return new BBOutcomeDefinition(OutcomeDefinitionDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(OutcomeDefinition.DATA_TYPE,outcomeDef.getOutcomeDefinitionBbId())));
        }
        catch(KeyNotFoundException e)
        {
            throw new WebServiceException("Error: No outcomeDef found. Does outcomeDef exist?");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: Could not retrieve outcomeDefinition details "+e.toString());
        }
    }

    private List<BBOutcomeDefinition> gradeCentreOutcomeDefinitionReadByCourseId(BBCourse course) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(((OutcomeDefinitionDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(OutcomeDefinitionDbLoader.TYPE)).loadByCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId()));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No outcome definitions found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private BBOutcome gradeCentreOutcomeRead(BBOutcome outcome) throws WebServiceException
    {
        try
        {
            return new BBOutcome(((OutcomeDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(OutcomeDbLoader.TYPE)).loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Outcome.DATA_TYPE, outcome.getOutcomeBbId())));
            //return new BBOutcome(OutcomeDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Outcome.DATA_TYPE, outcome.getOutcomeBbId())));
        }
        catch(KeyNotFoundException e)
        {
            throw new WebServiceException("Error: No outcome found. Does outcome exist?");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: While retrieving outcome... "+e.toString());
        }
    }

    private List<BBOutcome> gradeCentreOutcomeReadByOutcomeDefinitionId(BBOutcomeDefinition outcomeDef) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(((OutcomeDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(OutcomeDbLoader.TYPE)).loadByOutcomeDefinitionId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(OutcomeDefinition.DATA_TYPE,outcomeDef.getOutcomeDefinitionBbId())));
            //return BBListFactory.getNonVerboseBBList(OutcomeDbLoader.Default.getInstance().loadByOutcomeDefinitionId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(OutcomeDefinition.DATA_TYPE,outcomeDef.getOutcomeDefinitionBbId())));
            //return BBListFactory.getNonVerboseBBList(Arrays.asList(((OutcomeDefinitionDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(OutcomeDefinitionDbLoader.TYPE)).loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(OutcomeDefinition.DATA_TYPE,outcomeDef.getOutcomeDefinitionBbId())).getOutcomes()));
            //return BBListFactory.getNonVerboseBBList(Arrays.asList(OutcomeDefinitionDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(OutcomeDefinition.DATA_TYPE,outcomeDef.getOutcomeDefinitionBbId())).getOutcomes()));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No outcomes found for outcomeDefBbId");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private BBScore gradeCentreScoreRead(BBScore score, BBScore.BBScoreVerbosity verbosity) throws WebServiceException
    {
        try
        {
            return new BBScore(((ScoreDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(ScoreDbLoader.TYPE)).loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Score.SCORE_DATA_TYPE,score.getScoreBbId())),verbosity);
        }
        catch(KeyNotFoundException e)
        {
            throw new WebServiceException("Error: No score found, has user taken test and has it been marked? Is user on course? Does lineitem exist?");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: While retrieving score... "+e.toString());
        }
    }

    private List<BBScore> gradeCentreScoreReadByLineitemId(BBLineitem lineitem, BBScore.BBScoreVerbosity verbosity) throws WebServiceException
    {
        try
        {
            return BBListFactory.getBBScoreListFromList(((LineitemDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(LineitemDbLoader.TYPE)).loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Lineitem.LINEITEM_DATA_TYPE,lineitem.getLineItemBbId())).getScores(),verbosity);
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No scores found for line item");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private BBScore gradeCentreScoreReadByLineitemIdAndUserId(BBLineitem lineitem, BBUser user, BBScore.BBScoreVerbosity verbosity) throws WebServiceException
    {
        try
        {
            return new BBScore
            (
                getScoreObjForGivenCourseMembershipBbIdAndLineItemBbId
                (
                    CourseMembershipDbLoader.Default.getInstance().loadByCourseAndUserId
                    (
                        ((LineitemDbLoader)BbServiceManager.getPersistenceService().getDbPersistenceManager().getLoader(LineitemDbLoader.TYPE)).loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Lineitem.LINEITEM_DATA_TYPE,lineitem.getLineItemBbId())).getCourseId(),
                        UserDbLoader.Default.getInstance().loadByUserName(user.getUserName()).getId()
                    ).getId().toExternalString(),
                    lineitem.getLineItemBbId()
                ),
                verbosity
            );
        }
        catch(KeyNotFoundException e)
        {
            throw new WebServiceException("Error: No score found, has user taken test and has it been marked? Is user on course? Does lineitem exist?");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: While retrieving score... "+e.toString());
        }
    }

    private BBScore gradeCentreScoreReadByLineitemIdAndCourseMembershipId(BBLineitem lineitem, BBCourseMembership courseMembership, BBScore.BBScoreVerbosity verbosity) throws WebServiceException
    {
        try
        {
            return new BBScore(getScoreObjForGivenCourseMembershipBbIdAndLineItemBbId(courseMembership.getCourseMembershipBbId(), lineitem.getLineItemBbId()),verbosity);
        }
        catch(KeyNotFoundException e)
        {
            throw new WebServiceException("Error: No score found, has user taken test and has it been marked? Is user on course? Does lineitem exist?");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: While retrieving score... "+e.toString());
        }
    }

    private BBGradeCentreSettings gradeCentreSettingsRead(BBCourse course) throws WebServiceException
    {
        try
        {
            return new BBGradeCentreSettings(GradeBookSettingsDbLoader.Default.getInstance().loadByCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseBbId()).getId()));
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private boolean groupAdd(BBGroup group, BBCourse course, String descType) throws WebServiceException
    {
        try
        {
            Group g = new Group();
            g.setCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseBbId()).getId());
            g.setDescription(new FormattedText(group.getDescription(),Type.fromFieldName(descType.trim().toUpperCase())));
            g.setIsAvailable(group.getAvailable());
            g.setIsChatRoomAvailable(group.getChatRoomsAvailable());
            g.setIsDiscussionBoardAvailable(group.getDiscussionBoardsAvailable());
            g.setIsEmailAvailable(group.getEmailAvailable());
            g.setIsTransferAreaAvailable(group.getTransferAreaAvailable());
            g.setTitle(group.getTitle());
            GroupDbPersister.Default.getInstance().persist(g);
        }
        catch(Exception e)
        {
            //return "Invalid description formatted text type, try: HTML/PLAIN_TEXT/SMART_TEXT";
            throw new WebServiceException("Error while trying to add group "+e.toString());
        }
        return true;
    }

    private boolean groupDelete(BBGroup group) throws WebServiceException
    {
        try
        {
            GroupDbPersister.Default.getInstance().deleteById(GroupDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Group.DATA_TYPE,group.getGroupBbId())).getId());
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: Please provide a valid Id for a group");
        }
        return true;
    }

    private List<BBGroup> groupReadByCourseId(BBCourse course) throws WebServiceException
    {
        try
        {
            return BBListFactory.getNonVerboseBBList(GroupDbLoader.Default.getInstance().loadByCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId()));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No groups found");
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private BBGroup groupRead(BBGroup group) throws WebServiceException
    {
        try
        {
            return new BBGroup(GroupDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Group.DATA_TYPE,group.getGroupBbId())));
        }
        catch(KeyNotFoundException knfe)
        {
            throw new WebServiceException("Error: The given group does not exist");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: Could not retrieve group for given Id "+e.toString());
        }
    }

    private boolean groupUpdate(BBGroup group, String descType) throws WebServiceException
    {
        try
        {
            Group g = GroupDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Group.DATA_TYPE,group.getGroupBbId()));
            g.setDescription(new FormattedText(group.getDescription(),Type.fromFieldName(descType)));
            g.setIsAvailable(group.getAvailable());
            g.setIsChatRoomAvailable(group.getChatRoomsAvailable());
            g.setIsDiscussionBoardAvailable(group.getDiscussionBoardsAvailable());
            g.setIsEmailAvailable(group.getEmailAvailable());
            g.setIsTransferAreaAvailable(group.getTransferAreaAvailable());
            g.setTitle(group.getTitle());
            GroupDbPersister.Default.getInstance().persist(g);
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
        return true;
    }

    private List<BBGroupMembership> groupMembershipReadByGroupId(BBGroup group) throws WebServiceException
    {
        try
        {
           return BBListFactory.getNonVerboseBBList(GroupMembershipDbLoader.Default.getInstance().loadByGroupId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Group.DATA_TYPE,group.getGroupBbId())));
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No group memberships found");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error: "+e.toString());
        }
    }

    private boolean groupMembershipCreateByUserIdAndGroupId(BBUser user, BBGroup group) throws WebServiceException
    {
        try
        {
            Group g = GroupDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Group.DATA_TYPE,group.getGroupBbId()));
            GroupMembership gm = new GroupMembership();
            gm.setCourseMembershipId(CourseMembershipDbLoader.Default.getInstance().loadByCourseAndUserId(g.getCourseId(),UserDbLoader.Default.getInstance().loadByUserName(user.getBbId()).getId()).getId());
            gm.setGroupId(g.getId());
            GroupMembershipDbPersister.Default.getInstance().persist(gm);
        }
        catch(PersistenceException pe)
        {
            throw new WebServiceException("Error: Is user already part of this group?");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error while trying to add user to group: "+e.toString());
        }
        return true;
    }

    private boolean groupMembershipDeleteByUserIdAndGroupId(BBUser user, BBGroup group) throws WebServiceException
    {
        try
        {
            GroupMembershipDbPersister.Default.getInstance().deleteById(GroupMembershipDbLoader.Default.getInstance().loadByGroupAndUserId(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(Group.DATA_TYPE,group.getGroupBbId()),UserDbLoader.Default.getInstance().loadByUserName(user.getBbId()).getId()).getId());
        }
        catch(KeyNotFoundException knfe)
        {
            throw new WebServiceException("Error: User is not a member of this group");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error while attempting to delete group membership: "+e.toString());
        }
        return true;
    }

    private boolean isAMatch(Pattern pattern, String searchStr)
    {
        boolean match = false;
        Matcher matcher = pattern.matcher(searchStr);
        while (matcher.find())
        {
            //System.err.println("I found the text "+matcher.group()+" starting at index "+matcher.start()+" and ending at index "+matcher.end());
            match = true;
        }
        return match;
    }

    private List<PortalRole> parseSecondaryPortalRoles(List<BBRole> roles) throws WebServiceException
    {
        List<PortalRole> pRoles = new ArrayList<PortalRole>();
        try
        {
            PortalRoleDbLoader prl = PortalRoleDbLoader.Default.getInstance();
            Iterator<BBRole> i = roles.iterator();
            BBRole r = null;
            while(i.hasNext())
            {
                r = i.next();
                if(r.getRoleId()!=null && !r.getRoleId().trim().equalsIgnoreCase(""))
                {
                    pRoles.add(prl.loadByRoleId(r.getRoleId().trim()));
                }
            }
        }
        catch(Exception e)
        {
            throw new WebServiceException("Invalid role specified "+e.toString());
        }
        return pRoles;
    }

    private List<BBRole> roleSecondaryPortalReadByUserId(BBUser user) throws WebServiceException
    {
        List<BBRole> prl = new ArrayList<BBRole>();
        try
        {
            List<PortalRole> rl = PortalRoleDbLoader.Default.getInstance().loadSecondaryRolesByUserId(UserDbLoader.Default.getInstance().loadByUserName(user.getUserName()).getId());
            Iterator<PortalRole> i = rl.iterator();

            while(i.hasNext())
            {
                prl.add(new BBRole(i.next()));
            }
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
        return prl;
    }

    private Boolean roleSecondaryPortalUpdate(BBUser user, List<BBRole> roles) throws WebServiceException
    {
        //if roles.Length == 0 it will simply delete any existing roles
        try
        {
            User u = UserDbLoader.Default.getInstance().loadByUserName(user.getUserName(),null,true);
            PortalRole priPR = u.getPortalRole();
            UserRoleDbPersister prstr = UserRoleDbPersister.Default.getInstance();
            prstr.deleteAllByUserId(u.getId());

            Iterator<PortalRole> i = parseSecondaryPortalRoles(roles).iterator();
            PortalRole pr = null;
            UserRole ur = null;
            while(i.hasNext())
            {
                ur = new UserRole();
                ur.setUser(u);
                pr = i.next();

                if(!pr.getRoleID().equalsIgnoreCase(priPR.getRoleID()))
                {
                    ur.setPortalRoleId(pr.getId());
                    prstr.persist(ur);
                }
            }
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
        return true;
    }

    private List<BBRole> roleSecondarySystemReadByUserId(BBUser user) throws WebServiceException
    {
        List<BBRole> srl = new ArrayList<BBRole>();
        try
        {
	    List<SystemRole> rl = DomainManagerFactory.getInstance().getDefaultDomainRolesForUser(user.getUserName());
            Iterator<SystemRole> i = rl.iterator();

            while(i.hasNext())
            {
                srl.add(new BBRole(i.next()));
            }
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
        return srl;
    }

    private BBRole roleUserReadByUserIdAndCourseId(BBUser user, BBCourse course) throws WebServiceException
    {
        try
        {
            return new BBRole(CourseMembershipDbLoader.Default.getInstance().loadByCourseAndUserId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId(),UserDbLoader.Default.getInstance().loadByUserName(user.getUserName()).getId()));
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
    }

    private Boolean setOrModifyCourseRegistryValue(Id crsId, String regKey, String value) throws Exception
    {
        CourseRegistryEntry entry = null;
        try
        {
            entry = CourseRegistryEntryDbLoader.Default.getInstance().loadByKeyAndCourseId(regKey,crsId);
        }
        catch(Exception e)
        {
            //ignore this error. if there is an exception, it means entry not found for this course,
            entry = new CourseRegistryEntry();
            entry.setCourseId(crsId);
            entry.setKey(regKey);
            entry.validate();
        }
        entry.setValue(value);
        CourseRegistryEntryDbPersister.Default.getInstance().persist(entry);
        return true;
    }

    private Boolean setOrModifySecondaryPortalRolesForGivenUserId(String userId, List<PortalRole> roles) throws WebServiceException
    {
   	    //if roles.Length == 0 it will simply delete any existing roles
	    try
	    {
                User u = UserDbLoader.Default.getInstance().loadByUserName(userId,null,true);
                PortalRole priPR = u.getPortalRole();
                UserRoleDbPersister prstr = UserRoleDbPersister.Default.getInstance();
                prstr.deleteAllByUserId(u.getId());
                PortalRole pr = null;
                UserRole ur = null;

                for(int i=0; i<roles.size();i++)
                {
                    ur = new UserRole();
                    ur.setUser(u);
                    pr = roles.get(i);

                    if(!pr.getRoleID().equalsIgnoreCase(priPR.getRoleID()))
                    {
                        ur.setPortalRoleId(pr.getId());
                        prstr.persist(ur);
                    }
                }
	    }
	    catch(Exception e)
	    {
                //return "Error: could not set secondary roles for user "+e.toString();
                throw new WebServiceException(e.toString()+": "+e.getMessage());
	    }
	    return true;
    }

    private Boolean userCreateOrUpdate(BBUser user,BBRole portalRole,List<BBRole> secPortalRoles,BBRole systemRole,Boolean isUpdate) throws WebServiceException
    {
        Person p = null;
        if(isUpdate)
        {
            try
            {
                p = PersonDbLoader.Default.getInstance().load(user.getUserName());
            }
            catch(KeyNotFoundException knfe)
            {
                //We need the user to exist
                throw new WebServiceException("User "+user.getUserName()+" does not exist");
            }
            catch(Exception e)
            {
                //return "Error while trying to check if user already exists: "+e;
                throw new WebServiceException(e.toString()+": "+e.getMessage());
            }

        }
        else
        {
            try
            {
                PersonDbLoader.Default.getInstance().load(user.getUserName());
                //return "Error: User may already exist";
                throw new WebServiceException("User may already exist");
            }
            catch(KeyNotFoundException knfe){} //We need the user to not exist
            catch(Exception e)
            {
                //return "Error while trying to check if user already exists: "+e;
                throw new WebServiceException(e.getMessage());
            }
            p = new Person();
        }
        List<PortalRole> secPRoles = null;
        String debug = "setting user name";
        try
	{
            p.setUserName(checkAndTrimParam(user.getUserName()));//userid
            debug = "setting batch uid";
            p.setBatchUid(checkAndTrimParam(user.getBatchUserBbId()));//batchuid
            debug = "setting given name";
            p.setGivenName(checkAndTrimParam(user.getGivenName()));//firstname
            debug = "setting middle name";
            try{p.setMiddleName(checkAndTrimParam(user.getMiddleName()));}catch(Exception e){}//middlename - Catch Exception as it's not a mandatory field
            debug = "setting family name";
            p.setFamilyName(checkAndTrimParam(user.getFamilyName()));//lastname
            debug = "setting email address";
            p.setEmailAddress(user.getEmailAddress());//emailaddress
            debug = "setting student id";
            try{p.setStudentId(checkAndTrimParam(user.getStudentId()));}catch(Exception e){}//studentid - Catch Exception as it's not a mandatory field
            debug = "setting password";
            p.setPassword(SecurityUtil.getHashValue(user.getPassword()));//password - The password in blackboard is irrelevant if you're using ldap
            debug = "setting gender";
            try//gender - Is this working?
            {
                p.setGender(Gender.fromExternalString(user.getGender().trim().toUpperCase()));
            }
            catch(Exception e)
            {
                p.setGender(Gender.UNKNOWN);
            }
            debug = "setting birthdate";
            try//birthdate
            {
                p.setBirthDate(new GregorianCalendar(Integer.parseInt(user.getBirthDate().substring(0,3)),Integer.parseInt(user.getBirthDate().substring(5, 7))-1,Integer.parseInt(user.getBirthDate().substring(9, 11))));
            }catch(Exception e){}
            debug = "setting education level";
            try//Education Level
            {
                p.setEducationLevel(EducationLevel.fromExternalString(user.getEducationLevel().trim().toUpperCase()));
            }
            catch(Exception e)
            {
                p.setEducationLevel(EducationLevel.UNKNOWN);
            }
            debug = "setting company";
            try{p.setCompany(checkAndTrimParam(user.getCompany()));}catch(Exception e){}//Company - Catch Exception as it's not a mandatory field
            debug = "setting job title";
            try{p.setJobTitle(checkAndTrimParam(user.getJobTitle()));}catch(Exception e){}//Job Title - Catch Exception as it's not a mandatory field
            debug = "setting department";
            try{p.setDepartment(checkAndTrimParam(user.getDepartment()));}catch(Exception e){}//Department - Catch Exception as it's not a mandatory field
            debug = "setting street1";
            try{p.setStreet1(checkAndTrimParam(user.getStreet1()));}catch(Exception e){}//Street 1 - Catch Exception as it's not a mandatory field
            debug = "setting street2";
            try{p.setStreet2(checkAndTrimParam(user.getStreet2()));}catch(Exception e){}//Street 2 - Catch Exception as it's not a mandatory field
            debug = "setting city";
            try{p.setCity(checkAndTrimParam(user.getCity()));}catch(Exception e){}//City - Catch Exception as it's not a mandatory field
            debug = "setting state or province";
            try{p.setState(checkAndTrimParam(user.getStateOrProvince()));}catch(Exception e){}//State / Province - Catch Exception as it's not a mandatory field
            debug = "setting zip or post code";
            try{p.setZipCode(checkAndTrimParam(user.getPostCode()));}catch(Exception e){}//Zip / Postal Code - Catch Exception as it's not a mandatory field
            debug = "setting country";
            try{p.setCountry(checkAndTrimParam(user.getCountry()));}catch(Exception e){}//Country - Catch Exception as it's not a mandatory field
            debug = "setting website";
            try{p.setWebPage(checkAndTrimParam(user.getWebPage()));}catch(Exception e){}//Website - Catch Exception as it's not a mandatory field
            debug = "setting home phone";
            try{p.setHomePhone1(checkAndTrimParam(user.getHomePhone1()));}catch(Exception e){}//Home Phone - Catch Exception as it's not a mandatory field
            debug = "setting work phone";
            try{p.setHomePhone2(checkAndTrimParam(user.getHomePhone2()));}catch(Exception e){}//Work Phone - Catch Exception as it's not a mandatory field
            debug = "setting work fax";
            try{p.setHomeFax(checkAndTrimParam(user.getHomeFax()));}catch(Exception e){}//Work Fax - Catch Exception as it's not a mandatory field
            debug = "setting mobile phone";
            try{p.setMobilePhone(checkAndTrimParam(user.getMobilePhone()));}catch(Exception e){}//Mobile Phone - Catch Exception as it's not a mandatory field
            debug = "setting portal role";
            //Portal Role
            PortalRole pr = null;
            if(portalRole.getRoleId()!=null && !portalRole.getRoleId().equalsIgnoreCase(""))
            {
                portalRole.setRoleId(portalRole.getRoleId().trim());
                pr = PortalRoleDbLoader.Default.getInstance().loadByRoleId(portalRole.getRoleId());

            }
            else
            {
                pr = PortalRoleDbLoader.Default.getInstance().loadByRoleId("STUDENT");
            }
            p.setPortalRole(pr);
            debug = "setting system role";
            //System Role
            if(systemRole.getRoleId()!=null && !systemRole.getRoleId().equalsIgnoreCase(""))
            {
                p.setSystemRole(blackboard.data.user.User.SystemRole.fromFieldName(systemRole.getRoleId().trim().toUpperCase()));
            }
            else
            {
                p.setSystemRole(blackboard.data.user.User.SystemRole.NONE);
            }
            debug = "setting available";
            if(user.getIsAvailable()!=null){p.setIsAvailable(user.getIsAvailable());}else{throw new Exception("Invalid availability");}//Available
            debug = "setting row status";
            p.setRowStatus(RowStatus.ENABLED);
            debug = "setting rec status";
            p.setRecStatus(RecStatus.ADD);
            //p.setReplacementBatchUid(userId);
            debug = "checking secondary portal roles, number specified="+secPortalRoles.size();
            Iterator<BBRole> i = secPortalRoles.iterator();
            while(i.hasNext())
            {
                debug += ", "+i.next();
            }
            //parse valid secondary roles, assuming any roles are specified then they
            //must be valid or the user is not added. No specified roles, null or blank roles are ignored.
            secPRoles = parseSecondaryPortalRoles(secPortalRoles);
            debug = "exiting try to persist user, you really shouldn't see this!";
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.toString()+" (hint: code was in the process of... "+debug+")");
        }

        try
        {
            if (isUpdate)
            {
                PersonDbPersister.Default.getInstance().update(p);
            }
            else
            {
                PersonDbPersister.Default.getInstance().insert(p);
            }
        }
        catch(ConstraintViolationException cve)
        {
            throw new WebServiceException("The user you're trying to add may already exist");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error while trying to add user: "+e.toString());
        }

        /******
         * Following must be set AFTER user is created as you need User.Id
         * in order to set their UserRoles
         *****/

        //Secondary Portal Roles
        if(secPRoles!=null && secPRoles.size()>0)
        {
            try
            {
                setOrModifySecondaryPortalRolesForGivenUserId(user.getUserName(),secPRoles);
            }
            catch(Exception e)
            {
                throw new WebServiceException(e.toString());
            }
        }
        return true;
    }

    private boolean userDelete(BBUser user)
    {
        String error = "";
        try
        {
            if(checkParam(user.getUserName()))
            {
                UserDbPersister.Default.getInstance().deleteById(UserDbLoader.Default.getInstance().loadByUserName(user.getUserName()).getId());
                return true;
            }
            else if(checkParam(user.getBbId()))
            {
                UserDbPersister.Default.getInstance().deleteById(UserDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(User.DATA_TYPE,user.getBbId())).getId());
                return true;
            }
            error = "You must specify either userId or userBBId";
        }
        catch(KeyNotFoundException knfe)
        {
            error = "No matching user";
        }
        catch(Exception e)
        {
            error = "Error whilst deleting user: "+e.toString();
        }
        throw new WebServiceException(error);
    }

    private BBUser userRead(BBUser user, BBUser.BBUserVerbosity verbosity) throws WebServiceException
    {
        String error = "";
        try
        {
            if(user.getUserName()!=null && !user.getUserName().equalsIgnoreCase(""))
            {
                return new BBUser(UserDbLoader.Default.getInstance().loadByUserName(user.getUserName()),verbosity);
            }
            else if(user.getBbId()!=null && !user.getBbId().equalsIgnoreCase(""))
            {
                return new BBUser(UserDbLoader.Default.getInstance().loadById(BbServiceManager.getPersistenceService().getDbPersistenceManager().generateId(User.DATA_TYPE, user.getBbId())),verbosity);
            }
            error = "You must specify either userId or userBBId";
        }
        catch(KeyNotFoundException knfe)
        {
            error = "No matching user";
        }
        catch(Exception e)
        {
            error = "Error whilst finding user: "+e.toString();
        }
        throw new WebServiceException(error);
    }

    private List<BBUser> userReadAll(BBUser.BBUserVerbosity verbosity)
    {
        try
        {
	    Person p = new Person();
	    p.setBatchUid("%%");
            //return getBBUserListFromList(PersonDbLoader.Default.getInstance().load(p),verbosity);
            return BBListFactory.getBBUserListFromList(PersonDbLoader.Default.getInstance().load(p), verbosity);
        }
        catch(EmptyListException ele)
        {
            throw new WebServiceException("No users found");
        }
        catch(Exception e)
        {
            throw new WebServiceException("Error whilst searching to see if course exists: "+e.toString());
        }
    }

    private List<BBUser> userReadByCourseIdAndCMRole(BBCourse course, BBCourseMembershipRole cmRole, BBUser.BBUserVerbosity verbosity) throws WebServiceException
    {
        List<BBUser> rl = new ArrayList<BBUser>();
        try
        {
            //This may require heavy loading instead of lightweight
            List<CourseMembership> cml = CourseMembershipDbLoader.Default.getInstance().loadByCourseIdAndRole(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId(),CourseMembership.Role.fromExternalString(cmRole.name()),null,true);
	    if(cml.size()>0)
	    {
		Iterator<CourseMembership> i = cml.iterator();
		while(i.hasNext())
		{
		    rl.add(new BBUser(i.next().getUser(),verbosity));
		}
		return rl;
	    }
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
        return rl;
    }

    private List<BBUser> userReadByCourseId(BBCourse course, BBUser.BBUserVerbosity verbosity) throws WebServiceException
    {
        List<BBUser> al = new ArrayList<BBUser>();
        try
        {
            List<CourseMembership> membershipList = CourseMembershipDbLoader.Default.getInstance().loadByCourseId(CourseDbLoader.Default.getInstance().loadByCourseId(course.getCourseId()).getId(),null,true);
            Iterator<CourseMembership> i = membershipList.iterator();
            User u = null;
            while(i.hasNext())
            {
                u = i.next().getUser();
                try{al.add(new BBUser(u,verbosity));}catch(Exception e){System.out.println("Error while instantiating user "+u.getUserName()+": "+e.getMessage());}
            }
        }
        catch(Exception e)
        {
            throw new WebServiceException(e.getMessage());
        }
        return al;
    }

    /**
     * This method is provided as a convenience to developers
     *
     * Usage: If creating new methods, replace...
     *
     * authoriseMethod(magKey,"bbMyMethod");
     *
     * with...
     *
     * authoriseMethod(magKey,getMethodName());
     *
     * @return Name of the current method
     */
    private String getMethodName()
    {
        Throwable throwable = new Throwable();
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[1];  // index is 1 to get the name of the method that called this method
        return stackTraceElement.getMethodName();
    }
}