'use strict'

$(function () {
	
	$('#selectbox').on('change', function () {
		
		toContoroller();
	});
	function toContoroller(){
		let selectedValue = $('#selectbox option:selected').val();
		if(selectedValue==='人気順'){
		$('#searchFormSortType').val(selectedValue);
			$('#searchForm').submit();
		}else if(selectedValue==='値段が高い順'){
			$('#searchFormSortType').val(selectedValue);
			$('#searchForm').submit();
		}else{
			$('#searchFormSortType').val(selectedValue);
			$('#searchForm').submit();
		}
	
	};
	
	

});