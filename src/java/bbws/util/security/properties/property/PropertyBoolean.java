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
package bbws.util.security.properties.property;

public class PropertyBoolean
{
    private boolean setting = false;

    private PropertyBoolean(){}
    public PropertyBoolean(String setting)
    {
        this.setting = (setting!=null&&setting.toString().equals("Yes"))?true:false;
    }

    public boolean getSetting()
    {
	return setting;
    }
}
