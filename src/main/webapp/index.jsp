<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
<title>防雷技术监管平台2</title>
</head>
<body>
<input type="button"  value="生成数据" onclick="shengcheng()"></input>
</body>
<script type="text/javascript">

/*$.getJSON("http://localhost:7656/LightningDensity/resources/json/2014.json", function(json) { 

	});*/
/*$.getJSON( "http://localhost:7656/LightningDensity/resources/json/2014-radium-5.json" )
.done(function( json ) {
  console.log( "JSON Data: " + json.users[ 3 ].name );
})
.fail(function( jqxhr, textStatus, error ) {
  var err = textStatus + ", " + error;
  console.log( "Request Failed: " + err );
});*/
function shengcheng()
{
	$.ajax
    ({
	//type:"POST",
	  dataType:"json",
	  cache:true,
	  url:"http://172.20.62.132:7656/LightningDataInterface/service/adtd/LightningDensity",
	  data:{year:2014,radium:5},
	  contentType:"application/json",
	  error: function(XMLHttpRequest, textStatus, errorThrown) {
		  alert("查询失败");
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
           alert(textStatus);
	  },
	  success:function(res){
		  
		alert(res.data.length);
	
	  }
	 
});
}
</script>
</html>