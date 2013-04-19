<%@ page language="java" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.Iterator,java.util.List,bbws.util.security.properties.property.PropertyBoolean" %>
<%@ taglib uri="/bbUI" prefix="bbUI"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%
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
%>
<bbData:context>
    <bbUI:docTemplate title="Blackboard Administrator Tools">
	<bbUI:titleBar>Webservice security properties</bbUI:titleBar>
	<jsp:useBean id="PersistWSProperties" scope="session" class="bbws.util.security.properties.PropertiesBean"/>
<%
    String submit = request.getParameter("submit");
    if(submit==null){submit = "false";}
    if(!submit.equalsIgnoreCase("true"))
    {
%>
    <form action="#" method="POST" name="aForm">
	<input type="hidden" name="submit" value="true" />
	<bbUI:step title="Allow access to method?">
<%
    Iterator<String> i = new bbws.util.security.MethodAccess().getMethodsIterator();
    String currentMethod = "";
    while(i.hasNext())
    {
        currentMethod = i.next();
        PropertyBoolean methodProperty = new PropertyBoolean(PersistWSProperties.getProperty(currentMethod+".access"));
%>
                <bbUI:dataElement label="<%= currentMethod %>" required="true">
		    <input type="radio" name="<%= currentMethod+".access" %>" id="<%= currentMethod+".access" %>" value="Yes" <%= methodProperty.getSetting()?"checked":""%>> Yes
		    <input type="radio" name="<%= currentMethod+".access" %>" id="<%= currentMethod+".access" %>" value="No"  <%= methodProperty.getSetting()?"":"checked"%>> No
		</bbUI:dataElement>
<%
    }
%>
	</bbUI:step>
	<bbUI:stepSubmit cancelUrl="properties.jsp" />
    </form>
<%
    }
    else
    {
	try
	{
	    PersistWSProperties.setProperties(request);
	}catch(Exception e){}
%>
	<form action="#" method="POST" name="aForm" />
	<script type="text/javascript">
	    document.aForm.submit();
	</script>
	<bbUI:receipt recallUrl = "../properties/properties.jsp">Your settings have been saved.</bbUI:receipt>
<%
    }
%>
    </bbUI:docTemplate>
</bbData:context>
    
