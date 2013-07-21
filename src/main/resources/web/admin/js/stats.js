var globalCounter = 0;
var globalTimestamp = 0;

$(function() {

var seriesData = [ [], []];
seriesData.forEach(function(series) {
	series.push(  {x: moment().unix(), y: NaN} );
});

var palette = new Rickshaw.Color.Palette( { scheme: 'colorwheel' } );

var graph = new Rickshaw.Graph( {
	element: document.getElementById("chart"),
	width: 900,
	height: 450,
	renderer: 'line',
	padding : {top : 0.09},
	stroke: true,
	preserve: true,
	series: [
		{
			color: palette.color(),
			data: seriesData[0],
			name: 'Open requests'
		}, {
			color: palette.color(),
			data: seriesData[1],
			name: 'Open connections'
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
var gauge = initSseGauge();
listenFirehose(gauge);

setInterval( function() {
	updateData(seriesData);
	graph.update();
	globalCounter = 0;
    globalTimestamp = new Date().getTime();
	gauge.set(0);
	$('#ssespeed').text(0);
}, 4000 );
});

function updateData(series) {
	$.ajax({
	        url: "/stats",
	        type: 'GET',
	        dataType: "json",
	        success: function(stats) {
				if (stats !== null && stats !== undefined) {
					$('#updatetime').text(moment().format('HH:mm:ss'));
					$('#uptime').text(moment.duration(stats.uptime.length).humanize());

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
				}
	        },
	        error: function(xhr, ajaxOptions, thrownError) {
				console.log("Error during stats retrieval");
			}
    	});
}

function initSseGauge(){
	var opts = {
	  lines: 12, // The number of lines to draw
	  angle: 0.15, // The length of each line
	  lineWidth: 0.44, // The line thickness
	  pointer: {
	    length: 0.9, // The radius of the inner circle
	    strokeWidth: 0.035, // The rotation offset
	    color: '#000000' // Fill color
	  },
	  limitMax: 'true',   // If true, the pointer will not go past the end of the gauge

	  colorStart: '#6FADCF',   // Colors
	  colorStop: '#8FC0DA',    // just experiment with them
	  strokeColor: '#E0E0E0',   // to see which ones work best for you
	  generateGradient: true
	};
	var target = document.getElementById('sse-gauge'); // your canvas element
	var gauge = new Gauge(target).setOptions(opts); // create sexy gauge!
	gauge.maxValue = 200; // set max gauge value
	gauge.animationSpeed = 32; // set animation speed (32 is default value)
	gauge.set(0); // set actual value
	return gauge;
}

function listenFirehose(gauge){
	var source = new EventSource("/stream/firehose");
	source.addEventListener('message', function(e) {
		globalCounter = globalCounter + 1;
		interval = (new Date().getTime() - globalTimestamp) / 1000;
		var newValue = (globalCounter / interval).toFixed(1);
		gauge.set(newValue);
		$('#ssespeed').text(newValue);
	}, false);
}