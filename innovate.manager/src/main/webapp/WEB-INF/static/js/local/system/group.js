/**
 * Created by laizhiyuan on 2017/3/26.
 */
define(['jquery','bootstrap', 'validate','bootbox', 'datetimepicker', 'datetimepicker-zh', 'dataTable','ColReorderWithResize','common','treeview'],function($,$1,$2,$3,$4,$5,$6,$7,$8,$9) {

    var fn = {};
    var table = null;

    //删除
    fn.del = function(){
        var ids = $8.getCheckedIds();
        if (ids.length < 1){
            $3.alert('请至少选择一项进行操作!');
            return;
        }
        $3.setLocale("zh_CN");
        $3.confirm('确定删除' + ids.length + '条数据吗?',function(r){
            if (r){
                var isDelUser = "0";
                $3.confirm('是否将该用户组/部门下的所有用户一并删除?',function(r2){
                    if (r2){
                        isDelUser = "1";
                    }
                    $.post(
                        basePath20170311 + 'group/del/' + ids.join(','),
                        {isDelUser:isDelUser},
                        function(_data){
                            $8.handleWebResult(_data);
                            table.fnDraw();
                        },'json'
                    )
                })
            }
        })
    }

    //编辑
    fn.edit = function(){
        fn.submitForm('edit');
    }

    //添加
    fn.add = function(){
        fn.submitForm('add');
    }

    fn.submitForm = function(action){
        var isOk = $('#groupForm').valid();
        if (!isOk){
            return;
        }
        $('#groupModal').modal('hide');
        if (action =='add'){
            $.post(
                basePath20170311 + 'group/add',
                $8.setter({name:'',remark:''}),
                function(_data){
                    $8.handleWebResult(_data);
                    table.fnDraw();
                },'json'
            )
        }
        if (action == 'edit'){
            $.post(
                basePath20170311 + 'group/edit',
                $8.setter({name:'',remark:'',uuid:$('#uuid').val()}),
                function(_data){
                    $8.handleWebResult(_data);
                    table.fnDraw();
                },'json'
            )
        }
    }

    //去编辑
    fn.toEdit = function(){
        var ids = $8.getCheckedIds();
        if (ids.length != 1){
            $3.alert('请选择一项进行操作!');
            return;
        }
        $('#save').attr('onclick','javascript:current.edit();');
        $.post(
            basePath20170311 +  'group/toEdit/' + ids[0],
            function(_data){
                _data = _data.object;
                $8.setNode({name:_data.name,remark:_data.remark,uuid:_data.uuid});
                $('#groupModalLabel').html('编辑');
                $('#groupModal').modal('show',true);
            },'json'
        )
    }

    //去添加
    fn.toAdd = function(){
        $8.clear(['name','remark']);
        $('#save').attr('onclick','javascript:current.add();');
        $('#groupModal').modal('show',true);
    }

    //关联菜单
    fn.contactMenu = function(){
        var menuIds = fn.getUserGroupSelectNodeIds();
        var groupId = $('#uuid').val();
        $.post(
            basePath20170311 + 'group/contactMenu/' + groupId, {
                menuIds: menuIds.join(',')
            },
            function(_data) {
                $8.handleWebResult(_data,'groupMenuModal');
                table.fnDraw();
            },
            'json')
    }

    //去关联菜单
    fn.toContactMenu = function(){
        var ids = $8.getCheckedIds();
        if (ids.length != 1){
            $3.alert('请选择一项进行操作!');
            return;
        }
        $('#uuid').val(ids[0]);
        $.post(
            basePath20170311 + 'group/toContactMenu/' + ids[0],
            function(_data) {
                if (!$8.checkWebResult(_data)){
                    $8.handleWebResult(_data);
                    return;
                };
                var contactMenus = _data.map.contactMenus;
                var canContactMenus = _data.map.canContactMenus;
                fn.initMenuTree(canContactMenus);
                fn.setUserGroupMenuNodesSelected(contactMenus);
                $('#groupMenuModal').modal('show', true);
            },
            'json')
    }

    fn.initMenuTree = function(data){
        $('#menuTree').treeview({
            data: data,
            multiSelect: true,
            collapseIcon: "glyphicon glyphicon-chevron-down",
            expandIcon: 'glyphicon glyphicon-chevron-right',
            onhoverColor: '#DDDDDD',
            selectedBackColor: '#DDDDDD',
            backColor: 'white',
            color:'rgb(0,0,0)',
            borderColor: '#C0C0C0',
            levels: 5,
            onNodeSelected: function (event, data) {
                fn.checkParentNode(data.pid);
            },
            onNodeUnselected: function (event, data) {
                fn.checkChildNode(data.uuid);
            }
        });
    }

    //递归查找子节点
    fn.checkParentNode = function(pid){
        var tempPid = pid;
        var nodes = $('#menuTree').treeview('getEnabled');
        for (var i in nodes){
            if (nodes[i].uuid == pid){
                pid = nodes[i].pid;
                nodes[i].state.selected = true;
                break;
            }
        }
        if (tempPid != pid){
            fn.checkParentNode(pid);
        }
        return;
    }

    //取消父节点的同时取消子节点
    fn.checkChildNode = function(nid){
        var nodes = $('#menuTree').treeview('getEnabled');
        for (var i in nodes){
            if (nodes[i].pid == nid){
                nodes[i].state.selected = false;
                fn.checkChildNode(nodes[i].uuid);
            }
        }
        return;
    }

    //关联操作
    fn.contactOper = function(){
        var ids = [];
        $('input[name="operCheck"]:checkbox').each(function(){
            if ($(this).is(':checked')){
                ids.push($(this).val());
            }
        })
        var id = $('#uuid').val();
        $('#groupOperModal').modal('hide');
        $.post(
            basePath20170311 + 'group/contactOper/' + id,
            {operIds:ids.join(',')},
            function(_data){
                $8.handleWebResult(_data);
                table.fnDraw();
            },'json'
        )
    }

    //去关联操作
    fn.toContactOper = function(){
        var ids = $8.getCheckedIds();
        if (ids.length != 1){
            $3.alert('请选择一项进行操作!');
            return;
        }
        $('#uuid').val(ids[0]);
        fn.initOperTab(ids[0]);
    }

    //初始化组操作Tab
    fn.initOperTab = function(groupId){

        $('#groupOpers-ul').empty();
        $('#groupOpers-content').empty();
        if (groupId == '') {
            return;
        }
        $.post(
            basePath20170311 + 'group/toContactOper/' + groupId,
            function(_data) {
                if (!$8.checkWebResult(_data)){
                    $8.handleWebResult(_data);
                    return;
                }
                var canContactOpers = _data.map.canContactOpers;
                var contactOpers = _data.map.contactOpers;
                var menuOpers = '';
                var ul = $('#groupOpers-ul');
                var content = $('#groupOpers-content');
                var lis = '';
                var dropdown = '';
                var contents = '';
                var opers = '';
                var tag = 0;
                for (var i in canContactOpers) {
                    tag++;
                    if (tag > 5) {
                        /**
                         * 更多下拉
                         */
                        if (tag == 6) {
                            /**
                             * 下拉的第一个
                             * @type {string}
                             */
                            dropdown = dropdown + '<li class="dropdown"><a href="#" id="myTabDrop1" class="dropdown-toggle"' + 'data-toggle="dropdown">更多<b class="caret"></b> </a><ul class="dropdown-menu" role="menu" aria-labelledby="myTabDrop1">' + '<li><a href="#' + (tag + 1) + '" tabindex="-1" data-toggle="tab">' + i + '</a></li>';
                        } else {
                            /**
                             * 下拉的其它
                             * @type {string}
                             */
                            dropdown = dropdown + '<li><a href="#' + (tag + 1) + '" tabindex="-1" data-toggle="tab">' + i + '</a></li>';
                        }
                    } else {
                        /**
                         * 正常Tab
                         */
                        if (tag == 1) {
                            /**
                             * 默认选中第一个Tab
                             * @type {string}
                             */
                            lis = lis + '<li class="active"><a href="#' + (tag + 1) + '" data-toggle="tab">' + i + '</a></li>';
                        } else {
                            /**
                             * Tab的除更多其它
                             * @type {string}
                             */
                            lis = lis + '<li><a href="#' + (tag + 1) + '" data-toggle="tab">' + i + '</a></li>';
                        }
                    }
                    /**
                     * 开始拼接Tab下的操作
                     * @type {string}
                     */
                    opers = '<div>';
                    //当前Tab页的所有操作
                    menuOpers = canContactOpers[i];
                    for (var j in menuOpers) {
                        if (j % 5 == 0) {
                            opers = opers + '<br><br>';
                        }
                        /**
                         * 检查该操作是否已经拥有，是的话选中
                         */
                        if (fn.checkOperId(contactOpers, menuOpers[j].uuid)){
                            opers = opers + '<label class="checkbox-inline"><input type="checkbox" checked name="operCheck" value="' + menuOpers[j].uuid + '">' + menuOpers[j].name + '</label>';
                        }else{
                            opers = opers + '<label class="checkbox-inline"><input type="checkbox" name="operCheck" value="' + menuOpers[j].uuid + '">' + menuOpers[j].name + '</label>';
                        }
                    }
                    /**
                     * 当前菜单Tab的结束
                     * @type {string}
                     */
                    opers = opers + '</div>';
                    /**
                     * 把当前的菜单Tab页的操作填充进去
                     */
                    if (tag == 1) {
                        contents = contents + '<div class="tab-pane fade in active" id="' + (tag + 1) + '">' + opers + '</div>';
                    } else {
                        contents = contents + '<div class="tab-pane fade" id="' + (tag + 1) + '">' + opers + '</div>';
                    }
                }
                /**结束*/
                dropdown = dropdown + '</ul></li>';
                lis = lis + dropdown;
                ul.append(lis);
                content.append(contents);
                $('#groupOpers').append(ul).append(content);
                $('#groupOperModal').modal('show', true);
            },'json')
    }

    fn.checkOperId = function(userGroup, operId){
        for (var i in userGroup){
            if (userGroup[i].uuid == operId){
                return true;
            }
        }
        return false;
    }

    //将用户组关联的菜单选中
    fn.setUserGroupMenuNodesSelected = function(data){
        var nodes = $('#menuTree').treeview('getEnabled');
        var userGroupMenus = new Array();

        for (var i in data) {
            userGroupMenus.push(data[i].uuid);
        }
        var len = userGroupMenus.length;
        var len2 = 0;
        for (var i in nodes) {
            if (len2 == len) {
                break;
            }
            for (var j in userGroupMenus) {
                if (nodes[i].uuid == userGroupMenus[j]) {
                    len2++;
                    $('#menuTree').treeview('selectNode', [nodes[i], {
                        silent: true
                    }]);
                    break;
                }
            }
        }
        return;
    }

    fn.getUserGroupSelectNodeIds = function(){
        var nodes = $('#menuTree').treeview('getSelected');
        var ids = [];
        for (var i in nodes) {
            ids.push(nodes[i].uuid);
        }
        return ids;
    }

    fn.initBinding = function(){
        //条件查找
        $('.search').click(function(){
            fn.queryAllOrByCondition('seach');
        });


        //全选
        $("#check_all").click(function(e) {
            $('input', table.fnGetNodes()).prop('checked', this.checked);
        });
    }

    //初始化插件
    fn.initPlus = function(){
        //日历控件
        $('.form_datetime').datetimepicker({
            language:  'zh-CN',
            format : 'yyyy-mm-dd hh:ii:ss',
            autoclose: true
        });

        var validateParam = {
            rules:{
                name:{required:true}
            },
            messages:{
                name:{required:$.format('用户组名称不能为空')}
            },
            submitHandler:function(form){
                return false;
            }
        }

        $('#groupForm').validate(validateParam);
    }

    //初始化操作
    fn.findCurrentMenuOper = function(){
        var parentMenuId = $('#currentMenuId').val();
        $.post(
            basePath20170311 + 'oper/findOpersByMenuId',
            {menuId:parentMenuId},
            function(_data){
                if (_data){
                    var temp = '';
                   for (var i in _data){
                       temp  += '<a class="btn btn-default" title="'+_data[i].name+'" href="javascript:current.'+_data[i].url+'">'+ _data[i].name+'</a>';
                   }
                    $('#oper-btn').append(temp);
                }

            },'json'
        )
    }

    //请求服务器数据
    fn.retrieveData = function(sSource,aoData,fnCallback) {
        var condition = [];

        if ($('#name').val()){
            condition.push({"name":"name","value":$('#nameSeach').val()});
            condition.push({"name":"name_q","value":$('#nameSeach_q').val()});
        }

        if ($('#createTimeS').val()){
            condition.push({"name":"createTime","value":$('#createTimeS').val()});
            condition.push({"name":"createTime_q","value":$('#createTimeS_q').val()});
        }
        if ($('#createTimeE').val()){
            condition.push({"name":"createTime","value":$('#createTimeE').val()});
            condition.push({"name":"createTime_q","value":$('#createTimeE_q').val()});
        }
        if ($('#createTimeE').val()  && $('#createTimeS').val() ){
            condition.push({"name":"createTime","value":$('#createTimeS').val()+'@_date_@'+$('#createTimeE').val()});
            condition.push({"name":"createTime_q","value":$('#createTimeS_q').val()+'@_date_@'+$('#createTimeE_q').val()});
        }

        if ($('#updateTimeS').val()){
            condition.push({"name":"updateTime","value":$('#updateTimeS').val()});
            condition.push({"name":"updateTime_q","value":$('#updateTimeS_q').val()});
        }
        if ($('#updateTimeE').val()){
            condition.push({"name":"updateTime","value":$('#updateTimeE').val()});
            condition.push({"name":"updateTime_q","value":$('#updateTimeE_q').val()});
        }
        if ($('#updateTimeE').val() && $('#updateTimeS').val()){
            condition.push({"name":"updateTime","value":$('#updateTimeS').val()+'@_date_@'+$('#updateTimeE').val()});
            condition.push({"name":"updateTime_q","value":$('#updateTimeS_q').val()+'@_date_@'+$('#updateTimeE_q').val()});
        }

        $.ajax({
            "dataType" : 'json',
            "type" : "POST",
            "url" : sSource,
            "data": {"aoData" : JSON.stringify(aoData),"condition":JSON.stringify(condition)},
            "success" : function(_data){
                fnCallback(_data);
            }
        });
    }

    fn.queryAllOrByCondition = function(tag){
        if (table == null){
            if ($('.dataTable').length > 0) {
                $('.dataTable').each(function(){
                    var opt = {
                        "bProcessing" : true, //DataTables载入数据时，是否显示‘进度’提示
                        "bServerSide" : true, //是否启动服务器端数据导入
                        "bStateSave" : true, //是否打开客户端状态记录功能,此功能在ajax刷新纪录的时候不会将个性化设定回复为初始化状态
                        //"bJQueryUI" : true, //是否使用 jQury的UI theme
                        "aLengthMenu" : [5, 10, 20, 40, 60], //更改显示记录数选项
                        "iDisplayLength" : 10, //默认显示的记录数
                        "bAutoWidth" : true, //是否自适应宽度
                        //"bScrollInfinite" : false, //是否启动初始化滚动条
                        "bScrollCollapse" : true, //是否开启DataTables的高度自适应，当数据条数不够分页数据条数的时候，插件高度是否随数据条数而改变
                        "bPaginate" : true, //是否显示（应用）分页器
                        "bInfo" : true, //是否显示页脚信息，DataTables插件左下角显示记录数
                        "sPaginationType" : "full_numbers", //详细分页组，可以支持直接跳转到某页
                        "bSort" : true, //是否启动各个字段的排序功能
                        "aaSorting" : [[3, "desc"]], //默认的排序方式，第2列，升序排列
                        "bFilter" : true, //是否启动过滤、搜索功能

                        "oLanguage": { //国际化配置
                            "sProcessing" : "正在获取数据，请稍后...",
                            "sLengthMenu" : "显示 _MENU_ 条",
                            "sZeroRecords" : "没有您要搜索的内容",
                            "sInfo" : "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
                            "sInfoEmpty" : "记录数为0",
                            "sInfoFiltered" : "(全部记录数 _MAX_ 条)",
                            "sInfoPostFix" : "",
                            "sSearch" : "查询",

                            "oPaginate": {
                                "sFirst" : "第一页",
                                "sPrevious" : "上一页",
                                "sNext" : "下一页",
                                "sLast" : "最后一页"
                            }
                        },
                        'sDom': "rtlip",
                        'aoColumnDefs': [{
                            'aTargets': [0, 1, 2, 3]
                        }],

                        "aoColumns" : [{
                            "mDataProp":"checkbox" ,
                            "sDefaultContent":"",
                            'bSortable': false,
                            "fnRender": function(obj) {
                                if (obj.aData.name != '超级用户组' || obj.aData.uuid != '1'){
                                    var sReturn = "<input  type='checkbox' name='check' value='"+obj.aData.uuid+"' style='margin-left:12px'/>";
                                    return sReturn;
                                }
                            }
                        }, {
                            "mDataProp" : "name",
                            "sTitle" : "组/部门名称",
                            "sDefaultContent" : "",
                            "sClass" : "center"
                        }, {
                            "mDataProp" : "createTime",
                            "sTitle" : "创建时间",
                            "sDefaultContent" : "",
                            "sClass" : "center",
                            'bSortable': true
                        }, {
                            "mDataProp" : "updateTime",
                            "sTitle" : "修改时间",
                            "sDefaultContent" : "",
                            "sClass" : "center",
                            'bSortable': true
                        }, {
                            "mDataProp" : "remark",
                            "sTitle" : "备注",
                            "sDefaultContent" : "",
                            "sClass" : "center" ,
                            'bSortable': false
                        }],

                        "bServerSide" : true, //是否启动服务器端数据导入
                        "sAjaxSource" : basePath20170311 + 'group/findGroupListByPagin',
                        //服务器端，数据回调处理
                        "fnServerData" : fn.retrieveData,
                    }
                    table = $(this).dataTable(opt);
                });
            }
        }
        if('init' != tag) {
            table.fnDraw();
        }
    }

    fn.excute = function(){
        this.queryAllOrByCondition('init');
        this.findCurrentMenuOper();
        this.initBinding();
        this.initPlus();
    }

    return fn;
})