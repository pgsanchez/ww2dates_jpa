
/******************************************************************************/
/*************************   TIMELINE   ***************************************/
var listaEventos = [];
var eventoSeleccionado;
var mouseOverItemId;

// Variables necesarias para el timeline
var timeline;
var container;
var dataSet;
var options = {
  width: '100%',
  height: '20vh',
  maxHeight: '290px'
};


/******************************************************************************/
/**************************   LEAFLET   ***************************************/
var markersListOnMap = []; 	// Listado de marcas del mapa. Es un array con los idTimeline
							// de los eventos que se están mostrando en el mapa




function inicializarVistaIndex(json2){
	
	/* ********** Inicializaciones del mapa ********** */
	
	map = L.map('map').setView([51.505, -0.09], 13);

	L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>',
		maxZoom: 18
	}).addTo(map);

	L.control.scale().addTo(map);

	// Centrar el mapa (Alemania, norte de Munich) y poner zoom inicial
	map.setView(L.latLng(49.410973, 11.425781), maxZoom=5 );	
	
	
	
	
	/* ********** Inicializaciones del Timeline ********** */
	
	// El elemento del html en el que va a ir el timeline
	container = document.getElementById('timeline');
	// Se crea el DataSet del timeline
	dataSet = new vis.DataSet();
	// Se crea el Timeline
	timeline = new vis.Timeline(container, dataSet, options);
	
	eventoSeleccionado = new Object();
	eventoSeleccionado.idTimeline = -1;
	
	mouseOverItemId = -1;
	
	/* OJO: el json tarda en leerse, así que estas funciones de centrar, se ejecutan
	ANTES de que termine de leer el json, por eso la opción focus no funciona; porque
	cuando se llama a la función, el DataSet aun no está disponible. Y por eso, el rango
	de fechas inicial se pone con fechas fijas*/
	timeline.setWindow("08/25/1939", "03/01/1940");
	
	// Eventos del Timeline
	/* Función onRangeChanged del Timeline: se ejecuta cuando se mueve el timeline y cambia
	el rango de fechas visibles. Se borran todas las marcas del mapa (no de listaEventos) y
	se vuelven a dibujar.
	*/
	timeline.on('rangechanged', onRangeChanged);
	
	timeline.on('select', onSelect);
	
	timeline.on('mouseOver', onMouseOver);
	
	pasarDatos(json2);
}


/******************************************************************************/
/* $(function() {
var ficheroJSON = "events.json"; */
function pasarDatos(json2){
	let json = JSON.parse(json2);
	for(var i=0; i<json.length; i++){
			var evento = new Object();
			evento.id = json[i].id;
			evento.name = json[i].name;
			evento.description = json[i].description;
			evento.importance = json[i].importance;
			evento.latitude = json[i].latitude;
			evento.longitude = json[i].longitude;
			evento.imageName = json[i].imageName;
			evento.date = json[i].date;
			// campos para el timeline
			evento.idTimeline = i;
			// campos para el mapMarker
			evento.marker = L.marker([json[i].latitude, json[i].longitude]);
			// Se guarda el evento en la lista de eventos
			listaEventos.push(evento);
			// Se añade el evento al DataSet del timeline
			dataSet.add({id: i, content: json[i].name, title: json[i].description, start: json[i].date});
		};
	//console.log("listaEventos = " + listaEventos.length);
	
};
/******************************************************************************/

function getEventIdFromTimelineId(idTimeline){
	var x = 0;
	listaEventos.forEach((item) => {
		if (item.idTimeline == idTimeline){
			x = item.id;
		}
	});
	return x;
}
/*
Función onRangeChanged del Timeline: se ejecuta cuando se mueve el timeline y cambia
el rango de fechas visibles. Se borran todas las marcas del mapa (no de listaEventos) y
se vuelven a dibujar.
*/
function onRangeChanged(properties){
	clearMarks();
	timelineEventsToMap();
}


