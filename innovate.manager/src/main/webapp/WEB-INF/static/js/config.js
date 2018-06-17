/**
 * Created by lzy on 2017/3/11.
 */
//挂载
var shim ={
    'validate':{
        deps:['jquery'],
        exports:'jQuery.fn.validate'
    },
    'bootbox':{
        deps:['jquery'],
        exports:'jQuery.fn.bootbox'
    },
    'datetimepicker-zh':{
        deps:['datetimepicker'],
        exports:'datetimepicker-zh'
    },
    'datetimepicker':{
        deps:['jquery'],
        exports:'datetimepicker'
    },
    'ColReorderWithResize':{
        deps:['jquery','bootstrap'],
        exports:'ColReorderWithResize'
    },
    'globel':{
        exports:'globel'
    }

}

//配置
var basePath20170311 = document.getElementById('basePath20170311').value;
var common = null;
var current = null;
require.config({
    baseUrl: basePath20170311 + "static/js/",
    urlArgs: 'arg=' + 6 * Math.random().valueOf(),
    paths:{
        "jquery":"plus/jquery.min",
        'common':'local/common',
        'validate':'plus/jquery.validate.min',
        'bootstrap':'plus/bootstrap.min',
        'bootbox':'plus/bootbox.min',
        'datetimepicker-zh':'plus/bootstrap-datetimepicker.zh-CN',
        'datetimepicker':'plus/bootstrap-datetimepicker.min',
        'ColReorderWithResize':'plus/ColReorderWithResize',
        'dataTable':'plus/jquery.dataTables.min',
        'sysGlobel':'local/system/globel',
        'treeview':'plus/bootstrap-treeview.min'
    },
    shim:shim
})


//全局模块调用
require(['jquery'],function($){

    var currentPage = $('#currentPage').attr('data-target');

    if (currentPage){
        require([currentPage, 'common'],function(current, common){
            this.common = common;
            this.current = current;
            common.init();
            current.excute();
        })

    }else{
        common.init();
    }

})