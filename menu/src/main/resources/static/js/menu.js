var added = false;//限制只能每次添加一个
var addHoverDom = function(treeId,treeNode){
	var sObj = $("#" + treeNode.tId + "_span");
	if (added  //已经添加了子菜单，不要再显示添加按钮
			||
			treeNode.editNameFlag //判断是否正在编辑名字
			|| $("#addBtn_"+treeNode.tId).length > 0 //判断是否有添加按钮
			){
		//在编辑名字，有添加按钮都不需要再增加新的自定义按钮
		return;
	} 
	// 自定义按钮，其实也是一个span
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='add node' onfocus='this.blur();'></span>";
	// 把自定义的按钮的HTML，放到节点的span之后
	sObj.after(addStr);
	// 给按钮绑定事件
	var btn = $("#addBtn_"+treeNode.tId);
	if (btn) btn.bind("click", function(){
		
		//绑定每次只能添加1次
		added = true;
		//用完之后要删除自定义按钮，否则还是可以添加的
		removeHoverDom(treeId,treeNode);
		//找到已有的Tree
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		// treeNode : 添加新的节点到treeNode里面
		//id：如果为null，则自动增加1个
		//pid:上级节点的名称
		//name:显示的名称
		var nodes = zTree.addNodes(treeNode, {name:"新菜单"});
		//选择节点，因为只是添加一个，所以nodes里面只有1个
		zTree.selectNode(nodes[0],false,true);
		// 当点击添加按钮的时候，执行的代码
		return false;
	});
};
//treeId表示树的顶层id, node是选中的节点
var showToForm = function(treeId,node){
	//解决bug，在选中之前，先要情况表单
	resetForm();
	var id = node.id;
	var name = node.name;
	var url = node.url;
	var type = node.type;
	var roles = node.roles;
	$(".form-container #id").val(id);
	$(".form-container #inputName").val(name);
	$(".form-container #inputURL").val(url);
	
	// name="type"
	// input[name='type'][value='" + type + "']" : 先找到input，然后匹配name属性，并且匹配value属性
	$(".form-container input[name='type'][value='" + type + "']").prop("checked",true);
	
	//处理上级菜单
	var parentNode = node.getParentNode();
		if( parentNode ){
			var parentId = parentNode.id;
			var parentName = parentNode.name;
			
			$(".form-container #parentId").val(parentId);
			// span、div可以用text、html函数设置内容
			$(".form-container #parentName").text(parentName);	
		}else{
			$(".form-container #parentId").val("");
			$(".form-container #parentName").text("");	
		}
		
		//解决当点击节点树的时候，对应的角色也附带过去
		//如果节点有角色的，当点击的时候，要把角色赋过去右边
		if(node.roles){
			$(".remove-all").click();
			//选中菜单关联的角色
			//JQuery里面调用找到的对象的方法，如果全部对象执行相同的操作，不需要一个一个来
			// 此时直接调用prop相当于会调用每个元素的prop方法
			$(".unselect-roles ul li input").prop("checked",false);
			for(var i = 0;i<node.roles.length;i++){
				var role = node.roles[i];
				$(".unselect-roles ul li input[value='"+ role.id + "']").prop("checked",true);
			}
			// 把所有选中的角色添加到【左边的框】里面
			$(".add-selected").click();//点击按钮
			
		}
}

