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
package bbws.discussionboard;

import blackboard.data.discussionboard.Message;

public class BBMessage
{
    private String forumBbId;
    private String messageBbId;
    private String parentMessageBbId;
    private String subject;
    private String text;

    public BBMessage(){}
    public BBMessage(Message m)
    {
	this.forumBbId = m.getForumId().toExternalString();
	this.messageBbId = m.getId().toExternalString();
	this.parentMessageBbId = m.getParentId().toExternalString();
	this.subject = m.getSubject();
	this.text = m.getBody().getText();
    }

    private String[] getMessageDetails()
    {
	return new String[]{
	    this.messageBbId,
	    this.parentMessageBbId,
	    this.forumBbId,
	    this.subject,
	    this.text
	};
    }

    public String getForumBbId()
    {
	return this.forumBbId;
    }

    public void setForumBbId(String forumBbId)
    {
	this.forumBbId = forumBbId;
    }

    public String getMessageBbId()
    {
	return this.messageBbId;
    }

    public void setMessageBbId(String messageBbId)
    {
	this.messageBbId = messageBbId;
    }

    public String getParentMessageBbId()
    {
	return this.parentMessageBbId;
    }

    public void setParentMessageBbId(String parentMessageBbId)
    {
	this.parentMessageBbId = parentMessageBbId;
    }

    public String getSubject()
    {
	return this.subject;
    }

    public void setSubject(String subject)
    {
	this.subject = subject;
    }

    public String getText()
    {
	return this.text;
    }

    public void setText(String text)
    {
	this.text = text;
    }

}
