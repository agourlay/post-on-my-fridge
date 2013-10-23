var globalCounter = 0;
var globalTimestamp = 0;

$(function() {

	var seriesData = [ [], [], [] ];
	seriesData.forEach(function(series) {
		series.push(  {x: moment().unix(), y: NaN} );
	});
	
	listenStats(seriesData);
	listenFirehose();

	var palette = new Rickshaw.Color.Palette( { scheme: 'colorwheel' } );

	var graph = new Rickshaw.Graph( {
		element: document.getElementById("chart"),
		width: calculateFitWidth(),
		height: calculateFitHeight(),
		renderer: 'multi',
		padding : {
			top : 0.05,
			bottom : 0.05
		},
		stroke: true,
		preserve: true,
		series: [
			{
				color: palette.color(),
				data: seriesData[0],
				renderer: 'bar',
				name: 'Firehose throughput / sec'
			},
			{
				color: palette.color(),
				data: seriesData[1],
				renderer: 'line',
				name: 'Opened requests'
			}, {
				color: palette.color(),
				data: seriesData[2],
				renderer: 'line',
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

	//long polling 1s
	setInterval( function() {
		graph.update();
	}, 1000 );

	// reboot speed counters
	setInterval( function() {
		globalCounter = 0;
	    globalTimestamp = new Date().getTime();
		$('#ssespeed').text(0.0);
	}, 10000 );

});

function listenFirehose(){
	var source = new EventSource("/stream/firehose");
	source.addEventListener('message', function(e) {
		globalCounter = globalCounter + 1;
	}, false);
    
    source.addEventListener('open', function(e) {
		//Alertify.log.success("Listening to firehose stream");
	}, false);

	source.addEventListener('error', function(e) {
	    if (e.readyState == EventSource.CLOSED) {
		    //Alertify.log.error("Firehose stream error");
		}
	}, false);
}

function listenStats(series){
	var source = new EventSource("/stream/stats");
	source.addEventListener('message', function(e) {
		var stats = $.parseJSON(e.data);
		var totalRequests = stats.totalRequests;
		var openRequests = stats.openRequests;
		var maxOpenRequests = stats.maxOpenRequests;
		var totalConnections = stats.totalConnections;
		var openConnections = stats.openConnections;
		var maxOpenConnections = stats.maxOpenConnections;
		var requestTimeouts = stats.requestTimeouts;

		var interval = (new Date().getTime() - globalTimestamp) / 1000;
		var sseSpeed = (globalCounter / interval).toFixed(1);
		
		var xNow = moment().unix();
		series[0].push({x: xNow, y:Math.round(sseSpeed)});
		series[1].push({x: xNow, y:openRequests});
		series[2].push({x: xNow, y:openConnections});

		$('#totalRequests').text(totalRequests);
		$('#openRequests').text(openRequests);
		$('#maxOpenRequests').text(maxOpenRequests);
		$('#totalConnections').text(totalConnections);
		$('#openConnections').text(openConnections);
		$('#maxOpenConnections').text(maxOpenConnections);
		$('#requestTimeouts').text(requestTimeouts);
		$('#uptime').text(moment.duration(stats.uptimeInMilli).humanize());
		$('#ssespeed').text(sseSpeed);
	}, false);
    
    source.addEventListener('open', function(e) {
		//Alertify.log.success("Listening to stats stream");
	}, false);

	source.addEventListener('error', function(e) {
	    if (e.readyState == EventSource.CLOSED) {
		    //Alertify.log.error("Stats stream error");
		}
	}, false);
}

function calculateFitWidth() {
	return $(window).width() - $("#chart").offset().left - 200;
}

function calculateFitHeight() {
	return $(window).height() - $("#chart").offset().top - 70;
}