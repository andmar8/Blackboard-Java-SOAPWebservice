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

package bbws.user;

import blackboard.data.role.PortalRole;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.role.PortalRoleDbLoader;
import blackboard.platform.BbServiceManager;
import java.util.Calendar;

public class BBUser
{
    public enum BBUserVerbosity{minimal,standard,extended}

    private BBUserVerbosity verbosity;
    //standard details
    private String bbId;
    private String userName;
    private String givenName;
    private String middleName;
    private String familyName;
    private String emailAddress;
    private String lastLogin;
    private String roleName;
    private String systemRole;
    //extended details
    private String batchUserBbId;
    private String dataSourceBbId;
    private String studentId;
    private String title;
    private String systemRoleId;
    private String portalRoleId;
    private String gender;
    private String birthDate;
    private String educationLevel;
    private String jobTitle;
    private String company;
    private String department;
    private String street1;
    private String street2;
    private String city;
    private String county;
    private String country;
    private String postCode;
    private String businessPhone1;
    private String businessPhone2;
    private String mobilePhone;
    private String homePhone1;
    private String homePhone2;
    private String businessFax;
    private String homeFax;
    private String webPage;
    private String cardNumber;
    private String cdROMDriveMac;
    private String cdROMDrivePC;
    private Boolean showAddContactInfo;
    private Boolean showAddressInfo;
    private Boolean showEmailInfo;
    private Boolean showWorkInfo;
    private Boolean isAvailable;
    private Boolean isInfoPublic;
    private String modifiedDate;
    private String locale;
    private String password;
    private String stateOrProvince;

    //standard details

    public String getBbId() {
        return bbId;
    }

    public void setBbId(String newId) {
        bbId = newId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String newGivenName) {
        givenName = newGivenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String newMiddleName) {
        middleName = newMiddleName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String newFamilyName) {
        familyName = newFamilyName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String newEmailAddress) {
        emailAddress = newEmailAddress;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String newLastLogin) {
        lastLogin = newLastLogin;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String newRoleName) {
        roleName = newRoleName;
    }

    public String getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(String newSystemRole) {
        systemRole = newSystemRole;
    }

    public String getBatchUserBbId() {
        return batchUserBbId;
    }

    //extended details

    public void setBatchUserBbId(String batchUserBbId) {
        this.batchUserBbId = batchUserBbId;
    }

    public String getDataSourceBbId() {
        return dataSourceBbId;
    }

