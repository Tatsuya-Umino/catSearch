$(function(){
	$(".delete-confirm").click(function(){
		if(window.confirm('削除します。よろしいですか？')){
			return true;
		}	else	{
			return false;
		}
	});
	$(".update-confirm").click(function(){
		if(window.confirm('更新します。よろしいですか？')){
			return true;
		}	else	{
			return false;
		}
	});
});