    function hashPwd(pwd)
    {
	var hashedPwd = "";
	hashedPwd = hex_md5(pwd);
	return hashedPwd;
    }

    function preSubmit()
    {
	var hash = document.getElementById("pwd.isHashed");
	if(hash.checked)
	{
	    var obj=document.aForm.elements['pwd.pwd'];
	    obj.value=hashPwd(obj.value);
	}
    }

    function cancelPwdChange()
    {
	var pwd = document.getElementById("pwd.pwd");
	pwd.disabled=true;
    }