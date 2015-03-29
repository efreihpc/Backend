$(document).ready(function(){
	$("#MonteCarlo").submit(function() { return false; });
	$("#MonteCarlo #Query").click(function(){monteCarloQuery($("#MonteCarlo #SearchField").val());});
});

function addCheckStatusJob()
{
  $.getJSON("http://localhost:8080/state");
}

function displayService(jsonData)
{
  $.each(jsonData, function(key, val)
  {
      $("<p>" + val.id + "</p>").appendTo("#Services");
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
  setInterval(function() {doOnServiceFinishJsonId(serviceUid, onFinish);}, 5000);
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