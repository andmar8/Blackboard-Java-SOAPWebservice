<%@ page language="java" pageEncoding="UTF-8" import="java.util.*,bbws.util.security.properties.property.PropertyBoolean" %>
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
    if(submit==null) submit = "false";

    PropertyBoolean usePwd = new PropertyBoolean(PersistWSProperties.getProperty("pwd.use"));
    PropertyBoolean pwdSet = new PropertyBoolean(PersistWSProperties.getProperty("pwd.set"));

    if(!submit.equalsIgnoreCase("true"))
    {
%>
	<script type="text/javascript" src="md5.js"></script>
	<script type="text/javascript" src="hash.js"></script>
	    <form action="#" method="POST" name="aForm" onsubmit="preSubmit()">
	    <input type="hidden" name="submit" value="true" />
	    <bbUI:step number="1" title="Security options">
<%
		if(!usePwd.getSetting() || pwdSet.getSetting())
		{
%>
		<bbUI:dataElement label="Use passwords?" required="true">
		    <input type="radio" name="pwd.use" id="pwd.use" value="Yes" <%=usePwd.getSetting()?"checked":""%>> Yes 
		    <input type="radio" name="pwd.use" id="pwd.use" value="No"  <%=usePwd.getSetting()?"":"checked"%>> No
		</bbUI:dataElement>
<%
		if(pwdSet.getSetting())
		{
%>
		<bbUI:instructions><input type="submit" value="Change my password >>"/></bbUI:instructions>
		<input type="hidden" name="pwd.set" value="No" />
<%
		}
	}
	else
	{
%>

		<bbUI:instructions>Enter a password:</bbUI:instructions>
		<input type="hidden" name="pwd.set" value="Yes" />
		<bbUI:dataElement label="Hash password?" required="true">
		    <input type="checkbox" id="pwd.isHashed" checked />
		</bbUI:dataElement>
		<bbUI:dataElement label="Password" required="true">
		    <input type="password" name="pwd.pwd" id="pwd.pwd" maxlength="32" size="20" value="" />
		</bbUI:dataElement>
		<bbUI:instructions><input type="submit" value="<< Use existing password" onclick="cancelPwdChange()" /></bbUI:instructions>
<%
	}
%>
	    </bbUI:step>

	    <bbUI:step number="2" title="Method access">
		<bbUI:dataElement><a href="methodAccess.jsp">Change >></a></bbUI:dataElement>
            </bbUI:step>
	    <bbUI:stepSubmit number="3" cancelUrl="/webapps/blackboard/admin/manage_plugins.jsp" />
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
    <a href="/webapps/amnl-BBWebService-bb_bb60/BBWebService">Click for endpoint</a><br/>
    <a href="/webapps/amnl-BBWebService-bb_bb60/BBWebService?wsdl">Click for wsdl</a>
    </bbUI:docTemplate>
</bbData:context>