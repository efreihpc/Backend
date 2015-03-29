$(document).ready(function(){
	$("#MonteCarlo").submit(function() { return false; });
	$("#MonteCarlo #Query").click(function(){monteCarloQuery($("#MonteCarlo #SearchField").val());});
});

google.load("visualization", "1", { packages: ["corechart"] });

var timer;

function addCheckStatusJob()
{
  $.getJSON("http://localhost:8080/state");
}

function displayService(jsonData)
{
  $.each(jsonData, function(key, val)
  {
         var datarray = [['day', 'close']];
      listElement = $("<li  class=\"media\"><a class=\"media-left\" href=\"/job.html?identifier=" + val.id + "\"><span class=\"glyphicon glyphicon-tag\" aria-hidden=\"true\"></a><div class=\"media-body\"></span><h4 class=\"media-heading\">ID: " + val.id + "</h4><p>Name: " + val.descriptor.commonName + "</p><div id=\"chart_div\" style=\"width: 900px; height: 500px\"></div>  </div></li>");

      $.each(val.result.storage.storage.result_put, function(key, val)
      {
        datarray.push([key, val]);
      });

      listElement.appendTo("#Services");



            var data = google.visualization.arrayToDataTable(datarray);

            var options = {
                title: 'Monte Carlo Estimation'
            };

            var chart = new google.visualization.LineChart(
                        document.getElementById('chart_div'));
            chart.draw(data, options);
  });
}

function monteCarloQuery(searchTerm)
{
  configuration = {
    stockId: searchTerm,
    OauthToken: ""
  }

  queryService("ba6527deaac9505f6db41b10c6424ee463f4c2df", configuration, displayService);
}

function queryService(ServiceIdentifier, configuration, onFinish)
{
  serviceTransfer = {
    descriptor: {
      identifier: ServiceIdentifier
    },
    configuration: configuration
  }

  doWithService(serviceTransfer, pollService, onFinish);
}

function pollService(serviceUid, onFinish)
{
  timer = setInterval(function() {doOnServiceFinishJsonId(serviceUid, onFinish);}, 2000);
}

function doOnServiceFinishJsonId(serviceUid, finishedFunction)
{
  $.getJSON(
    "http://localhost:8080/service/id/" + serviceUid,
    function(data){ doOnServiceFinishJson(data, finishedFunction);});
}

function doOnServiceFinishJson(jsonData, finishedFunction)
{
  $.each(jsonData, function(key, val)
  {
    if(val.state == 1.0)
      clearInterval(timer);
      finishedFunction(jsonData);
  });
}

function doWithService(serviceTransfer, subject, object)
{
  jQuery.ajax ({
    url: "http://localhost:8080/backend/schedule/",
    type: "POST",
    data: JSON.stringify(serviceTransfer),
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    success: function(responsedata) {doWithServiceJson(responsedata, subject, object);}
  });
}

function doWithServiceJson(jsonData, subject, object)
{
    subject(jsonData.id, object);
}