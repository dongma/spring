// 存放主要交互逻辑的js代码
// javascript 模块化(package.类名.方法名)

var seckillJS = {
    // Spring restful风格的URL地址.
    URL : {
        now : function () {
            return '/spring/seckill/time/now';
        },
        exposer : function(seckillId) {
            return '/spring/seckill/' + seckillId + '/exposer';
        },
        execution : function(seckillId, md5) {
            return '/spring/seckill/' + seckillId + '/' + md5 + '/' + 'execution';
        }
    },

    // 验证手机号
    validatePhone : function(phone) {
        if(phone && phone.length == 11 && !isNaN(phone)) {
            return true; // 判断对象会直接查看对象是否为空undefined,isNaN会判断对象是否为数字。
        } else {
            return false;
        }
    },

    // 详情页秒杀逻辑
    detail : {
        // 详情页初始化
        init : function(param) {
            // 手机验证和登录,计时操作,在Cookie中查找用户的手机号
            // var userPhone = $.cookie('userPhone');       // jQuery的写法.
            var userPhone = Cookies.get('userPhone');       // Jquery-cookie官方文档的格式.
            // 1.对用户输入的电话号码进行校验
            if(!seckillJS.validatePhone(userPhone)) {
                // 绑定手机控制输出.
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true, // 弹出对话层
                    backdrop: 'static', // 禁止位置关闭
                    keyboard: false  // 关闭键盘事件
                });
                // 对秒杀btn绑定OnClick事件
                $('#killPhoneBtn').click(function() {
                    // 获取得到用户输入的电话号码.
                    var inputPhone = $('#killPhoneKey').val();
                    console.log('inputPhone: ' + inputPhone);
                    // 对输入的phone进行校验
                    if(seckillJS.validatePhone(inputPhone)) {
                        // 电话写入到cookie中 7天
                        // $.cookie('userPhone', inputPhone, {expires: 7, path: '/seckill'});
                        // JQuery cookie官方文档的写法,在Idea中webapp发布的目录为 /spring.
                        Cookies.set('userPhone', inputPhone, {expires: 7, path: '/spring'});
                        // 验证通过刷新页面.
                        window.location.reload();
                    } else {
                        // 错误文案信息提取到前端字典里
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号码错误!</label>').show(300);
                    }
                });
            }

            // 2.用户已经登录,计时交互
            var startTime = param['startTime'];
            var endTime = param['endTime'];
            var seckillId = param['seckillId'];
            // alert(startTime + "_" + endTime + "_" + seckillId);
            // 使用Ajax的GET请求从服务器端获取当前的系统时间.
            $.get(seckillJS.URL.now(), {}, function (result) {
                if(result && result['success']) {
                    var nowTime = result['data'];
                    // 判断时间,计时交互
                    seckillJS.countDown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                    alert('result:' + result);
                }
            });
        }
    },

    // 执行真正的秒杀逻辑.
    handlerSeckill : function(seckillId, node) {
        // 获取秒杀地址,控制显示器执行秒杀
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');

        // 使用JQuery AjaxGet方法进行秒杀操作.
        $.get(seckillJS.URL.exposer(seckillId), {}, function(result) {
            // 在回调函数中执行交互流程
            if(result && result['success']) {
                var exposer = result['data'];
                if(exposer['exposed']) {    // 当开启秒杀的时候,获取md5数据进行处理,获取秒杀的地址
                    var md5 = exposer['md5'];
                    var killUrl = seckillJS.URL.execution(seckillId, md5);
                    // console.log向控制台输出将要访问的地址.
                    console.log('killUrl:' + killUrl);
                    // 绑定一次点击的时间
                    $('#killBtn').one('click', function() {
                        // 1.执行秒杀请求,禁用按钮防止重复操作
                        $(this).addClass('disabled');  // $(this) == $('#killBtn')
                        // 2.发送秒杀请求执行秒杀的操作
                        $.post(killUrl, {}, function(result) {
                            if(result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                // 显示秒杀的结果
                                node.html('<label class="label label-success">' + stateInfo + '</label>');
                            }
                        });
                    });
                    node.show();   // 显示被隐藏的节点
                } else {
                    // 当为开启秒杀的时候(浏览器倒计时)
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckillJS.countDown(seckillId, now, start, end);
                }
            } else {
                // 当result的值为Null或者result['success']为false的时候,向控制台打印出返回的结果.
                console.log('result:' + result);
            }
        });
    },

    // countDown操作,对于秒杀的时间进行处理.
    countDown : function(seckillId, nowTime, startTime, endTime) {
        // 系控制台打印出参数的信息
        console.log(seckillId + '_' + nowTime + '_' + startTime + "_" + endTime);
        var seckillBox = $('#seckill-box');
        if(nowTime > endTime) {
            // 秒杀结束
            seckillBox.html('秒杀结束!');
        } else if(nowTime < startTime) {
            // 秒杀未开始,计时事件绑定.
            var killTime = new Date(startTime + 1000); // 防止时间偏移
            seckillBox.countdown(killTime, function(event) {
                // 时间格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function(){
                // 时间完成后的回调事件,获取秒杀地址,控制执行逻辑,进行秒杀.
                console.log('____finish.countdown');
                seckillJS.handlerSeckill(seckillId, seckillBox);
            });
        } else {
            // 秒杀开始
            seckillJS.handlerSeckill(seckillId, seckillBox);
        }
    }
};


