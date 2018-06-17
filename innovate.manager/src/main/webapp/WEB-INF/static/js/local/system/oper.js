/**
 * Created by lzy on 2017/3/23.
 * 操作模块
 */
define(['jquery','bootstrap', 'validate','bootbox', 'datetimepicker', 'datetimepicker-zh', 'dataTable','ColReorderWithResize','common','treeview'],function($,$1,$2,$3,$4,$5,$6,$7,$8,$9) {

    var fn = {};
    var table = null;

    //去关联菜单
    fn.toContactMenu = function(){
        fn.initMenuTree();
        var ids = $8.getCheckedIds();
        if (ids.length != 1) {
            $3.alert('请选择一项进行操作！');
            return;
        }
        $('#uuid').val(ids[0]);
        $.post(
            basePath20170311 + 'oper/toContactMenu/' + ids[0],
            function(_data) {
                if (!$8.checkWebResult(_data)){
                    $8.handleWebResult(_data);
                    return;
                }
                fn.setOperMenuNodeSelect(_data.object.menuId);
                $('#contactMenu').attr('onclick', 'javascript:current.contactMenu();');
                $('#contactMenuModal').modal('show', true);
            },
            'json')
    }

    //标记已关联菜单
    fn.setOperMenuNodeSelect = function(pid){
        var nodes = $('#menuTree').treeview('getEnabled');
        for (var i in nodes) {
            if (pid == nodes[i].uuid) {
                $('#menuTree').treeview('selectNode', [nodes[i], {
                    silent: true
                }]);
                break;
            }
        }
        return;
    }

    fn.contactMenu = function(){
        var node = $8.getSelectTreeNodes('menuTree')[0];
        var pid = '';
        if (node){
            pid = node.uuid;
        }
        var id = $('#uuid').val();

        $.post(
            basePath20170311 + 'oper/contactMenu/' + id,
            {menuId:pid},
            function(_data) {
                $8.handleWebResult(_data,'contactMenuModal');
                table.fnDraw();
            },
            'json')
    }

    fn.toAdd = function(){
        $8.clear(['name','status','code','sortCode','url','remark','']);
        $('#operModalLabel').html('新增');
        $('#save').attr('onclick', 'javascript:current.add();');
        $('#operModal').modal('show', true);
    }

    fn.add = function(){
        fn.submitForm('add');
    }

    fn.toEdit = function(){
        $8.clear(['name','status','code','sortCode','url','remark','']);
        var ids = $8.getCheckedIds();

        if (ids.length < 1) {
            $3.alert('请选择一项进行操作！');
            return;
        }
        $('#uuid').val(ids[0]);
        $('#operModalLabel').html('编辑');
        $('#operModal').modal('show', true);
        $('#save').attr('onclick', 'javascript:current.edit();');
        $.post(
            basePath20170311 + 'oper/toEdit/' + ids[0],
            function(_data) {
                if (!$8.checkWebResult(_data)){
                    $8.handleWebResult(_data);
                    return;
                }
                _data = _data.object;
                $8.setNode({name:_data.name,url:_data.url,code:_data.code,sortCode:_data.sortCode,remark:_data.remark,status:_data.status});
            },
            'json')
    }

    fn.edit = function(){
        fn.submitForm('edit');
    }

    //提交表单
    fn.submitForm = function(action){
        var isOk = $('#operForm').valid();
        if (!isOk) {
            return;
        }
        $('#operModal').modal('hide');
        if (action == 'add') {
            $.post(
                basePath20170311 + 'oper/add',
                $8.setter({name:'',url:'',status:'',code:'',sortCode:'',remark:''}),
                function(_data) {
                    $8.handleWebResult(_data);
                    table.fnDraw();
                },
                'json')
        }

        if (action == 'edit') {
            $.post(
                basePath20170311 + 'oper/edit',
                $8.setter({name:'',url:'',status:'',code:'',sortCode:'',remark:'',uuid:''}),
                function(_data) {
                    $8.handleWebResult(_data);
                    table.fnDraw();
                },
                'json')
        }
    }

    //删除
    fn.del = function(){
        var ids = $8.getCheckedIds();
        if (ids.length == 0) {
            $3.alert('至少选择一项进行操作!');
            return;
        }

        $3.confirm('确认删除' + ids.length + '条数据吗?',
            function(r) {
                if (r) {
                    $.post(
                        basePath20170311 + 'oper/del/'+ ids.join(','),
                        function(_data) {
                            $8.handleWebResult(_data, 'operModal');
                            table.fnDraw();
                        },
                        'json')

                }
            })
    }

    //详情
    fn.detail = function(id){
        $.post(
            basePath20170311 + 'oper/detail/' + id,
            function(_data) {
                var data = _data.map;
                var roles = data.roles;
                var groups = data.groups;
                var menu = data.menu;
                var temp1 = '';var temp2 = '';
                $('#belongGroups')[0].style = 'color:red';
                $('#belongRoles')[0].style = 'color:red';
                $('#belongMenu')[0].style = 'color:red';
                $('#belongRoles').html('未关联任何角色');
                $('#belongGroups').html('未关联任何组');
                $('#belongMenu').html('未关联任何菜单');
                for (var i in roles){
                    $('#belongRoles')[0].style = 'color:gray';
                    if (i != 0 && i % 5 == 0){
                        temp1 += '<br>';
                    }
                    temp1 += roles[i].name + ' |';
                }
                for (var i in groups){
                    $('#belongGroups')[0].style = 'color:gray';
                    if (i != 0 && i % 5 == 0){
                        temp2 += '<br>';
                    }
                    temp2 += groups[i].name + ' |';
                }
                if (temp1){
                    $('#belongRoles').html(temp1);
                }
                if (temp2){
                    $('#belongGroups').html(temp2);
                }
                if (menu){
                    $('#belongMenu')[0].style = 'color:gray';
                    $('#belongMenu').html(menu.name);
                }
                $('#detailModal').modal('show', true);
            },'json')
    }

    //初始化菜单数，及下拉框菜单数据
    fn.initMenuTree = function(){
        $.post(
            basePath20170311 + 'menu/findMenuList',
            {isMenu:'true'},
            function(_data) {
                var data = _data.list;
                var selectData = _data.map;
                //初始化关联菜单数据
                $('#menuTree').treeview({
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
                $('#menuIdSeach').empty();
                $('#menuIdSeach').append('<option value="">--请选择--</option>');
                for (var i in selectData) {
                    var opt = new Option();
                    opt.value = i;
                    opt.text = selectData[i];
                    $('#menuIdSeach').append(opt);
                }
            },
            'json')
    }

    //初始化插件
    fn.initPlus = function(){
        //日历控件
        $('.form_datetime').datetimepicker({
            language:  'zh-CN',
            format : 'yyyy-mm-dd hh:ii:ss',
            autoclose: true
        });

        var validataParam = {
            rules: {
                name: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: $.format('该项不能为空!')
                }
            },
            submitHandler: function(form) {
                return false;
            }
        }
            $('#operForm').validate(validataParam);

    }

    //初始化绑定事件
    fn.initBinding = function(){
        //条件查找
        $('.search').click(function() {
            fn.queryAllOrByCondition('seach');
        });

        //全选
        $("#check_all").click(function(e) {
            $('input', table.fnGetNodes()).prop('checked', this.checked);
        });
    }

    //获得当前菜单下的所有操作
    fn.findCurrentMenuOper = function(){
        var parentMenuId = $('#currentMenuId').val();
        $.post(
            basePath20170311 + 'oper/findOpersByMenuId', {
                menuId: parentMenuId
            },
            function(_data) {
                if (_data) {
                    var temp = '';
                   for (var i in _data){
                       temp = temp + '<a class="btn btn-default" title="' + _data[i].name + '" href="javascript:current.' + _data[i].url + '">' + _data[i].name + '</a>';
                   }
                    $('#oper-btn').append(temp);
                }
            },
            'json')
    }

    fn.clearSearch = function(){
        $8.clear(['nameSeach','statusSeach','updateTimeS','updateTimeE','createTimeS','createTimeE','menuIdSeach']);
    }

    //检索入口
    fn.retrieveData = function(sSource, aoData, fnCallback) {
        var condition = [];

        if ($('#nameSeach').val()) {
            condition.push({
                "name": "name",
                "value": $('#nameSeach').val()
            });
            condition.push({
                "name": "name_q",
                "value": $('#nameSeach_q').val()
            });
        }
        if ($('#statusSeach').val()) {
            condition.push({
                "name": "status",
                "value": $('#statusSeach').val()
            });
            condition.push({
                "name": "status_q",
                "value": $('#statusSeach_q').val()
            });
        }

        if ($('#menuIdSeach').val()) {
            condition.push({
                "name": "menuId",
                "value": $('#menuIdSeach').val()
            });
            condition.push({
                "name": "menuId_q",
                "value": $('#menuIdSeach_q').val()
            });
        }

        if ($('#createTimeS').val()) {
            condition.push({
                "name": "createTime",
                "value": $('#createTimeS').val()
            });
            condition.push({
                "name": "createTime_q",
                "value": $('#createTimeS_q').val()
            });
        }
        if ($('#createTimeE').val()) {
            condition.push({
                "name": "createTime",
                "value": $('#createTimeE').val()
            });
            condition.push({
                "name": "createTime_q",
                "value": $('#createTimeE_q').val()
            });
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

        if ($('#updateTimeS').val()) {
            condition.push({
                "name": "updateTime",
                "value": $('#updateTimeS').val()
            });
            condition.push({
                "name": "updateTime_q",
                "value": $('#updateTimeS_q').val()
            });
        }
        if ($('#updateTimeE').val()) {
            condition.push({
                "name": "updateTime",
                "value": $('#updateTimeE').val()
            });
            condition.push({
                "name": "updateTime_q",
                "value": $('#updateTimeE_q').val()
            });
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
            "data": {
                "aoData": JSON.stringify(aoData),
                "condition": JSON.stringify(condition)
            },
            "success": function(_data) {
                fnCallback(_data);
            }
        });
    }

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
                        "aaSorting": [[6, "desc"]], //默认的排序方式，第n列，升序排列
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
                                if (obj.aData.oper != 'sys') {
                                    var sReturn = "<input type='checkbox' name='check' value='" + obj.aData.uuid + "' style='margin:9px;'/>";
                                    return sReturn;
                                }

                            }
                        }, {
                            "mDataProp": "name",
                            "sTitle": "操作名称",
                            "sDefaultContent": "",
                            'bSortable': false,
                            'sClass':'center'
                        }, {
                            "mDataProp": "code",
                            "sTitle": "权限码",
                            "sDefaultContent": "",
                            'bSortable': false,
                        }, {
                            "mDataProp": "url",
                            "sTitle": "统一资源标识符",
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
                                    status = '启用';
                                } else if (obj.aData.status == 2) {
                                    status = '禁用';
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
                                var btn = "javascript:current.detail('" + obj.aData.uuid + "');";
                                return "<button onclick=" + btn + "  class='btn btn-default'>详情</button>";
                            }
                        }],

                        "bServerSide": true, //是否启动服务器端数据导入
                        "sAjaxSource": basePath20170311 + 'oper/findOperListByPagin',
                        //服务器端，数据回调处理
                        "fnServerData": fn.retrieveData
                    }
                    table = $(this).dataTable(opt);
                });
            }
        }
        if (s != 'init'){
            table.fnDraw();
        }
    };

    fn.excute = function(){
        this.queryAllOrByCondition('init');
        this.findCurrentMenuOper();
        this.initMenuTree();
        this.initPlus();
        this.initBinding();
    }

    return fn;
})
