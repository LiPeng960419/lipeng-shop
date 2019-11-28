<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>家居商城-详情</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="../res/layui/css/layui.css">
  <link rel="stylesheet" href="../res/static/css/index.css">
</head>
<body>

<style type="text/css">
  ._end {
    color: #fff;
    font-size: 30px;
  }
</style>
<script type="text/javascript">
    window.onload = function countTime() {
        //获取当前时间
        var date = new Date();
        var now = date.getTime();
        //设置截止时间
        var str = "2019/11/28 23:59:59";
        var endDate = new Date(str);
        var end = endDate.getTime();
        //时间差
        var leftTime = end - now;
        //定义变量 d,h,m,s保存倒计时的时间
        var d, h, m, s;
        d = Math.floor(leftTime / 1000 / 60 / 60 / 24);
        h = Math.floor(leftTime / 1000 / 60 / 60 % 24);
        m = Math.floor(leftTime / 1000 / 60 % 60);
        s = Math.floor(leftTime / 1000 % 60);
        if (leftTime > 1) {
            //活动剩余时间
            document.getElementById("_end").innerHTML = "活动剩余<span class='_end'> " + d
                    + "天</span> <span class='_end'> " + h +
                    "时</span> <span class='_end'> " + m + "分</span> <span class='_end'> " + s
                    + "秒</span> ";
        } else {
            //倒计时结束
            document.getElementById("_end").innerHTML = "<span class='_end'>活动已结束</span>";
        }
        setTimeout(countTime, 1000);
    }
</script>

<div class="house-header">
  <div class="layui-container">
    <div class="house-nav">
      <span class="layui-breadcrumb" lay-separator="|">
        <a href="login.html">登录</a>
        <a href="">我的订单</a>
        <a href="http://wpa.qq.com/msgrd?v=3&uin=483966038&site=qq&menu=yes">在线客服</a>
      </span>
      <span class="layui-breadcrumb house-breadcrumb-icon" lay-separator=" ">
        <a id="search"><i class="layui-icon layui-icon-house-find"></i></a>
        <a href="login.html"><i class="layui-icon layui-icon-username"></i></a>
        <a href="usershop.html"><i class="layui-icon layui-icon-house-shop"></i></a>
      </span>
    </div>
    <div class="house-banner layui-form">
      <a class="banner" href="index.html">
        <img src="http://static.itmayiedu.com/123123123.png" alt="家居商城">
      </a>
      <div class="layui-input-inline">
        <input type="text" placeholder="搜索好物" class="layui-input"><i
            class="layui-icon layui-icon-house-find"></i>
      </div>
      <a class="shop" href="usershop.html"><i class="layui-icon layui-icon-house-shop"></i><span
            class="layui-badge">1</span></a>
    </div>
    <ul class="layui-nav close">
      <li class="layui-nav-item layui-this"><a href="index.html">首页</a></li>
      <li class="layui-nav-item"><a href="list.html">居家用品</a></li>
      <li class="layui-nav-item"><a href="list.html">小家电</a></li>
      <li class="layui-nav-item"><a href="list.html">洗护</a></li>
      <li class="layui-nav-item"><a href="list.html">厨具</a></li>
      <li class="layui-nav-item"><a href="list.html">日用品</a></li>
    </ul>
    <button id="switch">
      <span></span><span class="second"></span><span class="third"></span>
    </button>
  </div>
</div>

