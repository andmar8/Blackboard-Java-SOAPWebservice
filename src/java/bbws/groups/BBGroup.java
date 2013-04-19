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

//blackboard - data
import blackboard.data.course.Group;

//java
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BBGroup
{
    private Boolean available;
    private Boolean chatRoomsAvailable;
    private String description;
    private Boolean discussionBoardsAvailable;
    private Boolean emailAvailable;
    private String groupBbId;
    private String modifiedDate;
    private String title;
    private Boolean transferAreaAvailable;

    public BBGroup(){}
    public BBGroup(Group g)
    {
	this.available = g.getIsAvailable();
	this.chatRoomsAvailable = g.getIsChatRoomAvailable();
	this.description = g.getDescription().getText();
	this.discussionBoardsAvailable = g.getIsDiscussionBoardAvailable();
	this.emailAvailable = g.getIsEmailAvailable();
	this.groupBbId = g.getId().toExternalString();
	this.modifiedDate = getDateTimeFromCalendar(g.getModifiedDate());
	this.title = g.getTitle();
	this.transferAreaAvailable = g.getIsTransferAreaAvailable();
    }

    public Boolean getAvailable()
    {
	return this.available;
    }

    public void setAvailable(Boolean available)
    {
	this.available = available;
    }

    public Boolean getChatRoomsAvailable()
    {
	return this.chatRoomsAvailable;
    }

    public void setChatRoomsAvailable(Boolean chatRoomsAvailable)
    {
	this.chatRoomsAvailable = chatRoomsAvailable;
    }

    public String getDescription()
    {
	return this.description;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

    public Boolean getDiscussionBoardsAvailable()
    {
	return this.discussionBoardsAvailable;
    }

    public void setDiscussionBoardsAvailable(Boolean discussionBoardsAvailable)
    {
	this.discussionBoardsAvailable = discussionBoardsAvailable;
    }

    public Boolean getEmailAvailable()
    {
	return this.emailAvailable;
    }

    public void setEmailAvailable(Boolean emailAvailable)
    {
	this.emailAvailable = emailAvailable;
    }

    public String getGroupBbId()
    {
	return this.groupBbId;
    }

    public void setGroupBbId(String groupBbId)
    {
	this.groupBbId = groupBbId;
    }

    public String getModifiedDate()
    {
	return this.modifiedDate;
    }

    public void setModifiedDate(String modifiedDate)
    {
	this.modifiedDate = modifiedDate;
    }

    public String getTitle()
    {
	return this.title;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public Boolean getTransferAreaAvailable()
    {
	return this.transferAreaAvailable;
    }

    public void setTransferAreaAvailable(Boolean transferAreaAvailable)
    {
	this.transferAreaAvailable = transferAreaAvailable;
    }

    private String getDateTimeFromCalendar(Calendar c)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(c.getTime());
        }
        catch(Exception e)
        {
            return "";
        }
    }
}
