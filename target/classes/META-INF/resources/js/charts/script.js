$(function() {
var tt = document.createElement('div'),
  leftOffset = -(~~$('html').css('padding-left').replace('px', '') + ~~$('body').css('margin-left').replace('px', '')),
  topOffset = -32;
tt.className = 'ex-tooltip';
document.body.appendChild(tt);

var opts = {
  "dataFormatX": function (x) { return d3.time.format('%Y-%m-%d %H:%M').parse(x); },
  "tickFormatX": function (x) { return d3.time.format('%d.%m.%Y')(x); },
  "mouseover": function (d, i) {
    var pos = $(this).offset();
    $(tt).text(d3.time.format('%d.%m.%Y')(d.x) + ': ' + d.y)
      .css({top: topOffset + pos.top, left: pos.left + leftOffset})
      .show();
  },
  "mouseout": function (x) {
    $(tt).hide();
  }
};
var myChart1 = new xChart('line-dotted', data_inc, '#chart_inc', opts);
var myChart2 = new xChart('line-dotted', data_ord, '#chart_ord', opts);
var myChart3 = new xChart('line-dotted', data_cnv, '#chart_cnv', opts);
});