function onSelect(properties){
	// Mostrar web con la información
	//console.log("onSelect properties: " + properties.items);
	var x = getEventIdFromTimelineId(properties.items);
	// console.log("onSelect id: " + x);
	if (x > 0)
		document.location.href = 'http://localhost:8080/infoEvent?id='+ x;
		
	//console.log("¿Petición enviada?");
}

//***************************************************************************************

function onMouseOver(properties){

	if (mouseOverItemId == properties.item){
		// seguimos sobre el mismo elementod el timeline. No hacer nada
	} else{
			mouseOverItemId = properties.item;
			console.log("properties.item = " + properties.item);


	// Si había un evento que ya se había marcado previamente, hay que volver a poner
	// su marcador original
	listaEventos.forEach((item, i) => {
		// Si el elemento de listaEventos es el que estába seleccionado --> se borra la capa de ese marcador
	  // y se vuelve a poner su marcador original (que se dibujará luego)
		if (item.idTimeline == eventoSeleccionado.idTimeline){
			map.removeLayer(item.marker);
			item.marker = L.marker([item.latitude, item.longitude]);
			console.log("elemento " + item.name + " vuelve al icono azul");
		}

		// Si el elemento de listaEventos es el que se ha marcado ahora --> se pone su marcador
		// al color resaltado y se copia el elemento al objeto eventoSeleccionado
		if (item.idTimeline == properties.item){
			var greenIcon = new L.Icon({
	  		//iconUrl: 'resources/js/img/marker-icon-2x-green.png',
	  		iconUrl: 'js/img/marker-icon-2x-green.png',
	  		shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
	  		iconSize: [25, 41],
	  		iconAnchor: [12, 41],
	  		popupAnchor: [1, -34],
	  		shadowSize: [41, 41]
			});
			map.removeLayer(item.marker);
			item.marker = L.marker([item.latitude, item.longitude], {icon: greenIcon});

			// Se copia el elemento que se ha marcado al objeto eventoSeleccionado
			Object.assign(eventoSeleccionado, item);
			console.log("eventoSeleccionado.name = " + eventoSeleccionado.name);

		}
	});

	// Y, a continuación, se borran todas las marcas del mapa y se vuelven a Crear
	clearMarks();
	timelineEventsToMap();

	}
	// FIN
}


/******************************************************************************/
/**************************   LEAFLET   ***************************************/

function addMark(lat, lng){

}

function clearMarks(){
	//console.log("clearMarks");
	/*var count = 0;
	markersListOnMap.forEach((item, i) => {
		//console.log("markersListOnMap -> " + item);
		listaEventos.forEach((event, j) => {
			if(event.idTimeline == item){
				// Hay que eliminar "event" del mapa
				map.removeLayer(event.marker);
				count++;
			}
		});

	});
	markersListOnMap = [];
	console.log("ClearMark borrados = " + count);
*/

	// Se borran todas las marcas del mapa
	var count = 0;
	listaEventos.forEach((event, j) => {
			// Hay que eliminar "event" del mapa
			map.removeLayer(event.marker);
			count++;
			markersListOnMap = [];
	});
//	console.log("ClearMark borrados = " + count);
}


/******************************************************************************/
/*****************************   MAIN   ***************************************/

// Aquí ya tengo una variable map y otra timeline

/* Función que obtiene los eventos que se están mostrando en el timeline  y los
	 pone en el mapa, cada uno con un marcador */
function timelineEventsToMap(){
		var idsTimeline = []
		idsTimeline = timeline.getVisibleItems();
		var contador = 0;

		idsTimeline.forEach((item, i) => {
			listaEventos.forEach((event, j) => {
				if(event.idTimeline == item){
					// Hay que mostrar "event" en el mapa
					map.addLayer(event.marker);
					markersListOnMap.push(item);
					contador++;
				}
			});

		});
	//	console.log("timelineEventsToMap contador = " + contador);
		//console.log("markersListOnMap = " + markersListOnMap.length);
}
