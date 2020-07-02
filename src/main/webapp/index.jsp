<%--
  Created by IntelliJ IDEA.
  User: 策
  Date: 2020/6/29
  Time: 23:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>尚筹网—秒杀</title>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
</head>
<body>
<form action="sk/doSecondKill" method="post">
    <input type="hidden" name="id" value="10001">
    <a href="#">一元秒杀李光光</a>
</form>
<script type="text/javascript">
    $("a").click(function () {
        $.ajax({
            type:"post",
            url:$("form").prop("action"),
            data:$("form").serialize(),
            success:function (res) {
                if(res=="ok"){
                    alert("秒杀成功");
                }else {
                    alert(res);
                    $("a").prop("disabled",true);
                }
            }
        });
        return false;
    });
</script>
</body>
</html>