<div class="layui-container house-detail">
  <p class="title"><a href="index.html">首页</a> &gt; <a href="list.html">家居用品</a> &gt;
    <span>产品详情</span></p>
  <div style="background-color: #fb4856;"><span class="layui-badge">抢购</span><span>距离活动结束还有:</span>
    <span id="_end"></span></div>

  <div class="layui-row price">
    <div class="layui-col-sm6">
      <div class="intro-img"><img src="../res/static/img/goods_img1.jpg"></div>
      <ul class="thumb">
        <li><img src="../res/static/img/goods_img2.jpg"></li>
        <li><img src="../res/static/img/goods_img3.jpg"></li>
        <li><img src="../res/static/img/goods_img4.jpg"></li>
        <li><img src="../res/static/img/goods_img5.jpg"></li>
      </ul>
    </div>
    <div class="layui-col-sm6 shopChoose">


      <div class="title"><p><span class="layui-badge">新品</span>轻奢吊灯现代极简创意灯具</p>好评率 <span>90%</span>
      </div>
      <p><span>￥<big><b>199</b></big></span>原价￥<big>
          <del>399</del>
        </big></p>
      <dl>
        <dt>颜色</dt>
        <dd>白色</dd>
        <dd class="active">灰色</dd>
        <dd>黑色</dd>
      </dl>
      <dl>
        <dt>尺寸</dt>
        <dd>1.2米</dd>
        <dd class="active">0.5米</dd>
      </dl>
      <div class="number layui-form">
        <label>数量</label>
        <div class="layui-input-inline btn-input">
          <button class="layui-btn layui-btn-primary sup">-</button>
          <input type="text" class="layui-input">
          <button class="layui-btn layui-btn-primary sub">+</button>
        </div>
        <p class="inputTips">已超出库存数量！</p>
      </div>
      <div class="shopBtn">
        <button class="layui-btn layui-btn-primary sale">立即购买</button>
        <button class="layui-btn shop"><i class="layui-icon layui-icon-house-shop"></i>加入购物车
        </button>
        <button class="layui-btn layui-btn-primary collect"><i class="layui-icon layui-icon-rate"
                                                               id="collect"></i>收藏
        </button>
      </div>
    </div>
  </div>
  <div class="layui-row layui-col-space30">
    <div class="layui-col-sm8 detailTab">
      <div class="layui-tab layui-tab-brief">
        <ul class="layui-tab-title">
          <li class="layui-this">详情</li>
          <li>评论 <span>(120)</span></li>
        </ul>
        <div class="layui-tab-content">
          <div class="layui-tab-item layui-show">
            <img src="../res/static/img/goods_img3.jpg">
            <img src="../res/static/img/goods_img4.jpg">
            <img src="../res/static/img/goods_img5.jpg">
          </div>
          <div class="layui-tab-item">
            <div class="comment">
              <ul>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
                <li>
                  <div class="img"><img src="../res/static/img/person.png"></div>
                  <p class="txt">质量还可以！纯棉的，盖着挺舒服的，对皮肤也好。</p>
                  <p class="time">2018年05月02日 11:20</p>
                </li>
              </ul>
              <div id="detailList"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="layui-col-sm4 detailCom">
      <p class="title">热销推荐</p>
      <ul class="detailCom-content hot-sell">
        <li><a class="text">
            <div><img src="../res/static/img/goods_img6.jpg"></div>
            <p>森系小清新四件套</p>
            <p class="price">￥200</p>
          </a></li>
        <li><a class="text">
            <div><img src="../res/static/img/goods_img7.jpg"></div>
            <p>森系小清新四件套</p>
            <p class="price">￥200</p>
          </a></li>
        <li><a class="text">
            <div><img src="../res/static/img/goods_img8.jpg"></div>
            <p>森系小清新四件套</p>
            <p class="price">￥200</p>
          </a></li>
        <li><a class="text">
            <div><img src="../res/static/img/goods_img9.jpg"></div>
            <p>森系小清新四件套</p>
            <p class="price">￥200</p>
          </a></li>
        <li><a class="text">
            <div><img src="../res/static/img/goods_img6.jpg"></div>
            <p>森系小清新四件套</p>
            <p class="price">￥200</p>
          </a></li>
      </ul>
    </div>
  </div>
</div>

<div class="house-footer">
  <div class="layui-container">
    <div class="intro">
      <span class="first"><i class="layui-icon layui-icon-house-shield"></i>7天无理由退换货</span>
      <span class="second"><i class="layui-icon layui-icon-house-car"></i>满99元全场包邮</span>
      <span class="third"><i class="layui-icon layui-icon-house-diamond"></i>100%品质保证</span>
      <span class="last"><i class="layui-icon layui-icon-house-tel"></i>客服400-2888-966</span>
    </div>
    <div class="about">
      <span class="layui-breadcrumb" lay-separator="|">
        <a href="about.html">关于我们</a>
        <a href="about.html">帮助中心</a>
        <a href="about.html">售后服务</a>
        <a href="about.html">配送服务</a>
        <a href="about.html">关于货源</a>
      </span>
      <p>家居商城版权所有 &copy; 2012-2020</p>
    </div>
  </div>
</div>

<script src="../res/layui/layui.js"></script>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<script>
    layui.config({
        base: '../res/static/js/'
    }).use('house');
</script>

</body>
</html>