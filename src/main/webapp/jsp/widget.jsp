<%--
  Created by IntelliJ IDEA.
  User: PerevalovaMA
  Date: 01.11.2016
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="./ColorPicker/css/bootstrap-colorpicker.min.css" rel="stylesheet">
    <link href="//fonts.googleapis.com/css?family=Open+Sans:300,400,700&amp;subset=latin,cyrillic-ext" rel="stylesheet">

    <script src="./ColorPicker/js/bootstrap-colorpicker.js"></script>

</head>
<body>
<div id="widget" class="tab-pane fade">
    <br>
    <div id="widgetAlert"></div>
    <div class="row">
        <form class="form-inline col-xs-12">
            <div class="form-group">
                <label for="color">Основной цвет</label>
                <div id="color" class="input-group colorpicker-component">
                    <input type="text" value="${widgetColors.body}" class="form-control"/>
                    <span class="input-group-addon"><i></i></span>
                </div>
                <div class="form-group" style="margin-left:25px;">
                    <label for="buttonColor">Цвет кнопки</label>
                    <div id="buttonColor" class="input-group colorpicker-component">
                        <input type="text" value="${widgetColors.buttonTop}" class="form-control"/>
                        <span class="input-group-addon"><i></i></span>
                    </div>
                </div>
                <input id="buttonBottomColor" type="hidden" value="${widgetColors.buttonBottom}">
            </div>
        </form>
    </div>
    <div class="row">
        <br>
        <div class="form-group col-xs-12">
            <div id="h-bot-popup"
                 style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); background: rgb(255, 255, 255); border-radius: 16px; box-shadow: rgba(0, 0, 0, 0.298039) 0px 0px 20px; box-sizing: border-box; display: block; font-family: &quot;Open Sans&quot;, sans-serif; letter-spacing: normal; max-width: 768px; min-width: 320px;>
                <div style=" -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing:
                 border-box; display: block; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal;
            ">
            <div id="h-bot-top"
                 style="-webkit-border-top-left-radius: 16px; -webkit-border-top-right-radius: 16px; -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); background: ${widgetColors.body}; border-top-left-radius: 16px; border-top-right-radius: 16px; box-sizing: border-box; color: #fff; display: block; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal; line-height: 1.2; min-width: 320px; padding: 20px 38px; position: relative; text-align: center; width: 100%;">
                <h4 id="h-bot-title"
                    style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); background-color: inherit; border: none; box-sizing: border-box; color: inherit; display: block; font-family: &quot;Open Sans&quot;, sans-serif; font-size: 28px; font-weight: 400; letter-spacing: normal; line-height: 1.2; margin: 0px; padding: 0px; text-transform: none;">
                    Персональное предложение!</h4>
                <p id="h-bot-sub-title"
                   style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); background-color: inherit; border: none; box-sizing: border-box; color: inherit; display: block; font-family: &#39;Open Sans&#39;,sans-serif; font-size: 18px; font-smoothing: antialiased; font-weight: 400; letter-spacing: normal; line-height: 1.2; margin: 0; padding: 0; text-align: center; text-transform: none;">
                    Просто выберите товары:</p>
                <a id="h-bot-close-top" href="#" title="Закрыть"
                   style="-ms-transform: rotate(45deg); -webkit-border-radius: 50%; -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); -webkit-transform: rotate(45deg); background: #fff; border-radius: 50%; box-sizing: border-box; display: block; font-family: &#39;Open Sans&#39;,sans-serif; height: 28px; letter-spacing: normal; outline: none; position: absolute; right: 33px; top: 23px; transform: rotate(45deg); width: 28px; z-index: 1000001;">
                                <span id="h-bot-cross-first"
                                      style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); background: ${widgetColors.body}; box-sizing: border-box; display: inline; font-family: &#39;Open Sans&#39;,sans-serif; height: 2px; left: 5px; letter-spacing: normal; margin-top: -1px; position: absolute; top: 50%; width: 18px;"></span>
                                <span id="h-bot-cross-second"
                                      style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); background: ${widgetColors.body}; box-sizing: border-box; display: inline; font-family: &#39;Open Sans&#39;,sans-serif; height: 18px; left: 50%; letter-spacing: normal; margin-left: -1px; position: absolute; top: 5px; width: 2px;"></span>
                </a>
            </div>
            <div id="h-bot-carousel"
                 style="-webkit-border-radius: 0; -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); border: 1px solid #b4bec5; border-radius: 0; box-sizing: border-box; display: block; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal; margin: 0 auto; overflow: hidden; padding: 0; position: relative; width: 768px;">
                <div id="h-bot-carousel-items"
                     style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box; display: block; font-family: &quot;Open Sans&quot;, sans-serif; font-size: 0px; left: 0px; letter-spacing: normal; position: relative; width: 100%;">
                    <div class="h-bot-item h-bot-item-selected" data-item-id="2822"
                         style="position: relative; max-width: 100%; padding: 10px; display: inline-block; vertical-align: top; cursor: pointer; border-right: 1px solid rgb(180, 190, 197); box-sizing: border-box; font-size: 0px; box-shadow: ${widgetColors.body} 0px 0px 0px 3px inset; width: 100%;">
                        <div class="h-bot-img-wrap"
                             style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box; font-family: &quot;Open Sans&quot;, sans-serif; letter-spacing: normal; min-height: auto; display: inline-block; vertical-align: middle; width: 50%; height: auto; max-height: 250px; margin: 0px;">
                            <img alt="" src="http://files.hucksterbot.com/widget_image.jpg"
                                 style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; display: block; font-family: &#39;Open Sans&#39;,sans-serif; height: auto; letter-spacing: normal; margin: 0 auto; max-height: 100%; max-width: 100%;"
                                 data-src="//files.hucksterbot.com/widget_image.jpg"></div>
                        <div class="h-bot-description"
                             style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box; font-family: &quot;Open Sans&quot;, sans-serif; letter-spacing: normal; display: inline-block; vertical-align: middle; width: 50%;">
                            <p class="h-bot-item-title"
                               style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); border-bottom: 1px solid rgb(232, 235, 237); box-sizing: border-box; color: rgb(0, 0, 0); display: block; font-family: &quot;Open Sans&quot;, sans-serif; letter-spacing: normal; line-height: 1.2; overflow: hidden; text-align: left; text-transform: uppercase; font-size: 24px; font-weight: 300; margin: 10px 0px 17px; padding-bottom: 15px; height: auto; min-height: 102px;">
                                Кроватка Giovanni Belcanto Lux с колесами и бортиками</p>
                            <div class="h-bot-item-bottom"
                                 style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; display: block; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal; position: relative;">
                                <p class="h-bot-item-discount"
                                   style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box; font-family: &quot;Open Sans&quot;, sans-serif; letter-spacing: normal; margin: 0px 0px 3px; background: ${widgetColors.body}; position: relative; display: inline-block; padding: 0px 15px; font-size: 48px; font-weight: 700; line-height: 1; text-align: left; color: rgb(255, 255, 255);">
                                                <span class="h-bot-item-amount"
                                                      style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box; font-family: &quot;Open Sans&quot;, sans-serif; letter-spacing: normal; display: inline-block; font-size: 24px; vertical-align: top; font-weight: 300; text-transform: uppercase; line-height: 2.1;">Скидка:</span>
                                    3%<span class="h-bot-item-arrow"
                                            style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box; font-family: &quot;Open Sans&quot;, sans-serif; font-size: 17px; letter-spacing: normal; line-height: 1.2; display: block; position: absolute; right: -25px; top: 0px; border-style: solid; border-width: 25px 0px 25px 25px; border-color: transparent transparent transparent ${widgetColors.body}; width: 0px; height: 0px;"></span>
                                </p>
                                <div class="h-bot-item-prices"
                                     style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; display: block; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal;">
                                    <p class="h-bot-item-old"
                                       style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box; color: rgb(89, 99, 106); display: block; font-family: &quot;Open Sans&quot;, sans-serif; letter-spacing: normal; margin: 0px; padding: 0px; text-decoration: line-through; text-align: left; font-size: 24px; font-weight: 300;">
                                                    <span class="h-bot-hidden"
                                                          style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; display: inline; font-family: &#39;Open Sans&#39;,sans-serif; font-size: 14px; letter-spacing: normal;">Цена:</span>
                                        17 290р.
                                    </p>
                                    <p class="h-bot-item-new"
                                       style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); box-sizing: border-box; color: ${widgetColors.body}; display: block; font-family: &quot;Open Sans&quot;, sans-serif; margin: 0px 0px 15px; text-align: left; font-size: 32px; font-weight: 400; letter-spacing: -1px; line-height: 1;">
                                                    <span class="h-bot-hidden"
                                                          style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; display: inline; font-family: &#39;Open Sans&#39;,sans-serif; font-size: 16px; letter-spacing: normal; line-height: 1.4;">Ваша цена:</span>
                                        16 770р.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <a id="h-bot-nav-prev" href="#" class="h-bot-nav"
                   style="-ms-transform: rotate(225deg); -webkit-border-radius: 50%; -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); -webkit-transform: rotate(225deg); background: #b4bec5; border-radius: 50%; box-sizing: border-box; display: none; font-family: &#39;Open Sans&#39;,sans-serif; height: 48px; left: 19px; letter-spacing: normal; margin-top: -24px; opacity: .7; position: absolute; top: 50%; transform: rotate(225deg); width: 48px; z-index: 1000001;">
                    <span style="-webkit-border-radius: 3px; -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); border-color: #fff; border-radius: 3px; border-style: solid; border-width: 3px 3px 0 0; box-sizing: border-box; display: block; font-family: &#39;Open Sans&#39;,sans-serif; height: 14px; left: 50%; letter-spacing: normal; margin-left: -9px; margin-top: -5px; position: absolute; top: 50%; width: 14px;"></span>
                </a>
                <a id="h-bot-nav-next" href="#" class="h-bot-nav"
                   style="-ms-transform: rotate(45deg); -webkit-border-radius: 50%; -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); -webkit-transform: rotate(45deg); background: #b4bec5; border-radius: 50%; box-sizing: border-box; display: none; font-family: &#39;Open Sans&#39;,sans-serif; height: 48px; letter-spacing: normal; margin-top: -24px; opacity: .7; position: absolute; right: 19px; top: 50%; transform: rotate(45deg); width: 48px; z-index: 1000001;">
                    <span style="-webkit-border-radius: 3px; -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); border-color: #fff; border-radius: 3px; border-style: solid; border-width: 3px 3px 0 0; box-sizing: border-box; display: inline; font-family: &#39;Open Sans&#39;,sans-serif; height: 14px; left: 50%; letter-spacing: normal; margin-left: -8px; margin-top: -6px; position: absolute; top: 50%; width: 14px;"></span>
                </a>
            </div>
            <form id="h-bot-form" method="POST"
                  style="-webkit-border-bottom-left-radius: 16px; -webkit-border-bottom-right-radius: 16px; -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); background: #e8ecef; border-bottom-left-radius: 16px; border-bottom-right-radius: 16px; box-sizing: border-box; display: block; font-family: &#39;Open Sans&#39;,sans-serif; height: auto; letter-spacing: normal; margin: 0!important; min-width: 320px; padding: 11px 38px 20px!important; width: auto!important;">
                <label id="h-bot-form-label" for="h-bot-form-phone"
                       style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; color: #59636a; display: block; font-family: &#39;Open Sans&#39;,sans-serif; font-size: 28px; font-smoothing: antialiased; font-weight: 400; height: auto; letter-spacing: -1px; line-height: 1.2; margin: 0 0 16px; padding: 0; text-align: center; width: auto;">Введите
                    ваш номер телефона</label>
                <div style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; display: block; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal;">
                    <input id="h-bot-form-phone" type="tel" name="phone" placeholder="+70000000000"
                           style="-webkit-border-radius: 4px; -webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); border: 1px solid #b4bec5; border-radius: 4px; box-sizing: border-box; float: left; font-family: &#39;Open Sans&#39;,sans-serif; font-size: 24px; font-weight: 400; height: 52px; letter-spacing: normal; line-height: 1; margin: 0; outline: none; padding: 0 40px; text-align: center; width: 61%;">
                    <input id="h-bot-form-submit" type="submit" value="Хочу скидку"
                           style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); border-radius: 4px; box-sizing: border-box; color: rgb(255, 255, 255); cursor: pointer; float: right; font-family: &quot;Open Sans&quot;, sans-serif; font-size: 24px; font-weight: 400; height: 52px; letter-spacing: normal; line-height: 1; padding: 0px; text-align: center; text-transform: uppercase; width: 36%; background: linear-gradient(to top, ${widgetColors.buttonTop}, ${widgetColors.buttonBottom}); border: 1px solid ${widgetColors.buttonBottom};">
                    <div id="h-bot-clear"
                         style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; clear: both; display: block; font-family: &#39;Open Sans&#39;,sans-serif; height: 0; letter-spacing: normal; width: 0;"></div>
                </div>
                <p id="h-bot-message"
                   style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; color: #7a7878; display: block; font-family: &#39;Open Sans&#39;,sans-serif; font-size: 13px; font-smoothing: antialiased; letter-spacing: normal; line-height: 2.4; margin: 20px 0 0; padding: 0; text-align: center; text-transform: none;">
                    Мы&nbsp;свяжемся с&nbsp;Вами для оформления заказа</p>
                <a id="h-bot-by" href="http://hucksterbot.ru/?utm_medium=partner&amp;utm_source=2"
                   title="hucksterbot.ru"
                   style="-webkit-box-align: center; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); align-items: center; box-sizing: border-box; color: rgb(122, 120, 120); display: flex; float: right; font-family: &quot;Open Sans&quot;, sans-serif; font-size: 11px; letter-spacing: normal; margin: -31px 0px 0px; padding: 0px; text-decoration: none; text-transform: none; line-height: 1.4;">
                    <svg viewBox="0 0 496 496" width="30" height="30"
                         style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; font-family: &#39;Open Sans&#39;,sans-serif; height: 30px; letter-spacing: normal; margin-right: 10px; width: 30px;">
                        <g transform="matrix(0.14490311926116237, 0, 0, 0.14490311926116237, 13.128222605061318, 51.70850540404666)"
                           style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal;">
                            <path fill="#666666"
                                  d="M2819.897 1355.227c0 661.845-536.451 1198.367-1198.451 1198.367v-684.692c-283.686 0-513.453-229.953-513.453-513.675H423.015c0-661.953 536.593-1198.404 1198.431-1198.404v-513.672C676.046-356.849-90.6 409.612-90.6 1355.214c0 945.464 766.646 1712.079 1712.045 1712.079 945.662 0 1712.063-766.603 1712.063-1712.079h-513.611V1355.227z"
                                  style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal;"></path>
                            <path fill="#888888"
                                  d="M1621.446 156.823c-661.821 0-1198.431 536.451-1198.431 1198.404h684.978 153.979c0-198.553 160.863-359.686 359.464-359.686 198.682 0 359.687 161.133 359.687 359.686 0 198.355-160.991 359.343-359.687 359.343v154.333 684.692c662 0 1198.445-536.505 1198.445-1198.367C2819.897 693.274 2283.436 156.823 1621.446 156.823z"
                                  style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal;"></path>
                        </g>
                    </svg>
                    Работает на<br
                        style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; font-family: &#39;Open Sans&#39;,sans-serif; letter-spacing: normal;">HucksterBot
                </a>
                <p id="h-bot-timer"
                   style="-webkit-box-sizing: border-box; -webkit-tap-highlight-color: rgba(0,0,0,0); box-sizing: border-box; color: ${widgetColors.body}; display: none; font-family: &#39;Open Sans&#39;,sans-serif; font-size: 14px; font-smoothing: antialiased; font-weight: 700; letter-spacing: normal; margin: 5px 0 0; padding: 0; text-align: center; text-transform: none;">
                    Персональное предложение действительно {{ time }}</p>
            </form>
        </div>
        <br>
        <div class="col-xs-10 pull-right">
            <button type="button" class="btn btn-default cancel">Отмена</button>
            <button id="saveColors" type="button" class="btn btn-primary">Сохранить</button>
        </div>
    </div>
