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

package bbws.content;

public abstract class AbstractBBContent
{
    protected Boolean available;
    protected String contentBbId;
    protected String dataType;
    protected Integer position;
    protected String url;

    public Boolean getAvailable()
    {
	return this.available;
    }

    public void setAvailable(Boolean available)
    {
	this.available = available;
    }

    public String getDataType()
    {
	return this.dataType;
    }

    public void setDataType(String dataType)
    {
	this.dataType = dataType;
    }

    public String getContentBbId()
    {
	return this.contentBbId;
    }

    public void setContentBbId(String contentBbId)
    {
	this.contentBbId = contentBbId;
    }

    public Integer getPosition()
    {
	return this.position;
    }

    public void setPosition(Integer position)
    {
	this.position = position;
    }

    public String getUrl()
    {
	return this.url;
    }

    public void setUrl(String url)
    {
	this.url = url;
    }
}
