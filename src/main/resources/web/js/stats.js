var globalCounter = 0;
var globalTimestamp = 0;

$(function() {

	var seriesData = [ [], [] ];
	seriesData.forEach(function(series) {
		series.push(  {x: moment().unix(), y: NaN} );
	});

	updateData(seriesData);

	var palette = new Rickshaw.Color.Palette( { scheme: 'colorwheel' } );

	var graph = new Rickshaw.Graph( {
		element: document.getElementById("chart"),
		width: 1220,
		height: 500,
		renderer: 'line',
		padding : {top : 0.09},
		stroke: true,
		preserve: true,
		series: [
			{
				color: palette.color(),
				data: seriesData[0],
				name: 'Opened requests'
			}, {
				color: palette.color(),
				data: seriesData[1],
				name: 'Opened connections'
			}
		]
	} );

	graph.render();

	var slider = new Rickshaw.Graph.RangeSlider( {
		graph: graph,
		element: $('#slider')
	} );

	var hoverDetail = new Rickshaw.Graph.HoverDetail( {
		graph: graph
	} );

	var annotator = new Rickshaw.Graph.Annotate( {
		graph: graph,
		element: document.getElementById('timeline')
	} );

	var legend = new Rickshaw.Graph.Legend( {
		graph: graph,
		element: document.getElementById('legend')

	} );

	var order = new Rickshaw.Graph.Behavior.Series.Order( {
		graph: graph,
		legend: legend
	} );

	var highlighter = new Rickshaw.Graph.Behavior.Series.Highlight( {
		graph: graph,
		legend: legend
	} );

	var ticksTreatment = 'glow';

	var xAxis = new Rickshaw.Graph.Axis.Time( {
		graph: graph,
		ticksTreatment: ticksTreatment
	} );

	xAxis.render();

	var yAxis = new Rickshaw.Graph.Axis.Y( {
		graph: graph,
		tickFormat: Rickshaw.Fixtures.Number.formatKMBT,
		ticksTreatment: ticksTreatment
	} );

	yAxis.render();

	listenFirehose();

	//long polling 1s
	setInterval( function() {
		updateData(seriesData);
		graph.update();
	}, 1000 );

	// reboot counters
	setInterval( function() {
		globalCounter = 0;
	    globalTimestamp = new Date().getTime();
		$('#ssespeed').text(0.0);
	}, 10000 );

});

function updateData(series) {
	$.ajax({
	        url: "/stats",
	        type: 'GET',
	        dataType: "json",
	        success: function(stats) {
				if (stats !== null && stats !== undefined) {
					var totalRequests = stats.totalRequests;
					var openRequests = stats.openRequests;
					var maxOpenRequests = stats.maxOpenRequests;
					var totalConnections = stats.totalConnections;
					var openConnections = stats.openConnections;
					var maxOpenConnections = stats.maxOpenConnections;
					var requestTimeouts = stats.requestTimeouts;

					var xNow = moment().unix();
					series[0].push({x: xNow, y:openRequests});
					series[1].push({x: xNow, y:openConnections});

					$('#totalRequests').text(totalRequests);
					$('#openRequests').text(openRequests);
					$('#maxOpenRequests').text(maxOpenRequests);
					$('#totalConnections').text(totalConnections);
					$('#openConnections').text(openConnections);
					$('#maxOpenConnections').text(maxOpenConnections);
					$('#requestTimeouts').text(requestTimeouts);
					$('#uptime').text(moment.duration(stats.uptime.length).humanize());
				}
	        },
	        error: function(xhr, ajaxOptions, thrownError) {
				Alertify.log.error("Error during stats retrieval");
			}
    	});
}

function listenFirehose(){
	var source = new EventSource("/stream/firehose");
	source.addEventListener('message', function(e) {
		globalCounter = globalCounter + 1;
		interval = (new Date().getTime() - globalTimestamp) / 1000;
		var newValue = (globalCounter / interval).toFixed(1);
		$('#ssespeed').text(newValue);
	}, false);
    
    source.addEventListener('open', function(e) {
		Alertify.log.success("Listening to Streaming service");
	}, false);

	source.addEventListener('error', function(e) {
	    if (e.readyState == EventSource.CLOSED) {
		    Alertify.log.error("Streaming service error");
		}
	}, false);
}