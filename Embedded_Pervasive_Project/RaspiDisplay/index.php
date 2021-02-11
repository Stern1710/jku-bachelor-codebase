<?php

function getDataPoints($period){

  $servername = "localhost";
  $username = "plantuser";
  $password = "Litec01";
  $dbname = "plantdb";
  
  // Create connection
  $conn = new mysqli($servername, $username, $password, $dbname);
  // Check connection
  if ($conn->connect_error) {
      die("Connection failed: " . $conn->connect_error);
  }
  
  $sql = "SELECT * FROM datasets WHERE readTime >= DATE_SUB(NOW(),INTERVAL 1 " . $period . ")";
  
  $result = $conn->query($sql);
  
  $dataPoints = array();
  
  date_default_timezone_set('Europe/Berlin');
  
  if ($result->num_rows > 0) {
      while($row = $result->fetch_assoc()) {
          
          if ($row['isWatered'] == 0) {
            $dataPoints[] = array("x"=> strtotime($row["readTime"]) * 1000, "y"=> intval($row["temp"]), "lineColor"=> "Red");
          }
          else{
            $dataPoints[] = array("x"=> strtotime($row["readTime"]) * 1000, "y"=> intval($row["temp"]), "lineColor"=> "Green");
          }
      }
  } else {
  }
  
  return $dataPoints;
  
  $conn->close();
}

?>

<!DOCTYPE HTML>
<html>
<head> 
<meta charset="UTF-8"> 

<script>
window.onload = function () {
  setGraph(1);
  console.log("onload");
  document.getElementById("category_faq").onchange=function() {
    var a = document.getElementById("category_faq");
    
    setGraph(a.value);
    
  }
 
  function setGraph(dropDownId){
    if(dropDownId == 1){
      var chart = new CanvasJS.Chart("chartContainer", {
      	animationEnabled: true,
      	title:{
      		text: "Statistics"
      	},
      	axisX:{
      		title: "Time"
      	},
      	axisY:{
      		title: "Temperature",
      	},
      	legend:{
      		cursor: "pointer",
      		dockInsidePlotArea: true
      	},
      	data: [{
          type: "spline",
      		name: "Temperature",
      		markerSize: 0,
      		toolTipContent: "Temperature: {y} &#176;C <br>{x}",
          xValueType: "dateTime",
      		xValueFormatString: "hh:mm:ss TT",
      		showInLegend: true,
      		dataPoints: <?php echo json_encode(getDataPoints("HOUR")); ?>
      	}]
      });
      chart.render();
    }
    else if(dropDownId == 2){
      var chart = new CanvasJS.Chart("chartContainer", {
      	animationEnabled: true,
      	title:{
      		text: "Statistics"
      	},
      	axisX:{
      		title: "Time"
      	},
      	axisY:{
      		title: "Temperature",
      	},
      	legend:{
      		cursor: "pointer",
      		dockInsidePlotArea: true
      	},
      	data: [{
      		type: "spline",
      		name: "Temperature",
      		markerSize: 0,
      		toolTipContent: "Temperature: {y} &#176;C <br>{x}",
          xValueType: "dateTime",
      		xValueFormatString: "D.MMM YYYY hh:mm:ss TT",
      		showInLegend: true,
      		dataPoints: <?php echo json_encode(getDataPoints("DAY")); ?>
      	}]
      });
      chart.render();
    }
    else if(dropDownId == 3){
      var chart = new CanvasJS.Chart("chartContainer", {
      	animationEnabled: true,
      	title:{
      		text: "Statistics"
      	},
      	axisX:{
      		title: "Time"
      	},
      	axisY:{
      		title: "Temperature",
      	},
      	legend:{
      		cursor: "pointer",
      		dockInsidePlotArea: true
      	},
      	data: [{
      		type: "spline",
      		name: "Temperature",
      		markerSize: 0,
      		toolTipContent: "Temperature: {y} &#176;C <br>{x}",
          xValueType: "dateTime",
      		xValueFormatString: "D.MMM YYYY hh:mm:ss TT",
      		showInLegend: true,
      		dataPoints: <?php echo json_encode(getDataPoints("WEEK")); ?>
      	}]
      });
      chart.render();
    }
    else{
      var chart = new CanvasJS.Chart("chartContainer", {
      	animationEnabled: true,
      	title:{
      		text: "Statistics"
      	},
      	axisX:{
      		title: "Time"
      	},
      	axisY:{
      		title: "Temperature",
      	},
      	legend:{
      		cursor: "pointer",
      		dockInsidePlotArea: true
      	},
      	data: [{
      		type: "spline",
      		name: "Temperature",
      		markerSize: 0,
      		toolTipContent: "Temperature: {y} &#176;C <br>{x}",
          xValueType: "dateTime",
      		xValueFormatString: "D.MMM YYYY hh:mm:ss TT",
      		showInLegend: true,
      		dataPoints: <?php echo json_encode(getDataPoints("MONTH")); ?>
      	}]
      });
      chart.render();
    }
  }
}
</script>
</head>
<body>
	 	<div style="padding:10px;">
		 	<label>Choose Option</label>
		 	<select id="category_faq">
	             <option value="1">last hour</option>
	             <option value="2">last 24 hours</option>
	             <option value="3">last week</option>
               <option value="4">last month</option>
      </select>
    </div>
<div id="chartContainer" style="height: 370px; width: 100%;"></div>
<script src="Chart.min.js"></script>
</body>
</html>      