    public void setDataSourceBbId(String dataSourceBbId) {
        this.dataSourceBbId = dataSourceBbId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSystemRoleId() {
        return systemRoleId;
    }

    public void setSystemRoleId(String systemRoleId) {
        this.systemRoleId = systemRoleId;
    }

    public String getPortalRoleId() {
        return portalRoleId;
    }

    public void setPortalRoleId(String portalRoleId) {
        this.portalRoleId = portalRoleId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getBusinessPhone1() {
        return businessPhone1;
    }

    public void setBusinessPhone1(String businessPhone1) {
        this.businessPhone1 = businessPhone1;
    }

    public String getBusinessPhone2() {
        return businessPhone2;
    }

    public void setBusinessPhone2(String businessPhone2) {
        this.businessPhone2 = businessPhone2;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone1() {
        return homePhone1;
    }

    public void setHomePhone1(String homePhone1) {
        this.homePhone1 = homePhone1;
    }

    public String getHomePhone2() {
        return homePhone2;
    }

    public void setHomePhone2(String homePhone2) {
        this.homePhone2 = homePhone2;
    }

    public String getBusinessFax() {
        return businessFax;
    }

    public void setBusinessFax(String businessFax) {
        this.businessFax = businessFax;
    }

    public String getHomeFax() {
        return homeFax;
    }

    public void setHomeFax(String homeFax) {
        this.homeFax = homeFax;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCdROMDriveMac() {
        return cdROMDriveMac;
    }

    public void setCdROMDriveMac(String cdROMDriveMac) {
        this.cdROMDriveMac = cdROMDriveMac;
    }

    public String getCdROMDrivePC() {
        return cdROMDrivePC;
    }

    public void setCdROMDrivePC(String cdROMDrivePC) {
        this.cdROMDrivePC = cdROMDrivePC;
    }

    public Boolean getShowAddContactInfo() {
        return showAddContactInfo;
    }

    public void setShowAddContactInfo(Boolean showAddContactInfo) {
        this.showAddContactInfo = showAddContactInfo;
    }

    public Boolean getShowAddressInfo() {
        return showAddressInfo;
    }

    public void setShowAddressInfo(Boolean showAddressInfo) {
        this.showAddressInfo = showAddressInfo;
    }

    public Boolean getShowEmailInfo() {
        return showEmailInfo;
    }

    public void setShowEmailInfo(Boolean showEmailInfo) {
        this.showEmailInfo = showEmailInfo;
    }

    public Boolean getShowWorkInfo() {
        return showWorkInfo;
    }

    public void setShowWorkInfo(Boolean showWorkInfo) {
        this.showWorkInfo = showWorkInfo;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Boolean getIsInfoPublic() {
        return isInfoPublic;
    }

    public void setIsInfoPublic(Boolean isInfoPublic) {
        this.isInfoPublic = isInfoPublic;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public BBUser(){}
    public BBUser(BBUserVerbosity verbosity)
    {
	this.verbosity = verbosity;
    }
    public BBUser(User bbUser, BBUserVerbosity verbosity) throws Exception
    {
	this.verbosity = verbosity;
	switch(this.verbosity)
	{
	    case minimal:
		this.userName = bbUser.getUserName();
		return;
	    case extended:
		batchUserBbId = bbUser.getBatchUid();
		dataSourceBbId = bbUser.getDataSourceId().toExternalString();
		userName = bbUser.getUserName();
		studentId = bbUser.getStudentId();
		title = bbUser.getTitle();
		systemRoleId = bbUser.getSystemRoleIdentifier();
		portalRoleId = bbUser.getPortalRoleId().toExternalString();
		gender = bbUser.getGender().toFieldName();
		birthDate = extractBirthDate(bbUser);
		educationLevel =  bbUser.getEducationLevel().toFieldName();
		jobTitle = bbUser.getJobTitle();
		company = bbUser.getCompany();
		department = bbUser.getDepartment();
		street1 = bbUser.getStreet1();
		street2 = bbUser.getStreet2();
		city = bbUser.getCity();
		county = bbUser.getState();
		country = bbUser.getCountry();
		postCode = bbUser.getZipCode();
		businessPhone1 = bbUser.getBusinessPhone1();
		businessPhone2 = bbUser.getBusinessPhone2();
		mobilePhone = bbUser.getMobilePhone();
		homePhone1 = bbUser.getHomePhone1();
		homePhone2 = bbUser.getHomePhone2();
		businessFax = bbUser.getBusinessFax();
		homeFax = bbUser.getHomeFax();
		webPage = bbUser.getWebPage();
		cardNumber = bbUser.getCardNumber();
		cdROMDriveMac = bbUser.getCDRomDriveMac();
		cdROMDrivePC = bbUser.getCDRomDrivePC();
		//user.getCreatedDate()

		showAddContactInfo = bbUser.getShowAddContactInfo();
		showAddressInfo = bbUser.getShowAddressInfo();
		showEmailInfo = bbUser.getShowEmailInfo();
		showWorkInfo = bbUser.getShowWorkInfo();
		isAvailable = bbUser.getIsAvailable();
		isInfoPublic = bbUser.getIsInfoPublic();

		modifiedDate = extractModifiedDate(bbUser);
		locale = bbUser.getLocale();
                password = bbUser.getPassword();
                stateOrProvince = bbUser.getState();
	    case standard:
		bbId = bbUser.getId().toExternalString();
		givenName = bbUser.getGivenName();
		middleName = bbUser.getMiddleName();
		familyName = bbUser.getFamilyName();
		emailAddress = bbUser.getEmailAddress();
		lastLogin = retrieveLastLogin((Calendar) bbUser.getLastLoginDate());
		roleName = retrieveRoleName(bbUser.getId());
		systemRole = retrieveSystemRole(bbUser);
		return;
	}
	throw new Exception("Undefined verbosity of user");
    }

    @SuppressWarnings("static-access")
    private String retrieveLastLogin(Calendar calendar) {
        try
        {
            return (calendar.get(Calendar.YEAR)+"-"+(calendar.get(calendar.MONTH)+1)+"-"+calendar.get(calendar.DAY_OF_MONTH)+" "+calendar.get(calendar.HOUR_OF_DAY)+":"+calendar.get(calendar.MINUTE)+":"+calendar.get(calendar.SECOND));
        }
        catch(Exception e)
        {
            return "Never";
        }
    }

    private String retrieveRoleName(Id id) {
        try
        {
            @SuppressWarnings("static-access")
            BbPersistenceManager bbPm = new BbServiceManager().getPersistenceService().getDbPersistenceManager();
            PortalRoleDbLoader prl = (PortalRoleDbLoader)bbPm.getLoader(PortalRoleDbLoader.TYPE);
            PortalRole pr = prl.loadPrimaryRoleByUserId(id);
            return pr.getRoleName();
        }
        catch(Exception e)
        {
            return "";
        }
    }

    private String retrieveSystemRole(User bbUser) {
        try
        {
            return bbUser.getSystemRole().toFieldName();
        }
        catch(Exception e)
        {
            return "";
        }
    }

    @SuppressWarnings("static-access")
    private String extractBirthDate(User bbUser) {
        try
        {
            Calendar gc = (Calendar)bbUser.getBirthDate();
            return gc.get(Calendar.YEAR)+"-"+(gc.get(gc.MONTH)+1)+"-"+gc.get(gc.DAY_OF_MONTH);
        }
        catch(Exception e)
        {
            return "Not Given";
        }
    }

    @SuppressWarnings("static-access")
    private String extractModifiedDate(User bbUser) {
        try
        {
            Calendar gc = (Calendar)bbUser.getModifiedDate();
            return gc.get(Calendar.YEAR)+"-"+(gc.get(gc.MONTH)+1)+"-"+gc.get(gc.DAY_OF_MONTH)+" "+gc.get(gc.HOUR_OF_DAY)+":"+gc.get(gc.MINUTE)+":"+gc.get(gc.SECOND);
        }
        catch(Exception e)
        {
            return "Never";
        }
    }
}
