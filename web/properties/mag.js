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
