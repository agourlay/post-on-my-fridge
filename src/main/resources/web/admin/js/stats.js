$(function() {
     
var seriesData = [ [], [], [], [], []];
seriesData.forEach(function(series) {
	series.push(  {x: moment().unix(), y: NaN} );
});

updateData(seriesData);

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
			name: 'Max open request'
		}, {
			color: palette.color(),
			data: seriesData[2],
			name: 'Open connections'
		}, {
			color: palette.color(),
			data: seriesData[3],
			name: 'Max open connections '
		}, {
			color: palette.color(),
			data: seriesData[4],
			name: 'Requests timeout '
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

setInterval( function() {
	updateData(seriesData);
	graph.update();
}, 3000 );
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
					series[1].push({x: xNow, y:maxOpenRequests});
					series[2].push({x: xNow, y:openConnections});
					series[3].push({x: xNow, y:maxOpenConnections});
					series[4].push({x: xNow, y:requestTimeouts});

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