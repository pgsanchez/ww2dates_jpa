/*
 * Este fichero controla el mapa que aparece en las páginas addEvent.jsp, updateEvent.jsp e infoEvent.jsp
 * El funcionamiento del mapa de ambas páginas es casi el mismo, así que se hace todo desde
 * este fichero.
 * La primera función "miFuncion" es para las páginas addEvent.jsp y updateEvent.jsp
 * La segunda función "infoEventMap" es para la página de infoEvent.jsp
 */
function miFuncion() {
	/* Si estamos abriendo la página de addEvent.jsp, las coordenadas iniciales de latitud/longitud 
	*  serán [0.0, 0.0]. En ese caso, centraremos el mapa en Europa (aunque la marca esté en dichas coordenadas)
	*  Si estamos abriendo la página de editEvent.jsp, las coordenadas iniciales de latitud/longitud
	*  serán distintas de [0.0, 0.0]. En ese caso, se centra el mapa en dichas coordenadas
	*/
	//console.log("mifuncion sin parámetros");
	var map;
	if ((document.getElementById("editLat").value == 0.0) && (document.getElementById("editLng").value == 0.0)){
		map = L.map('map').setView([51.505, -0.09], 13);
	} else {
		map = L.map('map').setView([document.getElementById("editLat").value, document.getElementById("editLng").value], 13);
	}
	


	L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>',
	maxZoom: 18
	}).addTo(map);

	L.control.scale().addTo(map);

	var marker = L.marker([document.getElementById("editLat").value, document.getElementById("editLng").value]);
	map.addLayer(marker);
	
	function onMapClick(e) {
			
	        document.getElementById("editLat").value = e.latlng.lat;
	        document.getElementById("editLng").value = e.latlng.lng;
	        
	        if (marker != null)
	        	map.removeLayer(marker);
	        
	        marker = L.marker([e.latlng.lat, e.latlng.lng]);
	        map.addLayer(marker);
	}
	
	map.on('click', onMapClick);
} // del function miFuncion()


/*
 * Esta es la función que se utiliza para la página de infoEvent.jsp. Solo muestra el mapa y marca
 * la posición que le llega por parámetro. No permite hacer clic.
 */
function infoEventMap(latitude, longitude) {
	/*console.log("mifuncion CON parámetros");
	console.log("latitude = " + latitude);*/
	var map;
	map = L.map('map').setView([latitude, longitude], 13);
	
	L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>',
		maxZoom: 18
		}).addTo(map);

	L.control.scale().addTo(map);

	var marker = L.marker([latitude, longitude]);
	map.addLayer(marker);
}

