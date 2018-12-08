var loadURL = contextPath + "/human-resources/department";
var removeURL = contextPath + "/human-resources/department";
var moveURL = contextPath + "/human-resources/department/move";

var added = false;// 限制只能每次添加一个菜单
var addHoverDom = function(treeId, treeNode) {
	// 找到节点的span
	var sObj = $("#" + treeNode.tId + "_span");
	// 
	if (added // 已经添加一个子菜单，不要再显示添加按钮
			|| treeNode.editNameFlag // 判断是否正在编辑名字
			|| $("#addBtn_" + treeNode.tId).length > 0// 判断是否有添加按钮
	) {
		// 在编辑名字、有添加按钮都不需要再增加新的自定义按钮
		return;
	}
	// 自定义按钮，其实也是一个span
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
			+ "' title='添加部门' onfocus='this.blur();'></span>";
	// 把自定义的按钮的HTML，放到节点的span之后
	sObj.after(addStr);
	// 给按钮绑定事件
	var btn = $("#addBtn_" + treeNode.tId);
	if (btn)
		btn.bind("click", function() {
			added = true;
			// 删除自定义按钮
			removeHoverDom(treeId, treeNode);
			// 找到已有的tree
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			// treeNode : 添加新的节点到treeNode里面
			// id : 如果为null则自动增加一个、pId : 上级节点的id、name : 显示的名称
			var nodes = zTree.addNodes(treeNode, {
				name : "新部门"
			});
			// 选中节点，因为只是添加了一个，所以nodes里面只有一个！
			zTree.selectNode(nodes[0], false, true);

			return false;
		});
};
// treeId表示树的顶层id, node是选中的节点
var showToForm = function(treeId, node) {

	resetForm();// 在选中之前，先要清空表单

	var id = node.id;
	var name = node.name;
	var url = node.url;
	var type = node.type;
	var roles = node.roles;

	$(".form-container #id").val(id);
	$(".form-container #inputName").val(name);

	if (node.manager && node.manager.user) {
		var userId = node.manager.user.id;
		$(".form-container #selectManager").val(userId);
	}

	// 处理上级菜单
	var parentNode = node.getParentNode();
	if (parentNode) {
		var parentId = parentNode.id;
		var parentName = parentNode.name;

		$(".form-container #parentId").val(parentId);
		// span、div可以用text、html函数设置内容
		$(".form-container #parentName").text(parentName);
	} else {
		$(".form-container #parentId").val("");
		$(".form-container #parentName").text("");
	}
};

var dropNode = function(event, treeId, treeNodes, targetNode, moveType) {
	var requestData = new Object();
	// 要移动的节点的id
	requestData.id = treeNodes[0].id;
	if (!requestData.id) {
		// 没有node的ID，表示新增的节点，不需要到服务器移动
		return;
	}
	// 要把节点移动到哪个目标位置
	if (targetNode) {
		requestData.targetId = targetNode.id;
	} else {
		// 没有目标节点
		requestData.targetId = "";
	}
	// inner、prev、next
	requestData.moveType = moveType;

	$.ajax({
		url : moveURL,
		method : "POST",
		dataType : "json",
		data : requestData,
		success : function(data, status, xhr) {
			// 正常的时候，什么都处理
		},
		error : function(data, status, xhr) {
			alert(data.responseJSON.message);
		}
	});
};

// 显示删除按钮
var showRemoveBtn = function(treeId, treeNode) {
	// 没有下级菜单，可以删除
	return treeNode.children == 0;
};

// 执行删除的操作
var removeNode = function(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	// false 表示不要触发回调
	zTree.removeNode(treeNode, false);
};

var beforeRemoveNode = function(treeId, treeNode) {
	$.ajax({
		url : removeURL + "/" + treeNode.id,
		method : "DELETE",
		dataType : "json",
		success : function(data, status, xhr) {
			if (data.code === 1) {
				// 删除成功，把节点从页面移除
				removeNode(treeId, treeNode);
			}
		},
		error : function(data, status, xhr) {
			alert(data.responseJSON.message);
		}
	});

	return false;
};

var setting = {
	async : {
		// 激活异步请求
		enable : true,
		// 异步请求的URL，默认POST方式发送请求
		url : loadURL,
		// 使用GET方式发送请求
		type : "GET",
		// 要求返回JSON，数据类型参考jQuery的dataType
		dataType : "JSON"
	},
	view : {
		// 当鼠标移动在节点上的时候，增加自定义的按钮
		addHoverDom : addHoverDom,
		// 当鼠标离开节点的时候，删除自定义按钮
		removeHoverDom : removeHoverDom,
		// 禁止多选
		selectedMulti : false
	},
	edit : {
		enable : true,
		// 禁止在菜单树中直接修改名字，而是在点击以后在表单里面来修改
		showRenameBtn : false,
		drag : {
			// 禁止复制
			isCopy : false,
			// 允许拖动
			isMove : true
		},
		showRemoveBtn : showRemoveBtn
	},
	callback : {
		onSelected : showToForm,
		onDrop : dropNode,
		beforeRemove : beforeRemoveNode
	}
};

function removeHoverDom(treeId, treeNode) {
	$("#addBtn_" + treeNode.tId).unbind().remove();
};

// 两个地方调用此函数：点击重置按钮的时候(.reset-button)、在选中节点之前（showToForm）
var resetForm = function() {
	$(".form-container #id").val("");
	$(".form-container #inputName").val("");
	$(".form-container #selectManager").val("");

	$(".form-container #parentId").val("");
	$(".form-container #parentName").text("");

	$(".remove-all").click();

};

$(document).ready(function() {
	$.fn.zTree.init($("#departmentTree"), setting);

	$(".reset-button").click(resetForm);

	// 处理部门经理选择的自动完成
	$('#selectManager').autocomplete({
		serviceUrl : contextPath + '/identity/user',
		dataType : "json",// 返回JSON
		onSelect : function(suggestion) {
			// 当选中某个选项的时候要执行的回调，需要把用户的ID存储到表单里面
			$("#managerUserId").val( suggestion.user.id );
		}
	});
});