<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common/tag.jsp"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>秒杀详情页</title>
    <%--引入一些外部的js,css文件--%>
    <%@include file="common/head.jsp"%>
    <!-- <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet"> -->
</head>
<body>
    <div class="container">
        <div class="panel panel-default text-center">
            <div class="panel-heading">
                <h1>${seckill.name}</h1>
            </div>
            <div class="panel-body">
                <h3 class="text-danger">
                    <!-- 显示时间的图标 -->
                    <span class="glyphicon glyphicon-time"></span>
                    <!-- 展示的是倒计时-->
                    <span class="glyphicon" id="seckill-box"></span>
                </h3>
            </div>
        </div>
    </div>

    <!-- 登录弹出层,输入的是电话号码 -->
    <div id="killPhoneModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title text-center">
                        <span class="glyphicon glyphicon-phone"></span>秒杀电话:
                    </h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            <input type="text" name="killPhone" id="killPhoneKey"
                                placeholder="填写手机号码^O^" class="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <!-- 验证信息 -->
                    <span id="killPhoneMessage" class="glyphicon"></span>
                    <button type="button" id="killPhoneBtn" class="btn btn-success">
                        <span class="glyphicon glyphicon-phone"></span>Submit
                    </button>
                </div>
            </div>  <!-- /.modal-content-->
        </div>  <!-- /.modal-dialog-->
    </div> <!-- /.modal -->
</body>
<!-- 引入一些js插件 -->
<script src="<%=path%>/script/jquery-1.11.3.js"></script>
<script src="<%=path%>/script/bootstrap.min.js"></script>
<!-- 使用CDN获取公共js http://www.bootcdn.cn/-->
<!-- JQuery Cookie插件
<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script> -->
<!-- JQuery countdown倒计时插件
<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>-->
<!--使用本地的JQuery Cookie插件-->
<script src="<%=path%>/script/js.cookie.js"></script>
<!-- 使用本地的Jquery.countdown插件-->
<script src="<%=path%>/script/jquery.countdown.min.js"></script>
<!-- 自定义的js文件-->
<script src="<%=path%>/script/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function() {
        // 使用EL表达式进行传递参数
        seckillJS.detail.init({
            seckillId: ${seckill.seckillId},
            startTime: ${seckill.startTime.time}, // 毫秒
            endTime: ${seckill.endTime.time}
        });
    });
</script>
</html>
