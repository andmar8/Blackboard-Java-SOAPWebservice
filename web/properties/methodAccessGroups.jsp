<%@ page language="java" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.Arrays,java.util.Iterator,java.util.List,blackboard.data.user.User,blackboard.platform.context.ContextManagerFactory" %>
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
%>
<bbData:context>
    <bbUI:docTemplate title="Blackboard Administrator Tools">
	<bbUI:titleBar>Webservice Method Access Groups</bbUI:titleBar>
	<jsp:useBean id="PersistWSProperties" scope="session" class="bbws.util.security.properties.PropertiesBean"/>
<%
    if(ContextManagerFactory.getInstance().getContext().getUser().getSystemRole()==User.SystemRole.SYSTEM_ADMIN)
    {
        String persistType = request.getParameter("persistType");
        String magncsv = null;
        String magnSeperator = ";";
        List<String> magnl = null;
        Iterator<String> magni;

        //Get all Method Access Groups
        try
        {
            magncsv = PersistWSProperties.getProperty(applicationHandle,"methodAccessGroupNames");
            if(magncsv!=null&&!magncsv.equalsIgnoreCase(""))
            {
                if(magncsv.contains(magnSeperator))
                {
                    magnl = Arrays.asList(magncsv.split(magnSeperator));
                }
                else
                {
                    magnl = new ArrayList<String>();
                    magnl.add(magncsv);
                }
            }
        }catch(Exception e){}

        //If we haven't submitted anything
        if(persistType==null || persistType.equalsIgnoreCase(""))
        {
            String magName = "";
            if(magnl!=null&&!magnl.isEmpty())
            {
%>
        <script type="text/javascript">
            <!--
                function persistMAG(whattodo,mag)
                {
                    var persist = document.getElementById("persistType");
                    persist.value = whattodo;
                    setMAGAndSubmit(mag);
                }

                function goToMethods(mag)
                {
                    document.aForm.action="methodAccessForMAGKey.jsp"
                    setMAGAndSubmit(mag);
                }

                function setMAGAndSubmit(mag)
                {
                    var magHidden = document.getElementById("magname");
                    magHidden.value=mag;
                    document.aForm.submit();
                }
            //-->
        </script>
        <form action="#" method="POST" name="aForm">
            <input type="hidden" name="persistType" id="persistType" value="" />
            <input type="hidden" name="magname" id="magname" value="" />
            <bbUI:step title="Existing Method Access Groups">
<%
                magni = magnl.iterator();
                while(magni.hasNext())
                {
                    magName = magni.next();
%>
                <bbUI:dataElement canFormatLabel="true" label="<%= magName %>" labelWidth="150">
                    <p style="text-align:center">
                        New Key: <input type="password" name="<%= magName %>.key" value="" maxlength="25" />&nbsp;&nbsp;
                        <input type="button" name="change" value="Change Key" onclick="persistMAG('change','<%= magName %>')" />
                        <input type="button" name="methods" value="Change Method Access Permissions" onclick="goToMethods('<%= magName %>')" />
                        <input type="button" name="remove" value="Remove" onclick="persistMAG('remove','<%= magName %>')" />
                    </p>
                </bbUI:dataElement>
<%
                }
%>            </bbUI:step>
        </form>
<%
            }
%>
        <form action="#" method="POST" name="bForm">
            <bbUI:step title="Add a Method Access Group">
                    <p style="text-align:center">
                        * Both the Method Access Group's Name and Key must be entirely unique to the system<br/>
                        * The Method Access Group Name MUST be alphanumeric ONLY (a-z, A-Z, 0-9)<br/>
                        * The Method Access Group Key can be alphanumeric and/or containing ! ? & $ £ ( ) _ - +                     </p>
                    <p style="text-align:center">
                        Name:&nbsp;<input type="text" name="newMAGName" value="" maxlength="20"/>
                        Key:&nbsp;<input type="password" name="newMAGKey" value="" maxlength="25"/>
                        <input type="submit" name="persistType" value="Add"/>
                    </p>
            </bbUI:step>
        </form>
        <p style="text-align:center">
<%
            String bbUid = ContextManagerFactory.getInstance().getContext().getVirtualInstallation().getBbUid();
%>
            <a href="/webapps/amnl-BBWebService-<%= bbUid %>/BBWebService">Click for endpoint</a>&nbsp;/&nbsp;
            <a href="/webapps/amnl-BBWebService-<%= bbUid %>/BBWebService?wsdl">Click for wsdl</a><br/>
            <a href="/webapps/blackboard/admin/manage_plugins.jsp">Click to exit</a>
        </p>
<%
        }
        else //Persist changes
        {
%>
        <h3>Please wait...</h3>
	<form action="#" method="POST" name="aForm">
<%
            boolean persist = true;

            //Add a new MAG
            if(persistType.equalsIgnoreCase("add"))
            {
                persist=false;
                String newMAGName = request.getParameter("newMAGName");
                if(newMAGName!=null&&!newMAGName.trim().equalsIgnoreCase(""))
                {
                    newMAGName = newMAGName.trim();
                    String newMAGKey = request.getParameter("newMAGKey");
                    if(newMAGKey!=null)
                    {
                        newMAGKey = newMAGKey.trim();
                        if(newMAGName.matches("[a-zA-Z0-9]*")&&newMAGKey.matches("[a-zA-Z0-9\\!\\?\\&\\$\\£\\(\\)\\_\\-\\+]*"))
                        {
                            //If any MAGs exist
                            if(magnl!=null&&!magnl.isEmpty())
                            {
                                String methodAccessGroupName;
                                Boolean doesNotMatch = true;
                                magni = magnl.iterator();
                                while(magni.hasNext())
                                {
                                    methodAccessGroupName = magni.next();
                                    if(newMAGName.equalsIgnoreCase(methodAccessGroupName)||newMAGName.equals(PersistWSProperties.getProperty(applicationHandle,methodAccessGroupName+".magkey")))
                                    {
                                        doesNotMatch = false;
                                        break;
                                    }
                                }
                                if(doesNotMatch)
                                {
                                    String methodAccessGroupNames = magncsv+magnSeperator+newMAGName;
%>
            <input type="hidden" name="persistType" value="addPersist" />
            <input type="hidden" name="methodAccessGroupNames" value="<%= methodAccessGroupNames %>" />
            <input type="hidden" name="<%= newMAGName %>.magkey" value="<%= newMAGKey %>" />
<%
                                    //Submit new stuff
                                }
                            }
                            else //MAGNames property doesn't exist!
                            {
%>
            <input type="hidden" name="persistType" value="addPersist" />
            <input type="hidden" name="methodAccessGroupNames" value="<%= newMAGName %>" />
            <input type="hidden" name="<%= newMAGName %>.magkey" value="<%= newMAGKey %>" />
<%
                            }
                        }
                    }
                    //Submit nothing
                }
            }
            else if(persistType.equalsIgnoreCase("change")) //change MAG key
            {
                persist=false;
                /*java.util.Enumeration m = request.getParameterNames();
                String p;
                while(m.hasMoreElements())
                {
                    p = m.nextElement().toString();
                    System.err.println(p+":"+request.getParameter(p));
                }*/
                String magName = "";
                String changedMAGKey = "";
                Boolean doesNotMatch = true;
                try
                {
                    magName = request.getParameter("magname");
                    //System.err.println("mName: "+magName);
                    changedMAGKey = request.getParameter(magName+".key");
                    //System.err.println("cKey: "+changedMAGKey);
                    if(magName!=null&&!magName.equalsIgnoreCase("")&&changedMAGKey.matches("[a-zA-Z0-9\\!\\?\\&\\$\\£\\(\\)\\_\\-\\+]*"))
                    {
                        //System.err.println("inside if");
                        List<String> magkl = new ArrayList<String>();
                        magni = magnl.iterator();
                        while(magni.hasNext())
                        {
                            try{magkl.add(PersistWSProperties.getProperty(applicationHandle,magni.next()+".magkey"));}catch(Exception e){}
                        }
                        Iterator<String> magki = magkl.iterator();
                        //This doesnt account for ""????
                        while(magki.hasNext()){if(magki.next()==changedMAGKey){doesNotMatch = false;break;}}
                    }else{doesNotMatch = false;}
                }catch(Exception e){doesNotMatch = false;}//Just in case
                if(doesNotMatch)
                {
%>
            <input type="hidden" name="persistType" value="changePersist" />
            <input type="hidden" name="<%= magName %>.magkey" value="<%= changedMAGKey %>" />
<%
                }
            }
            else if(persistType.equalsIgnoreCase("remove"))
            {
                persist=false;
                String magNameToDelete = request.getParameter("magname");
                //System.err.println("attempt to remove MAG: "+magNameToDelete);
                if(magNameToDelete!=null&&!magNameToDelete.trim().equalsIgnoreCase(""))
                {
                    magNameToDelete = magNameToDelete.trim();
                    //If any MAGs exist
                    if(magnl!=null&&!magnl.isEmpty())
                    {
                        String methodAccessGroupNames = "";
                        String methodAccessGroupName;
                        Boolean deleted = false;
                        magni = magnl.iterator();
                        //System.err.println("Current MAGNames csv: "+magncsv);
                        while(magni.hasNext())
                        {
                            methodAccessGroupName = magni.next();
                            if(magNameToDelete.equalsIgnoreCase(methodAccessGroupName))
                            {
                                deleted = true;
                            }
                            else
                            {
                                //System.err.println("new MAGnames csv in while else: "+methodAccessGroupNames);
                                if(methodAccessGroupNames.equalsIgnoreCase(""))
                                {
                                    //System.err.println("new MAGnames csv in while else if: "+methodAccessGroupNames);
                                    methodAccessGroupNames=methodAccessGroupName;
                                }
                                else
                                {
                                    methodAccessGroupNames+=magnSeperator+methodAccessGroupName;
                                }
                            }
                            //System.err.println("new MAGnames csv in while: "+methodAccessGroupNames);
                        }
                        if(deleted)
                        {
%>
            <input type="hidden" name="persistType" value="removePersist" />-->
            <input type="hidden" name="methodAccessGroupNames" value="<%= methodAccessGroupNames %>" />
            <input type="hidden" name="<%= magNameToDelete %>.magkey" value="<%= new java.util.Random().nextLong()*10000000 %>" />
<%
                        }
                        //System.err.println("new MAGnames csv: "+methodAccessGroupNames);
                        //System.err.println("Found anything to delete? "+deleted);
                    }
                    //Submit nothing
                }

            }
            //Else: Persist!
               //Requests must be sent from this server
            //System.err.println("rAddr==lAddr:"+request.getRemoteAddr()==java.net.InetAddress.getLocalHost().getHostAddress());
            //System.err.println("rAddr:"+request.getRemoteAddr());
            //System.err.println("lAddr:"+java.net.InetAddress.getLocalHost().getHostAddress());
            if(persist){try{PersistWSProperties.setProperties(applicationHandle,request);}catch(Exception e){}}
            //System.err.println("persist: "+persist);
%>
        </form>
	<script type="text/javascript">
        <!--
            document.aForm.submit();
        //-->
	</script>
	<bbUI:receipt recallUrl = "../properties/methodAccessGroups.jsp">Your settings have been saved.</bbUI:receipt>
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

