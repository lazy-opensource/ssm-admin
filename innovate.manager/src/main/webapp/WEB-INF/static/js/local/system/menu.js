/**
 * Created by laizhiyaun on 2017/3/20.
 * 菜单模块
 */
define(['jquery', 'bootstrap', 'validate', 'bootbox', 'datetimepicker', 'datetimepicker-zh', 'dataTable', 'ColReorderWithResize', 'common', 'treeview'], function ($, $1, $2, $3, $4, $5, $6, $7, $8, $9) {

    var fn = {};

    fn.expandAll = function () {
        $('#menuTree').treeview('expandAll', {
            levels: 5,
            silent: true
        });
    }

    fn.collapseAll = function () {
        $('#menuTree').treeview('collapseAll', {
            silent: true
        });
    }

    fn.cleanSelect = function(){
        var selectNodes = $('#menuTree').treeview('getSelected');
        for (var i in selectNodes){
            $('#menuTree').treeview('unselectNode', [ selectNodes[i], { silent: true } ]);
        }
    }

    fn.findChildNode = function(pid){
        var nodes = $('#menuTree').treeview('getEnabled');
        var childIds = [];
        for (var i in nodes){
            if (nodes[i].pid == pid){
                childIds.push(nodes[i].uuid);
            }
        }
        return childIds.join(',');
    }

    //删除
    fn.del = function () {
        var ids = '';
        var selectNodes = $('#menuTree').treeview('getSelected');

        var selectIds = new Array();
        var childIds = [];
        for (var i in selectNodes) {
            selectIds.push(selectNodes[i].uuid);
            childIds = fn.findChildNode(selectNodes[i].uuid);
            if (childIds instanceof Array){
                for (var j in childIds){
                    selectIds.push(childs[i]);
                }
            }
        }
        //console.info(selectNodes)
        if (selectIds.length == 0) {
            $3.alert('请至少选择一项进行操作!');
            return;
        }
        var is = fn.checkIsSystemMenu(selectIds);
        if (is) {
            $3.alert('不可编辑系统菜单!');
            return;
        }
        $3.confirm('确定删除' + selectNodes.length + '条数据吗?', function (r) {
            if (r) {
                $.post(
                    basePath20170311 + 'menu/del/' + selectIds.join(','),
                    function (_data) {
                        $8.handleWebResult(_data);
                        fn.initMenuTree();
                    }, 'json'
                )
            }
        })
    }

    //添加
    fn.add = function () {
        fn.submitForm('add');
    }

    //编辑
    fn.edit = function () {
        fn.submitForm('edit');
    }

    //提交表单
    fn.submitForm = function (action) {
        var isOk = $('#menuForm').valid();

        if (!isOk) {
            return;
        }
        $('#menuModal').modal('hide');
        if (action == 'add') {
            $.post(
                basePath20170311 + 'menu/add',
                $8.setter({name: '', url: '', remark: '', icon: '', sortCode: '',code:'',status:''}),
                function (_data) {
                    fn.initMenuTree();
                    $8.handleWebResult(_data);
                }, 'json')
        }

        if (action == 'edit') {
            var id = $('#uuid').val();
            $.post(
                basePath20170311 + 'menu/edit',
                $8.setter({uuid: id, name: '', url: '', remark: '', icon: '', sortCode: '',code:'',status:''}),
                function (_data) {
                    $8.initMenuTree();
                    $8.handleWebResult(_data);
                }, 'json'
            )
        }
    }

    //去添加
    fn.toAdd = function () {
        $8.clear(['name', 'url', 'sortCode', 'remark', 'icon']);
        $('#url').attr('disabled', false);
        $('#menuModalLabel').html('新增');
        $('#save').attr('onclick', 'javascript:current.add();');
        $('#menuModal').modal('show', true);
    }

    //去编辑
    fn.toEdit = function () {
        var ids = '';
        var nodes = $('#menuTree').treeview('getSelected');
        var selectNodes = new Array();
        for (var i = 0; i < nodes.length; i++) {
            selectNodes.push(nodes[i].uuid);
        }

        if (selectNodes.length != 1) {
            $3.alert('请选择一项进行操作!');
            return;
        }
        var is = fn.checkIsSystemMenu(selectNodes);
        if (is) {
            $3.alert('不可编辑系统菜单!');
            return;
        }
        $('#uuid').val(selectNodes[0]);

        $.post(
            basePath20170311 + 'menu/toEdit/' + selectNodes[0],
            function (_data) {
                var _data = _data.object;
                $8.setNode({uuid:_data.uuid,name:_data.name , icon:_data.icon, url:_data.url, remark:_data.remark, sortCode:_data.sortCode,code:_data.code,status:_data.status,url:_data.url});
                $('#menuModalLabel').html('编辑');
                $('#menuModal').modal('show', true);
                $('#save').attr('onclick', 'javascript:current.edit();');
            }, 'json'
        )
    }

    //检查是否是系统菜单
    fn.checkIsSystemMenu = function (selectIds) {
        if (selectIds==null || selectIds.length < 1) {
            return;
        }
        var systemMenus = ['0', '1', '2', '3', '4', '5', '6'];
        var is = false;
        for (var i in systemMenus) {
            for (var j in selectIds) {
                if (selectIds[j] == systemMenus[i]) {
                    is = true;
                    break;
                }
            }
        }
        return is;
    }

    //关联父菜单时不可选择自己
    fn.setParentNodeSelected = function (pids, selectIds) {
        var nodes = $('#parentMenuModalBody').treeview('getEnabled');
        for (var i in nodes) {
            for (var j in selectIds) {
                if (selectIds[j] == nodes[i].uuid) {
                    $('#parentMenuModalBody').treeview('disableNode', [nodes[i], {silent: true}]);
                    break;
                }
            }
        }
        return;
    }

    //去关联父菜单
    fn.toContactParentMenu = function () {

        var selects = $('#menuTree').treeview('getSelected');
        if (selects.length == 0) {
            $3.alert('请至少选择一项进行操作!');
            return;
        }

        var pids = new Array();
        var selectIds = new Array();
        for (var i in selects) {
            pids.push(selects[i].pid);
            selectIds.push(selects[i].uuid);
        }
        var is = fn.checkIsSystemMenu(selectIds);
        if (is) {
            $3.alert('不可编辑系统菜单!');
            return;
        }

        $('#uuid').val(selectIds.join(','));
        fn.setParentNodeSelected(pids, selectIds);
        $('#parentMenuModal').modal('show', true);
    }

//获取关联父菜单框已经选择的节点
    fn.findSelectParentMenuIds = function () {
        var nodes = $('#parentMenuModalBody').treeview('getSelected');
        var pids = new Array();
        if (nodes.length == 0){
            //默认加到顶级菜单
            pids.push("0");
        }else{
            for (var i in nodes) {
                pids.push(nodes[i].uuid);
            }
        }
        return pids;
    }

//关联父菜单
    fn.contactParentMenu = function () {
        var pids = fn.findSelectParentMenuIds();
        var ids = $('#uuid').val();

        $.post(
            basePath20170311 + 'menu/contactParentMenu/' + ids +'/' + pids.join(','),
            function (_data) {
                fn.initMenuTree();
                $8.handleWebResult(_data, 'parentMenuModal');
            }
        ), 'json'
    }

//初始化操作
    fn.findCurrentMenuOper = function () {
        var parentMenuId = $('#currentMenuId').val();
        $.post(
            basePath20170311 + 'oper/findOpersByMenuId', {
                menuId: parentMenuId
            },
            function (_data) {
                var temp = '';
                for (var i in _data) {
                    temp = temp + '<button class="btn btn-default" title="' + _data[i].name + '" onclick="javascript:current.' +  _data[i].url + '">' +  _data[i].name + '</button>';
                }
                $('#oper-btn').append(temp);

            }, 'json')

    }

//初始化树
    fn.initMenuTree = function () {
        $.post(
            basePath20170311 + 'menu/findMenuList',
            {isMenu: true},
            function (_data) {
                var data = _data.list;
                var selectMenus = _data.map;

                //初始化主数据
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
                    levels: 5
                });

                //初始化关联菜单数据
                $('#parentMenuModalBody').treeview({
                    data: data,
                    collapseIcon: "glyphicon glyphicon-chevron-down",
                    expandIcon: 'glyphicon glyphicon-chevron-right',
                    onhoverColor: '#DDDDDD',
                    selectedBackColor: '#DDDDDD',
                    backColor: 'white',
                    color:'rgb(0,0,0)',
                    borderColor: '#C0C0C0',
                    levels: 5
                });

                //填充下拉框数据
                $('#parentIdSeach').empty();
                $('#parentIdSeach').append('<option value="">--请选择--</option>');
                for (var i in selectMenus) {
                    var opt = new Option();
                    opt.value = i;
                    opt.text = selectMenus[i];
                    $('#parentIdSeach').append(opt);
                }
            }, 'json')
    }

