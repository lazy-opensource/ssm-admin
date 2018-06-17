/**
 * Created by laizy on 2017/3/11.
 */
define(['jquery', 'bootstrap',  'treeview'], function ($, bootstrap,  treeview) {

    var fn = {};
    //初始化侧边栏
    fn.initSide = function () {
        $.post(
            basePath20170311 + 'menu/findMenuList',
            function(_data){
                var data = _data.list
                var tree = JSON.stringify(data);
                $('#sidebar_div').treeview({
                    data:tree,
                    highlightSearchResults:true,
                    searchResultBackColor:'#D3AE30',
                    collapseIcon: "glyphicon glyphicon-chevron-down",
                    expandIcon: 'glyphicon glyphicon-chevron-right',
                    borderColor: 'white',
                    color:'white',
                    levels: 5,
                    enableLinks:false,
                    onhoverColor:'#C0C0C0',
                    selectedBackColor:'#C0C0C0',
                    backColor:'#808080',
                    onNodeSelected: function(event, data) {
                        var selectNode = $('#sidebar_div').treeview('getSelected')[0];
                        var url = selectNode.href;
                        if (url){
                            document.getElementById("content").src=selectNode.href;
                        }
                        return false;
                    }
                });
            },'json'
        )
    }

    //初始化导航
    fn.initNav = function () {
        $('#navigation').load(basePath20170311 + '/menu/loadNavigation');
        $.post(
            basePath20170311 + 'user/findCurrentName',
            function (_data) {
                var username = _data.object;
                $('#user').html('<span class="glyphicon glyphicon-user"></span> &nbsp;' + username);
            }, 'json')
    }

    //处理ifrawork高度问题
    fn.initIfraworkHeight = function(){
        var content = document.getElementById("content");
        var h = document.documentElement.clientHeight - 100;
        content.height = h;
        window.onresize = function () {
            var content = document.getElementById("content");
            var h = document.documentElement.clientHeight - 100;
            content.height = h;
        }
    }

    //执行
    fn.excute = function () {

        fn.initNav();
        fn.initSide();
        fn.initIfraworkHeight();
    }

    return fn;
})
