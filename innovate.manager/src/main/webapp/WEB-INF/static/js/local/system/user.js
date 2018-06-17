/**
 * Created by laizhiyuan on 2017/3/13.
 * 用户管理模块js
 */
define(['jquery','bootstrap', 'validate','bootbox', 'datetimepicker', 'datetimepicker-zh', 'dataTable','ColReorderWithResize','common'],function($,$1,$2,$3,$4,$5,$6,$7,$8){

    //对象主体
    var fn = {};
    var table = null;

    //去添加
    fn.toAdd = function(){
        $8.clear(['loginName','email','sort','password','password_confirm','status','remark']);
        $8.setLabel('userModalLabel' , '新增');
        $('#password_div').removeClass('hidden');
        $('#password_confirm_div').removeClass('hidden');
        $('#status_div').addClass('hidden');
        $('#save').attr('onclick', 'javascript:current.add();');
        $('#clearForm').attr('onclick' , "javascript:common.clear(['name','email','password','password_confirm','remark']);");
        $('#userModal').modal('show', true);
    };

    //去编辑
    fn.toEdit = function(){
        var ids = $8.getCheckedIds();
        if (ids.length < 1 || ids.length > 1){
            $3.alert('请选择一项进行操作!');
            return;
        }
        $8.setLabel('userModalLabel' , '编辑');
        $('#uuid').val(ids[0]);
        $.post(
            basePath20170311 + 'user/toEdit/' + ids.join(','),
            function(_data){
                $8.setNode({loginName:_data.loginName , email:_data.email, password:_data.password, remark:_data.remark, password_confirm:_data.password});
                $8.setSelectedByNode('status' ,_data.status);
                $('#save').attr('onclick', 'javascript:current.edit();');
                $('#password_div').addClass('hidden');
                $('#password_confirm_div').addClass('hidden');
                $('#status_div').removeClass('hidden');
                $('#userModal').modal('show' , true);
            },'json'
        )
    };

    //添加
    fn.add = function(){
        fn.submitForm('add');
    };

    //编辑
    fn.edit = function(){
        fn.submitForm('edit');
    };


    //编辑.新增用户表单提交
    fn.submitForm = function(action) {
        var isOk = $('#userForm').valid();
        if (!isOk) {
            return;
        }
        $('#userModal').modal('hide');
        if (action == 'add') {
            $.post(
                basePath20170311 + 'user/add',
                $8.setter({loginName:'',email:'',password:'',remark:''}),
                function (_data) {
                    $8.handleWebResult( _data);
                    table.fnDraw();
                }, 'json'
            )
        }

        if (action == 'edit') {
            var status = $('select[name="status"]')[0].value;
            $.post(
                basePath20170311 + 'user/edit',
                $8.setter({loginName:'',email:'',remark:'',uuid:'',status:status}),
                function (_data) {
                    $8.handleWebResult( _data);
                    table.fnDraw();
                }
            )
        }
    };

    //删除
    fn.del = function(){
        var ids = $8.getCheckedIds();
        if (ids.length < 1){
            $3.alert('请至少选择一项进行操作!');
            return;
        }
        $3.confirm('确认删除' + ids.length + '条数据吗？',function(r){
            if (r){
                $.post(
                    basePath20170311 + 'user/del/' + ids.join(','),
                    function(_data){
                        $8.handleWebResult(_data);
                        table.fnDraw();
                    },'json'
                )
            }
        })

    };

    //去关联用户组
    fn.toContactGroup = function(){
        var ids = $8.getCheckedIds();
        if (ids.length != 1){
            $3.alert('请选择一项进行操作');
            return;
        }
        $('#uuid').val(ids[0]);
        $.post(
            basePath20170311 + 'user/toContactGroup/' + ids[0],
            function(_data){
                if (!$8.checkWebResult(_data)){
                    $8.handleWebResult(_data);
                    return;
                }
                var groupId = _data.object;
                $('input[name="groupId"]:radio').each(function(){
                    if ($(this).val() == groupId){
                        this.checked = true;
                    }
                })
                $('#userGroupModal').modal('show' , true);
            },'json'
        )
    };

    //关联用户组
    fn.contactGroup = function(){
        $.post(
            basePath20170311 + 'user/contactGroup/' + $('#uuid').val(),
            {groupId:$("input[type='radio']:checked").val()},
            function(_data){
                $8.handleWebResult(_data , 'userGroupModal');
                table.fnDraw();
            },'json'
        )
    };

    //去关联角色
    fn.toContactRoles = function(){
        var ids = $8.getCheckedIds();
        if (ids.length < 1 || ids.length > 1){
            $3.alert('请选择一项进行操作！');
            return;
        }

        $('#userCanContactRoles').empty();
        $('#userHaveContactRoles').empty();
        $('#uuid').val(ids[0]);

        $.post(
            basePath20170311 + 'user/findUserById/' + ids[0],
            function(_data){
                var groupId = _data.groupId;
                if (!groupId){
                    $3.alert('请先为该用户关联用户组！');
                    return;
                }

                $.post(
                    basePath20170311 + 'user/toContactRoles/' + ids[0] + "/" + groupId,
                    function(_data2){
                        var canContactRoles = _data2.map.canContactRoles; //可关联角色
                        var haveContactRoles = _data2.map.haveContactRoles; //已经关联角色
                        var temp = '';
                        for (var i in canContactRoles) {
                            temp = temp + '<option value="' + canContactRoles[i].uuid + '">' + canContactRoles[i].name + '</option>';
                        }
                        $('#userCanContactRoles').append(temp);
                        temp = '';
                        for (var i in haveContactRoles) {
                            temp = temp + '<option value="' + haveContactRoles[i].uuid + '">' + haveContactRoles[i].name + '</option>';
                        }

                        $('#userHaveContactRoles').append(temp);
                        $('#userContactRoleModal').modal('show', true);
                    },'json'
                )
            },'json'
        )

    };

    //确认关联角色
    fn.contactRoles = function(){
        var roleIds = [];
        var opts = $('#userHaveContactRoles')[0].options;
        for (var i = 0; i < opts.length; i++) {
             roleIds.push(opts[i].value);
        }
        $.post(
            basePath20170311 + 'user/contactRoles/' + $('#uuid').val(),
            {roleIds:roleIds.join(',')},
            function(_data){
                $8.handleWebResult(_data, 'userContactRoleModal');
                table.fnDraw();
            },'json'
        )
    };

    //详情
    fn.detail = function (userId, groupId) {
        $.post(
           basePath20170311 +  'user/detail/' + userId,
            {groupId: groupId},
            function (_data) {
                $('#belongUserRoles').html('');
                $('#belongUserGroups').html('');
                var roles = _data.map.roles;
                var group = _data.map.group;
                //console.info(_data);

                if (!$8.isEmptyObj(roles)) {
                    var roleName = '';
                    for (var i =0 ;i < roles.length; i++) {
                        if (i > 0){
                            roleName = roleName + roles[i].name + ' | ';
                        }else{
                            roleName = roleName + roles[i].name;
                        }
                    }
                    $('#belongUserRoles')[0].style = 'color:gray';
                    $('#belongUserRoles').html(roleName);
                } else {
                    $('#belongUserRoles')[0].style = 'color:red';
                    $('#belongUserRoles').html('未关联任何角色');
                }

                if (!$8.isEmptyObj(group)) {
                    $('#belongUserGroups')[0].style = 'color:gray';
                    $('#belongUserGroups').html(group.name);
                } else {
                    $('#belongUserGroups')[0].style = 'color:red';
                    $('#belongUserGroups').html('未关联部门');
                }
                $('#userDetailModal').modal('show', true);
            },'json')
    }

    //初始化条件检索下拉框
    fn.setUserStatus = function(){
        $.post(
            basePath20170311 + 'enum/findUserStatusEnum',
            function(_data){
                var node = $('#statusSeach');
                var node2 = $('#status');
                var option , option2 = null;
                for (var i in _data){
                    option = new Option();
                    option2 = new Option();
                    option2.text = i;
                    option2.value = _data[i];
                    option.text = i;
                    option.value = _data[i];
                    node.append(option);
                    node2.append(option2);
                }
            },'json'
        )
    };

    //初始化用户组条件下拉框，以及关联用户组列表数组
    fn.findUserGroups = function(){
        $.post(
            basePath20170311 + 'user/findUserGroups',
            function(_data){
                var list = _data.list;
                var opt = null;
                var opt2 = null;
                var temp = '';
                for (var i =0; i < list.length; i++){
                    if (i != 0 && i % 5 == 0){
                        temp += '<br>';
                    }
                    temp += '<label class="checkbox-inline"><input type="radio" value ="' + list[i].uuid + '" name = "groupId">' + list[i].name + '</label>';
                    opt = new Option();
                    opt.text = list[i].name;
                    opt.value = list[i].uuid;
                    $('#groupIdSeach').append(opt);
                }
                $('#userGroupModalBody').append(temp);
            },'json'
        )
    };

    //获得当前用户菜单下的所有操作
    fn.findCurrentMenuOper = function(){
        var menuId = $('#currentMenuId').val();
        $.post(
            basePath20170311 + 'oper/findOpersByMenuId',
            {menuId:menuId},
            function(_data){
                // console.info(_data);
                var node = $('#oper-btn');
                var temp = '';
                for (var i = 0; i < _data.length; i++){
                    temp += "<a class='btn btn-default' href='javascript:current."
                        + _data[i].url
                        + "' id = '"
                        + _data[i].uuid
                        +"'>"
                        + _data[i].name
                        + "</a>"
                }
                node.append(temp);
            },'json'
        )
    };

    //表格数据渲染
    fn.queryAllOrByCondition = function (s) {
        if (table == null) {
            if ($('.dataTable').length > 0) {
                $('.dataTable').each(function () {
                    var opt = {
                        "bProcessing": true, //DataTables载入数据时，是否显示‘进度’提示
                        "bServerSide": true, //是否启动服务器端数据导入
                        //"bStateSave": true, //是否打开客户端状态记录功能,此功能在ajax刷新纪录的时候不会将个性化设定回复为初始化状态
                        // "bJQueryUI": true, //是否使用 jQury的UI theme
                        "aLengthMenu": [5, 10, 20, 40, 60], //更改显示记录数选项
                        "iDisplayLength": 10, //默认显示的记录数
                        "bAutoWidth": true, //是否自适应宽度
                        //"bScrollInfinite" : false, //是否启动初始化滚动条
                        "bScrollCollapse": true, //是否开启DataTables的高度自适应，当数据条数不够分页数据条数的时候，插件高度是否随数据条数而改变
                        "bPaginate": true, //是否显示（应用）分页器
                        "bInfo": true, //是否显示页脚信息，DataTables插件左下角显示记录数
                        "sPaginationType": "full_numbers", //详细分页组，可以支持直接跳转到某页
                        "bSort": true, //是否启动各个字段的排序功能
                        "aaSorting": [[5, "desc"]], //默认的排序方式，第n列，升序排列
                        "bFilter": true, //是否启动过滤、搜索功能

                        "oLanguage": { //国际化配置
                            "sProcessing": "正在获取数据，请稍后...",
                            "sLengthMenu": "显示 _MENU_ 条",
                            "sZeroRecords": "没有您要搜索的内容",
                            "sInfo": "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
                            "sInfoEmpty": "记录数为0",
                            "sInfoFiltered": "(全部记录数 _MAX_ 条)",
                            "sInfoPostFix": "",
                            "sSearch": "查询",

                            "oPaginate": {
                                "sFirst": "第一页",
                                "sPrevious": "上一页",
                                "sNext": "下一页",
                                "sLast": "最后一页"
                            }
                        },
                        'sDom': "rtlip",
                        "aoColumns": [{
                            "mDataProp": "checkbox",
                            "sDefaultContent": "",
                            'bSortable': false,
                            "fnRender": function (obj) {
                                if (obj.aData.uuid != '1') {
                                    var sReturn = "<input type='checkbox' name='check' value='" + obj.aData.uuid + "' style='margin:9px;'/>";
                                    return sReturn;
                                }

                            }
                        }, {
                            "mDataProp": "loginName",
                            "sTitle": "用户名称",
                            "sDefaultContent": "",
                            'bSortable': false,
                            'sClass':'center'
                        }, {
                            "mDataProp": "email",
                            "sTitle": "电子邮箱",
                            "sDefaultContent": "",
                            'bSortable': false,
                        },{
                            "mDataProp": "status",
                            "sTitle": "状态",
                            "sDefaultContent": "",
                            'bSortable': true,
                            "fnRender": function (obj) {
                                var status = '';
                                if (obj.aData.status == 1) {
                                    status = '激活';
                                } else if (obj.aData.status == 2) {
                                    status = '不合法';
                                } else if (obj.aData.status == 3) {
                                    status = '停用';
                                }
                                return status;
                            }
                        }, {
                            "mDataProp": "createTime",
                            "sTitle": "创建时间",
                            "sDefaultContent": "",
                            'sClass':'center'
                        }, {
                            "mDataProp": "updateTime",
                            "sTitle": "修改时间",
                            "sDefaultContent": "",
                            'sClass':'center'
                        }, {
                            "mDataProp": "remark",
                            "sTitle": "备注",
                            "sDefaultContent": "",
                            'bSortable': false
                        }, {
                            "mDataProp": "oper",
                            "sTitle": "操作",
                            "sDefaultContent": "",
                            'bSortable': false,
                            "fnRender": function (obj) {
                                var btn = "javascript:current.detail('" + obj.aData.uuid + "','" + obj.aData.groupId + "');";
                                return "<button onclick=" + btn + "  class='btn btn-default'>详情</button>";
                            }
                        }],

                        "bServerSide": true, //是否启动服务器端数据导入
                        "sAjaxSource": basePath20170311 + 'user/findUserListByPagin',
                        //服务器端，数据回调处理
                        "fnServerData": fn.excuteQuery
                    }
                    table = $(this).dataTable(opt);
                });
            }
        }
        if (s != 'init'){
            table.fnDraw();
        }
    };

    //数据检索入口
    fn.excuteQuery = function (sSource, aoData, fnCallback) {
        var condition = [];

        if ($('#nameSeach').val()) {
            condition.push({"name": "loginName", "value": $('#nameSeach').val()});
            condition.push({"name": "loginName_q", "value": $('#nameSeach_q').val()});
        }

        if ($('#statusSeach').val()) {
            condition.push({"name": "status", "value": $('#statusSeach').val()});
            condition.push({"name": "status_q", "value": $('#statusSeach_q').val()});
        }

        if ($('#groupIdSeach').val()) {
            condition.push({"name": "groupId", "value": $('#groupIdSeach').val()});
            condition.push({"name": "groupId_q", "value": $('#groupIdSeach_q').val()});
        }


        if ($('#createTimeS').val() && !$('#createTimeE').val() ) {
            condition.push({"name": "createTime", "value": $('#createTimeS').val()});
            condition.push({"name": "createTime_q", "value": $('#createTimeS_q').val()});
        }
        if ($('#createTimeE').val() && !$('#createTimeS').val()) {
            condition.push({"name": "createTime", "value": $('#createTimeE').val()});
            condition.push({"name": "createTime_q", "value": $('#createTimeE_q').val()});
        }
        if ($('#createTimeE').val() && $('#createTimeS').val()) {
            condition.push({
                "name": "createTime",
                "value": $('#createTimeS').val() + '@_date_@' + $('#createTimeE').val()
            });
            condition.push({
                "name": "createTime_q",
                "value": $('#createTimeS_q').val() + '@_date_@' + $('#createTimeE_q').val()
            });
        }

        if ($('#updateTimeS').val() && !$('#updateTimeE').val()) {
            condition.push({"name": "updateTime", "value": $('#updateTimeS').val()});
            condition.push({"name": "updateTime_q", "value": $('#updateTimeS_q').val()});
        }
        if ($('#updateTimeE').val() && !$('#updateTimeS').val()) {
            condition.push({"name": "updateTime", "value": $('#updateTimeE').val()});
            condition.push({"name": "updateTime_q", "value": $('#updateTimeE_q').val()});
        }
        if ($('#updateTimeE').val() && $('#updateTimeS').val()) {
            condition.push({
                "name": "updateTime",
                "value": $('#updateTimeS').val() + '@_date_@' + $('#updateTimeE').val()
            });
            condition.push({
                "name": "updateTime_q",
                "value": $('#updateTimeS_q').val() + '@_date_@' + $('#updateTimeE_q').val()
            });
        }

        $.ajax({
            "dataType": 'json',
            "type": "POST",
            "url": sSource,
            "data": {"aoData": JSON.stringify(aoData), "condition": JSON.stringify(condition)},
            "success": fnCallback
        });
    };

    fn.initValidate = function(){
        var validateParam = {
            rules:{
                name:{
                    required:true,
                    minlength:2
                },
                password:{
                    required:true,
                    minlength:6
                },
                email:{
                    required:true,
                    email:true
                },
                password_confirm:{
                    required:true,
                    equalTo:'#password'
                },
                sort:{
                    required:true
                }

            },
            messages:{
                name:{
                    required:'用户名不能为空',
                    minlength:$.format('用户名不能少于两位')
                },
                password:{
                    required:'密码不能为空',
                    minlength:$.format('密码长度不能少于6位')
                },
                email:{
                    required:'邮箱不能为空',
                    email:$.format('邮箱格式错误，必须如13423803766@163.com')
                },
                password_confirm:{
                    required:$.format('不能为空'),
                    equalTo:$.format('两次密码不一致')
                },
                sort:{
                    required:$.format('不能为空')
                }
            },
            submitHandler:function(form){
                return false;
            }


        };
        $('#userForm').validate(validateParam);
    };

    //添加角色到用户区分全部添加或者选择性添加
    fn.addRoleToUserBeginWork = function (isAll) {
        if (isAll) {
            $('#userCanContactRoles option').each(function () {

                $('#userHaveContactRoles option[value="' + $(this).val() + '"]').remove();
            })

        } else {
            $('#userCanContactRoles option:selected').each(function () {

                $('#userHaveContactRoles option[value="' + $(this).val() + '"]').remove();
            })
        }

    };

    //取消角色关联，区分全部取消或者选择性取消
    fn.removeRoleFromUserBeginWork = function (isAll) {
        if (isAll) {
            $('#userHaveContactRoles option').each(function () {

                $('#userCanContactRoles option[value="' + $(this).val() + '"]').remove();
            })
        } else {
            $('#userHaveContactRoles option:selected').each(function () {

                $('#userCanContactRoles option[value="' + $(this).val() + '"]').remove();
            })
        }
    }

    //初始化事件绑定
    fn.initBinding = function(){

        $('#clearSeach').click(function(){
            $8.clear(['nameSeach','statusSeach','groupIdSeach','createTimeS','createTimeE','updateTimeS','updateTimeE']);
        });

        //移到右边
        $('#addRole').click(function() {

            //获取选中的选项，删除并追加给对方
            fn.addRoleToUserBeginWork();
            $('#userCanContactRoles option:selected').appendTo('#userHaveContactRoles');
        });

        //移到左边
        $('#removeRole').click(function() {

            fn.removeRoleFromUserBeginWork();
            $('#userHaveContactRoles option:selected').appendTo('#userCanContactRoles');
        });
        //全部移到右边
        $('#addAllRoles').click(function() {

            fn.addRoleToUserBeginWork(true);
            //获取全部的选项,删除并追加给对方
            $('#userCanContactRoles option').appendTo('#userHaveContactRoles');
        });
        //全部移到左边
        $('#removeAllRoles').click(function() {

            fn.removeRoleFromUserBeginWork(true);
            $('#userHaveContactRoles option').appendTo('#userCanContactRoles');
        });
        //双击选项
        $('#userCanContactRoles').dblclick(function(){ //绑定双击事件
            //获取全部的选项,删除并追加给对方
            fn.addRoleToUserBeginWork();
            $("option:selected",this).appendTo('#userHaveContactRoles'); //追加给对方
        });
        //双击选项
        $('#userHaveContactRoles').dblclick(function(){
            fn.removeRoleFromUserBeginWork();
            $("option:selected",this).appendTo('#userCanContactRoles');
        });


        //条件查找
        $('.search').click(function(){
            fn.queryAllOrByCondition('seach');
        });


        //全选
        $("#check_all").click(function(e) {
            $('input', table.fnGetNodes()).prop('checked', this.checked);
        });

        //日历控件
        $('.form_datetime').datetimepicker({
            language:  'zh-CN',
            format : 'yyyy-mm-dd hh:ii:ss',
            autoclose: true
        });
    };

    //处理结果
    fn.handleResult = function (_data) {
        table.fnDraw();
        if (_data) {
            $('#userModal').modal('hide');
            $('#messageModalBody').html('成功操作' + _data + '条数据');
            $('#messageModal').modal('show', true);
        } else {
            $('#userModal').modal('hide');
            $('#messageModalBody').html(' 操作失败!');
            $('#messageModal').modal('show', true);
        }
    };

    //入口主函数
    fn.excute = function(){
        this.queryAllOrByCondition('init');
        this.initValidate();
        this.initBinding();
        this.findUserGroups();
        this.findCurrentMenuOper();
        this.setUserStatus();
    }

    return fn;

})