//初始化表单验证
    fn.initPlus = function () {
        var validateParam = {
            rules: {
                name: {
                    required: true
                },
                sortCode:{
                    required:true,
                    number:true
                }
            },
            messages: {
                name: {
                    required: $.format('菜单名称不能为空!')
                },
                sortCode:{
                    required: $.format('菜单优先级不能为空!'),
                    number: $.format('必须是合法数字!'),
                }
            },
            submitHandler: function (form) {
                return false;
            }
        }
        $('#menuForm').validate(validateParam);

        //日历控件
        $('.form_datetime').datetimepicker({
            language:  'zh-CN',
            format : 'yyyy-mm-dd hh:ii:ss',
            autoclose: true
        });
    }

    //将所有的结果选中状态
    fn.checkSearchResult = function (id) {
        if (!id) {
            return;
        }

        var nodes = $('#menuTree').treeview('getEnabled');

        for (var i in nodes) {
            if (id == nodes[i].uuid) {
                $('#menuTree').treeview('selectNode', [nodes[i], {silent: true}]);
                break;
            }
        }
        return false;
    }

    fn.clearSearch = function(){
        $8.clear(['nameSeach','updateTimeS','updateTimeE','createTimeS','createTimeE','parentIdSeach']);
    }

    //处理条件检索结果
    fn.handleSeachResult = function (data) {
        if (!data) {
            return;
        }
        var nodes = $('#menuTree').treeview('getEnabled');
        if (data.length < 1){
            $3.alert('查无数据!');
            return;
        }
        for (var j in data) {

            fn.checkSearchResult(data[j].uuid);
        }
    }

    fn.seach = function () {
        fn.initMenuTree();
        var conditions = [];

        if ($('#nameSeach').val()) {
            conditions.push({
                "name": "name",
                "value": $('#nameSeach').val()
            });
            conditions.push({
                "name": "name_q",
                "value": $('#nameSeach_q').val()
            });
        }

        if ($('#statusSeach').val()) {
            conditions.push({
                "name": "status",
                "value": $('#statusSeach').val()
            });
            conditions.push({
                "name": "status_q",
                "value": $('#statusSeach_q').val()
            });
        }

        if ($('#parentIdSeach').val()) {
            conditions.push({
                "name": "parentId",
                "value": $('#parentIdSeach').val()
            });
            conditions.push({
                "name": "parentId_q",
                "value": $('#parentIdSeach_q').val()
            });
        }

        if ($('#createTimeS').val()) {
            conditions.push({
                "name": "createTime",
                "value": $('#createTimeS').val()
            });
            conditions.push({
                "name": "createTime_q",
                "value": $('#createTimeS_q').val()
            });
        }
        if ($('#createTimeE').val()) {
            conditions.push({
                "name": "createTime",
                "value": $('#createTimeE').val()
            });
            conditions.push({
                "name": "createTime_q",
                "value": $('#createTimeE_q').val()
            });
        }
        if ($('#createTimeE').val() && $('#createTimeS').val()) {
            conditions.push({
                "name": "createTime",
                "value": $('#createTimeS').val() + '@_date_@' + $('#createTimeE').val()
            });
            conditions.push({
                "name": "createTime_q",
                "value": $('#createTimeS_q').val() + '@_date_@' + $('#createTimeE_q').val()
            });
        }

        if ($('#updateTimeS').val()) {
            conditions.push({
                "name": "updateTime",
                "value": $('#updateTimeS').val()
            });
            conditions.push({
                "name": "updateTime_q",
                "value": $('#updateTimeS_q').val()
            });
        }
        if ($('#updateTimeE').val()) {
            conditions.push({
                "name": "updateTime",
                "value": $('#updateTimeE').val()
            });
            conditions.push({
                "name": "updateTime_q",
                "value": $('#updateTimeE_q').val()
            });
        }
        if ($('#updateTimeE').val() && $('#updateTimeS').val()) {
            conditions.push({
                "name": "updateTime",
                "value": $('#updateTimeS').val() + '@_date_@' + $('#updateTimeE').val()
            });
            conditions.push({
                "name": "updateTime_q",
                "value": $('#updateTimeS_q').val() + '@_date_@' + $('#updateTimeE_q').val()
            });
        }

        $.post(
            basePath20170311 + 'menu/findMenuListByCondition',
            {condition:JSON.stringify(conditions)},
            function(_data){
                fn.handleSeachResult(_data.object);
            },'json'
        )
    }


    fn.excute = function () {
        this.initMenuTree();
        this.initPlus();
        this.findCurrentMenuOper();
    }
    return fn;

})