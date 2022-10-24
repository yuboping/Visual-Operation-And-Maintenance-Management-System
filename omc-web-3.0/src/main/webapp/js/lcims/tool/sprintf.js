define(function (require) {

function sprintf(text) {
    var i = 1, args = arguments;
    return text.replace(/%s/g, function () { return (i < args.length) ? nullToStr(args[i++]) : ""});
};

function nullToStr(str){
    if(str==null || str == ""){
        return "&nbsp;";
    }
    return str;
}

return {
sprintf:sprintf,
};
});