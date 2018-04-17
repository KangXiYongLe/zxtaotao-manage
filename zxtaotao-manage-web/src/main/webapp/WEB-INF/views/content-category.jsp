<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">
    </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
$(function(){
	$("#contentCategory").tree({
		url : '/rest/content/category',
		animate: true,
		method : "GET",
		onContextMenu: function(e,node){//在右键点击节点的时候触发
            e.preventDefault();//阻止默认事件的执行
            $(this).tree('select',node.target);//选中当前的节点
            $('#contentCategoryMenu').menu('show',{ 
                left: e.pageX,
                top: e.pageY
            });
        },
        onAfterEdit : function(node){//在编辑节点之后触发
        	var _tree = $(this);
        	if(node.id == 0){
        		// 新增节点
        		$.post("/rest/content/category",{parentId:node.parentId,name:node.text},function(data){
        			_tree.tree("update",{
        				target : node.target,
        				id : data.id  //将临时的id修改为正式的id值
        			});
        		});
        	}else{
        		$.ajax({
        			   type: "PUT",
        			   url: "/rest/content/category",
        			   data: {id:node.id,name:node.text},
        			   statusCode: {
        				   204:function(msg){
            				   //$.messager.alert('提示','重命名成功!');
            			   },
            			   500:function(){
            				   $.messager.alert('提示','重命名失败!');
            			   }
        			   }
        			});
        	}
        }
	});
});
function menuHandler(item){
	var tree = $("#contentCategory");
	var node = tree.tree("getSelected");
	if(item.name === "add"){
		tree.tree('append', {//在当前节点下添加子节点
            parent: (node?node.target:null),
            data: [{
                text: '新建分类',
                id : 0,
                parentId : node.id
            }]
        }); 
		var _node = tree.tree('find',0);//查找到新添加的节点
		//选中该节点并且开始编辑
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);//开始编辑一个节点
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){
				$.ajax({
     			   type: "POST",
     			   url: "/rest/content/category",
     			   data : {parentId:node.parentId,id:node.id,"_method":"DELETE"},
     			   statusCode: {
     				   204:function(msg){
     					  tree.tree('remove',node.target);//移除一个节点和它的子节点，'target'参数是该节点的DOM对象。
          			   },
          			   500:function(){
         				   $.messager.alert('提示','删除失败!');
         			   }         			 
     			   }
     			});
			}
		});
	}
}
</script>