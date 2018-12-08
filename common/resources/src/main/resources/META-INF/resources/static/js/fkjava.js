/**
 * 
 */
$(function(){
		$(".add-selected").click(function(){
			// 找到勾选了的【未选中角色】
			$(".unselect-roles ul li input:checked").each(function(index,input){
				//取消选中
				$(input).prop("checked",false);
				// 找到input的li
				// 如果两边都要有，那么就在最后加上.clone()，拷贝一份
				//相当于把右边的值移到左边，然后把右边的选中该为未选中
				var li = $(input).parent().parent()//.clone();
				$(".selected-roles ul").append(li);
			})
		})
		
		$(".add-all").click(function(){
			$(".unselect-roles ul li input").each(function(index,input){
				$(input).prop("checked",false);
				var li = $(input).parent().parent();
				$(".selected-roles ul").append(li);
			})
		})
		
		$(".remove-selected").click(function(){
			//找到勾选了的【选中角色】
			$(".selected-roles ul li input:checked").each(function(index,input){
				//取消选中
				$(input).prop("checked",false);
				//选中的移除到左边
				var li = $(input).parent().parent();
				$(".unselect-roles ul").append(li);
			})
		})
		
		$(".remove-all").click(function(){
			$(".selected-roles ul li input").each(function(index,input){
				$(input).prop("checked",false);
				
				var li = $(input).parent().parent();
				$(".unselect-roles ul").append(li);
			})
		})
		
		//在提交的表单的时候，把选中的角色全部勾选上
		//另外，未选中的角色，要全部取消掉
		// 最后，还需要把勾选的input的name属性中的数字，替换成0\1\2这种形式
		$("form").bind("submit",function(){
			//左边的勾取消掉
			$(".selected-roles ul li input").each(function(index,input){
				//把选中的角色全部勾选上
				$(input).prop("checked",true);
				//替换掉name里面的数字
				var name = $(input).attr("name");
				//  /\d+/正则表达式
				name = name.replace(/\d+/,index);
				//最后要设置进去
				$(input).attr("name",name);
			})
			//右边的勾取消掉
			$(".unselect-roles ul li input").each(function(index,input){
				$(input).prop("checked",false);//取消勾选
			})
		})
		
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		// 在所有的AJAX请求发送之前，统一设置请求头
		// 这种方式设置的，只能处理通过jQuery发送的AJAX请求
		if (token && header) {
			$(document).ajaxSend(function(e, xhr, options) {
				xhr.setRequestHeader(header, token);
			});
		}
	})