$(document).ready(function(){
	$("#MonteCarlo").submit(function() { return false; });
	// $("#MonteCarlo #Query").click(function(){monteCarloQuery($("#MonteCarlo #SearchField").val());});
  $("#MonteCarlo #Query").click(function(){queryQuandlCodes($("#MonteCarlo #SearchField").val());});
});

google.load("visualization", "1", { packages: ["corechart"] });

var timer = new Array ();;

function addCheckStatusJob()
{
  $.getJSON("http://localhost:8080/state");
}

function displayService(jsonData)
{
  $.each(jsonData, function(key, val)
  {
         var datarray = [['day', 'close']];
      listElement = $("<li id=\"result_"  + val.id + "\"  class=\"media\"><a class=\"media-left\" href=\"/job.html?identifier=" + val.id + "\"><span class=\"glyphicon glyphicon-tag\" aria-hidden=\"true\"></a><div class=\"media-body\"></span><h4 class=\"media-heading\">ID: " + val.id + "</h4><p>Name: " + val.descriptor.commonName + "</p><div id=\"chart_div_" + val.id + "\" style=\"width: 900px; height: 500px\"></div>  </div></li>");

      $.each(val.result.storage.storage.result_put, function(key, val)
      {
        datarray.push([key, val]);
      });

      if ($("#Services #result_"+val.id).length  > 0)
        $("#Services #result_"+val.id).replaceWith(listElement);
      else
        listElement.prependTo("#Services");

      var data = google.visualization.arrayToDataTable(datarray);

      var options = {
          title: 'Monte Carlo Estimation'
      };

      var chart = new google.visualization.LineChart(
      document.getElementById("chart_div_" + val.id));
      chart.draw(data, options);
  });
}

function monteCarloQuery(searchTerm)
{
  configuration = {
    stockId: searchTerm,
    OauthToken: "4e-vC8QFHWFu5zLHA6yu"
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

  var throb = Throbber({ color: "#000000", size: "100"});

  listElement = $("<li id=\"result_"  + serviceUid + "\"  class=\"media\"><a class=\"media-left\" href=\"/job.html?identifier=" + serviceUid + "\"><span class=\"glyphicon glyphicon-tag\" aria-hidden=\"true\"></a><div class=\"media-body\"></span><h4 class=\"media-heading\">ID: " + serviceUid + "</h4><p> Loading </p><div style=\"margin-left:auto; margin-right:auto\" id=\"chart_div_" + serviceUid + "\" style=\"width: 900px; height: 100px\"></div>  </div></li>");

  if ($("#Services #result_" + serviceUid).length > 0)
    $("#Services #result_" + serviceUid).replaceWith(listElement);
  else
    listElement.prependTo("#Services");

  throb.appendTo(document.getElementById("chart_div_" + serviceUid));
  throb.start();

  timer[serviceUid] = setInterval(function() {doOnServiceFinishJsonId(serviceUid, onFinish);}, 2000);
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
    if(val.state == 1)
    {
      clearInterval(timer[val.id]);
      finishedFunction(jsonData);
    }
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

function queryQuandlCodes(searchTerm)
{
    $.getJSON(
    "https://www.quandl.com/api/v1/datasets.json?query=" + searchTerm,
    function(data){ extractQuandlCodes(data, completeSearchField);});
}

function extractQuandlCodes(jsonData, onFinish)
{
  var quandlCodes = new Array();
  $.each(jsonData.docs, function(key, val)
  {
    quandlCodes.push(val.source_code + "/" + val.code);
  });

  onFinish(quandlCodes);
}

function showArray(array)
{
  alert(array);
}

function completeSearchField(array)
{
  $("#MonteCarlo #SearchField").autocomplete(
    {
      source: array,
      select: function(e,ui) {
        monteCarloQuery(ui.item.value);
      }
    }
  );
    $("#MonteCarlo #SearchField").autocomplete( "search");
}