</div>
</div>
</div>
</body>
<script>
    function ColorLuminance(hex, lum) {
        // validate hex string
        hex = String(hex).replace(/[^0-9a-f]/gi, '');
        if (hex.length < 6) {
            hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
        }
        lum = lum || 0;

        // convert to decimal and change luminosity
        var rgb = "#", c, i;
        for (i = 0; i < 3; i++) {
            c = parseInt(hex.substr(i * 2, 2), 16);
            c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
            rgb += ("00" + c).substr(c.length);
        }

        return rgb;
    }

    var body = $('#color').colorpicker('getValue', '#e2595c');
    var buttonTop = $('#buttonColor').colorpicker('getValue', '#57ba00');
    var buttonBottom = $('#buttonBottomColor').val();

    $('#color').colorpicker().on('changeColor', function (e) {
        body = e.color.toHex();
        document.getElementById('h-bot-top').style.background = body;
        document.getElementById('h-bot-cross-first').style.background = body;
        document.getElementById('h-bot-cross-second').style.background = body;
        document.getElementsByClassName('h-bot-item h-bot-item-selected')[0].style.boxShadow = body + ' 0px 0px 0px 3px inset';
        document.getElementsByClassName('h-bot-item-discount')[0].style.background = body;
        document.getElementsByClassName('h-bot-item-new')[0].style.color = body;
        document.getElementsByClassName('h-bot-item-arrow')[0].style.borderColor = 'transparent transparent transparent ' + body;
        document.getElementById('h-bot-timer').style.color = body;
    });

    $('#buttonColor').colorpicker().on('changeColor', function (e) {
        buttonTop = e.color.toHex();
        buttonBottom = ColorLuminance(buttonTop, -0.2);
        document.getElementById('h-bot-form-submit').style.background = 'linear-gradient(to top, ' + buttonTop + ', ' + buttonBottom + ')';
        document.getElementById('h-bot-form-submit').style.border = '1px solid ' + buttonBottom;
    });

    $('#saveColors').on('click', function (e) {
        $.ajax({
            url: "settings",
            type: "POST",
            data: {
                type: "save_colors",
                body: body,
                buttonTop: buttonTop,
                buttonBottom: buttonBottom
            }
        }).done(function (data) {
            if (!data.success) {
                alert('widgetAlert', data.error, 'danger', 5000);
            } else {
                location.reload();
            }
        });
    });
</script>
</html>
