<#import "spring.ftl" as spring>
<!DOCTYPE HTML>
<html>
 <head>
  <title>上传文件</title>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
 </head>
 <body>
         </br></br></br>
	     <form action="" id="params">
	       <div class="row">
	            <div class="col-sm-12 col-md-6 col-lg-2">
	                <div class="form-group">
	                    <input id="importFile" name="importFile" type="file" accept="*.csv">
	                </div>
	            </div>
	            <div class="col-sm-12 col-md-6 col-lg-6">
	                <div class="form-group">
	                    <button type="button" class="btn btn-primary btn-sm btn-analyse-clinic">分析诊断</button>
	                    <button type="button" class="btn btn-primary btn-sm btn-analyse-codematch">分析对码</button>
	                    <button type="button" class="btn btn-primary btn-sm btn-analyse-hospital">分析医院</button>
	                </div>
	            </div>
	        </div>
	     </form>
       
    
     
 </body>
 
 <script src="/js/jquery.min.js" type="text/javascript"></script>
 <script src="/js/bootstrap.min.js" type="text/javascript"></script>
 <script>
 
    $(function () {
    
	    $(".btn-analyse-clinic").on('click',function(e){
	        if (!$("#importFile").val()) return  alert("没有上传文件！！！");
	        $.ajax({
	            url: "/upload/csv",
	            type: "POST",
	            data: new FormData($("#params")[0]),
	            enctype: 'multipart/form-data',
	            processData: false,
	            contentType: false,
	            cache: false,
	            success: function(data) {
	                sleep(5000);
                    if(data=="success"){
                        $('#fadeupload').modal('hide');
                        var input = '<input type="hidden" name="filepath" value="' + $("#importFile").val() + '">';
                        var url = "/download/icd";
                        jQuery('<form action="' + url + '" method="' + 'post'+ '">' + input + '</form>').appendTo('body').submit().remove();
                    }
	           	},
	            error: function () {
	                $('#fadeupload').modal('hide');
	                alert("我出错了，去找工程师麻烦！！！");
	            }
	        });
	    });
	    
	    
	    function sleep(numberMillis) { 
			var now = new Date(); 
			var exitTime = now.getTime() + numberMillis; 
			while (true) { 
				now = new Date(); 
				if (now.getTime() > exitTime) 
				return; 
			} 
        }
	    
    });
    
    $(".btn-analyse-codematch").on('click',function(e){
        alert("逗你玩，功能还没做呢！！！");
    });
    $(".btn-analyse-hospital").on('click',function(e){
        alert("逗你玩，功能还没做呢！！！");
    });
    
   
 </script>
</html>