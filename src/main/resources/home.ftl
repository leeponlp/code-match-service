<#import "spring.ftl" as spring>
<!DOCTYPE HTML>
<html>
 <head>
  <title>系统主页</title>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 </head>
 <body>
 ${username}登陆成功！！！&nbsp;&nbsp;&nbsp;
<form action="/logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit">登出</button>
</form>
 <hr>
 <a href = "/book/view">书类管理</a></br>
 </br>
 </br>
<#if authlist?seq_contains("ROLE_ADMIN")>
  <a href = "/system/view">系统设置</a></br>
</#if>
</br>
</br>
<#list authlist as word>
    <span>${word}</span></br>
</#list>
 </body>
</html>