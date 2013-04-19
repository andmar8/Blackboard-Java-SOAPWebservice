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

package bbws.util.security.impl;

//java
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class WSMethods
{
    static public List getWSMethods()
    {
        List<String> webMethodl = new ArrayList<String>();
        Iterator<Method> mi = Arrays.asList(bbws.BBWebService.class.getDeclaredMethods()).iterator();
        Iterator<Annotation> ai;
        java.lang.reflect.Method m;
        //Keep methods that are webservice methods
        while(mi.hasNext())
        {
            m = mi.next(); ai = Arrays.asList(m.getDeclaredAnnotations()).iterator();
            while(ai.hasNext()){if(ai.next().toString().toLowerCase().contains("webmethod")){webMethodl.add(m.getName());}}
        }
        return webMethodl;
    }
}
