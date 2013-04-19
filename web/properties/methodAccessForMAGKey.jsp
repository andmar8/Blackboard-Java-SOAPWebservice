<%@ page language="java"
         pageEncoding="UTF-8"
         import="java.util.ArrayList,
                    java.util.Iterator,
                    java.util.List,
                    bbws.util.security.impl.WSMethods,
                    bbws.util.security.properties.property.PropertyBoolean,
                    blackboard.data.user.User,
                    blackboard.platform.context.ContextManagerFactory" %>
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
<%
    //Any reference to a "MAG" means "Method Access Group"
    String applicationHandle = "BBWebService";
    String methodAccessLabel = "methodAccess";
    String sslLabel = "sslReq";
    String magName = "default";
    String persist = request.getParameter("persist");
    magName = request.getParameter("magname");
%>
<bbData:context>
    <bbUI:docTemplate title="Blackboard Administrator Tools">
	<bbUI:titleBar>Webservice Methods for <%= magName %></bbUI:titleBar>
	<jsp:useBean id="PersistWSProperties" scope="session" class="bbws.util.security.properties.PropertiesBean"/>
<%
    if(ContextManagerFactory.getInstance().getContext().getUser().getSystemRole()==User.SystemRole.SYSTEM_ADMIN)
    {
        if(persist==null)
        {
%>
        <form action="#" method="POST" name="aForm">
            <input type="hidden" name="magname" value="<%= magName %>" />
            <input type="hidden" name="persist" value="true" />
            <bbUI:step title="Method settings" numbered="false" width="500">
<%
            String currentMethod = "";
            PropertyBoolean accessGroupProperty;
            PropertyBoolean sslProperty;
            Iterator<String> wmi = WSMethods.getWSMethods().iterator();
            while(wmi.hasNext())
            {
                currentMethod = wmi.next();
                accessGroupProperty = new PropertyBoolean(PersistWSProperties.getProperty(applicationHandle,magName+"."+currentMethod+"."+methodAccessLabel));
                sslProperty = new PropertyBoolean(PersistWSProperties.getProperty(applicationHandle,magName+"."+currentMethod+"."+sslLabel));
%>
                    <bbUI:dataElement canFormatLabel="true" label="<%= currentMethod %>">
                        <bbUI:dataSubElement label="Allow Access" required="true">
                            <input type="radio" name="<%= magName+"."+currentMethod+"."+methodAccessLabel %>" id="<%= magName+"."+currentMethod+"."+methodAccessLabel %>" value="Yes" <%= accessGroupProperty.getSetting()?"checked":""%>> Yes
                            <input type="radio" name="<%= magName+"."+currentMethod+"."+methodAccessLabel %>" id="<%= magName+"."+currentMethod+"."+methodAccessLabel %>" value="No"  <%= accessGroupProperty.getSetting()?"":"checked"%>> No
                        </bbUI:dataSubElement>
                        <bbUI:dataSubElement label="SSL Required" required="true">
                            <input type="radio" name="<%= magName+"."+currentMethod+"."+sslLabel %>" id="<%= magName+"."+currentMethod+"."+sslLabel %>" value="Yes" <%= sslProperty.getSetting()?"checked":""%>> Yes
                            <input type="radio" name="<%= magName+"."+currentMethod+"."+sslLabel %>" id="<%= magName+"."+currentMethod+"."+sslLabel %>" value="No"  <%= sslProperty.getSetting()?"":"checked"%>> No
                        </bbUI:dataSubElement>
                    </bbUI:dataElement>
<%
            }
%>
            </bbUI:step>
            <bbUI:stepSubmit cancelUrl="methodAccessGroups.jsp" />
        </form>
<%
        }
        else
        {
            try{PersistWSProperties.setProperties(applicationHandle,request);}catch(Exception e){}
%>
        <h3>Persisting method options, please wait...</h3>
	<form action="#" method="POST" name="aForm">
            <input type="hidden" name="magname" value="<%= magName %>" />
        </form>
	<script type="text/javascript">
        <!--
	    document.aForm.submit();
        //-->
	</script>
	<bbUI:receipt recallUrl = "../properties/methodAccessForMAGKey.jsp">Your settings have been saved.</bbUI:receipt>
<%
        }
    }
    else
    {
%>
    <h1 style="text-align: center">Unauthorised access</h1>
<%
    }
%>
    </bbUI:docTemplate>
</bbData:context>