var dropNode = function(event, treeId, treeNodes, targetNode, moveType){
	var requestData = new Object();
	//要移动的节点的id
	requestData.id = treeNodes[0].id;
	
	//必须加上这个判断
	if(!requestData.id){
		//没有node的id，表示新增的节点，不需要移动到服务器
		return;
	}
	
	//在这里要加上判断，如果节点移动在外面区域的时候，此时会没有targetId，也就是没有目标id，会报错。
	//所以当没有targetId直接放在下一级菜单的后面
	if(targetNode){
		//要把节点移到哪个目标位置
		requestData.targetId = targetNode.id;
	}else{
		requestData.targetId ="";
	}
	
	//inner(中间) perv(前面)  next(后面)
	requestData.moveType = moveType;
	$.ajax({
		url: moveURL,
		method: "POST",
		dataType: "json",
		data: requestData,
		success: function(data,status,xhr){
			//正常的时候，什么都处理
		},
		error: function(data,status,xhr){
			alert(data.responseJSON.message);
		}
	})
} 
//显示删除按钮,只有没有下级菜单的，才可以删除
var showRemoveBtn = function(treeId,treeNode){
	//没有下级菜单的，可以删除
	return treeNode.children == 0;
}
//执行删除的操作
var removeNode = function(treeId,treeNode){
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	// false 表示不要触发回调
	zTree.removeNode(treeNode,false);
}

var beforeRemoveNode = function(treeId,treeNode){
	// 无论如何，都返回false，避免zTree自己把节点删除
	// 应该通过AJAX把服务器里面的数据删除成功以后，手动调用API来删除节点！
	$.ajax({
		url: removeURL + "/" + treeNode.id,
		method: "DELETE",
		dataType: "JSON",
		success: function(data,status,xhr){
			// ==  两个等号会进行类型转换，比如 1 == "1"  返回true
			// === 三个等号不会进行类型转换，比如 1 === "1" 返回false
			// 在error回调中要加上responseJSON获取服务器返回的JSON
			// 在success回调，则不需要responseJSON
			if(data.code == 1){
				//删除成功，从页面上把节点移除
				removeNode(treeId,treeNode);
			}
		},
		error: function(data,status,xhr){
			alert(data.responseJSON.message);
		}
	})
	//返回false，页面上点击删除的时候，就删除不掉，必须通过ajax发送请求到服务器才可以删除。
	return false;
}
var setting = {
		async: {
			//激活异步请求
			enable: true,
			//异步请求的URL，默认POST方式发送请求
			url: loadURL,
			// 使用GET方式发送请求
			type: "GET",
			//要求放回JSON，数据类型参考JQuery的dataType
			dataType: "JSON"
		},
		view: {
			//当鼠标移动在节点上的时候，增加自定义的按钮
			addHoverDom: addHoverDom,
			//当鼠标离开节点的时候，删除自定义的按钮
			removeHoverDom: removeHoverDom,
			//禁止多选
			selectedMulti: false 
		},
		edit: {
			enable: true,
			//禁止在菜单树里面直接修改名字，而且在点击以后再表单里面修改
			showRenameBtn: false,
			drag: {
				// 禁止复制
				isCopy: false,
				// 允许拖动
				isMove: true
			},
			//显示删除按钮
			showRemoveBtn: showRemoveBtn  
		},
		callback:{
			onSelected:showToForm,
			onDrop: dropNode,
			beforeRemove: beforeRemoveNode
		}
};


//使用var声明的函数，系统在运行的时候会把var放到最上面，但是还未赋值
//如果使用function直接声明函数，那么会在所有执行语句执行之前，先把函数分配空间
//内存分配顺序： function -> var -> let
//var removeHoverDom = function(treeId, treeNode){
function removeHoverDom(treeId, treeNode) {    //有坑---》因为在下面定义，必须使用function先分配空间
	//删除自定义的按钮
	$("#addBtn_"+treeNode.tId).unbind().remove();
};
//两个地方调用这里的函数，点击重置按钮的时候，(.reset-button)、在选中节点之前（showToForm）
var resetForm = function(){
	$(".form-container #id").val("");
	$(".form-container #inputName").val("");
	$(".form-container #inputURL").val("");
	$(".form-container input[name='type']").prop("checked",false);
	
	$(".form-container #parentId").val("");
	$(".form-container #parentName").text("");
	
	$(".remove-all").click();
}
$(document).ready(function(){
	$.fn.zTree.init($("#treeDemo"), setting);
	//重置按钮的设置
	$(".reset-button").click(resetForm);
});
