/**
 * 常用的工具方法
 */
define(function(require) {
	"use strict";
	// 用于处理复选框问题
	var arr = new Array();
	/**
	 * @checkBoxAll 全选框id
	 * @checkDivId 复选框区域ID
	 */
	function checkboxAll(flag, checkBoxAll, checkDivId) {
		if (flag) {
			$("#" + checkDivId + " :checkbox").each(function() {
				if ($(this).attr("disabled") != 'disabled') {
					$(this).prop("checked", true);
					var value = $(this).val();
					addArr(value);
				}
			});
		} else {
			$("#" + checkDivId + " :checkbox").each(function() {
				if ($(this).attr("disabled") != 'disabled') {
					$(this).prop("checked", false);
					var value = $(this).val();
					arr = deleteArr(value);
				}
			});
		}
	}

	function addArr(value) {
		var flag_v = true;
		if (arr.length == 0) {
			arr.push(value);
		} else {
			for (var i = 0; i < arr.length; i++) {
				if (value == arr[i]) {
					flag_v = false;
				}
			}
			if (flag_v) {
				arr.push(value);
			}
		}
	}

	function deleteArr(value) {
		for (var i = 0; i < arr.length; i++) {
			if (value == arr[i]) {
				arr.splice(i, 1)
			}
		}
		return arr;
	}

	/**
	 * 单个复选框选中事件
	 * 
	 * @returns
	 */
	function checkboxSingle(flag, value) {
		if (flag) {
			// 向数组中添加 value 值
			addArr(value);
		} else {
			// 向数组中取消 value 值
			deleteArr(value);
		}
	}

	/**
	 * 返回数据
	 * 
	 * @returns Array
	 */
	function getReturnArray() {
		return arr;
	}

	/**
	 * 清空数组
	 * 
	 * @returns
	 */
	function cleanArray() {
		var length = arr.length;
		if (length != null && length > 0) {
			for (var i = 0; i < length; i++) {
				arr.pop();
			}
		}
	}

	/**
	 * 区域area下的所有checkbox 绑定事件
	 * 
	 * @param area_id
	 * @returns
	 */
	function bindSingleCheckbox(area_id) {
		$("#" + area_id + " :checkbox").each(function() {
			if ($(this).attr("disabled") != 'disabled') {
				$(this).click(function() {
					var flag = $(this).prop('checked');
					var value = $(this).val();
					checkboxSingle(flag, value);
				});
			}
		});
	}

	/**
	 * 
	 * @param checkbox_id
	 *            复选框id
	 * @param area_id
	 *            区域area下的所有checkbox
	 * @returns
	 */
	function bindAllCheckbox(checkbox_id, area_id) {
		// 查询页面全选框事件
		$("#" + checkbox_id).click(function() {
			var flag = $(this).prop('checked');
			checkboxAll(flag, checkbox_id, area_id);
		});
	}

	function isExitArray(value) {
		for (var i = 0; i < arr.length; i++) {
			if (value == arr[i])
				return true;
		}
		return false;
	}

	/**
	 * 返回checkbox的选中的id的数组
	 */
	function getCheckboxArray(groupCheckbox) {
		var checkboxArray = new Array();
		for (var i = 0; i < groupCheckbox.length; i++) {
			if (groupCheckbox[i].checked) {
				var id = groupCheckbox[i].id;
				checkboxArray.push(id);
			}
		}
		return checkboxArray;
	}

	return {
		checkboxAll : checkboxAll,
		checkboxSingle : checkboxSingle,
		getReturnArray : getReturnArray,
		cleanArray : cleanArray,
		bindSingleCheckbox : bindSingleCheckbox,
		bindAllCheckbox : bindAllCheckbox,
		isExitArray : isExitArray,
		getCheckboxArray : getCheckboxArray
	};
});