/**
 * Created by lzy on 2017/3/7.
 */
define(['jquery','validate'],function($,e){

    return {
        excute:function(){
            this.activeTab();
        },

        activeTab:function(){
            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                // 获取已激活的标签页的名称
                var activeTab = $(e.target).text();
                // 获取前一个激活的标签页的名称
                var previousTab = $(e.relatedTarget).text();
                $(".active-tab span").html(activeTab);
                $(".previous-tab span").html(previousTab);
            });
        }
    }
})