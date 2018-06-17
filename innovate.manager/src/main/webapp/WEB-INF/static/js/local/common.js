/**
 * Created by lzy on 2017/3/11.
 * 视为工具类，将需要在初始化时总是加载的函数放到init中
 */

define(['jquery'], function ($) {

    return {

        //清空函数
        clear: function (nodes) {
            if (!nodes instanceof Array) {
                return;
            }

            for (var i = 0; i < nodes.length; i++) {
                $('#' + nodes[i]).val('');
            }
        },

        setLabel: function (id, text) {
            $('#' + id).html(text);
        },

        setter: function (nodes) {
            if (!nodes instanceof Object) {
                return;
            }

            for (var i in nodes) {
                if (nodes[i] == ''){
                    nodes[i] = $('#' + i).val();
                }
            }
            return nodes;
        },

        setNode: function (obj) {
            if (!obj instanceof Object) {
                return;
            }

            for (var i in obj) {
                $('#' + i).val(obj[i]);
            }
            return;
        },

        isEmptyObj:function(obj){
            for (var i in obj){
                return false;
            }
            return true;
        },

        handleWebResult: function (data , modal) {
            if (data.state == 'success') {
                $('#messageModalBody').html(data.message);
            } else {
                $('#messageModalBody').html(data.message);
            }
            if (modal){
                $('#' + modal).modal('hide');
            }
            $('#messageModal').modal('show', true);
        },

        getSelectTreeNodes:function(nodeId){
            return $('#' + nodeId).treeview('getSelected');
        },

        checkWebResult:function(data){
            if (data.state == 'success'){
                return true;
            }
            return false;
        },

        setSelectedByNode: function (n, v) {
            if (!v || !n) {
                return;
            }
            var options = $('select[name="' + n + '"]')[0].options;
            $(options).each(function () {
                if (this.value == v) {
                    this.selected = true;
                    return;
                }
            })
        },

        getCheckedIds: function () {
            var ids = [];

            $('input[name="check"]:checkbox').each(function () {
                if ($(this).is(":checked")) {
                    ids.push($(this).val());
                }
            });

            return ids;
        },
        test: function () {
            alert('hello test succefully');
        },

        init: function () {
            //TODO init work
            //console.info('hello world');
        },
    }
})
