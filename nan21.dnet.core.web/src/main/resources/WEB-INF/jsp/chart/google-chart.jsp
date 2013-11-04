<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
  <head>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
    google.load("visualization", "1", {packages:["corechart"]});
    google.setOnLoadCallback(drawChart);
    function drawChart() {
      var data = google.visualization.arrayToDataTable([
		['${xField}', '${yField}'],
		<c:forEach var="d" items="${dataList}">
		['${d[xField]}', ${d[yField]}],	 
		</c:forEach>
      ]);

      var options = {
        title: '${title}'
      };

      var chart = new ${chart}(document.getElementById('chart_div'));
      chart.draw(data, options);
    }
    </script>
  </head>

  <body>  
 
    <div id="chart_div" style="width:800; height:600"></div>
  </body>
</html>