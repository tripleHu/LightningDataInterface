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
<input type="button"  value="生成雷电密度数据" onclick="shengcheng()"></input>
<input type="button"  value="按天生成雷电活动数据" onclick="shengcheng1()"></input>
<input type="button"  value="按时间生成雷电活动数据" onclick="shengcheng2()"></input>
<input type="button"  value="获取2014-08-01年30.5, 107.2数据" onclick="shengcheng3()"></input>
<input type="button"  value="获取2014-08-01年30.0, 106---31.0, 107数据" onclick="shengcheng4()"></input>

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
	  //dataType:"json",
	  cache:true,
	  url:"service/adtd/LightningDensity",
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
function shengcheng1()
{
	var temper1="2014";
	var dt1 = new Date(temper1.replace(/-/g,"/"));

	$.ajax
    ({
	//type:"POST",
	  dataType:"json",
	  cache:true,
	  url:"service/adtd/AreaStatistic",
	  data:{AreaIndex:40,Date:temper1},
	  contentType:"application/json",
	  error: function(XMLHttpRequest, textStatus, errorThrown) {
		  alert("查询失败");
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
           alert(textStatus);
	  },
	  success:function(res){
		  
		alert(res);
	
	  }
	 
});
}
function shengcheng2()
{
	var temper1="2014-08-01"+" "+"0:0:0";
	var dt1 = new Date(temper1.replace(/-/g,"/"));
	var temper2="2014-08-04"+" "+"0:0:0";
	var dt2 = new Date(temper2.replace(/-/g,"/"));
	$.ajax
    ({
	//type:"POST",
	  dataType:"json",
	  cache:false,
	  url:"service/adtd/LightningActiveByTime",
	  data:{StartTime:dt1.getTime(),EndTime:dt2.getTime()},
	  contentType:"application/json",
	  error: function(XMLHttpRequest, textStatus, errorThrown) {
		  alert("查询失败");
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
           alert(textStatus);
	  },
	  success:function(res){
		  
		alert(res.length);
	
	  }
	 
});
}
function shengcheng3()
{
	var temper1="2014-08-01";

	$.ajax
    ({
	//type:"POST",
	  dataType:"json",
	  cache:false,
	  url:"service/adtd/CricleStatistic",
	  data:{CricleLatitude:30.5,CricleLongitude:107.2,Radium:50,Date:temper1},
	  contentType:"application/json",
	  error: function(XMLHttpRequest, textStatus, errorThrown) {
		  alert("查询失败");
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
           alert(textStatus);
	  },
	  success:function(res){
		  
		alert(res.length);
	
	  }
	 
});
}
function shengcheng4()
{
	var temper1="2014-08-01";

	$.ajax
    ({
	//type:"POST",
	  dataType:"json",
	  cache:false,
	  url:"service/adtd/RectangleStatistic",
	  data:{latitudeLower:30.0,latitudeUpper:31.0, longitudeLeft:106.0, longitudeRight:107.0,Date:temper1},
	  contentType:"application/json",
	  error: function(XMLHttpRequest, textStatus, errorThrown) {
		  alert("查询失败");
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
           alert(textStatus);
	  },
	  success:function(res){
		  
		alert(res.length);
	
	  }
	 
});
}
</script>
</html>