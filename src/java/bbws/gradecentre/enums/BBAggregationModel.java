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

package bbws.gradecentre.enums;

public enum BBAggregationModel
{
    AVERAGE,//("Grade of Average Attempt"),
    BEST,//("Grade of Best Attempt"),
    DEFAULT,//("Grade of Default Attempt"),
    FIRST,//("Grade of First Attempt"),
    HIGHEST,//("Grade of Highest Attempt"),
    LAST,//("Grade of Last Attempt"),
    LOWEST,//("Grade of Lowest Attempt"),
    OVERRIDEN,//("Grade of Overriden Attempt"),
    SUM,//("Grade of Sum Attempt"),
    WORST,//("Grade of Worst Attempt"),
    PROPERTYDOESNOTEXIST;//("Does Not Exist");

    //private final String displayName;
    /*BBAggregationModel(String displayName)
    {
        this.displayName = displayName;
    }*/

    /*public String getDisplayName()
    {
        return this.displayName;
    }*/

    public static BBAggregationModel valueOfSafe(String str)
    {
        try{return valueOf(str);}catch(Exception e){return PROPERTYDOESNOTEXIST;}
    }
}
